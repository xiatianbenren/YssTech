package com.xak.utils;

import java.io.*;

/**
 * writer: xiaankang
 * date: 2019/3/11.
 */
public class WipeBlankLine {

    private static String srcFile = "D:\\ChromeDownload\\all.txt";
    private static String tarFile ;

    public static void main(String[] args){
        System.out.println(generateTar(srcFile));
        wipe();
    }

    public static String generateTar(String src){
        return src.replace(".txt","_wipe.txt");
    }

    public static void wipe(){
        tarFile=generateTar(srcFile);
        File file=new File(srcFile);
        InputStream in;
        BufferedReader br;
        String tmp;
        FileWriter fw;
        int i=0;
        try {
            in=new BufferedInputStream(new FileInputStream(file));
            br=new BufferedReader(new InputStreamReader(in,"utf-8"));
            fw=new FileWriter(tarFile,true);

            while ((tmp=br.readLine())!=null){//核心：空行判断
                if(tmp.equals(""));
                else{
                    fw.write(tmp+"\n");
                    i++;
                    System.out.println(i);
                }
            }
        System.out.println("replace success!!!");
            fw.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
