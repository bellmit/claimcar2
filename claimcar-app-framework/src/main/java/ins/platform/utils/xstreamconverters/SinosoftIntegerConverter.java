package ins.platform.utils.xstreamconverters;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Integer类型转换器对为空的字符串或格式化出错时转换为NULL
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2012-5-15
 * @since (2012-5-15 上午11:50:34): <br>
 */
public class SinosoftIntegerConverter extends AbstractSingleValueConverter {

	private final boolean nullToZero;// 是否将null或空值转换成0输出

	public SinosoftIntegerConverter(){
		this(false);
	}

	public SinosoftIntegerConverter(boolean nullToZero){
		this.nullToZero = false;
	}

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return type.equals(Integer.class)||type.equals(int.class);
	}

	public Object fromString(String str) {
		if(str==null||str.trim().equals("")) return nullToZero ? 0 : null;
		try{
			long value = Long.decode(str).longValue();
			if(value<Integer.MIN_VALUE||value>0xFFFFFFFFl){
				return nullToZero ? 0 : null;
			}
			return new Integer((int)value);
		}
		catch(NumberFormatException e){
			return nullToZero ? 0 : null;
		}
	}

	public String toString(Object obj) {
		if(obj==null){
			return nullToZero ? "0" : null;
		}else{
			return obj.toString();
		}
	}

}
