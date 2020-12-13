package ins.sino.claimcar.moblie.certify.vo;

import ins.platform.utils.xstreamconverters.SinosoftDateConverter;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("PRPLCERTIFYDIRECT")
public class CertifyDirect implements Serializable{
	/**  */
	private static final long serialVersionUID = -4623864062836013585L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号

    @XStreamAlias("LOSSITEMCODE")
    private String lossItemCode; //单证代码
	
    @XStreamAlias("LOSSITEMNAME")
    private String lossItemName; //单证名称
    
    @XStreamAlias("TYPECODE")
    private String typeCode; //单证分类代码
    
    @XStreamAlias("TYPENAME")
    private String typeName; //单证分类名称
    
    @XStreamAlias("CHECKNODE")
    private String checkNode; //勾选节点
    
    @XStreamAlias("CHECKUSER")
    private String checkUser; //勾选人员代码
    
    @XStreamAlias("CREATETIME")
    @XStreamConverter(value = SinosoftDateConverter.class, strings = {"yyyy-MM-dd HH:mm:ss"})
    private Date createTime; //上传时间

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    
    public String getLossItemCode() {
        return lossItemCode;
    }

    
    public void setLossItemCode(String lossItemCode) {
        this.lossItemCode = lossItemCode;
    }

    
    public String getLossItemName() {
        return lossItemName;
    }

    
    public void setLossItemName(String lossItemName) {
        this.lossItemName = lossItemName;
    }

    
    public String getTypeCode() {
        return typeCode;
    }

    
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    
    public String getTypeName() {
        return typeName;
    }

    
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    
    public String getCheckNode() {
        return checkNode;
    }

    
    public void setCheckNode(String checkNode) {
        this.checkNode = checkNode;
    }

    
    public String getCheckUser() {
        return checkUser;
    }

    
    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    
    public Date getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    
    
}
