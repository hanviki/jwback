package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.entity.EvaluationNonClinical;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @description: 非临床医务人员考核
 * @author: luox
 * @date： 2020/11/17 10:28
 */

@Mapper
@Component
public interface EvaluationNonClinicalMapper {
    String allColumns = " id," +
            "username," +
            "user_id," +
            "sex," +
            "birth," +
            "hire_date," +
            "job_content," +
            "education_level," +
            "current_position," +
            " political_status," +
            "self_summary," +
            "dept_head_opinion," +
            "branch_opinion," +
            "step," +
            "title," +
            "party_committees_id," +
            "branch_id," +
            "general_branch_id," +
            "level,score,year," +
            "self_submit_name," +
            "self_submit_time," +
            "head_submit_name," +
            "head_submit_time," +
            "branch_submit_name," +
            "branch_submit_time ";

    String sql_insert = "<script>" +
            "INSERT INTO evaluation_non_clinical(id," +
            "username," +
            "sex," +
            "birth," +
            "hire_date," +
            "job_content," +
            "education_level," +
            "current_position," +
            "political_status," +
            "user_id," +
            "self_summary," +
            "dept_head_opinion," +
            "branch_opinion," +
            "step," +
            "title," +
            "party_committees_id," +
            "branch_id," +
            "general_branch_id," +
            "level," +
            "score," +
            "year," +
            "self_submit_name," +
            "self_submit_time," +
            "head_submit_name," +
            "head_submit_time," +
            "branch_submit_name," +
            "branch_submit_time)"
            + " VALUES" +
            "(#{id}," +
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
            "#{year}," +
            "#{selfSubmitName}," +
            "#{selfSubmitTime}," +
            "#{headSubmitName}," +
            "#{headSubmitTime}," +
            "#{branchSubmitName}," +
            "#{branchSubmitTime})" +
            "</script>";

    @Insert(sql_insert)
    Integer insert(EvaluationNonClinical evaluationNonClinical);

    String sql_selectByUserId = "<script>" +
            "SELECT " +
            "id," +
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
            "party_committees_id, " +
            "branch_id, " +
            "general_branch_id, " +
            "step, " +
            "title, " +
            "level, " +
            "score," +
            "year," +
            "self_submit_name," +
            "self_submit_time," +
            "head_submit_name," +
            "head_submit_time," +
            "branch_submit_name," +
            "branch_submit_time " +
            "FROM " +
            "evaluation_non_clinical " +
            "WHERE user_id=#{userId}"
            + "</script>";

    @Select(sql_selectByUserId)
    EvaluationNonClinical selectByUserId(String id);

    String sql_updateById = "<script>" +
            "UPDATE evaluation_non_clinical " +
            "SET " +
            "<if test='sex != null'> sex= #{sex},</if> " +
            "<if test ='birth!=null'> birth= #{birth}, </if>" +
            "<if test = 'username != null'> username= #{username}, </if>" +
            "<if test='hireDate != null'>  hire_date= #{hireDate}, </if>" +
            "<if test='jobContent != null'> job_content= #{jobContent}, </if>" +
            "<if test= 'educationLevel != null'> education_level = #{educationLevel},</if> " +
            " <if test = 'currentPosition != null'> current_position=#{currentPosition},</if> " +
            "<if test='politicalStatus != null'> political_status=#{politicalStatus},</if> " +
            "<if test='selfSummary != null'> self_summary = #{selfSummary},</if> " +
            " dept_head_opinion=#{deptHeadOpinion}," +
            " branch_opinion=#{branchOpinion}," +
            "<if test = 'branchId != null'> branch_id=#{branchId}, </if>" +
            "<if test = 'generalBranchId != null'> general_branch_id=#{generalBranchId}, </if>" +
            "<if test = 'partyCommitteesId != null'> party_committees_id=#{partyCommitteesId}, </if>" +
            "<if test='title != null'> title=#{title},</if> " +
            " level = #{level}, " +
            " score=#{score}," +
            "<if test = 'selfSubmitName != null ' >  self_submit_name=#{selfSubmitName}, </if> " +
            "<if test = 'selfSubmitTime != null' >  self_submit_time=#{selfSubmitTime}, </if> " +
            "<if test = 'headSubmitName != null' >  head_submit_name=#{headSubmitName}, </if> " +
            "<if test = 'headSubmitTime != null ' >  head_submit_time=#{headSubmitTime}, </if> " +
            "<if test = 'branchSubmitName != null' >  branch_submit_name=#{branchSubmitName}, </if> " +
            "<if test = 'branchSubmitTime != null' >  branch_submit_time=#{branchSubmitTime}, </if> " +
            " step=#{step} WHERE id = #{id}" +
            "</script>";

    @Update(sql_updateById)
    void updateById(EvaluationNonClinical evaluationNonClinical);

    @Select(" <script> " +
            " SELECT " +
            " COUNT(0) FROM evaluation_non_clinical " +
            "<where>" +
            " <if test=  'branchOpinion != null '>" + " and branch_opinion = #{branchOpinion} </if> " +
            " </where> " +
            " </script> ")
    Integer selectCountByParams(Map<String, String> params);

    @Select("<script> " +
            " SELECT " + allColumns + " FROM evaluation_non_clinical " +
            " <where> " +

            " <if test=\"userId != null \"> " +
            " and user_id=#{userId} " +
            " </if> " +


            " <if test=\"year != null \"> " +
            " and year=#{year} " +
            " </if> " +

            " </where> order by year desc" +
            " </script> ")
    List<EvaluationNonClinical> list(Map<String, String> params);

    @Update("UPDATE evaluation_non_clinical SET step=#{step} WHERE user_id=#{userId}")
    void updateStepByUserId(int step, String userId);

    @Delete("<script>" +
            " delete from evaluation_non_clinical where user_id = #{userId}" +
            "</script>")
    void deleteByUserId(String userId);


    @Update("<script> " +
            "update  evaluation_non_clinical set step = #{step} WHERE user_id IN " +
            "<foreach collection='idList' item='item' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "</script>")
    void batchFinish(@Param("idList") List<String> idList, int step);

    @Update("<script> " +
            "update  evaluation_non_clinical set step = #{step}" +
            "</script>")
    void finishAll(int step);

    @Select(" SELECT " +
            " user_id " +
            " FROM " +
            " evaluation_non_clinical " +
            " WHERE " +
            " user_id IN ( SELECT user_id  FROM medical_ethics_msg WHERE person_type  = 0 )")
    List<String> selectWrongNonCliIds();
}
