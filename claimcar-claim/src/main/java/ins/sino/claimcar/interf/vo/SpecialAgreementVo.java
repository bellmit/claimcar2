package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*保单特别约定*/
@XStreamAlias("SpecialAgreement")
public class SpecialAgreementVo {
	/*序号*/
	@XStreamAlias("Order")
	private String order;
	/*约定条文*/
	@XStreamAlias("Content")
	private String content;
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
