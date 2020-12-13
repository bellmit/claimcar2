package ins.sino.claimcar.moblie.msgNotified.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HEAD")
public class SendMsgToMobileHead implements Serializable {

    /**  */
    private static final long serialVersionUID = 3644263531118557352L;

    @XStreamAlias("REQUESTTYPE")
    private String requestType;    //请求类型
    
    @XStreamAlias("USER")
    private String user;    // 用户名
    
    @XStreamAlias("PASSWORD")
    private String passWorld;   // 密码

    
    public String getRequestType() {
        return requestType;
    }

    
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    
    public String getUser() {
        return user;
    }

    
    public void setUser(String user) {
        this.user = user;
    }

    
    public String getPassWorld() {
        return passWorld;
    }

    
    public void setPassWorld(String passWorld) {
        this.passWorld = passWorld;
    }
    

}
