package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("META_DATA")
public class ImageMetaDataVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("BATCH")
	private ImageBatchVo imageBatchVo;
	
	public ImageBatchVo getImageBatchVo() {
		return imageBatchVo;
	}
	public void setImageBatchVo(ImageBatchVo imageBatchVo) {
		this.imageBatchVo = imageBatchVo;
	}
	
	

}
