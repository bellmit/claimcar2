package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class CiitcAccidentReqBodyVo implements Serializable{
	 private static final long serialVersionUID = 1L;
	@XStreamAlias("DataInformation")
	private DataInformation dataInformation;
	public DataInformation getDataInformation() {
		return dataInformation;
	}
	public void setDataInformation(DataInformation dataInformation) {
		this.dataInformation = dataInformation;
	}
	
	
}
