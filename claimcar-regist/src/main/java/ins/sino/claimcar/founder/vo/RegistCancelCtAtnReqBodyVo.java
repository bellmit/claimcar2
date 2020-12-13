package ins.sino.claimcar.founder.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 车险报案vo（理赔请求客服系统-请求body,体信息）
 * @author Luwei
 *
 */
@XStreamAlias("BODY")
public class RegistCancelCtAtnReqBodyVo {

	/**  */
	@XStreamAlias("OUTDATE")
	private RegistCancelCtAtnInputdataVo registCancelCtAtnInputdataVo;

    
    public RegistCancelCtAtnInputdataVo getRegistCancelCtAtnInputdataVo() {
        return registCancelCtAtnInputdataVo;
    }

    
    public void setRegistCancelCtAtnInputdataVo(RegistCancelCtAtnInputdataVo registCancelCtAtnInputdataVo) {
        this.registCancelCtAtnInputdataVo = registCancelCtAtnInputdataVo;
    }




}
