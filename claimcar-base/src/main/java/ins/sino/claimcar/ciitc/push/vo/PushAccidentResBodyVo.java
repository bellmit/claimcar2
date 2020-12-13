package ins.sino.claimcar.ciitc.push.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Body")
public class PushAccidentResBodyVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamImplicit(itemFieldName="ResInformation")
	private List<ResInformation> resInformations;
	
	public List<ResInformation> getResInformation() {
		return resInformations;
	}
	
	public void setResInformation(List<ResInformation> resInformations) {
		this.resInformations = resInformations;
	}
	
}
