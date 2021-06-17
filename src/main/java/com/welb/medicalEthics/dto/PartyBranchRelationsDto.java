package com.welb.medicalEthics.dto;

import com.welb.medicalEthics.entity.PartyBranchRelations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/16
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartyBranchRelationsDto {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 党支部关系名
     */
    private String relationsName;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 院区
     */
    private String hospitalDistrict;

    /**
     * 人员类型
     */
    private int personType;

    /**
     * 是否删除
     */
    private int isDelete;


    /**
     * 上级节点名字
     */
    private String parentRelationName;

    /**
     * 书记名字
     */
    private String leaderName;

    /**
     * 领导ID对应h_user表的u id
     */
    private Integer leaderUserId;

    /**
     * 级别 1-党委 2-总支 3-支部
     */
    private Integer level;

    /**
     * 最高比例
     */
    private Integer excellentPercent;

    /**
     * 当前优秀比例
     */
    private Double currentExcellentPercent;

    /**
     * 打分书记
     */
    private String directorName;

    /**
     * 打分书记发薪号
     */
    private Integer directorUserId;
}
