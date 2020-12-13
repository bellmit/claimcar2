package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  结算查询 返回商业 body
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiRecoveryQueryResBodyVo {

	@XmlElement(name = "QueryResultData")
	private List<BiRecoveryQueryDataVo> recoveryQueryDataList;

	
	public List<BiRecoveryQueryDataVo> getRecoveryQueryDataList() {
		return recoveryQueryDataList;
	}

	
	public void setRecoveryQueryDataList(List<BiRecoveryQueryDataVo> recoveryQueryDataList) {
		this.recoveryQueryDataList = recoveryQueryDataList;
	}
	
}
