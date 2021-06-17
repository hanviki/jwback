package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.service.IDeptCheckService;
import com.welb.personnel_check.entity.DeptCheck;
import com.welb.personnel_check.mapper.DeptCheckMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DeptCheckServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/1316:40
 */
@Service
@Transactional
public class DeptCheckServiceImpl implements IDeptCheckService {
    @Resource
    DeptCheckMapper deptCheckMapper;
    @Override
    public int insertSelective(DeptCheck deptCheck) {
        String maxCode = deptCheckMapper.selectMaxCode();
        if (maxCode==null){
            deptCheck.setDeptcheckcode("100");
        }else {
            int count = Integer.parseInt(maxCode);
            count++;
            String deptCheckCode = String.valueOf(count);
            deptCheck.setDeptcheckcode(deptCheckCode);
        }
        return deptCheckMapper.insertSelective(deptCheck);
    }

    @Override
    public int updateByPrimaryKeySelective(DeptCheck deptCheck) {
        return deptCheckMapper.updateByPrimaryKeySelective(deptCheck);
    }

    @Override
    public DeptCheck selectDeptCheckByYear(String year,String moneyCard) {
        return deptCheckMapper.selectDeptCheckByYear(year,moneyCard);
    }

    @Override
    public DeptCheck selectDeptCheckByYearAndMoneyCard(String year, String moneycard) {
        return deptCheckMapper.selectDeptCheckByYearAndMoneyCard(year, moneycard);
    }

    @Override
    public DeptCheck selectDeptCheckByYearAndDepart(String year, String depart) {
        return deptCheckMapper.selectDeptCheckByYearAndDepart(year, depart);
    }

    @Override
    public List<DeptCheck> selectList(DeptCheck deptCheck) {
        return deptCheckMapper.selectList(deptCheck);
    }

}
