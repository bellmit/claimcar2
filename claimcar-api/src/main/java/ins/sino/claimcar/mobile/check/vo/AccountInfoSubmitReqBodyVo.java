package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * 收款人信息（快赔请求理赔）
 * @author ★zhujunde
 */
@XStreamAlias("CHECKTASKINFO")
public class AccountInfoSubmitReqBodyVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
    @XStreamAlias("REGISTNO")
    private String registNo; //报案号
    
    @XStreamAlias("NEXTHANDLERCODE")
    private String nextHandlerCode; //处理人代码
    
    @XStreamAlias("NEXTHANDLERNAME")
    private String nextHandlerName; //处理人名称
    
    @XStreamAlias("SCHEDULEOBJECTID")
    private String scheduleObjectId; //处理人员归属机构编码
    
    @XStreamAlias("SCHEDULEOBJECTNAME")
    private String scheduleObjectName; //处理人员归属机构名称
    
    @XStreamAlias("ACCOUNTLIST")
    private List<AccountInfoVo> accountInfo; //收款人信息

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
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


    public List<AccountInfoVo> getAccountInfo() {
        return accountInfo;
    }

    
    public void setAccountInfo(List<AccountInfoVo> accountInfo) {
        this.accountInfo = accountInfo;
    }
    
    
}
