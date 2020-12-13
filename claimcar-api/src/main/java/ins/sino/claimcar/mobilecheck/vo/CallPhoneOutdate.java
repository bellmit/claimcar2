package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 查勘员号码更新请求vo
 * 
 * @author zjd
 *
 */
@XStreamAlias("OUTDATE")
public class CallPhoneOutdate implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("ClmNo")
	private String clmNo;

	public String getClmNo() {
		return clmNo;
	}

	public void setClmNo(String clmNo) {
		this.clmNo = clmNo;
	}

	
	
}
