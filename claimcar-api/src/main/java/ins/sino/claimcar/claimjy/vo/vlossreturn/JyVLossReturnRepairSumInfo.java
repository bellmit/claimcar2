package ins.sino.claimcar.claimjy.vo.vlossreturn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyVLossReturnRepairSumInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "WorkTypeCode")
	private String workTypeCode;

	@XmlElement(name = "ItemCount")
	private String itemCount;

	@XmlElement(name = "ApprRepairSum")
	private String apprRepairSum;

	public String getWorkTypeCode() {
		return workTypeCode;
	}

	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getApprRepairSum() {
		return apprRepairSum;
	}

	public void setApprRepairSum(String apprRepairSum) {
		this.apprRepairSum = apprRepairSum;
	}
	
	
	

}
