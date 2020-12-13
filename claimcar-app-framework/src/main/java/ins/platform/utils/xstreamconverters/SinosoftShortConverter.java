package ins.platform.utils.xstreamconverters;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Short类型转换器对为空的字符串或格式化出错时转换为NULL
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2012-5-15
 * @since (2012-5-15 下午12:03:32): <br>
 */
public class SinosoftShortConverter extends AbstractSingleValueConverter {

	private final boolean nullToZero;// 是否将null或空值转换成0输出

	public SinosoftShortConverter(){
		this(false);
	}

	public SinosoftShortConverter(boolean nullToZero){
		this.nullToZero = false;
	}

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return type.equals(Short.class)||type.equals(short.class);
	}

	public Object fromString(String str) {
		if(str==null||str.trim().equals("")) return nullToZero ? 0 : null;
		try{
			long value = Integer.decode(str).intValue();
			if(value<Short.MIN_VALUE||value>0xFFFF){
				return nullToZero ? 0 : null;
			}
			return new Short((short)value);
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
