package com.tr.service.impl;

import java.io.Serializable;
import java.util.List;

import com.tr.dao.Dao;
import com.tr.entity.BizClaimVoucherDetail;
import com.tr.service.BizClaimVoucherDetailService;

/**
 * 报销单明细业务实现类
 *
 */
public class BizClaimVoucherDetailServiceImpl implements
		BizClaimVoucherDetailService {
	
	// dao 接口
	private Dao dao;
	
	@Override
	public void addNewClaimVoucherDetail(
			List<BizClaimVoucherDetail> bizClaimVoucherDetails) {
		for (BizClaimVoucherDetail claimVoucherDetail : bizClaimVoucherDetails) {
			Long id = (Long) dao.save(claimVoucherDetail);
			if (id == null || id <= 0) {
				throw new RuntimeException("添加明细失败!!!");
			}
		}
		
	}

	@Override
	public boolean deleteDetail(Long id) {
		try {
			int count = (Integer) dao.deleteDetail(id);
			return count >= 0 ? true : false;
		} catch (Exception e) {
			return false;
		}
	}

	public Dao getDao() {
		return dao;
	}

	public void setDao(Dao dao) {
		this.dao = dao;
	}

}
