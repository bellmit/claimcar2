package ins.sino.claimcar.genilex.comResVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FraudResponse")
public class FraudResponseVo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("ProductRequestId")
	private String productRequestId;//产品请求ID
	@XStreamAlias("TransactionId")
	private String transactionId;//交易记录ID(用于客户追踪)
	@XStreamAlias("ProductResult")
	private ProductResultVo productResultVo;//响应结果	
	public String getProductRequestId() {
		return productRequestId;
	}
	public void setProductRequestId(String productRequestId) {
		this.productRequestId = productRequestId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public ProductResultVo getProductResultVo() {
		return productResultVo;
	}
	public void setProductResultVo(ProductResultVo productResultVo) {
		this.productResultVo = productResultVo;
	}
	
	
						

}
