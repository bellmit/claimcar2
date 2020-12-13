package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Item")
public class ItemCover implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAlias("ItemCover")
	private String itemCover;//险别
	@XStreamAlias("ItemCoverCode")
	private String itemCoverCode;//险别代码
	
	public String getItemCover() {
		return itemCover;
	}
	public void setItemCover(String itemCover) {
		this.itemCover = itemCover;
	}
	public String getItemCoverCode() {
		return itemCoverCode;
	}
	public void setItemCoverCode(String itemCoverCode) {
		this.itemCoverCode = itemCoverCode;
	}
}
