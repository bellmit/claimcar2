package ins.platform.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 基础平台开户银行省信息响应数据实体
 *
 * @author maofengning
 * @date 2020/7/2 17:51
 */
public class ProvinceResponseDto implements Serializable {

    private static final long serialVersionUID = -8291411632840200660L;
    /**响应代码 */
    private int code;
    /**提示信息 */
    private String msg;
    /**结果数据 */
    private List<ProvinceDetailDto> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ProvinceDetailDto> getData() {
        return data;
    }

    public void setData(List<ProvinceDetailDto> data) {
        this.data = data;
    }
}
