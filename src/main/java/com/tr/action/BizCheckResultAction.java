package com.tr.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.tr.entity.BizCheckResult;
import com.tr.entity.BizClaimVoucher;
import com.tr.entity.SysEmployee;
import com.tr.entity.SysPosition;
import com.tr.service.BizCheckResultService;
import com.tr.service.BizClaimVoucherService;
import com.tr.service.SysEmployeeService;
import com.tr.service.SysPositionService;

/**
 * ����������
 *
 */
public class BizCheckResultAction extends ActionSupport {
	
	//����ҵ��ӿ�
	private BizCheckResultService resultService;
	// ������ҵ��ӿ�
	private BizClaimVoucherService claimVocherService;
	// Ա��ҵ��ӿ�
	private SysEmployeeService employeeService;
	// ְ�񼯺Ͻӿ�
	private SysPositionService positionService;
	// ��ť����
	private String[] button;
	// �������
	private String textarea;
	
	// ����
	public String sub() {
		// �ӻỰ�л�õ�ǰԱ���͵�ǰ������
		BizClaimVoucher claimVoucher = (BizClaimVoucher) ActionContext.getContext().getSession().get("voucher");
		SysEmployee emp = (SysEmployee) ActionContext.getContext().getSession().get("emp");
		// ���ְ�񼯺�
		List<SysPosition> positionList = positionService.getAll();
		// ���ݱ�����»�õ�ǰ������,����������
		claimVoucher = claimVocherService.getBizClaimVoucher(claimVoucher.getId());
		// ������������
		BizCheckResult result = new BizCheckResult();
		result.setCheckTime(new Date());
		result.setComm(textarea);
		result.setBizClaimVoucher(claimVoucher);
		result.setSysEmployee(emp);
		if (button[0].equals("����ͨ��")) {
			result.setResult("ͨ��");
			if (emp.getSysPosition().getNameCn().equals("���ž���")) {		// ���ž�������
				if (claimVoucher.getTotalAccount() >= 5000) {
					claimVoucher.setStatus("������");
					// �ύ����һ��������
					SysEmployee empSub = null;
					for (SysPosition item : positionList) {
						if (item.getNameCn().equals("�ܾ���")) {
							List<SysEmployee> empList = new ArrayList<SysEmployee>(item.getSysEmployees());
							empSub = empList.get(0);
							break;
						}
					}
					if (empSub != null) {
						// ����������
						resultService.addNewResult(result);
						claimVoucher.setSysEmployeeByNextDealSn(empSub);
					} else {
						return "input";
					}
				} else {
					claimVoucher.setStatus("������");
					boolean flag = cashierSub(claimVoucher,positionList,result);
					if (!flag) {
						return "input";
					}
				}
			} else if(emp.getSysPosition().getNameCn().equals("�ܾ���")) {		// �ܾ�������
				claimVoucher.setStatus("������");
				cashierSub(claimVoucher,positionList,result);
				boolean flag = cashierSub(claimVoucher,positionList,result);
				if (!flag) {
					return "input";
				}
			} else if(emp.getSysPosition().getNameCn().equals("����")) {		// ���񲦿�
				claimVoucher.setStatus("�Ѹ���");
				claimVoucher.setSysEmployeeByNextDealSn(null);
				// ����������
				resultService.addNewResult(result);
			}
		} else if (button[0].equals("�����ܾ�")) {
			claimVoucher.setStatus("����ֹ");
			result.setResult("�ܾ�");
			claimVoucher.setSysEmployeeByNextDealSn(null);
			// ����������
			resultService.addNewResult(result);
		} else if (button[0].equals("���")) {
			claimVoucher.setStatus("�Ѵ��");
			claimVoucher.setSysEmployeeByNextDealSn(claimVoucher.getSysEmployeeByCreateSn());
		}
		// �޸ı�����
		claimVocherService.editBizClaimVoucher(claimVoucher);
		return SUCCESS;
	}
	
	// �ύ����������
	private boolean cashierSub(BizClaimVoucher claimVoucher,List<SysPosition> positionList,BizCheckResult result){
		// �ύ����һ��������
		SysEmployee empSub = null;
		for (SysPosition item : positionList) {
			if (item.getNameCn().equals("����")) {
				List<SysEmployee> empList = new ArrayList<SysEmployee>(item.getSysEmployees());
				empSub = empList.get(0);
				break;
			}
		}
		if (empSub != null) {
			// ����������
			Long flag = resultService.addNewResult(result);
			if (flag > 0) {
				claimVoucher.setSysEmployeeByNextDealSn(empSub);
				return true;
			}else{
				return false;
			}
		} else {
			return false;
		}
	}

	public String[] getButton() {
		return button;
	}

	public void setButton(String[] button) {
		this.button = button;
	}

	public BizCheckResultService getResultService() {
		return resultService;
	}

	public void setResultService(BizCheckResultService resultService) {
		this.resultService = resultService;
	}

	public String getTextarea() {
		return textarea;
	}

	public void setTextarea(String textarea) {
		this.textarea = textarea;
	}

	public BizClaimVoucherService getClaimVocherService() {
		return claimVocherService;
	}

	public void setClaimVocherService(BizClaimVoucherService claimVocherService) {
		this.claimVocherService = claimVocherService;
	}

	public SysEmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(SysEmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public SysPositionService getPositionService() {
		return positionService;
	}

	public void setPositionService(SysPositionService positionService) {
		this.positionService = positionService;
	}
}	
