package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 返回风险预警承保信息查询 Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class SubrogationCheckResBodyVo {

	@XmlElement(name = "CheckBase")
	private List<SubrogationCheckDataVo> subrogationCheckDataVoList;

	public List<SubrogationCheckDataVo> getSubrogationCheckDataVoList() {
		return subrogationCheckDataVoList;
	}

	public void setSubrogationCheckDataVoList(
			List<SubrogationCheckDataVo> subrogationCheckDataVoList) {
		this.subrogationCheckDataVoList = subrogationCheckDataVoList;
	}
	
}
