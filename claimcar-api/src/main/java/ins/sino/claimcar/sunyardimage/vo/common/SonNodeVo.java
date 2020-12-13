package ins.sino.claimcar.sunyardimage.vo.common;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class SonNodeVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("ID")
	private String id;
	@XStreamAsAttribute
	@XStreamAlias("NAME")
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
