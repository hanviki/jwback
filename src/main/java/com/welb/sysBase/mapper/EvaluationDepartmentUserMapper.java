package com.welb.sysBase.mapper;

import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.vo.CheckResultVo;
import com.welb.sysBase.DepartmentUserVo;
import com.welb.sysBase.entity.EvaluationDepartmentUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/7
 */

@Mapper
@Component
public interface EvaluationDepartmentUserMapper {

    String tableName = " evaluation_department_user ";
    String allColumns = " id , department_id , user_id ";

    String sql_insert = "<script> insert into " + tableName + " ( id , department_id , user_id ) VALUES ( #{id} , #{departmentId} , #{userId}  ) </script> ";

    String sql_batchInsert = "<script> insert into " + tableName + " ( id , department_id , user_id ) VALUES " +
            "<foreach collection = 'list' item = 'list'  separator =',' >" +
            "( #{list.id} , #{list.departmentId} , #{list.userId}  )" +
            "</foreach>" +
            " </script> ";

    String sql_deleteByParams = "<script> delete from " + tableName +
            " <where> " +
            " <if test = 'id != null' > and id = #{id} </if>" +
            " <if test = 'departmentId != null' >and department_id = #{departmentId} </if>" +
            " <if test = 'userId != null' > and user_id = #{userId} </if>" +
            " </where> " + "</script>";

    String sql_batchDelete = "<script> delete from " + tableName + " where id in " +
            " <foreach collection = 'list'" +
            " item = 'ids'  separator =','  open = '(' close = ')'> " +
            " ( #{ids}  )" +
            " </foreach>" + "</script> ";

    String sql_selectByUserId = " <script> select " + allColumns + " from " + tableName + " where user_id = #{userId} </script> ";
    String sql_selectByDepartmentId = " <script> select " + allColumns + " from " + tableName + " where department_id = #{departmentId} </script>";

    String sql_selectUserDetailByDepartmentId = " <script> " +
            " select" +
            " msg.user_name AS  username," +
            " msg.user_id as userId," +
            " msg.person_type as personType," +
            " dep.department_name as departmentName, " +
            " h_user.u_name as directorName, " +
            " dep.director_user_id as directorUserId, " +
            " p1.relations_name as committeesName, " +
            " p2.relations_name as generalBranchName, " +
            " p3.relations_name as partyBranchName " +
            "" +
            "" +
            " from  evaluation_department_user depUser left join medical_ethics_msg msg on depUser.user_id = msg.user_id " +
            " left join evaluation_department dep on depUser.department_id = dep.id " +
            " left join h_user on dep.director_user_id = h_user.u_id  " +
            " left join party_branch_relations p1 on msg.party_committees_id = p1.id " +
            " left join party_branch_relations p2 on msg.general_branch_id = p2.id " +
            " left join party_branch_relations p3 on msg.branch_id = p3.id " +
            " <where> " +
            "<if test = 'username != null'> and msg.user_name= #{username} </if>" +
            "<if test = 'departmentId != null'> and dep.id = #{departmentId} </if>" +
            "<if test = 'userId != null'> and msg.user_id = #{userId} </if>" +
            "<if test = 'personType != null'> and msg.person_type = #{personType} </if>" +
            "<if test = 'departmentName != null'> and dep.department_name = #{departmentName} </if>" +
            " </where> " +
            " </script> ";

    String sql_selectUserDetailByBranchId = " <script> " +
            " select distinct " +
            " msg.user_name AS  username," +
            " msg.user_id as userId," +
            " msg.person_type as personType, " +
            " p1.relations_name as committeesName, " +
            " p2.relations_name as generalBranchName, " +
            " p3.relations_name as partyBranchName, " +
            " dept.department_name as departmentName " +
            " from " +
            " medical_ethics_msg msg " +
            " left join party_branch_relations p1 on msg.party_committees_id = p1.id " +
            " left join party_branch_relations p2 on msg.general_branch_id = p2.id " +
            " left join party_branch_relations p3 on msg.branch_id = p3.id " +
            " left join evaluation_department_user deptUser on msg.user_id = deptUser.user_id  " +
            " inner join evaluation_branch_department bd on msg.branch_id = bd.branch_id" +
            " left join evaluation_department dept on deptUser.department_id = dept.id " +
            " <where> " +
            " <if test = 'username != null'> and msg.user_name= #{username} </if> " +
            " <if test = 'departmentId != null'> and dep.id = #{departmentId} </if> " +
            " <if test = 'userId != null'> and msg.user_id = #{userId} </if> " +
            " <if test = 'personType != null'> and msg.person_type = #{personType} </if> " +
            " <if test = 'branchId != null'> and bd.branch_id  = #{branchId} </if> " +
            " </where>" +
            " </script> ";

    String sql_selectUserDetailByParams =

            " <script> " +
                    " select distinct " +
                    " msg.user_name AS  username," +
                    " msg.user_id as userId," +
                    " msg.person_type as personType, " +
                    " p1.relations_name as committeesName, " +
                    " p2.relations_name as generalBranchName, " +
                    " p3.relations_name as partyBranchName, " +
                    " p3.director_name as partyDirectorName, " +
                    " dept.department_name as departmentName, " +
                    " dept.id as departmentId  " +
                    " from " +
                    " medical_ethics_msg msg " +
                    " left join party_branch_relations p1 on msg.party_committees_id = p1.id " +
                    " left join party_branch_relations p2 on msg.general_branch_id = p2.id " +
                    " left join party_branch_relations p3 on msg.branch_id = p3.id " +
                    " left join evaluation_department_user deptUser on msg.user_id = deptUser.user_id  " +
                    " left join evaluation_branch_department bd on msg.branch_id = bd.branch_id" +
                    " left join evaluation_department dept on deptUser.department_id = dept.id " +
                    " <where> " +
                    " <if test = 'username != null'> and msg.user_name LIKE CONCAT(CONCAT('%', #{username}),'%') </if> " +
                    " <if test = 'departmentIdList != null'> " +
                    " and deptUser.department_id IN " +
                    " <foreach item = 'item' index = 'index' collection ='departmentIdList' open = '(' separator=',' close = ')'  > " +
                    " #{item} " +
                    "  </foreach> " +
                    "</if> " +

                    "<if test = 'branchIdList != null'>" +
                    " and msg.branch_id in " +
                    " <foreach item = 'item2' index = 'index' collection ='branchIdList' open = '(' separator=',' close = ')'  > " +
                    " #{item2} " +
                    "  </foreach> " +
                    " </if> " +
                    " <if test = 'departmentName != null'> and dept.department_name LIKE CONCAT(CONCAT('%', #{departmentName}),'%') </if> " +
                    " <if test = 'personType != null'> and msg.person_type = #{personType} </if> " +
                    " <if test = 'branchId != null'> and (msg.party_committees_id  =   #{branchId}  or  msg.general_branch_id = #{branchId} or msg.branch_id=  #{branchId})   </if> " +
                    " <if test = 'userId != null'> and msg.user_id = #{userId} </if> " +
                    " </where> order  by deptUser.department_id desc, p1.id,p2.id,p3.id asc" +
                    " </script> ";

    String sql_selectMsgList = " <script> " +
            " select " +
            " msg.user_name as userName , " +
            " msg.user_id as userId , " +
            " msg.person_type as personType, " +
            " p1.relations_name as partyCommitteesName, " +
            " p2.relations_name as generalBranchName, " +
            " p3.relations_name as branchName " +
            " from   evaluation_department_user depUser left join medical_ethics_msg msg" +
            " on depUser.user_id = msg.user_id  " +
            " left join party_branch_relations p1 on msg.party_committees_id = p1.id " +
            " left join party_branch_relations p2 on msg.general_branch_id = p2.id " +
            " left join party_branch_relations p3 on msg.branch_id = p3.id " +
            " <where> " +
            " <if test='departmentId != null'> and depUser.department_id = #{departmentId} </if> " +
            "" +
            " </where> " +
            "" +
            "</script> ";

    @Select(sql_selectUserDetailByDepartmentId)
    List<DepartmentUserVo> selectUserDetailByDepartmentId(Map<String, String> params);

    @Select(sql_selectUserDetailByBranchId)
    List<DepartmentUserVo> selectUserDetailByBranchId(Map<String, String> params);

    @Select(sql_selectUserDetailByParams)
    List<DepartmentUserVo> selectUserDetailByParams(Map<String, Object> params);

    String sql_selectCheckDetailByParams =

            " <script> " +
                    " select distinct " +
                    " msg.user_name AS  username," +
                    " msg.user_id as userId," +
                    " msg.person_type as personType, " +
                    " p1.relations_name as committeesName, " +
                    " p2.relations_name as generalBranchName, " +
                    " p3.relations_name as partyBranchName, " +
                    " p3.director_name as partyDirectorName, " +
                    " dept.department_name as departmentName, " +
                    " dept.id as departmentId,  " +
                    " cli.birth as birth, " +
                    " cli.education_level as educationLevel, " +
                    " cli.step as step, " +
                    " cli.level as level, " +
                    " cli.hire_date as hireDate, " +
                    " cli.year as year, " +
                    " cli.political_status as politicalStatus, " +
                    " cli.title as title " +
                    " from " +
                    "evaluation_clinical cli" +
                    " INNER JOIN medical_ethics_msg msg ON  cli.user_id = msg.user_id  " +
                    " left join party_branch_relations p1 on msg.party_committees_id = p1.id  " +
                    " left join party_branch_relations p2 on msg.general_branch_id = p2.id  " +
                    " left join party_branch_relations p3 on msg.branch_id = p3.id " +
                    " left join evaluation_department_user deptUser on msg.user_id  = deptUser.user_id  " +
                    " left join evaluation_branch_department bd on msg.branch_id  = bd.branch_id" +
                    " left join evaluation_department dept on deptUser.department_id = dept.id " +
                    " <where> " +
                    "cli.step = '5' " +
                    " <if test = 'username != null'> and msg.user_name LIKE CONCAT(CONCAT('%', #{username}),'%') </if> " +
                    " <if test = 'departmentIdList != null'> " +
                    " and deptUser.department_id IN " +
                    " <foreach item = 'item' index = 'index' collection ='departmentIdList' open = '(' separator=',' close = ')'  > " +
                    " #{item} " +
                    "  </foreach> " +
                    "</if> " +
                    " <if test = 'departmentId != null'> and dept.id  = #{departmentId}</if> " +
                    " <if test = 'personType != null'> and msg.person_type = #{personType} </if> " +
                    " <if test = 'branchId != null'> and ( msg.branch_id=  #{branchId} or msg.general_branch_id = #{branchId} or msg.party_committees_id = #{branchId} ) </if> " +
                    " <if test = 'userId != null'> and msg.user_id = #{userId} </if> " +
                    " </where>" +

                    " union " +

                    " select distinct " +
                    " msg.user_name AS  username," +
                    " msg.user_id as userId," +
                    " msg.person_type as personType, " +
                    " p1.relations_name as committeesName, " +
                    " p2.relations_name as generalBranchName, " +
                    " p3.relations_name as partyBranchName, " +
                    " p3.director_name as partyDirectorName, " +
                    " dept.department_name as departmentName, " +
                    " dept.id as departmentId,  " +
                    " non.birth as birth, " +
                    " non.education_level as educationLevel, " +
                    " non.step as step, " +
                    " non.level as level, " +
                    " non.hire_date as hireDate, " +
                    " non.year as year, " +
                    " non.political_status as politicalStatus, " +
                    " non.title as title " +
                    " from " +
                    " evaluation_non_clinical non INNER JOIN medical_ethics_msg msg ON  msg.user_id  = non.user_id  " +
                    " left join party_branch_relations p1 on msg.party_committees_id = p1.id  " +
                    " left join party_branch_relations p2 on msg.general_branch_id = p2.id  " +
                    " left join party_branch_relations p3 on msg.branch_id = p3.id  " +
                    " left join evaluation_department_user deptUser on msg.user_id = deptUser.user_id   " +
                    " left join evaluation_branch_department bd on msg.branch_id = bd.branch_id  " +
                    " left join evaluation_department dept on deptUser.department_id = dept.id " +
                    " <where> " +
                    " and non.step = '3' " +
                    " <if test = 'username != null'> and msg.user_name LIKE CONCAT(CONCAT('%', #{username}),'%') </if> " +
                    " <if test = 'departmentIdList != null'> " +
                    " and deptUser.department_id IN " +
                    " <foreach item = 'item' index = 'index' collection ='departmentIdList' open = '(' separator=',' close = ')'  > " +
                    " #{item} " +
                    "  </foreach> " +
                    "</if> " +
                    " <if test = 'departmentId != null'> and dept.id  = #{departmentId}</if> " +
                    " <if test = 'personType != null'> and msg.person_type = #{personType} </if> " +
                    " <if test = 'branchId != null'> and ( msg.branch_id=  #{branchId} or msg.general_branch_id = #{branchId} or msg.party_committees_id = #{branchId} ) </if> " +
                    " <if test = 'userId != null'> and msg.user_id = #{userId} </if> " +
                    " </where>" +
                    " </script> ";

    @Select(sql_selectCheckDetailByParams)
    List<CheckResultVo> selectCheckDetailByParams(Map<String, Object> params);

    String checkResultByType =
            " <script> " +
                    " select distinct " +
                    " msg.user_name AS  username," +
                    " msg.user_id as userId," +
                    " msg.person_type as personType, " +
                    " msg.branch_id as branchId, " +
                    " msg.party_committees_id as partyCommitteesId, " +
                    " msg2.user_name as departmentDirectorName , " +
                    " p1.relations_name as committeesName, " +
                    " p2.relations_name as generalBranchName, " +
                    " p3.relations_name as partyBranchName, " +
                    " p3.director_name as partyDirectorName, " +
                    " dept.department_name as departmentName, " +
                    " dept.id as departmentId,  " +
                    " cli.birth as birth, " +
                    " cli.education_level as educationLevel, " +
                    " cli.step as step, " +
                    " cli.level as level, " +
                    " cli.hire_date as hireDate, " +
                    " cli.year as year, " +
                    " cli.political_status as politicalStatus, " +
                    " cli.title as title " +
                    " from " +
                    " medical_ethics_msg msg inner JOIN  evaluation_clinical cli " +
                    " ON  cli.user_id = msg.user_id  " +
                    " left join party_branch_relations p1 on msg.party_committees_id = p1.id  " +
                    " left join party_branch_relations p2 on msg.general_branch_id = p2.id  " +
                    " left join party_branch_relations p3 on msg.branch_id = p3.id " +
                    " left join evaluation_department_user deptUser on msg.user_id  = deptUser.user_id  " +
                    " left join evaluation_branch_department bd on msg.branch_id  = bd.branch_id" +
                    " left join evaluation_department dept on deptUser.department_id = dept.id " +
                    " left join medical_ethics_msg msg2 on dept.director_user_id = msg2.user_id "+
                    " <where> " +
                    " <if test = 'username != null'> and msg.user_name LIKE CONCAT(CONCAT('%', #{username}),'%') </if> " +
                    " <if test = 'departmentId != null'> and dept.id  = #{departmentId}</if> " +
                    " <if test = 'personType != null'> and msg.person_type = #{personType} </if> " +
                    " <if test = 'branchId != null'> and ( msg.branch_id=  #{branchId} or msg.general_branch_id = #{branchId} or msg.party_committees_id = #{branchId} ) </if> " +
                    " <if test = 'userId != null'> and msg.user_id = #{userId} </if> " +
                    " </where>" +

                    " union " +

                    " select distinct " +
                    " msg.user_name AS  username," +
                    " msg.user_id as userId," +
                    " msg.person_type as personType, " +
                    " msg.branch_id as branchId, " +
                    " msg.party_committees_id as partyCommitteesId, " +
                    " msg3.user_name as departmentDirectorName , " +
                    " p1.relations_name as committeesName, " +
                    " p2.relations_name as generalBranchName, " +
                    " p3.relations_name as partyBranchName, " +
                    " p3.director_name as partyDirectorName, " +
                    " dept.department_name as departmentName, " +
                    " dept.id as departmentId,  " +
                    " non.birth as birth, " +
                    " non.education_level as educationLevel, " +
                    " non.step as step, " +
                    " non.level as level, " +
                    " non.hire_date as hireDate, " +
                    " non.year as year, " +
                    " non.political_status as politicalStatus, " +
                    " non.title as title " +
                    " from " +
                    " medical_ethics_msg msg inner join evaluation_non_clinical non  "+
                    " ON  msg.user_id  = non.user_id  " +
                    " left join party_branch_relations p1 on msg.party_committees_id = p1.id  " +
                    " left join party_branch_relations p2 on msg.general_branch_id = p2.id  " +
                    " left join party_branch_relations p3 on msg.branch_id = p3.id  " +
                    " left join evaluation_department_user deptUser on msg.user_id = deptUser.user_id   " +
                    " left join evaluation_branch_department bd on msg.branch_id = bd.branch_id  " +
                    " left join evaluation_department dept on deptUser.department_id = dept.id " +
                    " left join medical_ethics_msg msg3 on dept.director_user_id = msg3.user_id "+
                    " <where> " +
                    " <if test = 'username != null'> and msg.user_name LIKE CONCAT(CONCAT('%', #{username}),'%') </if> " +
                    " <if test = 'personType != null'> and msg.person_type = #{personType} </if> " +
                    " <if test = 'branchId != null'> and ( msg.branch_id=  #{branchId} or msg.general_branch_id = #{branchId} or msg.party_committees_id = #{branchId} ) </if> " +
                    " <if test = 'userId != null'> and msg.user_id = #{userId} </if> " +
                    " </where>" +
                    " </script> ";
    @Select(checkResultByType)
    List<CheckResultVo> checkResultByType(Map<String, Object> params);

    @Insert(sql_insert)
    void insert(EvaluationDepartmentUser departmentUser);

    @Insert(sql_batchInsert)
    void batchInsert(List<EvaluationDepartmentUser> list);

    @Delete(sql_batchDelete)
    void batchDelete(List<String> ids);

    @Delete(sql_deleteByParams)
    void deleteByParams(Map<String, String> prams);

    @Select(sql_selectByUserId)
    List<EvaluationDepartmentUser> selectByUserId(Integer userId);

    @Select(sql_selectByDepartmentId)
    List<EvaluationDepartmentUser> selectByDepartmentId(Integer departmentId);

    @Select(sql_selectMsgList)
    List<MedicalEthicsMsg> selectMsgList(Map<String, String> params);

}
