package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.entity.PartyBranchRelations;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 生成党组织tree
 * @author: luox
 * @date： 2020/11/19
 */
public class PartyBranchTree {

    /**
     * getTree 生成tree的总方法
     *
     * @param data data
     * @return {@link List<PartyBranchRelations>}
     *
     */
    public static List<PartyBranchRelations> getTree(List<PartyBranchRelations> data){
        List<PartyBranchRelations> treeNodes = new ArrayList<>();
        List<PartyBranchRelations> rootNodes = getRootNodes(data);
        for (PartyBranchRelations rootNode : rootNodes) {
            buildChildNodes(rootNode, data);
            treeNodes.add(rootNode);
        }
        return treeNodes;
    }

    /**
     * buildChildNodes 当前节点获取子节点list
     *
     * @param node 当前节点
     * @param treeNodesList 子列表
     *
     */
    private static void buildChildNodes(PartyBranchRelations node, List<PartyBranchRelations> treeNodesList) {
        List<PartyBranchRelations> children = getChildNodes(node, treeNodesList);
        if (!children.isEmpty()) {
            for (PartyBranchRelations child : children) {
                buildChildNodes(child, treeNodesList);
            }
            node.setChildren(children);
        }
    }

    /**
     * rootNode 判断是否为根节点
     *
     * @param node 当前节点
     * @param treeNodesList 节点list
     * @return {@link boolean}
     *
     */
    private static boolean rootNode(PartyBranchRelations node, List<PartyBranchRelations> treeNodesList) {
        boolean isRootNode = true;
        for (PartyBranchRelations n : treeNodesList) {
            if (node.getParentId().equals(n.getId())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    /**
     * getRootNodes 获取集合中所有的根节点
     *
     * @param treeNodesList 节点列表
     * @return {@link List<PartyBranchRelations>}
     *
     */
    private static List<PartyBranchRelations> getRootNodes(List<PartyBranchRelations> treeNodesList) {
        List<PartyBranchRelations> rootNodes = new ArrayList<>();
        for (PartyBranchRelations n : treeNodesList) {
            if (rootNode(n, treeNodesList)) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }

    /**
     * getChildNodes 获取父节点下所有的子节点
     *
     * @param pNode 父节点
     * @param treeNodesList treeNodesList
     * @return {@link List<PartyBranchRelations>}
     *
     */
    private static List<PartyBranchRelations> getChildNodes(PartyBranchRelations pNode, List<PartyBranchRelations> treeNodesList) {
        List<PartyBranchRelations> childNodes = new ArrayList<>();
        for (PartyBranchRelations n : treeNodesList) {
            if (pNode.getId().equals(n.getParentId())) {
                childNodes.add(n);
            }
        }
        return childNodes;
    }


}
