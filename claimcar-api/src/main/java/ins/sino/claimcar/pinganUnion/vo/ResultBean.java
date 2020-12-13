package ins.sino.claimcar.pinganUnion.vo;

import java.io.Serializable;
import java.util.Set;

/**
 * @Description 描述
 * @Author liuys
 * @Date 2020/7/21 8:39
 */
public class ResultBean implements Serializable {
    private Boolean success;
    private String message;
    private Object data;
    private Set<String> compensateNo;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<String> getCompensateNo() {
        return compensateNo;
    }

    public void setCompensateNo(Set<String> compensateNo) {
        this.compensateNo = compensateNo;
    }

    public static ResultBean success(){
        ResultBean resultBean = new ResultBean();
        resultBean.setSuccess(true);
        resultBean.setMessage("处理成功");
        return resultBean;
    }

    public ResultBean success(String message, Object data){
        this.success = true;
       this.message = message;
       this.data = data;
       return this;
    }

    public ResultBean fail(String message){
        this.success = false;
        this.message = message;
        return this;
    }

    public ResultBean fail(String message, Object data){
        this.success = false;
        this.message = message;
        this.data = data;
        return this;
    }

    public Boolean isSuccess(){
        return this.success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
