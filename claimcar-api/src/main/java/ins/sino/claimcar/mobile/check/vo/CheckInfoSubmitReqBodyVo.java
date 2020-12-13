package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CHECKTASKINFO")
public class CheckInfoSubmitReqBodyVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("CHECKTASKINFO")
	private CheckTaskInfoSubmitVo checkTaskInfo;
	
	@XStreamAlias("CARLIST")
	private List<CarInfoVo> carInfo; //车辆信息
	
	@XStreamAlias("PERSONLIST")
	private List<PersonInfoVo> personInfo; //人伤信息
	
	@XStreamAlias("PROPLIST")
	private List<PropInfoVo> propInfo; //物损信息
	
	@XStreamAlias("ACCOUNTLIST")
	private List<AccountInfoVo> accountInfo; //收款人信息
	
	@XStreamAlias("EXTENDINFO")
	private CheckExtendInfoVo extendInfoVo;	//查勘扩展信息
	
	@XStreamAlias("PHOTOLIST")
	private List<PhotoInfo> photoInfoList;	//单证目录信息
	
	public CheckTaskInfoSubmitVo getCheckTaskInfo() {
		return checkTaskInfo;
	}
	public void setCheckTaskInfo(CheckTaskInfoSubmitVo checkTaskInfo) {
		this.checkTaskInfo = checkTaskInfo;
	}
	public List<CarInfoVo> getCarInfo() {
		return carInfo;
	}
	public void setCarInfo(List<CarInfoVo> carInfo) {
		this.carInfo = carInfo;
	}
	public List<PersonInfoVo> getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(List<PersonInfoVo> personInfo) {
		this.personInfo = personInfo;
	}
	public List<PropInfoVo> getPropInfo() {
		return propInfo;
	}
	public void setPropInfo(List<PropInfoVo> propInfo) {
		this.propInfo = propInfo;
	}
	public List<AccountInfoVo> getAccountInfo() {
		return accountInfo;
	}
	public void setAccountInfo(List<AccountInfoVo> accountInfo) {
		this.accountInfo = accountInfo;
	}
	public CheckExtendInfoVo getExtendInfoVo() {
		return extendInfoVo;
	}
	public void setExtendInfoVo(CheckExtendInfoVo extendInfoVo) {
		this.extendInfoVo = extendInfoVo;
	}
	public List<PhotoInfo> getPhotoInfoList() {
		return photoInfoList;
	}
	public void setPhotoInfoList(List<PhotoInfo> photoInfoList) {
		this.photoInfoList = photoInfoList;
	}


	
	
	
}
