package ins.sino.claimcar.other.vo;

import java.util.Date;



public class PrplQuartzLogVo extends PrplQuartzLogVoBase{

    /**  */
    private static final long serialVersionUID = 1L;

    private Date reportTime;
    private Date reportTimeStart;
    private Date reportTimeEnd;
    
    private Date createTimeStart;
    private Date createTimeEnd;
    
    private String exception;

    
    public Date getReportTime() {
        return reportTime;
    }

    
    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    
    public Date getReportTimeStart() {
        return reportTimeStart;
    }

    
    public void setReportTimeStart(Date reportTimeStart) {
        this.reportTimeStart = reportTimeStart;
    }

    
    public Date getReportTimeEnd() {
        return reportTimeEnd;
    }

    
    public void setReportTimeEnd(Date reportTimeEnd) {
        this.reportTimeEnd = reportTimeEnd;
    }

    
    public Date getCreateTimeStart() {
        return createTimeStart;
    }

    
    public void setCreateTimeStart(Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    
    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    
    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    
    public String getException() {
        return exception;
    }

    
    public void setException(String exception) {
        this.exception = exception;
    }
    
    
}
