package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ExtraBusinessInfo")
public class ExtraBusinessInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("IsLawsuit")
	private String isLawsuit;//是否诉讼案件
	@XStreamAlias("SelfCompensation")
	private String selfCompensation;//是否互碰自赔
	public String getIsLawsuit() {
		return isLawsuit;
	}
	public void setIsLawsuit(String isLawsuit) {
		this.isLawsuit = isLawsuit;
	}
	public String getSelfCompensation() {
		return selfCompensation;
	}
	public void setSelfCompensation(String selfCompensation) {
		this.selfCompensation = selfCompensation;
	}
}
