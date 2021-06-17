package com.welb.medicalEthics.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @description: 数据字典
 * @author: luox
 * @date： 2020/12/23
 */

@Mapper
@Component
public interface EvaluationEthicsDicMapper {

    @Select(
            "<script>" +
            " select dic_value  from  medical_ethics_dic" +
            " where dic_type = #{dicType} " +
            " and " +
            " dic_code = #{dicCode} " +
            "</script>")
    String selectByCodeAndType(String dicType,String dicCode);

}
