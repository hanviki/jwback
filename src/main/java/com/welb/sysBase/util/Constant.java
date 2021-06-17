package com.welb.sysBase.util;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    public static final String COMMA = ",";
    public static final String DON = "、";
    public static final int NUMBER_ZERO = 0;
    public static final String STRING_ZERO = "0";
    public static final String INITIAL_PASSWORD = "whuh123456";

    /**
     * 数据删除状态
     */
    public static final Integer DELETED = 1;
    public static final Integer NOT_DELETED = 0;

    public static final Map<String, String> PERSON_PERMISSION_MAP = new HashMap<String, String>() {{
        put("400", "考勤超级管理员");
        put("500", "考勤管理员");
    }};

    public static final Map<String, String> ORGANIZATION_PERMISSION_MAP = new HashMap<String, String>() {{
        put("100", "组织部");
        put("150", "打分用户");
        put("200", "部门长");
        put("300", "普通用户");
    }};

    public static final Map<String, String> QUESTIONNAIRE_PERMISSION_MAP = new HashMap<String, String>() {{
        put("600", "调查问卷管理员");
    }};

    //用户类型(0-组织部  1-人事部  2-调查问卷' 3-医德医风)
    public static final String USER_TYPE_QUESTIONNAIRE = "2";
    public static final String USER_TYPE_MEDICAL_ETHICS = "3";

    //医德医风权限
    public static final String MEDICAL_ETHICS_USER_ROLE = "700";

    public static final String EMPTY_STR = "";
    public static final String SPACE_STR = " ";

    public static final String SCORE_RELATIONSHIP_SHEET_NAME = "评分关系";
    public static final int SCORE_RELATIONSHIP_FIRST_ROW_DATA_INDEX = 0;
    public static final String SCORE_RELATIONSHIP_TEMPLATE = "ScoreRelationship.xlsx";
    public static final String CATEGORY_A = "A";
    public static final String CATEGORY_B = "B";
    public static final String CATEGORY_C = "C";
    public static final String CATEGORY_D = "D";

    public static final String MEDICAL_ETHICS_MSG_SHEET_NAME = "医德医风人员模板";
    public static final int MEDICAL_ETHICS_MSG_FIRST_ROW_DATA_INDEX = 0;
    public static final String MEDICAL_ETHICS_MSG_TEMPLATE = "MedicalEthicsMsg.xlsx";

    public static final int STATUS_ZERO = 0;
    public static final int STATUS_ONE = 1;
    public static final String SEX_MALE = "男";
    public static final String SEX_FEMALE = "女";

    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_PATTERN = "HH:mm:ss";
    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";


    public static final String TREE_ROOT_ID = "-1";

    /**
     * 党委树根节点
     */
    public static final Integer BRANCH_TREE_ROOT = -1;

    /**
     * 一级单位 党委
     */
    public static final Integer LEVEL_DW = 1;

    /**
     * 二级单位党总支
     */
    public static final Integer LEVEL_DZZ = 2;

    /**
     * 三级单位党支部
     */
    public static final Integer LEVEL_DZB = 3;

    public static final String MEDICAL_ETHICS_ADMINISTRATOR = "100";  //医德医风管理员
    public static final String MEDICAL_ETHICS_SECRETARY_USER = "101";  //医德医风书记
    public static final String MEDICAL_ETHICS_GENERAL_USER = "120";  //党总支书记
    public static final String MEDICAL_ETHICS_COMMITTEE_USER = "103";  //党总支书记
    public static final String MEDICAL_ETHICS_ORDINARY_USER = "200";  //医德医风普通用户
    public static final String MEDICAL_ETHICS_ORDINARY_DIRECTOR = "300";  //医德医风科室主任
    public static final String MEDICAL_ETHICS_SCORING_SECRETARY = "400";  //打分书记

    /**
     * 角色代码
     */

    /**
     * 管理员
     */
    public static final String ROLE_ID_ADMIN = "1";

    /**
     * 支部书记
     */
    public static final String ROLE_ID_BRANCH = "2";

    /**
     * 普通用户
     */
    public static final String ROLE_ID_COMMON = "3";

    /**
     * 科室主任
     */
    public static final String ROLE_ID_DIRECTOR = "4";

    /**
     * 打分书记
     */
    public static final String ROLE_ID_SCORE_DIC = "7";

    /**
     * 默认空表单
     */
    public static final Integer EVALUATION_NON_CLINICAL_DEFAULT = 0;

    /**
     * 填写完自评
     */
    public static final Integer EVALUATION_NON_CLINICAL_SELF_SUMMARY = 1;

    /**
     * 部门领导填写完意见
     */
    public static final Integer EVALUATION_NON_CLINICAL_DEPT_HEAD_OPINION = 2;

    /**
     * 党委领导意见填写
     */
    public static final Integer EVALUATION_NON_CLINICAL_BRANCH_OPINION = 3;


    /**
     * 临床默认空表单
     */
    public static final Integer EVALUATION_CLINICAL_DEFAULT = 0;

    /**
     * 临床填写完基本信息
     */
    public static final Integer EVALUATION_CLINICAL_BASE_INFO = 1;

    /**
     * 临床填写完自我总结
     */
    public static final Integer EVALUATION_CLINICAL_SELF_SUMMARY = 2;

    /**
     * 临床
     */
    public static final Integer ITEM_COUNT = 10;

    /**
     * 临床用户问卷生成id
     */
    public static final int[] EVALUATION_CLINICAL_ITEM_ID = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    /**
     * 医德医风人员管理 0-临时用户
     */
    public static final Integer EVALUATION_CLINICAL_TEMP_USER = 0;

    /**
     * 医德医风人员管理 1-非临时用户
     */
    public static final Integer EVALUATION_CLINICAL_NOT_TEMP_USER = 1;

    /**
     * 临床用户评分结果 0-操作成功 1-操作失败 2-百分比不达标
     */
    public static final Integer EVALUATION_CLINICAL_SUCCESS = 0;
    public static final Integer EVALUATION_CLINICAL_FAIL = 1;
    public static final Integer EVALUATION_CLINICAL_PERCENT_ERROR = 2;

    /**
     * 用户分数类型 1-自我评分 2-领导评分  3-党支部数据评分
     */
    public static final Integer SCORE_TYPE_SELF = 1;
    public static final Integer SCORE_TYPE_HEAD = 2;
    public static final Integer SCORE_TYPE_BRANCH = 3;

    /**
     * 人员类型 0-临床
     * 人员类型 1-非临床
     */
    public static final Integer USER_TYPE_CLI = 0;
    public static final Integer USER_TYPE_NO_CLI = 1;

    /**
     * 自我评分权重
     */
    public static final Double SCORE_PERCENT_SELF = 0.3;

    /**
     * 主任评分权重
     */
    public static final double SCORE_PERCENT_HEAD = 0.35;

    /**
     * 书记评分权重
     */
    public static final double SCORE_PERCENT_BRANCH = 0.35;

    public static final Integer SCORE_LEVEL0 = 0;
    /**
     * 90-100
     */
    public static final Integer SCORE_LEVEL1 = 90;

    /**
     * 80-89
     */
    public static final Integer SCORE_LEVEL2 = 80;

    /**
     * 60-79
     */
    public static final Integer SCORE_LEVEL3 = 60;

    /**
     * < 60
     */
    public static final Integer SCORE_LEVEL4 = 60;

    public static final Integer LEVEL0 = 0;
    public static final Integer LEVEL1 = 1;
    public static final Integer LEVEL2 = 2;
    public static final Integer LEVEL3 = 3;
    public static final Integer LEVEL4 = 4;

    public static final String FIRST_STEP = "1";

    public static final int SELF_SUBMIT = 0; //自己提交
    public static final int DIRECTOR_SUBMIT = 1;  //主任提交
    public static final int SECRETARY_SUBMIT = 2; //书记提交

    /**
     * 非临床状态码
     */
    public static final int NON_CLI_STEP_ZERO = 0;
    public static final int NON_CLI_STEP_ONE = 1;
    public static final int NON_CLI_STEP_TWO = 2;
    public static final int NON_CLI_STEP_THREE = 3;

    /**
     * 临床用户状态码
     */
    public static final int CLI_STEP_ZERO = 0;
    public static final int CLI_STEP_ONE = 1;
    public static final int CLI_STEP_TWO = 2;
    public static final int CLI_STEP_THREE = 3;
    public static final int CLI_STEP_FOUR = 4;
    public static final int CLI_STEP_FIVE = 5;
    public static final int STEP_FINISH = 6;

    /**
     * 评分等级
     */
    public static final String SCORE_LEVEL_ONE_TEXT = "优秀";
    public static final String SCORE_LEVEL_TWO_TEXT = "良好";
    public static final String SCORE_LEVEL_THREE_TEXT = "一般";
    public static final String SCORE_LEVEL_FOUR_TEXT = "较差";

    /**
     * 政治面貌
     */
    public static final String DIC_TYPE_POLITICAL_STATUS = "political_status";

    /**
     * 职称
     */
    public static final String DIC_TYPE_TITLE = "title";

    /**
     * 教育程度
     */
    public static final String DIC_TYPE_EDUCATION_LEVEL = "education_level";

    /**
     * 评分等级
     */
    public static final String DIC_TYPE_SCORE_LEVEL = "score_level";

    /**
     * 考核年份
     */
    public static final String CURRENT_YEAR = "2020";




}
