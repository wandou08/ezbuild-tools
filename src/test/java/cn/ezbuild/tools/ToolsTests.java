package cn.ezbuild.tools;

import cn.ezbuild.tools.entity.Menu;
import cn.ezbuild.tools.utils.QiniuUtils;
import cn.ezbuild.tools.utils.TreeUtils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.junit.Test;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ToolsTests {

    @Test
    public void testTree() {
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

        menuList = TreeUtils.buildTree(menuList, null);

        System.out.println(JSONUtil.toJsonPrettyStr(menuList));
    }

    @Test
    @SneakyThrows
    public void testQiniu() {
//        File file = FileUtil.newFile("xxx.jpg");
//        System.out.println(QiniuUtils.upload(file,
//                                             "xxx",
//                                             "xxx",
//                                             "xxx",
//                                             "xxx"));
//        System.out.println(QiniuUtils.uploadWithName(file,
//                                             "xxx.jpg",
//                                             "xxx",
//                                             "xxx",
//                                             "xx",
//                                             "xx"));
    }

}
