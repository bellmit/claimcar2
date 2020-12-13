/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;



//单证信息明细列表(隶属于单证信息)单证信息 
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class SubrogationDocDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 单证名称 **/ 
	private String docName;
	
	
	
	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
	
}
