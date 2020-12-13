package ins.sino.claimcar.sunyardimage.vo.common;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ImageBaseRootVo implements Serializable{
  @XStreamAlias("BASE_DATA")
  private BaseDataVo baseDataVo;
  
public BaseDataVo getBaseDataVo() {
	return baseDataVo;
}

public void setBaseDataVo(BaseDataVo baseDataVo) {
	this.baseDataVo = baseDataVo;
}

  
}
