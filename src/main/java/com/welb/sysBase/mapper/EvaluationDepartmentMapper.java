package com.welb.sysBase.mapper;

import com.welb.sysBase.entity.EvaluationDepartment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/3
 */

@Mapper
@Component
public interface EvaluationDepartmentMapper {

    String allColumns = " id , department_name , director_user_id, department_order ,secretary_user_id ";
    String tableName = " evaluation_department ";

    String sql_insert = " <script>  insert into " + tableName + "(" + allColumns + " ) " + " VALUES " + "(" +
            "  #{id}, " +
            " #{departmentName}, " +
            " #{directorUserId}, " +
            " #{departmentOrder}, " +
            " #{secretaryUserId} " +
            " ) </script> ";

    String sql_update = " <script> update" + tableName
            + " set "
            + " department_name = #{departmentName}, "
            + " director_user_id = #{directorUserId}, "
            + " department_order = #{departmentOrder}, "
            + " secretary_user_id = #{secretaryUserId} "
            + " WHERE id = #{id} "
            + " </script> ";

    String sql_delete = "<script>  delete from " + tableName + " where id = #{id} </script> ";

    String sql_selectById = "<script> select" + allColumns + " from " + tableName + " WHERE id = #{id} </script> ";

    String sql_list = " <script> select " +
            " u.u_name as directorName, " +
            " u1.u_name as secretaryName, " +
            " d.id, " +
            " d.director_user_id as directorUserId, " +
            " d.secretary_user_id as secretaryUserId, " +
            " d.department_name as departmentName," +
            " bd.branch_id as branchId " +
            " from " + tableName + " d "
            + " left join h_user u on  d.director_user_id = u.u_id " +
            " left join h_user u1 on d.secretary_user_id = u1.u_id " +
            " left join evaluation_branch_department bd on d.id = bd.department_id"
            + " <where> "
            + "<if test='id!=null '> and d.id  = #{id} </if>"
            + "<if test='departmentName!=null '> and d.department_name like concat('%', #{departmentName},'%') </if>"  //发薪号
            + "<if test='branchId!=null '> and bd.branch_id = #{branchId} </if>"
            + "<if test='directorUserId!=null '> and d.director_user_id = #{directorUserId} </if>"  //发薪号
            + "<if test='queryName!=null '> and u.u_name like CONCAT(CONCAT('%', #{queryName}),'%')  OR u1.u_name like  CONCAT(CONCAT('%', #{queryName}),'%') </if>"  //发薪号
            + " </where>" +
            "</script> ";

    @Select(sql_selectById)
    EvaluationDepartment selectById(Integer id);

    @Select(sql_list)
    List<EvaluationDepartment> list(Map<String, String> params);

    @Update(sql_update)
    void update(EvaluationDepartment department);

    @Delete(sql_delete)
    void delete(String id);

    @Insert(sql_insert)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(EvaluationDepartment department);

    @Select("<script> select d.id, d.department_name as departmentName , p.relations_name as branchName from evaluation_department d" + "" +
            " left join evaluation_branch_department bp on d.id = bp.department_id  " +
            " left join party_branch_relations p on bp.branch_id = p.id " +
            " order by d.department_name" + " </script>")
    List<EvaluationDepartment> listAll();

    @Select("<script>" +
            " select " +
            " d.department_name" +
            " from evaluation_department_user du left join evaluation_department d  " +
            " on  du.department_id  = d.id  " +
            " where du.user_id = #{userId} " +
            "</script>")
    String getDepartmentNameByUserId(String userId);


    @Select("<script>" +
            " select msg.user_name" +
            " from evaluation_department d left join medical_ethics_msg msg on d.director_user_id = msg.user_id " +
            " where d.id = #{departmentId} " +
            "" +
            "</script>")
    String selectDirectorNameByDepartmentId(int departmentId);

}
