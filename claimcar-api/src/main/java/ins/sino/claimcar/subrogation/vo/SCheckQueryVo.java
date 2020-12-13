package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 *  案件互审查询页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SCheckQueryVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
    private String comCode;
	private String accountsNo;   //结算码
	
	private String checkStats;     ///互审状态
	
	private String checkOwnType;    //审核方类型

	private Date recoverDStart;//追偿起始时间起
	
	private Date RecoverDEnd;//追偿起始时间至
	
	private String checkOpinion;//互审意见

	public String getAccountsNo() {
		return accountsNo;
	}

	public void setAccountsNo(String accountsNo) {
		this.accountsNo = accountsNo;
	}

	public String getCheckStats() {
		return checkStats;
	}

	public void setCheckStats(String checkStats) {
		this.checkStats = checkStats;
	}

	public String getCheckOwnType() {
		return checkOwnType;
	}

	public void setCheckOwnType(String checkOwnType) {
		this.checkOwnType = checkOwnType;
	}

	public Date getRecoverDStart() {
		return recoverDStart;
	}

	public void setRecoverDStart(Date recoverDStart) {
		this.recoverDStart = recoverDStart;
	}

	public Date getRecoverDEnd() {
		return RecoverDEnd;
	}

	public void setRecoverDEnd(Date recoverDEnd) {
		RecoverDEnd = recoverDEnd;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}
	
	

}
