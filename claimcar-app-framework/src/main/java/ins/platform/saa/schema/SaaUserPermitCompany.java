/******************************************************************************
* CREATETIME : 2016年6月29日 上午10:09:36
******************************************************************************/
package ins.platform.saa.schema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月29日
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "V_SAA_UserPermitCompany")
public class SaaUserPermitCompany implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String userCode;
	private String comCode;

	@Id
	@Column(name = "ID", nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(name = "USERCODE", nullable = false, length = 64)
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(name = "COMCODE", nullable = false, length = 64)
	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}



}
