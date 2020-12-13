package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PHOTOINFO")
public class PhotoInfo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("PHOTOTYPE")
	private String photoType;	//单证类型
	
	@XStreamAlias("PHOTHNUM")
	private String photoNum;	//2	单证数量

	public String getPhotoType() {
		return photoType;
	}

	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}

	public String getPhotoNum() {
		return photoNum;
	}

	public void setPhotoNum(String photoNum) {
		this.photoNum = photoNum;
	}
	
}
