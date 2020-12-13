package ins.sino.claimcar.subrogationsh.platform.vo;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 互审明细信息（多条）（隶属于互审）CheckDetailList
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CopyInformationCheckDetailListVo {

	@XmlElement(name = "RecoverAmount")
	private String recoverAmount;// 追偿金额

	@XmlElement(name = "CompensateAmount")
	private String compensateAmount;// 清付金额

	@XmlElement(name = "CheckDate")
	private Date checkDate;// 互审时间

	@XmlElement(name = "CheckOpinion")
	private String checkOpinion;// 互审意见

	/** 1-追偿方,2-责任对方 */
	@XmlElement(name = "CheckOwnType")
	private String checkOwnType;// 审核方类型

	/**1 待追偿方审核 2 待责任对方审核 3 互审通过 4 争议 5 放弃追偿 6 无需互审（责任对方未结案） 7 无需互审（责任对方已结案）*/
	@XmlElement(name = "CheckStats")
	private String checkStats;// 互审状态

	
	//setters and getters
	
	public String getRecoverAmount() {
		return recoverAmount;
	}

	public void setRecoverAmount(String recoverAmount) {
		this.recoverAmount = recoverAmount;
	}

	public String getCompensateAmount() {
		return compensateAmount;
	}

	public void setCompensateAmount(String compensateAmount) {
		this.compensateAmount = compensateAmount;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public String getCheckOwnType() {
		return checkOwnType;
	}

	public void setCheckOwnType(String checkOwnType) {
		this.checkOwnType = checkOwnType;
	}

	public String getCheckStats() {
		return checkStats;
	}

	public void setCheckStats(String checkStats) {
		this.checkStats = checkStats;
	}

}
