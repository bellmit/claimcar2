package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 保单查询 Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class CiClaimResBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiClaimDataVo CiClaimDataVos;

	public CiClaimDataVo getCiClaimDataVos() {
		return CiClaimDataVos;
	}

	public void setCiClaimDataVos(CiClaimDataVo ciClaimDataVos) {
		CiClaimDataVos = ciClaimDataVos;
	}




	
	
	
}
