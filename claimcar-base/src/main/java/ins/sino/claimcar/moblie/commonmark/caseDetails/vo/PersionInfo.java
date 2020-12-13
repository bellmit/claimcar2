package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 人伤信息
 * @author j2eel
 *
 */
@XStreamAlias("PERSIONINFO")
public class PersionInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("TRACER")
	private String tracer;				//跟踪人员
	@XStreamAlias("TRACERIDENTIFYNO")
	private String tracerIdentifyNo;	//跟踪员身份证号
	@XStreamAlias("ISSMALLCASE")		
	private String isSmallCase;			//是否小额案件
	@XStreamAlias("ISDEROGATIONIS")
	private String isDerogationIs;		//是否减损
	
	public String getTracer() {
		return tracer;
	}
	public void setTracer(String tracer) {
		this.tracer = tracer;
	}
	public String getTracerIdentifyNo() {
		return tracerIdentifyNo;
	}
	public void setTracerIdentifyNo(String tracerIdentifyNo) {
		this.tracerIdentifyNo = tracerIdentifyNo;
	}
	public String getIsSmallCase() {
		return isSmallCase;
	}
	public void setIsSmallCase(String isSmallCase) {
		this.isSmallCase = isSmallCase;
	}
	public String getIsDerogationIs() {
		return isDerogationIs;
	}
	public void setIsDerogationIs(String isDerogationIs) {
		this.isDerogationIs = isDerogationIs;
	}

}
