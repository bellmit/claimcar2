package ins.sino.claimcar.base.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "PRPLKEYWORDSCFG")
public class PrpLKeyWordsCfg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 	关键字
	 */
	private String keyword;
	/**
	 * 	是否有效标识 1-有效 0-无效
	 */
	private String status;
	
	@Id
	@Column(name = "KEYWORD", nullable = false, length = 50)
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(name = "STATUS", nullable = false, length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
