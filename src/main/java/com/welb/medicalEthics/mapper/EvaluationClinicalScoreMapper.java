package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.entity.EvaluationClinicalScore;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @description: 临床评分相关
 * @author: luox
 * @date： 2020/11/18
 */

@Mapper
@Component
public interface EvaluationClinicalScoreMapper {

    String allColumns = " id,user_id,item,self_score,head_score,branch_score,step,question,max_score,year,self_submit_name,self_submit_time,head_submit_name,head_submit_time,branch_submit_name,branch_submit_time ";

    String sql_deleteByUserId = "<script> delete from evaluation_clinical_score where user_id = #{userId} </script>";

    @Delete(sql_deleteByUserId)
    void deleteByUserId(String userId);

    /**
     * 批量插入
     */
    String sql_batchInsertScore =
            "<script> " +
                    "INSERT INTO evaluation_clinical_score (id," +
                    "user_id," +
                    "item," +
                    "self_score," +
                    "head_score," +
                    "branch_score," +
                    "step," +
                    "question," +
                    "max_score," +
                    "self_submit_name," +
                    "self_submit_time," +
                    "head_submit_name," +
                    "head_submit_time," +
                    "branch_submit_name," +
                    "branch_submit_time)" +
                    " VALUES " +
                    " <foreach collection ='list' item='item' separator =','>" +
                    " (#{item.id}, " +
                    " #{item.userId}, " +
                    " #{item.item}, " +
                    " #{item.selfScore}, " +
                    " #{item.headScore}, " +
                    " #{item.branchScore}, " +
                    " #{item.step}, " +
                    " #{item.question}, " +
                    " #{item.maxScore}," +
                    " #{item.selfSubmitName}," +
                    " #{item.selfSubmitTime}," +
                    " #{item.headSubmitName}," +
                    " #{item.headSubmitTime}," +
                    " #{item.branchSubmitName}," +
                    " #{item.branchSubmitTime}" +
                    ") " +
                    "</foreach >" +
                    "</script>";

    @Insert(sql_batchInsertScore)
    void batchInsertScore(List<EvaluationClinicalScore> dataList);

    String sql_insertScore =
            "<script> " +
                    "INSERT INTO evaluation_clinical_score (id," +
                    "user_id," +
                    "item," +
                    "self_score," +
                    "head_score," +
                    "branch_score," +
                    "step," +
                    "question," +
                    "max_score," +
                    "year," +
                    "self_submit_name," +
                    "self_submit_time," +
                    "head_submit_name," +
                    "head_submit_time," +
                    "branch_submit_name," +
                    "branch_submit_time)" +
                    " VALUES " +
                    " (#{id}, " +
                    " #{userId}, " +
                    " #{item}, " +
                    " #{selfScore}, " +
                    " #{headScore}, " +
                    " #{branchScore}, " +
                    " #{step}, " +
                    " #{question}, " +
                    " #{maxScore}," +
                    " #{year}," +
                    " #{selfSubmitName}," +
                    " #{selfSubmitTime}," +
                    " #{headSubmitName}," +
                    " #{headSubmitTime}," +
                    " #{branchSubmitName}," +
                    " #{branchSubmitTime}" +
                    ") " +
                    "</script>";

    @Insert(sql_insertScore)
    void insertScore(EvaluationClinicalScore evaluationClinicalScore);

    String sql_selectByParams =
            " <script> " +
                    " SELECT " +
                    allColumns +
                    " FROM evaluation_clinical_score " +
                    " <where> " +
                    " <if test= 'userId != null '> " +
                    " AND user_id= #{userId} " +
                    " </if> " +
                    " <if test='item != null '> " +
                    " AND item = #{item} " +
                    " </if> " +
                    " <if test='id != null '> " +
                    " AND id = #{id} " +
                    " </if> " +
                    " <if test='year != null '> " +
                    " AND year = #{year} " +
                    " </if> " +
                    " </where> " +
                    " ORDER BY item ASC " +
                    " </script> ";

    @Select(sql_selectByParams)
    List<EvaluationClinicalScore> selectByParams(Map<String, String> params);

    @Select(sql_selectByParams)
    List<EvaluationClinicalScore> list(Map<String, String> params);

    String sql_batchUpdate = "<script> " +
            "<foreach collection ='list' item='item' separator =';'>" +
            "UPDATE evaluation_clinical_score <set> " +
            "user_id=#{item.userId}, " +
            "  self_score=#{item.selfScore}," +
            "  head_score=#{item.headScore}, " +
            "   branch_score=#{item.branchScore}, " +
            "   self_submit_name=#{item.selfSubmitName}, " +
            "   self_submit_time=#{item.selfSubmitTime}, " +
            "   head_submit_name=#{item.headSubmitName},  " +
            "   head_submit_time=#{item.headSubmitTime}, " +
            "   branch_submit_name=#{item.branchSubmitName}, " +
            " branch_submit_time=#{item.branchSubmitTime},  " +
            " step=#{item.step}, " +
            " question = #{item.question}, " +
            " max_score = #{item.maxScore} " +
            " WHERE id = #{item.id} " +
            " </set> " +
            " </foreach> " +
            " </script> ";

    @Update(sql_batchUpdate)
    void batchUpdate(List<EvaluationClinicalScore> scoreList);

    @Update("UPDATE evaluation_clinical_score SET step=#{step} WHERE user_id=#{userId}")
    void updateStepByUserId(int step, String userId);
}
