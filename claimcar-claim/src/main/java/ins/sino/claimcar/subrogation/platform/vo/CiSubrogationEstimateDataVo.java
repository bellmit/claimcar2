/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

//定核损信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiSubrogationEstimateDataVo {
	/** 保险事故分类 **/ 
	@XmlElement(name="ACCIDENT_TYPE")
	private String accidentType; 

	/** 事故责任划分代码 **/ 
	@XmlElement(name="ACCIDENT_LIABILITY", required = true)
	private String accidentLiability; 

	/** 事故处理方式代码 **/ 
	@XmlElement(name="MANAGE_TYPE", required = true)
	private String manageType;
	
	/** 车辆损失情况*/
	@XmlElementWrapper(name = "CAR_LIST")
	@XmlElement(name = "CAR_DATA")
	private List<CiEstimateCarDataVo> carDataList;
	
	/** 财产损失情况列表(隶属于定核损信息) */
	@XmlElementWrapper(name = "PROTECT_LIST")
	@XmlElement(name = "PROTECT_DATA")
	private List<CiEstimatePropDataVo> propDataList;
	
	/** 人员损失情况列表(隶属于定核损信息) */
	@XmlElementWrapper(name = "PERSON_LIST")
	@XmlElement(name = "PERSON_DATA")
	private List<CiEstimatePersonDataVo> personDataList;

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

	
	public List<CiEstimateCarDataVo> getCarDataList() {
		return carDataList;
	}

	
	public void setCarDataList(List<CiEstimateCarDataVo> carDataList) {
		this.carDataList = carDataList;
	}

	
	public List<CiEstimatePropDataVo> getPropDataList() {
		return propDataList;
	}

	
	public void setPropDataList(List<CiEstimatePropDataVo> propDataList) {
		this.propDataList = propDataList;
	}

	
	public List<CiEstimatePersonDataVo> getPersonDataList() {
		return personDataList;
	}

	
	public void setPersonDataList(List<CiEstimatePersonDataVo> personDataList) {
		this.personDataList = personDataList;
	} 

	
	 
	
}
