package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
@XStreamAlias("lossInfo")  // 物损
public class CaseDetaillossInfosVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("itemsName") 
	private String  itemsName;  //物损信息

	public String getItemsName() {
		return itemsName;
	}

	public void setItemsName(String itemsName) {
		this.itemsName = itemsName;
	}
	
	

}
