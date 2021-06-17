package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.entity.PartyBranchRelations;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface PartyBranchRelationsMapper {

    @Select("<script>" +
            " SELECT " +
            " id, " +
            " relations_name, " +
            " parent_id, " +
            " hospital_district, " +
            " person_type," +
            " leader_name," +
            " director_name, " +
            " director_user_id , " +
            " leader_user_id, " +
            " is_delete, " +
            " level, " +
            " excellent_percent " +
            " FROM " +
            " party_branch_relations "
            + " <where>"
            + " is_delete = 0 "
            + "<if test='id!=null '> and id = #{id} </if>"
            + "<if test='relationsName!=null '> and  relations_name LIKE CONCAT(CONCAT('%', #{relationsName}),'%') </if>"  //关系名
            + "<if test='parentId!=null '> and parent_id = #{parentId} </if>"  //父id
            + "<if test='hospitalDistrict!=null '> and  hospital_district LIKE CONCAT(CONCAT('%', #{hospitalDistrict}),'%') </if>"  //院区
            + "<if test='personType!=null '> and person_type = #{personType} </if>"  //人员类型:0-医务人员，1-非临床人员
            + "<if test='leaderName!=null '> and leader_name = #{leaderName} </if>"
            + "<if test='leaderUserId!=null '> and leader_user_id = #{leaderUserId} </if>"
            + "<if test='directorName!=null '> and director_name = #{directorName} </if>"
            + "<if test='directorUserId!=null '> and director_user_id = #{directorUserId} </if>"
            + "<if test='level!=null '> and level = #{level} </if>"
            + " </where>"
            + "</script>")
    List<PartyBranchRelations> list(Map<String, String> map);

    @Select("<script>" +
            "SELECT " +
            "id," +
            "relations_name," +
            "parent_id," +
            " director_name, " +
            " director_user_id , " +
            "hospital_district," +
            "person_type," +
            "leader_name," +
            "leader_user_id," +
            "is_delete, " +
            " level, " +
            " excellent_percent " +
            "FROM " +
            "party_branch_relations "
            + " WHERE "
            + " is_delete = 0 "
            + " and level = 1 or level = 2"
            + "</script>")
    List<PartyBranchRelations> parentList();

    String sql_batchUpdate = "<script> " +
            "<foreach collection ='list' item='item' separator =';'>" +
            "UPDATE party_branch_relations SET " +
            "relations_name=#{item.relationsName}," +
            "parent_id=#{item.parentId}," +
            "hospital_district=#{item.hospitalDistrict}," +
            "person_type=#{item.personType}," +
            "leader_name = #{leaderName}," +
            "leader_user_id = #{leaderUserId}," +
            "is_delete=#{item.isDelete}, " +
            "level = #{level}, " +
            "excellent_percent = #{excellentPercent}, " +
            " director_name = #{directorName}, " +
            " director_user_id = #{directorUserId}, " +
            " WHERE id=#{item.id}" +
            "</foreach >" +
            "</script>";

    @Update(sql_batchUpdate)
    void batchUpdate(List<PartyBranchRelations> dataSource);

    String sql_insert = " <script> " + "" +
            " INSERT INTO party_branch_relations" +
            "( " +
            " id , " +
            " relations_name , " +
            " parent_id , " +
            " hospital_district , " +
            " person_type , " +
            " leader_name, " +
            " leader_user_id, " +
            " is_delete, " +
            " level, " +
            " excellent_percent, " +
            " director_name, " +
            " director_user_id " +
            " ) " +
            " VALUES ( " +
            " #{id}," +
            " #{relationsName}, " +
            " #{parentId}, " +
            " #{hospitalDistrict} , " +
            " #{personType}, " +
            " #{leaderName}, " +
            " #{leaderUserId}, " +
            " #{isDelete}, " +
            " #{level}, " +
            " #{excellentPercent}, " +
            " #{directorName}, " +
            " #{directorUserId} " +
            " ) " +
            "</script>";

    @Insert(sql_insert)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(PartyBranchRelations partyBranchRelations);

    String sql_update = "<script> " +
            " UPDATE party_branch_relations <set> " +
            "<if test='relationsName != null'> relations_name=#{relationsName}, </if>" +
            "<if test='parentId != null'> parent_id=#{parentId}, </if> " +
            "<if test='hospitalDistrict != null'>  hospital_district=#{hospitalDistrict},</if> " +
            "<if test= 'personType != null'> person_type=#{personType}, </if>" +
            " leader_name = #{leaderName}," +
            " leader_user_id = #{leaderUserId}, " +
            " director_name = #{directorName}," +
            " director_user_id = #{directorUserId}," +
            "<if test='isDelete != null'> is_delete=#{isDelete},</if> " +
            "<if test='level != null'> level = #{level},</if> " +
            "<if test='excellentPercent != null'> excellent_percent = #{excellentPercent}, </if>" +
            "<if test='currentExcellentPercent != null'> current_excellent_percent = #{currentExcellentPercent}, </if> " +
            " </set> " +
            " WHERE id=#{id} " +
            " </script> ";

    @Update(sql_update)
    void update(PartyBranchRelations partyBranchRelations);

    String sql_selectById = "<script>" +
            " SELECT " +
            " id," +
            " relations_name, " +
            " parent_id, " +
            " hospital_district, " +
            " person_type, " +
            " is_delete, " +
            " level, " +
            " excellent_percent as excellentPercent, " +
            " director_name as directorName, " +
            " director_user_id as directorUserId ," +
            " leader_name as leaderName, " +
            " leader_user_id as leaderUserId " +
            " FROM " +
            " party_branch_relations "
            + " where "
            + " is_delete = 0 AND "
            + " id = #{id} "
            + " </script> ";

    @Select(sql_selectById)
    PartyBranchRelations selectById(Integer id);

    String sql_selectByParentId = "<script>" +
            " SELECT " +
            " id," +
            " relations_name, " +
            " parent_id, " +
            " hospital_district, " +
            " person_type, " +
            " is_delete, " +
            " level, " +
            " excellent_percent, " +
            " director_name, " +
            " leader_name," +
            " leader_user_id ," +
            " excellent_percent " +
            " FROM " +
            " party_branch_relations " +
            " where " +
            " is_delete = 0 AND " +
            " parent_id = #{parentId} " +
            "</script>";

    @Select(sql_selectByParentId)
    List<PartyBranchRelations> selectByParentId(Integer parentId);

}
