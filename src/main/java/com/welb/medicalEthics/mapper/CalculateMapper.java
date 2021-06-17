package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.dto.CalculateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 统计相关
 * @author: luox
 * @date： 2020/12/22
 */

@Mapper
@Component
public interface CalculateMapper {

    /**
     * 非临床的条件统计
     *
     * @param params
     * @return
     */
    @Select("<script>" +
            " select count(*) " +
            " from evaluation_non_clinical " +
            " <where>" +
            " <if test = 'year != null'> and year = #{year} </if> " +
            " <if test = 'level != null'> and level = #{level} </if> " +
            " <if test = 'step != null'> and step = #{step} </if> " +
            " <if test = 'politicalStatus != null'> and political_status = #{politicalStatus} </if> " +
            " <if test = 'educationLevel != null'> and education_level = #{educationLevel} </if> " +
            " <if test = 'branchId != null'> and (branch_id = #{branchId} or general_branch_id = #{branchId} or party_committees_id = #{branchId})  </if> " +
            " </where> " +
            " </script> ")
    int nonCliCountByParams(Map<String, String> params);

    /**
     * 临床的统计
     *
     * @param params
     * @return
     */
    @Select("<script>" +
            " select  count(*) " +
            " from evaluation_clinical " +
            " <where>" +
            " <if test = 'year != null'> and year = #{year} </if> " +
            " <if test = 'level != null'> and level = #{level} </if> " +
            " <if test = 'step != null'> and step = #{step} </if> " +
            " <if test = 'politicalStatus != null'> and political_status = #{politicalStatus} </if> " +
            " <if test = 'educationLevel != null'> and education_level = #{educationLevel} </if> " +
            " <if test = 'branchId != null'> and (branch_id = #{branchId} or general_branch_id = #{branchId} or party_committees_id = #{branchId})  </if> " +
            " </where>" +
            " </script> ")
    int cliCountByParams(Map<String, String> params);

    @Select("<script> " +
            " SELECT " +
            "count( case when cli.`level` = '1' then 1 end ) as totalLevelOneCount, " +
            "count( case when cli.`level` = '2' then 2 end ) as totalLevelTwoCount, " +
            "count( case when cli.`level` = '3' then 3 end ) as totalLevelThreeCount, " +
            "count( case when cli.`level` = '4' then 4 end ) as totalLevelFourCount " +
            " FROM " +
            " evaluation_clinical cli " +
            " LEFT JOIN medical_ethics_msg msg ON cli.user_id = msg.user_id   " +
            "<where>" +
            " AND cli.step=5  " +
            " <if test = 'branchId != null'> and (msg.branch_id = #{branchId} or msg.general_branch_id = #{branchId} or msg.party_committees_id = #{branchId})  </if> " +
            " <if test = 'year != null'> and cli.year = #{year} </if> " +
            "</where>" +
            "</script>")
    CalculateDto cliResultCalculate(Map<String, String> prams);

    @Select("select count(*) from medical_ethics_msg where (branch_id = #{branchId} or general_branch_id = #{branchId} or party_committees_id = #{branchId}) ")
    int selectPartyCount(String branchId);

    @Select("<script> " +
            " SELECT " +
            "count( case when non.`level` = '1' then 1 end ) as totalLevelOneCount, " +
            "count( case when non.`level` = '2' then 2 end ) as totalLevelTwoCount, " +
            "count( case when non.`level` = '3' then 3 end ) as totalLevelThreeCount, " +
            "count( case when non.`level` = '4' then 4 end ) as totalLevelFourCount " +
            " FROM " +
            " evaluation_non_clinical non " +
            " LEFT JOIN medical_ethics_msg msg ON non.user_id = msg.user_id   " +
            "<where>" +
            " AND non.step=3  " +
            " <if test = 'branchId != null'> and (msg.branch_id = #{branchId} or msg.general_branch_id = #{branchId} or msg.party_committees_id = #{branchId})  </if> " +
            " <if test = 'year != null'> and non.year = #{year} </if> " +
            "</where>" +
            "</script>")
    CalculateDto nonCliResultCalculate(Map<String, String> prams);


}
