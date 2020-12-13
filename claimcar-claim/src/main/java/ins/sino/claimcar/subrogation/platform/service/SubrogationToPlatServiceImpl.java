/******************************************************************************
* CREATETIME : 2016年3月16日 下午3:32:55
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.service;

import ins.framework.service.CodeTranService;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.utils.DataUtils;
import ins.sino.claimcar.carplatform.constant.RequestType;
import ins.sino.claimcar.carplatform.controller.PlatformController;
import ins.sino.claimcar.carplatform.util.PlatformFactory;
import ins.sino.claimcar.carplatform.vo.BiResponseHeadVo;
import ins.sino.claimcar.carplatform.vo.CiResponseHeadVo;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.subrogation.platform.vo.AccountsInfoVo;
import ins.sino.claimcar.subrogation.platform.vo.AccountsQueryBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.AccountsQueryPacketInfoVo;
import ins.sino.claimcar.subrogation.platform.vo.AccountsQueryReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.AccountsQueryResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiBeSubrogationBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiBeSubrogationDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiBeSubrogationReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiBeSubrogationResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiClaimBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiClaimDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiClaimReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiClaimResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiClaimRiskWarnBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiClaimRiskWarnDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiClaimRiskWarnReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiClaimRiskWarnResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedCancelBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedCancelReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedConfirmBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedConfirmReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedConfirmResBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedConfirmResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedPolicyDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedQueryBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedQueryReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiLockedQueryResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiPolicyRiskWarnBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiPolicyRiskWarnDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiPolicyRiskWarnReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiPolicyRiskWarnResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryConfirmBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryConfirmDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryConfirmReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryConfirmResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryQueryBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryQueryDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryQueryReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryQueryResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryReturnConfirmDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiRecoveryReturnConfirmResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationBasePartVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationCheckDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationClaimBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationClaimCloseDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationClaimDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationClaimReopenDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationClaimReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationClaimResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationDocDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationEstimateDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationRecoveryConfirmDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationReportDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiSubrogationUnderWriteDataVo;
import ins.sino.claimcar.subrogation.platform.vo.BiUndwrtWriteAdjustmentDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CheckBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.CheckReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiBeSubrogationBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.CiBeSubrogationDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiBeSubrogationReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiBeSubrogationResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiClaimBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.CiClaimDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiClaimReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiClaimResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiClaimRiskWarnBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.CiClaimRiskWarnDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiClaimRiskWarnReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiClaimRiskWarnResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiPolicyRiskWarnBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.CiPolicyRiskWarnDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiPolicyRiskWarnReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiPolicyRiskWarnResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiRecoveryQueryBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.CiRecoveryQueryDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiRecoveryQueryReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiRecoveryQueryResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationBasePartVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationCheckDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationClaimBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationClaimCloseDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationClaimDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationClaimReopenDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationClaimReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationClaimResBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationDocDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationEstimateDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationRecoveryConfirmDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationReportDataVo;
import ins.sino.claimcar.subrogation.platform.vo.CiSubrogationUnderWriteDataVo;
import ins.sino.claimcar.subrogation.platform.vo.SubrogationCheckBaseVo;
import ins.sino.claimcar.subrogation.platform.vo.SubrogationCheckDataVo;
import ins.sino.claimcar.subrogation.platform.vo.SubrogationCheckReqBodyVo;
import ins.sino.claimcar.subrogation.platform.vo.SubrogationCheckResBodyVo;
import ins.sino.claimcar.subrogation.po.PrpLRecoveryOrPay;
import ins.sino.claimcar.subrogation.service.LockedPolicyService;
import ins.sino.claimcar.subrogation.service.PlatLockService;
import ins.sino.claimcar.subrogation.vo.BeSubrogationVo;
import ins.sino.claimcar.subrogation.vo.CClaimDataVo;
import ins.sino.claimcar.subrogation.vo.ClaimDataVo;
import ins.sino.claimcar.subrogation.vo.ClaimRiskWarnVo;
import ins.sino.claimcar.subrogation.vo.ConfirmQueryVo;
import ins.sino.claimcar.subrogation.vo.ConfirmVo;
import ins.sino.claimcar.subrogation.vo.PolicyRiskWarnVo;
import ins.sino.claimcar.subrogation.vo.PrpLLockedPolicyVo;
import ins.sino.claimcar.subrogation.vo.PrpLPlatLockVo;
import ins.sino.claimcar.subrogation.vo.PrpLRecoveryOrPayVo;
import ins.sino.claimcar.subrogation.vo.RecoveryResultVo;
import ins.sino.claimcar.subrogation.vo.RiskWarnQueryVo;
import ins.sino.claimcar.subrogation.vo.SCheckQueryVo;
import ins.sino.claimcar.subrogation.vo.SubrogationBasePartVo;
import ins.sino.claimcar.subrogation.vo.SubrogationCheckDataVos;
import ins.sino.claimcar.subrogation.vo.SubrogationCheckVo;
import ins.sino.claimcar.subrogation.vo.SubrogationClaimCloseDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationClaimDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationClaimReopenDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationDataVos;
import ins.sino.claimcar.subrogation.vo.SubrogationDocDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationDocVo;
import ins.sino.claimcar.subrogation.vo.SubrogationEstimateDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationQueryVo;
import ins.sino.claimcar.subrogation.vo.SubrogationRecoveryConfirmDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationReportDataVo;
import ins.sino.claimcar.subrogation.vo.SubrogationUnderWriteDataVo;
import ins.sino.claimcar.subrogation.vo.UndwrtWriteAdjustmentDataVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"}, timeout = 1000000)
@Path("subrogationToPlatService")
public class SubrogationToPlatServiceImpl implements SubrogationToPlatService {
	
	private Logger logger = LoggerFactory.getLogger(SubrogationToPlatServiceImpl.class);
	
	@Autowired
	private CodeTranService codeTranService;
	
	@Autowired
	private LockedPolicyService lockedPolicyService;
	
	@Autowired
	private SubrogationAddService subrogationAddService;
	@Autowired
	private RegistQueryService registQueryService;
	
	@Autowired
	private AreaDictService areaDictService;
	@Autowired
	private PlatLockService platLockService;
	/**
	 * 保单风险信息查询  交强
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:47:54): <br>
	 */
	@Override
	public List<PolicyRiskWarnVo> sendPolicyRiskWarnCI(RiskWarnQueryVo queryVo) throws Exception{
		
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.PolicyRiskWarnCI);
		CiPolicyRiskWarnReqBodyVo bodyVo = new CiPolicyRiskWarnReqBodyVo();
		CiPolicyRiskWarnBaseVo baseVo = new CiPolicyRiskWarnBaseVo();
		baseVo.setEngineNo(queryVo.getEngineNo());
		baseVo.setReportNo(queryVo.getRegistNo());
		baseVo.setCarMark(queryVo.getLicenseNo());
		baseVo.setRackNo(queryVo.getVinNo());
		baseVo.setVehicleType(queryVo.getLicenseType());
		baseVo.setAreaCode(queryVo.getAreaCode());
		bodyVo.setPolicyRiskWarnBaseVo(baseVo);

		controller.callPlatform(bodyVo);


		List<PolicyRiskWarnVo> warnList = new ArrayList<PolicyRiskWarnVo>();
		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			//String msg = resHeadVo.getErrorDesc();
			//throw new BusinessException("风险预警",msg);
			return warnList;
			
		}
		CiPolicyRiskWarnResBodyVo resBodyVo = controller.getBodyVo(CiPolicyRiskWarnResBodyVo.class);
		
		List<CiPolicyRiskWarnDataVo> warnDataList = resBodyVo.getPolicyRiskWarnDataList();
		if(warnDataList ==null || warnDataList.isEmpty() == true){
			return warnList;
		}
		warnList = Beans.copyDepth().from(warnDataList).toList(PolicyRiskWarnVo.class);
		return warnList;
	}
	
	/**
	 * 保单风险信息查询  商业
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:48:39): <br>
	 */
	@Override
	public List<PolicyRiskWarnVo> sendPolicyRiskWarnBI(RiskWarnQueryVo queryVo) throws Exception{
		
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.PolicyRiskWarnBI);
		BiPolicyRiskWarnReqBodyVo bodyVo = new BiPolicyRiskWarnReqBodyVo();
		BiPolicyRiskWarnBaseVo baseVo = new BiPolicyRiskWarnBaseVo();
		
		baseVo.setClaimNotificationNo(queryVo.getRegistNo());
		baseVo.setLicensePlateNo(queryVo.getLicenseNo());
		baseVo.setLicensePlateType(queryVo.getLicenseType());
		baseVo.setEngineNo(queryVo.getEngineNo());
		baseVo.setVin(queryVo.getVinNo());
		baseVo.setAreaCode(queryVo.getAreaCode());
		bodyVo.setPolicyRiskWarnBaseVo(baseVo);

		controller.callPlatform(bodyVo);
		
		
		List<PolicyRiskWarnVo> warnList = new ArrayList<PolicyRiskWarnVo>();
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			//String msg = resHeadVo.getErrorDesc();
			//throw new BusinessException("风险预警",msg);
			return warnList;
			
		}
		BiPolicyRiskWarnResBodyVo resBodyVo = controller.getBodyVo(BiPolicyRiskWarnResBodyVo.class);
		
		List<BiPolicyRiskWarnDataVo> warnDataList = resBodyVo.getPolicyRiskWarnDataList();
		if(warnDataList ==null || warnDataList.isEmpty() == true){
			return warnList;
		}
		
		warnList = Beans.copyDepth().from(warnDataList).toList(PolicyRiskWarnVo.class);
		return warnList;
	}
	
	/**
	 * 理赔风险信息查询  交强
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:48:51): <br>
	 */
	@Override
	public List<ClaimRiskWarnVo> sendClaimRiskWarnCI(RiskWarnQueryVo queryVo) throws Exception{
		
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.ClaimRiskWarnCI);
		CiClaimRiskWarnReqBodyVo bodyVo = new CiClaimRiskWarnReqBodyVo();
		CiClaimRiskWarnBaseVo baseVo = new CiClaimRiskWarnBaseVo();
		
		baseVo.setEngineNo(queryVo.getEngineNo());
		baseVo.setReportNo(queryVo.getRegistNo());
		baseVo.setCarMark(queryVo.getLicenseNo());
		baseVo.setRackNo(queryVo.getVinNo());
		baseVo.setVehicleType(queryVo.getLicenseType());
		baseVo.setAreaCode(queryVo.getAreaCode());
		bodyVo.setCiClaimRiskWarnBaseVo(baseVo);
		
		controller.callPlatform(bodyVo);
		
		List<ClaimRiskWarnVo> warnList = new ArrayList<ClaimRiskWarnVo>();
		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			//String msg = resHeadVo.getErrorDesc();
			//throw new BusinessException("风险预警",msg);
			
			return warnList;
			
		}
		CiClaimRiskWarnResBodyVo resBodyVo = controller.getBodyVo(CiClaimRiskWarnResBodyVo.class);
		
		List<CiClaimRiskWarnDataVo> warnDataList = resBodyVo.getClaimRiskWarnDataList();
		if(warnDataList ==null || warnDataList.isEmpty() == true){
			return warnList;
		}
		//warnList = Beans.copyDepth().from(warnDataList).toList(PolicyRiskWarnVo.class);
		for(CiClaimRiskWarnDataVo warnVo : warnDataList){
			ClaimRiskWarnVo claimWarnVo = new ClaimRiskWarnVo();
			//保险公司	承保地区	车俩属性	报案时间	出险时间	出险地点	出险经过	案件状态
			claimWarnVo.setInsurerCode(warnVo.getInsurerCode());
			claimWarnVo.setInsurerArea(warnVo.getInsurerArea());
			claimWarnVo.setVehicleProperty(warnVo.getVehicleProperty());
			claimWarnVo.setNotificationTime(warnVo.getReportTime());
			claimWarnVo.setLossTime(warnVo.getAccidentTime());
			claimWarnVo.setLossArea(warnVo.getAccidentPlace());
			claimWarnVo.setLossDesc(warnVo.getAccidentDescription());
			claimWarnVo.setClaimStatus(warnVo.getClaimStatus());
			
			warnList.add(claimWarnVo);
		}
		
		return warnList;
	}
	
	/**
	 * 理赔风险信息查询  商业
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:49:10): <br>
	 */
	@Override
	public List<ClaimRiskWarnVo> sendClaimRiskWarnBI(RiskWarnQueryVo queryVo) throws Exception{
		
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.ClaimRiskWarnBI);
		BiClaimRiskWarnReqBodyVo bodyVo = new BiClaimRiskWarnReqBodyVo();
		BiClaimRiskWarnBaseVo baseVo = new BiClaimRiskWarnBaseVo();
		
		baseVo.setClaimNotificationNo(queryVo.getRegistNo());
		baseVo.setLicensePlateNo(queryVo.getLicenseNo());
		baseVo.setLicensePlateType(queryVo.getLicenseType());
		baseVo.setEngineNo(queryVo.getEngineNo());
		baseVo.setVin(queryVo.getVinNo());
		baseVo.setAreaCode(queryVo.getAreaCode());
		bodyVo.setBiClaimRiskWarnBaseVo(baseVo);
		
		controller.callPlatform(bodyVo);

		List<ClaimRiskWarnVo> warnList = new ArrayList<ClaimRiskWarnVo>();
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			//String msg = resHeadVo.getErrorDesc();
			//throw new BusinessException("风险预警",msg);
			return warnList;
			
		}
		BiClaimRiskWarnResBodyVo resBodyVo = controller.getBodyVo(BiClaimRiskWarnResBodyVo.class);
		
		List<BiClaimRiskWarnDataVo> warnDataList = resBodyVo.getBiClaimRiskWarnDataList();
		if(warnDataList ==null || warnDataList.isEmpty() == true){
			return warnList;
		}
		warnList = Beans.copyDepth().from(warnDataList).toList(ClaimRiskWarnVo.class);
		
		return warnList;
	}

	/**
	 * 被代位求偿查询 交强
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:58:15): <br>
	 */
	@Override
	public List<BeSubrogationVo> sendBeSubrogationQueryCI(SubrogationQueryVo queryVo)throws Exception {
		CiBeSubrogationReqBodyVo bodyVo = new CiBeSubrogationReqBodyVo();
		CiBeSubrogationBaseVo baseVo = new CiBeSubrogationBaseVo();

		baseVo.setReportNo(queryVo.getRegistNo());
		baseVo.setInsurerArea(queryVo.getAreaCode());
		baseVo.setInsurerCode(queryVo.getInsurerCode());
		baseVo.setClaimCode(queryVo.getClaimCode());
		baseVo.setConfirmSequenceNo(queryVo.getConfirmSequenceNo());
		baseVo.setLockedTimeEnd(queryVo.getLockedTimeEnd());
		baseVo.setLockedTimeStart(queryVo.getLockedTimeStart());
		bodyVo.setBeSubrogationBaseVo(baseVo);

		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.SubrogationRiskWarnCI);
		controller.callPlatform(bodyVo);
		List<BeSubrogationVo> warnList = new ArrayList<BeSubrogationVo>();
		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			//String msg = resHeadVo.getErrorDesc();
			//throw new BusinessException("风险预警",msg);
			
			return warnList;
			
		}
		CiBeSubrogationResBodyVo resBodyVo = controller.getBodyVo(CiBeSubrogationResBodyVo.class);
		List<CiBeSubrogationDataVo> beSubrogationDataList = resBodyVo.getBeSubrogationList();
		if(beSubrogationDataList ==null || beSubrogationDataList.isEmpty() == true){
			return warnList;
		}
		for(CiBeSubrogationDataVo ciBeSubrogationVo : beSubrogationDataList){
			BeSubrogationVo beSubrogationVo = new BeSubrogationVo();
			Beans.copy().from(ciBeSubrogationVo).to(beSubrogationVo);
//			beSubrogationVo.setEngineNo(ciBeSubrogationVo.getEngineNo());
//			beSubrogationVo.setClaimProgress(ciBeSubrogationVo.getClaimProgress());
			beSubrogationVo.setClaimNotificationNo(ciBeSubrogationVo.getReportNo());//追偿方报案号
			beSubrogationVo.setLicensePlateNo(ciBeSubrogationVo.getCarMark());//追偿方车辆号牌号码
			beSubrogationVo.setLicensePlateType(ciBeSubrogationVo.getVehicleType());//追偿方车辆号牌种类
			beSubrogationVo.setVin(ciBeSubrogationVo.getRackNo());//追偿方车辆VIN码
			beSubrogationVo.setOppoentClaimSequenceNo(ciBeSubrogationVo.getOppoentClaimCode());// 责任对方理赔编码
			beSubrogationVo.setOppoentClaimNotificationNo(ciBeSubrogationVo.getOppoentReportNo());//责任对方报案号
			
			warnList.add(beSubrogationVo);
		}
		
		return warnList;
	}

	/**
	 * 被代位求偿查询 商业
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:58:35): <br>
	 */
	@Override
	public List<BeSubrogationVo> sendBeSubrogationQueryBI(SubrogationQueryVo queryVo)throws Exception {
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.SubrogationRiskWarnBI);
		BiBeSubrogationReqBodyVo bodyVo = new BiBeSubrogationReqBodyVo();
		BiBeSubrogationBaseVo baseVo = new BiBeSubrogationBaseVo();
		
		baseVo.setClaimNotificationNo(queryVo.getRegistNo());
		baseVo.setClaimSequenceNo(queryVo.getClaimCode());
		baseVo.setInsurerArea(queryVo.getAreaCode());
		baseVo.setInsurerCode(queryVo.getInsurerCode());
		baseVo.setConfirmSequenceNo(queryVo.getConfirmSequenceNo());
		baseVo.setLockedTimeEnd(queryVo.getLockedTimeEnd());
		baseVo.setLockedTimeStart(queryVo.getLockedTimeStart());
		bodyVo.setBeSubrogationBaseVo(baseVo);
		
		controller.callPlatform(bodyVo);
		
		List<BeSubrogationVo> warnList = new ArrayList<BeSubrogationVo>();
		try{
			BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
			if( !"0000".equals(resHeadVo.getErrorCode())){
				//String msg = resHeadVo.getErrorDesc();
				//throw new BusinessException("风险预警",msg);
				return warnList;
				
			}
		}catch(Exception e){
			// TODO: handle exception
			return warnList;
		}
		
		BiBeSubrogationResBodyVo resBodyVo = controller.getBodyVo(BiBeSubrogationResBodyVo.class);
		List<BiBeSubrogationDataVo> beSubrogationDataList = resBodyVo.getBeSubrogationDataList();
		if(beSubrogationDataList ==null || beSubrogationDataList.isEmpty() == true){
			return warnList;
		}
		warnList = Beans.copyDepth().from(beSubrogationDataList).toList(BeSubrogationVo.class);
		
		return warnList;
	}

	/**
	 * 结算码接口查询 交强 
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午5:35:09): <br>
	 */
	@Override
	public List<RecoveryResultVo> sendRecoveryQueryCI(SubrogationQueryVo queryVo)throws Exception {
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.RecoveryRiskWarnCI);
		CiRecoveryQueryReqBodyVo bodyVo = new CiRecoveryQueryReqBodyVo();
		CiRecoveryQueryBaseVo baseVo = new CiRecoveryQueryBaseVo();

		baseVo.setReportNo(queryVo.getRegistNo());
		baseVo.setClaimCode(queryVo.getClaimCode());
		baseVo.setOppoentInsurerCode(queryVo.getOppoentInsureCode());
		baseVo.setOppoentInsurerArea(queryVo.getOppoentAreaCode());
		baseVo.setOppoentCoverageType(queryVo.getOppentPolicyType());
		baseVo.setOppoentReportNo(queryVo.getOppoentRegistNo());
		baseVo.setRecoverStatus(queryVo.getRecoverStatus());
		bodyVo.setRecoveryQueryBaseVo(baseVo);;

		controller.callPlatform(bodyVo);

		List<RecoveryResultVo> recoveryList = new ArrayList<RecoveryResultVo>();
		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			//String msg = resHeadVo.getErrorDesc();
			//throw new BusinessException("风险预警",msg);
			
			return recoveryList;
			
		}
		CiRecoveryQueryResBodyVo resBodyVo = controller.getBodyVo(CiRecoveryQueryResBodyVo.class);
		
		List<CiRecoveryQueryDataVo> resultList = resBodyVo.getRecoveryQueryList();
		if(resultList ==null || resultList.isEmpty() == true){
			return recoveryList;
		}
		recoveryList = Beans.copyDepth().from(resultList).toList(RecoveryResultVo.class);
		
		return recoveryList;
	}

	/**
	 * 结算码接口查询 商业
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午5:35:09): <br>
	 */
	@Override
	public List<RecoveryResultVo> sendRecoveryQueryBI(SubrogationQueryVo queryVo)throws Exception {
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.RecoveryRiskWarnBI);
		BiRecoveryQueryReqBodyVo bodyVo = new BiRecoveryQueryReqBodyVo();
		BiRecoveryQueryBaseVo baseVo = new BiRecoveryQueryBaseVo();
		
		baseVo.setClaimNotificationNo(queryVo.getRegistNo());
		baseVo.setClaimSequenceNo(queryVo.getClaimCode());
		baseVo.setOppoentInsurerCode(queryVo.getInsurerCode());
		baseVo.setOppoentInsurerArea(queryVo.getOppoentAreaCode());
		baseVo.setOppoentCoverageType(queryVo.getOppentPolicyType());
		baseVo.setRecoverStatus(queryVo.getRecoverStatus());
		bodyVo.setRecoveryQueryBaseVo(baseVo);

		controller.callPlatform(bodyVo);
		
		List<RecoveryResultVo> resultList = new ArrayList<RecoveryResultVo>();
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			String msg = resHeadVo.getErrorDesc();
			throw new IllegalArgumentException(msg);
//			return resultList;
		}
		BiRecoveryQueryResBodyVo resBodyVo = controller.getBodyVo(BiRecoveryQueryResBodyVo.class);
		
		List<BiRecoveryQueryDataVo> recoveryDataList = resBodyVo.getRecoveryQueryDataList();
		if(recoveryDataList ==null || recoveryDataList.isEmpty() == true){
			return resultList;
		}
		
		resultList = Beans.copyDepth().from(recoveryDataList).toList(RecoveryResultVo.class);
		
		return resultList;
	}

	@Override
	public List<PrpLLockedPolicyVo> sendLockConfirmQueryBI(SubrogationQueryVo queryVo)throws Exception {
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.LockConfirmQueryBI);
		BiLockedQueryReqBodyVo bodyVo = new BiLockedQueryReqBodyVo();
		BiLockedQueryBaseVo baseVo = new BiLockedQueryBaseVo();
		
		baseVo.setClaimSequenceNo(queryVo.getClaimSequenceNo());
		baseVo.setClaimNotificationNo(queryVo.getRegistNo());
		baseVo.setOppoentLicensePlateNo(queryVo.getOppoentLincenseNo());
		baseVo.setOppoentLicensePlateType(queryVo.getOppoentLincenseType());
		baseVo.setOppoentEngineNo(queryVo.getOppoentEngineNo());
		baseVo.setOppoentVIN(queryVo.getOppoentVinNo());
		baseVo.setOppoentInsurerCode(queryVo.getOppoentInsureCode());
		baseVo.setOppoentInsurerArea(queryVo.getOppoentAreaCode());
		baseVo.setOppoentPolicyNo(queryVo.getOppoentPolicyNo());
		baseVo.setOppoentClaimNotificationNo(queryVo.getOppoentRegistNo());
		bodyVo.setLockQueryBaseVo(baseVo);

		controller.callPlatform(bodyVo);
		
//		String responseXml ="<?xml version=\"1.0\" encoding=\"GBK\"?> <Packet type=\"RESPONSE\" version=\"1.0\"> <Head> <RequestType>V3201</RequestType> <ResponseCode>1</ResponseCode> <ErrorCode>0000</ErrorCode> <ErrorMessage>成功</ErrorMessage> </Head>"
//				+ " <Body> <LockedPolicyData> <InsurerCode>PICC</InsurerCode> <InsurerArea>110000</InsurerArea> <CoverageType>1</CoverageType> <PolicyNo>PICCPN1234567890</PolicyNo> <LimitAmount>1212</LimitAmount> <IsInsuredCA></IsInsuredCA> "
//				+ "<LicensePlateNo></LicensePlateNo> <LicensePlateType></LicensePlateType> <EngineNo></EngineNo> <VIN>111111111111</VIN> <MatchTimes>2</MatchTimes > <LockedNotifyData> "
//				+ "<ClaimNotificationNo>PICCCEN1234567890 </ClaimNotificationNo> <LossTime>201101010101</LossTime> <LossArea>雍和宫</LossArea> <LossDesc></LossDesc> <ClaimStatus>1</ClaimStatus> <ClaimProgress>01</ClaimProgress > <LockedThirdPartyData> "
//				+ "<LicensePlateNo></LicensePlateNo><LicensePlateType></LicensePlateType> <EngineNo></EngineNo> <VIN>111111111111</VIN> </LockedThirdPartyData> </LockedNotifyData> </LockedPolicyData> </Body> </Packet>";

		List<PrpLLockedPolicyVo> resultList = new ArrayList<PrpLLockedPolicyVo>();
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			String msg = resHeadVo.getErrorDesc();
//			throw new BusinessException("风险预警",msg);
			throw new IllegalStateException(msg);
		}
		BiLockedQueryResBodyVo resBodyVo = controller.getBodyVo(BiLockedQueryResBodyVo.class);
		List<BiLockedPolicyDataVo>  lockedPolicyDataList = resBodyVo.getPrplLockedNotifies();
		
		resultList = Beans.copyDepth().from(lockedPolicyDataList).toList(PrpLLockedPolicyVo.class);
		if(resultList!=null && !resultList.isEmpty()){
			for(PrpLLockedPolicyVo lockedPolicyVo : resultList){
				lockedPolicyVo.setRegistNo(queryVo.getRegistNo());
				lockedPolicyVo.setClaimSequenceNo(queryVo.getClaimSequenceNo());
			}
		}
		//保存锁定 相关信息
		lockedPolicyService.saveLockedPolicy(resultList,queryVo.getRegistNo(),queryVo.getClaimSequenceNo());
		
		return resultList;
	}

	/**
	 * 锁定确认发送报文
	 * @param lockedPolicyVo
	 * @modified:
	 * ☆YangKun(2016年3月30日 上午11:38:12): <br>
	 */
	@Override
	public String  sendLockedConfirmBI(PrpLLockedPolicyVo lockedPolicyVo,String comCode)throws Exception {
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.LockConfirmBI);
		BiLockedConfirmReqBodyVo bodyVo = new BiLockedConfirmReqBodyVo();
		BiLockedConfirmBaseVo baseVo = new BiLockedConfirmBaseVo();

		baseVo.setClaimSequenceNo(lockedPolicyVo.getClaimSequenceNo());
		baseVo.setClaimNotificationNo(lockedPolicyVo.getRegistNo());
		baseVo.setOppoentInsurerCode(lockedPolicyVo.getInsurerCode());
		baseVo.setOppoentInsurerArea(lockedPolicyVo.getInsurerArea());
		baseVo.setOppoentCoverageType(lockedPolicyVo.getCoverageType());
		baseVo.setOppoentPolicyNo(lockedPolicyVo.getPolicyNo());
		baseVo.setOppoentClaimNotificationNo(lockedPolicyVo.getPrplLockedNotifies().get(0).getClaimNotificationNo());
		bodyVo.setLockConfirmBaseVo(baseVo);
		
		controller.callPlatform(bodyVo);


//		responseXml ="<?xml version=\"1.0\" encoding=\"GBK\"?> <Packet type=\"RESPONSE\" version=\"1.0\"> <Head> <RequestType>V3201</RequestType> <ResponseCode>1</ResponseCode> <ErrorCode>0000</ErrorCode> <ErrorMessage>成功</ErrorMessage> </Head>"
//				+ " <Body> <LockedPolicyData> <InsurerCode>PICC</InsurerCode> <InsurerArea>110000</InsurerArea> <CoverageType>1</CoverageType> <PolicyNo>PICCPN1234567890</PolicyNo> <LimitAmount>1212</LimitAmount> <IsInsuredCA></IsInsuredCA> "
//				+ "<LicensePlateNo></LicensePlateNo> <LicensePlateType></LicensePlateType> <EngineNo></EngineNo> <VIN>111111111111</VIN> <MatchTimes>2</MatchTimes > <LockedNotifyData> <ClaimNotificationNo>PICCCEN1234567890 </ClaimNotificationNo> <LossTime>201101010101</LossTime> <LossArea>雍和宫</LossArea> <LossDesc></LossDesc> <ClaimStatus>1</ClaimStatus> <ClaimProgress>01</ClaimProgress > <LockedThirdPartyData> <LicensePlateNo></LicensePlateNo><LicensePlateType></LicensePlateType> <EngineNo></EngineNo> <VIN>111111111111</VIN> </LockedThirdPartyData> </LockedNotifyData> </LockedPolicyData> </Body> </Packet>";

		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			throw new IllegalStateException(resHeadVo.getErrorDesc());
		}
		BiLockedConfirmResBodyVo resBodyVo = controller.getBodyVo(BiLockedConfirmResBodyVo.class);
		BiLockedConfirmResBaseVo confirmVo = resBodyVo.getLockConfirmBaseVo();
		return confirmVo.getRecoveryCode();
		
	}
	
	/**
	 * 锁定取消发送报文
	 * @param lockedPolicyVo
	 * @modified:
	 * ☆YangKun(2016年3月30日 上午11:38:12): <br>
	 */
	@Override
	public String sendLockedCancelBI(String recoveryCode,String failureCause,String comCode)throws Exception {
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.LockCancelBI);
		BiLockedCancelReqBodyVo bodyVo = new BiLockedCancelReqBodyVo();
		BiLockedCancelBaseVo baseVo = new BiLockedCancelBaseVo();

		baseVo.setRecoveryCode(recoveryCode);
		
		baseVo.setFailureCause(failureCause);
		bodyVo.setLockCancelBaseVo(baseVo);
		
		controller.callPlatform(bodyVo);

//		responseXml ="<?xml version=\"1.0\" encoding=\"GBK\"?> <Packet type=\"RESPONSE\" version=\"1.0\"> <Head> <RequestType>V3201</RequestType> <ResponseCode>1</ResponseCode> <ErrorCode>0000</ErrorCode> <ErrorMessage>成功</ErrorMessage> </Head>"
//				+ " <Body> <LockedPolicyData> <InsurerCode>PICC</InsurerCode> <InsurerArea>110000</InsurerArea> <CoverageType>1</CoverageType> <PolicyNo>PICCPN1234567890</PolicyNo> <LimitAmount>1212</LimitAmount> <IsInsuredCA></IsInsuredCA> "
//				+ "<LicensePlateNo></LicensePlateNo> <LicensePlateType></LicensePlateType> <EngineNo></EngineNo> <VIN>111111111111</VIN> <MatchTimes>2</MatchTimes > <LockedNotifyData> <ClaimNotificationNo>PICCCEN1234567890 </ClaimNotificationNo> <LossTime>201101010101</LossTime> <LossArea>雍和宫</LossArea> <LossDesc></LossDesc> <ClaimStatus>1</ClaimStatus> <ClaimProgress>01</ClaimProgress > <LockedThirdPartyData> <LicensePlateNo></LicensePlateNo><LicensePlateType></LicensePlateType> <EngineNo></EngineNo> <VIN>111111111111</VIN> </LockedThirdPartyData> </LockedNotifyData> </LockedPolicyData> </Body> </Packet>";
		
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			throw new IllegalStateException(resHeadVo.getErrorDesc());
		}
		
		return resHeadVo.getResponseCode();
	}
	
	/**
	 * 案件互审
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SubrogationCheckVo> sendSubrogationCheck(SCheckQueryVo queryVo) throws Exception{
		String comCode =queryVo.getComCode();
		if(comCode.startsWith("00")){
			comCode = comCode.substring(0,4)+"0000";
		}else{
			comCode = comCode.substring(0,2)+"000000";
		}
	//	String[] areaStr = areaDictService.findAreaByComCode(comCode);
//		if(areaStr.length == 0){
//			throw new IllegalArgumentException("机构地区编码未配置");
//		}
		String areaCode = codeTranService.transCode("ComArea",comCode);
		
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(), areaCode, RequestType.SubrogationCheck);
		SubrogationCheckReqBodyVo bodyVo = new SubrogationCheckReqBodyVo();
	
		SubrogationCheckBaseVo baseVo = new SubrogationCheckBaseVo();
		baseVo.setAccountsNo(queryVo.getAccountsNo());
		baseVo.setCheckOwnType(queryVo.getCheckOwnType());
		baseVo.setCheckStats(queryVo.getCheckStats());
		baseVo.setRecoverDEnd(queryVo.getRecoverDEnd());;
		baseVo.setRecoverDStart(queryVo.getRecoverDStart());
		bodyVo.setSubrogationCheckBaseVo(baseVo);

		controller.callPlatform(bodyVo);
		
		List<SubrogationCheckVo> warnList = new ArrayList<SubrogationCheckVo>();
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			throw new IllegalArgumentException(resHeadVo.getErrorDesc());
			
		}
		SubrogationCheckResBodyVo resBodyVo = controller.getBodyVo(SubrogationCheckResBodyVo.class);
		
		List<SubrogationCheckDataVo> warnDataList = resBodyVo.getSubrogationCheckDataVoList();
		if(warnDataList ==null || warnDataList.isEmpty() == true){
			return warnList;
		}
		//
		warnList = Beans.copyDepth().from(warnDataList).toList(SubrogationCheckVo.class);
		//先删除再保存
		subrogationAddService.save(warnList);
		
		return warnList;
	}

	/**
	 * 互审确认 查询平台 是 先删除后插入，故不保留数据 
	 * @param queryVo
	 * @throws Exception
	 */
	@Override
	public void sendCheck(SCheckQueryVo queryVo) throws Exception{
		String comCode =queryVo.getComCode();
		if(comCode.startsWith("00")){
			comCode = comCode.substring(0,4)+"0000";
		}else{
			comCode = comCode.substring(0,2)+"000000";
		}
//		String[] areaStr = areaDictService.findAreaByComCode(comCode);
//		if(areaStr.length == 0){
//			throw new IllegalArgumentException("机构地区编码未配置");
//		}
		String areaCode = codeTranService.transCode("ComArea",comCode);
		
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(), areaCode, RequestType.Check);
		CheckReqBodyVo bodyVo = new CheckReqBodyVo();
		CheckBaseVo baseVo = new CheckBaseVo();
	
		baseVo.setAccountsNo(queryVo.getAccountsNo());
		baseVo.setCheckOpinion(queryVo.getCheckOpinion());
		baseVo.setCheckStats(queryVo.getCheckStats());
		bodyVo.setCheckBaseVo(baseVo);

		controller.callPlatform(bodyVo);
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			throw new IllegalArgumentException(resHeadVo.getErrorDesc());
			
		}
	
	}

	/**
	 * 开始追偿确认
	 * @param queryVo
	 * @return
	 * @throws Exception
	 * @modified:
	 * ☆YangKun(2016年3月28日 下午4:47:54): <br>
	 */
	@Override
	public String sendRecoveryConfirmBI(ConfirmQueryVo queryVo) throws Exception{
		
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.RecoveryConfirm);
		BiRecoveryConfirmReqBodyVo bodyVo = new BiRecoveryConfirmReqBodyVo();
		BiRecoveryConfirmBaseVo baseVo = new BiRecoveryConfirmBaseVo();
		double recoveryAmountD = 0D;
		List<PrpLRecoveryOrPayVo> listROP =  platLockService.findRecoveryList(queryVo.getRegistNo(),queryVo.getAccountsNo());
		if(listROP.size()>0&&listROP!=null){
			for (int i=0;i<listROP.size();i++){
				//更新
				//listROP.get(i).setRecoveryCodeStatus("00");
				PrpLRecoveryOrPay lRecoveryOrPay = new PrpLRecoveryOrPay();
//				lRecoveryOrPay =platLockService.findById(listROP.get(i).getId());
//				//subrogationAddService.updateByRecoveryOrPay(listROP.get(i));
//			//	lRecoveryOrPay.setRecoveryCodeStatus("00");
//				platLockService.updateByRecoveryOrPay(lRecoveryOrPay);
				//获取金额
				recoveryAmountD += listROP.get(i).getRecoveryOrPayAmount().doubleValue();
				
			}
		}
		baseVo.setRecoveryCode(queryVo.getAccountsNo());
		baseVo.setRecoveryAmount(recoveryAmountD);
		bodyVo.setBiRecoveryConfirmBaseVo(baseVo);
	
		controller.callPlatform(bodyVo);

		
		List<ConfirmVo> resultList = new ArrayList<ConfirmVo>();
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			String msg = resHeadVo.getErrorDesc();
			//throw new IllegalArgumentException(msg);
			return msg;
		}
		BiRecoveryConfirmResBodyVo resBodyVo = controller.getBodyVo(BiRecoveryConfirmResBodyVo.class);
		
		List<BiRecoveryConfirmDataVo> recoveryDataList = resBodyVo.getBiRecoveryConfirmDataVos();
		//recoveryDataList.get(0).getRecoveryStartTime();获取时间
		
		if(recoveryDataList.get(0).getRecoveryStartTime()!=null){
			List<PrpLPlatLockVo> listLockList =  platLockService.findPlatLockList(queryVo.getRegistNo(),queryVo.getAccountsNo());
			for(PrpLPlatLockVo lockVo : listLockList){
				lockVo.setLockTime(recoveryDataList.get(0).getRecoveryStartTime());
			}
			platLockService.savePlatLockVoList(listLockList);
//			for(int i=0;i<listLockList.size();i++){
//				//更新
//				//listLockVo.get(i).setLockTime(new Date());//修改时间
////				PrpLPlatLock lPlatLock = new PrpLPlatLock();
//				lPlatLock =platLockService.findByLPlatLockId(listLockList.get(i).getId());
//				lPlatLock.setLockTime(recoveryDataList.get(0).getRecoveryStartTime());
//				platLockService.updateByPrplplatlock(lPlatLock);
//			}
		}
		
		if(recoveryDataList ==null || recoveryDataList.isEmpty() == true){
			return "";
		}
		
		//resultList = Beans.copyDepth().from(recoveryDataList).toList(ConfirmVo.class);
		
		return "开始追偿确认成功，结算码："+queryVo.getAccountsNo();
	}
		
	/**
	 * 追偿回款确认
	 * @param basePart
	 * @param comCode
	 * @throws Exception 
	 * @modified:
	 * ☆YangKun(2016年4月8日 下午7:19:24): <br>
	 */
	//@Override
	private void recoveryReturnConfirm(BiRecoveryReturnConfirmResBodyVo bodyVo,String comCode) {
		PlatformController controller = PlatformFactory.getInstance(comCode,RequestType.RecoveryReturnConfirm);
		controller.callPlatform(bodyVo);
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			//String msg = resHeadVo.getErrorDesc();
			//throw new BusinessException("风险预警",msg);
			throw new IllegalArgumentException(resHeadVo.getErrorDesc()	);
		}
	}
	
	
	public List<AccountsInfoVo> sendAccountQuery(String comCode,String recoveryCode) throws Exception{
		AccountsQueryReqBodyVo bodyVo = new AccountsQueryReqBodyVo();
		AccountsQueryBaseVo baseVo = new AccountsQueryBaseVo();
		baseVo.setAccountsNo(recoveryCode);
		bodyVo.setAccountsQueryBaseVo(baseVo);
		return this.sendAccountQuery(comCode, bodyVo);
	}
	
	/**
	 * 结算查询接口
	 * @param recoveryCode
	 * @modified:
	 * ☆YangKun(2016年4月1日 上午11:23:34): <br>
	 */
	//@Override
	private List<AccountsInfoVo> sendAccountQuery(String comCode,AccountsQueryReqBodyVo bodyVo) throws Exception{
		//TODO 根据机构直接查询
		if(comCode.startsWith("00")){
			comCode = comCode.substring(0,4)+"0000";
		}else{
			comCode = comCode.substring(0,2)+"000000";
		}
		
//		String[] areaStr = areaDictService.findAreaByComCode(comCode);
//		if(areaStr.length == 0){
//			throw new IllegalArgumentException("机构地区编码未配置");
//		}
		String areaCode = codeTranService.transCode("ComArea",comCode);
		PlatformController controller = PlatformFactory.getInstance(comCode,areaCode,RequestType.AccountQuery);
		
		controller.callPlatform(bodyVo);
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			String msg = resHeadVo.getErrorDesc();
			throw new IllegalArgumentException(msg);
		}
		
		AccountsQueryResBodyVo resBodyVo = controller.getBodyVo(AccountsQueryResBodyVo.class);
		
		return resBodyVo.getAccountsInfoVos();
	}
	
	
	/**
	 * 代位求偿理赔信息交强查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public SubrogationDataVo sendSubrogationClaimCI(SubrogationQueryVo queryVo)throws Exception {	
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.SubrogationClaimCI);
		//请求基本信息
		CiSubrogationClaimReqBodyVo bodyVo = new CiSubrogationClaimReqBodyVo();
		CiSubrogationClaimBaseVo baseVo = new CiSubrogationClaimBaseVo();
		baseVo.setRecoveryCode(queryVo.getRecoveryCode());
		bodyVo.setClaimPaidBaseVo(baseVo);
		controller.callPlatform(bodyVo);
		SubrogationDataVo recoveryList = new SubrogationDataVo();
		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
		if( !"0000".equals(resHeadVo.getErrorCode())){
			return recoveryList;
		}
		CiSubrogationClaimResBodyVo resBodyVo = controller.getBodyVo(CiSubrogationClaimResBodyVo.class);
			
		//获取报文body
		List<CiSubrogationClaimReopenDataVo> subrogationClaimReopenDataVo= resBodyVo.getClaimReopenDataList();/** 重开赔案信息列表  */
		if(subrogationClaimReopenDataVo!=null){
			List<SubrogationClaimReopenDataVo> subrogationClaimReopenDataVos = Beans.copyDepth().from(subrogationClaimReopenDataVo).toList(SubrogationClaimReopenDataVo.class);
			recoveryList.setSubrogationClaimReopenDataVo(subrogationClaimReopenDataVos);
		}
		List<CiSubrogationRecoveryConfirmDataVo>  subrogationRecoveryConfirmDataVo= resBodyVo.getRecoveryConfirmDataList();/** 追回款信息列表 */
		if(subrogationRecoveryConfirmDataVo!=null){
			List<SubrogationRecoveryConfirmDataVo> subrogationRecoveryConfirmDataVos = Beans.copyDepth().from(subrogationRecoveryConfirmDataVo).toList(SubrogationRecoveryConfirmDataVo.class);
			recoveryList.setSubrogationRecoveryConfirmDataVo(subrogationRecoveryConfirmDataVos);
		}
		
		CiSubrogationReportDataVo subrogationReportDataVo=resBodyVo.getReportDataVo();/**报案信息*/
		//对象转换
		if(subrogationReportDataVo!=null){
			SubrogationReportDataVo subrogationReportData = Beans.copyDepth().from(subrogationReportDataVo).to(SubrogationReportDataVo.class);
			recoveryList.setSubrogationReportDataVo(subrogationReportData);
		}
		CiSubrogationClaimDataVo subrogationClaimDataVo= resBodyVo.getClaimDataVo();/**立案信息*/
		//对象转换
		if(subrogationClaimDataVo!=null){
			SubrogationClaimDataVo subrogationClaimData = Beans.copyDepth().from(subrogationClaimDataVo).to(SubrogationClaimDataVo.class);
			recoveryList.setSubrogationClaimDataVo(subrogationClaimData);
		}
		CiSubrogationCheckDataVo subrogationCheckDataVo=resBodyVo.getCheckDataVo();/**	查勘信息*/
		if(subrogationCheckDataVo!=null){
			SubrogationCheckDataVos subrogationCheckData = Beans.copyDepth().from(subrogationCheckDataVo).to(SubrogationCheckDataVos.class);
			recoveryList.setSubrogationCheckDataVo(subrogationCheckData);
		}
		CiSubrogationEstimateDataVo subrogationEstimateDataVo= resBodyVo.getEstimateDataVo();/**定核损信息*/
		if(subrogationEstimateDataVo!=null){
			SubrogationEstimateDataVo subrogationEstimateData = Beans.copyDepth().from(subrogationEstimateDataVo).to(SubrogationEstimateDataVo.class);
			recoveryList.setSubrogationEstimateDataVo(subrogationEstimateData);
		}
		CiSubrogationDocDataVo subrogationDocDataVo= resBodyVo.getDocDataVo();/** 单证信息 */
		if(subrogationDocDataVo.getDocDetailDataList()!=null){
			List<SubrogationDocDataVo> subrogationDocData = Beans.copyDepth().from(subrogationDocDataVo.getDocDetailDataList()).toList(SubrogationDocDataVo.class);
			recoveryList.setSubrogationDocDataVo(subrogationDocData);
		}
		CiSubrogationClaimCloseDataVo subrogationClaimCloseDataVo= resBodyVo.getClaimCloseDataVo();/**  结案信息  */
		if(subrogationClaimCloseDataVo!=null){
			SubrogationClaimCloseDataVo subrogationClaimCloseData = Beans.copyDepth().from(subrogationClaimCloseDataVo).to(SubrogationClaimCloseDataVo.class);
			recoveryList.setSubrogationClaimCloseDataVo(subrogationClaimCloseData);
		}
		CiSubrogationBasePartVo subrogationBasePartVo= resBodyVo.getSubrogationBaseVo();//基本数据
		if(subrogationBasePartVo!=null){
			SubrogationBasePartVo subrogationBasePart = Beans.copyDepth().from(subrogationBasePartVo).to(SubrogationBasePartVo.class);
			recoveryList.setSubrogationBasePartVo(subrogationBasePart);
		}
		List<CiSubrogationDataVo> subrogationDataVos= resBodyVo.getSubrogationDataList();//代位信息列表
		if(subrogationDataVos!=null){
			List<SubrogationDataVos> subrogationDataVoList = Beans.copyDepth().from(subrogationDataVos).toList(SubrogationDataVos.class);
			recoveryList.setSubrogationDataVos(subrogationDataVoList);
		}
		CiSubrogationUnderWriteDataVo  subrogationUnderWriteDataVo = resBodyVo.getUnderWriteDataVo();/** 理算核赔信息 */
		if(subrogationUnderWriteDataVo!=null){
			List<UndwrtWriteAdjustmentDataVo> s = Beans.copyDepth().from(subrogationUnderWriteDataVo.getAdjustmentDataList()).toList(UndwrtWriteAdjustmentDataVo.class);
			SubrogationUnderWriteDataVo subrogationUnderWriteVo = new SubrogationUnderWriteDataVo();
			subrogationUnderWriteVo.setAdjustmentDataList(s);
			recoveryList.setSubrogationUnderWriteDataVo(subrogationUnderWriteVo);
		}
		return recoveryList;
	}
	
	/**
	 * 代位求偿理赔信息商业查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public SubrogationDataVo sendSubrogationClaimBI(SubrogationQueryVo queryVo)throws Exception {
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(), RequestType.SubrogationClaimBI);
		// 请求基本信息
		BiSubrogationClaimReqBodyVo bodyVo = new BiSubrogationClaimReqBodyVo();
		BiSubrogationClaimBaseVo baseVo = new BiSubrogationClaimBaseVo();
		baseVo.setRecoveryCode(queryVo.getRecoveryCode());
		bodyVo.setBiSubrogationClaimBaseVo(baseVo);
		controller.callPlatform(bodyVo);
		SubrogationDataVo recoveryList = new SubrogationDataVo();
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if (!"0000".equals(resHeadVo.getErrorCode())) {
			return recoveryList;
		}
		BiSubrogationClaimResBodyVo resBodyVo = controller.getBodyVo(BiSubrogationClaimResBodyVo.class);
		// 获取报文body
		List<BiSubrogationClaimReopenDataVo> subrogationClaimReopenDataVo = resBodyVo.getClaimReopenDataList();
		/** 重开赔案信息列表 */
		if (subrogationClaimReopenDataVo != null) {
			List<SubrogationClaimReopenDataVo> subrogationClaimReopenDataVos = Beans.copyDepth().from(subrogationClaimReopenDataVo).toList(SubrogationClaimReopenDataVo.class);
			recoveryList.setSubrogationClaimReopenDataVo(subrogationClaimReopenDataVos);
		}
		List<BiSubrogationRecoveryConfirmDataVo> subrogationRecoveryConfirmDataVo = resBodyVo.getRecoveryConfirmDataList();
		/** 追回款信息列表 */
		if (subrogationRecoveryConfirmDataVo != null) {
			List<SubrogationRecoveryConfirmDataVo> subrogationRecoveryConfirmDataVos = Beans.copyDepth().from(subrogationRecoveryConfirmDataVo).toList(SubrogationRecoveryConfirmDataVo.class);
			recoveryList.setSubrogationRecoveryConfirmDataVo(subrogationRecoveryConfirmDataVos);
		}
		BiSubrogationReportDataVo subrogationReportDataVo = resBodyVo.getReportDataVo();// 报案信息
		// 对象转换
		if (subrogationReportDataVo != null) {
			SubrogationReportDataVo subrogationReportData = Beans.copyDepth().from(subrogationReportDataVo).to(SubrogationReportDataVo.class);
			recoveryList.setSubrogationReportDataVo(subrogationReportData);
		}
		BiSubrogationClaimDataVo subrogationClaimDataVo = resBodyVo.getClaimDataVo();
		/** 立案信息 */
		// 对象转换
		if (subrogationClaimDataVo != null) {
			SubrogationClaimDataVo subrogationClaimData = Beans.copyDepth().from(subrogationClaimDataVo).to(SubrogationClaimDataVo.class);
			recoveryList.setSubrogationClaimDataVo(subrogationClaimData);
		}
		BiSubrogationCheckDataVo subrogationCheckDataVo = resBodyVo.getCheckDataVo();
		/** 查勘信息 */
		if (subrogationCheckDataVo != null) {
			SubrogationCheckDataVos subrogationCheckData = Beans.copyDepth().from(subrogationCheckDataVo).to(SubrogationCheckDataVos.class);
			recoveryList.setSubrogationCheckDataVo(subrogationCheckData);
		}
		BiSubrogationEstimateDataVo subrogationEstimateDataVo = resBodyVo.getEstimateDataVo();
		/** 定核损信息 */
		if (subrogationEstimateDataVo != null) {
			SubrogationEstimateDataVo subrogationEstimateData = Beans.copyDepth().from(subrogationEstimateDataVo).to(SubrogationEstimateDataVo.class);
			recoveryList.setSubrogationEstimateDataVo(subrogationEstimateData);
		}
		BiSubrogationDocDataVo subrogationDocDataVo = resBodyVo.getDocDataVo();
		/** 单证信息 */
		SubrogationDocVo subrogationDocVo = new SubrogationDocVo();
		if (subrogationDocDataVo != null) {
			subrogationDocVo.setSubCertiType(subrogationDocDataVo.getSubCertiType());
			subrogationDocVo.setSubClaimFlag(subrogationDocDataVo.getSubClaimFlag());
			recoveryList.setSubrogationDocVo(subrogationDocVo);
		}
		if (subrogationDocDataVo.getDocDetailDataList() != null) {
			List<SubrogationDocDataVo> subrogationDocData = Beans.copyDepth().from(subrogationDocDataVo.getDocDetailDataList()).toList(SubrogationDocDataVo.class);
			recoveryList.setSubrogationDocDataVo(subrogationDocData);
		}
		BiSubrogationClaimCloseDataVo subrogationClaimCloseDataVo = resBodyVo.getClaimCloseDataVo();
		/** 结案信息 */
		if (subrogationClaimCloseDataVo != null) {
			SubrogationClaimCloseDataVo subrogationClaimCloseData = Beans.copyDepth().from(subrogationClaimCloseDataVo).to(SubrogationClaimCloseDataVo.class);
			recoveryList.setSubrogationClaimCloseDataVo(subrogationClaimCloseData);
		}
		BiSubrogationBasePartVo subrogationBasePartVo = resBodyVo.getSubrogationBaseVo();// 基本数据
		if (subrogationBasePartVo != null) {
			SubrogationBasePartVo subrogationBasePart = Beans.copyDepth().from(subrogationBasePartVo).to(SubrogationBasePartVo.class);
			recoveryList.setSubrogationBasePartVo(subrogationBasePart);
		}
		List<BiSubrogationDataVo> subrogationDataVos = resBodyVo.getSubrogationDataList();// 代位信息列表
		if (subrogationDataVos != null) {
			List<SubrogationDataVos> subrogationDataVoList = Beans.copyDepth().from(subrogationDataVos).toList(SubrogationDataVos.class);
			recoveryList.setSubrogationDataVos(subrogationDataVoList);
		}
		BiSubrogationUnderWriteDataVo subrogationUnderWriteDataVo = resBodyVo.getUnderWriteDataVo();
		/** 理算核赔信息 */
		if (subrogationUnderWriteDataVo != null) {
			if (subrogationUnderWriteDataVo.getAdjustmentDataList() != null) {
				List<BiUndwrtWriteAdjustmentDataVo> adjustmentDataList = subrogationUnderWriteDataVo.getAdjustmentDataList();
				List<UndwrtWriteAdjustmentDataVo> s = Beans.copyDepth().from(adjustmentDataList).toList(UndwrtWriteAdjustmentDataVo.class);
				SubrogationUnderWriteDataVo subrogationUnderWriteVo = new SubrogationUnderWriteDataVo();
				subrogationUnderWriteVo.setAdjustmentDataList(s);
				recoveryList.setSubrogationUnderWriteDataVo(subrogationUnderWriteVo);
			}

		}
		return recoveryList;
	}

	/**
	 * 保单交强查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public CClaimDataVo sendSubrogationBaodanCI(SubrogationQueryVo queryVo)throws Exception {	
		//保单查询
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(),RequestType.SubrogationPolicyCI);
			
		// 请求基本信息
		CiClaimReqBodyVo bodyVo = new CiClaimReqBodyVo();
		CiClaimBaseVo baseVo = new CiClaimBaseVo();
		baseVo.setRecoveryCode(queryVo.getRecoveryCode());
		bodyVo.setCiClaimBaseVo(baseVo);
		controller.callPlatform(bodyVo);
		CClaimDataVo recoveryList = new CClaimDataVo();
		CiResponseHeadVo resHeadVo = controller.getHeadVo(CiResponseHeadVo.class);
		if (!"0000".equals(resHeadVo.getErrorCode())) {
			return recoveryList;
		}
		CiClaimResBodyVo resBodyVo = controller
				.getBodyVo(CiClaimResBodyVo.class);
		// 获取报文body
		CiClaimDataVo ciClaimDataVo = resBodyVo.getCiClaimDataVos();
		recoveryList = Beans.copyDepth().from(ciClaimDataVo).to(CClaimDataVo.class);
		return recoveryList;
	}
			

	/**
	 * 保单商业查询
	 * @param queryVo
	 * @return
	 * @throws Exception
	 */
	@Override
	public ClaimDataVo sendSubrogationBaodanBI(SubrogationQueryVo queryVo)throws Exception {
		PlatformController controller = PlatformFactory.getInstance(queryVo.getComCode(), RequestType.BizSubrogationPolicyBI);
		// 请求基本信息
		BiClaimReqBodyVo bodyVo = new BiClaimReqBodyVo();
		BiClaimBaseVo baseVo = new BiClaimBaseVo();
		baseVo.setRecoveryCode(queryVo.getRecoveryCode());
		bodyVo.setBiClaimBaseVo(baseVo);
		controller.callPlatform(bodyVo);
		ClaimDataVo recoveryList = new ClaimDataVo();
		BiResponseHeadVo resHeadVo = controller.getHeadVo(BiResponseHeadVo.class);
		if (!"0000".equals(resHeadVo.getErrorCode())) {
			return recoveryList;
		}
		BiClaimResBodyVo resBodyVo = controller.getBodyVo(BiClaimResBodyVo.class);
		// 获取报文body
		BiClaimDataVo biClaimDataVo = resBodyVo.getBiClaimDataVos();
		recoveryList = Beans.copyDepth().from(biClaimDataVo).to(ClaimDataVo.class);
		return recoveryList;
	}

	/**
	 * 追偿回款确认
	 * @param basePart
	 * @param comCode
	 * @throws Exception 
	 * @modified:
	 * ☆YangKun(2016年4月8日 下午7:19:24): <br>
	 */
	@Override
	public void recoveryReturnConfirm(String comCode,PrpLPlatLockVo platLockVo,
			String claimSequenceNo, BigDecimal recoverAmount) {
		
		BiRecoveryReturnConfirmResBodyVo body = new BiRecoveryReturnConfirmResBodyVo();
		BiRecoveryReturnConfirmDataVo basePart = new BiRecoveryReturnConfirmDataVo();
		basePart.setClaimNotificationNo(platLockVo.getRegistNo());
		basePart.setClaimSequenceNo(claimSequenceNo);
		basePart.setPayMan(platLockVo.getInsureName());//清付人
		basePart.setRecoverAmount(DataUtils.NullToZero(recoverAmount).doubleValue());
		basePart.setRecoveryCode(platLockVo.getRecoveryCode());
		basePart.setRecoveryOrPayType(platLockVo.getRecoveryOrPayType());// 追偿清付类型
		Integer serialNo = platLockVo.getPrpLRecoveryOrPays().get(0).getSerialNo();
		if(serialNo == null){
			serialNo = 1;
		}
		basePart.setSerialNo(serialNo.toString());
		body.setRecoveryConfirmDataVo(basePart);
		
		this.recoveryReturnConfirm(body, comCode);
		
	}

	@Override
	public List<AccountsInfoVo> sendAccountSearch(String comCode,
			SubrogationQueryVo queryVo) throws Exception {
		
		AccountsQueryReqBodyVo bodyVo = new AccountsQueryReqBodyVo();
		AccountsQueryBaseVo baseVo = new AccountsQueryBaseVo();
		AccountsQueryPacketInfoVo packetInfo = new AccountsQueryPacketInfoVo();
		
		baseVo.setAccountsNo(queryVo.getRecoveryCode());
		baseVo.setAccountsNoStatus(queryVo.getRecoveryCodeStatus());
		baseVo.setAccountDateStart(queryVo.getPayStartTime());
		baseVo.setAccountDateEnd(queryVo.getPayEndTime());
		baseVo.setOppoentCompanyCode(queryVo.getInsurerCode());
		baseVo.setOppoentAreaCode(queryVo.getAreaCode());
		baseVo.setCoverageCode(queryVo.getCoverageType());
		baseVo.setRecoverStatus(queryVo.getRecoverStatus());
		baseVo.setCompAmountEnd(queryVo.getPayAmountMin());
		baseVo.setCompAmountStart(queryVo.getPayAmountMax());
		baseVo.setRecoverAmountEnd(queryVo.getRecoveryAmountMax());
		baseVo.setRecoverAmountStart(queryVo.getRecoveryAmountMin());
		baseVo.setAcrossProvinceFlag(queryVo.getAcrossProvinceFlag());
		packetInfo.setPacketNo("1");
		
		bodyVo.setAccountsQueryBaseVo(baseVo);
		bodyVo.setPacketInfo(packetInfo);
		
		return this.sendAccountQuery(comCode, bodyVo);
	}
}
