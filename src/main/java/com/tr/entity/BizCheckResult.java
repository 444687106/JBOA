package com.tr.entity;

import java.util.Date;

/**
 * BizCheckResult entity. @author MyEclipse Persistence Tools
 */

public class BizCheckResult implements java.io.Serializable {

	// Fields

	private Long id;
	private SysEmployee sysEmployee;
	private BizClaimVoucher bizClaimVoucher;
	private Date checkTime;
	private String result;
	private String comm;

	// Constructors

	/** default constructor */
	public BizCheckResult() {
	}

	/** full constructor */
	public BizCheckResult(SysEmployee sysEmployee,
			BizClaimVoucher bizClaimVoucher, Date checkTime, String result,
			String comm) {
		this.sysEmployee = sysEmployee;
		this.bizClaimVoucher = bizClaimVoucher;
		this.checkTime = checkTime;
		this.result = result;
		this.comm = comm;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SysEmployee getSysEmployee() {
		return this.sysEmployee;
	}

	public void setSysEmployee(SysEmployee sysEmployee) {
		this.sysEmployee = sysEmployee;
	}

	public BizClaimVoucher getBizClaimVoucher() {
		return this.bizClaimVoucher;
	}

	public void setBizClaimVoucher(BizClaimVoucher bizClaimVoucher) {
		this.bizClaimVoucher = bizClaimVoucher;
	}

	public Date getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getComm() {
		return this.comm;
	}

	public void setComm(String comm) {
		this.comm = comm;
	}

}