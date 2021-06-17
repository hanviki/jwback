package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.entity.Rater;
import com.welb.personnel_check.mapper.RaterMapper;
import com.welb.personnel_check.service.IRaterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: RaterServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 人事部评分人员业务层接口的实现类
 * @date 2019/9/310:59
 */
@Service
@Transactional
public class RaterServiceImpl implements IRaterService {
    @Resource
    RaterMapper raterMapper;
    @Override
    public int deleteByPrimaryKey(String ratercode) {
        return raterMapper.deleteByPrimaryKey(ratercode);
    }

    @Override
    public int insertSelective(Rater rater) {
        String maxCode = raterMapper.selectMaxCode();
        if (maxCode==null){
            rater.setRatercode("100");
        }else {
            int i = Integer.parseInt(maxCode)+1;
            String ratercode = String.valueOf(i);
            rater.setRatercode(ratercode);

        }
        return raterMapper.insertSelective(rater);
    }

    @Override
    public Rater selectByPrimaryKey(String ratercode) {
        return raterMapper.selectByPrimaryKey(ratercode);
    }

    @Override
    public int updateByPrimaryKeySelective(Rater rater) {
        return raterMapper.updateByPrimaryKeySelective(rater);
    }

    @Override
    public List<Rater> selectAllRater(Rater rater) {
        return raterMapper.selectAllRater(rater);
    }

    @Override
    public List<Rater> selectList() {
        return raterMapper.selectList();
    }

    @Override
    public Rater selectByMoneyCard(String moneycard) {
        return raterMapper.selectByMoneyCard(moneycard);
    }

    @Override
    public int deleteRaterByScorringCode(String scorringcode) {
        return raterMapper.deleteRaterByScorringCode(scorringcode);
    }

    @Override
    public List<Rater> selectDepartList() {
        return raterMapper.selectDepartList();
    }

    @Override
    public Rater selectRaterByDept(String department) {
        return raterMapper.selectRaterByDept(department);
    }
}
