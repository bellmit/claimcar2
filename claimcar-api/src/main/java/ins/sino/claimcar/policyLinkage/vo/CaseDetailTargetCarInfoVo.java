package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */

@XStreamAlias("targetCarInfo")  //标的车
public class CaseDetailTargetCarInfoVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("driverName") 
	private String driverName; // 驾驶员姓名

	
	@XStreamAlias("hphm") 
	private String hphm; // 标的车车牌号
	
	@XStreamAlias("injureLocation") 
	private String injureLocation; // 大致受损部位
	/*左前(1),右前(2),左后(3),右后(4),正前(5),正后(6),
	左侧(7),右侧(8),无(99)*/
	
	@XStreamAlias("isResp") 
	private String isResp; // 是否已向保险公司报案
	
	@XStreamAlias("jqx") 
	private String jqx; // 交强险
	
	@XStreamAlias("jqxOther") 
	private String jqxOther; // 交强险-其他
	
	@XStreamAlias("phone") 
	private String phone; // 联系方式
	
	@XStreamAlias("syx") 
	private String syx; // 商业险
	
	@XStreamAlias("type") 
	private String type; // 责任类型   全责(1),同责(2),主责(3),次责(4),无责(5),
	
	@XStreamAlias("syxOther") 
	private String syxOther; // 商业险-其他
	
	@XStreamAlias("orders") 
	private String orders; // 排序

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getHphm() {
		return hphm;
	}

	public void setHphm(String hphm) {
		this.hphm = hphm;
	}

	public String getInjureLocation() {
		return injureLocation;
	}

	public void setInjureLocation(String injureLocation) {
		this.injureLocation = injureLocation;
	}

	public String getIsResp() {
		return isResp;
	}

	public void setIsResp(String isResp) {
		this.isResp = isResp;
	}

	public String getJqx() {
		return jqx;
	}

	public void setJqx(String jqx) {
		this.jqx = jqx;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSyx() {
		return syx;
	}

	public void setSyx(String syx) {
		this.syx = syx;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSyxOther() {
		return syxOther;
	}

	public void setSyxOther(String syxOther) {
		this.syxOther = syxOther;
	}

	public String getJqxOther() {
		return jqxOther;
	}

	public void setJqxOther(String jqxOther) {
		this.jqxOther = jqxOther;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}
	
	
	
}
