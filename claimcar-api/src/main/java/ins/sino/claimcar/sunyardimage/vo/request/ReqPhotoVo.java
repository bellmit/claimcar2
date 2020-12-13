package ins.sino.claimcar.sunyardimage.vo.request;

import java.io.Serializable;

public class ReqPhotoVo implements Serializable{
 private String carCaseNo;//车牌号
 private String imageUrl;//图片url
 private String imageName;//图片名字
public String getCarCaseNo() {
	return carCaseNo;
}
public void setCarCaseNo(String carCaseNo) {
	this.carCaseNo = carCaseNo;
}
public String getImageUrl() {
	return imageUrl;
}
public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
}
public String getImageName() {
	return imageName;
}
public void setImageName(String imageName) {
	this.imageName = imageName;
}
 
 
}
