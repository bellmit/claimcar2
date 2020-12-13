package ins.sino.claimcar.certifyupload.vo;

import java.util.Date;


public class ImageFileIndexVoBase implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String imgId;//--主键，使用UUID
	private String bussNo;//--业务号 
	private String appendPath;//--扩展路径
	private String fileName;//--文件名称
	private String fileOrgName;//--原文件名称
	private String typePath;//--类型路径
	private String typeName;//--类型名称
	private String typePath1;//--分类路径1
	private String typePath2;//--分类路径2
	private String typePath3;//--分类路径3
	private String typePath4;//--分类路径4
	private String typePath5;//--分类路径5
	private String typePath6;//--分类路径6
	private String typePath7;//--分类路径7
	private String typePath8;//--分类路径8
	private String typePath9;//--分类路径9
	private String policyNo;//--保单号
	private String registNo;//--报案号
	private String licenseNo;//--车牌号
	private String carOwner;//--车主
	private String sysTag;//--系统标签 
	private String userTag;//--自定义标签
	private String imgFileFlag;//--是否是图片（0-否 1-是）
	private String reason;//--元数据信息
	private Double fileSize;//--文件大小kb
	private String fileExt;//--文件扩展名
	private String serverID;//--影像服务器ID
	private String serverURL;//--影像服务器URL
	private String remark;//--文件备注
	private String validStatus;//--有效标志（0-无效，1-有效）
	private String userCode;//--上传用户代码
	private String comCode;//--上传机构代码
	private String uploadNode;//--上传节点
	private Date createTime;//--创建时间
	private String operatorCode;//--操作员
	private Date updatedTime;//--修改时间
	private String shootModel;//--相机型号
	private String shootTime;//--拍摄时间
	private Integer autoZiped;//--是否自动zip压缩过，1-是
	private String rootPath;//根路径
	
	public String getImgId() {
		return imgId;
	}
	
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	
	public String getBussNo() {
		return bussNo;
	}
	
	public void setBussNo(String bussNo) {
		this.bussNo = bussNo;
	}
	
	public String getAppendPath() {
		return appendPath;
	}
	
	public void setAppendPath(String appendPath) {
		this.appendPath = appendPath;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileOrgName() {
		return fileOrgName;
	}
	
	public void setFileOrgName(String fileOrgName) {
		this.fileOrgName = fileOrgName;
	}
	
	public String getTypePath() {
		return typePath;
	}
	
	public void setTypePath(String typePath) {
		this.typePath = typePath;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getTypePath1() {
		return typePath1;
	}
	
	public void setTypePath1(String typePath1) {
		this.typePath1 = typePath1;
	}
	
	public String getTypePath2() {
		return typePath2;
	}
	
	public void setTypePath2(String typePath2) {
		this.typePath2 = typePath2;
	}
	
	public String getTypePath3() {
		return typePath3;
	}
	
	public void setTypePath3(String typePath3) {
		this.typePath3 = typePath3;
	}
	
	public String getTypePath4() {
		return typePath4;
	}
	
	public void setTypePath4(String typePath4) {
		this.typePath4 = typePath4;
	}
	
	public String getTypePath5() {
		return typePath5;
	}
	
	public void setTypePath5(String typePath5) {
		this.typePath5 = typePath5;
	}
	
	public String getTypePath6() {
		return typePath6;
	}
	
	public void setTypePath6(String typePath6) {
		this.typePath6 = typePath6;
	}
	
	public String getTypePath7() {
		return typePath7;
	}
	
	public void setTypePath7(String typePath7) {
		this.typePath7 = typePath7;
	}
	
	public String getTypePath8() {
		return typePath8;
	}
	
	public void setTypePath8(String typePath8) {
		this.typePath8 = typePath8;
	}
	
	public String getTypePath9() {
		return typePath9;
	}
	
	public void setTypePath9(String typePath9) {
		this.typePath9 = typePath9;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public String getCarOwner() {
		return carOwner;
	}
	
	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}
	
	public String getSysTag() {
		return sysTag;
	}
	
	public void setSysTag(String sysTag) {
		this.sysTag = sysTag;
	}
	
	public String getUserTag() {
		return userTag;
	}
	
	public void setUserTag(String userTag) {
		this.userTag = userTag;
	}
	
	public String getImgFileFlag() {
		return imgFileFlag;
	}
	
	public void setImgFileFlag(String imgFileFlag) {
		this.imgFileFlag = imgFileFlag;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Double getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Double fileSize) {
		this.fileSize = fileSize;
	}
	
	public String getFileExt() {
		return fileExt;
	}
	
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	
	public String getServerID() {
		return serverID;
	}
	
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	
	public String getServerURL() {
		return serverURL;
	}
	
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getValidStatus() {
		return validStatus;
	}
	
	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}
	
	public String getUserCode() {
		return userCode;
	}
	
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public String getComCode() {
		return comCode;
	}
	
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	
	public String getUploadNode() {
		return uploadNode;
	}
	
	public void setUploadNode(String uploadNode) {
		this.uploadNode = uploadNode;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getOperatorCode() {
		return operatorCode;
	}
	
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	
	public Date getUpdatedTime() {
		return updatedTime;
	}
	
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	public String getShootModel() {
		return shootModel;
	}
	
	public void setShootModel(String shootModel) {
		this.shootModel = shootModel;
	}
	
	public String getShootTime() {
		return shootTime;
	}
	
	public void setShootTime(String shootTime) {
		this.shootTime = shootTime;
	}
	
	public Integer getAutoZiped() {
		return autoZiped;
	}
	
	public void setAutoZiped(Integer autoZiped) {
		this.autoZiped = autoZiped;
	}
	
	public String getRootPath() {
		return rootPath;
	}
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
    
}
