package ins.platform.utils.xstreamconverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * 日期格式转换器
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2012-4-19
 * @since (2012-4-19 上午11:24:12): <br>
 */
public class SinosoftDateConverter extends AbstractSingleValueConverter {

	private final String format;

	public SinosoftDateConverter(){
		this("yyyyMMdd");
	}

	public SinosoftDateConverter(String format){
		this.format = format;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return Date.class.isAssignableFrom(type);
	}

	@Override
	public Object fromString(String str) {
		Date date = null;
		if(str!=null&& !str.trim().equals("")){
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			try{
				date = dateFormat.parse(str);
			}
			catch(ParseException e){
				e.printStackTrace();
			}
		}
		return date;
	}

	public String toString(final Object obj) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		final Date value = (Date)obj;
		return obj==null ? "" : dateFormat.format(value);
	}

}
