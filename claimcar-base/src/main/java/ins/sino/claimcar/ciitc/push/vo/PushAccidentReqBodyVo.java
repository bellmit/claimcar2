package ins.sino.claimcar.ciitc.push.vo;

import java.io.Serializable;


import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Body")
public class PushAccidentReqBodyVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamImplicit(itemFieldName="DataInformation")
	private List<DataInformation> dataInformations;

	public List<DataInformation> getDataInformations() {
		return dataInformations;
	}

	public void setDataInformations(List<DataInformation> dataInformations) {
		this.dataInformations = dataInformations;
	}
	
}
