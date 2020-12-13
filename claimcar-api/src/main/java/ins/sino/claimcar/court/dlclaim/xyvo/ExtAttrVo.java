package ins.sino.claimcar.court.dlclaim.xyvo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;


@XStreamAlias("EXT_ATTR")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"value1"})
public class ExtAttrVo implements Serializable{
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute
	@XStreamAlias("ID")
	private String id;
	@XStreamAsAttribute
	@XStreamAlias("NAME")
	private String name;
	@XStreamAsAttribute
	@XStreamAlias("IS_SHOW")
	private String is_show;
	@XStreamAsAttribute
	@XStreamAlias("IS_KEY")
	private String is_key;
	@XStreamAsAttribute
	@XStreamAlias("IS_NULL")
	private String is_null;
	@XStreamAsAttribute
	@XStreamAlias("INPUT_TYPE")
	private String input_type;
	private String value1;
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
	public String getIs_show() {
		return is_show;
	}
	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}
	public String getIs_key() {
		return is_key;
	}
	public void setIs_key(String is_key) {
		this.is_key = is_key;
	}
	public String getIs_null() {
		return is_null;
	}
	public void setIs_null(String is_null) {
		this.is_null = is_null;
	}
	public String getInput_type() {
		return input_type;
	}
	public void setInput_type(String input_type) {
		this.input_type = input_type;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
    

}
