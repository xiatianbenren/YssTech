package com.xak.utils;

import java.io.*;

/**
 * writer: xiaankang
 * date: 2019/3/11.
 */
public class WipeBlankLine {

    private static String srcPath="C:\\Users\\xiaankang\\Documents\\WeChat Files\\xia840004599\\FileStorage\\File\\2019-12\\";
    private static String srcFileName="reLiveBestEpoch_wipe";
    private static final String FILESUFFIX_TXT=".txt";
    private static final String REP_FILESUFFIX_TXT="_wipe.txt";
    private static String srcFile = srcPath+srcFileName+FILESUFFIX_TXT;
    private static final String encoding="utf-8";
    private static String tarFile ;
    private static PostProcessStrategy postProcessStrategy=PostProcessStrategy.DEL_OLD_FILE;

    public static void main(String[] args){
        System.out.println(generateTar(srcFile));
        wipe();
    }

    /**
     * wipe TASK执行完成后处理策略
     */
    enum PostProcessStrategy{
        DEL_OLD_FILE,
        REMAIN_OLD_FILE;
}
    private static void postProcess(PostProcessStrategy strategy, File file){
        if (strategy.equals(PostProcessStrategy.REMAIN_OLD_FILE))
            return;
        if (strategy.equals(PostProcessStrategy.DEL_OLD_FILE)){
            if (file.exists()){
                file.delete();
                System.out.println("File [ "+file.getName()+" ] has been delete!!");
            }
        }
    }

    /**
     * 新文件名生成
     * @param src
     * @return
     */
    public static String generateTar(String src){
        return src.replace(FILESUFFIX_TXT,REP_FILESUFFIX_TXT);
    }

    /**
     * 按行读文件，遇到空行跳过读下一行，否则写入新文件
     */
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
            br=new BufferedReader(new InputStreamReader(in,encoding));
            fw=new FileWriter(tarFile,true);
            while ((tmp=br.readLine())!=null){//核心：空行判断
                if(tmp.trim().equals(""));
                else{
                    fw.write(tmp+"\n");
                    i++;
                    System.out.println("当前已处理："+i+" 行数据！！");
                }
            }
            fw.close();
            in.close();
            System.out.println("The file [ "+srcFileName+" ] wipe blank line task success!!!");
            //执行完wipe task后的处理工作
            postProcess(postProcessStrategy,file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
