package ins.platform.shiro.source;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TB_RESOURCE")
public class Resource implements Serializable {
	// 主键id
	@Id
	private String id;
	// action url
	private String value;
	// shiro permission;
	private String permission;

	public Resource(String id, String value, String permission) {
		super();
		this.id = id;
		this.value = value;
		this.permission = permission;
	}

	// ------------------Getter/Setter---------------------//
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
