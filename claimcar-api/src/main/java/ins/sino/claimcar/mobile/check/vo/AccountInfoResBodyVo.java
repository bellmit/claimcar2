package ins.sino.claimcar.mobile.check.vo;

import ins.sino.claimcar.mobileCheckCommon.vo.MobileCheckBody;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("BODY")
public class AccountInfoResBodyVo extends MobileCheckBody implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ACCOUNTLIST")
	private List<AccountResVo> accountResVoList;

    
    public List<AccountResVo> getAccountResVoList() {
        return accountResVoList;
    }

    
    public void setAccountResVoList(List<AccountResVo> accountResVoList) {
        this.accountResVoList = accountResVoList;
    }
	
	
}
