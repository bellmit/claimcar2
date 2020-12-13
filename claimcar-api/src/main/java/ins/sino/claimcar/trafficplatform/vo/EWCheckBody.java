package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Body")
public class EWCheckBody implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("BasePart")
	private EWCheckBasePart basePart;
	
	@XStreamImplicit(itemFieldName="VehicleData")
	private List<EWCheckCarData> carDataList;
	
	@XStreamImplicit(itemFieldName="ProtectData")
	private List<EWCheckPropData> propDataList;
	
	@XStreamImplicit(itemFieldName="PersonData")
	private List<EWCheckPersonData> personDataList;

	public EWCheckBasePart getBasePart() {
		return basePart;
	}

	public void setBasePart(EWCheckBasePart basePart) {
		this.basePart = basePart;
	}

	public List<EWCheckCarData> getCarDataList() {
		return carDataList;
	}

	public void setCarDataList(List<EWCheckCarData> carDataList) {
		this.carDataList = carDataList;
	}

	public List<EWCheckPropData> getPropDataList() {
		return propDataList;
	}

	public void setPropDataList(List<EWCheckPropData> propDataList) {
		this.propDataList = propDataList;
	}

	public List<EWCheckPersonData> getPersonDataList() {
		return personDataList;
	}

	public void setPersonDataList(List<EWCheckPersonData> personDataList) {
		this.personDataList = personDataList;
	}


	
}
