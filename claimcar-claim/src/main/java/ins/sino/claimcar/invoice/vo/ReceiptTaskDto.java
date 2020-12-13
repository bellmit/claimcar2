package ins.sino.claimcar.invoice.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 发票认证过后回写费用信息
 * 
 */
@XStreamAlias("Packet")
public class ReceiptTaskDto implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAlias("Head")
	private HeadRemote head;
	@XStreamAlias("Body")
	private BodyReceiptTask body;

	public HeadRemote getHead() {
		return head;
	}

	public void setHead(HeadRemote head) {
		this.head = head;
	}

	public BodyReceiptTask getBody() {
		return body;
	}

	public void setBody(BodyReceiptTask body) {
		this.body = body;
	}
	

}
