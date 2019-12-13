package cn.ezbuild.tools.utils;


import cn.hutool.core.util.ReflectUtil;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 * <p>
 *    树结构工具类
 * </p>
 *
 * @author wandoupeas
 * @since 0.0.1
 */
public class TreeUtils<T> {
    private List<T> menuList;

    public TreeUtils(List<T> menuList) {
        this.menuList = menuList;
    }

    /**
     * 功能描述
     * <p>
     * 建立树形结构
     * </p>
     *
     * @return java.util.List
     * @author wandoupeas
     * @date 2019-12-12
     * @since 0.0.1
     */
    public List<T> buildTree() {
        List<T> treeMenus = new ArrayList<T>();
        for (T menuNode : getRootNode()) {
            buildChildTree(menuNode);
            treeMenus.add(menuNode);
        }
        return treeMenus;
    }

    /**
     * 功能描述
     * <p>
     * 递归，建立子树形结构
     * </p>
     *
     * @param pNode 父节点
     * @return T
     * @author wandoupeas
     * @date 2019-12-12
     * @since 0.0.1
     */
    @SneakyThrows
    private T buildChildTree(T pNode) {
        List<T> childMenus = new ArrayList<T>();
        Class<?> pNodeClass = pNode.getClass();

        Integer id = (Integer) ReflectUtil.getFieldValue(pNode, "id");
        Integer pId = (Integer) ReflectUtil.getFieldValue(pNode, "pId");

        for (T menuNode : menuList) {
            Integer parentId = (Integer) ReflectUtil.getFieldValue(menuNode, "parentId");
            if (parentId != null && parentId.equals(id)) {
                childMenus.add(buildChildTree(menuNode));
            }
        }
        Method method = ReflectUtil.getMethodByName(pNodeClass, "setChildren");
        method.invoke(pNode, childMenus);
//        ReflectUtil.setFieldValue(pNodeClass, "children", childMenus);
        return pNode;
    }

    /**
     * 功能描述
     * <p>
     * 获取根节点
     * </p>
     *
     * @return java.util.List
     * @author wandoupeas
     * @date 2019-12-12
     * @since 0.0.1
     */
    private List<T> getRootNode() {
        List<T> rootMenuLists = new ArrayList<T>();
        for (T menuNode : menuList) {
            Integer parentId = (Integer) ReflectUtil.getFieldValue(menuNode.getClass(), "parentId");
            if (parentId == null || parentId.equals(0)) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }
}
