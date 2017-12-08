package com.tr.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

/**
 * 自定义日期转换器
 *
 */
public class DateConverter extends StrutsTypeConverter {
	
	// 转换格式数组
	private final DateFormat[] dfs = {
			new SimpleDateFormat("yyyy-MM-dd HH:mm"),
			new SimpleDateFormat("yyyyMMdd HH:mm"),
			new SimpleDateFormat("yyyy年MM月dd日 HH:mm"),
			new SimpleDateFormat("yyyy.MM.dd HH:mm"),
			new SimpleDateFormat("yyyy/MM/dd HH:mm"),
			new SimpleDateFormat("yyyy-MM-dd"),
			new SimpleDateFormat("yyyyMMdd"),
			new SimpleDateFormat("yyyy年MM月dd日"),
			new SimpleDateFormat("yyyy.MM.dd"),
			new SimpleDateFormat("yyyy/MM/dd"),
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	};

	@Override
	public Object convertFromString(Map context, String[] values, Class toType) {
		String dateStr = values[0];		// 获取日期字符串
		for (int i = 0; i < dfs.length; i++) {		// 遍历格式数组进行转换
			try {
				return dfs[i].parse(dateStr);
			} catch (Exception e) {
				continue;
			}
		}
		// 遍历完毕后没有转换成功则抛出异常
		throw new TypeConversionException();
	}

	@Override
	public String convertToString(Map context, Object object) {
		Date date = (Date) object;
		// 输出格式
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

}
