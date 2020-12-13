/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:54:47
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiKsErrorProVo {

	/** 跨省查询失败的地区代码 **/
	@XmlElement(name="ErrorAreaCode")
	private String errorAreaCode;

	
	public String getErrorAreaCode() {
		return errorAreaCode;
	}

	
	public void setErrorAreaCode(String errorAreaCode) {
		this.errorAreaCode = errorAreaCode;
	}
	
	
}
