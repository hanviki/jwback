package com.welb.organization_check.controller;

import com.welb.constant.MapContant;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.organization_check.wsdl.SmsService;
import com.welb.survey.entity.Answer;
import com.welb.survey.entity.SurveyInfo;
import com.welb.survey.service.IAnswerService;
import com.welb.survey.service.ISurveyInfoService;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import com.welb.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author luoyaozu
 * @title: SendMessage
 * @projectName xh-360appraisal-interface
 * @description: 发送短信接口
 * @date 2019/7/216:50
 */
@RestController
@RequestMapping("/sendMessage")
public class SendMessageController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Resource
    IUserService userService;
    @Resource
    IMonthSummaryService monthSummaryService;
    @Resource
    IScoreService scoreService;
    @Resource
    IMessageLogService logService;
    @Resource
    IManualSetTimeService setTimeService;
    @Autowired
    ISurveyInfoService surveyInfoService;
    @Autowired
    IAnswerService answerService;


    /**
     * 一键发送短信
     *
     * @return
     */
    @RequestMapping("/sendMessageAll")
    @Transactional
    public Object sendMessageAll(HttpServletRequest req, String templatecontent) throws ParseException {
        ModelMap map = new ModelMap();
        String loginUserCode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        String year = CalendarUtil.getYear();
        String month = CalendarUtil.getMonth();
       // String quarter = CalendarUtil.getQuarter(month);
        int count = Integer.parseInt(month) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();

            //手动考核-一键发送短信
            manualSendMessageAll(templatecontent, map, loginUserCode, year, month, count, sysTime);


        return map;
    }

    private void manualSendMessageAll(String templatecontent, ModelMap map, String loginUserCode,
                                      String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
        if (setTime != null) {

                //开始新的季度考核
                month = quarter;
                judgeSendMessage(map, setTime.getYear(), setTime.getMonth(), templatecontent, loginUserCode);

        }
    }

    private void automaticSendMessageAll(String templatecontent, ModelMap map, String loginUserCode, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastyear);
            month = "12";
            judgeSendMessage(map, year, month, templatecontent, loginUserCode);
        } else {
            month = String.valueOf(count);
            judgeSendMessage(map, year, month, templatecontent, loginUserCode);
        }
    }


    public void judgeSendMessage(ModelMap map, String year, String month,
                                 String templatecontent, String loginUserCode) {
        MessageLog log = new MessageLog();
        //授权发送用户名
        String userName = "360kh_zzb";
        //授权发送密码
        String userPassword = "360kh_zzb";
        //发送客户端MAC地址
        String userMAC = "";
        //SP标识
        String smsCode = "360kh_zzb";
        //接收短信的手机号
        String mobile = "";
        //短信发送的内容
        String content = "";
//        content = template.getTemplatecontent() + " http://106.54.21.154:8091/360check/#/webShowLogin";
        content = templatecontent + " http://119.97.220.235:9908/360check/#/webShowLogin";
        log.setMessagecontent(content);
        List<User> users = userService.findUserAll();
        //该集合存放没有发送短信的手机号码
        List<String> mobileList = new ArrayList<>();
        for (User user : users) {
            mobile = user.getMobile();
            MonthSummary summary = monthSummaryService.selectSummaryByYearAndMonthAndCode(year, month, user.getUsercode());
            if (summary.getIssend().equals("0") || summary == null) {

                if (!"".equals(mobile) && mobile != null) {
                    mobileList.add(mobile);
                    summary.setIssend("1");
                    monthSummaryService.updateByPrimaryKeySelective(summary);
                    log.setSendercode(loginUserCode);
                    log.setReceivercode(user.getUsercode());
                    String receivetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    log.setReceivetime(receivetime);
                    logService.insertSelective(log);
                }
            }
        }
        //数据总量
        int totalSize = mobileList.size();
        if (totalSize > 0) {
            //测试用
            int count = 0;
            //每100条数据为一组
            int toIndex = 100;
            for (int i = 0; i < totalSize; i += toIndex) {
                if (i + toIndex > totalSize) {//toIndex最后没有100条数据则剩余几条newList中就装几条
                    toIndex = totalSize - i;
                }
                List newList = mobileList.subList(i, i + toIndex);
                mobile = StringUtils.collectionToDelimitedString(newList, ",");
                //调用短信发送接口
                String service = new SmsService().getSmsServiceHttpPort().service(userName, userPassword, userMAC, smsCode, mobile, content);
                getMessAgeState(map, service);
                //测试返回的结果
                count++;
            }
            //测试返回的结果
            map.put("content", content);
            map.put("mobile", mobile);
            map.put("总人数", totalSize);
            map.put("最后一次短信发送了人数", toIndex);
            map.put("一共发送了", count + "次短信");
        } else {
            map.put("msg", "短信已经全部发送");
            map.put("code", 1);
        }
    }

    /**
     * 给所有评分人发送短信
     *
     * @return
     */
    @RequestMapping("/sendOneMessage")
    public Object sendOneMessage(HttpServletRequest req, String usercode, String templatecontent) throws ParseException {
        ModelMap map = new ModelMap();
        String loginUserCode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        //授权发送用户名
        String userName = "360kh_zzb";
        //授权发送密码
        String userPassword = "360kh_zzb";
        //发送客户端MAC地址
        String userMAC = "";
        //SP标识
        String smsCode = "360kh_zzb";
        //接收短信的手机号
        String mobile = "";
        //短信发送的内容
        String content = "";
        String year = CalendarUtil.getYear();
        String month = CalendarUtil.getMonth();
      //  String quarter = CalendarUtil.getQuarter(month);
        int count = Integer.parseInt(month) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();

            //手动考核-给所有评分人发送短信
        manualSendOneMessage(usercode, templatecontent, map, loginUserCode, userName, userPassword, userMAC, smsCode, content, year, month, count, sysTime);

        return map;
    }

    private void manualSendOneMessage(String usercode, String templatecontent, ModelMap map, String loginUserCode,
                                      String userName, String userPassword, String userMAC, String smsCode, String content, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
        if (setTime != null) {

                //开始新的季度考核
                month = quarter;
                sendToScorringCode(usercode, map, userName, userPassword, userMAC, smsCode, content, setTime.getYear(), setTime.getMonth(), loginUserCode, templatecontent);

        }
    }

    private void automaticSendOneMessage(String usercode, String templatecontent, ModelMap map, String loginUserCode, String userName, String userPassword, String userMAC, String smsCode, String content, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastyear);
            month = "12";
            sendToScorringCode(usercode, map, userName, userPassword, userMAC, smsCode, content, year, month, loginUserCode, templatecontent);

        } else {
            month = String.valueOf(count);
            sendToScorringCode(usercode, map, userName, userPassword, userMAC, smsCode, content, year, month, loginUserCode, templatecontent);
        }
    }

    private void sendToScorringCode(String usercode, ModelMap map, String userName, String userPassword, String userMAC,
                                    String smsCode, String content, String year, String month, String loginUserCode, String templatecontent) {
        String mobile = "";
        //通过被评分人code查找所有评分人
        List<Score> scores = scoreService.selectScoreByScorredCode(usercode);
        MessageLog log = new MessageLog();
        //测试用
        int index = 0;
        int count = scores.size();
        for (Score score : scores) {
            MonthSummary scorringSummary = monthSummaryService.selectSummaryByYearAndMonthAndCode(year, month, score.getScorringcode());
            if (scorringSummary != null) {
                if (scorringSummary.getIssend().equals("0")) {
                    scorringSummary.setIssend("1");
                    monthSummaryService.updateByPrimaryKeySelective(scorringSummary);
                    User user = userService.findUserByUserCode(scorringSummary.getEmployeecode());
                    mobile = user.getMobile();
                    if (!"".equals(mobile) && mobile != null) {
                        // hsc20210628
//                        content = template.getTemplatecontent() + " http://106.54.21.154:8091/360check/#/web?usercode=" + user.getUsercode();
                        content = templatecontent + "http://58.49.116.106:1099/#/web?usercode=" + user.getUsercode();
                        String service = new SmsService().getSmsServiceHttpPort().service(userName, userPassword, userMAC, smsCode, mobile, content);
                        getMessAgeState(map, service);
                        index++;
                        log.setReceivercode(user.getUsercode());
                        log.setSendercode(loginUserCode);
                        log.setMessagecontent(content);
                        log.setMessagecontent(content);
                        String receivetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        log.setReceivetime(receivetime);
                        logService.insertSelective(log);
                        map.put("content", content);
                    }
                }
            }
            count--;
        }

        if (count == 0) {
            map.put("code", 0);
            //测试返回的结果
            map.put("mobile", mobile);
            map.put("需要发送短信的总人数", scores.size());
            map.put("一共发送了", index + "次短信");
            map.put("content", content);
        }
        map.put("msg", "短信已经给所有评分人发送");
    }

    /**
     * 给单个用户发送短信
     *
     * @param usercode
     * @param templatecontent
     * @return
     */
    @RequestMapping("/sendMessAgeToUser")
    public Object sendMessAgeToUser(HttpServletRequest req, String usercode, String templatecontent) {
        ModelMap map = new ModelMap();
        String loginUserCode = (String) req.getSession().getAttribute("usercode");
        //授权发送用户名
        String userName = "360kh_zzb";
        //授权发送密码
        String userPassword = "360kh_zzb";
        //发送客户端MAC地址
        String userMAC = "";
        //SP标识
        String smsCode = "360kh_zzb";
        //接收短信的手机号
        String mobile = "";
        //短信发送的内容
        String content = "";
        User user = userService.findUserByUserCode(usercode);
        mobile = user.getMobile();
        if (!"".equals(mobile) && mobile != null) {
            //hsc20210628
//                content = template.getTemplatecontent() + " http://106.54.21.154:8091/360check/#/web?usercode=" + user.getUsercode();
            content = templatecontent + " http://58.49.116.106:1099/#/web?usercode=" + user.getUsercode();
            String service = new SmsService().getSmsServiceHttpPort().service(userName, userPassword, userMAC, smsCode, mobile, content);
            getMessAgeState(map, service);
            //添加短信发送日志记录
            MessageLog log = new MessageLog();
            log.setReceivercode(user.getUsercode());
            log.setSendercode(loginUserCode);
            log.setMessagecontent(content);
            String receivetime = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
            log.setReceivetime(receivetime);
            logService.insertSelective(log);
            //测试返回的结果
            map.put("content", content);
            map.put("mobile", mobile);
        } else {
            map.put("msg", "该用户没有手机号码，无法发送短信，请确认该用户是否已填写手机号码");
            map.put("code", 1);
        }
        return map;
    }

    private void getMessAgeState(ModelMap map, String service) {
        if (service.equals("0")) {
            map.put("msg", "发送成功");
            map.put("code", 0);
        } else if (service.equals("1")) {
            map.put("msg", "授权认证失败，您的授权发送用户名或授权发送密码错误");
            map.put("code", 1);
        } else if (service.equals("2")) {
            map.put("msg", " PS标识错误");
            map.put("code", 2);
        } else if (service.equals("3")) {
            map.put("msg", " 发送错误，请检查发送内容合法性");
            map.put("code", 3);
        }
    }


    /**
     * 给人事部各个部门长发送短信
     *
     * @return
     */
    @RequestMapping("sendMessageToRaterUsers")
    public Object sendMessageToRaterUsers(HttpServletRequest req, String templatecontent, String mobiles) {
        ModelMap map = new ModelMap();
        //授权发送用户名
        String userName = "360kh_zzb";
        //授权发送密码
        String userPassword = "360kh_zzb";
        //发送客户端MAC地址
        String userMAC = "";
        //SP标识
        String smsCode = "360kh_zzb";
        //接收短信的手机号
        String mobile = "";
        //短信内容
        String content = "";
        try {
            String loginUserCode = (String) req.getSession().getAttribute("usercode");
            String[] list = mobiles.split(",");
            List<String> mobileList = Arrays.asList(list);
            for (String mo : mobileList) {
                mobile = mo;
                content = templatecontent;
                String service = new SmsService().getSmsServiceHttpPort().service(userName, userPassword, userMAC, smsCode, mobile, content);
                getMessAgeState(map, service);
                //添加短信发送日志记录
                MessageLog log = new MessageLog();
                log.setSendercode(loginUserCode);
                log.setMessagecontent(content);
                String receivetime = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(new Date());
                log.setReceivetime(receivetime);
                logService.insertSelective(log);
            }
            map.put("msg", " 发送成功");
            map.put("content", content);
            map.put("size", mobileList);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", " 发送失败");
            map.put("code", 1);
        }
        return map;
    }

    @RequestMapping("/sendCheckCode")
    public Object sendCheckCode(String moneycard) {
        return this.loadCheckCode(moneycard, "360考核系统找回密码验证码");
    }

    @RequestMapping("/sendVoteCheckCode")
    public Object sendVoteCheckCode(String uId, String phoneNum, String surveyInfoId) {
        ModelMap map = new ModelMap();
        User user = userService.selectUserByMoneyCard(uId);
        if (user == null || !(user.getMoneycard().equals(uId) && user.getMobile().equals(phoneNum))){
            map.put("msg", "该用户的发薪号与手机号不匹配，请确认");
            map.put("code", 1);
        }else {
            //查询是否已经有人答题
            List<Answer> answerList = answerService.selectAnswerByMoneyCardAndInfoId(Integer.valueOf(surveyInfoId), uId);
            SurveyInfo surveyInfo = surveyInfoService.selectByPrimaryKey(Integer.valueOf(surveyInfoId));
            if (surveyInfo.getAnswercount() > 0 && answerList != null && !answerList.isEmpty()){
                map.put("msg", "该问卷您已答过,不能重复提交");
                map.put("code", 1);
            }else {
                map = this.loadCheckCode(uId, "360考核系统投票验证码");
                map.put("msg", "查询成功");
                map.put("code", 0);
            }
        }
        return map;
    }

    private ModelMap loadCheckCode(String moneycard, String itemName) {
        ModelMap map = new ModelMap();
        //授权发送用户名
        String userName = "360kh_zzb";
        //授权发送密码
        String userPassword = "360kh_zzb";
        //发送客户端MAC地址
        String userMAC = "";
        //SP标识
        String smsCode = "360kh_zzb";
        //接收短信的手机号
        String mobile = "";
        //短信内容
        String content = "";
        try {

            User user = userService.getUserByMoneyCard(moneycard);
            if (!user.getMobile().equals("") || user.getMobile() != null) {//判断手机号是否为空
                Object countObject = MapContant.getMap(moneycard + "count");
                int count;
                if (countObject == null) {//初始化短信发送次数  默认一天最多发送五次
                    MapContant.setMap(user.getMoneycard() + "count", MapContant.count);//短信发送次数
                }
                Object firstDayObject = MapContant.getMap(user.getMoneycard() + "day");
                if (firstDayObject == null) {//初始化时间戳
                    MapContant.setMap(user.getMoneycard() + "day", Long.parseLong(DateUtil.getDays()));
                }
                count = (int) MapContant.getMap(moneycard + "count");//发送短信次数
                long day = (long) MapContant.getMap(moneycard + "day");//每天只能发送5次短信，第二天恢复短信发送次数
                long nextDay = Long.parseLong(DateUtil.getDays());
                if (nextDay - day > 0) {
                    MapContant.setMap(user.getMoneycard() + "day",nextDay);//清除时间戳
                    count = MapContant.count;
                    //是否重发短信
                    Object messageTime = MapContant.getMap(moneycard + "message_time");//重发短信有效时间
                    judgeMessageTimeIsNull(moneycard, map, userName, userPassword, userMAC, smsCode, mobile, content, user, messageTime, count, itemName);
                } else {
                    if (count > 0) {
                        //是否重发短信
                        Object messageTime = MapContant.getMap(moneycard + "message_time");//重发短信有效时间
                        judgeMessageTimeIsNull(moneycard, map, userName, userPassword, userMAC, smsCode, mobile, content, user, messageTime, count, itemName);
                    } else {
                        map.put("msg", "短信发送次数今天已达上限，请明日再来");
                        map.put("code", 1);
                    }
                }
            } else {
                map.put("msg", "该用户未检测到手机号,如有疑问,请联系管理员");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", LogUtil.getTrace(e));
            map.put("msg", " 发送失败");
            map.put("code", 1);
        }
        return map;
    }

    private void judgeMessageTimeIsNull(String moneycard, ModelMap map, String userName, String userPassword, String userMAC, String smsCode, String mobile, String content, User user, Object messageTime, int count, String preStr) {
        if (messageTime == null) {
            MapContant.setMap(user.getMoneycard() + "count", count - 1);//短信发送剩余次数
            getCheckCode(user, map, userName, userPassword, userMAC, smsCode, mobile, content, preStr);
        } else {
            long endTime = System.currentTimeMillis();//当前时间戳
            int time = (int) (57 - ((endTime - (long) messageTime) / 1000));
            if (time > 0) {
                map.put("msg", "请" + time + "秒后重新发送短信");
                map.put("code", 1);
            } else {
                MapContant.deleteMap(moneycard);
                MapContant.deleteMap(moneycard + "time");
                MapContant.deleteMap(moneycard + "message_time");
                MapContant.setMap(user.getMoneycard() + "count", count - 1);//短信发送剩余次数
                getCheckCode(user, map, userName, userPassword, userMAC, smsCode, mobile, content, preStr);
            }
        }
    }

    private void getCheckCode(User user, ModelMap map, String userName, String userPassword, String userMAC, String smsCode, String mobile, String content, String preStr) {
        String checkCode = StringUtil.generateRandomNum();
        //将验证码和存入验证码的时间戳存到map常量集合中
        MapContant.setMap(user.getMoneycard(), checkCode);//验证码
        MapContant.setMap(user.getMoneycard() + "time", System.currentTimeMillis());//验证码有效时间 5分钟
        MapContant.setMap(user.getMoneycard() + "message_time", System.currentTimeMillis());//重发短信有效时间  1分钟
        content = preStr + "：" + checkCode + ",有效时间 5 分钟，请勿泄露给他人";
        mobile = user.getMobile();
        //调用短信发送接口 -- 部署正式环境打开
        String service = new SmsService().getSmsServiceHttpPort().service(userName, userPassword, userMAC, smsCode, mobile, content);
        getMessAgeState(map, service);
        //测试返回数据  部署正式环境删掉
      /*  map.put("msg", "发送成功");
        map.put("验证码", checkCode);
        map.put("content", content);
        map.put("mobile", mobile);
        map.put("code", 0);*/
    }
}
