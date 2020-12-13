package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 保单查询 Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiClaimResBodyVo {

	@XmlElement(name = "BasePart")
	private BiClaimDataVo biClaimDataVos;

	public BiClaimDataVo getBiClaimDataVos() {
		return biClaimDataVos;
	}

	public void setBiClaimDataVos(BiClaimDataVo biClaimDataVos) {
		this.biClaimDataVos = biClaimDataVos;
	}


	
	
	
}
