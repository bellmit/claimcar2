package ins.sino.claimcar.ciitc.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Head") 
public class CiitcReqHeadVo implements Serializable{
	 private static final long serialVersionUID = 1L;

	    @XStreamAlias("requestType") 
	    private String  requestType;  //请求类型
	    
	    @XStreamAlias("userName")
	    private String userName;    //用户名
	    
	    @XStreamAlias("passWord")
	    private String passWord;   // 密码
	    
	    @XStreamAlias("institutionCode")
	    private String institutionCode;    //公司代码
	    
	    @XStreamAlias("acciAreaCode")
	    private String acciAreaCode;   // 事故地区代码

		public String getRequestType() {
			return requestType;
		}

		public void setRequestType(String requestType) {
			this.requestType = requestType;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassWord() {
			return passWord;
		}

		public void setPassWord(String passWord) {
			this.passWord = passWord;
		}

		public String getInstitutionCode() {
			return institutionCode;
		}

		public void setInstitutionCode(String institutionCode) {
			this.institutionCode = institutionCode;
		}

		public String getAcciAreaCode() {
			return acciAreaCode;
		}

		public void setAcciAreaCode(String acciAreaCode) {
			this.acciAreaCode = acciAreaCode;
		}
	    
	  
}
