package com.welb.sysBase.service;

import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.sysBase.mapper.PerconneMapper;
import com.welb.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 人事
 * LCL
 *  2018.07.27
 */
@Service("PersonneService")
public class HUserService {

	@Autowired(required = false)
	private PerconneMapper dao;

	public int save1(PageData pd) {
		return dao.save1(pd);

	}

	public int save2(PageData pd) {
		return dao.save2(pd);
	}

	public PageData findUid1(String uId) {
		return dao.findUid1(uId);
	}

	public PageData findUid2(String uId) {
		return dao.findUid2(uId);
	}

	public int upd1(PageData pd) {
		return dao.upd1(pd);
	}

	public int upd2(PageData pd) {
		return dao.upd2(pd);
	}

	//查询所有的u_id
	public List<String>  findAllUids() {
		return dao.findAllUids();
	}

	/**
	 * 删除退休的人员信息
	 */
	public void delDistribution(PageData pageData) {
		dao.delDistribution(pageData);
	}

	public void delUser(PageData pageData) {
		dao.delUser(pageData);
	}

	/**
	 * 同步完成后  更新大科室(根据整理的三级标准部门  映射到大科室)
	 */

	public void updateD_upper_id(){
		dao.updateD_upper_id();
	}

	/**
	 * 根据u_id查询
	 * @param userId
	 * @return
	 */
	public PersonnelUser selectByUserId(String userId){
	   return dao.selectByUserId(userId);
	}

	/**
	 * checkIdAndName 检查发薪号和用户名是否匹配
	 * true - 合法
	 * @param id id
	 * @param name name
	 * @return {@link Boolean}
	 *
	 */
	public Boolean checkIdAndName(Integer id,String name){
		PersonnelUser user = selectByUserId(String.valueOf(id));
		if(user != null){
			return name.equals(user.getUsername());
		}
		return false;
	}




}
