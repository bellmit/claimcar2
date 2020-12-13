package ins.sino.claimcar.invoice.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Head")
public class HeadRemote implements java.io.Serializable{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		/** 请求代码 **/
		@XStreamAlias("RequestType")
		private String requestType;
		/** 请求用户 **/
		@XStreamAlias("UserCode")
		private String userCode;
		/** 密码 **/
		@XStreamAlias("Password")
		private String password;
		
		public String getRequestType() {
			return requestType;
		}
		public void setRequestType(String requestType) {
			this.requestType = requestType;
		}
		public String getUserCode() {
			return userCode;
		}
		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
}
