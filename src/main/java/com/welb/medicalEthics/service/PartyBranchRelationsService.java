package com.welb.medicalEthics.service;

import com.welb.medicalEthics.entity.PartyBranchRelations;
import com.welb.medicalEthics.entity.Tree;
import com.welb.medicalEthics.dto.CurrentExcellentDto;

import java.util.List;
import java.util.Map;

/**
 * 党组织相关
 */
public interface PartyBranchRelationsService {

    List<PartyBranchRelations> list(Map<String,String> params);

    List<PartyBranchRelations> listDetail(Map<String,String> params);

    void batchUpdate(List<PartyBranchRelations> dataSource);

    Tree<PartyBranchRelations> tree(Map<String,String> params);

    Tree<PartyBranchRelations> partyBranchRelations();

    List<PartyBranchRelations> getBranchTree(Map<String, String> params);

    void insert(PartyBranchRelations partyBranchRelations);

    Boolean deleteById(Integer id);

    void update(PartyBranchRelations partyBranchRelations);

    void updatePartyBranchInfo(PartyBranchRelations partyBranchRelations);

    PartyBranchRelations selectById(Integer id);

    public void calculateExcellentPercent();

    CurrentExcellentDto getCurrentExcellentInfo(String branchId);


}