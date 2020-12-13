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
public class CheckInfoSubmitResBodyVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("CHECKTASKINFO")
	private CheckTaskInfoSubmitResVo checkTaskInfo;
	
	@XStreamAlias("CARLIST")
	private List<CarInfoResVo> carInfo; //车辆信息
	
	@XStreamAlias("PERSONLIST")
	private List<PersonInfoResVo> personInfo; //人伤信息
	
	@XStreamAlias("PROPLIST")
	private List<PropInfoResVo> propInfo; //物损信息
	
	@XStreamAlias("ACCOUNTLIST")
	private List<AccountInfoResVo> accountInfo; //收款人信息

    
    public CheckTaskInfoSubmitResVo getCheckTaskInfo() {
        return checkTaskInfo;
    }

    
    public void setCheckTaskInfo(CheckTaskInfoSubmitResVo checkTaskInfo) {
        this.checkTaskInfo = checkTaskInfo;
    }

    
    public List<CarInfoResVo> getCarInfo() {
        return carInfo;
    }

    
    public void setCarInfo(List<CarInfoResVo> carInfo) {
        this.carInfo = carInfo;
    }

    
    public List<PersonInfoResVo> getPersonInfo() {
        return personInfo;
    }

    
    public void setPersonInfo(List<PersonInfoResVo> personInfo) {
        this.personInfo = personInfo;
    }

    
    public List<PropInfoResVo> getPropInfo() {
        return propInfo;
    }

    
    public void setPropInfo(List<PropInfoResVo> propInfo) {
        this.propInfo = propInfo;
    }

    
    public List<AccountInfoResVo> getAccountInfo() {
        return accountInfo;
    }

    
    public void setAccountInfo(List<AccountInfoResVo> accountInfo) {
        this.accountInfo = accountInfo;
    }
	
	
}
