package ins.sino.claimcar.sunyardimage.vo.response;

import ins.sino.claimcar.sunyardimage.vo.common.SonNodeVo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("NODE")
public class ResSonNodeVo extends SonNodeVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamImplicit(itemFieldName="LEAF")//标注加在list上
	private List<String> leafs;

	public List<String> getLeafs() {
		return leafs;
	}

	public void setLeafs(List<String> leafs) {
		this.leafs = leafs;
	}

	

}
