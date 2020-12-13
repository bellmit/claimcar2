package ins.sino.claimcar.genilex.comResVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ProductResponse")
public class ProductResponseVo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("FraudResponse")
	private FraudResponseVo fraudResponseVo;//反欺诈评分列表

	public FraudResponseVo getFraudResponseVo() {
		return fraudResponseVo;
	}

	public void setFraudResponseVo(FraudResponseVo fraudResponseVo) {
		this.fraudResponseVo = fraudResponseVo;
	}
	

}
