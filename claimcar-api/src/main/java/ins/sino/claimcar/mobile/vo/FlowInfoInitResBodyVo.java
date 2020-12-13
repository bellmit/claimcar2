package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("BODY")
public class FlowInfoInitResBodyVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号
	
    @XStreamAlias("LICENSENO")
    private String licenseNo; //车牌号
    
	@XStreamAlias("TASKINFOLIST")
	private List<TaskInfoVo> taskInfoList; //节点信息
	
	@XStreamAlias("PAYINFOLIST")
	private List<PayInfoVo> payInfoList; //支付信息

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

    public List<TaskInfoVo> getTaskInfoList() {
		return taskInfoList;
	}

	public void setTaskInfoList(List<TaskInfoVo> taskInfoList) {
		this.taskInfoList = taskInfoList;
	}

	public List<PayInfoVo> getPayInfoList() {
		return payInfoList;
	}

	public void setPayInfoList(List<PayInfoVo> payInfoList) {
		this.payInfoList = payInfoList;
	}
	
	
}
