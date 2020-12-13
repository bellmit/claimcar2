package ins.sino.claimcar.ciitc.vo.accident;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ImageInfo") 
public class ImageInfo implements Serializable{
	 private static final long serialVersionUID = 1L;

		@XStreamAlias("imageType")
		private String imageType;//影像类型
		@XStreamAlias("photoType")
		private String photoType;//照片类型
		@XStreamAlias("imageTime")
		private String imageTime;//拍摄时间
		@XStreamAlias("longitude")
		private String longitude;//纬度
		@XStreamAlias("latitude")
		private String latitude;//纬度
		@XStreamAlias("imageAddress")
		private String imageAddress;//影像定位地址
		@XStreamAlias("imageUrl")
		private String imageUrl;//影像下载地址
		public String getImageType() {
			return imageType;
		}
		public void setImageType(String imageType) {
			this.imageType = imageType;
		}
		public String getPhotoType() {
			return photoType;
		}
		public void setPhotoType(String photoType) {
			this.photoType = photoType;
		}
		public String getImageTime() {
			return imageTime;
		}
		public void setImageTime(String imageTime) {
			this.imageTime = imageTime;
		}
		public String getLongitude() {
			return longitude;
		}
		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}
		public String getLatitude() {
			return latitude;
		}
		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}
		public String getImageAddress() {
			return imageAddress;
		}
		public void setImageAddress(String imageAddress) {
			this.imageAddress = imageAddress;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		
		
}
