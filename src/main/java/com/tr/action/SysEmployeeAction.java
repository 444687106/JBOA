package com.tr.action;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.tr.entity.SysEmployee;
import com.tr.entity.SysPosition;
import com.tr.service.SysEmployeeService;

public class SysEmployeeAction extends ActionSupport {

	// 员工业务借口
	private SysEmployeeService employeeService;
	// 员工对象
	private SysEmployee emp;
	// 职务对象
	private SysPosition position;
	// 提交方式
	private String[] button;

	// 登陆
	public String login() {
		this.emp = employeeService.login(this.emp);
		if (emp == null) {
			return INPUT;
		} else {
			ActionContext.getContext().getSession().put("emp", emp);
			this.position = emp.getSysPosition();
			ActionContext.getContext().getSession()
					.put("loginPosition", position.getNameCn());
			if (emp.getSysPosition().getNameCn().equals("员工")) {
				return SUCCESS;
			} else {
				return "admin";
			}
		}
	}

	// 获取要添加报销单的员工对应的信息显示到网页上
	public String save() {
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		return SUCCESS;
	}

	public SysEmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(SysEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public SysEmployee getEmp() {
		return emp;
	}

	public void setEmp(SysEmployee emp) {
		this.emp = emp;
	}

	public SysPosition getPosition() {
		return position;
	}

	public void setPosition(SysPosition position) {
		this.position = position;
	}

	public String[] getButton() {
		return button;
	}

	public void setButton(String[] button) {
		this.button = button;
	}
}
