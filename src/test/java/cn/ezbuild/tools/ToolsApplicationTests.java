package cn.ezbuild.tools;

import cn.ezbuild.tools.entity.Menu;
import cn.ezbuild.tools.utils.TreeUtils;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration
class ToolsApplicationTests {

    @Test
    void contextLoads() {
        List<Menu> menuList = new ArrayList<>();
        menuList.add(Menu.builder().id(1).parentId(0).name("1").build());
        menuList.add(Menu.builder().id(2).parentId(0).name("2").build());
        menuList.add(Menu.builder().id(3).parentId(0).name("3").build());
        menuList.add(Menu.builder().id(4).parentId(0).name("4").build());
        menuList.add(Menu.builder().id(5).parentId(1).name("1-1").build());
        menuList.add(Menu.builder().id(6).parentId(1).name("1-2").build());
        menuList.add(Menu.builder().id(7).parentId(2).name("2-1").build());
        menuList.add(Menu.builder().id(8).parentId(2).name("2-2").build());
        menuList.add(Menu.builder().id(9).parentId(3).name("3-1").build());
        menuList.add(Menu.builder().id(10).parentId(9).name("3-1-1").build());
        menuList.add(Menu.builder().id(11).parentId(9).name("3-1-2").build());

        menuList = new TreeUtils<>(menuList).buildTree();

        System.out.println(JSONUtil.toJsonPrettyStr(menuList));
    }

}
