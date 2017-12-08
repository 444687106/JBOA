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
 * ������������
 * 
 */
public class BizClaimVoucherAction extends ActionSupport {

	// ������ҵ��ӿ�
	private BizClaimVoucherService claimVocherService;
	// ��������ϸҵ��ӿ�
	private BizClaimVoucherDetailService claimVoucherDetailService;
	// Ա��ҵ��ӿ�
	private SysEmployeeService employeeService;
	// ������ҵ��ӿ�
	private BizCheckResultService resultService;

	// ��д��
	private SysEmployee emp;
	// ��������
	private String textarea;
	// �ܽ��
	private Double totalPrice;
	// ״̬
	private String status;
	// ��ʼʱ��
	private Date startDate;
	// ����ʱ��
	private Date endDate;
	// ��ϸ����
	private List<BizClaimVoucherDetail> detailList;
	// ��ϸ set ����
	private Set<BizClaimVoucherDetail> details;
	// ����������
	private List<BizClaimVoucher> claimVoucherList;
	// ����������
	private BizClaimVoucher voucher;
	// ��������������
	private List<BizCheckResult> resultList;
	// ������Ŀ����
	private String[] items;
	// �����������
	private Double[] accounts;
	// ����˵����Ϣ����
	private String[] dess;
	// ���������
	private Long id;
	// ҳ��
	private Integer pageNo;
	// ��ҳ��
	private Integer totalPage;
	// ÿҳ��С
	private Integer pageSize = 5;

	// �ύ��ť
	private String[] button;
	// �ܼ�¼��
	private Long total;
	// ����������
	private InputStream inputStream;

	// �����ϸ
	public String add() {
		// ����������
		BizClaimVoucher claimVoucher = null;
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		this.emp = employeeService.getEmployeeBySn(emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		if (button == null) { // �޸�
			// ��ûỰ
			Map<String, Object> session = ActionContext.getContext()
					.getSession();
			this.items = (String[]) session.get("items");
			this.accounts = (Double[]) session.get("accounts");
			this.dess = (String[]) session.get("dess");
			this.voucher = (BizClaimVoucher) session.get("voucher");
		} else { // ���
			claimVoucher = new BizClaimVoucher();
			// �жϱ��滹���ύ
			if (button[0].equals("�� ��")) {
				this.status = "�´���";
				claimVoucher.setSysEmployeeByNextDealSn(emp);
			} else if (button[0].equals("���沢�ύ")) {
				this.status = "���ύ";
				// �õ�Ա�����Ŷ����ְ�����
				SysDepartment department = emp.getSysDepartment();
				// ��ò���������Ա��
				Set<SysEmployee> employees = department.getSysEmployees();
				for (SysEmployee item : employees) {
					if (item.getSysPosition().getNameCn().equals("���ž���")) {
						claimVoucher.setSysEmployeeByNextDealSn(item);
					}
				}
			}
			// ��ӱ�����
			claimVoucher.setSysEmployeeByCreateSn(emp);
			claimVoucher.setCreateTime(new Date());
			claimVoucher.setModifyTime(null);
			claimVoucher.setEvent(textarea);
			claimVoucher.setTotalAccount(this.totalPrice);
			claimVoucher.setStatus(status);
			// ���浽���ݿ�,����ȡ���������
			Long id = claimVocherService.addNewClaimVoucher(claimVoucher);
			// �ж��Ƿ���ӳɹ�
			if (id == null || id <= 0) {
				throw new RuntimeException("��ӱ�����ʧ��!!");
			}
		}
		// ��ӱ�������ϸ
		detailList = new ArrayList<BizClaimVoucherDetail>();
		for (int i = 0; i < items.length; i++) {
			BizClaimVoucherDetail detail = new BizClaimVoucherDetail();
			detail.setItem(items[i]);
			detail.setAccount(accounts[i]);
			detail.setDes(dess[i]);
			detailList.add(detail);

			if (claimVoucher == null) // �޸�
				detail.setBizClaimVoucher(this.voucher);
			else
				// ����
				detail.setBizClaimVoucher(claimVoucher);

			detail.setItem(items[i]);
			detail.setAccount(accounts[i]);
			detail.setDes(dess[i]);
			detailList.add(detail);
		}
		// ���浽���ݿ�
		claimVoucherDetailService.addNewClaimVoucherDetail(detailList);

		return SUCCESS;
	}

	// ��ñ�������ϸ
	public String detail() {
		Map<String, Object> map = ActionContext.getContext().getParameters();
		String[] ids = (String[]) map.get("id");
		String id = ids[0];
		// ��ò�������
		String[] oprs = (String[]) map.get("opr");
		String opr = oprs[0];
		getVoucherDetails(new Long(id));
		// �����������
		this.resultList = resultService.getCheckResult(this.voucher);

		if (opr.equals("search")) { // �鿴��ϸ
			return "view";
		} else if (opr.equals("edit")) { // �޸ı�����
			return "edit";	
		} else if (opr.equals("sub")) {	// ����������
			// ���汨��������(�� getVoucherDetails() ����������Ա�����󵽻Ự,���Դ˴�������)
			ActionContext.getContext().getSession().put("voucher", voucher);
			return "sub";
		}
		return "success";
	}

	// �޸ı�������ϸ
	public String edit() {
		// ���ò鿴��������ϸ�ķ���,����Ҫ��ֵ��ֵ
		this.getVoucherDetails(this.id);
		this.voucher.setModifyTime(new Date());
		// ���¼����ܽ��
		this.totalPrice = 0.0;
		for (int i = 0; i < this.accounts.length; i++) {
			this.totalPrice += accounts[i];
		}
		this.voucher.setTotalAccount(totalPrice);
		this.voucher.setEvent(textarea);
		if (button[0].equals("�� ��")) {
			this.voucher.setStatus("�´���");
			this.voucher.setSysEmployeeByNextDealSn(this.emp);
		} else if (button[0].equals("���沢�ύ")) {
			this.voucher.setStatus("���ύ");
			// �õ�Ա�����Ŷ����ְ�����
			SysDepartment department = emp.getSysDepartment();
			// ��ò���������Ա��
			Set<SysEmployee> employees = department.getSysEmployees();
			for (SysEmployee item : employees) {
				if (item.getSysPosition().getNameCn().equals("���ž���")) {
					this.voucher.setSysEmployeeByNextDealSn(item);
				}
			}
		}
		// �޸ı�����
		claimVocherService.editBizClaimVoucher(voucher);
		if (claimVoucherDetailService.deleteDetail(id)) {		// ɾ���ñ�������������ϸ���������
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

	// �鿴��������ϸ
	private void getVoucherDetails(Long id) {
		// ��ñ���������
		this.voucher = claimVocherService.getBizClaimVoucher(id);
		// �����ϸ����
		this.details = voucher.getBizClaimVoucherDetails();
		// ���Ա������
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		this.emp = employeeService.getEmployeeBySn(emp.getSn());
		ActionContext.getContext().getSession().put("emp", emp);
		// ���沿�ŵ��Ự
		ActionContext.getContext().getSession()
				.put("dept", emp.getSysDepartment().getName());
		// ����ְλ���Ự
		ActionContext.getContext().getSession()
				.put("positionName", emp.getSysPosition().getNameCn());
//		// ��ô��������������浽�Ự
//		ActionContext.getContext().getSession().put("dealName", voucher.getSysEmployeeByNextDealSn().getName());
	}

	// ��ѯ�������б�
	public String find() {
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		this.emp = employeeService.getEmployeeBySn(emp.getSn());
		// ���浱ǰԱ�����Ự
		ActionContext.getContext().getSession().put("emp", emp);
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("position", this.emp.getSysPosition().getNameCn());
		conditions.put("empNo", this.emp.getSn());
		conditions.put("deptNo", this.emp.getSysDepartment().getId());
		// ���������浽�Ự
		ActionContext.getContext().getSession().put("conditions", conditions);
		// �ܼ�¼��
		this.total = claimVocherService.getTotal(conditions);
		// ���ҳ��Ϊ��,����Ϊ��һҳ
		if (this.pageNo == null) {
			pageNo = 1;
		}
		// ������ҳ��
		this.totalPage = getTotal() % pageSize == 0 ? getTotal().intValue()
				/ pageSize : getTotal().intValue() / pageSize + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		// ����������
		// employeeService.getEmployeeBySn(conditions,pageNo,pageSize);
		// ��ҳ��ѯ
		this.claimVoucherList = claimVocherService.getClaimVoucherByConditions(
				conditions, this.pageNo, pageSize);

		return SUCCESS;
	}

	// ��ҳɸѡ��ѯ
	public String findJson() throws IOException {
		this.emp = (SysEmployee) ActionContext.getContext().getSession()
				.get("emp");
		// ��ҳ��ѯ����
		Map<String, Object> conditions = new HashMap<String, Object>();
		if (this.pageNo == null) {	//��ǰִ�в�ѯ�����Ƿ�ҳ
			// ״̬
			conditions.put("status", status);
			// ��ʼʱ��
			conditions.put("startDate", startDate);
			// ����ʱ��
			conditions.put("endDate", endDate);
			// ְ��
			conditions.put("position", this.emp.getSysPosition().getNameCn());
			// Ա�����
			conditions.put("empNo", this.emp.getSn());
			// ���ű��
			conditions.put("deptNo", emp.getSysDepartment().getId());
			// ���������浽�Ự
			ActionContext.getContext().getSession().put("conditions", conditions);
			// ����ǰҳ��Ϊ��һҳ
			this.pageNo = 1;
		} else {		// ��ҳ
			// ���ϴ�����Ľ���л������ɸѡ
			conditions = (Map<String, Object>) ActionContext.getContext().getSession().get("conditions");
		}
		// �ܼ�¼��
		this.setTotal(claimVocherService.getTotal(conditions));
		// ������ҳ��
		this.totalPage = getTotal() % pageSize == 0 ? getTotal().intValue()
				/ pageSize : getTotal().intValue() / pageSize + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		// ��ҳ��ѯ
		this.claimVoucherList = claimVocherService.getClaimVoucherByConditions(
				conditions, this.pageNo, pageSize);
		
		// ��������
		StringBuffer buffer = new StringBuffer("");
		// ���ڸ�ʽ
		SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (BizClaimVoucher item : claimVoucherList) {
			// ƴ����ҳ
			buffer.append("<tr class='voucher'>");
			buffer.append("<td>" + item.getId() + "</td>");
			buffer.append("<td> " + smf.format(item.getCreateTime()) + " </td>");
			// buffer.append("<td>"+ item.getCreateTime() +"</td>");
			buffer.append("<td> " + item.getSysEmployeeByCreateSn().getName()
					+ " </td>");
			// ������λС��
			buffer.append("<td>�� "
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
			if (this.emp.getSysPosition().getNameCn().equals("Ա��")) {
				if (item.getStatus().equals("�´���")
						|| item.getStatus().equals("�Ѵ��"))
					buffer.append("<img class='edit' src='images/edit.gif' width='16' height='16' /> ");
				else
					buffer.append("<img src='images/empty.png' width='16' height='16' /> ");
				buffer.append("<img class='search' src='images/search.gif' width='16' height='16' /> ");
			} else {
				buffer.append("<img class='search' src='images/search.gif' width='16' height='16' /> ");
				if (this.emp.getSysPosition().getNameCn().equals("���ž���")) {
					if (item.getStatus().equals("���ύ")
							&& item.getSysEmployeeByNextDealSn().getSn()
									.equals(emp.getSn()))
						buffer.append("<img class='sub' src='images/sub.gif' width='16' height='16' /> ");
					else
						buffer.append("<img src='images/empty.png' width='16' height='16' /> ");
				} else if (this.emp.getSysPosition().getNameCn().equals("�ܾ���")) {
					if (item.getStatus().equals("������")
							&& item.getSysEmployeeByNextDealSn().getSn()
							.equals(emp.getSn()))
						buffer.append("<img class='sub' src='images/sub.gif' width='16' height='16' /> ");
					else
						buffer.append("<img src='images/empty.png' width='16' height='16' /> ");
				} else if (this.emp.getSysPosition().getNameCn().equals("����")) {
					if (item.getStatus().equals("������")
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
			buffer.append("<a id='next' href='javascript:;'>&nbsp;��һҳ&nbsp;</a> ");
			buffer.append("<a id='last' href='javascript:;'>&nbsp;ĩ ҳ&nbsp;</a> ");
		} else if (pageNo > 1 && pageNo < totalPage) {
			buffer.append("<a id='home' href='javascript:;'>&nbsp;�� ҳ&nbsp;</a> ");
			buffer.append("<a id='prev' href='javascript:;'>&nbsp;��һҳ&nbsp;</a> ");
			buffer.append("<a id='next' href='javascript:;'>&nbsp;��һҳ&nbsp;</a> ");
			buffer.append("<a id='last' href='javascript:;'>&nbsp;ĩ ҳ&nbsp;</a> ");
		} else if (pageNo == totalPage) {
			buffer.append("<a id='home' href='javascript:;'>&nbsp;�� ҳ&nbsp;</a> ");
			buffer.append("<a id='prev' href='javascript:;'>&nbsp;��һҳ&nbsp;</a> ");
			buffer.append("<span>&nbsp;&nbsp;&nbsp;</span> ");
			buffer.append("<span>&nbsp;&nbsp;</span> ");
		}
		buffer.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;</span> ");
		buffer.append("<span id='pageInfo' >�� <span id='curPage' > " + pageNo
				+ "</span> ҳ / �� <span id='totalPage' >" + totalPage
				+ "</span> ҳ </span> ");
		buffer.append("<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> ");
		buffer.append("�� <span id='total' >" + total + "</span> ����¼");
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
