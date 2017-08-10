<%@page import="com.fis.web.tools.DateTime"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>框架速度测试</title>
</head>
<body>
框架速度测试:<br/><br/>

<%SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"); %>
执行开始时间：<% out.print(sf.format(DateTime.getNowDate())+"<br/>"); %><br/>


<c:forEach items="${list}" var="aa">
	歌名:${aa.songName}，发布时间:<fmt:formatDate value="${aa.stime}" type="date" pattern="yyyy-MM-dd HH:mm"/>
	自定义排序:${aa.sortNum},idRank:${aa.idRank}<br/>
</c:forEach>

总条数:${total},当前页:${pg},一页大小:${pgsize}<br/><br/>

执行结束时间：<% out.print(sf.format(DateTime.getNowDate())+"<br/>"); %><br/>

</body>
</html>