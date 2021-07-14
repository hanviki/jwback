package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ScoreFlow;
import com.welb.organization_check.mapper.ScoreFlowMapper;
import com.welb.organization_check.service.IScoreFlowService;
import com.welb.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ScoreFlowServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 当前上一季度评分人给被评分人打分的业务层逻辑接口的实现类
 * @date 2019/6/516:54
 */
@Service
@Transactional
public class ScoreFlowServiceImpl implements IScoreFlowService {
    @Resource
    ScoreFlowMapper flowMapper;

    @Override
    public List<ScoreFlow> selectByScoreFlow(String mserialno, String scorringcode) {
        return flowMapper.selectByScoreFlow(mserialno, scorringcode);
    }

    @Override
    public Double getScoreByType(String mserialno, String scoretype) {
        return flowMapper.getScoreByType(mserialno, scoretype);
    }

    @Override
    public int getScoreByTypeCount(String mserialno, String scoretype) {
        return flowMapper.getScoreByTypeCount(mserialno, scoretype);
    }

    @Override
    public int updateByPrimaryKeySelective(ScoreFlow record) {
        return flowMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public  int insertSelective(ScoreFlow record) {//主键自增长
        String serialNo = StringUtil.getGuid();
        record.setSerialno(serialNo);
        return flowMapper.insertSelective(record);
    }

    @Override
    public List<ScoreFlow> selectScoreAllByScoredCode(String scoredcode, String year, String month) {
        return flowMapper.selectScoreAllByScoredCode(scoredcode, year, month);
    }

    @Override
    public Double selectMaxScoreByScoredCode(String scoredcode, String year, String month) {
        return flowMapper.selectMaxScoreByScoredCode(scoredcode, year, month);
    }

    @Override
    public Double selectMinScoreByScoredCode(String scoredcode, String year, String month) {
        return flowMapper.selectMinScoreByScoredCode(scoredcode,year,month);
    }

    @Override
    public List<ScoreFlow> selectFlowByScorredCode(String scorredcode) {
        return flowMapper.selectFlowByScorredCode(scorredcode);
    }

    @Override
    public List<ScoreFlow> selectFlowByScorringCode(String scorringcode) {
        return flowMapper.selectFlowByScorringCode(scorringcode);
    }

    @Override
    public List<ScoreFlow> getSingleTotalScoreAll(String mserialno, String scoretype, String dutytype, Integer orderid) {
        return flowMapper.getSingleTotalScoreAll(mserialno, scoretype, dutytype, orderid);
    }

    @Override
    public int deleteByPrimaryKey(String serialno) {
        return flowMapper.deleteByPrimaryKey(serialno);
    }

    @Override
    public List<ScoreFlow> selectScoreByCodeAndType(ScoreFlow scoreFlow) {
        return flowMapper.selectScoreByCodeAndType(scoreFlow);
    }

    @Override
    public int batchDelete(List<String> scorringSerialnos) {
        return flowMapper.batchDelete(scorringSerialnos);
    }

    @Override
    public int batchUpdate(List<ScoreFlow> list) {
        return flowMapper.batchUpdate(list);
    }

    @Override
    public int batchInsert(List<ScoreFlow> list) {
        return flowMapper.batchInsert(list);
    }

    @Override
    public ScoreFlow selectFlowByMserialNoAndScorringCode(String mserialno, String scorringcode) {
        return flowMapper.selectFlowByMserialNoAndScorringCode(mserialno, scorringcode);
    }

    @Override
    public Double getTypeAvgScore(String mserialno, String scoretype) {
        return flowMapper.getTypeAvgScore(mserialno,scoretype);
    }

    @Override
    public int getTotalCount(String mserialno, String scorringcode) {
        return flowMapper.getTotalCount(mserialno, scorringcode);
    }

    @Override
    public List<ScoreFlow> findFlowsByCode(String usercode) {
        return flowMapper.findFlowsByCode(usercode);
    }

    @Override
    public List<ScoreFlow> selectByScoreFlowType(String mserialno,String scoreType) {
        return flowMapper.selectByScoreFlowType(mserialno, scoreType);
    }
}
