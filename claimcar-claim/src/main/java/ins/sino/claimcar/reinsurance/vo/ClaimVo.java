package ins.sino.claimcar.reinsurance.vo;

import java.io.Serializable;
import java.util.Collection;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/*
 * �������ٱ��Ѿ���ݷ�̯����ClaimVo
 * @author luows
 * @since (2016��8��2�� ����9:11:16): <br>
 */
@XStreamAlias("ClaimVo")
public class ClaimVo implements Serializable {
	private static final long serialVersionUID = 1L;
	@XStreamAlias("prpLClaimVo")
	private PrpLClaimVo   prpLClaimVo;  //����������Ϣ
	@SuppressWarnings("rawtypes")
	@XStreamImplicit
	private Collection PrpLclaimHisVo; //��ʷ������Ϣ
	
	public ClaimVo() {

	}
	
	public PrpLClaimVo getPrpLClaimVo() {
		return this.prpLClaimVo;
	}

	public void setPrpLClaimVo(PrpLClaimVo prpLClaimVo) {
		this.prpLClaimVo = prpLClaimVo;
	}
	
	@SuppressWarnings("rawtypes")
	public Collection getPrpLclaimHisVo() {
		return this.PrpLclaimHisVo;
	}

	@SuppressWarnings("rawtypes")
	public void setPrpLclaimHisVo(Collection PrpLclaimHisVo) {
		this.PrpLclaimHisVo = PrpLclaimHisVo;
	}
}
