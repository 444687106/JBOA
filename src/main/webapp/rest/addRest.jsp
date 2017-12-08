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
		<link href="css/style.css" rel="stylesheet" type="text/css" />

		<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
		<script type="text/javascript">
			$(function(){
				// 表单验证
				function checkForm(){
					var day = $('#day').val();
					var start = $('#start').val();
					var end = $('#end').val();
					if (start.length == 0) {
						alert("请输入开始时间");
						return false;
					} else if(end.length == 0) {
						alert("请输入结束时间");
						return false;
					} else if(day.length == 0) {
						alert("请输入请假天数");
						return false;
					} else if (isNaN(day)) {
						alert("请假天数只能输入数字");
						return false;
					}
					return true;
				};
				// 取消
				$('#back').click(function(){
					location = 'restList.action';
				});
				// 表单提交
				$('#form1').submit(checkForm);
			});
		</script>
		<style type="text/css">
			#save,.del,.submit_01 {
				cursor: pointer;
			}
			input,select{
				width:132px;
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
						<dl>
							<dt onclick="this.parentNode.className=this.parentNode.className=='open'?'':'open';">报销单管理</dt>
								<dd>
									<a href="save.action" target="_self">添加报销单</a>
								</dd>
							<dd>
								<a href="find.action" target="_self">查看报销单</a>
							</dd>
						</dl>
						 <dl class="open">
			        	<dt onclick="this.parentNode.className=this.parentNode.className=='open'?'':'open';">请假管理</dt>
			            	<dd><a href="restList.action">查看请假</a></dd>
				            <s:if test="%{#session.loginPosition=='员工' }">
					            <dd><a href="rest.action">申请请假</a></dd>
				            </s:if>
			        </dl>
						<dl>
							<dt onclick="this.parentNode.className=this.parentNode.className=='open'?'':'open';">信息中心</dt>
							<dd>信息收件箱</dd>
							<dd>信息发件箱</dd>
						</dl>
					</div>
					<div class="action" style="font:12px 宋体">
					<form id="form1" name="form1" method="post" action="restAdd">
						<div class="t">请假申请</div>
						<div class="pages">
							<!--增加请假 区域 开始-->
							<table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
								<tr>
									<td width="45%" >姓　　名：<s:property value="emp.name" /></td>
									<td width="45%" >部　　门: <s:property value="emp.sysDepartment.name" /></td>
								</tr>
								<tr>
									<td width="45%" >开始时间：<input id="start" name="leave.starttime" /></td>
									<td width="45%" >结束时间: <input id="end" name="leave.endtime" /></td>
								</tr>
								<tr>
									<td width="45%" >请假天数：<input id="day" name="leave.leaveday" /> (天)</td>
									<td width="45%" >休假类型: 
										<select name="leave.leavetype">
											<option value="事假" >事假</option>
											<option value="婚假" >婚假</option>
											<option value="年假" >年假</option>
											<option value="病假" >病假</option>
										</select>
									</td>
								</tr>
								<tr>
									<td width="45%" >请假事由: </td>
									<td width="45%" style="display: block;margin-left: -267px;">
										<textarea name="leave.reason" rows="5" cols="63"></textarea>
									</td>
								</tr>
								<tr>
									<td colspan="2" >&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" class="submit">
										<input type="submit" name="button" id="button" value="提交流程" class="submit_01" />
										<input type="button" name="button" id="back" value="取消" class="submit_01" />
									</td>
								</tr>
							</table>
						</div>
					</form>
				</div>
			</div>
		</div>

		<div class="copyright">Copyright &nbsp; &copy; &nbsp; 北大青鸟</div>

	</body>

</html>