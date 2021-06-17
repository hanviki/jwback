package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.entity.EvaluationClinical;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @description: 临床医务人员考核
 * @author: luox
 * @date： 2020/11/17 10:28
 */

@Mapper
@Component
public interface EvaluationClinicalMapper {
    /**
     * 数据库所有列
     */
    String allColumns = "id," +
            "username," +
            "user_id," +
            "sex," +
            "birth," +
            "hire_date," +
            "job_content," +
            "education_level," +
            "current_position," +
            "political_status," +
            "self_summary," +
            "dept_head_opinion," +
            "branch_opinion," +
            "step," +
            "title,"+
            "party_committees_id," +
            "branch_id," +
            "general_branch_id," +
            "level," +
            "score," +
            "year ";

    String sql_insert = "<script>" +
            "INSERT INTO evaluation_clinical(id,username,sex,birth,hire_date,job_content,education_level,current_position,political_status,user_id,self_summary,dept_head_opinion,branch_opinion,step,title,party_committees_id,branch_id,general_branch_id,level,score,year) "
            + "VALUES" +
            "(" +
            "#{id}," +
            "#{username}," +
            "#{sex}," +
            "#{birth}," +
            "#{hireDate}," +
            "#{jobContent}," +
            "#{educationLevel}," +
            "#{currentPosition}," +
            "#{politicalStatus}," +
            "#{userId}," +
            "#{selfSummary}," +
            "#{deptHeadOpinion}," +
            "#{branchOpinion}," +
            "#{step}," +
            "#{title}, " +
            "#{partyCommitteesId}, " +
            "#{branchId}, " +
            "#{generalBranchId}, " +
            "#{level}, " +
            "#{score}, " +
            "#{year} " +
            ")" +
            "</script>";


    String sql_selectByUserId = "<script>" +
            "SELECT " +
            allColumns +
            "FROM " +
            "evaluation_clinical " +
            "WHERE user_id=#{userId}"
            + "</script>";

    String sql_deleteByUserId = "<script> delete from  evaluation_clinical where user_id = #{userId} </script>";

    @Delete(sql_deleteByUserId)
    void deleteByUserId(String userId);

    String sql_updateByUserId = "<script>" +
            "UPDATE evaluation_clinical " +
            "<set> " +
            "<if test= 'sex != null'>" +
            "sex= #{sex}, " +
            "</if> " +

            "<if test= 'birth != null'>" +
            "birth= #{birth}, " +
            "</if> " +

            "<if test= 'username != null'>" +
            "username= #{username}, " +
            "</if> " +

            "<if test= 'hireDate != null'>" +
            "hire_date= #{hireDate}, " +
            "</if> " +

            "<if test= 'jobContent != null'>" +
            "job_content= #{jobContent}, " +
            "</if> " +

            "<if test= 'educationLevel != null'>" +
            "education_level = #{educationLevel}, " +
            "</if> " +

            "<if test= 'currentPosition != null'>" +
            "current_position=#{currentPosition}, " +
            "</if> " +

            "<if test= 'politicalStatus != null'>" +
            "political_status=#{politicalStatus}, " +
            "</if> " +

            "<if test= 'selfSummary != null'>" +
            "self_summary = #{selfSummary}, " +
            "</if> " +

            "<if test= 'deptHeadOpinion != null'>" +
            "dept_head_opinion=#{deptHeadOpinion}, " +
            "</if> " +

            "<if test= 'branchOpinion != null'>" +
            "branch_opinion=#{branchOpinion}, " +
            "</if> " +

            "<if test= 'title != null'>" +
            "title=#{title}, " +
            "</if> " +

            "<if test= 'step != null'>" +
            "step=#{step}, " +
            "</if> " +

            "<if test= 'level != null'>" +
            "level=#{level}, " +
            "</if> " +

            "<if test= 'score != null'>" +
            "score=#{score}, " +
            "</if> " +

            "<if test= 'branchId != null'>" +
            "branch_id=#{branchId}, " +
            "</if> " +
            "<if test= 'generalBranchId != null'>" +
            "general_branch_id=#{generalBranchId}, " +
            "</if> " +
            "<if test= 'partyCommitteesId != null'>" +
            "party_committees_id=#{partyCommitteesId} " +
            "</if> " +

            "</set> " +
            " WHERE id = #{id}" +
            "</script>";


    String sql_updateById = "<script>" +
            "UPDATE evaluation_clinical " +
            "<set> " +
            "<if test=\"sex != null \">" +
            "sex= #{sex}, " +
            "</if> " +

            "<if test=\"birth != null \">" +
            "birth= #{birth}, " +
            "</if> " +

            "<if test=\"username != null \">" +
            "username= #{username}, " +
            "</if> " +

            "<if test=\"hireDate != null \">" +
            "hire_date= #{hireDate}, " +
            "</if> " +

            "<if test=\"jobContent != null \">" +
            "job_content= #{jobContent}, " +
            "</if> " +

            "<if test=\"educationLevel != null \">" +
            "education_level = #{educationLevel}, " +
            "</if> " +

            "<if test=\"currentPosition != null \">" +
            "current_position=#{currentPosition}, " +
            "</if> " +

            "<if test=\"politicalStatus != null \">" +
            "political_status=#{politicalStatus}, " +
            "</if> " +

            "<if test=\"selfSummary != null \">" +
            "self_summary = #{selfSummary}, " +
            "</if> " +

            "<if test=\"deptHeadOpinion != null \">" +
            "dept_head_opinion=#{deptHeadOpinion}, " +
            "</if> " +

            "<if test=\"branchOpinion != null \">" +
            "branch_opinion=#{branchOpinion}, " +
            "</if> " +

            "<if test=\"branchId != null \">" +
            "branch_id=#{branchId}, " +
            "</if> " +

            "<if test=\"generalBranchId != null \">" +
            "general_branch_id=#{generalBranchId}, " +
            "</if> " +

            "<if test=\"partyCommitteesId != null \">" +
            "party_committees_id=#{partyCommitteesId}, " +
            "</if> " +

            "<if test=\"title != null \">" +
            "title=#{title}, " +
            "</if> " +

            "<if test='step != null'>" +
            "step=#{step}, " +
            "</if> " +

            "level=#{level}, " +

            "score=#{score} " +

            "</set> " +
            " WHERE id = #{id}" +
            "</script>";
    @Insert(sql_insert)
    Integer insert(EvaluationClinical evaluationClinical);

    @Select(sql_selectByUserId)
    EvaluationClinical selectByUserId(String userId);

    @Update(sql_updateByUserId)
    void updateByUserId(EvaluationClinical evaluationClinical);

    @Update(sql_updateById)
    void updateById(EvaluationClinical evaluationClinical);


    @Select("<script>"+"SELECT " +
            allColumns +
            " FROM " +
            " evaluation_clinical " +

            " <where> " +
            " <if test= ' userId != null '>" +
            " and user_id = #{userId} " +
            " </if> " +

            " <if test= 'year != null '>" +
            " and year= #{year} " +
            " </if> " +

            " </where> " +
            " </script> ")
    List<EvaluationClinical> list(Map<String, String> params);

    @Select(" <script> " +
            " SELECT " +
            " COUNT(0) FROM evaluation_clinical " +
            "<where>" +
            " <if test=  'year != null '>" + " and year = #{year} </if> " +
            " and level = 1 " +
            " </where> " +
            " </script> ")
    Integer selectCountByParams(Map<String, String> params);

    @Update("UPDATE evaluation_clinical SET step=#{step} WHERE user_id=#{userId}")
    void updateStepByUserId(int step, String userId);

    @Update("<script> " +
            "update  evaluation_clinical set step = #{step} WHERE user_id IN " +
            "<foreach collection='idList' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    void batchFinish(@Param("idList") List<String> idList,int step);

    @Update("<script> " +
            "update  evaluation_clinical set step = #{step}"+
            "</script>")
    void finishAll(int step);

    @Select(" SELECT " +
            " user_id " +
            " FROM " +
            " evaluation_clinical " +
            " WHERE " +
            " user_id IN ( SELECT user_id  FROM medical_ethics_msg WHERE person_type  = 1 )")
    List<String> selectWrongCliIds();
}
