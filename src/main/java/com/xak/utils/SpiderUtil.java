package com.xak.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Jsoup标签匹配规则和jQuery基本一致
 * class属性用"."，id属性用"#",标签名直接写名字，获取标签属性用Element.attr("属性名")
 *
 * 受网络波动影响，偶发性得会出现socekt timeout
 *
 * writer: xiaankang
 * date: 2019/4/3.
 */
public class SpiderUtil {
//TODO:断点续写，offset设计
//TODO:任务写入结束后续操作，如：写入完成后更改File parent文件夹名字为书名并压缩，若压缩包大小过大放入其他路径
    private static String path="D:\\ChromeDownload\\spider1\\mySpiderResult.txt";
    private static String spideUrl="https://www.bequge.com/30_30046/";
    private static File file=new File(path);
    private static int WriteCounter=0;
    private static int fileSequence=0;
    private static int fileParentSequence=0;
    private static int getDocCount=0;

    public static void main(String[] args){

        try {
            spiderNovella();
            System.out.println("本次任务执行完毕~~~");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private static Connection getConn(String url,int timeout){
        Connection con=null;
        try {
            con=Jsoup.connect(url).timeout(timeout);
        }catch (Exception e){
            getConn(url,timeout);
        }
        return con;
    }*/

    private static Document getDoc(Connection con){
        Document doc=null;
        try {
            getDocCount++;
            System.out.println("第"+WriteCounter+"次写入,"+"第"+getDocCount+"次获取doc。。。");
            doc=con.get();
        } catch (Exception e) {
            getDoc(con);
        }
        //获取成功，重置0
        getDocCount=0;
        return doc;
    }

    /**
     * 从笔趣阁爬取数据
     * www.bequge.com
     *
     * @throws IOException
     */
    private static void spiderNovella() throws IOException {
        //String url="https://www.bequge.com/30_30046/";
        //增大网络超时时间，减小网络波动的影响
        Connection con=Jsoup.connect(spideUrl).timeout(100000);
        //递归获取doc，防止读取超时
        Document document = getDoc(con);
        Element ele = document.getElementById("list");
        Elements dds = ele.select("dl").select("dd");

        //校验文件路径是否存在
        checkFileParent();

        for (Element dd: dds){
            //章节url（相对路径）
            String chapterUrl=dd.select("a").attr("href");
            String chapterName=dd.select("a").text();
            System.out.println(dd.select("a").text());

            //爬取每一章正文

            //拼接绝对路径
            chapterUrl="https://www.bequge.com"+chapterUrl;
            Connection connect = Jsoup.connect(chapterUrl);
            Document document1 = getDoc(connect);
           /* Elements select = document.select("div").get(0).select(".content_read").select(".box_con").
                    select(".bookname").
                    select("h1");
            String chpterName=select.text();*/
            Elements contentEle = document.select("div").get(0).select(".content_read").select(".box_con").
                    select("#content");
            String content=contentEle.text();

            //每次写校验文件是否存在，大小是否超标
            write2MyFile(chapterName);
            write2MyFile(content);
        }
    }

   /* private static void spideChapter(String chapterUrl){
        //拼接绝对路径
        chapterUrl="https://www.bequge.com"+chapterUrl;
        Connection connect = Jsoup.connect(chapterUrl);
        try {
            Document document = connect.get();
            Elements select = document.select("div").get(0).select(".content_read").select(".box_con").
                    select(".bookname").
                    select("h1");
            String chpterName=select.text();
            write2MyFile(chpterName);
            Elements contentEle = document.select("div").get(0).select(".content_read").select(".box_con").
                    select("#content");
            String content=contentEle.text();
            write2MyFile(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 使用带缓冲区的输出流，将爬取的文本写入文件
     * @param input wenben
     */
    private static void write2MyFile(String input){

        checkFile();

        BufferedWriter bw=null;
        try {
            //new FileWriter(fileName,isAppend),true为追加写入文本
             bw = new BufferedWriter(new FileWriter(file,true),1024);
            if(input.length() > 0){
                bw.write(input);
                //写完一段文本后换行，下次追加写入时从新行写入
                bw.newLine();
            }
            bw.flush();
            WriteCounter++;
            System.out.println("第"+WriteCounter+"次写入完成。。。");

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert bw != null;
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 文件大小校验
     * 每次写子任务之前校验，所以实际文件大小有可能超过800m
     */
    private static void checkFile(){
        //如果文件不存在则创建
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //文件过大会对写入效率造成较大影响，所以定时切割文件
        //经测试，文件达到800M时速度下降明显
        if (file.length()>800*1024){
            file=new File(path.replace(".txt",fileSequence+".txt"));
            fileSequence++;
        }
    }

    /**
     * 校验父文件夹
     * 一次任务校验一次即可
     */
    private static void checkFileParent(){
        //文件夹不存在则创建
        File fileParentDir=new File(file.getParent());
        if (!fileParentDir.exists()){
            //mkdirs()可以创建多级目录，父目录不一定存在。
            fileParentDir.mkdirs();
        }
    }
}
