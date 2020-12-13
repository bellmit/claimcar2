package ins.sino.claimcar.manager.vo;

/**
 * Custom VO class of PO SysMsgcontent
 */ 
public class SysMsgContentVo extends SysMsgContentVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private SysMsgReContentVo reMsgVo;

	public SysMsgReContentVo getReMsgVo() {
		return reMsgVo;
	}

	public void setReMsgVo(SysMsgReContentVo reMsgVo) {
		this.reMsgVo = reMsgVo;
	}
}
