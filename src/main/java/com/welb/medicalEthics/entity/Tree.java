package com.welb.medicalEthics.entity;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

public class Tree<T> {
    /**
     * 节点ID
     */
    private String value;
    /**
     * 显示节点文本
     */
    private String label;
    /**
     * 节点状态，open closed
     */
    private Map<String, Object> state;
    /**
     * 节点是否被选中 true false
     */
    private boolean checked = false;
    /**
     * 节点属性
     */
    private Map<String, Object> attributes;

    /**
     * 节点的子节点
     */
    private List<Tree<T>> children;

    /**
     * 父ID
     */
    private String parentId;
    /**
     * 是否有父节点
     */
    private boolean hasParent = false;
    /**
     * 是否有子节点
     */
    private boolean hasChildren = false;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public void setState(Map<String, Object> state) {
        this.state = state;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }

    public boolean isHasParent() {
        return hasParent;
    }

    public void setHasParent(boolean isParent) {
        this.hasParent = isParent;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Tree(String value, String label, Map<String, Object> state, boolean checked, Map<String, Object> attributes, List<Tree<T>> children, String parentId, boolean hasParent, boolean hasChildren) {
        this.value = value;
        this.label = label;
        this.state = state;
        this.checked = checked;
        this.attributes = attributes;
        this.children = children;
        this.parentId = parentId;
        this.hasParent = hasParent;
        this.hasChildren = hasChildren;
    }

    public Tree() {
        super();
    }

    @Override
    public String toString() {

        return JSON.toJSONString(this);
    }
}
