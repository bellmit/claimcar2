package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度请求接口 - 调度对象（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("SCHEDULEITEM")
public class HandleScheduleReqScheduleItem implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 任务id */
	@XStreamAlias("TASKID")
	private String taskId;
	
	/** 调度节点 */
	@XStreamAlias("NODETYPE")
	private String nodeType;
	
	/** 标的序号 */
	@XStreamAlias("ITEMNO")
	private String itemNo;
	
	/** 标的名称 */
	@XStreamAlias("ITEMNONAME")
	private String itemName;
	
	/** 事故车所在地省代码 */
	@XStreamAlias("PROVINCECODE")
	private String provinceCode;

	/** 事故车所在地城市代码 */
	@XStreamAlias("CITYCODE")
	private String cityCode;
	
	/** 事故车所在地区域代码 */
	@XStreamAlias("REGIONCODE")
	private String regionCode;
	
	/** 出险地点 */
	@XStreamAlias("DAMAGEADDRESS")
	private String damageAddress;
	
	/** 坐标 */
	@XStreamAlias("LNGXLATY")
	private String lngXlatY;
	
	/** 自定义区域编码 */
	@XStreamAlias("SELFDEFINAREACODE")
	private String selfDefinareaCode;
	
	

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getDamageAddress() {
		return damageAddress;
	}

	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}

	public String getLngXlatY() {
		return lngXlatY;
	}

	public void setLngXlatY(String lngXlatY) {
		this.lngXlatY = lngXlatY;
	}

	public String getSelfDefinareaCode() {
		return selfDefinareaCode;
	}

	public void setSelfDefinareaCode(String selfDefinareaCode) {
		this.selfDefinareaCode = selfDefinareaCode;
	}
	
	
}
