package ins.platform.common.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import com.sinosoft.insaml.apiclient.FxqAPIPanel;

/**
 * 反洗钱系统跳转标签
 * 
 * <pre></pre>
 * @author ★zhujunde
 */
public class AmlRegTag extends SimpleTagSupport {
    
    private static Logger logger = LoggerFactory.getLogger(AmlRegTag.class);
    
    private String userCode;
    private String comCode;
    private String bussNo;
    private String bussType;
    private String recTime;
    private String btnName;
    private String btnID;


    @Override
    public void doTag() throws JspException {

        PageContext pageContext = (PageContext)this.getJspContext();
        JspWriter out = pageContext.getOut();
        try{
            String amlUrl = SpringProperties.getProperty("dhic.aml.url");
            logger.info("amlUrl============================"+amlUrl);
            FxqAPIPanel fxqAPIPanel = new FxqAPIPanel(amlUrl);
            String html = fxqAPIPanel.loadCustCheckButton(userCode,comCode,bussNo,bussType,recTime,btnName,btnID);
            out.write(html);
        }
        catch(Exception e){
            e.printStackTrace();
            throw new JspException(e.getMessage());
        }
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getComCode() {
        return comCode;
    }

    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    public String getBussNo() {
        return bussNo;
    }

    public void setBussNo(String bussNo) {
        this.bussNo = bussNo;
    }

    public String getBussType() {
        return bussType;
    }

    public void setBussType(String bussType) {
        this.bussType = bussType;
    }

    public String getRecTime() {
        return recTime;
    }

    public void setRecTime(String recTime) {
        this.recTime = recTime;
    }

    public String getBtnName() {
        return btnName;
    }

    public void setBtnName(String btnName) {
        this.btnName = btnName;
    }

    public String getBtnID() {
        return btnID;
    }

    public void setBtnID(String btnID) {
        this.btnID = btnID;
    }
}
