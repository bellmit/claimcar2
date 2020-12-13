package ins.platform.utils.xstreamconverters;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Dobule类型转换器对为空的字符串或格式化出错时转换为NULL
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2012-5-15
 * @since (2012-5-15 上午11:37:00): <br>
 */
public class SinosoftDoubleConverter extends AbstractSingleValueConverter {

	private final String decimalFormat;// 格式化输出数值如：0.00
	private final boolean nullToZero;// 是否将null或空值转换成0输出

	public SinosoftDoubleConverter(){
		this(null,false);
	}

	public SinosoftDoubleConverter(String decimalFormat){
		this.decimalFormat = decimalFormat;
		this.nullToZero = false;
	}

	public SinosoftDoubleConverter(String decimalFormat,boolean nullToZero){
		this.decimalFormat = decimalFormat;
		this.nullToZero = nullToZero;
	}

	public SinosoftDoubleConverter(boolean nullToZero){
		this.decimalFormat = null;
		this.nullToZero = nullToZero;
	}

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return type.equals(Double.class)||type.equals(double.class);
	}

	public Object fromString(String str) {
		if(str==null||str.trim().equals("")) return nullToZero ? 0d : null;
		try{
			return Double.parseDouble(str.trim());
		}
		catch(NumberFormatException e){
			return nullToZero ? 0d : null;
		}
	}

	public String toString(Object obj) {
		if(obj==null){
			return nullToZero ? "0" : null;
		}else{
			if(decimalFormat==null||"".equals(decimalFormat)){
				return new BigDecimal(obj.toString()).toString();
			}else{
				return new DecimalFormat(decimalFormat).format(new BigDecimal((Double)obj));
			}
		}
	}
}
