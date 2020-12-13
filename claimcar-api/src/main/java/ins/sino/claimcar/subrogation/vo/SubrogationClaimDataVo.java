package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 *  //立案信息
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SubrogationClaimDataVo implements Serializable{
		private static final long serialVersionUID = 1L;

		/** 立案号 **/ 
		private String registrationNo; 

		/** 立案时间 **/ 
		private Date registrationTime; 

		/** 估损金额 **/ 
		private Double estimateAmount;

		private String claimRegistrationNo;//立案号

		private Date claimRegistrationTime;//立案时间；格式：精确到分钟

		private Double estimatedLossAmount;//估损金额
		public String getRegistrationNo() {
			return registrationNo;
		}

		public void setRegistrationNo(String registrationNo) {
			this.registrationNo = registrationNo;
		}

		public Date getRegistrationTime() {
			return registrationTime;
		}

		public void setRegistrationTime(Date registrationTime) {
			this.registrationTime = registrationTime;
		}

		public Double getEstimateAmount() {
			return estimateAmount;
		}

		public void setEstimateAmount(Double estimateAmount) {
			this.estimateAmount = estimateAmount;
		}

		public String getClaimRegistrationNo() {
			return claimRegistrationNo;
		}

		public void setClaimRegistrationNo(String claimRegistrationNo) {
			this.claimRegistrationNo = claimRegistrationNo;
		}

		public Date getClaimRegistrationTime() {
			return claimRegistrationTime;
		}

		public void setClaimRegistrationTime(Date claimRegistrationTime) {
			this.claimRegistrationTime = claimRegistrationTime;
		}

		public Double getEstimatedLossAmount() {
			return estimatedLossAmount;
		}

		public void setEstimatedLossAmount(Double estimatedLossAmount) {
			this.estimatedLossAmount = estimatedLossAmount;
		}
		
}
