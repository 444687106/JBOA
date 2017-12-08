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
			.search,.edit,#search,.sub{
				cursor: pointer;
			}
			#filter td {
				padding-right: 5px;
			}
			#page a{
				text-decoration: none;
			}
			.list td {
				text-align: center;
				border:1px #ccc solid;
			}
			#filter{
				margin-bottom: 15px;
			}
		</style>
		<script type="text/javascript" src="js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="js/rest.js"></script>
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
		<div class="main" style="height:66%" >
			<div class="global-width">
				<div class="nav" id="nav">
			    	<div class="t"></div>
			    	<dl>
			        	<dt onclick="this.parentNode.className=this.parentNode.className=='open'?'':'open';">报销单管理</dt>
			        	<s:if test="%{#session.loginPosition=='员工' }">
			            	<dd><a href="save.action" target="_self">添加报销单</a></dd>
			            </s:if>
			            <dd><a href="find.action" target="_self">查看报销单</a></dd>
			        </dl>
			        <dl class="open">
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
			    	<div class="t">请假列表</div>
			   		<div class="pages">
				    	<div id="filter" >
			    			<table>
			    				<tr>
			    					<td> 请假类型  </td>
			    					<td>
			    						<select name="type" id="type" >
			    							<option value="-1">全部</option>
			    							<option value="1" >事假</option>
											<option value="2" >婚假</option>
											<option value="3" >年假</option>
											<option value="4" >病假</option>
			    						</select>
			    					</td>
			    					<td>开始时间</td>
			    					<td><input id="startDate" /> </td>
			    					<td>结束时间</td>
			    					<td><input id="endDate" /></td>
			    					<td class="submit">
										<input type="button" name="button" id="search" value="查 询 " class="submit_01" />
									</td>
			    				</tr>
			    			</table>
				    	</div>
			        	<!--报销单列表 区域 开始-->
			                <table width="90%" border="0" cellspacing="0" cellpadding="0" class="list">
			                  <tr>
			                    <td>编号</td>
			                    <td>名称</td>
			                    <td>发起时间</td>
			                    <td>审批时间</td>
			                    <td>审批意见</td>
			                    <td>审批状态</td>
			                    <td>操作</td>
			                  </tr>
			                  <s:iterator value="leaveList" var="leave" >
				                  <tr class="leave">
				                    <td><s:property value="#leave.id" /></td>
				                    <td><s:property value="#leave.sysEmployeeByEmployeeSn.name" />请假<fmt:formatNumber pattern="0.0" value="${leaveday }" />天</td>
				                    <td><s:property value="#leave.createtime" /></td>
				                    <td style='width:151px;' ><s:property value="#leave.modifytime" /></td>
				                    <td><s:property value="#leave.approveOpinion" /></td>
				                    <td><s:property value="#leave.status" /> </td>
				                    <td>
				                    	<img class="search" src="images/search.gif" width="16" height="16" />
				                    	<s:if test="%{#session.loginPosition != '员工'}">
				                    		<s:if test="%{#leave.status == '待审批'}">
					                    		<img class="sub" src="images/sub.gif" width="16" height="16" /> 
				                    		</s:if>
					                    	<s:else>
					                    		<img src="images/empty.png" width="16" height="16" /> 
					                    	</s:else>
				                    	</s:if>
				                    </td>
			                  </tr>
			                  </s:iterator>	
			                  <tr id="page" >
			                    <td colspan="7" align="center">
			                    	<s:if test="pageNo == 1 && totalPage == 1">
			                    		<span>&nbsp;&nbsp;</span>
				                    	<span>&nbsp;&nbsp;&nbsp;</span>
			                    		<span>&nbsp;&nbsp;&nbsp;</span>
				                    	<span>&nbsp;&nbsp;</span>
			                    	</s:if>
			                    	<s:elseif test="pageNo == 1">
				                    	<span>&nbsp;&nbsp;</span>
				                    	<span>&nbsp;&nbsp;&nbsp;</span>
				                    	<a id="next" href="javascript:;">&nbsp;下一页&nbsp;</a>
				                    	<a id="last" href="javascript:;">&nbsp;末 页&nbsp;</a>
			                    	</s:elseif>
			                    	<s:elseif test="pageNo != 1 && pageNo < totalPage">
				                    	<a id="home" href="javascript:;">&nbsp;首 页&nbsp;</a>
				                    	<a id="prev" href="javascript:;">&nbsp;上一页&nbsp;</a>
				                    	<a id="next" href="javascript:;">&nbsp;下一页&nbsp;</a>
				                    	<a id="last" href="javascript:;">&nbsp;末 页&nbsp;</a>
			                    	</s:elseif>
			                    	<s:elseif test="pageNo==totalPage">
				                    	<a id="home" href="javascript:;">&nbsp;首 页&nbsp;</a>
				                    	<a id="prev" href="javascript:;">&nbsp;上一页&nbsp;</a>
				                    	<span>&nbsp;&nbsp;&nbsp;</span>
				                    	<span>&nbsp;&nbsp;</span>
			                    	</s:elseif>
			                    	<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
			                    	<span id="pageInfo" >
			                    		第 <span id="curPage" ><s:property value="pageNo" /></span> 页 / 
			                    		共 <span id="totalPage" ><s:property value="pageCount" /></span> 页 
			                    	</span>
			                    	<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			                    	共 <span id="total" ><s:property value="total" /></span> 条记录 
			                    </td>
			                  </tr>
			                </table>        
			            <!--增加报销单 区域 结束-->
			        </div>
			    </div>
			</div>
		</div>
		<div class="copyright">Copyright  &nbsp;   &copy;  &nbsp; 北大青鸟</div>	
	</body>
	
</html>
