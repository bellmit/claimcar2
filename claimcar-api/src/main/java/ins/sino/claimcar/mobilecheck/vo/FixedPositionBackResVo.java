package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 客户定位界面返回数据请求接口-返回vo（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("PACKET")
public class FixedPositionBackResVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String type = "REQUEST";
	
	@XStreamAsAttribute
	private String version = "1.0";
	
	@XStreamAlias("HEAD")
	private HeadRes head;
	
	@XStreamAlias("BODY")
	private FixedPositionBackResBody body;
}
