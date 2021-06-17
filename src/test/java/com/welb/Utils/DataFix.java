package com.welb.Utils;

import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.service.EvaluationClinicalScoreService;
import com.welb.medicalEthics.service.EvaluationClinicalService;
import com.welb.medicalEthics.service.EvaluationNonClinicalService;
import com.welb.medicalEthics.service.MedicalEthicsMsgService;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IUserService;
import com.welb.organization_check.service_impl.UserRoleServiceImpl;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.personnel_check.service.IPersonnelUserService;
import com.welb.sysBase.service.EvaluationDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.service.IPermissionService;
import com.welb.sysBase.util.Constant;
import com.welb.util.MD5;
import com.welb.util.PageData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/17
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class DataFix {

    @Autowired
    MedicalEthicsMsgService msgService;

    @Autowired
    EvaluationNonClinicalService evaluationNonClinicalService;

    @Autowired
    EvaluationClinicalService clinicalService;

    @Autowired
    EvaluationClinicalScoreService scoreService;

    @Autowired
    IPersonnelUserService personnelUserService;

    @Autowired
    private HUserService personneService;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private MedicalEthicsMsgService medicalEthicsMsgService;

    @Autowired
    private EvaluationDepartmentService departmentService;

    @Autowired
    private EvaluationDepartmentUserService departmentUserService;


    /**
     * 非临床用户填写了临床信息的用户
     */
    @Test
    @Transactional
    public void testWrongData(){
//        Map<String,String> param = new HashMap<>();
//        //查询所有的非临床用户
//        param.put("personType","1");
//        List<MedicalEthicsMsg> msgList = msgService.selectAll(param);
//        List<NameCodeVo> resList = new ArrayList<>();
//        msgList.forEach(m->{
//            //存在临床表单的 非临床用户
//            EvaluationClinical evaluationClinical = clinicalService.selectByUserId(m.getUserId());
//            if(evaluationClinical != null){
//                NameCodeVo name = new NameCodeVo();
//                name.setUsername(m.getUserName());
//                name.setUserCode(m.getUserId());
//                resList.add(name);
//                clinicalService.deleteByUserId(m.getUserId());
//                scoreService.deleteByUserId(m.getUserId());
//            }
//        });
    }

    /**
     * 用户电话修复
     */
    @Test
    public void handleLoginUser(){
        //所有已经提交的用户id
        List<String> list = msgService.allIdList();
        //查找所有的登录用户
        List<String> allUserIdList = personnelUserService.allUserIds();
        list.forEach(user->{
            if(allUserIdList.contains(user)){
                allUserIdList.remove(user);
            }else{
                PersonnelUser hUser = personneService.selectByUserId(String.valueOf(user));

                User newUser = new User();
                newUser.setUsername(hUser.getUsername());
                newUser.setPassword(MD5.md5(INITIAL_PASSWORD));
                newUser.setUserstate("1");
                newUser.setRoletype(Constant.USER_TYPE_MEDICAL_ETHICS);
                newUser.setSex(SEX_MALE.equals(hUser.getSex()) ? "1" : "2");
                newUser.setMoneycard(hUser.getId().toString());
                newUser.setMobile(hUser.getPhone());
                newUser.setUsercode(newUser.getUsercode());
                userService.insertSelective(newUser);
                UserRoleKey userRoleKey = new UserRoleKey();
                userRoleKey.setUsercode(newUser.getUsercode());
                userRoleKey.setRolecode(Constant.MEDICAL_ETHICS_USER_ROLE);
                List<UserRoleKey> userRoleKeys = userRoleService.selectUserRoleByUserCode(user);
                if(!userRoleKeys.contains(userRoleKey)){
                    userRoleService.insertSelective(userRoleKey);
                }
            }
        });
    }


    /**
     * 同步h_user电话到user表
     */
    @Test
    public void syncPhoneNumber(){
            List<PageData> hUser = permissionService.showAllUser(new HashMap<>());
            List<PageData> user =  permissionService.showUser(new HashMap<>());
            for (PageData h: hUser) {
                for (PageData u: user) {
                    if (h.getString("u_id").equals(u.getString("MoneyCard"))){
                        permissionService.updatePhone(h.getString("u_id"), h.getString("u_phone"));
                    }
                }
            }
    }

    /**
     * 用户部门自动分配
     */
    @Test
    public void autoDepartment(){
        //查询所有的已经提交的用户
        List<MedicalEthicsMsg> allUser = medicalEthicsMsgService.selectAll(null);
        try {
            departmentUserService.autoDepartment(allUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动修复用户不存在
     */
    @Test
    public void autoAddMsgToUser(){
        List<MedicalEthicsMsg> missingUsers = medicalEthicsMsgService.showMissingUsers();
        List<String> userIdList = missingUsers.stream().map(u -> u.getUserId()).collect(Collectors.toList());
        List<PageData> hUser = permissionService.showUserByUserId(userIdList);
        for (MedicalEthicsMsg medicalEthicsMsg: missingUsers) {
            String userId = medicalEthicsMsg.getUserId();
            PageData curUser = hUser.stream().filter(u -> u.getString("u_id").equals(userId)).collect(Collectors.toList()).get(0);
            User user = new User();
            //检查邮箱是否合法
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex  = Pattern.compile(check);
            String email = curUser.getString("u_email");
            Matcher matcher = regex.matcher(email);
            boolean isMatched = matcher.matches();
            if(isMatched){
                user.setEmail(email);
            }else{
                user.setEmail("");
            }
            user.setUsername(curUser.getString("u_name"));
            user.setPassword(MD5.md5(INITIAL_PASSWORD));
            user.setMobile(curUser.getString("u_phone"));
            user.setNation(curUser.getString("u_nation"));
            user.setMoneycard(curUser.getString("u_id"));
            user.setSex(SEX_MALE.equals(curUser.getString("u_sex")) ? "1" : "2");
            //默认给为问卷调查类型(0-组织部  1-人事部  2-调查问卷' 3-医德医风)
            user.setRoletype(USER_TYPE_MEDICAL_ETHICS);
            userService.insertSelective(user);
            //user_role
            UserRoleKey userRoleKey = new UserRoleKey();
            userRoleKey.setRolecode(MEDICAL_ETHICS_USER_ROLE);
            //增加新用户的医德医风权限
            userRoleKey.setUsercode(user.getUsercode());
            userRoleService.insertSelective(userRoleKey);
        }

        System.out.println("Fix finish!");
    }

    /**
     * 自动修复用户缺少医德医风权限数据
     */
    @Test
    public void autoAddMsgToUserRole(){
        List<MedicalEthicsMsg> allMsgUser = medicalEthicsMsgService.selectAll(null);
        List<PageData> allUser =  permissionService.showUser(new HashMap<>());
        List<PageData> missUser = new ArrayList<>();
        for (MedicalEthicsMsg msg :allMsgUser) {
            for (PageData pd: allUser) {
                if (msg.getUserId().equals(pd.getString("MoneyCard"))){
                    missUser.add(pd);
                }
            }
        }

//        List<PageData> missRoleKey = new ArrayList<>();
        for (PageData pageData:missUser) {
            String userCode = pageData.getString("UserCode");
            List<UserRoleKey> roleKeyList = userRoleService.selectUserRoleByUserCode(userCode);
            List<String>  roleCode = roleKeyList.stream().map(r -> r.getRolecode()).collect(Collectors.toList());
            if (!roleCode.contains(MEDICAL_ETHICS_USER_ROLE)){
                UserRoleKey userRoleKey = new UserRoleKey();
                userRoleKey.setUsercode(userCode);
                userRoleKey.setRolecode(MEDICAL_ETHICS_USER_ROLE);
                userRoleService.insertSelective(userRoleKey);
//                missRoleKey.add(pageData);
            }
        }

//        System.out.println("missRoleKey.size() = " + missRoleKey.size());
//        missRoleKey.stream().forEach(m-> System.out.println(m.getString("MoneyCard") + "  - " + m.getString("UserName")));
        System.out.println("Fix finish!");
    }
}
