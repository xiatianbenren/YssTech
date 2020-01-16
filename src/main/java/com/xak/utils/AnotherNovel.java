package com.xak.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * writer: xiaankang
 * date: 2019/12/31.
 */
public class AnotherNovel extends SpiderUtil{

    public static void main(String[] args){
        int counter=1;
        String navURL = "https://m.shuhaige.com/75509";
        AnotherNovel instance = new AnotherNovel();
        while (true) {
            try {
                instance.spiderNovella(navURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            navURL=instance.nextNavPageURL(navURL);
            counter++;
        }
    }
    String nextNavPageURL(String navURL){
        String[] s = navURL.split("_");
        if (s.length<2){
            return navURL+"_2";
        }
        int page = Integer.parseInt(s[1]);
        return s[0]+"_"+(page+1);
    }

    @Override
    protected String appendAbsoluteURL(String navURL, String relativeChapterURL) {
        String[] strs = relativeChapterURL.split("/");
        //翻页会改变navURL，但是绝对路径不能使用翻页后生成的navURL
        String[] s = navURL.split("_");
        navURL=s[0];
        return navURL+"/"+strs[strs.length-1];
    }

    /**
     * 解析包含所有章节的父标签
     * @param document
     * @return
     */
    @Override
    protected Elements getChapterElements(Document document) {
        Elements chapters = document.select("div.main").select("ul.read").select("li");
        return chapters;
    }

    /**
     * 从标签中解析章节相对路径url
     * @param dd 章节标签
     * @return
     */
    @Override
    protected String getChapterRelativeURL(Element dd) {
        String relativeURL = dd.select("a").attr("href");
        return relativeURL;
    }

    /**
     * 抓取章节内容
     * @param chapterDoc 章节内容页面Doc
     * @return content
     */
    @Override
    protected String fetchContentFromChapterDoc(Document chapterDoc) {
        Elements contentEle = chapterDoc.select("div.content");
        return contentEle.text();
    }
}
