package ins.sino.claimcar.check.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ImagesCheckReqVo {

	private static final long serialVersionUID = 1L;
	

	
    @XStreamAlias("username")
    private String userName; //用户名（注：页面系统用户）
    
    @XStreamAlias("password")
    private String passWord; //密码
    
    @XStreamAlias("claimSequenceNo")
    private String claimSequenceNo; //理赔编号
    
    @XStreamAlias("claimPeriod")
    private String claimPeriod; //理赔阶段
    
    
    @XStreamAlias("comparePicURL")
    private String comparePicURL; //图像对比调用地址


    
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


    
    public String getClaimSequenceNo() {
        return claimSequenceNo;
    }


    
    public void setClaimSequenceNo(String claimSequenceNo) {
        this.claimSequenceNo = claimSequenceNo;
    }


    
    public String getClaimPeriod() {
        return claimPeriod;
    }


    
    public void setClaimPeriod(String claimPeriod) {
        this.claimPeriod = claimPeriod;
    }


    
    public String getComparePicURL() {
        return comparePicURL;
    }


    
    public void setComparePicURL(String comparePicURL) {
        this.comparePicURL = comparePicURL;
    }
    
  
}
