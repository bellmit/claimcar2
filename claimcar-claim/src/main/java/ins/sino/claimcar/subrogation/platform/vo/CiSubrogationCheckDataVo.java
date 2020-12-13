/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

//查勘信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationCheckDataVo {

	/** 保险事故分类 **/ 
	@XmlElement(name="ACCIDENT_TYPE")
	private String accidentType; 

	/** 事故责任划分代码 **/ 
	@XmlElement(name="ACCIDENT_LIABILITY", required = true)
	private String accidentLiability; 

	/** 事故处理方式代码 **/ 
	@XmlElement(name="MANAGE_TYPE")
	private String manageType;
	
	/**车辆损失情况列表(隶属于查勘信息)*/
	@XmlElementWrapper(name = "CHECK_CAR_LIST")
	@XmlElement(name = "CHECK_CAR_DATA")
	private List<CiCheckCarDataVo> checkCarDataList;
	/**财产损失情况列表(隶属于查勘信息)*/
	@XmlElementWrapper(name = "CHECK_PROTECT_LIST")
	@XmlElement(name = "CHECK_PROTECT_DATA")
	private List<CiCheckPropDataVo> checkPropDataList;

	/**人员损失情况列表(隶属于查勘信息)*/
	@XmlElementWrapper(name = "CHECK_PERSON_LIST")
	@XmlElement(name = "CHECK_PERSON_DATA")
	private List<CiCheckPersonDataVo> checkPersonDataList;

	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public String getAccidentLiability() {
		return accidentLiability;
	}

	public void setAccidentLiability(String accidentLiability) {
		this.accidentLiability = accidentLiability;
	}

	public String getManageType() {
		return manageType;
	}

	public void setManageType(String manageType) {
		this.manageType = manageType;
	}

	
	public List<CiCheckCarDataVo> getCheckCarDataList() {
		return checkCarDataList;
	}

	
	public void setCheckCarDataList(List<CiCheckCarDataVo> checkCarDataList) {
		this.checkCarDataList = checkCarDataList;
	}

	
	public List<CiCheckPropDataVo> getCheckPropDataList() {
		return checkPropDataList;
	}

	
	public void setCheckPropDataList(List<CiCheckPropDataVo> checkPropDataList) {
		this.checkPropDataList = checkPropDataList;
	}

	
	public List<CiCheckPersonDataVo> getCheckPersonDataList() {
		return checkPersonDataList;
	}

	
	public void setCheckPersonDataList(List<CiCheckPersonDataVo> checkPersonDataList) {
		this.checkPersonDataList = checkPersonDataList;
	} 

	


}
