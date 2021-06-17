package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.dto.CalculateDto;
import com.welb.medicalEthics.entity.*;
import com.welb.medicalEthics.mapper.PartyBranchRelationsMapper;
import com.welb.medicalEthics.service.CalculateService;
import com.welb.medicalEthics.service.MedicalEthicsMsgService;
import com.welb.medicalEthics.service.MedicalEthicsUserService;
import com.welb.medicalEthics.service.PartyBranchRelationsService;
import com.welb.medicalEthics.dto.CurrentExcellentDto;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IUserService;
import com.welb.organization_check.service_impl.UserRoleServiceImpl;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.Constant;
import com.welb.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

/**
 * 党组织管理
 *
 * @author lx
 */
@Service
public class PartyBranchRelationsImpl implements PartyBranchRelationsService {

    @Autowired(required = false)
    private PartyBranchRelationsMapper mapper;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private HUserService personneService;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private MedicalEthicsMsgService msgService;

    @Autowired
    private CalculateService calculateService;

    /**
     * list 点击左侧加载右侧列表
     *
     * @param params 目前参数为parentId 父级id
     * @return {@link List<PartyBranchRelations>}
     */
    @Override
    public List<PartyBranchRelations> list(Map<String, String> params) {
        List<PartyBranchRelations> resList = mapper.list(params);
        List<PartyBranchRelations> voList = new ArrayList<>();
        resList.forEach(d -> {
            voList.add(extendBranchInfo(d));
        });
        return voList;
    }

    /**
     * 统计科室的相关信息
     *
     * @param params
     * @return
     */
    @Override
    public List<PartyBranchRelations> listDetail(Map<String, String> params) {
        List<PartyBranchRelations> resList = mapper.list(params);
        List<PartyBranchRelations> voList = new ArrayList<>();
        resList.forEach(d -> {
            voList.add(extendResultInfo(d));
        });
        return voList;
    }

    @Override
    public void batchUpdate(List<PartyBranchRelations> dataSource) {
        mapper.batchUpdate(dataSource);
    }

    @Override
    public Tree<PartyBranchRelations> tree(Map<String, String> params) {
        List<Tree<PartyBranchRelations>> trees = new ArrayList<>();
        List<PartyBranchRelations> partyBranchRelationsList = mapper.list(params);
        for (PartyBranchRelations partyBranchRelations : partyBranchRelationsList) {
            Tree<PartyBranchRelations> tree = new Tree<>();
            tree.setValue(String.valueOf(partyBranchRelations.getId()));
            tree.setParentId(String.valueOf(partyBranchRelations.getParentId()));
            tree.setLabel(partyBranchRelations.getRelationsName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为-1，根据数据库实际情况调整
        return BuildTree.build(trees);
    }

    @Override
    public Tree<PartyBranchRelations> partyBranchRelations() {
        List<Tree<PartyBranchRelations>> trees = new ArrayList<>();
        List<PartyBranchRelations> partyBranchRelationsList = mapper.parentList();
        for (PartyBranchRelations partyBranchRelations : partyBranchRelationsList) {
            Tree<PartyBranchRelations> tree = new Tree<>();
            tree.setValue(String.valueOf(partyBranchRelations.getId()));
            tree.setParentId(String.valueOf(partyBranchRelations.getParentId()));
            tree.setLabel(partyBranchRelations.getRelationsName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为-1，根据数据库实际情况调整
        return BuildTree.build(trees);
    }


    /**
     * getBranchTree 加载左侧党委树
     *
     * @return {@link List<PartyBranchRelations>}
     */
    @Override
    public List<PartyBranchRelations> getBranchTree(Map<String, String> params) {
        List<PartyBranchRelations> list = list(params);
        PartyBranchRelations root = new PartyBranchRelations();
        root.setId(-1);
        root.setParentId(0);
        root.setRelationsName("所有党组织");
        root.setIsDelete(0);
        root.setLevel(0);
        list.add(root);
        return PartyBranchTree.getTree(list);
    }

    /**
     * extendBranchInfo 填充 parentName属性
     *
     * @param data 原始数据
     * @return {@link PartyBranchRelations} 扩充后的数据
     */
    private PartyBranchRelations extendBranchInfo(PartyBranchRelations data) {
        if (!Constant.BRANCH_TREE_ROOT.equals(data.getParentId())) {
            data.setParentRelationName(selectById(data.getParentId()).getRelationsName());
        }
        return data;
    }

    /**
     * 扩展分数信息
     *
     * @param data
     * @return
     */
    private PartyBranchRelations extendResultInfo(PartyBranchRelations data) {
        if (!Constant.BRANCH_TREE_ROOT.equals(data.getParentId())) {
            data.setParentRelationName(selectById(data.getParentId()).getRelationsName());
        }
        Map<String, String> cliParams = new HashMap<>();
        String branId = data.getId().toString();
        cliParams.put("branchId", branId);
        cliParams.put("year", CURRENT_YEAR);
        CalculateDto cliResult = calculateService.cliResultCalculate(cliParams);
        CalculateDto nonCliResult = calculateService.nonCliResultCalculate(cliParams);

        //统计总人数
        int levelOneCount = cliResult.getTotalLevelOneCount() + nonCliResult.getTotalLevelOneCount();
        int levelTwoCount = cliResult.getTotalLevelTwoCount() + nonCliResult.getTotalLevelTwoCount();
        int levelThreeCount = cliResult.getTotalLevelThreeCount() + nonCliResult.getTotalLevelThreeCount();
        int levelFourCount = cliResult.getTotalLevelFourCount() + nonCliResult.getTotalLevelFourCount();
        int totalCount = calculateService.selectPartyCount(branId);
        int notFinishedCount = totalCount-levelOneCount-levelTwoCount-levelThreeCount-levelFourCount;
        if(totalCount != 0){
            DecimalFormat df = new DecimalFormat("0.00");
            double levelOnePercent = Double.parseDouble(df.format(((float)levelOneCount / totalCount)*100 ));
            double levelTwoPercent = Double.parseDouble(df.format(((float)levelTwoCount / totalCount)*100));
            double levelThreePercent = Double.parseDouble(df.format(((float)levelThreeCount / totalCount)*100));
            double levelFourPercent = Double.parseDouble(df.format(((float)levelFourCount / totalCount)*100));
            double tmp = levelOnePercent + levelTwoPercent + levelThreePercent+levelFourCount;
            double notFinishedPercent =  Double.parseDouble(df.format(((float)notFinishedCount / totalCount)*100));

            data.setTotalCount(totalCount);
            data.setTotalLevelOneCount(levelOneCount);
            data.setTotalLevelTwoCount(levelTwoCount);
            data.setTotalLevelThreeCount(levelThreeCount);
            data.setTotalLevelFourCount(levelFourCount);
            data.setNotFinishedCount(notFinishedCount);

            data.setLevelOnePercent(levelOnePercent);
            data.setLevelTwoPercent(levelTwoPercent);
            data.setLevelThreePercent(levelThreePercent);
            data.setLevelFourPercent(levelFourPercent);
            data.setNotFinishedPercent(notFinishedPercent);
        }else{
            data.setTotalCount(0);
            data.setTotalLevelOneCount(0);
            data.setTotalLevelTwoCount(0);
            data.setTotalLevelThreeCount(0);
            data.setTotalLevelFourCount(0);

            data.setLevelOnePercent(0.00);
            data.setLevelTwoPercent(0.00);
            data.setLevelThreePercent(0.00);
            data.setLevelFourPercent(0.00);
        }


        return data;
    }


    /**
     * insert新增党委信息
     *
     * @param partyBranchRelations partyBranchRelations
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(PartyBranchRelations partyBranchRelations) {
        Integer leaderUserId = partyBranchRelations.getLeaderUserId();
        String leaderName = partyBranchRelations.getLeaderName();
        //如果没有指定书记,直接插入基本信息
        if (leaderUserId == null) {
            mapper.insert(partyBranchRelations);
        } else {
            //指定了书记
            //检查该用户是否存在user表--登录
            User user = userService.selectUserByMoneyCard(leaderUserId.toString());
            String userCode;
            //用户表不存在,新建user
            if (user == null) {
                //从h_user获取基本信息
                PersonnelUser hUser = personneService.selectByUserId(String.valueOf(leaderUserId));
                User newUser = new User();
                newUser.setUsername(hUser.getUsername());
                newUser.setPassword(MD5.md5(INITIAL_PASSWORD));
                newUser.setUserstate("1");
                newUser.setRoletype(Constant.USER_TYPE_MEDICAL_ETHICS);
                newUser.setSex(hUser.getSex());
                newUser.setMoneycard(hUser.getId().toString());
                newUser.setUsercode(leaderUserId.toString());
                userCode = leaderUserId.toString();
                userService.add(newUser);
            } else {
                userCode = user.getUsercode();
            }
            //检查userRole表是否存在信息,不存在新增userRole信息
            UserRoleKey userRoleKey = new UserRoleKey();
            userRoleKey.setUsercode(userCode);
            userRoleKey.setRolecode(Constant.MEDICAL_ETHICS_USER_ROLE);
            List<UserRoleKey> userRoleKeys = userRoleService.selectUserRoleByUserCode(userCode);
            if (!userRoleKeys.contains(userRoleKey)) {
                userRoleService.insertSelective(userRoleKey);
            }
            //处理考核用户-角色表
            MedicalEthicsUserRole medicalEthicsUserRole = new MedicalEthicsUserRole();
            //新建考核用户
            MedicalEthicsUser medicalEthicsUser = new MedicalEthicsUser();
            medicalEthicsUser.setUserId(String.valueOf(leaderUserId));
            medicalEthicsUser.setUserName(leaderName);
            Integer level = partyBranchRelations.getLevel();
            medicalEthicsUserRole.setPartyId(partyBranchRelations.getId());
            medicalEthicsUserRole.setUserId(leaderUserId.toString());
            if (Constant.LEVEL2.equals(level)) {
                //党总支
                medicalEthicsUserRole.setRoleId(5);
                medicalEthicsUserRole.setPartyLevel(2);
                medicalEthicsUser.setGeneralBranchId(partyBranchRelations.getId());
                medicalEthicsUser.setPartyCommitteesId(partyBranchRelations.getParentId());
            } else if (Constant.LEVEL3.equals(level)) {
                //支部
                medicalEthicsUserRole.setRoleId(2);
                medicalEthicsUserRole.setPartyLevel(3);
                medicalEthicsUser.setBranchId(partyBranchRelations.getId());
                medicalEthicsUser.setGeneralBranchId(partyBranchRelations.getParentId());
                PartyBranchRelations parent = mapper.selectById(partyBranchRelations.getParentId());
                medicalEthicsUser.setPartyCommitteesId(parent.getParentId());
            } else if (Constant.LEVEL1.equals(level)) {
                //党委
                medicalEthicsUserRole.setRoleId(6);
                medicalEthicsUserRole.setPartyLevel(1);
                medicalEthicsUser.setPartyCommitteesId(partyBranchRelations.getId());
            }
            mapper.insert(partyBranchRelations);
            //得到新的partyId
            Integer partyId = partyBranchRelations.getId();
            medicalEthicsUserRole.setPartyId(partyId);
            //提交userRole
            medicalEthicsUserService.addUserRole(medicalEthicsUserRole);
            //提交user
            medicalEthicsUserService.addUser(medicalEthicsUser);
        }
    }

    /**
     * deleteById 根据id逻辑删除
     *
     * @param id 支部id
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Integer id) {
        if (checkDeleted(id)) {
            PartyBranchRelations partyBranchRelations = mapper.selectById(id);
            partyBranchRelations.setIsDelete(Constant.DELETED);
            try {
                update(partyBranchRelations);
                Map<String, String> deleteParams = new HashMap<>();
                deleteParams.put("partyId", id.toString());
                deleteParams.put("roleId", "2");
                medicalEthicsUserService.deleteUserRoleByParams(deleteParams);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * update 党支部信息更新,包含党委信息
     *
     * @param partyBranchRelations partyBranchRelations
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PartyBranchRelations partyBranchRelations) {
        Integer partyId = partyBranchRelations.getId();
        Map<String, String> deleteParams = new HashMap<>();
        //一级对应党委书记,二级对应总支书记,三级对应支部书记的角色
        String roleId = "";
        if (partyBranchRelations.getLevel() == 3) {
            roleId = "2";
        }
        if (partyBranchRelations.getLevel() == 2) {
            roleId = "5";
        }
        if (partyBranchRelations.getLevel() == 1) {
            roleId = "6";
        }
        deleteParams.put("roleId", roleId);
        deleteParams.put("partyId", String.valueOf(partyId));
        //删除支部角色
        Integer leaderUserId = partyBranchRelations.getLeaderUserId();
        String leaderName = partyBranchRelations.getLeaderName();
        PartyBranchRelations oldData = mapper.selectById(partyId);
        ;
        //书记是否存在,不存在直接update
        //未指定书记
        if (leaderUserId == null) {
            //如果原来存在书记,删除掉书记的角色
            Integer oldDataLeaderUserId = oldData.getLeaderUserId();
            if (oldDataLeaderUserId != null) {
                deleteParams.put("roleId", "2");
                medicalEthicsUserService.deleteUserRoleByParams(deleteParams);
            }
            mapper.update(partyBranchRelations);
        } else {
            //指定了书记
            Integer oldLeaderUserId = oldData.getLeaderUserId();
            if (!(oldLeaderUserId != null && partyBranchRelations.getLeaderUserId() != null && oldLeaderUserId.equals(partyBranchRelations.getLeaderUserId()))) {
                //新旧书记不是同一人,删除掉原来书记的角色
                Map<String, String> m2 = new HashMap<>();
                m2.put("userId", String.valueOf(oldLeaderUserId));
                m2.put("partyId", String.valueOf(partyId));
                m2.put("roleId", roleId);
                medicalEthicsUserService.deleteUserRoleByParams(m2);
                //指定了新的书记修改的信息,否则只需直接更新支部基础信息
                if (personneService.checkIdAndName(leaderUserId, leaderName)) {
                    //用户名发薪号合法
                    //检查User表,userRole表是否存在
                    User user = userService.selectUserByMoneyCard(leaderUserId.toString());
                    //新增user,从h_user获取基本信息
                    PersonnelUser hUser = personneService.selectByUserId(String.valueOf(leaderUserId));
                    String userCode;
                    if (user == null) {
                        User newUser = new User();
                        newUser.setUsername(hUser.getUsername());
                        newUser.setPassword(MD5.md5(INITIAL_PASSWORD));
                        newUser.setUserstate("1");
                        newUser.setRoletype(Constant.USER_TYPE_MEDICAL_ETHICS);
                        newUser.setSex(SEX_MALE.equals(hUser.getSex()) ? "1" : "2");
                        newUser.setMoneycard(hUser.getId().toString());
                        newUser.setUsercode(leaderUserId.toString());
                        newUser.setMobile(hUser.getPhone());
                        userCode = leaderUserId.toString();
                        userService.add(newUser);
                    } else {
                        userCode = user.getUsercode();
                    }
                    UserRoleKey userRoleKey = new UserRoleKey();
                    userRoleKey.setUsercode(userCode);
                    userRoleKey.setRolecode(Constant.MEDICAL_ETHICS_USER_ROLE);
                    List<UserRoleKey> userRoleKeys = userRoleService.selectUserRoleByUserCode(userCode);
                    if (!userRoleKeys.contains(userRoleKey)) {
                        userRoleService.insertSelective(userRoleKey);
                    }
                    //重新写入书记信息
                    MedicalEthicsUserRole medicalEthicsUserRole = new MedicalEthicsUserRole();
                    MedicalEthicsUser medicalEthicsUser = new MedicalEthicsUser();
                    medicalEthicsUser.setUserId(String.valueOf(leaderUserId));
                    medicalEthicsUser.setUserName(leaderName);
                    medicalEthicsUser.setSex(hUser.getSex().equals(SEX_MALE) ? STATUS_ONE : STATUS_ZERO);
                    Integer level = partyBranchRelations.getLevel();
                    medicalEthicsUserRole.setPartyId(partyBranchRelations.getId());
                    medicalEthicsUserRole.setUserId(leaderUserId.toString());
                    if (Constant.LEVEL2.equals(level)) {
                        //党总支
                        medicalEthicsUserRole.setRoleId(5);
                        medicalEthicsUserRole.setPartyLevel(2);
                        medicalEthicsUser.setGeneralBranchId(partyBranchRelations.getId());
                        medicalEthicsUser.setPartyCommitteesId(partyBranchRelations.getParentId());
                    } else if (Constant.LEVEL3.equals(level)) {
                        //支部
                        medicalEthicsUserRole.setRoleId(2);
                        medicalEthicsUserRole.setPartyLevel(3);
                        medicalEthicsUser.setBranchId(partyBranchRelations.getId());
                        medicalEthicsUser.setGeneralBranchId(partyBranchRelations.getParentId());
                        PartyBranchRelations parent = mapper.selectById(partyBranchRelations.getParentId());
                        medicalEthicsUser.setPartyCommitteesId(parent.getParentId());
                    } else if (Constant.LEVEL1.equals(level)) {
                        //党委
                        medicalEthicsUserRole.setRoleId(6);
                        medicalEthicsUserRole.setPartyLevel(1);
                        medicalEthicsUser.setPartyCommitteesId(partyBranchRelations.getId());
                    }
                    //提交userRole
                    Map<String, String> m1 = new HashMap<>();
                    m1.put("userId", leaderUserId.toString());
                    m1.put("partyId", partyId.toString());
                    m1.put("roleId", String.valueOf(medicalEthicsUserRole.getRoleId()));
                    List<MedicalEthicsUserRole> userRoleList = medicalEthicsUserService.selectUserRoleByParams(m1);
                    if (userRoleList.isEmpty()) {
                        medicalEthicsUserService.addUserRole(medicalEthicsUserRole);
                    }

                    List<MedicalEthicsUser> medicalUserList = medicalEthicsUserService.selectByUserId(leaderUserId.toString());
                    boolean sameParty = false;
                    for (MedicalEthicsUser m : medicalUserList) {
                        //需要添加的用户-部门信息是否已经存在
                        if (m.getUserId().equals(medicalEthicsUser.getUserId())
                                && m.getPartyCommitteesId() == medicalEthicsUser.getPartyCommitteesId()
                                && m.getGeneralBranchId() == medicalEthicsUser.getGeneralBranchId()
                                && m.getBranchId() == medicalEthicsUser.getBranchId()) {
                            sameParty = true;
                            break;
                        }
                    }
                    //不存在添加,同一个用户可以在不同部门当书记
                    if (!sameParty) {
                        medicalEthicsUserService.addUser(medicalEthicsUser);
                    }
                }
            }
        }
        //处理打分书记
        Integer oldDirectorUserId = oldData.getDirectorUserId();
        String oldDirectorName = oldData.getDirectorName();
        Integer newDirectorUserId = partyBranchRelations.getDirectorUserId();
        String newDirectorName = partyBranchRelations.getDirectorName();
        //未指定打分书记
        //新书记为空,存在旧的打分书记
        if (newDirectorUserId == null) {
            //删除原来的打分书记
            Map<String, String> deleteParam = new HashMap<>();
            if (oldDirectorUserId != null) {
                deleteParam.put("userId", oldDirectorUserId.toString());
            }
            deleteParam.put("partyId", String.valueOf(partyId));
            deleteParam.put("roleId", "7");
            medicalEthicsUserService.deleteUserRoleByParams(deleteParam);
            //新书记不为空
        } else {
            //指定了新的打分书记
            //新旧书记不是同一人
            if (!(oldDirectorUserId != null && partyBranchRelations.getDirectorUserId() != null && oldDirectorUserId.equals(newDirectorUserId))) {
                //删除原来的打分书记
                Map<String, String> directorDeleteParam = new HashMap<>();
                directorDeleteParam.put("userId", String.valueOf(oldDirectorUserId));
                directorDeleteParam.put("partyId", String.valueOf(partyId));
                directorDeleteParam.put("roleId", "7");
                medicalEthicsUserService.deleteUserRoleByParams(directorDeleteParam);
                //指定了新的打分书记
                if (personneService.checkIdAndName(newDirectorUserId, newDirectorName)) {
                    User user = userService.selectUserByMoneyCard(newDirectorUserId.toString());
                    if (user == null) {
                        PersonnelUser hUser = personneService.selectByUserId(String.valueOf(leaderUserId));
                        User newUser = new User();
                        newUser.setUsername(hUser.getUsername());
                        newUser.setPassword(MD5.md5(INITIAL_PASSWORD));
                        newUser.setUserstate("1");
                        newUser.setRoletype(Constant.USER_TYPE_MEDICAL_ETHICS);
                        newUser.setSex(SEX_MALE.equals(hUser.getSex()) ? "1" : "2");
                        newUser.setMoneycard(hUser.getId().toString());
                        newUser.setUsercode(leaderUserId.toString());
                        newUser.setMobile(hUser.getPhone());
                        userService.insertSelective(newUser);
                        UserRoleKey userRoleKey = new UserRoleKey();
                        userRoleKey.setUsercode(newUser.getUsercode());
                        userRoleKey.setRolecode(Constant.MEDICAL_ETHICS_USER_ROLE);
                        List<UserRoleKey> userRoleKeys = userRoleService.selectUserRoleByUserCode(newDirectorUserId.toString());
                        if ((!userRoleKeys.contains(userRoleKey))) {
                            userRoleService.insertSelective(userRoleKey);
                        }
                    }
                    //重新写入书记信息
                    MedicalEthicsUserRole medicalEthicsUserRole = new MedicalEthicsUserRole();
                    MedicalEthicsUser medicalEthicsUser = new MedicalEthicsUser();
                    //考核user信息
                    medicalEthicsUser.setUserId(newDirectorUserId.toString());
                    medicalEthicsUser.setUserName(newDirectorName);
                    medicalEthicsUser.setBranchId(partyBranchRelations.getId());
                    medicalEthicsUser.setGeneralBranchId(partyBranchRelations.getParentId());
                    PartyBranchRelations parent = mapper.selectById(partyBranchRelations.getParentId());
                    medicalEthicsUser.setPartyCommitteesId(parent.getParentId());

                    //考核userRole信息
                    medicalEthicsUserRole.setPartyId(partyId);
                    medicalEthicsUserRole.setUserId(newDirectorUserId.toString());
                    medicalEthicsUserRole.setRoleId(Integer.parseInt(Constant.ROLE_ID_SCORE_DIC));
                    medicalEthicsUserRole.setPartyLevel(Constant.LEVEL3);
                    medicalEthicsUserService.addUserRole(medicalEthicsUserRole);

                    List<MedicalEthicsUser> medicalUserList = medicalEthicsUserService.selectByUserId(newDirectorUserId.toString());
                    boolean sameParty = false;
                    for (MedicalEthicsUser m : medicalUserList) {
                        //需要添加的用户-部门信息是否已经存在
                        if (m.getUserId().equals(medicalEthicsUser.getUserId())
                                && m.getPartyCommitteesId() == medicalEthicsUser.getPartyCommitteesId()
                                && m.getGeneralBranchId() == medicalEthicsUser.getGeneralBranchId()
                                && m.getBranchId() == medicalEthicsUser.getBranchId()) {
                            sameParty = true;
                            break;
                        }
                    }
                    //不存在添加,一个部门只能有一个打分书记
                    if (!sameParty) {
                        medicalEthicsUserService.addUser(medicalEthicsUser);
                    }
                }
            }
        }
        //新旧书记为同一人,无需操作其他关联表,直接更新
        mapper.update(partyBranchRelations);
    }


    /**
     * updatePartyBranchInfo 更新支部信息的党支部书记和主任的时候,同时会更新考核领导信息和
     * 考核领导的角色信息 对应 medical_ethics_user 和 medical_ethics_user_role
     *
     * @param partyBranchRelations partyBranchRelations
     */
    @Override
    public void updatePartyBranchInfo(PartyBranchRelations partyBranchRelations) {

    }

    @Override
    public PartyBranchRelations selectById(Integer id) {
        return mapper.selectById(id);
    }

    /**
     *
     */
    @Override
    public void calculateExcellentPercent() {

    }

    /**
     * getCurrentExcellentInfo 获取当前支部信息/
     *
     * @param branchId branchId
     * @return {@link CurrentExcellentDto}
     */
    @Override
    public CurrentExcellentDto getCurrentExcellentInfo(String branchId) {
        CurrentExcellentDto resultDto = new CurrentExcellentDto();
        //获取所有的支部被考核用户
        List<MedicalEthicsMsg> msgList = msgService.selectCommonUser(branchId);
        //区分临床&非临床的用户id
        List<String> cliUserIdList = new ArrayList<>();
        List<String> nonCliUserIdList = new ArrayList<>();
        //计算临床用户的优秀人数
        if (!msgList.isEmpty()) {
            msgList.forEach(m -> {
                if (Constant.USER_TYPE_CLI == m.getPersonType()) {
                    cliUserIdList.add(m.getUserId());
                } else if (Constant.USER_TYPE_NO_CLI == m.getPersonType()) {
                    nonCliUserIdList.add(m.getUserId());
                }
            });
        }
        //支部总人数
        Map<String, String> queryParam = new HashMap<>();
        //非临床优秀人数
        int nonCliExcellentCount = 0;
        //临床优秀人数
        int cliExcellentCount = 0;
        //临床&非临床优秀人数
        int totalExcellentCount = 0;
        //支部总人数
        int totalPartyPeople = 0;
        queryParam.put("year", "2020");
        queryParam.put("branchId", branchId);
        queryParam.put("level", Constant.LEVEL1.toString());
        //计算非临床用户的优秀人数
        if (!nonCliUserIdList.isEmpty()) {
            nonCliExcellentCount = calculateService.nonCliCountByParams(queryParam);
        }
        //计算临床用户的优秀人数
        if (!cliUserIdList.isEmpty()) {
            cliExcellentCount = calculateService.cliCountByParams(queryParam);
        }
        //获取支部信息
        PartyBranchRelations partyBranchRelations = mapper.selectById(Integer.valueOf(branchId));
        if (partyBranchRelations != null) {
            if (partyBranchRelations.getExcellentPercent() == null) {
                resultDto.setMaxExcellentPercent(0);
            } else {
                resultDto.setMaxExcellentPercent(partyBranchRelations.getExcellentPercent());
            }
        }
        totalExcellentCount = nonCliExcellentCount + cliExcellentCount;
        totalPartyPeople = cliUserIdList.size() + nonCliUserIdList.size();
        resultDto.setTotalCount(totalPartyPeople);
        resultDto.setCurrentExcellentCount(totalExcellentCount);
        return resultDto;
    }

    /**
     * checkDeleted 查该单位是否可以删除
     *
     * @param id 支部id
     * @return {@link Boolean}
     */
    private Boolean checkDeleted(Integer id) {
        //党支部下有人员角色禁止删除
        Map<String, String> params = new HashMap<>();
        params.put("partyId", String.valueOf(id));
        List<MedicalEthicsUserRole> medicalEthicsUserRoles = medicalEthicsUserService.selectUserRoleByParams(params);
        if (!medicalEthicsUserRoles.isEmpty()) {
            return false;
        }
        //当支部有下级，禁止删除
        List<PartyBranchRelations> relationsList = mapper.selectByParentId(id);
        List<PartyBranchRelations> resList;
        if (!relationsList.isEmpty()) {
            //存在使用中的子节点无法删除
            resList = relationsList.stream().filter(r -> Constant.NOT_DELETED.equals(r.getIsDelete())).collect(Collectors.toList());
            return resList.isEmpty();
        }
        return true;
    }


}
