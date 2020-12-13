package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 手动调度返回查勘定损员信息接口 - 调度对象（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("SCHEDULEITEM")
public class HandleScheduleBackReqScheduleItem implements Serializable {

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
	
	/** 处理人代码 */
	@XStreamAlias("NEXTHANDLERCODE")
	private String nextHandlerCode;
	
	/** 处理人名称 */
	@XStreamAlias("NEXTHANDLERNAME")
	private String nextHandlerName;
	
	/** 处理人员归属机构编码 */
	@XStreamAlias("SCHEDULEOBJECTID")
	private String scheduleObjectId;
	
	/** 处理人员归属机构名称 */
	@XStreamAlias("SCHEDULEOBJECTNAME")
	private String scheduleObjectName;
	
	/** 是否存在移动端权限 */
	@XStreamAlias("ISCLAIMHELPER")
	private String isClaimHelper;
	
	/** 是否自定义 */
	@XStreamAlias("ISSELFDEFIN")
	private String isSelfDefin;

	/** 自定义区域编码 */
	@XStreamAlias("SELFDEFINAREACODE")
	private String selfDefinAreaCode;
	
	/** 关联查勘员 */
	@XStreamAlias("RELATEHANDLERNAME")
	private String relateHandlerName;
	
	/** 关联查勘员电话 */
	@XStreamAlias("RELATEHANDLERMOBILE")
	private String relateHandlerMobile;
	
	/** 是否承保地人员*/
	@XStreamAlias("ISCOMUSERCODE")
	private String isComuserCode;
	
	/** 呼出电话 */
	@XStreamAlias("CALLNUMBER")
	private String callNumber;
	
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

	public String getNextHandlerCode() {
		return nextHandlerCode;
	}

	public void setNextHandlerCode(String nextHandlerCode) {
		this.nextHandlerCode = nextHandlerCode;
	}

	public String getNextHandlerName() {
		return nextHandlerName;
	}

	public void setNextHandlerName(String nextHandlerName) {
		this.nextHandlerName = nextHandlerName;
	}

	public String getScheduleObjectId() {
		return scheduleObjectId;
	}

	public void setScheduleObjectId(String scheduleObjectId) {
		this.scheduleObjectId = scheduleObjectId;
	}

	public String getScheduleObjectName() {
		return scheduleObjectName;
	}

	public void setScheduleObjectName(String scheduleObjectName) {
		this.scheduleObjectName = scheduleObjectName;
	}

	public String getIsClaimHelper() {
		return isClaimHelper;
	}

	public void setIsClaimHelper(String isClaimHelper) {
		this.isClaimHelper = isClaimHelper;
	}

	public String getIsSelfDefin() {
		return isSelfDefin;
	}

	public void setIsSelfDefin(String isSelfDefin) {
		this.isSelfDefin = isSelfDefin;
	}

	public String getSelfDefinAreaCode() {
		return selfDefinAreaCode;
	}

	public void setSelfDefinAreaCode(String selfDefinAreaCode) {
		this.selfDefinAreaCode = selfDefinAreaCode;
	}

	public String getRelateHandlerName() {
		return relateHandlerName;
	}

	public void setRelateHandlerName(String relateHandlerName) {
		this.relateHandlerName = relateHandlerName;
	}

	public String getRelateHandlerMobile() {
		return relateHandlerMobile;
	}

	public void setRelateHandlerMobile(String relateHandlerMobile) {
		this.relateHandlerMobile = relateHandlerMobile;
	}

	public String getIsComuserCode() {
		return isComuserCode;
	}

	public void setIsComuserCode(String isComuserCode) {
		this.isComuserCode = isComuserCode;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public void setCallNumber(String callNumber) {
		this.callNumber = callNumber;
	}
	
	
}
