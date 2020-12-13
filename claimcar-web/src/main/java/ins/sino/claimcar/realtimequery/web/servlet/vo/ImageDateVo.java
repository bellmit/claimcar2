package ins.sino.claimcar.realtimequery.web.servlet.vo;

import java.io.Serializable;

public class ImageDateVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pcturesCount;
	private String pictures;
	private String picturesSize;
	public String getPcturesCount() {
		return pcturesCount;
	}
	public void setPcturesCount(String pcturesCount) {
		this.pcturesCount = pcturesCount;
	}
	public String getPictures() {
		return pictures;
	}
	public void setPictures(String pictures) {
		this.pictures = pictures;
	}
	public String getPicturesSize() {
		return picturesSize;
	}
	public void setPicturesSize(String picturesSize) {
		this.picturesSize = picturesSize;
	}
	
	
	
}
