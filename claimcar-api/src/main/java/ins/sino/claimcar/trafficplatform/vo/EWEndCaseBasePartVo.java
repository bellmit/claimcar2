package ins.sino.claimcar.trafficplatform.vo;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 山东预警-结案登记-BasePart
 * <pre></pre>
 * @author ★WeiLanlei
 */
@XStreamAlias("BasePart")
public class EWEndCaseBasePartVo {
	
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ClaimSequenceNo") 
	private String claimSequenceNo;// 理赔编号
	
	@XStreamAlias("ConfirmSequenceNo")
	private String confirmSequenceNo;// 投保确认码

	@XStreamAlias("ClaimNotificationNo")
	private String claimNotificationNo;// 报案号
	
	@XStreamAlias("ClaimAmount")
	private BigDecimal claimAmount;// 赔款总金额（含施救费）

	@XStreamAlias("ClaimCloseTime")
	private String claimCloseTime;// 结案时间；精确到分
	
	@XStreamAlias("IsInsured")
	private String isInsured;// 是否属于保险责任
	
	@XStreamAlias("ClaimType")
	private String claimType;// 理赔类型
	
	@XStreamAlias("PayCause")
	private String payCause;// 垫付原因
	
	@XStreamAlias("RefuseCause")
	private String refuseCause;// 拒赔原因描述
	
	@XStreamAlias("AccidentType")
	private String accidentType;// 保险事故分类
	
	@XStreamAlias("IsRefuseCase")
	private String isRefuseCase;// 是否拒赔案件
	
	@XStreamAlias("DirectClaimAmount")
	private BigDecimal directClaimAmount;// 直接理赔费用总金额
	
	@XStreamAlias("IsTotalLoss")
	private String isTotalLoss;// 是否全损
	
	public String getClaimSequenceNo() {
		return claimSequenceNo;
	}

	
	public void setClaimSequenceNo(String claimSequenceNo) {
		this.claimSequenceNo = claimSequenceNo;
	}

	
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	
	public String getConfirmSequenceNo() {
		return confirmSequenceNo;
	}

	
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		this.confirmSequenceNo = confirmSequenceNo;
	}


	
	public BigDecimal getClaimAmount() {
		return claimAmount;
	}


	
	public void setClaimAmount(BigDecimal claimAmount) {
		this.claimAmount = claimAmount;
	}


	
	public String getClaimCloseTime() {
		return claimCloseTime;
	}


	
	public void setClaimCloseTime(String claimCloseTime) {
		this.claimCloseTime = claimCloseTime;
	}


	
	public String getIsInsured() {
		return isInsured;
	}


	
	public void setIsInsured(String isInsured) {
		this.isInsured = isInsured;
	}


	
	public String getClaimType() {
		return claimType;
	}


	
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}


	
	public String getPayCause() {
		return payCause;
	}


	
	public void setPayCause(String payCause) {
		this.payCause = payCause;
	}


	
	public String getRefuseCause() {
		return refuseCause;
	}


	
	public void setRefuseCause(String refuseCause) {
		this.refuseCause = refuseCause;
	}


	
	public String getAccidentType() {
		return accidentType;
	}


	
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}


	
	public String getIsRefuseCase() {
		return isRefuseCase;
	}


	
	public void setIsRefuseCase(String isRefuseCase) {
		this.isRefuseCase = isRefuseCase;
	}


	
	public BigDecimal getDirectClaimAmount() {
		return directClaimAmount;
	}


	
	public void setDirectClaimAmount(BigDecimal directClaimAmount) {
		this.directClaimAmount = directClaimAmount;
	}


	
	public String getIsTotalLoss() {
		return isTotalLoss;
	}


	
	public void setIsTotalLoss(String isTotalLoss) {
		this.isTotalLoss = isTotalLoss;
	}
	
	
	
}
