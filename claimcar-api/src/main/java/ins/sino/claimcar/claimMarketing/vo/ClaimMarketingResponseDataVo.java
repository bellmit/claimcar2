package ins.sino.claimcar.claimMarketing.vo;

import java.io.Serializable;

/**
 * description: ClaimMarketingResponseDataVo
 * date: 2020/9/25 15:29
 * author: lk
 * version: 1.0
 */
public class ClaimMarketingResponseDataVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 状态，0-失败，1-成功。
     */
    private String status;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 数据
     */
    private ClaimMarketingDataVo data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ClaimMarketingDataVo getData() {
        return data;
    }

    public void setData(ClaimMarketingDataVo data) {
        this.data = data;
    }
}
