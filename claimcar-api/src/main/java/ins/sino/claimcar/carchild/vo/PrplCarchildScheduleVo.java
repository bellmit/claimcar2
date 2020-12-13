package ins.sino.claimcar.carchild.vo;


public class PrplCarchildScheduleVo extends PrplCarchildScheduleVoBase implements java.io.Serializable {

    /**  */
    private static final long serialVersionUID = 1L;
    
    private String lossCountent;//损失项
    private String scheduleUserName;
    private String scheduleUserPhone;
    
    public String getLossCountent() {
        return lossCountent;
    }

    
    public void setLossCountent(String lossCountent) {
        this.lossCountent = lossCountent;
    }
    
    
    public String getScheduleUserName() {
        return scheduleUserName;
    }

    
    public void setScheduleUserName(String scheduleUserName) {
        this.scheduleUserName = scheduleUserName;
    }


    public String getScheduleUserPhone() {
        return scheduleUserPhone;
    }

    
    public void setScheduleUserPhone(String scheduleUserPhone) {
        this.scheduleUserPhone = scheduleUserPhone;
    }

}
