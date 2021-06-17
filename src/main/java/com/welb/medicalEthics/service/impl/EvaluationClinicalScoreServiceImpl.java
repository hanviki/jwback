package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.dto.EvaluationClinicalScoreDto;
import com.welb.medicalEthics.dto.ScoreDto;
import com.welb.medicalEthics.entity.EvaluationClinical;
import com.welb.medicalEthics.entity.EvaluationClinicalScore;
import com.welb.medicalEthics.mapper.EvaluationClinicalMapper;
import com.welb.medicalEthics.mapper.EvaluationClinicalScoreMapper;
import com.welb.medicalEthics.service.EvaluationClinicalScoreService;
import com.welb.medicalEthics.service.EvaluationClinicalService;
import com.welb.sysBase.util.Constant;
import com.welb.util.DateUtil;
import com.welb.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.welb.sysBase.util.Constant.*;

/**
 * @description: 测试
 * @author: luox
 * @date： 2020/11/19
 */

@Service
public class EvaluationClinicalScoreServiceImpl implements EvaluationClinicalScoreService {

    Logger logger = LoggerFactory.getLogger(EvaluationClinicalScoreServiceImpl.class);

    @Autowired
    private EvaluationClinicalScoreMapper scoreMapper;

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationClinicalMapper evaluationClinicalMapper;

    /**
     * 插入单条空数据
     *
     * @param evaluationClinicalScore
     */
    @Override
    public void insertScore(EvaluationClinicalScore evaluationClinicalScore) {
        scoreMapper.insertScore(evaluationClinicalScore);
    }

    /**
     * 生成空的分数条目
     *
     * @param userId
     */
    @Transactional
    @Override
    public Boolean generateFormItem(String userId) {
        //检查是否创建过表单
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        List<EvaluationClinicalScore> list = scoreMapper.list(params);
        if (!list.isEmpty() && Constant.EVALUATION_CLINICAL_ITEM_ID.length == list.size()) {
            //已经创建过,而且数据正常
            logger.info("已经创建过,而且数据正常");
            return true;
        }
        //开始创建
//        String year = DateUtil.getYear();
        for (int i = 0; i < Constant.EVALUATION_CLINICAL_ITEM_ID.length; i++) {
            EvaluationClinicalScore data = new EvaluationClinicalScore();
            data.setUserId(userId);
            data.setStep(0);
            data.setYear(CURRENT_YEAR);
            data.setItem(Constant.EVALUATION_CLINICAL_ITEM_ID[i]);
            try {
                insertScore(data);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        logger.info("已经重新生成临床表单");
        return true;
    }

    @Override
    public void update(EvaluationClinicalScore score) {

    }

    /**
     * 查询用户分数条目列表
     *
     * @return
     */
    @Override
    public List<EvaluationClinicalScore> getFormData(Map<String, String> params) {
        return scoreMapper.selectByParams(params);
    }

    /**
     * 批量提交分数
     *
     * @param scores
     */
    @Transactional
    @Override
    public void batchUpdate(List<EvaluationClinicalScore> scores) {
        scoreMapper.batchUpdate(scores);
    }

    /**
     * updateScore 评分操作
     *
     * @param scores   scores
     * @param roleCode roleCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void score(List<EvaluationClinicalScore> scores, String roleCode, int step, String userName) {
        String userId = scores.get(0).getUserId();
//        String year = DateUtil.getYear();
        String year = CURRENT_YEAR;
        //增加操作人和时间
        this.addSubmitMsg(scores, userName, step);
        EvaluationClinical evaluationClinical = clinicalService.selectByUserId(userId);
        switch (roleCode) {
            //普通用户的自我评分
            case Constant.MEDICAL_ETHICS_ORDINARY_USER:
                scores.forEach(s -> {
                    s.setStep(1);
                });
                scoreMapper.batchUpdate(scores);
                ScoreDto calculate = calculate(userId, year);
                evaluationClinical.setStep(3);
                evaluationClinical.setLevel(calculate.getLevel());
                evaluationClinical.setScore(calculate.getScore());
                clinicalService.updateById(evaluationClinical);
                break;
            //科室主任评分
            case Constant.MEDICAL_ETHICS_ORDINARY_DIRECTOR:
                scores.forEach(s -> {
                    s.setStep(2);
                });
                scoreMapper.batchUpdate(scores);
                ScoreDto calculate2 = calculate(userId, year);
                evaluationClinical.setStep(4);
                evaluationClinical.setLevel(calculate2.getLevel());
                evaluationClinical.setScore(calculate2.getScore());
                clinicalService.updateById(evaluationClinical);
                break;
            //打分书记评分
            case MEDICAL_ETHICS_SCORING_SECRETARY:
                scores.forEach(s -> {
                    s.setStep(3);
                });
                scoreMapper.batchUpdate(scores);
                evaluationClinical.setStep(5);
                ScoreDto calculate3 = calculate(userId, year);
                evaluationClinical.setLevel(calculate3.getLevel());
                evaluationClinical.setScore(calculate3.getScore());
                clinicalService.updateById(evaluationClinical);
                break;
            default: {
                throw new RuntimeException("用户类型错误");
            }
        }
    }

    @Override
    public void reScore(EvaluationClinicalScoreDto dto, String userName) {
        //科室主任的重新评分
        List<EvaluationClinicalScore> scores = dto.getScoreList();
        String roleType = dto.getRoleType();
        String userId = scores.get(0).getUserId();
        EvaluationClinical evaluationClinical = clinicalService.selectByUserId(userId);
        scoreMapper.batchUpdate(scores);
        ScoreDto calculate = calculate(userId, CURRENT_YEAR);
        scores.forEach(s -> {
            if (ROLE_ID_DIRECTOR.equals(roleType)) {
                s.setHeadSubmitName(userName);
                s.setHeadSubmitTime(DateUtils.getNowTime());
            } else if (ROLE_ID_SCORE_DIC.equals(roleType)) {
                s.setBranchSubmitName(userName);
                s.setHeadSubmitTime(DateUtils.getNowTime());
            }
        });
        evaluationClinical.setLevel(calculate.getLevel());
        evaluationClinical.setScore(calculate.getScore());
        evaluationClinicalMapper.updateById(evaluationClinical);
        scoreMapper.batchUpdate(scores);
    }

    private void addSubmitMsg(List<EvaluationClinicalScore> scores, String userName, int step) {
        for (EvaluationClinicalScore evaluationClinicalScore : scores) {
            switch (step) {
                case SELF_SUBMIT:
                    //增加自我评分提交信息
                    evaluationClinicalScore.setSelfSubmitName(userName);
                    evaluationClinicalScore.setSelfSubmitTime(DateUtils.getNowTime());
                    break;
                case DIRECTOR_SUBMIT:
                    //增加主任评分提交信息
                    evaluationClinicalScore.setHeadSubmitName(userName);
                    evaluationClinicalScore.setHeadSubmitTime(DateUtils.getNowTime());
                    break;
                case SECRETARY_SUBMIT:
                    //增加书记评分提交信息
                    evaluationClinicalScore.setBranchSubmitName(userName);
                    evaluationClinicalScore.setBranchSubmitTime(DateUtils.getNowTime());
                    break;
            }
        }
    }

    /**
     * list 得分记录
     *
     * @param params params
     * @return {@link List<EvaluationClinicalScore>}
     */
    @Override
    public List<EvaluationClinicalScore> list(Map<String, String> params) {
        return scoreMapper.list(params);
    }

    /**
     * checkScore 检查分数是够合法,书记提交时候检查
     *
     * @return {@link Boolean}
     */
    @Override
    public ScoreDto calculate(String userId, String year) {
        Map<String, String> params = new HashMap<>();
        ScoreDto score = new ScoreDto();
        params.put("year", year);
        params.put("userId", userId);
        int level = 0;
        int totalSelfScore = 0;
        int totalHeadScore = 0;
        int totalBranchScore = 0;
        //数据库里面的分数
        List<EvaluationClinicalScore> resList = list(params);
        for (EvaluationClinicalScore s : resList) {
            if (s.getSelfScore() != null) {
                totalSelfScore += s.getSelfScore();
            }
            if (s.getHeadScore() != null) {
                totalHeadScore += s.getHeadScore();
            }
            if (s.getBranchScore() != null) {
                totalBranchScore += s.getBranchScore();
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        Double r1 =  Double.parseDouble(df.format(Constant.SCORE_PERCENT_SELF * totalSelfScore));
        Double r2 =  Double.parseDouble(df.format(Constant.SCORE_PERCENT_BRANCH * totalHeadScore));
        Double r3 =  Double.parseDouble(df.format( Constant.SCORE_PERCENT_HEAD * totalBranchScore));
        Double totalScore = Double.parseDouble(df.format(r1+r2+r3)) ;
        if (totalScore <= 100 && totalScore >= Constant.SCORE_LEVEL1) {
            level = Constant.LEVEL1;
        } else if (totalScore < Constant.SCORE_LEVEL1 && totalScore >= Constant.SCORE_LEVEL2) {
            level = Constant.LEVEL2;
        } else if (totalScore < Constant.SCORE_LEVEL2 && totalScore >= Constant.SCORE_LEVEL3) {
            level = Constant.LEVEL3;
        } else if (totalScore < Constant.SCORE_LEVEL3 && totalScore >= 0) {
            level = Constant.LEVEL4;
        } else {
            level = Constant.LEVEL0;
        }
        score.setScore(totalScore.toString());
        score.setLevel(level);
        score.setTotalSelfScore(totalSelfScore);
        score.setTotalHeadScore(totalHeadScore);
        score.setTotalBranchScore(totalBranchScore);
        return score;
    }

    @Override
    public ScoreDto calculateScore(List<EvaluationClinicalScore> scoreList) {
        ScoreDto score = new ScoreDto();
        int level = 0;
        int totalSelfScore = 0;
        int totalHeadScore = 0;
        int totalBranchScore = 0;
        //数据库里面的分数
        for (EvaluationClinicalScore s : scoreList) {
            if (s.getSelfScore() != null) {
                totalSelfScore += s.getSelfScore();
            }
            if (s.getHeadScore() != null) {
                totalHeadScore += s.getHeadScore();
            }
            if (s.getBranchScore() != null) {
                totalBranchScore += s.getBranchScore();
            }
        }
        DecimalFormat df = new DecimalFormat("#.##");
        Double r1 =  Double.parseDouble(df.format(Constant.SCORE_PERCENT_SELF * totalSelfScore));
        Double r2 =  Double.parseDouble(df.format(Constant.SCORE_PERCENT_BRANCH * totalHeadScore));
        Double r3 =  Double.parseDouble(df.format( Constant.SCORE_PERCENT_HEAD * totalBranchScore));
        Double totalScore = Double.parseDouble(df.format(r1+r2+r3)) ;
        if (totalScore <= 100 && totalScore >= Constant.SCORE_LEVEL1) {
            level = Constant.LEVEL1;
        } else if (totalScore < Constant.SCORE_LEVEL1 && totalScore >= Constant.SCORE_LEVEL2) {
            level = Constant.LEVEL2;
        } else if (totalScore < Constant.SCORE_LEVEL2 && totalScore >= Constant.SCORE_LEVEL3) {
            level = Constant.LEVEL3;
        } else if (totalScore < Constant.SCORE_LEVEL3 && totalScore >= 0) {
            level = Constant.LEVEL4;
        } else {
            level = Constant.LEVEL0;
        }
        score.setScore(totalScore.toString());
        score.setLevel(level);
        score.setTotalSelfScore(totalSelfScore);
        score.setTotalHeadScore(totalHeadScore);
        score.setTotalBranchScore(totalBranchScore);
        return score;
    }

    @Override
    public void updateScoreInfo(String userId, String year) {
        ScoreDto dto = calculate(userId, year);
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        params.put("year", year);
        EvaluationClinical clinical = clinicalService.list(params).get(0);
        clinical.setScore(dto.getScore());
        clinical.setLevel(dto.getLevel());
        evaluationClinicalMapper.updateById(clinical);
    }

    /**
     * checkScore 检查分值是够正常
     *
     * @param scoreList scoreList
     * @return {@link Boolean}
     */
    @Override
    public Boolean checkScore(List<EvaluationClinicalScore> scoreList) {
        for (EvaluationClinicalScore s : scoreList) {
            if (s.getSelfScore() != null) {
                if (s.getSelfScore() < 0 || s.getSelfScore() > s.getMaxScore()) {
                    return false;
                }
            }
            if (s.getHeadScore() != null) {
                if (s.getHeadScore() < 0 || s.getHeadScore() > s.getMaxScore()) {
                    return false;
                }
            }
            if (s.getBranchScore() != null) {
                if (s.getBranchScore() < 0 || s.getBranchScore() > s.getMaxScore()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void deleteByUserId(String userId) {
        scoreMapper.deleteByUserId(userId);
    }

    @Override
    public void updateStepByUserId(int step, String userId) {
        scoreMapper.updateStepByUserId(step, userId);
    }
}
