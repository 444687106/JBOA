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
 * ��ٿ�����
 * 
 */
public class BizLeaveAction extends ActionSupport {
	// ���ҵ��ӿ�
	private BizLeaveService leaveService;
	// Ա��ҵ��ӿ�
	private SysEmployeeService employeeService;
	// ְ��ҵ��ӿ�
	private SysPositionService positionService;

	// ��ٶ���
	private BizLeave leave;
	// Ա������
	private SysEmployee emp;
	// ���Ŷ���
	private SysDepartment dept;
	// ��ټ���
	private List<BizLeave> leaveList;
	// �������
	private Long id;
	// ��������
	private String opr;
	// �������
	private String approveOpinion;
	// �������
	private String[] button;
	// �������
	private String type;
	// ��ʼʱ��
	private Date start;
	// ����ʱ��
	private Date end;
	// �����
	private InputStream inputStream;
	// �ܼ�¼��
	private Long total;
	// ��ǰҳ
	private Integer pageNo;
	// ÿҳ��С
	private Integer pageSize = 5;
	// ��ҳ��
	private Integer pageCount;

	// ���Ա���������Ϣ
	public String rest() {
		// �ӻỰ�л��Ա��
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		// ���»��Ա�����浽�Ự
		this.emp = employeeService.getEmployeeBySn(this.emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		return SUCCESS;
	}

	// ����
	public String restAdd() {
		// �ӻỰ�л�ȡԱ��
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		// ���»��Ա�����浽�Ự
		this.emp = employeeService.getEmployeeBySn(this.emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		this.leave.setSysEmployeeByEmployeeSn(this.emp);
		this.leave.setCreatetime(new Date());
		this.leave.setStatus("������");
		// ��һ������
		SysEmployee employee = null;
		// ��ø�Ա�����ڲ��ŵ�����Ա�������������ž���
		for (SysEmployee item : (Set<SysEmployee>) this.emp.getSysDepartment()
				.getSysEmployees()) {
			if (item.getSysPosition().getNameCn().equals("���ž���")) {
				employee = item;
				break;
			}
		}
		if (employee == null) {
			// ���������ò���û�в��ž��� ,�����ܾ�������
			for (SysPosition item : positionService.getAll()) {
				if (item.getNameCn().equals("�ܾ���")) {
					List<SysEmployee> empList = new ArrayList<SysEmployee>(
							item.getSysEmployees());
					employee = empList.get(0);
				}
			}
		}
		this.leave.setSysEmployeeByNextDealSn(employee);
		// ��ӵ����ݿ�
		leaveService.addNewLeave(this.leave);

		return SUCCESS;
	}

	// �鿴����б�
	public String restList() {
		// ��ѯ����
		Map<String, Object> conditions = new HashMap<String, Object>();
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		this.emp = employeeService.getEmployeeBySn(this.emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		// ����������浽 map ��
		conditions.put("position",
				ActionContext.getContext().getSession().get("loginPosition"));
		conditions.put("empNo", emp.getSn());
		conditions.put("deptNo", this.emp.getSysDepartment().getId());
		// ���������浽�Ự
		ActionContext.getContext().getSession().put("conditions", conditions);
		// �����ǰҳΪ��,��Ϊ��һҳ
		if (pageNo == null) {
			pageNo = 1;
		}
		// ����ܼ�¼��
		this.total = leaveService.getTotal(conditions);
		// ������ҳ��
		this.pageCount = this.total.intValue() % this.pageSize == 0 ? this.total
				.intValue() / this.pageSize
				: this.total.intValue() / this.pageSize + 1;
		// ���û�м�¼, �ͽ���ҳ����Ϊ 1
		if (this.pageCount == 0) {
			this.pageCount = 1;
		}
		// ��ѯ���ݲ�����
		this.leaveList = leaveService
				.getLeaveList(conditions, pageNo, pageSize);
		return SUCCESS;
	}

	// �첽����
	public String restJson() throws IOException {
		// ɸѡ����
		Map<String, Object> conditions = new HashMap<String, Object>();
		// ���Ա��
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		// ����������
		this.emp = employeeService.getEmployeeBySn(this.emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		// �ж��Ƿ��ǲ�ѯ
		if (this.pageNo == null) { // ��ѯ
			// ����ǰҳ��Ϊ��һҳ
			this.pageNo = 1;
			// ��������
			conditions.put("type", type);
			conditions.put("startDate", start);
			conditions.put("endDate", end);
			conditions.put("position", this.emp.getSysPosition().getNameCn());
			conditions.put("deptNo", this.emp.getSysDepartment().getId());
			conditions.put("empNo", this.emp.getSn());
		} else { // ��ҳ
			// ���ϴβ�ѯ�����л������ɸѡ
			conditions = (Map<String, Object>) ActionContext.getContext().getSession().get("conditions");
		}
		// ����ǰɸѡ�������浽�Ự
		ActionContext.getContext().getSession().put("conditions", conditions);
		// ����ܼ�¼��
		this.total = leaveService.getTotal(conditions);
		// ������ҳ��
		this.pageCount = this.total.intValue() % this.pageSize == 0 ? this.total.intValue() / this.pageSize :
						this.total.intValue() / this.pageSize + 1;
		// ���û�м�¼, �ͽ���ҳ����Ϊ 1
		if (this.pageCount == 0) {
			this.pageCount = 1;
		}
		// �������
		this.leaveList = leaveService.getLeaveList(conditions, pageNo, pageSize);
		
		// ƴ����ҳ
		StringBuffer buffer = new StringBuffer("");
		for (BizLeave item : this.leaveList) {
			buffer.append("<tr class='leave'>");
			buffer.append("<td>"+ item.getId() +"</td>");
			buffer.append("<td>"+ item.getSysEmployeeByEmployeeSn().getName() + "���" + 
						 	new DecimalFormat("#0.0").format(item.getLeaveday()) +"��</td>");
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
			if (!this.emp.getSysPosition().getNameCn().equals("Ա��") && 
					!this.emp.getSysPosition().getNameCn().equals("����")) {	// �ж��Ƿ��ǹ�����Ա��½����
				if (item.getStatus().equals("������")) {
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
			buffer.append("<a id='next' href='javascript:;'>&nbsp;��һҳ&nbsp;</a>");
			buffer.append("<a id='last' href='javascript:;'>&nbsp;ĩ ҳ&nbsp;</a>");
		} else if (this.pageNo > 1 && this.pageNo < this.pageCount) {
			buffer.append("<a id='home' href='javascript:;'>&nbsp;�� ҳ&nbsp;</a>");
			buffer.append("<a id='prev' href='javascript:;'>&nbsp;��һҳ&nbsp;</a>");
			buffer.append("<a id='next' href='javascript:;'>&nbsp;��һҳ&nbsp;</a>");
			buffer.append("<a id='last' href='javascript:;'>&nbsp;ĩ ҳ&nbsp;</a>");
		} else if (this.pageNo == this.pageCount) {
			buffer.append("<a id='home' href='javascript:;'>&nbsp;�� ҳ&nbsp;</a>");
			buffer.append("<a id='prev' href='javascript:;'>&nbsp;��һҳ&nbsp;</a>");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span>");
			buffer.append("<span>&nbsp;&nbsp;</span>");
		}
		buffer.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		buffer.append("<span id='pageInfo' >");
		buffer.append("�� <span id='curPage' >"+ this.pageNo +"</span> ҳ / "
                    		+ "�� <span id='totalPage' >"+ this.pageCount +"</span> ҳ ");
		buffer.append("</span>");
		buffer.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		buffer.append("�� <span id='total' >"+ this.total +"</span> ����¼ ");
		this.inputStream = new ByteArrayInputStream(buffer.toString().getBytes("utf-8"));

		return SUCCESS;
	}
	
	// �鿴�����Ϣ
	public String leaveView() {
		// ����������
		this.getLeaveInfo(this.id);
		if (this.opr.equals("view")) {		// �鿴
			return "success";
		} else if(this.opr.equals("approval")) {		// ����
			// �� id ���浽�Ự
			ActionContext.getContext().getSession().put("id", id);
			return "approval";
		}
		return "view";
	}
	
	// �������
	public String approval() {
		this.id = (Long) ActionContext.getContext().getSession().get("id");
		this.getLeaveInfo(this.id);
		this.leave.setApproveOpinion(this.approveOpinion);
		this.leave.setModifytime(new Date());
		// �ж���ͨ�����Ǿܾ�
		if (button != null && button[0].equals("����ͨ��")) {
			this.leave.setStatus("������");
		} else if (button != null && button[0].equals("���")) {
			this.leave.setStatus("�Ѵ��");
		}
		// �޸����
		leaveService.updateLeave(this.leave);
		return SUCCESS;
	}
	
	// �����ٵ������Ϣ
	public void getLeaveInfo(Long id) {
		// �����ٶ���
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
