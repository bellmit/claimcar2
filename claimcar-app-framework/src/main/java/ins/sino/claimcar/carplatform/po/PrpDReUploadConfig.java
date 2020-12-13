package ins.sino.claimcar.carplatform.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_PRPDREUPLOADCONFIG", allocationSize = 10)
@Table(name = "PRPDREUPLOADCONFIG")
public class PrpDReUploadConfig  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String requestType;
	private String requestName;
	private String servicerName;
	private String className;
	private String methodName;
	private String paramTypes;
	private String validStatus;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=12, scale=0)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "REQUESTTYPE")
	public String getRequestType() {
		return requestType;
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	@Column(name = "REQUESTNAME")
	public String getRequestName() {
		return requestName;
	}
	
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	@Column(name = "SERVICERNAME")
	public String getServicerName() {
		return servicerName;
	}
	
	public void setServicerName(String servicerName) {
		this.servicerName = servicerName;
	}
	@Column(name = "CLASSNAME")
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	@Column(name = "METHODNAME")
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	@Column(name = "PARAMTYPES")
	public String getParamTypes() {
		return paramTypes;
	}
	
	public void setParamTypes(String paramTypes) {
		this.paramTypes = paramTypes;
	}
	@Column(name = "VALIDSTATUS")
	public String getValidStatus() {
		return validStatus;
	}
	
	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}
	
	
	
	
}
