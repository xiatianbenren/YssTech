<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <%--<style type="text/css">
        /*form{
            margin:0 auto;

            width:500px;
            padding:20px;
        }*/
        body{
            background-image: url("/WEB-INF/img/bgimg1.jpg");
        }
    </style>--%>
    <title></title>
</head>

<body style="background:url(img/timg.jpg) no-repeat;background-size: cover">
<h1 style="color: red;" align="center">登录</h1>
<h1 style="color: red;" align="center">Login</h1>
<form action="${pageContext.request.contextPath }/emp/add.do" method="post">
    <%--用户名：<input name="name"/> <br/>
    用户薪资：<input name="salary"/><br/>
    <input type="submit" value="save"/>--%>

    <table border="0" align="center">
        <tr>
            <td style="color:green;font-size: larger">用户名：</td>
            <td><input name="name"/></td>
        </tr>
        <tr></tr>
        <tr>
            <td style="color:green;font-size: large">密码：</td>
            <td><input name="salary"/></td>
        </tr>
        <tr></tr>
        <tr align="center">
            <td></td>
            <td>
                <input type="submit" value="save"/>
            </td>
        </tr>
    </table>
</form>
<br/>
<a href="${pageContext.request.contextPath }/emp/getJson.do">json test</a>
</body>
</html>