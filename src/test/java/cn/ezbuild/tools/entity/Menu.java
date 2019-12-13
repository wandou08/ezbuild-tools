package cn.ezbuild.tools.entity;

import cn.hutool.core.collection.CollUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *  测试目录类
 * </p>
 *
 * @author wandoupeas
 * @since 0.0.1
 */
@Data
@Builder
public class Menu  {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 父级目录id
     */
    private Integer parentId;

    /**
     * 目录中文名
     */
    private String name;

    private List<Menu> children;

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", children=" + (CollUtil.isNotEmpty(children) ? children.size() : 0) +
                '}';
    }
}
