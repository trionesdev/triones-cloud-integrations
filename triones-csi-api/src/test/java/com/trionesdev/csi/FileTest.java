package com.trionesdev.csi;

import cn.hutool.core.io.FileUtil;
import com.trionesdev.csi.util.OssUtils;
import org.junit.jupiter.api.Test;

public class FileTest {

    @Test
    public void join_test() {
        String path = FileUtil.normalize("/sd/ss//d");
        System.out.println(path);
    }

    @Test
    public void join_test2() {
        String path = OssUtils.joinPrefix("/SS","https://");
        System.out.println(path);
    }

}
