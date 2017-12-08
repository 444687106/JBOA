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
		<script type="text/javascript" src="js/index.js"></script>
		<script type="text/javascript">
			function checkTime(time) {
				if(time < 10){
					return "0" + time;
				} else {
					return time;
				}
			};
			function time(){
				// 获得当前时间
				var date = new Date();
				var year = date.getFullYear();
				var month = checkTime(date.getMonth());
				var day = checkTime(date.getDate());
				var hours = checkTime(date.getHours());
				var min = checkTime(date.getMinutes());
				var ss = checkTime(date.getSeconds());
				
				$('#year').text(year);
				$('#month').text(month);
				$('#day').text(day);
				$('#hh').text(hours);
				$('#min').text(min);
				$('#ss').text(ss);
			};
			$(function(){
				time();
				window.setInterval(function(){
					time();
				},1000);
			});
		</script>
		<style type="text/css">
			#save,.del,.submit_01 {
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
					<form id="form1" name="form1" method="post" action="add">
						<div class="t">报销单添加</div>
						<div class="pages">
							<!--增加报销单 区域 开始-->
							<table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-base">
								<caption>基本信息</caption>
								<tr>
									<td width="36%">填写人：<s:property value="emp.name" /></td>
									<td width="64%">填报时间：
										<span id="year" ></span>-
										<span style="margin-left:-5px" id="month" ></span>-
										<span style="margin-left:-5px" id="day" ></span>
										<span id="hh" ></span>:
										<span style="margin-left:-5px" id="min" ></span>:
										<span style="margin-left:-5px" id="ss" ></span>
									</td>
								</tr>
								<tr>
									<td>总金额：￥ 
										<span id="totalPrice1">0.00</span>
										<input type="hidden" name="totalPrice" value="0.00" >
									</td>
									<td>状　　态：新创建<input name="status" value="新创建" type="hidden"/></td>
								</tr>
							</table>
							<p>&nbsp;</p>
							<table width="90%" border="0" cellspacing="0" cellpadding="0" class="addform-item">
									<thead>
										<tr>
											<td>项目</td>
											<td>金额</td>
											<td>费用说明</td>
											<td>操作</td>
										</tr>
									</thead>
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
												<label>事  由：</label><textarea name="textarea" id="textarea" cols="55" rows="105"></textarea>
											</td>
										</tr>
		
										<!--表单提交行-->
										<tr>
											<td colspan="4" class="submit">
												<input type="submit" name="button" id="button" value="保 存" class="submit_01" />
												<input type="submit" name="button" id="button" value="保存并提交" class="submit_01" />
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