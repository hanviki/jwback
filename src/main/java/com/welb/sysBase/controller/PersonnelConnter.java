package com.welb.sysBase.controller;

import com.welb.constant.MapContant;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.service.BatchService;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.HttpUtils;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import com.welb.util.PageData;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 2018-7-27
 *
 * @author LCL
 */
@RestController
@RequestMapping("/personnel")
public class PersonnelConnter extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(PersonnelConnter.class);

    @Autowired
    private HUserService service;

    @Autowired
    private BatchService batchService;

    @Autowired
    private HUserService personneService;

    @Autowired
    IUserService userService;

    /**
     * 人事数据同步
     * 部门需要特殊处理（就是原本从hrp同步过来的人员科室的末级 与 48个大科室做一个映射关系，
     * 然后我们科研系统和学科系统都用大科室扁平化管理）
     * 难点就是 这个映射要做好 需要协和的老师配合
     *
     * @return
     */
    @RequestMapping(value = "/ToUpdate")
    public Object ToUpdate() {
        int upd = 0;
        int add = 0;
        int del = 0;
        ModelMap map = new ModelMap();
        try {
            HttpUtils weath = new HttpUtils();
            String date = DateUtil.getDays();// 获取今天的日期
            //2.同步人事信息
            logger.info("开始获取人事信息");
            String[] len2;
            long start = System.currentTimeMillis();
            /**   获取h_distribution表的数据  */
            len2 = getPersonnel(weath, date);
            long end = System.currentTimeMillis();
            logger.info("获取人事信息完成,耗时" + (end - start) + "ms");
            Msg msg = new Msg(upd, add, del).invoke(len2);
            map.put("msg", "同步成功");
            map.put("code", 0);
        } catch (Exception e) {
            map.put("msg", "同步失败");
            map.put("code", 1);
            map.put("error", e.getStackTrace());
            logger.error(LogUtil.getTrace(e));

        }
        return ajaxJson(map);
    }

    private String[] getPersonnel(HttpUtils weath, String date) throws DocumentException {
        String weather = weath.sendGet("http://192.168.64.200/SAPService/HRService.asmx/Get_HR_KYXX?DATE=" + date + "&destinationName=P01_800");
        Document doc = DocumentHelper.parseText(weather);
        Element root = doc.getRootElement();
        String arr = root.getText();
        String[] len = arr.split("\\|");
        return len;
    }

    private class Msg {
        private int upd;
        private int add;
        private int del;

        public Msg(int upd, int add, int del) {
            this.upd = upd;
            this.add = add;
            this.del = del;
        }

        public int getUpd() {
            return upd;
        }

        public int getAdd() {
            return add;
        }

        public int getDel() {
            return del;
        }

        /**
         * 开始更新人事信息到数据库
         */
        public Msg invoke(String[] len) {
            //只同步h_user表
            List<PageData> updateList = new ArrayList<>();
            List<PageData> insertList = new ArrayList<>();
            List<String> allIds = service.findAllUids();//本地 发薪号
            List<String> hrpIds = new ArrayList<>();//hrp 发薪号
            List<PageData> delList = new ArrayList<>();//要删除的发薪号
            for (int i = 0; i < len.length; i++) {
                PageData pd = new PageData();
                String[] as = len[i].split(",");
                for (int j = 0; j < as.length; j++) {
                    pd.put("pd" + j, as[j]);
                }
                hrpIds.add(pd.getString("pd0"));
                PageData is2 = service.findUid2(pd.getString("pd0"));// 判断人事信息是否存在
                if (is2 == null) {
                    insertList.add(pd);
                } else {
                    updateList.add(pd);
                }
            }
            //这里处理删除逻辑
            for (int i = 0; i < allIds.size(); i++) {
                if (!hrpIds.contains(allIds.get(i))) {//如果hrp中不包括本地的发薪号就删除掉本地的
                    PageData pageData = new PageData();
                    pageData.put("u_id", allIds.get(i));
                    delList.add(pageData);
                    del++;
                }
            }
            if (!delList.isEmpty()) {//删除非hrp中的本地h_user的数据
                batchService.doBatch(delList, "com.welb.sysBase.mapper.PerconneMapper.delDistribution");
            }
            if (!updateList.isEmpty()) {//根据hrp获取的信息,更新本地h_user中的其他字段信息
                batchService.doBatch(updateList, "com.welb.sysBase.mapper.PerconneMapper.upd2");
            }
            if (!insertList.isEmpty()) {//增加hrp中的新用户到h_user
                batchService.doBatch(insertList, "com.welb.sysBase.mapper.PerconneMapper.save2");
            }
//            personneService.updateD_upper_id();//更新大科室
            return this;
        }
    }

    /**
     * 通过发薪号查找用户信息--针对忘记密码查询使用
     *
     * @param uId
     * @return
     */
    @RequestMapping("/getUserByMoneyCard")
    public Object getUserByMoneyCard(String uId) {
        ModelMap map = new ModelMap();
        try {
            User user = userService.getUserByMoneyCard(uId);
            if (user == null) {
                map.put("msg", "该用户不存在");
                map.put("code", 1);
            } else if (user.getUserstate().equals("0")) {
                map.put("msg", "该用户已被停用,如有疑问,请联系管理员");
                map.put("code", 1);
            } else {
                map.put("msg", "查询用户成功");
                map.put("data", user);
                map.put("code", 0);
            }
        } catch (Exception e) {
            map.put("msg", "查询用户失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 通过发薪号查看用户是否存在
     *
     * @param uId
     * @return
     */
    @RequestMapping("/findUserByUId")
    public Object findUserByUId(String uId, String checkCode) {
        ModelMap map = new ModelMap();
        try {
            String code = (String) MapContant.getMap(uId);//获取存放在map常量集合中的验证码
            Object startTime = MapContant.getMap(uId + "time");//获取存放在map常量集合中的验证码有效期
            if (startTime == null) {
                //删除map集合常量
                deleteMap(uId, map);
            } else {
                long endTime = System.currentTimeMillis();//当前时间戳
                int time = (int) (300 - ((endTime - (long) startTime) / 1000));//计算密码填写有效时间
                if (time > 0) {
                    if (code.equals(checkCode)) {
                        //清除验证码信息
                        MapContant.deleteMap(uId);
                        MapContant.deleteMap(uId + "time");
                        map.put("msg", "查询成功");
                        map.put("code", 0);
                    } else {
                        map.put("msg", "验证码输入错误,请检查并重新填写验证码");
                        map.put("code", 1);
                    }
                } else {
                    deleteMap(uId, map);
                }
            }
        } catch (Exception e) {
            map.put("error", e.getMessage());
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return map;
    }

    private void deleteMap(String moneycard, ModelMap map) {
        MapContant.deleteMap(moneycard);
        MapContant.deleteMap(moneycard + "time");
        MapContant.deleteMap(moneycard + "message_time");
        map.put("msg", "验证码已过期,请重新发送短信");
        map.put("code", 1);
    }

    /**
     * 通过主键(发薪号)查找hrp用户数据
     *
     * @param uId
     * @return
     */
    @RequestMapping("/findHrpUserById")
    public Object findHrpUserById(String uId) {
        ModelMap map = new ModelMap();
        try {
            PageData hrpUser = personneService.findUid2(uId);
            if (hrpUser == null) {
                map.put("msg", "暂无数据");
                map.put("code", 0);
            } else {
                map.put("data", hrpUser);
                map.put("msg", "查询成功");
                map.put("code", 0);
            }

        } catch (Exception e) {
            logger.error(LogUtil.getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }

        return map;


    }

}
