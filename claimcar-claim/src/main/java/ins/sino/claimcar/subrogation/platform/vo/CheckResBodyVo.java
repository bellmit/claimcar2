package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 被代位查询接口 交强返回body
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CheckResBodyVo {

	@XmlElementWrapper(name = "QUERY_RESULT_LIST")
	@XmlElement(name = "QUERY_RESULT_DATA")
	private List<CiBeSubrogationDataVo> beSubrogationList;

	
	public List<CiBeSubrogationDataVo> getBeSubrogationList() {
		return beSubrogationList;
	}

	
	public void setBeSubrogationList(List<CiBeSubrogationDataVo> beSubrogationList) {
		this.beSubrogationList = beSubrogationList;
	}

	
	

	
	
	
}
