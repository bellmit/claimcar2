package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("EXT_ATTR")
public class ExtAttrSonVo implements Serializable{

	private static final long serialVersionUID = 1L;
	@XStreamAlias("ID")
	@XStreamAsAttribute
	private String id;
	@XStreamAlias("NAME")
	@XStreamAsAttribute
	private String name;
	@XStreamAlias("VALUE")
	@XStreamAsAttribute
	private String value;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
