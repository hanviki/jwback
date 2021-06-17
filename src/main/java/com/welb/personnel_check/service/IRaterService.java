package com.welb.personnel_check.service;

import com.welb.personnel_check.entity.Rater;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IRaterService
 * @projectName xh-360appraisal-interface
 * @description: 人事部评分人员业务层
 * @date 2019/9/310:34
 */
public interface IRaterService {
    /**
     * 删除人事部评分人员
     * @param ratercode
     * @return
     */
    int deleteByPrimaryKey(String ratercode);

    /**
     * 新增人事部评分人员
     * @param rater
     * @return
     */
    int insertSelective(Rater rater);

    /**
     * 查询评分人员
     * @param ratercode
     * @return
     */
    Rater selectByPrimaryKey(String ratercode);

    /**
     * 修改评分人员
     * @param rater
     * @return
     */
    int updateByPrimaryKeySelective(Rater rater);


    /**
     * 多条件查询所有评人人员
     * @param rater
     * @return
     */
    List<Rater>selectAllRater(Rater rater);


    /**
     * 查询所有的评分人员
     * @return
     */
    List<Rater> selectList();

    /**
     * 通过发薪号查找评分人员
     * @param moneycard
     * @return
     */
    Rater selectByMoneyCard(String moneycard);

    /**
     * 删除评分人员
     * @param scorringcode
     * @return
     */
    int deleteRaterByScorringCode(String scorringcode);


    /**
     * 查询部门列表
     * @return
     */
    List<Rater>selectDepartList();


    /**
     * 通过部门名称查询部门长信息
     * @param department
     * @return
     */
    Rater selectRaterByDept(String department);


}
