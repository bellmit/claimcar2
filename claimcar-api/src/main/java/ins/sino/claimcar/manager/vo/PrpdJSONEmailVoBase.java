package ins.sino.claimcar.manager.vo;

import java.io.Serializable;
import java.util.Date;

public class PrpdJSONEmailVoBase implements Serializable {
    /**应用编码*/
    private String appCode;
    /**应用密码*/
    private String appSecret;
    /**是否即时消息：1-是，0-否*/
    private String isIm;
    /**发送（定时）时间*/
    private String atTime;
    /**有效存活时间*/
    private Date validTime;
    /**收件人*/
    private String emailTo;
    /**抄送人*/
    private String emailCc;
    /**密送人*/
    private String emailBcc;
    /**发件人昵称*/
    private String emailNickName;
    /**邮件主题*/
    private String subject;
    /**消息内容*/
    private String content;
    /**业务类型*/
    private String bizType;
    /**业务单号*/
    private String bizNo;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getIsIm() {
        return isIm;
    }

    public void setIsIm(String isIm) {
        this.isIm = isIm;
    }

    public String getAtTime() {
        return atTime;
    }

    public void setAtTime(String atTime) {
        this.atTime = atTime;
    }

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public void setEmailCc(String emailCc) {
        this.emailCc = emailCc;
    }

    public String getEmailBcc() {
        return emailBcc;
    }

    public void setEmailBcc(String emailBcc) {
        this.emailBcc = emailBcc;
    }

    public String getEmailNickName() {
        return emailNickName;
    }

    public void setEmailNickName(String emailNickName) {
        this.emailNickName = emailNickName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }
}
