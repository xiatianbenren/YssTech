package com.xak.utils;

import java.io.File;
import java.io.IOException;

/**
 * writer: xiaankang
 * date: 2019/4/4.
 */
public class MainTest1 {
    public static void main(String[] args) throws IOException {
        File file =new File("D:/ChromeDownload/spider/mySpiderResult.txt");
        System.out.println(file.getCanonicalPath());
        System.out.println(file.getParent());
    }
}
