/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//定核损信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiSubrogationEstimateDataVo {
	@XmlElement(name = "AccidentType")
	private String accidentType;//保险事故分类；参见代码

	@XmlElement(name = "AccidentLiability", required = true)
	private String accidentLiability;//事故责任划分 参见代码

	@XmlElement(name = "OptionType", required = true)
	private String optionType;//事故处理方式；参见代码

	@XmlElement(name = "SubCertiType")
	private String subCertiType;//责任认定书类型：参见代码

	@XmlElement(name = "SubClaimFlag")
	private String subClaimFlag;//代位求偿案件索赔申请书标志；参见代码


	/** 车辆损失情况*/
	@XmlElement(name = "VehicleData")
	private List<BiEstimateCarDataVo> carDataList;
	
	/** 财产损失情况列表(隶属于定核损信息) */
	@XmlElement(name = "ProtectData")
	private List<BiEstimatePropDataVo> propDataList;
	
	/** 人员损失情况列表(隶属于定核损信息) */
	@XmlElement(name = "PersonData")
	private List<BiEstimatePersonDataVo> personDataList;
	/** 
	 * @return 返回 accidentType  保险事故分类；参见代码
	 */ 
	public String getAccidentType(){ 
	    return accidentType;
	}

	/** 
	 * @param accidentType 要设置的 保险事故分类；参见代码
	 */ 
	public void setAccidentType(String accidentType){ 
	    this.accidentType=accidentType;
	}

	/** 
	 * @return 返回 accidentLiability  事故责任划分 参见代码
	 */ 
	public String getAccidentLiability(){ 
	    return accidentLiability;
	}

	/** 
	 * @param accidentLiability 要设置的 事故责任划分 参见代码
	 */ 
	public void setAccidentLiability(String accidentLiability){ 
	    this.accidentLiability=accidentLiability;
	}

	/** 
	 * @return 返回 optionType  事故处理方式；参见代码
	 */ 
	public String getOptionType(){ 
	    return optionType;
	}

	/** 
	 * @param optionType 要设置的 事故处理方式；参见代码
	 */ 
	public void setOptionType(String optionType){ 
	    this.optionType=optionType;
	}

	/** 
	 * @return 返回 subCertiType  责任认定书类型：参见代码
	 */ 
	public String getSubCertiType(){ 
	    return subCertiType;
	}

	/** 
	 * @param subCertiType 要设置的 责任认定书类型：参见代码
	 */ 
	public void setSubCertiType(String subCertiType){ 
	    this.subCertiType=subCertiType;
	}

	/** 
	 * @return 返回 subClaimFlag  代位求偿案件索赔申请书标志；参见代码
	 */ 
	public String getSubClaimFlag(){ 
	    return subClaimFlag;
	}

	/** 
	 * @param subClaimFlag 要设置的 代位求偿案件索赔申请书标志；参见代码
	 */ 
	public void setSubClaimFlag(String subClaimFlag){ 
	    this.subClaimFlag=subClaimFlag;
	}

	public List<BiEstimateCarDataVo> getCarDataList() {
		return carDataList;
	}

	public void setCarDataList(List<BiEstimateCarDataVo> carDataList) {
		this.carDataList = carDataList;
	}

	public List<BiEstimatePropDataVo> getPropDataList() {
		return propDataList;
	}

	public void setPropDataList(List<BiEstimatePropDataVo> propDataList) {
		this.propDataList = propDataList;
	}

	public List<BiEstimatePersonDataVo> getPersonDataList() {
		return personDataList;
	}

	public void setPersonDataList(List<BiEstimatePersonDataVo> personDataList) {
		this.personDataList = personDataList;
	}



	


	
}
