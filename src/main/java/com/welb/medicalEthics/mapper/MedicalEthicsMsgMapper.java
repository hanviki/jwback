package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface MedicalEthicsMsgMapper {

    @Select("<script>" +
            "SELECT " +
            "u.id," +
            "u.user_id," +
            "u.party_committees_id," +
            "r1.relations_name party_committees_name," +
            "r1.person_type party_committees_person_type," +
            "u.general_branch_id," +
            "r2.relations_name general_branch_name," +
            "r2.person_type general_branch_person_type," +
            "u.branch_id," +
            "r3.relations_name branch_name," +
            "r3.person_type branch_person_type," +
            "u.user_name," +
            "u.is_delete, " +
            "u.person_type " +
            "FROM " +
            "medical_ethics_msg u " +
            "LEFT JOIN party_branch_relations r1 ON u.party_committees_id = r1.id " +
            "LEFT JOIN party_branch_relations r2 ON u.general_branch_id = r2.id " +
            "LEFT JOIN party_branch_relations r3 ON u.branch_id = r3.id " +
            " inner join medical_ethics_user_role userRole on u.user_id = userRole.user_id "
            + " <where> userRole.role_id = '3'  "
            + "<if test='id!=null '> and u.id = #{id} </if>"
            + "<if test='userId!=null '> and u.user_id = #{userId} </if>"  //发薪号
            + "<if test='isDelete!=null '> and u.is_delete = #{isDelete} </if>"  //是否删除:0-否；1-是
            + "<if test='userName!=null '> and  u.user_name LIKE CONCAT(CONCAT('%', #{userName}),'%') </if>"  //用户名
            + "<if test='relationsId!=null '> and  (u.party_committees_id = #{relationsId} or u.general_branch_id = #{relationsId} or u.branch_id = #{relationsId}) </if> "//党委_党总支_党支部
            + "<if test='partyCommitteesId!=null '> and u.party_committees_id = #{partyCommitteesId} </if>"  ////党委
            + "<if test='generalBranchId!=null '> and u.general_branch_id = #{generalBranchId} </if>"  //党总支
            + "<if test='branchId!=null '> and u.branch_id = #{branchId} </if>"  //党支部
            + " </where>"
            + " order by id"
            + "</script>")
    List<MedicalEthicsMsg> list(Map<String, String> map);

    @Select("<script>" +
            "SELECT distinct " +
            "u.id," +
            "u.user_id," +
            "u.party_committees_id," +
            "r1.relations_name party_committees_name," +
            "r1.person_type party_committees_person_type," +
            "u.general_branch_id," +
            "r2.relations_name general_branch_name," +
            "r2.person_type general_branch_person_type," +
            "u.branch_id," +
            "r3.relations_name branch_name," +
            "r3.person_type branch_person_type," +
            "u.user_name," +
            "u.is_delete, " +
            "u.person_type " +
            "FROM " +
            "medical_ethics_msg u " +
            " left join party_branch_relations r1 ON u.party_committees_id = r1.id " +
            " left join party_branch_relations r2 ON u.general_branch_id = r2.id " +
            " left join party_branch_relations r3 ON u.branch_id = r3.id " +
            " left join medical_ethics_user_role userRole on u.user_id = userRole.user_id " +
            " left join evaluation_department_user du on u.user_id = du.user_id "
            + " <where> userRole.role_id = '3'  "
            + "<if test='id!=null '> and u.id = #{id} </if>"
            + "<if test='userId!=null '> and u.user_id = #{userId} </if>"  //发薪号
            + "<if test='isDelete!=null '> and u.is_delete = #{isDelete} </if>"  //是否删除:0-否；1-是
            + "<if test=\"userName!=null and userName !='' \"> and  u.user_name LIKE CONCAT(CONCAT('%', #{userName}),'%') </if>"  //用户名
            + "<if test='relationsId!=null '> and  (u.party_committees_id = #{relationsId} or u.general_branch_id = #{relationsId} or u.branch_id = #{relationsId}) </if> "//党委_党总支_党支部
            + "<if test='partyCommitteesId!=null '> and u.party_committees_id = #{partyCommitteesId} </if>"  ////党委
            + "<if test='generalBranchId!=null '> and u.general_branch_id = #{generalBranchId} </if>"  //党总支
            + "<if test='branchId!=null '> and u.branch_id = #{branchId} </if>" //党支部
            + "<if test='searchKey!=null '>and ( u.user_id = #{searchKey} or u.user_name LIKE CONCAT(CONCAT('%', #{searchKey}),'%')  )</if>" //党支部
            + "<if test='personType!=null and personType != 2 '> and u.person_type = #{personType} </if>" +//党支部
            "<if test = 'departmentIdList != null'>" +
            "  and du.department_id in " +
            " <foreach item = 'item2' index = 'index' collection ='departmentIdList' open = '(' separator=',' close = ')'  > " +
            " #{item2} " +
            "  </foreach> " +
            " </if> " +

            "<if test = 'branchIdList != null'>" +
            "  and u.branch_id in " +
            " <foreach item = 'item' index = 'index' collection ='branchIdList' open = '(' separator=',' close = ')'  > " +
            " #{item} " +
            "  </foreach> " +
            " </if> " +
            " </where>"
            + "</script>")
    List<MedicalEthicsMsg> listByParam(Map<String, Object> map);


    @Select("<script>" +
            "SELECT " +
            "u.id," +
            "u.user_id," +
            "u.party_committees_id," +
            "r1.relations_name party_committees_name," +
            "r1.person_type party_committees_person_type," +
            "u.general_branch_id," +
            "r2.relations_name general_branch_name," +
            "r2.person_type general_branch_person_type," +
            "u.branch_id," +
            "r3.relations_name branch_name," +
            "r3.person_type branch_person_type," +
            "u.user_name," +
            "u.is_delete, " +
            "u.person_type " +
            "FROM " +
            "medical_ethics_msg u " +
            "LEFT JOIN party_branch_relations r1 ON u.party_committees_id = r1.id " +
            "LEFT JOIN party_branch_relations r2 ON u.general_branch_id = r2.id " +
            "LEFT JOIN party_branch_relations r3 ON u.branch_id = r3.id " +
            " inner join medical_ethics_user_role userRole on u.user_id = userRole.user_id  WHERE userRole.role_id = 3 AND u.user_id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>"
            + " order by id"
            + "</script>")
    List<MedicalEthicsMsg> listByUserId(List<String> userIdsList);

    @Delete("DELETE FROM medical_ethics_msg WHERE id=#{id}")
    void deleteById(int id);

    @Delete("DELETE FROM medical_ethics_msg WHERE user_id=#{userId}")
    void deleteByUserId(String userId);

    @Update("<script>"
            + "UPDATE "
            + "medical_ethics_msg "
            + "<set> "
            + "<if test=\"userId != null \">"
            + "user_id = #{userId}, "
            + "</if> "
            + "<if test=\"userName != null \">"
            + "user_name = #{userName}, "
            + "</if> "
            + "<if test=\"isDelete != null \">"
            + "is_delete = #{isDelete}, "
            + "</if> "
            + "<if test=\"partyCommitteesId != null \">"
            + "party_committees_id = #{partyCommitteesId}, "
            + "</if> "
            + "<if test=\"generalBranchId != null \">"
            + "general_branch_id = #{generalBranchId}, "
            + "</if> "
            + "<if test=\"branchId != null \">"
            + "branch_id = #{branchId}, "
            + "</if> "
            + "<if test=\"personType != null \">"
            + "person_type = #{personType}, "
            + "</if> "
            + "</set> "
            + "WHERE id = #{id} "
            + "</script>")
    void update(MedicalEthicsMsg medicalEthicsMsg);

    @Insert("INSERT INTO medical_ethics_msg " +
            " (id, user_id, party_committees_id, general_branch_id, branch_id, user_name, is_delete) " +
            " VALUES(#{id}, #{userId}, #{partyCommitteesId}, #{generalBranchId}, #{branchId}, #{userName}, #{isDelete}) ")
    void insert(MedicalEthicsMsg medicalEthicsMsg);

    String sql_batchInsert = "<script> " +
            "INSERT INTO medical_ethics_msg (id, user_id, party_committees_id, general_branch_id, branch_id, user_name, is_delete , person_type )" + " VALUES " +
            "<foreach collection ='list' item='item' separator =','>(#{item.id}," +
            "#{item.userId}," +
            "#{item.partyCommitteesId}," +
            "#{item.generalBranchId}," +
            "#{item.branchId}," +
            "#{item.userName}," +
            "#{item.isDelete}, " +
            "#{item.personType} ) " +
            "</foreach >" +
            "</script>";

    @Insert(sql_batchInsert)
    void batchInsert(List<MedicalEthicsMsg> dataSource);

    String sql_batchDelete = "<script> " +
            "DELETE FROM medical_ethics_msg WHERE id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>";

    @Delete(sql_batchDelete)
    void batchDelete(List<Integer> idList);

    @Select("<script> SELECT id,user_id,party_committees_id,general_branch_id,branch_id,user_name,person_type FROM medical_ethics_msg WHERE" +
            " user_id = #{userId} " +
            " and is_delete = 0" +
            " </script> ")
    MedicalEthicsMsg selectByUserId(String userId);

    @Select("<script> SELECT id,user_id,party_committees_id,general_branch_id,branch_id,user_name,person_type FROM medical_ethics_msg WHERE" +
            " id = #{id} " +
            " and is_delete = 0" +
            " </script> ")
    MedicalEthicsMsg selectByUId(Integer userId);

    @Select("<script> SELECT id,user_id,party_committees_id,general_branch_id,branch_id,user_name,person_type FROM medical_ethics_msg" +
            "<where>" +
            "<if test = 'personType != null'>" +
            " and person_type = #{personType}" +
            "</if>" +
            "<if test = 'branchId != null'>" +
            " and branch_id = #{branchId}" +
            "</if>" +
            "" +
            "</where>" +
            " </script>")
    List<MedicalEthicsMsg> selectAll(Map<String, String> params);

    @Select("<script> select user_id from medical_ethics_msg </script>")
    List<String> allIdList();

    @Select("select * from medical_ethics_msg tmp WHERE tmp.user_id not in (select Moneycard from `user`)")
    List<MedicalEthicsMsg> showMissingUsers();

    @Select(" <script>" +
            " select  " +
            " msg.id," +
            " msg.user_id," +
            " msg.party_committees_id," +
            " msg.general_branch_id," +
            " msg.branch_id," +
            " msg.user_name," +
            " msg.person_type  " +
            " from  " +
            " medical_ethics_msg msg inner join medical_ethics_user_role ur on msg.user_id = ur.user_id " +
            " where role_id = 3 and msg.branch_id = #{branchId}" +
            "</script> ")
    List<MedicalEthicsMsg> selectCommonUser(String branchId);
}
