/******************************************************************************
* CREATETIME : 2016年2月29日 下午4:08:37
******************************************************************************/
package ins.sino.claimcar.carplatform.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年2月29日
 */
public class DateXmlAdapter extends XmlAdapter<String,Date> {

	@Override
	public Date unmarshal(String v) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		return format.parse(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		return format.format(v);
	}

	/*	@Override
		public String unmarshal(Date v) throws Exception {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			return format.format(v);
		}

		@Override
		public Date marshal(String v) throws Exception {
			return DateUtils.parseDate(v);
		}*/
}
