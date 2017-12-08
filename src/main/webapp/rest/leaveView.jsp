<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
		
		<link href="css/style.css" rel="stylesheet" type="text/css" />
		<style type="text/css">
			#back{
				cursor: pointer;
			}
		</style>
		<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
		<script type="text/javascript">
			$(function(){
				$('#back').click(function(){
					location = 'restList.action';
				});
			});
		</script>
	
	</head>

	<body>
		<div class="top">
			<div class="global-width"><img src="images/logo.gif" class="logo" /></div>
		</div>
		<div class="status">
			<div class="global-width">
				<span class="usertype">【登录角色：<s:property value="#session.loginPosition" />】</span>
				<s:property value="emp.name" />你好！欢迎访问青鸟办公管理系统！
			</div>
		</div>
		<div class="main">
			<div class="global-width">
	
				<div class="nav" id="nav">
    				<div class="t"></div>
			    	<dl class="open">
			        	<dt onclick="this.parentNode.className=this.parentNode.className=='open'?'':'open';">报销单管理</dt>
			            <dd><a href="save.action" target="_self">添加报销单</a></dd>
			            <dd><a href="find.action" target="_self">查看报销单</a></dd>
			        </dl>
			        <dl>
			        	<dt onclick="this.parentNode.className=this.parentNode.className=='open'?'':'open';">请假管理</dt>
			            	<dd><a href="restList.action">查看请假</a></dd>
				            <s:if test="%{#session.loginPosition=='员工' }">
					            <dd><a href="rest.action">申请请假</a></dd>
				            </s:if>
			        </dl>
			        <dl>
			        	<dt onclick="this.parentNode.className=this.parentNode.className=='open'?'':'open';">我要销售</dt>
			            <dd>信心收件箱</dd>
			            <dd>信心发件箱</dd>
			        </dl>
   				</div>
				<div class="action">
		    		<div class="t">查看请假</div>
					<div class="pages">
		        		<!--增加报销单 区域 开始-->
		                <table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
							<caption>基本信息</caption>
								<tr>
									<td width="40%">姓　　名: <s:property value="leave.sysEmployeeByEmployeeSn.name" /> </td>
									<td width="40%">部　　门：<s:property value="leave.sysEmployeeByEmployeeSn.sysDepartment.name" /></td>
								</tr>
								<tr>
									<td width="40%">开始时间: <fmt:formatDate value="${leave.starttime }" pattern="yyyy-MM-dd HH:mm"/> </td>
									<td width="40%">结束时间：<fmt:formatDate value="${leave.endtime }" pattern="yyyy-MM-dd HH:mm"/> </td>
								</tr>
								<tr>
									<td width="40%">请假天数: <s:property value="leave.leaveday" /> </td>
									<td width="40%">休假类型：<s:property value="leave.leavetype" /></td>
								</tr>
								<tr>
									<td width="40%">请假事由: <s:property value="leave.reason" /> </td>
									<td width="40%">审批状态：<s:property value="leave.status" /></td>
								</tr>
						</table>
	         			<p>&nbsp;</p>
	         			<p>----------------------------------------------------------------------------------------------------------------------</p>
	         			<p>&nbsp;</p>
	         			<p>审批意见: </p>
	         			<p>&nbsp;</p>
	         			<p>&nbsp;</p>
	         			<p style="color:red; font-weight: bold;">
	         				<s:if test="%{leave.status == '待审批'}">
	         					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;正在审批中...
	         				</s:if>
	         				<s:else>
	         					<s:property value="leave.approveOpinion" />
	         				</s:else>
	         			</p>
	         			<p>&nbsp;</p>
	         			<p>&nbsp;</p>
	                	<input class="submit_01" type="button" id="back" value="返　回" />
					</div>
				</div>
			</div>
		</div>
	
		<div class="copyright">Copyright  &nbsp;   &copy;  &nbsp; 北大青鸟</div>
		
	</body>
	
</html>
