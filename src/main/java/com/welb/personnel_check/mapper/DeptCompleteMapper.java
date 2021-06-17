package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.DeptComplete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptCompleteMapper {
    int deleteByPrimaryKey(String completecode);

    int insertSelective(DeptComplete deptComplete);

    DeptComplete selectByPrimaryKey(String completecode);

    int updateByPrimaryKeySelective(DeptComplete deptComplete);

    String selectMaxCode();

    List<DeptComplete>selectList(DeptComplete deptComplete);

    List<DeptComplete>selectAllDeptComplete(@Param("year")String year,@Param("month")String month,@Param("deptname")String  deptname,
                                            @Param("pageNum")int pageNum,@Param("pageSize")int pageSize);

    DeptComplete selectSingleDept(DeptComplete deptComplete);

    DeptComplete selectByDepart(DeptComplete deptComplete);

    int deleteByYearAndMonth(@Param("year")String year,@Param("month")String month);

    List<DeptComplete>getNoImportExcelAndPdf(DeptComplete deptComplete);

}
