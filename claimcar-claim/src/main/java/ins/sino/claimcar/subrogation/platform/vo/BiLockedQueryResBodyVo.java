package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 锁定查询接口 返回body 商业 
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiLockedQueryResBodyVo {

	@XmlElement(name = "LockedPolicyData")
	private List<BiLockedPolicyDataVo> prplLockedNotifies;

	
	public List<BiLockedPolicyDataVo> getPrplLockedNotifies() {
		return prplLockedNotifies;
	}

	
	public void setPrplLockedNotifies(List<BiLockedPolicyDataVo> prplLockedNotifies) {
		this.prplLockedNotifies = prplLockedNotifies;
	}
	
	
	
}
