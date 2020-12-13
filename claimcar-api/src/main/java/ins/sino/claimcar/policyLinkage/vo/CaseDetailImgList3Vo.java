package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */
@XStreamAlias("imgList3")  //环境照片
public class CaseDetailImgList3Vo implements Serializable {  

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("imgId")
	private String imgId;
	
	@XStreamAlias("orders")
	private String orders;  // 图片顺序
	
	@XStreamAlias("picUrl")
	private String picUrl; //图片地址
	
	@XStreamAlias("smallPicUrl")
	private String smallPicUrl; //缩略图地址
	
	@XStreamAlias("tags")
	private String tags;  // 照片分类 人车合影(1),架号钢印照片(2),环境照片(3),碰撞照片(4),人伤物损照片(5),证件照片(6),交强险标示(7),定责书协议书(8
	
	@XStreamAlias("type")
	private String type;  //照片提示语分类     图片(1),  普通文件(2),
	
	@XStreamAlias("watermarkingUrl")
	private String watermarkingUrl;   //带水印图片

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getSmallPicUrl() {
		return smallPicUrl;
	}

	public void setSmallPicUrl(String smallPicUrl) {
		this.smallPicUrl = smallPicUrl;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWatermarkingUrl() {
		return watermarkingUrl;
	}

	public void setWatermarkingUrl(String watermarkingUrl) {
		this.watermarkingUrl = watermarkingUrl;
	}
	
	

}
