package com.welb.sysBase.mapper;

import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.entity.EvaluationDepartment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/3
 */

@Mapper
@Component
public interface BranchDepartmentMapper {

    @Insert(" <script> insert into evaluation_branch_department ( id , department_id , branch_id ) VALUES ( #{id}, #{departmentId}, #{branchId} ) </script>")
    void insert(BranchDepartment branchDepartment);

    String sql_deleteByParams = "<script>" +
            " delete from evaluation_branch_department" +
            " <where> "
            + "<if test='departmentId !=null '> and department_id = #{departmentId} </if>"
            + "<if test='branchId!=null '> and branch_id = #{branchId} </if>"
            + "<if test='id !=null '> and id = #{id} </if>"
            + "</where>"
            + "</script>";

    String sql_deleteByDepartmentId = "<script>" +
            " delete from evaluation_branch_department" +
            " where department_id = #{departmentId} "
            + "</script> ";

    @Delete(sql_deleteByParams)
    void deleteByParams(Map<String, String> params);

    @Delete(sql_deleteByDepartmentId)
    void deleteByDepartmentId(String departmentId);

    @Select(" <script>  select id ,department_id,branch_id from  evaluation_branch_department where department_id = #{departmentId} </script>")
    BranchDepartment selectByDepartmentId(Integer departmentId);

    @Select(" <script> select id , department_id , branch_id from evaluation_branch_department where branch_id = #{branchId}</script> ")
    List<BranchDepartment> selectByBranchId(Integer branchId);


}
