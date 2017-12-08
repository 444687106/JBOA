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
 * 审批控制器
 *
 */
public class BizCheckResultAction extends ActionSupport {
	
	//审批业务接口
	private BizCheckResultService resultService;
	// 报销单业务接口
	private BizClaimVoucherService claimVocherService;
	// 员工业务接口
	private SysEmployeeService employeeService;
	// 职务集合接口
	private SysPositionService positionService;
	// 按钮数组
	private String[] button;
	// 审批意见
	private String textarea;
	
	// 审批
	public String sub() {
		// 从会话中获得当前员工和当前报销单
		BizClaimVoucher claimVoucher = (BizClaimVoucher) ActionContext.getContext().getSession().get("voucher");
		SysEmployee emp = (SysEmployee) ActionContext.getContext().getSession().get("emp");
		// 获得职务集合
		List<SysPosition> positionList = positionService.getAll();
		// 根据编号重新获得当前报销单,避免懒加载
		claimVoucher = claimVocherService.getBizClaimVoucher(claimVoucher.getId());
		// 创建审批对象
		BizCheckResult result = new BizCheckResult();
		result.setCheckTime(new Date());
		result.setComm(textarea);
		result.setBizClaimVoucher(claimVoucher);
		result.setSysEmployee(emp);
		if (button[0].equals("审批通过")) {
			result.setResult("通过");
			if (emp.getSysPosition().getNameCn().equals("部门经理")) {		// 部门经理审批
				if (claimVoucher.getTotalAccount() >= 5000) {
					claimVoucher.setStatus("待审批");
					// 提交到下一个审批人
					SysEmployee empSub = null;
					for (SysPosition item : positionList) {
						if (item.getNameCn().equals("总经理")) {
							List<SysEmployee> empList = new ArrayList<SysEmployee>(item.getSysEmployees());
							empSub = empList.get(0);
							break;
						}
					}
					if (empSub != null) {
						// 新增处理结果
						resultService.addNewResult(result);
						claimVoucher.setSysEmployeeByNextDealSn(empSub);
					} else {
						return "input";
					}
				} else {
					claimVoucher.setStatus("已审批");
					boolean flag = cashierSub(claimVoucher,positionList,result);
					if (!flag) {
						return "input";
					}
				}
			} else if(emp.getSysPosition().getNameCn().equals("总经理")) {		// 总经理审批
				claimVoucher.setStatus("已审批");
				cashierSub(claimVoucher,positionList,result);
				boolean flag = cashierSub(claimVoucher,positionList,result);
				if (!flag) {
					return "input";
				}
			} else if(emp.getSysPosition().getNameCn().equals("财务")) {		// 财务拨款
				claimVoucher.setStatus("已付款");
				claimVoucher.setSysEmployeeByNextDealSn(null);
				// 新增处理结果
				resultService.addNewResult(result);
			}
		} else if (button[0].equals("审批拒绝")) {
			claimVoucher.setStatus("已终止");
			result.setResult("拒绝");
			claimVoucher.setSysEmployeeByNextDealSn(null);
			// 新增处理结果
			resultService.addNewResult(result);
		} else if (button[0].equals("打回")) {
			claimVoucher.setStatus("已打回");
			claimVoucher.setSysEmployeeByNextDealSn(claimVoucher.getSysEmployeeByCreateSn());
		}
		// 修改报销单
		claimVocherService.editBizClaimVoucher(claimVoucher);
		return SUCCESS;
	}
	
	// 提交给财务审批
	private boolean cashierSub(BizClaimVoucher claimVoucher,List<SysPosition> positionList,BizCheckResult result){
		// 提交到下一个审批人
		SysEmployee empSub = null;
		for (SysPosition item : positionList) {
			if (item.getNameCn().equals("财务")) {
				List<SysEmployee> empList = new ArrayList<SysEmployee>(item.getSysEmployees());
				empSub = empList.get(0);
				break;
			}
		}
		if (empSub != null) {
			// 新增处理结果
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
