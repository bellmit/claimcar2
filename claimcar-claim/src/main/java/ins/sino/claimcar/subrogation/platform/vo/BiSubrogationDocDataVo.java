/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

//单证信息明细列表(隶属于单证信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiSubrogationDocDataVo {
	
	@XmlElement(name = "SubCertiType")
	private String subCertiType;//责任认定书类型：参见代码

	@XmlElement(name = "SubClaimFlag")
	private String subClaimFlag;//代位求偿案件索赔申请书标志；参见代码

	/**单证明细列表(隶属于单证信息)*/
	@XmlElement(name = "DocDetailData")
	private List<BiSubrogationDocDetailDataVo> docDetailDataList;
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

	public List<BiSubrogationDocDetailDataVo> getDocDetailDataList() {
		return docDetailDataList;
	}

	public void setDocDetailDataList(
			List<BiSubrogationDocDetailDataVo> docDetailDataList) {
		this.docDetailDataList = docDetailDataList;
	}




	
	
	
	
	
}
