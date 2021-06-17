package com.welb.sysBase.mapper;

import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.util.PageData;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * 人事dao接口
 * LCL 2018.07.27
 */
@Mapper
public interface PerconneMapper {


    //保存管理任务
    @Insert("insert into h_distribution("
            + "u_id,d_id,d_name,d_upper_id,d_upper_name,dis_job_code,dis_job_info,dis_hr_scope,dis_hr_scope2,dis_staff_group,dis_staff_group2,dis_center_code,dis_center_info"
            + ") values ("
            + "#{pd0},#{pd2},#{pd4},#{pd5},#{pd6},#{pd7},#{pd8},#{pd9},#{pd10},#{pd11},#{pd12}"
            + ")")
    int save1(PageData pd);


    //保存人事数据
    @Insert("insert into h_user("
            + "u_id,u_name,u_sex,u_birth,u_home_address,u_nation,"
            + "u_native_place,u_id_card,u_old_id,u_job_time,"
            + "u_hospital_time,u_job_number,u_email,u_phone,"
            + "u_title_level,u_technical_position1,u_position_time1,"
            + "u_technical_position2,u_position_time2,u_employment_unit,"
            + "u_check_department,u_statistic_department,u_his_number,"
            + "u_update_status,u_activate_status,u_edu"
            + ") values ("
            + "#{pd0},#{pd13},#{pd14},#{pd15},#{pd16},#{pd17},#{pd18},#{pd19},#{pd20},#{pd21},#{pd22},#{pd23},#{pd24},#{pd25},#{pd29},#{pd30},#{pd31},#{pd32},#{pd33},#{pd34},#{pd35},#{pd36},#{pd37},1,1,#{pd26}"
            + ")")
    int save2(PageData pd);

    //判断管理任务是否存在
    @Select("select * from h_distribution where u_id = #{uId}")
    PageData findUid1(@Param("uId") String uId);

    //判断人事信息是否存在
    @Select("select * from h_user where u_id = #{uId}")
    PageData findUid2(@Param("uId") String uId);

    @Select("select u_id from h_user ")
    List<String> findAllUids();

    //判断管理任务是否存在(存在修改，否则添加)
    @Update("UPDATE h_distribution set "
            + "d_id=#{pd1},d_name=#{pd2},"
            + "d_upper_id=#{pd3},d_upper_name=#{pd4},"
            + "dis_job_code=#{pd5},dis_job_info=#{pd6},"
            + "dis_hr_scope=#{pd7},dis_hr_scope2=#{pd8},"
            + "dis_staff_group=#{pd9},dis_staff_group2=#{pd10},"
            + "dis_center_code=#{pd11},dis_center_info=#{pd12}"
            + " where u_id=#{pd0}")
    int upd1(PageData pd);

    //判断人事信息是否存在(存在修改，否则添加)
    @Update("UPDATE h_user set "
            + "u_name=#{pd13},u_sex=#{pd14},u_birth=#{pd15},u_home_address=#{pd16},u_nation=#{pd17},"
            + "u_native_place=#{pd18},u_id_card=#{pd19},u_old_id=#{pd20},u_job_time=#{pd21},"
            + "u_hospital_time=#{pd22},u_job_number=#{pd23},u_email=#{pd24},u_phone=#{pd25},"
            + "u_title_level=#{pd29},u_technical_position1=#{pd30},u_position_time1=#{pd31},"
            + "u_technical_position2=#{pd32},u_position_time2=#{pd33},u_employment_unit=#{pd34},"
            + "u_check_department=#{pd35},u_statistic_department=#{pd37},u_his_number=#{pd37},"
            + "u_update_status=2,u_activate_status=1,u_edu=#{pd26}"
            + " where u_id=#{pd0}")
    int upd2(PageData pd);

    String sql_batchDelete_user = "DELETE FROM h_user WHERE u_id = #{u_id} ";
    @Delete(sql_batchDelete_user)
    void delDistribution(PageData pageData);

    String sql_batchDelete_distribution = "DELETE FROM h_distribution WHERE u_id = #{u_id} ";
    @Delete(sql_batchDelete_distribution)
    void delUser(PageData pageData);

    //同步完成后  更新大科室(根据整理的三级标准部门  映射到大科室)
    @Update(" update h_distribution a set a.d_upper_id = ( select d_first_id from h_department b  where a.d_id = b.d_id ), a.d_upper_name = (\n" +
            " select c.d_name from h_department c where c.d_id = (select d_first_id from h_department b where   a.d_id = b.d_id)\n" +
            ") ")
    void updateD_upper_id();

    @Select("SELECT " +
			"u_id as id,"+
            "u_sex as sex," +
            "u_name as username," +
            "u_birth as birth," +
            "u_phone as phone " +
            "FROM h_user "+
            "WHERE u_id = #{userId}")
    PersonnelUser selectByUserId(String userId);

    @Select("SELECT " +
            "u_id as id,"+
            "u_sex as sex," +
            "u_name as username," +
            "u_birth as birth " +
            "FROM h_user "+
            "" +
            "")
    List<PersonnelUser> selectListBy(Map<String,Object> params);

    @Select("<script> select u_id as id ,u_phone as  from h_user </script>")
    List<PersonnelUser> selectAll();

}
