package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 *  重开赔案信息列表返回页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SubrogationClaimReopenDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	/** 重开原因 **/ 
	private String reopenCause; 

	/** 重开时间 格式:YYYYMMDDHHMM **/ 
	private Date reopenDate;

	public String getReopenCause() {
		return reopenCause;
	}

	public void setReopenCause(String reopenCause) {
		this.reopenCause = reopenCause;
	}

	public Date getReopenDate() {
		return reopenDate;
	}

	public void setReopenDate(Date reopenDate) {
		this.reopenDate = reopenDate;
	}

	
	

}
