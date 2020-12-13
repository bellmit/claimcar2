package ins.sino.claimcar.check.vo;


import java.io.File;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ImagesUploadReqVo {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("url")
	private String url; //图片上传对接接口地址
	
    @XStreamAlias("username")
    private String userName; //用户名（注：页面系统用户）
    
    @XStreamAlias("password")
    private String passWord; //密码
    
    @XStreamAlias("requestType")
    private String requestType; //接口代码，固定值：C04
    
    @XStreamAlias("format")
    private String format; //返回数据格式，固定值：JSON
    
    @XStreamAlias("v")
    private String v; //API协议版本，固定值：1.0
    
    @XStreamAlias("claimSequenceNo")
    private String claimSequenceNo; //理赔编号
    
    @XStreamAlias("vehicleProperty")
    private String vehicleProperty; //车辆属性
    
    
    @XStreamAlias("licensePlateNo")
    private String licensePlateNo; //车辆号牌号码
    
    @XStreamAlias("licensePlateType")
    private String licensePlateType; //号牌种类代码
    
    @XStreamAlias("vin")
    private String vin; //车架号
    
    @XStreamAlias("images")
    private List<File[]> images; //车辆图片信息，可以传送多张

    
    public String getUrl() {
        return url;
    }

    
    public void setUrl(String url) {
        this.url = url;
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

    
    public String getRequestType() {
        return requestType;
    }

    
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    
    public String getFormat() {
        return format;
    }

    
    public void setFormat(String format) {
        this.format = format;
    }

    
    public String getV() {
        return v;
    }

    
    public void setV(String v) {
        this.v = v;
    }

    
    public String getClaimSequenceNo() {
        return claimSequenceNo;
    }

    
    public void setClaimSequenceNo(String claimSequenceNo) {
        this.claimSequenceNo = claimSequenceNo;
    }

    
    public String getVehicleProperty() {
        return vehicleProperty;
    }

    
    public void setVehicleProperty(String vehicleProperty) {
        this.vehicleProperty = vehicleProperty;
    }

    
    public String getLicensePlateNo() {
        return licensePlateNo;
    }

    
    public void setLicensePlateNo(String licensePlateNo) {
        this.licensePlateNo = licensePlateNo;
    }

    
    public String getLicensePlateType() {
        return licensePlateType;
    }

    
    public void setLicensePlateType(String licensePlateType) {
        this.licensePlateType = licensePlateType;
    }

    
    public String getVin() {
        return vin;
    }

    
    public void setVin(String vin) {
        this.vin = vin;
    }

    
    public List<File[]> getImages() {
        return images;
    }

    
    public void setImages(List<File[]> images) {
        this.images = images;
    }
 
    
}
