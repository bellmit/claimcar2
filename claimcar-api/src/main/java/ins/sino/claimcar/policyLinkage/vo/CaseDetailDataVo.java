package ins.sino.claimcar.policyLinkage.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * <pre></pre>
 * @author ★niuqiang
 */

@XStreamAlias("data")  
public class CaseDetailDataVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("baseInfo") 
	private CaseDetailBaseInfoVo baseInfo;
	
	@XStreamAlias("caseId") 
	private String caseId;
	
	@XStreamImplicit
	private List<CaseDetailImgList1Vo> imgList1;
	
	@XStreamImplicit
	private List<CaseDetailImgList2Vo> imgList2;
	
	@XStreamImplicit
	private List<CaseDetailImgList3Vo> imgList3;
	
	@XStreamImplicit
	private List<CaseDetailImgList4Vo> imgList4;
	
	@XStreamImplicit
	private List<CaseDetailImgList5Vo> imgList5;
	
	@XStreamImplicit
	private List<CaseDetailImgList6Vo> imgList6;
	
	@XStreamImplicit
	private List<CaseDetailImgList7Vo> imgList7;
	
	@XStreamImplicit
	private List<CaseDetailImgList8Vo> imgList8;
	
	
	@XStreamImplicit
	private List<CaseDetailInjuredInfosVo> injuredInfos;
	
	@XStreamImplicit
	private List<CaseDetaillossInfosVo> lossInfos;
	
	@XStreamAlias("targetCarInfo")
	private CaseDetailTargetCarInfoVo targetCarInfo;
	
	@XStreamImplicit
	private List<CaseDetailThirdCarInfosVo> thirdCarInfos;

	public CaseDetailBaseInfoVo getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(CaseDetailBaseInfoVo baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public List<CaseDetailImgList1Vo> getImgList1() {
		return imgList1;
	}

	public void setImgList1(List<CaseDetailImgList1Vo> imgList1) {
		this.imgList1 = imgList1;
	}

	public List<CaseDetailImgList2Vo> getImgList2() {
		return imgList2;
	}

	public void setImgList2(List<CaseDetailImgList2Vo> imgList2) {
		this.imgList2 = imgList2;
	}

	public List<CaseDetailImgList3Vo> getImgList3() {
		return imgList3;
	}

	public void setImgList3(List<CaseDetailImgList3Vo> imgList3) {
		this.imgList3 = imgList3;
	}

	public List<CaseDetailImgList4Vo> getImgList4() {
		return imgList4;
	}

	public void setImgList4(List<CaseDetailImgList4Vo> imgList4) {
		this.imgList4 = imgList4;
	}

	public List<CaseDetailImgList5Vo> getImgList5() {
		return imgList5;
	}

	public void setImgList5(List<CaseDetailImgList5Vo> imgList5) {
		this.imgList5 = imgList5;
	}

	public List<CaseDetailImgList6Vo> getImgList6() {
		return imgList6;
	}

	public void setImgList6(List<CaseDetailImgList6Vo> imgList6) {
		this.imgList6 = imgList6;
	}

	public List<CaseDetailImgList7Vo> getImgList7() {
		return imgList7;
	}

	public void setImgList7(List<CaseDetailImgList7Vo> imgList7) {
		this.imgList7 = imgList7;
	}

	public List<CaseDetailImgList8Vo> getImgList8() {
		return imgList8;
	}

	public void setImgList8(List<CaseDetailImgList8Vo> imgList8) {
		this.imgList8 = imgList8;
	}

	public List<CaseDetailInjuredInfosVo> getInjuredInfos() {
		return injuredInfos;
	}

	public void setInjuredInfos(List<CaseDetailInjuredInfosVo> injuredInfos) {
		this.injuredInfos = injuredInfos;
	}

	public List<CaseDetaillossInfosVo> getLossInfos() {
		return lossInfos;
	}

	public void setLossInfos(List<CaseDetaillossInfosVo> lossInfos) {
		this.lossInfos = lossInfos;
	}

	public CaseDetailTargetCarInfoVo getTargetCarInfo() {
		return targetCarInfo;
	}

	public void setTargetCarInfo(CaseDetailTargetCarInfoVo targetCarInfo) {
		this.targetCarInfo = targetCarInfo;
	}

	public List<CaseDetailThirdCarInfosVo> getThirdCarInfos() {
		return thirdCarInfos;
	}

	public void setThirdCarInfos(List<CaseDetailThirdCarInfosVo> thirdCarInfos) {
		this.thirdCarInfos = thirdCarInfos;
	}


	
	

}
