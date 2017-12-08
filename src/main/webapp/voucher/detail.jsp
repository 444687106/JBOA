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
					location = 'find.action';
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
				            <s:if test="%{#session.loginPosition == '员工' }">
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
		    		<div class="t">查看报销单</div>
					<div class="pages">
		        		<!--增加报销单 区域 开始-->
		                <table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
							<caption>基本信息</caption>
								<tr>
									<td width="20%">编　号: <s:property value="voucher.id" /> </td>
									<td width="35%">填  写  人：<s:property value="voucher.sysEmployeeByCreateSn.name" /></td>
									<td width="25%">部　　门：<s:property value="voucher.sysEmployeeByCreateSn.sysDepartment.name" /></td>
									<td width="25%">职　　位：<s:property value="voucher.sysEmployeeByCreateSn.sysPosition.nameCn" /></td>
								</tr>
								<tr>
									<td width="20%">总金额: ￥<fmt:formatNumber pattern="0.00" value="${voucher.totalAccount }" /> </td>
									<td width="35%">填报时间：<s:property value="voucher.createTime" /></td>
									<td width="25%">状　　态：<s:property value="voucher.status" /></td>
									<s:if test="%{voucher.sysEmployeeByNextDealSn == null}">
										<td width="25%">待处理人：&nbsp;&nbsp;&nbsp;</td>
									</s:if>
									<s:else>
										<td width="25%">待处理人：<s:property value="voucher.sysEmployeeByNextDealSn.name" /></td>
									</s:else>
								</tr>
						</table>
	         			<p>&nbsp;</p>
	         			<p>----------------------------------------------------------------------------------------------------------------------</p>
	         			<p>&nbsp;</p>
						<table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
							<tr>
								<td>项目类别</td>
								<td>项目金额</td>
								<td>费用说明</td>
							</tr>
							<s:iterator value="details" var="detail">
								<tr>
									<td> <s:property value="#detail.item" /> </td>
									<td>￥<fmt:formatNumber pattern="0.00" value="${account }" /> </td>
									<td> <s:property value="#detail.des" /> </td>
								</tr>
							</s:iterator>
						</table>
 	                	<p>&nbsp;</p>
 	                	<p>----------------------------------------------------------------------------------------------------------------------</p>
	                	<p>&nbsp;</p>
 	                	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
 	                		<s:iterator value="resultList" var="list" >
	 			                <tr>
	 			                    <td width="20%">审　批　人：<s:property value="#list.sysEmployee.name" />(<s:property value="#list.sysEmployee.sysPosition.nameCn" />) </td>
	 			                    <td width="25%">审 批 时 间：<s:property value="#list.checkTime" /> </td>
	 			                    <td width="15%">审  核：<span class="red"><strong><s:property value="#list.result" /> </strong></span> </td>
	 			                </tr>
	 			                    <td width="20%">审 核 意 见：<span class="red"><strong><s:property value="#list.comm" /> </strong></span></td>
	 			                <tr>
	 			                </tr>
	 			                <tr>
	 			                	<td colspan="5" >----------------------------------------------------------------------------------------------------------------------</td>
	 			                </tr>
 	                		</s:iterator>
		                </table>
		                <p>&nbsp;</p>  
		                <input class="submit_01" type="button" id="back" value="返　回" />      
		            	<!--增加报销单 区域 结束-->
					</div>
				</div>
			</div>
		</div>
	
		<div class="copyright">Copyright  &nbsp;   &copy;  &nbsp; 北大青鸟</div>
		
	</body>
	
</html>
