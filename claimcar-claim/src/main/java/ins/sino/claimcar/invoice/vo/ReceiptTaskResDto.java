package ins.sino.claimcar.invoice.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 发票登记后回写费用信息结果
 * @author ftonly
 *
 */
@XStreamAlias("Packet")
public class ReceiptTaskResDto implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("Head")
	private HeadRemoteRes head;

	public HeadRemoteRes getHead() {
		return head;
	}

	public void setHead(HeadRemoteRes head) {
		this.head = head;
	}
	

}
