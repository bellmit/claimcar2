package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 手动调度请求接口-返回vo（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("PACKET")
public class HandleScheduleResVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String type = "REQUEST";
	
	@XStreamAsAttribute
	private String version = "1.0";
	
	@XStreamAlias("HEAD")
	private HeadReq head;
	
	@XStreamAlias("BODY")
	private HandleScheduleResBody body;
}
