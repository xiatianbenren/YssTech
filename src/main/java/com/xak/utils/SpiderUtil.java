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

import static java.lang.System.currentTimeMillis;

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

    private static String savePath ="D:\\ChromeDownload\\spider\\mySpiderResult.txt";
    private static String navURL ="75_75933/";
    private static String website ="https://www.xsbiquge.com/";
    public static File file=new File(savePath);
    private static int WriteCounter=1,fileSequence=0,getDocCount=0;
    private static final String ERR_FLAG="Error to skip";
    private static final int MAX_FILE_SIZE=600*1024;

    public static void main(String[] args){
        SpiderUtil instance=new SpiderUtil();
        try {
            long start = currentTimeMillis();
            instance.spiderNovella(website + navURL);
            long end= currentTimeMillis();
            System.out.println("本次任务执行完毕,共耗时 "+(end-start)/1000+"秒");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**（模板方法模式：抽取每个网站不同的匹配步骤，交由子类实现）
     * 1.章节导航页获取章节url
     * 2.章节内容页抓取内容并写文件
     * @param navUrl 章节导航页 url
     * @throws IOException
     */
    void spiderNovella(String navUrl) throws IOException {
        Connection con=Jsoup.connect(navUrl);
        System.out.println("连接 导航【"+navUrl+"】成功！");
        //Document document = getDoc(con);
        Document document=con.get();
        //Need to be overwite
        Elements dds = this.getChapterElements(document);
        if (dds.size()<=0){
            throw new RuntimeException("当前导航页没有章节标签，爬虫解析结束！");
        }
        this.checkDirectory();
        for (Element dd : dds) {
            String relativeChapterURL = this.getChapterRelativeURL(dd);
            String chapterName = dd.select("a").text();
            System.out.println(dd.select("a").text());
            write2MyFile(chapterName);
            //拼接绝对路径=网站url+章节url
            String absoluteURL=this.appendAbsoluteURL(navUrl,relativeChapterURL);
            System.out.println(String.format("章节名：【%s】 - url: %s",chapterName,absoluteURL));

            Connection connect = Jsoup.connect(absoluteURL);
            Document contentDoc = connect.get();
            String content=this.fetchContentFromChapterDoc(contentDoc);
            if (ERR_FLAG.equals(content)) {
                System.out.println("");
                write2MyFile(content);
                continue;
            }
            if (content.equals("")) {
                throw new RuntimeException("The content fetch processing failed,please check agin!!");
            }
            write2MyFile(content);
            checkFile();
        }
    }

    protected String appendAbsoluteURL(String navUrl, String relativeChapterURL) {
        return navUrl + relativeChapterURL;
    }

    //重写此方法，定义获取章节elements规则
    protected Elements getChapterElements(Document document){
        Elements dds = document.getElementById("list").select("dl").select("dd");
        return dds;
    }

    /**
     * 从标签中解析章节url
     * @param dd 章节标签
     * @return
     */
    protected  String getChapterRelativeURL(Element dd){
        //章节url（相对路径）
        String relativeChapterURL = dd.select("a").attr("href");
        return relativeChapterURL;
    }

    protected  String fetchContentFromChapterDoc(Document contentDoc){
        String content = "";
        try {
            content=contentDoc.getElementById("content").text();
        } catch (Exception e) {
            content = ERR_FLAG;
        }
        return content;
    }

    @Deprecated
    private static Document getDoc(Connection con){
        try {
            return con.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
       /* Document doc=null;
        try {
            getDocCount++;
            System.out.println("第"+WriteCounter+"次写入,"+"第"+getDocCount+"次获取doc。。。");
            doc=con.get();
        } catch (Exception e) {
            if (getDocCount>5){
                throw new RuntimeException("获取Doc次数已达上限，获取失败");
            }
            getDoc(con);
        }
        //获取成功，重置0
        System.out.println("第"+WriteCounter+"次写入获取doc成功！");
        getDocCount=0;
        return doc;*/
    }

    /**
     * 校验父文件夹
     * 一次任务校验一次即可
     */
    private static void checkDirectory(){
        //文件夹不存在则创建
        File fileParentDir=new File(file.getParent());
        if (!fileParentDir.exists()){
            //mkdirs()可以创建多级目录，父目录不一定存在。
            fileParentDir.mkdirs();
        }
        if (file.exists()){
            file.delete();
        }
    }

    /**
     * 正文部分标签匹配
     * @param document
     * @return
     */
    private static String fetchContentElement(Document document){
        Elements contentEle = document.select("div").get(0).select(".content_read")
                .select(".box_con").select("#content");
        return contentEle.text();
    }
    /**
     * 使用带缓冲区的输出流，将爬取的文本写入文件
     * @param input wenben
     */
    private static void write2MyFile(String input){
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
        if (file.length()>MAX_FILE_SIZE){
            file=new File(savePath.replace(".txt",fileSequence+".txt"));
            System.out.println(String.format("当前文件大小已满，创建新文件：%s",file.getName()));
            fileSequence++;
        }
    }


}
