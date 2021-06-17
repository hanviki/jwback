package com.welb.sysBase.mapper;

import com.welb.sysBase.util.Permission;
import com.welb.util.PageData;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface PermissionMapper {

    @Select("<script>" +
            "SELECT * FROM h_user " +
            "WHERE 1=1  " +
            "<if test='u_id!=null '> and  u_id=#{u_id}</if>"
            + "</script>")
    List<PageData> showAllUser(Map<String, String> param);

    @Select("<script>" +
            "SELECT " +
            "u_id, " +
            "u_name," +
            "u_job_number," +
            "u_technical_position1," +
            "u_check_department," +
            "role_code," +
            "role_name " +
            "FROM " +
            "h_user hu " +
            "LEFT JOIN (" +
            "SELECT " +
            "UserCode user_code," +
            "UserName user_name," +
            "UserState user_state," +
            "Mobile mobile," +
            "Operator operator," +
            "StationCode station_code," +
            "PicturePath picture_path," +
            "Sex sex," +
            "Nation nation," +
            "Education education," +
            "Email email," +
            "Political political," +
            "MoneyCard money_card," +
            "BranchCode branch_code," +
            "ARatio a_ratio," +
            "BRatio b_ratio," +
            "CRatio c_ratio," +
            "DRatio d_ratio," +
            "FullStationCode full_station_code," +
            "FullBranchCode full_branch_code," +
            "Flag flag," +
            "RoleType role_type," +
            "IsAgent is_agent," +
            "RoleCode role_code," +
            "RoleName role_name " +
            "FROM " +
            "(" +
            "SELECT " +
            "u.UserCode," +
            "u.UserName," +
            "u.PASSWORD," +
            "u.UserState," +
            "u.Mobile," +
            "u.Operator," +
            "u.StationCode," +
            "u.PicturePath," +
            "u.Sex," +
            "u.Nation," +
            "u.Education," +
            "u.Email," +
            "u.Political," +
            "u.MoneyCard," +
            "u.BranchCode," +
            "u.ARatio," +
            "u.BRatio," +
            "u.CRatio," +
            "u.DRatio," +
            "u.FullStationCode," +
            "u.FullBranchCode," +
            "u.Flag," +
            "u.RoleType," +
            "u.IsAgent," +
            "group_concat( ur.RoleCode ORDER BY ur.UserCode ASC ) RoleCode," +
            "group_concat( ur.RoleName ORDER BY ur.UserCode ASC ) RoleName " +
            "FROM " +
            "USER u " +
            "LEFT JOIN (" +
            "SELECT " +
            "a.UserCode," +
            "a.RoleCode," +
            "b.RoleName," +
            "b.RoleDescription " +
            "FROM " +
            "user_role a " +
            "LEFT JOIN role b ON a.RoleCode = b.RoleCode " +
            ") ur ON u.UserCode = ur.UserCode " +
            "GROUP BY " +
            "u.UserCode " +
            ") t " +
            ") rs ON hu.u_id = rs.money_card " +
            "WHERE 1=1  " +
            "<if test='uId!=null '> and  hu.u_id=#{uId}</if>" +
            "<if test='f_condition!=null'>" +
            "and ( hu.u_job_number LIKE CONCAT(CONCAT('%', #{f_condition},'%')) " +
            "or hu.u_name LIKE CONCAT(CONCAT('%', #{f_condition},'%')) or hu.u_id=#{f_condition}) " +
            "</if> " +
            "<if test='role_code!=null '> and  role_code=${role_code}</if>" +
            "</script>")
    List<Permission> showAllData(Map<String, String> param);

    @Select("<script>" +
            "SELECT " +
            "u_id, " +
            "u_password," +
            "u_name," +
            "u_img," +
            "u_sex," +
            "u_birth," +
            "u_home_address," +
            "u_nation," +
            "u_native_place," +
            "u_id_card," +
            "u_old_id," +
            "u_job_time," +
            "u_hospital_time," +
            "u_job_number," +
            "u_email," +
            "u_phone," +
            "u_title_level," +
            "u_technical_position1," +
            "u_position_time1," +
            "u_technical_position2," +
            "u_position_time2," +
            "u_employment_unit," +
            "u_check_department," +
            "u_statistic_department," +
            "u_his_number," +
            "u_update_status," +
            "u_activate_status," +
            "u_check_data_status," +
            "u_tel," +
            "u_degree," +
            "u_edu," +
            "user_code," +
            "user_state," +
            "mobile," +
            "operator," +
            "station_code," +
            "picture_path," +
            "sex," +
            "nation," +
            "education," +
            "email," +
            "political," +
            "money_card," +
            "branch_code," +
            "a_ratio," +
            "b_ratio," +
            "c_ratio," +
            "d_ratio," +
            "full_station_code," +
            "full_branch_code," +
            "flag," +
            "role_type," +
            "is_agent," +
            "role_code," +
            "role_name " +
            "FROM " +
            "h_user hu " +
            "LEFT JOIN (" +
            "SELECT " +
            "UserCode user_code," +
            "UserName user_name," +
            "UserState user_state," +
            "Mobile mobile," +
            "Operator operator," +
            "StationCode station_code," +
            "PicturePath picture_path," +
            "Sex sex," +
            "Nation nation," +
            "Education education," +
            "Email email," +
            "Political political," +
            "MoneyCard money_card," +
            "BranchCode branch_code," +
            "ARatio a_ratio," +
            "BRatio b_ratio," +
            "CRatio c_ratio," +
            "DRatio d_ratio," +
            "FullStationCode full_station_code," +
            "FullBranchCode full_branch_code," +
            "Flag flag," +
            "RoleType role_type," +
            "IsAgent is_agent," +
            "RoleCode role_code," +
            "RoleName role_name " +
            "FROM " +
            "(" +
            "SELECT " +
            "u.UserCode," +
            "u.UserName," +
            "u.PASSWORD," +
            "u.UserState," +
            "u.Mobile," +
            "u.Operator," +
            "u.StationCode," +
            "u.PicturePath," +
            "u.Sex," +
            "u.Nation," +
            "u.Education," +
            "u.Email," +
            "u.Political," +
            "u.MoneyCard," +
            "u.BranchCode," +
            "u.ARatio," +
            "u.BRatio," +
            "u.CRatio," +
            "u.DRatio," +
            "u.FullStationCode," +
            "u.FullBranchCode," +
            "u.Flag," +
            "u.RoleType," +
            "u.IsAgent," +
            "group_concat( ur.RoleCode ORDER BY ur.UserCode ASC ) RoleCode," +
            "group_concat( ur.RoleName ORDER BY ur.UserCode ASC ) RoleName " +
            "FROM " +
            "USER u " +
            "LEFT JOIN (" +
            "SELECT " +
            "a.UserCode," +
            "a.RoleCode," +
            "b.RoleName," +
            "b.RoleDescription " +
            "FROM " +
            "user_role a " +
            "LEFT JOIN role b ON a.RoleCode = b.RoleCode " +
            ") ur ON u.UserCode = ur.UserCode " +
            "GROUP BY " +
            "u.UserCode " +
            ") t " +
            ") rs ON hu.u_id = rs.money_card " +
            "WHERE 1=1 and hu.u_id=#{uId}" +
            "</script>")
    Permission showSingleRecord(String uId);

    @Select("<script>" +
            "SELECT ur.UserCode,ur.RoleCode,r.RoleName,r.RoleDescription FROM user_role ur LEFT JOIN role r ON ur.RoleCode=r.RoleCode " +
            "WHERE 1=1 "
            +"<if test='user_code!=null '> and  ur.UserCode=#{user_code}</if>"
            + "</script>")
    List<PageData> showUserRole(Map<String, String> param);

    @Select("<script>" +
            "SELECT * FROM user " +
            "WHERE Flag=0 and 1=1  " +
            "<if test='user_code!=null '> and  UserCode=#{user_code}</if>"
            + "</script>")
    List<PageData> showUser(Map<String, String> param);

    @Delete("DELETE FROM user_role WHERE UserCode=#{userCode}")
    void deleteUserRoleByUserCode(String userCode);

    @Select("<script>" +
            "SELECT * FROM h_user " +
            "WHERE u_id in  " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>"  +
            "#{item}" +
            "</foreach>"
            + "</script>")
    List<PageData> showUserByUserId(List<String> userIdList);

    @Select("<script>" +
            "SELECT * FROM user " +
            " WHERE Flag=0 AND MoneyCard in  " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>"  +
            "#{item}" +
            "</foreach>"
            + "</script>")
    List<PageData> showLoginUserByUserId(List<String> userIdList);

    @Update("UPDATE `user` SET Mobile=#{u_phone} WHERE MoneyCard=#{u_id}")
    void updatePhone(String u_id, String u_phone);
}
