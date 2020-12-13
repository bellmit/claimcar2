package ins.sino.claimcar.subrogationsh.platform.vo;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 代位求偿信息抄回返回的信息（成功）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class CopyInformationResBodyVo {

	@XmlElementWrapper(name = "SubrogationList")
	@XmlElement(name = "SubrogationData")
	private List<CopyInformationSubrogationListVo> subrogationList;// 代位求偿信息列表（多条）

	public List<CopyInformationSubrogationListVo> getSubrogationList() {
		return subrogationList;
	}

	public void setSubrogationList(List<CopyInformationSubrogationListVo> subrogationList) {
		this.subrogationList = subrogationList;
	}

}
