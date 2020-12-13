package ins.sino.claimcar.moblie.caselist.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PRINTINFO")
public class PrintInfo implements Serializable{
	@XStreamAlias("IFOBJECT")
	private String ifObject="";  //类别---标的/三者
	
	@XStreamAlias("LICENSENO")
	private String licenseNo;  //车牌号
	
	@XStreamAlias("ISPRINT")
	private String isPrint="0";    //是否允许打印定损单

	public String getIfObject() {
		return ifObject;
	}

	public void setIfObject(String ifObject) {
		this.ifObject = ifObject;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}
	
	
	
}
