package com.welb.survey.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.survey.entity.SurveyInfo;
import com.welb.survey.entity.SurveyOption;
import com.welb.survey.entity.SurveyQuestion;
import com.welb.survey.info.BatchJson;
import com.welb.survey.info.OptionInfo;
import com.welb.survey.service.ISurveyInfoService;
import com.welb.survey.service.ISurveyQuestionService;
import com.welb.sysBase.service.IPermissionService;
import com.welb.util.LogUtil;
import com.welb.util.PageData;
import com.welb.util.Tools;
import net.sf.json.JSONArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author luoyaozu
 * @title: SurveyInfoController
 * @projectName xh-360appraisal-interface
 * @description: 问卷调查控制层
 * @date 2019/11/418:01
 */
@RestController
@RequestMapping("/surveyInfo")
public class SurveyInfoController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    ISurveyInfoService surveyInfoService;
    @Autowired
    IUserService userService;
    @Autowired
    ISurveyQuestionService questionService;
    @Autowired
    private IPermissionService permissionService;

    /**
     * 查询全部问卷
     *
     * @param surveyInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Object selectSurveyInfoList(SurveyInfo surveyInfo, int pageNum, int pageSize, HttpServletRequest request) {
        ModelMap map = new ModelMap();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<SurveyInfo> surveyInfos = surveyInfoService.selectSurveyInfoList(surveyInfo);
            //通过CreateUser(MoneyCard)过滤数据
            Map<String, String> param = Tools.getParamMap(request);
            String u_id = param.get("u_id");
            List<PageData> roleList = permissionService.showUserRole(new HashMap<String, String>(){{put("user_code", u_id);}}).stream().filter(r-> r.getString("RoleCode").equals("50")).collect(Collectors.toList());
            if (roleList != null && !roleList.isEmpty()){
                //权限为50的用户可查看所有问卷
                PageInfo<SurveyInfo> pageInfo = new PageInfo<>(surveyInfos);
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", surveyInfos);
            }else {
                //权限不为0
                User user = userService.getUserByUserCode(u_id);
                if (user != null){
                    //只能查看自己创建的问卷
                    surveyInfos = surveyInfos.stream().filter(o->o.getCreateuser().equals(user.getMoneycard())).collect(Collectors.toList());
                    PageInfo<SurveyInfo> pageInfo = new PageInfo<>(surveyInfos);
                    map.put("totalPages", pageInfo.getTotal());
                    map.put("data", surveyInfos);
                }
            }
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 查询全部星标问卷
     *
     * @param surveyInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/starList")
    public Object selectStarSurveyInfoList(SurveyInfo surveyInfo, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<SurveyInfo> surveyInfos = surveyInfoService.selectStarSurveyInfoList(surveyInfo);
            PageInfo<SurveyInfo> pageInfo = new PageInfo<>(surveyInfos);
            map.put("totalPages", pageInfo.getTotal());
            map.put("data", surveyInfos);
            map.put("msg", "查询全部星标问卷成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查询全部星标问卷失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 查询回收站所有问卷
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/recycleList")
    public Object selectRecycleSurveyInfoList(int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<SurveyInfo> surveyInfos = surveyInfoService.selectRecycleSurveyInfoList();
            PageInfo<SurveyInfo> pageInfo = new PageInfo<>(surveyInfos);
            map.put("totalPages", pageInfo.getTotal());
            map.put("data", surveyInfos);
            map.put("msg", "查询回收站所有问卷成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查询回收站所有问卷失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 添加问卷
     *
     * @param surveyInfo
     * @return
     */
    @RequestMapping("/add")
    public Object addSurveyInfo(SurveyInfo surveyInfo) {
        ModelMap map = new ModelMap();
        try {
            PageData pageData = new PageData(Tools.getRequest());
            String moneycard = pageData.getString("u_id");
            User user = userService.getUserByUserCode(moneycard);
            if (user == null){
                map.put("msg", "该账号暂无权限，有问题请联系管理员");
                map.put("code", 810);
            }else {
                surveyInfo.setCreateuser(user.getMoneycard());
                surveyInfoService.insertSelective(surveyInfo);
                map.put("msg", "添加问卷成功");
                map.put("code", 0);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "添加问卷失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 编辑问卷
     *
     * @param surveyInfo
     * @return
     */
    @RequestMapping("/update")
    public Object updateSurveyInfo(SurveyInfo surveyInfo) {
        ModelMap map = new ModelMap();
        try {
            PageData pageData = new PageData(Tools.getRequest());
            String moneycard = pageData.getString("u_id");
            User user = userService.getUserByUserCode(moneycard);
            if (user == null){
                map.put("msg", "该账号暂无权限，有问题请联系管理员");
                map.put("code", 810);
            }else {
                surveyInfo.setUpdateuser(user.getMoneycard());
                surveyInfoService.updateByPrimaryKeySelective(surveyInfo);
                map.put("msg", "编辑问卷成功");
                map.put("code", 0);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "编辑问卷失败");
            map.put("code", 1);
        }
        return map;
    }
    /**
     * 解除软删除状态，恢复数据
     *
     * @param id
     * @return
     */
    @RequestMapping("/updateFlag")
    public Object updateFlag(Integer id) {
        ModelMap map = new ModelMap();
        try {
            PageData pageData = new PageData(Tools.getRequest());
            String moneycard = pageData.getString("u_id");
            User user = userService.getUserByUserCode(moneycard);
            if (user == null){
                map.put("msg", "该账号暂无权限，有问题请联系管理员");
                map.put("code", 810);
            }else {
                surveyInfoService.updateFlag(id);
                map.put("msg", "操作成功");
                map.put("code", 0);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 永久删除单个问卷
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Object deleteSurveyInfo(Integer id) {
        ModelMap map = new ModelMap();
        try {
            PageData pageData = new PageData(Tools.getRequest());
            String moneycard = pageData.getString("u_id");
            User user = userService.getUserByUserCode(moneycard);
            if (user == null){
                map.put("msg", "该账号暂无权限，有问题请联系管理员");
                map.put("code", 810);
            }else {
                surveyInfoService.deleteByPrimaryKey(id);
                map.put("msg","操作成功");
                map.put("code",0);
            }
        }catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg","操作失败");
            map.put("code",1);
        }
        return map;
    }


    /**
     * 批量永久删除问卷
     * @param ids
     * @return
     */
    @RequestMapping("/batchDelete")
    public Object batchDelete(String ids) {
        ModelMap map = new ModelMap();
        try {
            PageData pageData = new PageData(Tools.getRequest());
            String moneycard = pageData.getString("u_id");
            User user = userService.getUserByUserCode(moneycard);
            if (user == null){
                map.put("msg", "该账号暂无权限，有问题请联系管理员");
                map.put("code", 810);
            }else {
                if (ids.equals("")) {
                    surveyInfoService.batchDelete(ids);
                    map.put("msg", "操作成功");
                    map.put("code", 0);
                }else {
                    map.put("msg", "请选择要删除的数据");
                    map.put("code", 1);
                }
            }
        }catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg","操作失败");
            map.put("code",1);
        }
        return map;
    }

    /**
     * 清空回收站所有问卷
     * @return
     */
    @RequestMapping("/deleteAll")
    public Object deleteAll() {
        ModelMap map = new ModelMap();
        try {
            PageData pageData = new PageData(Tools.getRequest());
            String moneycard = pageData.getString("u_id");
            User user = userService.getUserByUserCode(moneycard);
            if (user == null){
                map.put("msg", "该账号暂无权限，有问题请联系管理员");
                map.put("code", 810);
            }else {
                List<SurveyInfo> surveyInfos = surveyInfoService.selectRecycleSurveyInfoList();
                if (surveyInfos.size()>0) {
                    surveyInfoService.deleteAll(surveyInfos);
                    map.put("msg","操作成功");
                    map.put("code",0);
                }else {
                    map.put("msg","数据为空");
                    map.put("code",0);
                }
            }
        }catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg","操作失败");
            map.put("code",1);
        }
        return map;
    }
    /**
     * 修改发布状态
     *
     * @param publishstatus
     * @param id
     * @return
     */
    @RequestMapping("/updatePublishStatus")
    public Object updatePublishStatus(Integer publishstatus, Integer id) {
        ModelMap map = new ModelMap();
        try {
            PageData pageData = new PageData(Tools.getRequest());
            String moneycard = pageData.getString("u_id");
            User user = userService.getUserByUserCode(moneycard);
            if (user == null){
                map.put("msg", "该账号暂无权限，有问题请联系管理员");
                map.put("code", 810);
            }else {
                surveyInfoService.updatePublishStatus(publishstatus, id);
                map.put("msg", "修改发布状态成功");
                map.put("code", 0);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "修改发布状态失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 查看问卷详情/预览问卷
     * @param id
     * @return
     */
    @RequestMapping("/getDetail")
    public Object getDetail(Integer id){
        ModelMap map=new ModelMap();
        try {
            SurveyInfo detail = surveyInfoService.getDetail(id);
            List<BatchJson> batchJsonList = getBatchJsons(detail,id);
            map.put("msg","查询成功");
            map.put("data",detail);
            map.put("batchJsonList",batchJsonList);
            map.put("code",0);
        }catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查看失败");
            map.put("code", 1);
        }
        return map;
    }

    private List<BatchJson> getBatchJsons(SurveyInfo detail,Integer id) {
        List<BatchJson> batchJsonList = JSONObject.parseArray(detail.getBatchjson(), BatchJson.class);
        List<SurveyQuestion> questionList = questionService.selectListByInfoId(id);
        List<SurveyOption> allOptions = questionService.selectAllOptions();
        for (int i=0;i<batchJsonList.size();i++){
            BatchJson batchJson= batchJsonList.get(i);
            SurveyQuestion question=questionList.get(i);
            batchJson.setQuestionid(question.getId());
            batchJson.setAnswer("");
            batchJson.setIswrite(question.getIswrite());
            batchJson.setQuestionorder(question.getQuestionorder());
            batchJson.setMultiple(question.getMultiple());
            batchJson.setMultipletext(question.getMultipletext());
            List<OptionInfo> optionList = JSONObject.parseArray(batchJson.getNav(), OptionInfo.class);
            if (optionList.size()>0) {
                for (int j = 0; j < optionList.size(); j++) {
                    List<SurveyOption> options = allOptions.stream().filter(o->o.getQuestionid().intValue() == question.getId().intValue()).collect(Collectors.toList());
                    if (options.size()>0) {
                        optionList.get(j).setId(options.get(j).getId());
                    }
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(optionList);
            batchJson.setNav(jsonArray.toString());
            batchJson.setOptionInfoList(optionList);
        }
        return batchJsonList;
    }

}
