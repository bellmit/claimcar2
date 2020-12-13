package ins.sino.claimcar.base.claimyj.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PARTOFFER")
public class PartOffersVo implements Serializable{
	@XStreamAlias("PARTENQUIRYID")
	private String partEnquiryId;
	@XStreamAlias("OFFERLIST")
	private List<OfferVo> offerList;
	public String getPartEnquiryId() {
		return partEnquiryId;
	}
	public void setPartEnquiryId(String partEnquiryId) {
		this.partEnquiryId = partEnquiryId;
	}
	public List<OfferVo> getOfferList() {
		return offerList;
	}
	public void setOfferList(List<OfferVo> offerList) {
		this.offerList = offerList;
	}
	

   
}
