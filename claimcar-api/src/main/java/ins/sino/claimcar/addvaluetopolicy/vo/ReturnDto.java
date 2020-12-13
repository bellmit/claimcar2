package ins.sino.claimcar.addvaluetopolicy.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 车理赔对接车承保增值条款理赔次数查询    返回报文
 * @author Administrator
 *
 */
public class ReturnDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String policyNo;
	private List<KindDto> kindDtoList;
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public List<KindDto> getKindDtoList() {
		return kindDtoList;
	}
	public void setKindDtoList(List<KindDto> kindDtoList) {
		this.kindDtoList = kindDtoList;
	}
	
	
}
