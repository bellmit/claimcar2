package ins.sino.claimcar.ciitc.vo.compe;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Body")
public class CiitcCompeReqBody  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReqPayInformation") 
	private List<ReqPayInformation>  payInfoList;

	public List<ReqPayInformation> getPayInfoList() {
		return payInfoList;
	}

	public void setPayInfoList(List<ReqPayInformation> payInfoList) {
		this.payInfoList = payInfoList;
	}
	
	
}
