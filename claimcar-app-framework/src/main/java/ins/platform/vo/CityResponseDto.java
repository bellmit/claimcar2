package ins.platform.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 基础平台开户银行市信息响应数据实体
 *
 * @author maofengning
 * @date 2020/7/3 17:51
 */
public class CityResponseDto implements Serializable {

    private static final long serialVersionUID = 5158507259031202947L;

    /** 响应代码 */
    private int code;
    /** 提示信息 */
    private String msg;
    /** 结果数据 */
    private List<CityDetailDto> data;

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

    public List<CityDetailDto> getData() {
        return data;
    }

    public void setData(List<CityDetailDto> data) {
        this.data = data;
    }
}
