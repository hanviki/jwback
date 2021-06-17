package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.entity.MedicalEthicsRoleInfo;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.MedicalEthicsUserRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface MedicalEthicsUserMapper {

    String baseColumns =
            " id ," +
                    " user_id, " +
                    " sex, " +
                    " user_name, " +
                    " party_committees_id, " +
                    " general_branch_id, " +
                    " branch_id, " +
                    " id_card, " +
                    " political_status, " +
                    " education_level, " +
                    " current_position, " +
                    " hire_date, " +
                    " title, " +
                    " job_content, " +
                    " is_delete ";


    @Select("<script>" +
            "SELECT DISTINCT " +
            "u.id," +
            "u.user_id," +
            "u.user_name," +
            "u.party_committees_id," +
            "r1.relations_name as partyCommitteesName," +
            "r1.person_type party_committees_person_type," +
            "u.general_branch_id," +
            "r2.relations_name as generalBranchName," +
            "r2.person_type general_branch_person_type," +
            "u.branch_id," +
            "r3.relations_name as branchName," +
            "r3.person_type branch_person_type," +
            "u.sex," +
            "u.id_card," +
            "u.political_status," +
            "u.education_level," +
            "u.current_position," +
            "u.hire_date," +
            "u.job_content," +
            "u.is_delete," +
            "ri.role_code," +
            " ri.role_name, " +
            " dp.department_name " +
            " FROM " +
            " medical_ethics_user u " +
            "LEFT JOIN party_branch_relations r1 ON u.party_committees_id = r1.id " +
            "LEFT JOIN party_branch_relations r2 ON u.general_branch_id = r2.id " +
            "LEFT JOIN party_branch_relations r3 ON u.branch_id = r3.id " +
            "LEFT JOIN medical_ethics_user_role ur ON ur.user_id = u.user_id " +
            "LEFT JOIN medical_ethics_role_info ri ON ur.role_id = ri.id " +
            "LEFT JOIN evaluation_department dp ON ur.department_id = dp.id "+
            "LEFT JOIN medical_ethics_msg msg ON u.user_id = msg.user_id "+
            " <where>"
            + "<if test='id!=null '> and u.id = #{id} </if>"
            + "<if test='userId!=null '> and u.user_id = #{userId} </if>"  //发薪号
            + "<if test='sex!=null '> and u.sex = #{sex} </if>"  //性别
            + "<if test='userName!=null '> and  u.user_name LIKE CONCAT(CONCAT('%', #{userName}),'%') </if>"  //用户名
            + "<if test='isDelete!=null '> and u.is_delete = #{isDelete} </if>"  //是否删除:0-否；1-是
            + "<if test='roleCode!=null '> and ri.role_code = #{roleCode} </if>"  //角色代号：100-管理员; 101-书记; 200-普通用户
            + "<if test='roleName!=null '> and ri.role_name = #{roleName} </if>"  //角色名
            + "<if test='personType!=null '> and msg.person_type = #{personType} </if>"  //角色名
            + "<if test='relationsId!=null '> and (u.party_committees_id = #{relationsId} or  u.general_branch_id = #{relationsId} or u.branch_id = #{relationsId})   </if>"  //党委
            + " </where> ORDER BY u.user_name, r1.relations_name,r2.relations_name,r3.relations_name "
            + "</script>")
    List<MedicalEthicsUser> list(Map<String, String> map);

    @Select("<script>" +
            "SELECT DISTINCT " +
            "u.id," +
            "u.user_id," +
            "u.user_name," +
            "u.party_committees_id," +
            "r1.relations_name as partyCommitteesName," +
            "r1.person_type party_committees_person_type," +
            "u.general_branch_id," +
            "r2.relations_name as generalBranchName," +
            "r2.person_type general_branch_person_type," +
            "u.branch_id," +
            "r3.relations_name as branchName," +
            "r3.person_type branch_person_type," +
            "u.sex," +
            "u.id_card," +
            "u.political_status," +
            "u.education_level," +
            "u.current_position," +
            "u.hire_date," +
            "u.job_content," +
            "u.is_delete," +
            "ri.role_code," +
            "ri.role_name " +
            "FROM " +
            "medical_ethics_user u " +
            "LEFT JOIN party_branch_relations r1 ON u.party_committees_id = r1.id " +
            "LEFT JOIN party_branch_relations r2 ON u.general_branch_id = r2.id " +
            "LEFT JOIN party_branch_relations r3 ON u.branch_id = r3.id " +
            "LEFT JOIN medical_ethics_user_role ur ON ur.user_id = u.user_id " +
            "LEFT JOIN medical_ethics_role_info ri ON ur.role_id = ri.id WHERE u.user_id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>"  +
            "#{item}" +
            "</foreach>"
            + " ORDER BY r2.id,r3.id, r1.relations_name,r2.relations_name,r3.relations_name "
            + "</script>")
    List<MedicalEthicsUser> listByUserId(List<String> userIdsList);

    String sql_selectByParams =
            "<script> SELECT " + baseColumns
                    + " FROM "
                    + " medical_ethics_user "
                    + " <where>"
                    + "<if test='id!=null '> and id = #{id} </if>"
                    + "<if test='userId!=null '> and user_id = #{userId} </if>"  //发薪号
                    + "<if test='sex!=null '> and sex = #{sex} </if>"  //性别
                    + "<if test='userName!=null '> and user_name LIKE CONCAT(CONCAT('%', #{userName}),'%') </if>"  //用户名
                    + "<if test='partyCommitteesId!=null '> and party_committees_id = #{partyCommitteesId} </if>"  //党委
                    + "<if test='generalBranchId!=null '> and general_branch_id = #{generalBranchId} </if>"  //党总支
                    + "<if test='branchId!=null '> and branch_id = #{branchId} </if>"  //党支部
                    + " and is_delete = 0 "
                    + " </where>" +
                    "</script>";

    /**
     * selectByParams 根据参数查询列表，非详情
     *
     * @param params 查询参数
     * @return {@link List<MedicalEthicsUser>}
     */
    @Select(sql_selectByParams)
    List<MedicalEthicsUser> selectByParams(Map<String, String> params);

    @Select("<script>" +
            "SELECT " +
            "ur.id," +
            "ur.user_id," +
            "ri.role_code," +
            "ri.role_name " +
            "FROM " +
            "medical_ethics_user_role ur " +
            "LEFT JOIN medical_ethics_role_info ri ON ur.role_id = ri.id "
            + " <where>"
            + "<if test='id!=null '> and ur.id = #{id} </if>"
            + "<if test='userId!=null '> and ur.user_id = #{userId} </if>"  //发薪号
            + "<if test='roleCode!=null '> and ri.role_code = #{roleCode} </if>"  //角色代号：100-管理员；101-书记；200-普通用户
            + "<if test='roleName!=null '> and ri.role_name = #{roleName} </if>"  //角色名
            + " </where>"
            + "</script>")
    List<MedicalEthicsUserRole> showRoleMsg(Map<String, String> params);

    String sql_batchInsert = "<script> " +
            "INSERT INTO medical_ethics_user (id, user_id, user_name, sex, party_committees_id, general_branch_id, branch_id, id_card, political_status, education_level, current_position, hire_date, job_content, is_delete)" + " VALUES " +
            "<foreach collection ='list' item='item' separator =','>(#{item.id}," +
            "#{item.userId}," +
            "#{item.userName}," +
            "#{item.sex}," +
            "#{item.partyCommitteesId}," +
            "#{item.generalBranchId}," +
            "#{item.branchId}," +
            "#{item.idCard}," +
            "#{item.politicalStatus}," +
            "#{item.educationLevel}," +
            "#{item.currentPosition}," +
            "#{item.hireDate}," +
            "#{item.jobContent}," +
            "#{item.isDelete})" +
            "</foreach >" +
            "</script>";

    String sql_deleteByParams = " <script> DELETE FROM medical_ethics_user_role" +
            " <where> "
            + " <if test='id!=null '> and id = #{id} </if> "
            + " <if test='roleId!=null '> and role_id = #{roleId} </if> "
            + " <if test='partyId !=null '> and party_id = #{partyId} </if> "
            + " <if test='userId !=null '> and user_id = #{userId} </if> "
            + " <if test='departmentId !=null '> and department_id = #{departmentId} </if> "
            + " </where>"
            + " </script>  ";

    @Insert(sql_batchInsert)
    void batchInsert(List<MedicalEthicsUser> dataSource);

    String sql_batchDelete = "<script> " +
            "DELETE FROM medical_ethics_user WHERE id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>";

    @Delete(sql_batchDelete)
    void batchDelete(List<Integer> idList);

    @Delete("DELETE FROM medical_ethics_user WHERE user_id=#{userId}")
    void deleteByUserId(String userId);

    @Delete(" DELETE FROM medical_ethics_user_role WHERE user_id=#{userId} AND party_id = #{partyId} ")
    void deleteUserRoleByUserIdAndPartyId(String userId, Integer partyId);

    @Delete(sql_deleteByParams)
    void deleteUserRoleByParams(Map<String, String> prams);

    String sql_batchInsertIntoUserRole = "<script> " +
            "INSERT INTO medical_ethics_user_role (id, user_id, role_id , party_id ,party_level)" + " VALUES " +
            "<foreach collection ='list' item='item' separator =','>(" +
            "#{item.id}," +
            "#{item.userId}," +
            "#{item.roleId}," +
            "#{item.partyId}," +
            "#{item.partyLevel}" +
            ")" +
            "</foreach >" +
            "</script>";

    @Insert(sql_batchInsertIntoUserRole)
    void batchInsertIntoUserRole(List<MedicalEthicsUserRole> dataSource);

    String sql_batchDeleteUserRole = "<script> " +
            "DELETE FROM medical_ethics_user_role WHERE user_id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>";

    String sql_batchDeleteCommonUserRole = "<script> " +
            "DELETE FROM medical_ethics_user_role WHERE user_id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach> and role_id = 3" +
            "</script>";

    @Delete(sql_batchDeleteUserRole)
    void batchDeleteUserRole(List<String> userIds);

    @Delete(sql_batchDeleteCommonUserRole)
    void batchDeleteCommonUserRole(List<String> userIds);

    @Delete("DELETE FROM medical_ethics_user_role WHERE user_id=#{userId}")
    void deleteUserRoleByUserId(String userId);

    @Select("<script>" +
            "SELECT " +
            "id," +
            "role_code," +
            "role_name," +
            "description " +
            "FROM " +
            "medical_ethics_role_info "
            + " <where>"
            + "<if test='id!=null '> and id = #{id} </if>"
            + "<if test='roleCode!=null '> and role_code = #{roleCode} </if>"
            + " </where>"
            + "</script>")
    MedicalEthicsRoleInfo getRoleInfo(Map<String, String> params);

    /**
     * 临时表用户更新
     *
     * @param userId
     * @param partyCommitteesId
     * @param generalBranchId
     * @param branchId
     * @param personType
     */
    @Update("<script>" + "UPDATE medical_ethics_msg_temp " +
            " <set> " +
            " <if test= \"partyCommitteesId != null \">" +
            " party_committees_id=#{partyCommitteesId}, </if> " +
            " <if test=\"generalBranchId != null \">" +
            " general_branch_id=#{generalBranchId}, </if>" +
            " <if test= \"branchId != null \">" + " branch_id=#{branchId}, </if>" +
            " <if test= \"personType != null \">" + "  person_type=#{personType}, </if>" +
            " </set> " +
            " WHERE user_id=#{userId}" + "</script>")
    void updateRelations(String userId, Integer partyCommitteesId, Integer generalBranchId, Integer branchId, Integer personType);

    /**
     * 已提交用户数据更新
     *
     * @param userId
     * @param partyCommitteesId
     * @param generalBranchId
     * @param branchId
     * @param personType
     */
    @Update("<script>" + "UPDATE medical_ethics_msg " +
            " <set> " +
            " <if test= \"partyCommitteesId != null \">" +
            " party_committees_id=#{partyCommitteesId}, </if> " +
            " <if test=\"generalBranchId != null \">" +
            " general_branch_id=#{generalBranchId}, </if>" +
            " <if test= \"branchId != null \">" + " branch_id=#{branchId}, </if>" +
            " <if test= \"personType != null \">" + "  person_type=#{personType}, </if>" +
            " </set> " +
            " WHERE user_id=#{userId}" + "</script>")
    void updateRelations2(String userId, Integer partyCommitteesId, Integer generalBranchId, Integer branchId, Integer personType);

    @Select(
            "<script>" +
                    "SELECT" +
                    " user_id, " +
                    " user_name, " +
                    " sex," +
                    "party_committees_id, " +
                    "general_branch_id, " +
                    "branch_id, " +
                    "id_card, " +
                    "political_status, " +
                    "education_level, " +
                    "current_position, " +
                    "hire_date, " +
                    "job_content " +
                    "FROM medical_ethics_user WHERE user_id = #{userId}" +
                    "</script>"

    )
    List<MedicalEthicsUser> selectByUserId(String userId);

    @Update("<script>"
            + "UPDATE "
            + "medical_ethics_user "
            + "<set> "
            + "<if test=\"userId != null \">"
            + "user_id = #{userId}, "
            + "</if> "
            + "<if test=\"userName != null \">"
            + "user_name = #{userName}, "
            + "</if> "
            + "<if test=\"sex != null \">"
            + "sex = #{sex}, "
            + "</if> "
            + "<if test=\"politicalStatus != null \">"
            + "political_status = #{politicalStatus}, "
            + "</if> "
            + "<if test=\"educationLevel != null \">"
            + "education_level = #{educationLevel}, "
            + "</if> "
            + "<if test=\"currentPosition != null \">"
            + "current_position = #{currentPosition}, "
            + "</if> "
            + "<if test=\"hireDate != null \">"
            + "hire_date = #{hireDate}, "
            + "</if> "
            + "<if test=\"title != null \">"
            + "title = #{title}, "
            + "</if> "
            + "<if test=\"jobContent != null \">"
            + "job_content = #{jobContent}, "
            + "</if> "
            + "<if test=\"personType != null \">"
            + "person_type = #{personType}, "
            + "</if> "
            + "</set> "
            + "WHERE user_id = #{userId} "
            + "</script>")
    void updateBaseInfo(MedicalEthicsUser medicalEthicsUser);

    @Insert("<script> INSERT INTO medical_ethics_user_role ( id ,user_id ,role_id , party_id , party_level , department_id) VALUES ( #{id} , #{userId} , #{roleId} , #{partyId} , #{partyLevel} ,#{departmentId}) </script> ")
    void addUserRole(MedicalEthicsUserRole medicalEthicsUserRole);

    @Insert(" <script> " +
            " INSERT INTO medical_ethics_user( id,user_id,user_name,sex,party_committees_id,general_branch_id,branch_id,id_card, political_status,education_level,current_position,hire_date,job_content,is_delete,title) " +
            " VALUES( " +
            " #{id} ,#{userId},#{userName},#{sex},#{partyCommitteesId},#{generalBranchId},#{branchId},#{idCard},#{politicalStatus},#{educationLevel},#{currentPosition},#{hireDate},#{jobContent},#{isDelete},#{title} )" +
            " </script> ")
    void addUser(MedicalEthicsUser user);

    @Select(" <script> SELECT " +
            "ur.user_id as userId," +
            "ur.role_id as roleId," +
            "r.role_code as roleCode," +
            "r.role_name as roleName," +
            "ur.party_id as partyId," +
            "ur.party_level as partyLevel," +
            "ur.department_id as departmentId " +
            "" +
            " FROM medical_ethics_user_role ur" +
            " left join medical_ethics_role_info r on ur.role_id = r.id  WHERE user_id = #{userId} </script> ")
    List<MedicalEthicsUserRole> selectUserRoleByUserId(String userId);

    @Select(" <script> SELECT * FROM medical_ethics_user_role <where> " +
            " <if test = 'userId != null' > and user_id = #{userId}  </if> " +
            " <if test = 'partyId !=null'> and party_id = #{partyId}  </if> " +
            " <if test = 'roleId != null' > and role_id = #{roleId}  </if> " +
            " <if test = 'departmentId != null' > and department_id = #{departmentId}  </if> " +
            " </where> " +
            " </script> ")
    List<MedicalEthicsUserRole> selectUserRoleByParams(Map<String,String> params);

}
