package ins.sino.claimcar.carplatform.vo;


public class PrpDReUploadConfigVoBase  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String requestType;
	private String requestName;
	private String servicerName;
	private String className;
	private String methodName;
	private String paramTypes;
	private String validStatus;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getRequestType() {
		return requestType;
	}
	
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public String getRequestName() {
		return requestName;
	}
	
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	
	public String getServicerName() {
		return servicerName;
	}
	
	public void setServicerName(String servicerName) {
		this.servicerName = servicerName;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getParamTypes() {
		return paramTypes;
	}
	
	public void setParamTypes(String paramTypes) {
		this.paramTypes = paramTypes;
	}
	
	public String getValidStatus() {
		return validStatus;
	}
	
	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}
	
	
	
	
}
