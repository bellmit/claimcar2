package ins.sino.claimcar.subrogation.vo;



import java.io.Serializable;
import java.util.List;

/**
 *  查勘信息
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class SubrogationCheckDataVos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 保险事故分类 **/ 
	private String accidentType; 

	/** 事故责任划分代码 **/ 
	private String accidentLiability; 

	/** 事故处理方式代码 **/ 
	private String manageType;
	
	private String subCertiType;//责任认定书类型：参见代码

	private String subClaimFlag;//代位求偿案件索赔申请书标志；参见代码
	
	/**车辆损失情况列表(隶属于查勘信息)*/
	private List<CheckCarDataVo> checkCarDataList;
	/**财产损失情况列表(隶属于查勘信息)*/
	private List<CheckPropDataVo> checkPropDataList;

	/**人员损失情况列表(隶属于查勘信息)*/
	private List<CheckPersonDataVo> checkPersonDataList;
	
	private String optionType;//事故处理方式；参见代码

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

	public List<CheckCarDataVo> getCheckCarDataList() {
		return checkCarDataList;
	}

	public void setCheckCarDataList(List<CheckCarDataVo> checkCarDataList) {
		this.checkCarDataList = checkCarDataList;
	}

	public List<CheckPropDataVo> getCheckPropDataList() {
		return checkPropDataList;
	}

	public void setCheckPropDataList(List<CheckPropDataVo> checkPropDataList) {
		this.checkPropDataList = checkPropDataList;
	}

	public List<CheckPersonDataVo> getCheckPersonDataList() {
		return checkPersonDataList;
	}

	public void setCheckPersonDataList(List<CheckPersonDataVo> checkPersonDataList) {
		this.checkPersonDataList = checkPersonDataList;
	}

	public String getSubCertiType() {
		return subCertiType;
	}

	public void setSubCertiType(String subCertiType) {
		this.subCertiType = subCertiType;
	}

	public String getSubClaimFlag() {
		return subClaimFlag;
	}

	public void setSubClaimFlag(String subClaimFlag) {
		this.subClaimFlag = subClaimFlag;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	
	
	

}
