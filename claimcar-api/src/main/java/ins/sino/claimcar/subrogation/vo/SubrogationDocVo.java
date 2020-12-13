/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;


//单证信息 
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class SubrogationDocVo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String subCertiType;//责任认定书类型：参见代码

	private String subClaimFlag;//代位求偿案件索赔申请书标志；参见代码

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

	
}
