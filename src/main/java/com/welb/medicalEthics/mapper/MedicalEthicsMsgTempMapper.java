package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.entity.MedicalEthicsMsgTemp;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface MedicalEthicsMsgTempMapper {
    @Select("<script>" +
            "SELECT " +
            "t.id," +
            "t.party_committees_id," +
            "p1.relations_name party_committees_name," +
            "t.general_branch_id," +
            "p2.relations_name general_branch_name," +
            "t.branch_id," +
            "p3.relations_name branch_name," +
            "t.user_id," +
            "t.user_name," +
            "t.status," +
            "t.person_type " +
            "FROM " +
            "medical_ethics_msg_temp t " +
            "LEFT JOIN party_branch_relations p1 ON t.party_committees_id = p1.id " +
            "LEFT JOIN party_branch_relations p2 ON t.general_branch_id = p2.id " +
            "LEFT JOIN party_branch_relations p3 ON t.branch_id = p3.id "
            + " <where>"
            + "<if test='id!=null '> and t.id = #{id} </if>"
            + "<if test='userId!=null '> and t.user_id = #{userId} </if>"  //发薪号
            + "<if test='status!=null '> and t.status = #{status} </if>"  //提交状态
            + "<if test='userName!=null '> and  t.user_name LIKE CONCAT(CONCAT('%', #{userName}),'%') </if>"  //用户名
            + "<if test='personType!=null '> and t.person_type = #{personType} </if>"  //人员类型:0-医务人员，1-非临床人员
            + "<if test='relationsId!=null '> and  (t.party_committees_id = #{relationsId} or t.general_branch_id = #{relationsId} or t.branch_id = #{relationsId}) </if> "//党委_党总支_党支部
            + "<if test='partyCommitteesId!=null '> and t.party_committees_id = #{partyCommitteesId} </if>"  //党委
            + "<if test='generalBranchId!=null '> and t.general_branch_id = #{generalBranchId} </if>"  //党总支
            + "<if test='branchId!=null '> and t.branch_id = #{branchId} </if>"  //党支部
            + " </where>"
            + " order by t.status"
            + "</script>")
    List<MedicalEthicsMsgTemp> list(Map<String, String> map);

    @Select("<script>" +
            "SELECT " +
            "t.id," +
            "t.party_committees_id," +
            "p1.relations_name party_committees_name," +
            "t.general_branch_id," +
            "p2.relations_name general_branch_name," +
            "t.branch_id," +
            "p3.relations_name branch_name," +
            "t.user_id," +
            "t.user_name," +
            "t.status," +
            "t.person_type " +
            "FROM " +
            "medical_ethics_msg_temp t " +
            "LEFT JOIN party_branch_relations p1 ON t.party_committees_id = p1.id " +
            "LEFT JOIN party_branch_relations p2 ON t.general_branch_id = p2.id " +
            "LEFT JOIN party_branch_relations p3 ON t.branch_id = p3.id  WHERE t.id IN" +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>"
            + " order by t.status"
            + "</script>")
    List<MedicalEthicsMsgTemp> listByIds(List<Integer> idList);

    @Delete("DELETE FROM medical_ethics_msg_temp WHERE id=#{id}")
    void delete(int id);

    @Update("<script>"
            + "UPDATE "
            + "medical_ethics_msg_temp "
            + "<set> "
            + "<if test=\"userId != null \">"
            + "user_id = #{userId}, "
            + "</if> "
            + "<if test=\"userName != null \">"
            + "user_name = #{userName}, "
            + "</if> "
            + "<if test=\"status != null \">"
            + "status = #{status}, "
            + "</if> "
            + "<if test=\"personType != null \">"
            + "person_type = #{personType}, "
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
            + "</set> "
            + "WHERE id = #{id} "
            + "</script>")
    void update(MedicalEthicsMsgTemp medicalEthicsMsgTemp);

    @Insert("INSERT INTO medical_ethics_msg_temp " +
            " (id, user_id, party_committees_id, general_branch_id, branch_id, user_name, status, person_type) " +
            " VALUES(#{id}, #{userId}, #{partyCommitteesId}, #{generalBranchId}, #{branchId}, #{userName}, #{status}, #{personType}) ")
    void insert(MedicalEthicsMsgTemp medicalEthicsMsgTemp);

    String sql_batchInsert = "<script> " +
            "INSERT INTO medical_ethics_msg_temp (id, user_id, party_committees_id, general_branch_id, branch_id, user_name, status, person_type)" + " VALUES " +
            "<foreach collection ='list' item='item' separator =','>(#{item.id}," +
            "#{item.userId}," +
            "#{item.partyCommitteesId}," +
            "#{item.generalBranchId}," +
            "#{item.branchId}," +
            "#{item.userName}," +
            "#{item.status}," +
            "#{item.personType})" +
            "</foreach >" +
            "</script>";

    @Insert(sql_batchInsert)
    void batchInsert(List<MedicalEthicsMsgTemp> dataSource);

    String sql_batchUpdate = "<script> " +
            "<foreach collection ='list' item='item' separator =';'>" +
            "UPDATE medical_ethics_msg_temp SET " +
            "user_id=#{item.userId}," +
            "party_committees_id=#{item.partyCommitteesId}," +
            "general_branch_id=#{item.generalBranchId}," +
            "branch_id=#{item.branchId}," +
            "user_name=#{item.userName}," +
            "status=#{item.status}," +
            "person_type=#{item.personType}" +
            " WHERE id=#{item.id}" +
            "</foreach >" +
            "</script>";

    @Update(sql_batchUpdate)
    void batchUpdate(List<MedicalEthicsMsgTemp> dataSource);

    @Select(" <script>" +
            " select id, " +
            " user_id, " +
            " party_committees_id, " +
            " general_branch_id, " +
            "branch_id," +
            "user_name," +
            "status," +
            "person_type FROM medical_ethics_msg_temp where user_id = #{userId} " +
            "</script> ")
    MedicalEthicsMsgTemp selectByUserId(String userId);

    @Delete(" <script> delete from medical_ethics_msg_temp where user_id = #{userId} </script> ")
    void deleteByUserId(String userId);
}
