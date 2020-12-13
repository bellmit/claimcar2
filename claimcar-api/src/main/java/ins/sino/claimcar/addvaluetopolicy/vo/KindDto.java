package ins.sino.claimcar.addvaluetopolicy.vo;

import java.io.Serializable;
/**
 * 车理赔对接车承保增值条款理赔次数查询    返回报文 实体类 vo
 * @author Administrator
 *
 */
public class KindDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String kindCode;
	private String times;
	public String getKindCode() {
		return kindCode;
	}
	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}
	public String getTimes() {
		return times;
	}
	public void setTimes(String times) {
		this.times = times;
	}
	
}
