package ins.platform.utils.xstreamconverters;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Float类型转换器对为空的字符串或格式化出错时转换为NULL
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2012-5-15
 * @since (2012-5-15 上午11:47:53): <br>
 */
public class SinosoftFloatConverter extends AbstractSingleValueConverter {

	private final String decimalFormat;// 格式化输出数值如：0.00
	private final boolean nullToZero;// 是否将null或空值转换成0输出

	public SinosoftFloatConverter(){
		this(null,false);
	}

	public SinosoftFloatConverter(String decimalFormat){
		this.decimalFormat = decimalFormat;
		this.nullToZero = false;
	}

	public SinosoftFloatConverter(String decimalFormat,boolean nullToZero){
		this.decimalFormat = decimalFormat;
		this.nullToZero = nullToZero;
	}

	public SinosoftFloatConverter(boolean nullToZero){
		this.decimalFormat = null;
		this.nullToZero = nullToZero;
	}

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return type.equals(Float.class)||type.equals(float.class);
	}

	public Object fromString(String str) {
		if(str==null||str.trim().equals("")) return nullToZero ? 0 : null;
		try{
			return Float.parseFloat(str.trim());
		}
		catch(NumberFormatException e){
			return nullToZero ? 0 : null;
		}
	}

	public String toString(Object obj) {
		if(obj==null){
			return nullToZero ? "0" : null;
		}else{
			if(decimalFormat==null||"".equals(decimalFormat)){
				return obj.toString();
			}else{
				return new DecimalFormat(decimalFormat).format(new BigDecimal((Float)obj));
			}
		}
	}

}
