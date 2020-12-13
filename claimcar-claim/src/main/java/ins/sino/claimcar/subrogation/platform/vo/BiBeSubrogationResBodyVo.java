package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 被代位查询接口 商业返回body
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiBeSubrogationResBodyVo {

	@XmlElement(name = "QueryResultData")
	private List<BiBeSubrogationDataVo> beSubrogationDataList;

	
	public List<BiBeSubrogationDataVo> getBeSubrogationDataList() {
		return beSubrogationDataList;
	}

	
	public void setBeSubrogationDataList(List<BiBeSubrogationDataVo> beSubrogationDataList) {
		this.beSubrogationDataList = beSubrogationDataList;
	}

	
	
}
