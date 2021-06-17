package com.welb.medicalEthics.mapper;

import com.welb.medicalEthics.dto.MedicalPermDto;
import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.MedicalEthicsUserRole;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2021/2/1
 */

@Mapper
@Component
public interface MedicalPermMapper {

    @Select("<script>" +
            " select u.user_name as username," +
            " u.user_id as userId," +
            " u.is_delete as status," +
            " r.role_name as roleName, " +
            " r.id as roleId " +
            " from medical_ethics_user u LEFT JOIN medical_ethics_user_role ur ON u.user_id = ur.user_id  " +
            " LEFT JOIN medical_ethics_role_info r ON ur.role_id = r.id  " +
            "<where>" +
            "<if test = 'username != null'> AND u.user_name LIKE CONCAT(CONCAT('%', #{username}),'%') </if>" +
            "<if test = 'userId != null'> AND u.user_id  = #{userId} </if>" +
            "<if test = 'roleId != null'> AND ur.role_id  = #{roleId} </if>" +
            "<if test = 'isDelete != null'> AND u.is_delete  = #{isDelete} </if>" +
            " AND r.id in(1,8)  " +
            "</where>" +
            "</script>")
    List<MedicalPermDto> listAdmin(Map<String, String> params);

    @Select("<script>" +
            " select u.user_name as username," +
            " u.user_id as userId," +
            " u.is_delete as status," +
            " r.role_name as roleName " +
            " from medical_ethics_user u LEFT JOIN medical_ethics_user_role ur ON u.user_id = ur.user_id  " +
            " LEFT JOIN medical_ethics_role_info r ON ur.role_id = r.id  " +
            "<where>" +
            "<if test = 'username != null'> AND" +
            "u.user_name = #{user_name} </if>" +
            "<if test = 'userId != null'> AND u.user_id  = #{userId} </if>" +
            "<if test = 'roleId != null'> AND ur.role_id  = #{roleId} </if>" +
            "<if test = 'isDelete != null'> AND u.is_delete  = #{isDelete} </if>" +
            "</where>" +
            "" +
            "</script>")
    List<MedicalPermDto> list(Map<String, String> params);

    @Delete("<script>" +
            " delete from medical_ethics_user_role where (role_id = 1 or role_id = 8 ) and user_id = #{userId}" +
            "</script>")
    void deleteAdminRole(String userId);

    @Select("select role_id from medical_ethics_user_role where (role_id = 1 or role_id = 8 ) and user_id = #{userId}")
    List<MedicalEthicsUserRole> selectBaseByUserId(String userId);

    @Insert("<script>" +
            " insert into medical_ethics_user_role( user_id,role_id ) values (#{userId},#{roleId}) " +
            "</script>")
    void insertUserRole(MedicalPermDto dto);

    @Insert("<script>" +
            "update medical_ethics_user set is_delete = #{status} where user_id = #{userId}  " +
            "</script>")
    void updateStatus(String status,String userId);

    @Select(" select user_id,user_name from medical_ethics_msg where user_id like CONCAT(CONCAT('%', #{key}),'%') " +
            " or user_name like CONCAT(CONCAT('%', #{key}),'%') " +
            " ")
    List<MedicalEthicsMsg> selectUserByKey(String key);


}
