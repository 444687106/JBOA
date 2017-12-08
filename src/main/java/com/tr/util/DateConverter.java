package com.tr.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * �Զ�������ת����
 *
 */
public class DateConverter extends StrutsTypeConverter {
	
	// ת����ʽ����
	private final DateFormat[] dfs = {
			new SimpleDateFormat("yyyy-MM-dd HH:mm"),
			new SimpleDateFormat("yyyyMMdd HH:mm"),
			new SimpleDateFormat("yyyy��MM��dd�� HH:mm"),
			new SimpleDateFormat("yyyy.MM.dd HH:mm"),
			new SimpleDateFormat("yyyy/MM/dd HH:mm"),
			new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("yyyyMMdd"),
			new SimpleDateFormat("yyyy��MM��dd��"),
			new SimpleDateFormat("yyyy.MM.dd"),
			new SimpleDateFormat("yyyy/MM/dd"),
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	};

	@Override
	public Object convertFromString(Map context, String[] values, Class toType) {
		String dateStr = values[0];		// ��ȡ�����ַ���
		for (int i = 0; i < dfs.length; i++) {		// ������ʽ�������ת��
			try {
				return dfs[i].parse(dateStr);
			} catch (Exception e) {
				continue;
			}
		}
		// ������Ϻ�û��ת���ɹ����׳��쳣
		throw new TypeConversionException();
	}

	@Override
	public String convertToString(Map context, Object object) {
		Date date = (Date) object;
		// �����ʽ
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

}
