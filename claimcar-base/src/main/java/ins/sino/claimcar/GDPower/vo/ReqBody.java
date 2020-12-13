package ins.sino.claimcar.GDPower.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @Description:
 * @Author: Gusheng Huang
 * @Date: Create in 上午11:51 19-2-19
 * @Modified By:
 */
@XStreamAlias("Body")
public class ReqBody {
    @XStreamAlias("Token")
    private String token;

    @XStreamAlias("NewDate")
    private String newDate;

    @XStreamAlias("StartTime")
    private String startTime;

    @XStreamAlias("EndTime")
    private String endTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewDate() {
        return newDate;
    }

    public void setNewDate(String newDate) {
        this.newDate = newDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
