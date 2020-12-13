package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("ReqInfo")
public class JyDLChkReqBodyReqInfoVo implements Serializable {

	@XStreamAlias("ReturnURL")
	private String returnURL;

	@XStreamAlias("RefreshURL")
	private String refreshURL;

	@XStreamAlias("AddOnFlag")
	private String addOnFlag;

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

	public String getAddOnFlag() {
		return addOnFlag;
	}

	public void setAddOnFlag(String addOnFlag) {
		this.addOnFlag = addOnFlag;
	}

	public JyDLChkReqBodyReqInfoVo(String returnURL,String refreshURL,String addOnFlag){
		super();
		this.returnURL = returnURL;
		this.refreshURL = refreshURL;
		this.addOnFlag = addOnFlag;
	}

	public JyDLChkReqBodyReqInfoVo(){
		super();
	}

}
