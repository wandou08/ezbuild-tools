package cn.ezbuild.tools.utils;


import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

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
@UtilityClass
public class TreeUtils {

    /**
     * 功能描述
     * <p>
     * 建立树形结构
     * 默认 list 内的属性关系为
     * Integer id 主键
     * Integer parentId 关联父级id
     * java.util.List<T> children 子集
     * 起始节点为父级id为0
     * </p>
     *
     * @param <T> 原数据类
     * @param menuList 需要格式化的list
     * @return java.util.List
     * @since 0.0.1
     */
    public static <T>List<T> buildTree(List<T> menuList) {
        return buildTree(menuList, 0, "id", "parentId", "children");
    }

    /**
     * 功能描述
     * <p>
     * 建立树形结构
     * 默认 list 内的属性关系为
     * Integer id 主键
     * Integer parentId 关联父级id
     * java.util.List<T> children 子集
     * </p>
     *
     * @param <T> 原数据类
     * @param menuList 需要格式化的list
     * @param rootId 顶级id
     * @return java.util.List
     * @since 0.0.1
     */
    public static <T>List<T> buildTree(List<T> menuList, Integer rootId) {
        return buildTree(menuList, rootId, "id", "parentId", "children");
    }

    /**
     * 功能描述
     * <p>
     * 建立树形结构
     * </p>
     *
     * @param <T> 原数据类
     * @param menuList 需要格式化的list
     * @param rootId 顶级id 默认 0
     * @param idFieldName 主键字段名 默认 id
     * @param parentIdFieldName 父级id字段名 默认 parentId
     * @param childFieldName 子集合字段名 默认 children
     * @return java.util.List
     * @since 0.0.1
     */
    public static <T>List<T> buildTree(List<T> menuList, Integer rootId, String idFieldName, String parentIdFieldName, String childFieldName) {
        List<T> treeMenus = new ArrayList<>();
        for (T menuNode : getRootNode(menuList, parentIdFieldName, rootId)) {
            buildChildTree(menuNode, menuList, idFieldName, parentIdFieldName, childFieldName);
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
     * @param <T> 原数据类
     * @param pNode 父节点
     * @param menuList 需要格式化的list
     * @param idFieldName 主键字段名
     * @param parentIdFieldName 父级id字段名
     * @param childFieldName 子集合字段名
     * @return T
     * @since 0.0.1
     */
    @SneakyThrows
    private static <T>T buildChildTree(T pNode, List<T> menuList, String idFieldName, String parentIdFieldName, String childFieldName) {
        List<T> childMenus = new ArrayList<>();
        Class<?> pNodeClass = pNode.getClass();

        Integer id = (Integer) ReflectUtil.getFieldValue(pNode, idFieldName);

        for (T menuNode : menuList) {
            Integer parentId = (Integer) ReflectUtil.getFieldValue(menuNode, parentIdFieldName);
            if (parentId != null && parentId.equals(id)) {
                childMenus.add(buildChildTree(menuNode, menuList, idFieldName, parentIdFieldName, childFieldName));
            }
        }
        Method method = ReflectUtil.getMethodByName(pNodeClass, "set" + StrUtil.upperFirst(childFieldName));
        method.invoke(pNode, childMenus);
        return pNode;
    }

    /**
     * 功能描述
     * <p>
     * 获取根节点
     * </p>
     *
     * @param <T> 原数据类
     * @param menuList 需格式化的list
     * @param parentIdFieldName 父级id字段
     * @param rootId 设置顶级id
     * @return java.util.List
     * @since 0.0.1
     */
    private static <T>List<T> getRootNode(List<T> menuList, String parentIdFieldName, Integer rootId) {
        List<T> rootMenuLists = new ArrayList<>();
        for (T menuNode : menuList) {
            Integer parentId = (Integer) ReflectUtil.getFieldValue(menuNode, parentIdFieldName);
            if (rootId == null) {
                if (parentId == null || parentId == 0) {
                    rootMenuLists.add(menuNode);
                }
            } else if (rootId.equals(parentId)) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }
}
