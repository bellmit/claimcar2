/******************************************************************************
* CREATETIME : 2016年5月26日 上午11:06:31
******************************************************************************/
package ins.sino.claimcar.carplatform.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * <pre></pre>
 * @author ★Luwei
 */
public class DateXmlAdapterSH extends XmlAdapter<String,Date>{

	@Override
	public Date unmarshal(String v) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.parse(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(v);
	}

}
