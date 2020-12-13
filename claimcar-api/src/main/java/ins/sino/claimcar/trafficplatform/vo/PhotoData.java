package ins.sino.claimcar.trafficplatform.vo;

public class PhotoData implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String photoType;//照片类型
	private String photoName;//照片名称
	private String photoUrl;//照片URL
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhotoType() {
		return photoType;
	}
	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}


}
