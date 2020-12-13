package ins.sino.claimcar.subrogationsh.platform.vo;

import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * 互审信息（隶属于代位求偿信息列表）CheckList
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CopyInformationCheckListVo {

	@XmlElementWrapper(name = "CheckDetailList")
	@XmlElement(name = "CheckDetailData")
	private List<CopyInformationCheckDetailListVo> checkDetailListVo;

	/**1 待追偿方审核 2 待责任对方审核 3 互审通过 4 争议 5 放弃追偿 6 无需互审（责任对方未结案） 7 无需互审（责任对方已结案）*/
	@XmlElement(name = "CheckStats")
	private String checkStats;// 互审状态

	@XmlElement(name = "CheckEndDate")
	private Date checkEndDate;// 互审通过时间

	
	//setters and getters
	
	public List<CopyInformationCheckDetailListVo> getCheckDetailListVo() {
		return checkDetailListVo;
	}

	public void setCheckDetailListVo(List<CopyInformationCheckDetailListVo> checkDetailListVo) {
		this.checkDetailListVo = checkDetailListVo;
	}

	public String getCheckStats() {
		return checkStats;
	}

	public void setCheckStats(String checkStats) {
		this.checkStats = checkStats;
	}

	public Date getCheckEndDate() {
		return checkEndDate;
	}

	public void setCheckEndDate(Date checkEndDate) {
		this.checkEndDate = checkEndDate;
	}

}
