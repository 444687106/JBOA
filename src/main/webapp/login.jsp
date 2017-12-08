<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>北大青鸟办公自动化管理系统</title>
	
		<style type="text/css">
			* {
				margin: 0;
				padding: 0;
			}
			
			body {
				font: 12px 宋体;
				background: #4BB8EF url(images/bg.gif) repeat-x;
			}
			
			img {
				border: 0;
			}
			
			.login-top {
				width: 100%;
				height: 186px;
				margin: 147px auto 0;
				background: url(images/login_01.gif) no-repeat center 0;
			}
			
			.login-area {
				width: 100%;
				height: 140px;
				margin: 0 auto;
				background: url(images/login_02.gif) no-repeat center 0;
			}
			
			.login-area form {
				width: 290px;
				margin: 0 auto;
			}
			
			.login-area label {
				clear: left;
				float: left;
				margin-top: 13px;
				width: 60px;
				font: 600 14px 宋体;
			}
			
			.login-area input {
				width: 124px;
				height: 18px;
				margin-top: 11px;
				border: 1px #767F94 solid;
				font: 12px/16px 宋体;
			}
			
			input.login-sub {
				width: 204px;
				height: 34px;
				border: 0;
				background: url(images/login_sub.gif) no-repeat 90px 1px;
				*margin-top: 5px;
			}
			
			.login-copyright {
				width: 100%;
				height: 30px;
				margin: 18px auto 0;
				background: url(images/copyright.gif) no-repeat center 0;
			}
		</style>

	</head>

	<body>
		<div class="login-top"></div>
		<div class="login-area">
			<form action="login.action" method="post">
		    	<label>工&nbsp;&nbsp;号：</label><input type="text" name="emp.sn" />
		        <label>密&nbsp;&nbsp;码：</label><input type="password" name="emp.password" />
		        <input type="submit" class="login-sub" value="" />
		    </form>
		</div>
		<div class="login-copyright"></div>
	</body>
</html>
