package com.tr.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.tr.entity.BizCheckResult;
import com.tr.entity.BizClaimVoucher;
import com.tr.entity.BizClaimVoucherDetail;
import com.tr.entity.SysDepartment;
import com.tr.entity.SysEmployee;
import com.tr.service.BizCheckResultService;
import com.tr.service.BizClaimVoucherDetailService;
import com.tr.service.BizClaimVoucherService;
import com.tr.service.SysEmployeeService;

/**
 * 报销单控制器
 * 
 */
public class BizClaimVoucherAction extends ActionSupport {

	// 报销单业务接口
	private BizClaimVoucherService claimVocherService;
	// 报销单明细业务接口
	private BizClaimVoucherDetailService claimVoucherDetailService;
	// 员工业务接口
	private SysEmployeeService employeeService;
	// 处理结果业务接口
	private BizCheckResultService resultService;

	// 填写人
	private SysEmployee emp;
	// 报销事由
	private String textarea;
	// 总金额
	private Double totalPrice;
	// 状态
	private String status;
	// 开始时间
	private Date startDate;
	// 结束时间
	private Date endDate;
	// 明细集合
	private List<BizClaimVoucherDetail> detailList;
	// 明细 set 集合
	private Set<BizClaimVoucherDetail> details;
	// 报销单集合
	private List<BizClaimVoucher> claimVoucherList;
	// 报销单对象
	private BizClaimVoucher voucher;
	// 报销单审批集合
	private List<BizCheckResult> resultList;
	// 报销项目数组
	private String[] items;
	// 报销金额数组
	private Double[] accounts;
	// 费用说明信息数组
	private String[] dess;
	// 报销单编号
	private Long id;
	// 页码
	private Integer pageNo;
	// 总页数
	private Integer totalPage;
	// 每页大小
	private Integer pageSize = 5;

	// 提交按钮
	private String[] button;
	// 总记录数
	private Long total;
	// 数据流对象
	private InputStream inputStream;

	// 添加明细
	public String add() {
		// 报销单对象
		BizClaimVoucher claimVoucher = null;
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		this.emp = employeeService.getEmployeeBySn(emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		if (button == null) { // 修改
			// 获得会话
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			this.items = (String[]) session.get("items");
			this.accounts = (Double[]) session.get("accounts");
			this.dess = (String[]) session.get("dess");
			this.voucher = (BizClaimVoucher) session.get("voucher");
		} else { // 添加
			claimVoucher = new BizClaimVoucher();
			// 判断保存还是提交
			if (button[0].equals("保 存")) {
				this.status = "新创建";
				claimVoucher.setSysEmployeeByNextDealSn(emp);
			} else if (button[0].equals("保存并提交")) {
				this.status = "已提交";
				// 得到员工部门对象和职务对象
				SysDepartment department = emp.getSysDepartment();
				// 获得部门下所有员工
				Set<SysEmployee> employees = department.getSysEmployees();
				for (SysEmployee item : employees) {
					if (item.getSysPosition().getNameCn().equals("部门经理")) {
						claimVoucher.setSysEmployeeByNextDealSn(item);
					}
				}
			}
			// 添加报销单
			claimVoucher.setSysEmployeeByCreateSn(emp);
			claimVoucher.setCreateTime(new Date());
			claimVoucher.setModifyTime(null);
			claimVoucher.setEvent(textarea);
			claimVoucher.setTotalAccount(this.totalPrice);
			claimVoucher.setStatus(status);
			// 保存到数据库,并获取报销单编号
			Long id = claimVocherService.addNewClaimVoucher(claimVoucher);
			// 判断是否添加成功
			if (id == null || id <= 0) {
				throw new RuntimeException("添加报销单失败!!");
			}
		}
		// 添加报销单明细
		detailList = new ArrayList<BizClaimVoucherDetail>();
		for (int i = 0; i < items.length; i++) {
			BizClaimVoucherDetail detail = new BizClaimVoucherDetail();
			detail.setItem(items[i]);
			detail.setAccount(accounts[i]);
			detail.setDes(dess[i]);
			detailList.add(detail);

			if (claimVoucher == null) // 修改
				detail.setBizClaimVoucher(this.voucher);
			else
				// 新增
				detail.setBizClaimVoucher(claimVoucher);

			detail.setItem(items[i]);
			detail.setAccount(accounts[i]);
			detail.setDes(dess[i]);
			detailList.add(detail);
		}
		// 保存到数据库
		claimVoucherDetailService.addNewClaimVoucherDetail(detailList);

		return SUCCESS;
	}

	// 获得报销单明细
	public String detail() {
		Map<String, Object> map = ActionContext.getContext().getParameters();
		String[] ids = (String[]) map.get("id");
		String id = ids[0];
		// 获得操作类型
		String[] oprs = (String[]) map.get("opr");
		String opr = oprs[0];
		getVoucherDetails(new Long(id));
		// 获得审批集合
		this.resultList = resultService.getCheckResult(this.voucher);

		if (opr.equals("search")) { // 查看明细
			return "view";
		} else if (opr.equals("edit")) { // 修改报销单
			return "edit";	
		} else if (opr.equals("sub")) {	// 审批报销单
			// 保存报销单对象(因 getVoucherDetails() 方法保存了员工对象到会话,所以此处不保存)
			ActionContext.getContext().getSession().put("voucher", voucher);
			return "sub";
		}
		return "success";
	}

	// 修改报销单明细
	public String edit() {
		// 调用查看报销单明细的方法,将需要的值赋值
		this.getVoucherDetails(this.id);
		this.voucher.setModifyTime(new Date());
		// 重新计算总金额
		this.totalPrice = 0.0;
		for (int i = 0; i < this.accounts.length; i++) {
			this.totalPrice += accounts[i];
		}
		this.voucher.setTotalAccount(totalPrice);
		this.voucher.setEvent(textarea);
		if (button[0].equals("保 存")) {
			this.voucher.setStatus("新创建");
			this.voucher.setSysEmployeeByNextDealSn(this.emp);
		} else if (button[0].equals("保存并提交")) {
			this.voucher.setStatus("已提交");
			// 得到员工部门对象和职务对象
			SysDepartment department = emp.getSysDepartment();
			// 获得部门下所有员工
			Set<SysEmployee> employees = department.getSysEmployees();
			for (SysEmployee item : employees) {
				if (item.getSysPosition().getNameCn().equals("部门经理")) {
					this.voucher.setSysEmployeeByNextDealSn(item);
				}
			}
		}
		// 修改报销单
		claimVocherService.editBizClaimVoucher(voucher);
		if (claimVoucherDetailService.deleteDetail(id)) {		// 删除该报销单下所有明细后重新添加
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			session.put("accounts", this.accounts);
			session.put("voucher", this.voucher);
			session.put("items", items);
			session.put("dess", dess);
			return "success";
		} else {
			return "input";
		}
	}

	// 查看报销单明细
	private void getVoucherDetails(Long id) {
		// 获得报销单对象
		this.voucher = claimVocherService.getBizClaimVoucher(id);
		// 获得明细集合
		this.details = voucher.getBizClaimVoucherDetails();
		// 获得员工对象
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		this.emp = employeeService.getEmployeeBySn(emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		// 保存部门到会话
		ActionContext.getContext().getSession()
				.put("dept", emp.getSysDepartment().getName());
		// 保存职位到会话
		ActionContext.getContext().getSession()
				.put("positionName", emp.getSysPosition().getNameCn());
//		// 获得待处理人姓名保存到会话
//		ActionContext.getContext().getSession().put("dealName", voucher.getSysEmployeeByNextDealSn().getName());
	}

	// 查询报销单列表
	public String find() {
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		this.emp = employeeService.getEmployeeBySn(emp.getSn());
		// 保存当前员工到会话
		ActionContext.getContext().getSession().put("emp", emp);
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("position", this.emp.getSysPosition().getNameCn());
		conditions.put("empNo", this.emp.getSn());
		conditions.put("deptNo", this.emp.getSysDepartment().getId());
		// 将条件保存到会话
		ActionContext.getContext().getSession().put("conditions", conditions);
		// 总记录数
		this.total = claimVocherService.getTotal(conditions);
		// 如果页码为空,就设为第一页
		if (this.pageNo == null) {
			pageNo = 1;
		}
		// 计算总页数
		this.totalPage = getTotal() % pageSize == 0 ? getTotal().intValue()
				/ pageSize : getTotal().intValue() / pageSize + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		// 避免懒加载
		// employeeService.getEmployeeBySn(conditions,pageNo,pageSize);
		// 分页查询
		this.claimVoucherList = claimVocherService.getClaimVoucherByConditions(
				conditions, this.pageNo, pageSize);

		return SUCCESS;
	}

	// 分页筛选查询
	public String findJson() throws IOException {
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		// 分页查询条件
		Map<String, Object> conditions = new HashMap<String, Object>();
		if (this.pageNo == null) {	//当前执行查询而不是翻页
			// 状态
			conditions.put("status", status);
			// 开始时间
			conditions.put("startDate", startDate);
			// 结束时间
			conditions.put("endDate", endDate);
			// 职务
			conditions.put("position", this.emp.getSysPosition().getNameCn());
			// 员工编号
			conditions.put("empNo", this.emp.getSn());
			// 部门编号
			conditions.put("deptNo", emp.getSysDepartment().getId());
			// 将条件保存到会话
			ActionContext.getContext().getSession().put("conditions", conditions);
			// 将当前页设为第一页
			this.pageNo = 1;
		} else {		// 翻页
			// 从上次请求的结果中获得条件筛选
			conditions = (Map<String, Object>) ActionContext.getContext().getSession().get("conditions");
		}
		// 总记录数
		this.setTotal(claimVocherService.getTotal(conditions));
		// 计算总页数
		this.totalPage = getTotal() % pageSize == 0 ? getTotal().intValue()
				/ pageSize : getTotal().intValue() / pageSize + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		// 分页查询
		this.claimVoucherList = claimVocherService.getClaimVoucherByConditions(
				conditions, this.pageNo, pageSize);
		
		// 发送数据
		StringBuffer buffer = new StringBuffer("");
		// 日期格式
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (BizClaimVoucher item : claimVoucherList) {
			// 拼接网页
			buffer.append("<tr class='voucher'>");
			buffer.append("<td>" + item.getId() + "</td>");
			buffer.append("<td> " + smf.format(item.getCreateTime()) + " </td>");
			// buffer.append("<td>"+ item.getCreateTime() +"</td>");
			buffer.append("<td> " + item.getSysEmployeeByCreateSn().getName()
					+ " </td>");
			// 保留两位小数
			buffer.append("<td>￥ "
					+ new DecimalFormat("#.00").format(item.getTotalAccount())
					+ "</td>");
			buffer.append("<td>" + item.getStatus() + "</td>");
			if (item.getSysEmployeeByNextDealSn() != null) {
				buffer.append("<td>" + item.getSysEmployeeByNextDealSn().getName()
						+ " </td>");
			} else {
				buffer.append("<td>&nbsp;&nbsp;&nbsp;</td>");
			}
			buffer.append("<td>");
			if (this.emp.getSysPosition().getNameCn().equals("员工")) {
				if (item.getStatus().equals("新创建")
						|| item.getStatus().equals("已打回"))
					buffer.append("<img class='edit' src='images/edit.gif' width='16' height='16' /> ");
				else
					buffer.append("<img src='images/empty.png' width='16' height='16' /> ");
				buffer.append("<img class='search' src='images/search.gif' width='16' height='16' /> ");
			} else {
				buffer.append("<img class='search' src='images/search.gif' width='16' height='16' /> ");
				if (this.emp.getSysPosition().getNameCn().equals("部门经理")) {
					if (item.getStatus().equals("已提交")
							&& item.getSysEmployeeByNextDealSn().getSn()
									.equals(emp.getSn()))
						buffer.append("<img class='sub' src='images/sub.gif' width='16' height='16' /> ");
					else
						buffer.append("<img src='images/empty.png' width='16' height='16' /> ");
				} else if (this.emp.getSysPosition().getNameCn().equals("总经理")) {
					if (item.getStatus().equals("待审批")
							&& item.getSysEmployeeByNextDealSn().getSn()
							.equals(emp.getSn()))
						buffer.append("<img class='sub' src='images/sub.gif' width='16' height='16' /> ");
					else
						buffer.append("<img src='images/empty.png' width='16' height='16' /> ");
				} else if (this.emp.getSysPosition().getNameCn().equals("财务")) {
					if (item.getStatus().equals("已审批")
							&& item.getSysEmployeeByNextDealSn().getSn()
							.equals(emp.getSn()))
						buffer.append("<img class='sub' src='images/sub.gif' width='16' height='16' /> ");
					else
						buffer.append("<img src='images/empty.png' width='16' height='16' /> ");
				}
			}
		}
		buffer.append("</td></tr>");

		buffer.append("<tr id='page' >");
		buffer.append("<td colspan='7' align='center'>");
		buffer.append("<input type='hidden' id='pageNo' value='" + pageNo
				+ "' />");
		if (pageNo == 1 && totalPage == 1) {
			buffer.append("<span>&nbsp;&nbsp;</span> ");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span> ");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span> ");
			buffer.append("<span>&nbsp;&nbsp;</span> ");
		} else if (pageNo == 1) {
			buffer.append("<span>&nbsp;&nbsp;</span> ");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span> ");
			buffer.append("<a id='next' href='javascript:;'>&nbsp;下一页&nbsp;</a> ");
			buffer.append("<a id='last' href='javascript:;'>&nbsp;末 页&nbsp;</a> ");
		} else if (pageNo > 1 && pageNo < totalPage) {
			buffer.append("<a id='home' href='javascript:;'>&nbsp;首 页&nbsp;</a> ");
			buffer.append("<a id='prev' href='javascript:;'>&nbsp;上一页&nbsp;</a> ");
			buffer.append("<a id='next' href='javascript:;'>&nbsp;下一页&nbsp;</a> ");
			buffer.append("<a id='last' href='javascript:;'>&nbsp;末 页&nbsp;</a> ");
		} else if (pageNo == totalPage) {
			buffer.append("<a id='home' href='javascript:;'>&nbsp;首 页&nbsp;</a> ");
			buffer.append("<a id='prev' href='javascript:;'>&nbsp;上一页&nbsp;</a> ");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span> ");
			buffer.append("<span>&nbsp;&nbsp;</span> ");
		}
		buffer.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;</span> ");
		buffer.append("<span id='pageInfo' >第 <span id='curPage' > " + pageNo
				+ "</span> 页 / 共 <span id='totalPage' >" + totalPage
				+ "</span> 页 </span> ");
		buffer.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> ");
		buffer.append("共 <span id='total' >" + total + "</span> 条记录");
		buffer.append("</td></tr>");
		inputStream = new ByteArrayInputStream(buffer.toString().getBytes(
				"utf-8"));

		return SUCCESS;
	}

	public BizClaimVoucherService getClaimVocherService() {
		return claimVocherService;
	}

	public void setClaimVocherService(BizClaimVoucherService claimVocherService) {
		this.claimVocherService = claimVocherService;
	}

	public BizClaimVoucherDetailService getClaimVoucherDetailService() {
		return claimVoucherDetailService;
	}

	public void setClaimVoucherDetailService(
			BizClaimVoucherDetailService claimVoucherDetailService) {
		this.claimVoucherDetailService = claimVoucherDetailService;
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

	public String getTextarea() {
		return textarea;
	}

	public void setTextarea(String textarea) {
		this.textarea = textarea;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<BizClaimVoucherDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<BizClaimVoucherDetail> detailList) {
		this.detailList = detailList;
	}

	public Set<BizClaimVoucherDetail> getDetails() {
		return details;
	}

	public void setDetails(Set<BizClaimVoucherDetail> details) {
		this.details = details;
	}

	public List<BizClaimVoucher> getClaimVoucherList() {
		return claimVoucherList;
	}

	public void setClaimVoucherList(List<BizClaimVoucher> claimVoucherList) {
		this.claimVoucherList = claimVoucherList;
	}

	public BizClaimVoucher getVoucher() {
		return voucher;
	}

	public void setVoucher(BizClaimVoucher voucher) {
		this.voucher = voucher;
	}

	public String[] getItems() {
		return items;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	public Double[] getAccounts() {
		return accounts;
	}

	public void setAccounts(Double[] accounts) {
		this.accounts = accounts;
	}

	public String[] getDess() {
		return dess;
	}

	public void setDess(String[] dess) {
		this.dess = dess;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String[] getButton() {
		return button;
	}

	public void setButton(String[] button) {
		this.button = button;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public BizCheckResultService getResultService() {
		return resultService;
	}

	public void setResultService(BizCheckResultService resultService) {
		this.resultService = resultService;
	}

	public List<BizCheckResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<BizCheckResult> resultList) {
		this.resultList = resultList;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
