package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * @author zjd
 *
 */
@XStreamAlias("CHECKTASKINFO")
public class CheckTaskInfoSubmitResVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号

	@XStreamAlias("CHECKID")
	private String checkId; //理赔查勘ID
	
	@XStreamAlias("TASKID")
	private String taskId; //任务id
	
	@XStreamAlias("OPTIONTYPE")
	private String optionType; //操作类型

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    
    public String getCheckId() {
        return checkId;
    }

    
    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    
    public String getTaskId() {
        return taskId;
    }

    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    
    public String getOptionType() {
        return optionType;
    }

    
    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }


}
