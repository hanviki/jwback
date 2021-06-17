package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.entity.DeptComplete;
import com.welb.personnel_check.mapper.DeptCompleteMapper;
import com.welb.personnel_check.service.IDeptCompleteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DeptCompleteServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 部门完成情况业务层接口的实现类
 * @date 2019/8/2315:15
 */
@Service
@Transactional
public class DeptCompleteServiceImpl implements IDeptCompleteService {
    @Resource
    DeptCompleteMapper deptCompleteMapper;
    @Override
    public int deleteByPrimaryKey(String completecode) {
        return deptCompleteMapper.deleteByPrimaryKey(completecode);
    }

    @Override
    public int insertSelective(DeptComplete deptComplete) {
        String maxCode = deptCompleteMapper.selectMaxCode();
        if (maxCode==null){
            deptComplete.setCompletecode("1");
        }else {
            int i = Integer.parseInt(maxCode);
            i++;
            String code = String.valueOf(i);
            deptComplete.setCompletecode(code);
        }

        return deptCompleteMapper.insertSelective(deptComplete);
    }

    @Override
    public DeptComplete selectByPrimaryKey(String completecode) {
        return deptCompleteMapper.selectByPrimaryKey(completecode);
    }

    @Override
    public int updateByPrimaryKeySelective(DeptComplete deptComplete) {
        return deptCompleteMapper.updateByPrimaryKeySelective(deptComplete);
    }

    @Override
    public List<DeptComplete> selectList(DeptComplete deptComplete) {
        return deptCompleteMapper.selectList(deptComplete);
    }

    @Override
    public List<DeptComplete> selectAllDeptComplete(String year,String month,String  deptname,int pageNum,int pageSize) {
        return deptCompleteMapper.selectAllDeptComplete(year, month, deptname, pageNum, pageSize);
    }

    @Override
    public DeptComplete selectSingleDept(DeptComplete deptComplete) {
        return deptCompleteMapper.selectSingleDept(deptComplete);
    }

    @Override
    public DeptComplete selectByDepart(DeptComplete deptComplete) {
        return deptCompleteMapper.selectByDepart(deptComplete);
    }

    @Override
    public int deleteByYearAndMonth(String year,String month) {
        return deptCompleteMapper.deleteByYearAndMonth(year, month);
    }

    @Override
    public List<DeptComplete> getNoImportExcelAndPdf(DeptComplete deptComplete) {
        return deptCompleteMapper.getNoImportExcelAndPdf(deptComplete);
    }

}
