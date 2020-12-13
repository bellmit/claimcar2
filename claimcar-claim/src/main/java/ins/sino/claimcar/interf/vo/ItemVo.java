package ins.sino.claimcar.interf.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ItemVo")
public class ItemVo  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("CertiNo")
	private String certiNo;//计算书号
	@XStreamAlias("SerialNo")
	private Long serialNo;//序列号
	
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	public Long getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Long serialNo) {
		this.serialNo = serialNo;
	}
}
