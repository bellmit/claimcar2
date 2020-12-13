package ins.sino.claimcar.vat.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("root")
public class ImageResVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("RESPONSE_CODE")
	private String RESPONSE_CODE;
	@XStreamAlias("RESPONSE_MSG")
	private String RESPONSE_MSG;
	@XStreamAlias("PAGES")
	private List<ImagePageVo> imagePageVos;
	@XStreamAlias("META_DATA")
	private ImageMetaDataVo imageMetaDataVo;
	public String getRESPONSE_CODE() {
		return RESPONSE_CODE;
	}
	public void setRESPONSE_CODE(String rESPONSE_CODE) {
		RESPONSE_CODE = rESPONSE_CODE;
	}
	public String getRESPONSE_MSG() {
		return RESPONSE_MSG;
	}
	public void setRESPONSE_MSG(String rESPONSE_MSG) {
		RESPONSE_MSG = rESPONSE_MSG;
	}
	public List<ImagePageVo> getImagePageVos() {
		return imagePageVos;
	}
	public void setImagePageVos(List<ImagePageVo> imagePageVos) {
		this.imagePageVos = imagePageVos;
	}
	public ImageMetaDataVo getImageMetaDataVo() {
		return imageMetaDataVo;
	}
	public void setImageMetaDataVo(ImageMetaDataVo imageMetaDataVo) {
		this.imageMetaDataVo = imageMetaDataVo;
	}
}
