package ins.sino.claimcar.moblie.lossPerson.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 跟踪记录信息
 * @author j2eel
 *
 */
@XStreamAlias("TRACEINFO")
public class TraceInfoVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	private String id; 
	@XStreamAlias("TRACETYPE")
	private String traceType;     //跟踪形式
	@XStreamAlias("CONTENT")
	private String content;		//内容说明
	@XStreamAlias("ISCLAIM")
	private String isClaim;		//是否发起索赔
	
	@XStreamAlias("FEELIST")
	private List<FeeInfo> feeInfo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTraceType() {
		return traceType;
	}

	public void setTraceType(String traceType) {
		this.traceType = traceType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsClaim() {
		return isClaim;
	}

	public void setIsClaim(String isClaim) {
		this.isClaim = isClaim;
	}

	public List<FeeInfo> getFeeInfo() {
		return feeInfo;
	}

	public void setFeeInfo(List<FeeInfo> feeInfo) {
		this.feeInfo = feeInfo;
	}
	
	

}
