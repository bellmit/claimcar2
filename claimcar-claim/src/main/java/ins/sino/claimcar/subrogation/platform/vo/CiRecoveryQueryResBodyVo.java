package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 结算查询 商业返回body
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiRecoveryQueryResBodyVo {

	@XmlElementWrapper(name = "QUERY_RESULT_LIST")
	@XmlElement(name = "QUERY_RESULT_DATA")
	private List<CiRecoveryQueryDataVo> recoveryQueryList;

	
	public List<CiRecoveryQueryDataVo> getRecoveryQueryList() {
		return recoveryQueryList;
	}

	
	public void setRecoveryQueryList(List<CiRecoveryQueryDataVo> recoveryQueryList) {
		this.recoveryQueryList = recoveryQueryList;
	}

}
