package com.tr.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.apache.xml.internal.utils.IntVector;
import com.tr.entity.BizLeave;
import com.tr.entity.SysDepartment;
import com.tr.entity.SysEmployee;
import com.tr.entity.SysPosition;
import com.tr.service.BizLeaveService;
import com.tr.service.SysEmployeeService;
import com.tr.service.SysPositionService;

/**
 * 请假控制器
 * 
 */
public class BizLeaveAction extends ActionSupport {
	// 请假业务接口
	private BizLeaveService leaveService;
	// 员工业务接口
	private SysEmployeeService employeeService;
	// 职务业务接口
	private SysPositionService positionService;

	// 请假对象
	private BizLeave leave;
	// 员工对象
	private SysEmployee emp;
	// 部门对象
	private SysDepartment dept;
	// 请假集合
	private List<BizLeave> leaveList;
	// 主键编号
	private Long id;
	// 操作类型
	private String opr;
	// 审批意见
	private String approveOpinion;
	// 审批结果
	private String[] button;
	// 请假类型
	private String type;
	// 开始时间
	private Date start;
	// 结束时间
	private Date end;
	// 输出流
	private InputStream inputStream;
	// 总记录数
	private Long total;
	// 当前页
	private Integer pageNo;
	// 每页大小
	private Integer pageSize = 5;
	// 总页数
	private Integer pageCount;

	// 获得员工及相关信息
	public String rest() {
		// 从会话中获得员工
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		// 重新获得员工保存到会话
		this.emp = employeeService.getEmployeeBySn(this.emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		return SUCCESS;
	}

	// 新增
	public String restAdd() {
		// 从会话中获取员工
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		// 重新获得员工保存到会话
		this.emp = employeeService.getEmployeeBySn(this.emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		this.leave.setSysEmployeeByEmployeeSn(this.emp);
		this.leave.setCreatetime(new Date());
		this.leave.setStatus("待审批");
		// 下一审批人
		SysEmployee employee = null;
		// 获得该员工所在部门的所有员工并遍历出部门经理
		for (SysEmployee item : (Set<SysEmployee>) this.emp.getSysDepartment()
				.getSysEmployees()) {
			if (item.getSysPosition().getNameCn().equals("部门经理")) {
				employee = item;
				break;
			}
		}
		if (employee == null) {
			// 如果遍历完该部门没有部门经理 ,则交由总经理审批
			for (SysPosition item : positionService.getAll()) {
				if (item.getNameCn().equals("总经理")) {
					List<SysEmployee> empList = new ArrayList<SysEmployee>(
							item.getSysEmployees());
					employee = empList.get(0);
				}
			}
		}
		this.leave.setSysEmployeeByNextDealSn(employee);
		// 添加到数据库
		leaveService.addNewLeave(this.leave);

		return SUCCESS;
	}

	// 查看请假列表
	public String restList() {
		// 查询条件
		Map<String, Object> conditions = new HashMap<String, Object>();
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		this.emp = employeeService.getEmployeeBySn(this.emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		// 获得条件保存到 map 中
		conditions.put("position",
				ActionContext.getContext().getSession().get("loginPosition"));
		conditions.put("empNo", emp.getSn());
		conditions.put("deptNo", this.emp.getSysDepartment().getId());
		// 将条件保存到会话
		ActionContext.getContext().getSession().put("conditions", conditions);
		// 如果当前页为空,则为第一页
		if (pageNo == null) {
			pageNo = 1;
		}
		// 获得总记录数
		this.total = leaveService.getTotal(conditions);
		// 计算总页数
		this.pageCount = this.total.intValue() % this.pageSize == 0 ? this.total
				.intValue() / this.pageSize
				: this.total.intValue() / this.pageSize + 1;
		// 如果没有记录, 就将总页数设为 1
		if (this.pageCount == 0) {
			this.pageCount = 1;
		}
		// 查询数据并保存
		this.leaveList = leaveService
				.getLeaveList(conditions, pageNo, pageSize);
		return SUCCESS;
	}

	// 异步请求
	public String restJson() throws IOException {
		// 筛选条件
		Map<String, Object> conditions = new HashMap<String, Object>();
		// 获得员工
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		// 避免懒加载
		this.emp = employeeService.getEmployeeBySn(this.emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		// 判断是否是查询
		if (this.pageNo == null) { // 查询
			// 将当前页设为第一页
			this.pageNo = 1;
			// 设置条件
			conditions.put("type", type);
			conditions.put("startDate", start);
			conditions.put("endDate", end);
			conditions.put("position", this.emp.getSysPosition().getNameCn());
			conditions.put("deptNo", this.emp.getSysDepartment().getId());
			conditions.put("empNo", this.emp.getSn());
		} else { // 翻页
			// 从上次查询条件中获得条件筛选
			conditions = (Map<String, Object>) ActionContext.getContext().getSession().get("conditions");
		}
		// 将当前筛选条件保存到会话
		ActionContext.getContext().getSession().put("conditions", conditions);
		// 获得总记录数
		this.total = leaveService.getTotal(conditions);
		// 计算总页数
		this.pageCount = this.total.intValue() % this.pageSize == 0 ? this.total.intValue() / this.pageSize :
						this.total.intValue() / this.pageSize + 1;
		// 如果没有记录, 就将总页数设为 1
		if (this.pageCount == 0) {
			this.pageCount = 1;
		}
		// 获得数据
		this.leaveList = leaveService.getLeaveList(conditions, pageNo, pageSize);
		
		// 拼接网页
		StringBuffer buffer = new StringBuffer("");
		for (BizLeave item : this.leaveList) {
			buffer.append("<tr class='leave'>");
			buffer.append("<td>"+ item.getId() +"</td>");
			buffer.append("<td>"+ item.getSysEmployeeByEmployeeSn().getName() + "请假" + 
						 	new DecimalFormat("#0.0").format(item.getLeaveday()) +"天</td>");
			buffer.append("<td>"+ new SimpleDateFormat("yyyy-MM-dd HH:mm").format(item.getCreatetime()) +"</td>");
			if (item.getModifytime() == null) {
				buffer.append("<td style='width:151px;' ></td>");
			} else {
				buffer.append("<td>"+ new SimpleDateFormat("yyyy-MM-dd HH:mm").format(item.getModifytime()) +"</td>");
			}
			if (item.getApproveOpinion() == null) {
				buffer.append("<td></td>");
			} else {
				buffer.append("<td>"+ item.getApproveOpinion() +"</td>");
			}
			buffer.append("<td>"+ item.getStatus() +"</td>");
			buffer.append("<td><img class='search' src='images/search.gif' width='16' height='16' />");
			if (!this.emp.getSysPosition().getNameCn().equals("员工") && 
					!this.emp.getSysPosition().getNameCn().equals("财务")) {	// 判断是否是管理人员登陆审批
				if (item.getStatus().equals("待审批")) {
					buffer.append("<img class='sub' src='images/sub.gif' width='16' height='16' /> ");
				} else {
					buffer.append("<img class='sub' src='images/empty.png' width='16' height='16' /> ");
				}
			}
			buffer.append("</td></tr>");
		}
		buffer.append("<tr id='page' >");
		buffer.append("<td colspan='7' align='center'>");
		if (this.pageNo == 1 && this.pageCount == 1) {
			buffer.append("<span>&nbsp;&nbsp;</span>");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span>");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span>");
			buffer.append("<span>&nbsp;&nbsp;</span>");
		} else if (this.pageNo == 1 && this.pageCount > 1) {
			buffer.append("<span>&nbsp;&nbsp;</span>");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span>");
			buffer.append("<a id='next' href='javascript:;'>&nbsp;下一页&nbsp;</a>");
			buffer.append("<a id='last' href='javascript:;'>&nbsp;末 页&nbsp;</a>");
		} else if (this.pageNo > 1 && this.pageNo < this.pageCount) {
			buffer.append("<a id='home' href='javascript:;'>&nbsp;首 页&nbsp;</a>");
			buffer.append("<a id='prev' href='javascript:;'>&nbsp;上一页&nbsp;</a>");
			buffer.append("<a id='next' href='javascript:;'>&nbsp;下一页&nbsp;</a>");
			buffer.append("<a id='last' href='javascript:;'>&nbsp;末 页&nbsp;</a>");
		} else if (this.pageNo == this.pageCount) {
			buffer.append("<a id='home' href='javascript:;'>&nbsp;首 页&nbsp;</a>");
			buffer.append("<a id='prev' href='javascript:;'>&nbsp;上一页&nbsp;</a>");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span>");
			buffer.append("<span>&nbsp;&nbsp;</span>");
		}
		buffer.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		buffer.append("<span id='pageInfo' >");
		buffer.append("第 <span id='curPage' >"+ this.pageNo +"</span> 页 / "
                    		+ "共 <span id='totalPage' >"+ this.pageCount +"</span> 页 ");
		buffer.append("</span>");
		buffer.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		buffer.append("共 <span id='total' >"+ this.total +"</span> 条记录 ");
		this.inputStream = new ByteArrayInputStream(buffer.toString().getBytes("utf-8"));

		return SUCCESS;
	}
	
	// 查看相关信息
	public String leaveView() {
		// 获得相关数据
		this.getLeaveInfo(this.id);
		if (this.opr.equals("view")) {		// 查看
			return "success";
		} else if(this.opr.equals("approval")) {		// 审批
			// 将 id 保存到会话
			ActionContext.getContext().getSession().put("id", id);
			return "approval";
		}
		return "view";
	}
	
	// 审批请假
	public String approval() {
		this.id = (Long) ActionContext.getContext().getSession().get("id");
		this.getLeaveInfo(this.id);
		this.leave.setApproveOpinion(this.approveOpinion);
		this.leave.setModifytime(new Date());
		// 判断是通过还是拒绝
		if (button != null && button[0].equals("审批通过")) {
			this.leave.setStatus("已审批");
		} else if (button != null && button[0].equals("打回")) {
			this.leave.setStatus("已打回");
		}
		// 修改请假
		leaveService.updateLeave(this.leave);
		return SUCCESS;
	}
	
	// 获得请假的相关信息
	public void getLeaveInfo(Long id) {
		// 获得请假对象
		this.leave = leaveService.getLeave(id);
	}

	public BizLeaveService getLeaveService() {
		return leaveService;
	}

	public void setLeaveService(BizLeaveService leaveService) {
		this.leaveService = leaveService;
	}

	public SysEmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(SysEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public BizLeave getLeave() {
		return leave;
	}

	public void setLeave(BizLeave leave) {
		this.leave = leave;
	}

	public SysEmployee getEmp() {
		return emp;
	}

	public void setEmp(SysEmployee emp) {
		this.emp = emp;
	}

	public SysDepartment getDept() {
		return dept;
	}

	public void setDept(SysDepartment dept) {
		this.dept = dept;
	}

	public SysPositionService getPositionService() {
		return positionService;
	}

	public void setPositionService(SysPositionService positionService) {
		this.positionService = positionService;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public List<BizLeave> getLeaveList() {
		return leaveList;
	}

	public void setLeaveList(List<BizLeave> leaveList) {
		this.leaveList = leaveList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpr() {
		return opr;
	}

	public void setOpr(String opr) {
		this.opr = opr;
	}

	public String getApproveOpinion() {
		return approveOpinion;
	}

	public void setApproveOpinion(String approveOpinion) {
		this.approveOpinion = approveOpinion;
	}

	public String[] getButton() {
		return button;
	}

	public void setButton(String[] button) {
		this.button = button;
	}

}
