//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.welb.organization_check.service_impl;
import com.welb.organization_check.entity.ScoreDetail;
import com.welb.organization_check.mapper.ScoreDetailMapper;
import com.welb.organization_check.service.IScoreDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ScoreDetailService implements IScoreDetailService {
    @Resource
    private ScoreDetailMapper scoreDetailMapper;
    @Override
    public ScoreDetail selectDetailBySerialNo(String dutycode, String fserialno) {
        return scoreDetailMapper.selectDetailBySerialNo(dutycode, fserialno);
    }

    @Override
    public int insertSelective(ScoreDetail scoredetail) {
        return scoreDetailMapper.insertSelective(scoredetail);
    }

    @Override
    public int updateByPrimaryKeySelective(ScoreDetail scoredetail) {
        return scoreDetailMapper.updateByPrimaryKeySelective(scoredetail);
    }

    @Override
    public Double getTotalScoreByType(String mserialno, String scoretype, String dutytype) {
        return scoreDetailMapper.getTotalScoreByType(mserialno, scoretype, dutytype);
    }

    @Override
    public Double getSingleTotalScoreByType(String mserialno, String scoretype, String dutytype, Integer orderid) {
        return scoreDetailMapper.getSingleTotalScoreByType(mserialno, scoretype, dutytype, orderid);
    }

    @Override
    public int getCountByType(String mserialno, String scoretype, String dutytype, Integer orderid) {
        return scoreDetailMapper.getCountByType(mserialno, scoretype, dutytype, orderid);
    }

    @Override
    public List<ScoreDetail> getSingleTotalScore(String mserialno, String scoretype, String dutytype, Integer orderid) {
        return scoreDetailMapper.getSingleTotalScore(mserialno, scoretype, dutytype, orderid);
    }

    @Override
    public List<ScoreDetail> selectDetailByFSerialNo(String fserialno) {
        return scoreDetailMapper.selectDetailByFSerialNo(fserialno);
    }

    @Override
    public int deleteByPrimaryKey(String serialno) {
        return scoreDetailMapper.deleteByPrimaryKey(serialno);
    }

    @Override
    public int batchDelete(List<String> detailSerialNos) {
        return scoreDetailMapper.batchDelete(detailSerialNos);
    }

    @Override
    public List<ScoreDetail> selectSerialNoByFSerialNo(String fserialno) {
        return scoreDetailMapper.selectSerialNoByFSerialNo(fserialno);
    }

    @Override
    public int batchInset(List<ScoreDetail> list) {
        return scoreDetailMapper.batchInset(list);
    }

    @Override
    public int batchUpdate(List<ScoreDetail> list) {
        return scoreDetailMapper.batchUpdate(list);
    }

    @Override
    public List<ScoreDetail> batchSelectDetailBySerialNo(List<ScoreDetail> list) {
        return scoreDetailMapper.batchSelectDetailBySerialNo(list);
    }

}
