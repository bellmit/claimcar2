package ins.sino.claimcar.claimcarYJ.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class ImageReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("REGISTNO")
	private String registNo;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	
}
