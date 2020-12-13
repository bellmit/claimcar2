package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ReportInfo") 
public class ReportInfo implements Serializable{
	 private static final long serialVersionUID = 1L;

		@XStreamAlias("reportPhone")
		private String reportPhone;//报案人手机号
		@XStreamAlias("reportName")
		private String reportName;//报案人姓名
		@XStreamAlias("reportTime")
		private String reportTime;//报案时间
		@XStreamAlias("licenseNo")
		private String licenseNo;//车辆号牌号码
		public String getReportPhone() {
			return reportPhone;
		}
		public void setReportPhone(String reportPhone) {
			this.reportPhone = reportPhone;
		}
		public String getReportName() {
			return reportName;
		}
		public void setReportName(String reportName) {
			this.reportName = reportName;
		}
		public String getReportTime() {
			return reportTime;
		}
		public void setReportTime(String reportTime) {
			this.reportTime = reportTime;
		}
		public String getLicenseNo() {
			return licenseNo;
		}
		public void setLicenseNo(String licenseNo) {
			this.licenseNo = licenseNo;
		}
		
		
}
