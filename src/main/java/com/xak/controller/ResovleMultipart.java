package com.xak.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * writer: xiaankang
 * date: 2019/7/25.
 */
@Component
public class ResovleMultipart {

    public void resolveMulripartParam(HttpServletRequest request){
        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
        String channel = req.getParameter("channel");

    }
}
