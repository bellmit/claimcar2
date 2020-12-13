package ins.sino.claimcar.claimjy.vo.price;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ReqInfo") 
public class JyPriceReqInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReturnURL") 
    private String  returnURL="";	//返回URL

	@XStreamAlias("RefreshURL") 
    private String  refreshURL="";	//返回刷新URL

	@XStreamAlias("AddonFlag") 
    private String  addonFlag="";	//追加定损标志

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public String getRefreshURL() {
		return refreshURL;
	}

	public void setRefreshURL(String refreshURL) {
		this.refreshURL = refreshURL;
	}

	public String getAddonFlag() {
		return addonFlag;
	}

	public void setAddonFlag(String addonFlag) {
		this.addonFlag = addonFlag;
	}
	
	

}
