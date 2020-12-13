package ins.sino.claimcar.check.vo;

/**
 * Custom VO class of PO PrpLCheckPerson
 */
public class PrpLCheckPersonVo extends PrpLCheckPersonVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long checkTaskId;

	private String personSerialNo;
	
	public Long getCheckTaskId() {
		return checkTaskId;
	}

	public void setCheckTaskId(Long checkTaskId) {
		this.checkTaskId = checkTaskId;
	}

    
    public String getPersonSerialNo() {
        return personSerialNo;
    }

    
    public void setPersonSerialNo(String personSerialNo) {
        this.personSerialNo = personSerialNo;
    }

	
}
