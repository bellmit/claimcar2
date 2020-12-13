package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 
 * <pre>案件备注（快赔请求理赔）</pre>
 * @author ★zhujunde
 */
@XStreamAlias("REMARK")
public class RemarkInfo  implements Serializable{
	
	/**  */
    private static final long serialVersionUID = 1L;
    
    
    /**
	 * 报案号
	 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	/**
	 * 备注类型
	 */
	@XStreamAlias("REMARKTYPE")
	private String remarkType;
	
	/**
     * 操作员姓名
     */
    @XStreamAlias("HANDLERNAME")
    private String handlerName;
    
    /**
     * 留言节点
     */
    @XStreamAlias("MESSAGENODE")
    private String messageNode;
    
    /**
     * 填写时间
     */
    @XStreamAlias("INPUTDATE")
    private String inputDate;
    
    /**
     * 留言内容
     */
    @XStreamAlias("MESSAGEDESC")
    private String messageDesc;

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }


    
    public String getRemarkType() {
        return remarkType;
    }


    
    public void setRemarkType(String remarkType) {
        this.remarkType = remarkType;
    }


    
    public String getHandlerName() {
        return handlerName;
    }


    
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }


    
    public String getMessageNode() {
        return messageNode;
    }


    
    public void setMessageNode(String messageNode) {
        this.messageNode = messageNode;
    }


    
    public String getInputDate() {
        return inputDate;
    }


    
    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }


    
    public String getMessageDesc() {
        return messageDesc;
    }


    
    public void setMessageDesc(String messageDesc) {
        this.messageDesc = messageDesc;
    }

    
  
}
