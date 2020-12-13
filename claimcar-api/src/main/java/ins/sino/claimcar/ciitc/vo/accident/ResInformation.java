package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ResInformation") 
public class ResInformation implements Serializable{
	 private static final long serialVersionUID = 1L;

		@XStreamAlias("AcciMain")
		private AcciMain acciMain;
		@XStreamAlias("DutyJudge")
		private DutyJudge dutyJudge;
		@XStreamAlias("CarInfo")
		private List<CarInfo> carInfoList;
		@XStreamAlias("PartyInfo")
		private List<PartyInfo> partyInfoList;
		@XStreamAlias("ImageInfo")
		private List<ImageInfo> imageInfoList;
		@XStreamAlias("ReportInfo")
		private List<ReportInfo> reportInfoList;
		public AcciMain getAcciMain() {
			return acciMain;
		}
		public void setAcciMain(AcciMain acciMain) {
			this.acciMain = acciMain;
		}
		public DutyJudge getDutyJudge() {
			return dutyJudge;
		}
		public void setDutyJudge(DutyJudge dutyJudge) {
			this.dutyJudge = dutyJudge;
		}
		public List<CarInfo> getCarInfoList() {
			return carInfoList;
		}
		public void setCarInfoList(List<CarInfo> carInfoList) {
			this.carInfoList = carInfoList;
		}
		public List<PartyInfo> getPartyInfoList() {
			return partyInfoList;
		}
		public void setPartyInfoList(List<PartyInfo> partyInfoList) {
			this.partyInfoList = partyInfoList;
		}
		public List<ImageInfo> getImageInfoList() {
			return imageInfoList;
		}
		public void setImageInfoList(List<ImageInfo> imageInfoList) {
			this.imageInfoList = imageInfoList;
		}
		public List<ReportInfo> getReportInfoList() {
			return reportInfoList;
		}
		public void setReportInfoList(List<ReportInfo> reportInfoList) {
			this.reportInfoList = reportInfoList;
		}
		
	   
}
