package ins.sino.claimcar.carplatform.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateXmlAdapterDeath extends XmlAdapter<String,Date>{

	@Override
	public Date unmarshal(String v) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.parse(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(v);
	}

}
