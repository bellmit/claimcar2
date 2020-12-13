package ins.sino.claimcar.sunyardimage.vo.request;

import ins.sino.claimcar.sunyardimage.vo.common.SonNodeVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("NODE")
public class ReqSonNodeVo extends SonNodeVo implements Serializable{

	@XStreamAsAttribute
	@XStreamAlias("RIGHT")
	private String right;
	@XStreamAsAttribute
	@XStreamAlias("RESEIZE")
	private String reseize;
	@XStreamAsAttribute
	@XStreamAlias("CHILD_FLAG")
	private String childFlag;
	@XStreamAsAttribute
	@XStreamAlias("BARCODE")
	private String barCode;
	@XStreamAsAttribute
	@XStreamAlias("MAXPAGES")
	private String maxpages;
	@XStreamAsAttribute
	@XStreamAlias("MINPAGES")
	private String minpages;
	@XStreamImplicit(itemFieldName = "NODE")
	private List<ReqSonNodeVo> sonNodes;
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	public String getReseize() {
		return reseize;
	}
	public void setReseize(String reseize) {
		this.reseize = reseize;
	}
	public String getChildFlag() {
		return childFlag;
	}
	public void setChildFlag(String childFlag) {
		this.childFlag = childFlag;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getMaxpages() {
		return maxpages;
	}
	public void setMaxpages(String maxpages) {
		this.maxpages = maxpages;
	}
	public String getMinpages() {
		return minpages;
	}
	public void setMinpages(String minpages) {
		this.minpages = minpages;
	}
	public List<ReqSonNodeVo> getSonNodes() {
		return sonNodes;
	}
	public void setSonNodes(List<ReqSonNodeVo> sonNodes) {
		this.sonNodes = sonNodes;
	}
	
	

}
