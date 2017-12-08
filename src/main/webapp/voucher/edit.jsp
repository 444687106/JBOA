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

		<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="js/index.js"></script>
		<script type="text/javascript">
			$(function(){
				$('#back').click(function(){
					location = 'find.action';
				});
			});
		</script>
		<style type="text/css">
			#save,.submit_01,.del {
				cursor: pointer;
			}
		</style>

	</head>

	<body>
		<div class="top">
			<div class="global-width">
				<img src="images/logo.gif" class="logo" />
			</div>
		</div>
		<div class="status">
			<div class="global-width">
				<span class="usertype">【登录角色：<s:property value="#session.loginPosition" />】</span>
				<s:property value="emp.name" />你好！欢迎访问青鸟办公管理系统！
			</div>
		</div>
		<div class="main" style="height:100%">
			<div class="global-width">
				<div class="nav" id="nav">
						<div class="t"></div>
						<dl class="open">
							<dt onclick="this.parentNode.className=this.parentNode.className=='open'?'':'open';">报销单管理</dt>
								<dd>
									<a href="save.action" target="_self">添加报销单</a>
								</dd>
							<dd>
								<a href="find.action" target="_self">查看报销单</a>
							</dd>
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
							<dd>信息收件箱</dd>
							<dd>信息发件箱</dd>
						</dl>
					</div>
					<div class="action" style="font:12px 宋体">
					<s:fielderror />
					<form id="form1" name="form1" method="post" action="edit">
						<div class="t">报销单更新</div>
						<div class="pages">
							<!--增加报销单 区域 开始-->
							<table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
								<caption>基本信息</caption>
								<tr>
									<td width="20%">编　号: <s:property value="voucher.id" /><input type="hidden" name="id" value="<s:property value='voucher.id' />" /> </td>
									<td width="30%">填  写  人：<s:property value="voucher.sysEmployeeByCreateSn.name" /></td>
									<td width="25%">部　　门：<s:property value="voucher.sysEmployeeByCreateSn.sysDepartment.name" /></td>
									<td width="25%">职　　位：<s:property value="voucher.sysEmployeeByCreateSn.sysPosition.nameCn" /></td>
								</tr>
								<tr>
									<td width="20%">
										总金额: ￥<span id="totalPrice1"><fmt:formatNumber pattern="0.00" value="${voucher.totalAccount }" /></span>
										<input type="hidden" name="totalPrice" value="<s:property value='voucher.totalAccount' />" /> 
									</td>
									<td width="30%">填报时间：<s:property value="voucher.createTime" /></td>
									<td width="25%">状　　态：<s:property value="voucher.status" /></td>
									<td width="25%">待处理人：<s:property value="voucher.sysEmployeeByNextDealSn.name" /></td>
								</tr>
							</table>
							<p>&nbsp;</p>
		         			<p>----------------------------------------------------------------------------------------------------------------------</p>
							<table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-item">
								<thead>
									<tr>
										<td>项目</td>
										<td>金额</td>
										<td>费用说明</td>
										<td>操作</td>
									</tr>
								</thead>
								<s:iterator value="details" var="detail">
									<tr>
										<td><s:property value="#detail.item" /><input type="hidden" name="items" value="<s:property value='#detail.item' />" /></td>
										<td> ￥<fmt:formatNumber pattern="0.00" value="${account }" />
											<input type="hidden" name="accounts" value="<s:property value='#detail.account' />" />
										</td>
										<td><s:property value="#detail.des" /><input type="hidden" name="dess" value="<s:property value='#detail.des' />" /></td>
										<td class="del" >
											<img src="images/delete.gif" width="16" height="16" />
											<input type="hidden" value="<s:property value='#detail.id' />" />
										</td>
									</tr>
								</s:iterator>
								<tbody>
									<tr>
										<td>
											<select name="select" id="select" class="input_01">
												<option value="0">基本通讯费用</option>
												<option value="1">城际交通费</option>
												<option value="2">礼品费</option>
												<option value="3">交际餐费</option>
											</select>
										</td>
										<td><input type="text" name="textfield2" id="textfield2" class="input_01" /></td>
										<td><input type="text" name="textfield" id="textfield" class="input_01" /></td>
										<td id="save"><img src="images/save.gif" width="16" height="16" /></td>
									</tr>
	
									<!--报销单事由-->
									<tr>
										<td colspan="4" class="event">
											<label>事  由：</label><textarea name="textarea" id="textarea" cols="55" rows="4"><s:property value="voucher.event" /></textarea>
										</td>
									</tr>
	
									<!--表单提交行-->
									<tr>
										<td colspan="4" class="submit">
											<input type="submit" name="button" value="保 存" class="submit_01" />
											<input type="submit" name="button" value="保存并提交" class="submit_01" />
											<input class="submit_01" type="button" id="back" value="返 回" />
										</td>
									</tr>
								</tbody>
							</table>
						<!--增加报销单 区域 结束-->
						</div>
					</form>
				</div>
			</div>
		</div>

		<div class="copyright">Copyright &nbsp; &copy; &nbsp; 北大青鸟</div>

	</body>

</html>