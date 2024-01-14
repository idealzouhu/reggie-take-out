package com.idealzouhu.test;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UploadFileTest {
    @Test
    public void test1(){
        String originalFilename = "ere.jpg";
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));  // 得到文件后缀

        // 使用 UUID 重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
        System.out.println(suffix);
    }
}
