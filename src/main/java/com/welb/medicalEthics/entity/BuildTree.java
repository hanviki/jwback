package com.welb.medicalEthics.entity;

import com.welb.sysBase.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTree {
    public static <T> Tree<T> build(List<Tree<T>> nodes) {

        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<Tree<T>>();

        for (Tree<T> children : nodes) {

            String pid = children.getParentId();
            if (pid == null || Constant.TREE_ROOT_ID.equals(pid)) {
                topNodes.add(children);
                continue;
            }
            for (Tree<T> parent : nodes) {
                String id = parent.getValue();
                if (id != null && id.equals(pid)) {
                    if (null == parent.getChildren()){
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(children);
                    children.setHasParent(true);
                    parent.setHasChildren(true);
                    continue;
                }
            }

        }

        Tree<T> root = new Tree<T>();
        if (topNodes.size() == 1) {
            root = topNodes.get(0);
        } else {
            root.setValue(Constant.TREE_ROOT_ID);
            root.setParentId("");
            root.setHasParent(false);
            root.setHasChildren(true);
            root.setChecked(true);
            root.setChildren(topNodes);
            root.setLabel("顶级节点");
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            root.setState(state);
        }

        return root;
    }
}
