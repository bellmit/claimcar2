/******************************************************************************
 * CREATETIME : 2016年1月21日 下午2:59:08
 ******************************************************************************/
package ins.sino.claimcar.flow.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月21日
 */
public class TaskQueryUtils {

	private static final Map<String,String> TaskHandUrlMap = new HashMap<String,String>();

	static{
		TaskHandUrlMap.put("Regis","/claimcar/regist/edit.do");
		TaskHandUrlMap.put("Sched","/claimcar/schedule/preScheduleEdit.do");
		TaskHandUrlMap.put("Check","/claimcar/check/initCheck.do");
		TaskHandUrlMap.put("Chk","/claimcar/check/initCheck.do");
		TaskHandUrlMap.put("ChkRe","/claimcar/check/initCheck.do");
		TaskHandUrlMap.put("ChkBig","/claimcar/check/initCheck.do");
		TaskHandUrlMap.put("DLCar","/claimcar/defloss/preAddDefloss.do");
		TaskHandUrlMap.put("DLoss","/claimcar/defloss/preAddDefloss.do");
		TaskHandUrlMap.put("DLChk","/claimcar/defloss/preAddDefloss.do");
		TaskHandUrlMap.put("DLCarMod","/claimcar/defloss/preAddDefloss.do");
		TaskHandUrlMap.put("DLCarAdd","/claimcar/defloss/preAddDefloss.do");
		TaskHandUrlMap.put("DLProp","/claimcar/proploss/initPropCertainLoss.do");
		TaskHandUrlMap.put("DLPropAdd","/claimcar/proploss/initPropCertainLoss.do");
		TaskHandUrlMap.put("DLPropMod","/claimcar/proploss/initPropCertainLoss.do");
		TaskHandUrlMap.put("PLFirst","/claimcar/persTraceEdit/persTraceEdit.do");
		TaskHandUrlMap.put("PLNext","/claimcar/persTraceEdit/persTraceEdit.do");
		TaskHandUrlMap.put("PLBig","/claimcar/persTracePLBig/persTracePLBig.do");//
		TaskHandUrlMap.put("PLoss","/claimcar/persTraceEdit/persTraceEdit.do");//
		TaskHandUrlMap.put("PLVerify","/claimcar/persTraceVerify/personTraceVerify.do");
		TaskHandUrlMap.put("PLCharge","/claimcar/persTraceVerify/personTraceVerify.do");
		TaskHandUrlMap.put("VLCar","/claimcar/defloss/preAddVerifyLoss.do");
		TaskHandUrlMap.put("VLProp","/claimcar/proploss/initPropVerifyLoss.do");
		TaskHandUrlMap.put("VPCar","/claimcar/defloss/preAddVerifyPrice.do");
		TaskHandUrlMap.put("PolicyView","/claimcar/policyView/policyView.do");
		TaskHandUrlMap.put("Survey","/claimcar/survey/init.do"); // initSurvey
		TaskHandUrlMap.put("RecLoss","/claimcar/recLoss/init.do");
		TaskHandUrlMap.put("RecLossCar","/claimcar/recLoss/init.do");
		TaskHandUrlMap.put("RecLossProp","/claimcar/recLoss/init.do");
		TaskHandUrlMap.put("Certi","/claimcar/certify/init.do");
		TaskHandUrlMap.put("ClaimCI","/claimcar/claim/claimEdit.do");
		TaskHandUrlMap.put("ClaimBI","/claimcar/claim/claimEdit.do");
		TaskHandUrlMap.put("Claim","/claimcar/claim/claimView.do");
		//平级移交URL
		TaskHandUrlMap.put("Handover", "/claimcar/handoverTask/handoverTaskEdit.do");
		TaskHandUrlMap.put("SHandover", "/claimcar/handoverTask/handoverTaskEdit.do");
		TaskHandUrlMap.put("VClaim", "/claimcar/verifyClaim/verifyClaimEdit.do");//核赔
		TaskHandUrlMap.put("Compe", "/claimcar/compensate/compensateEdit.do");//理算
		TaskHandUrlMap.put("CompeBI", "/claimcar/compensate/compensateEdit.do");//理算
		TaskHandUrlMap.put("CompeCI", "/claimcar/compensate/compensateEdit.do");//理算
		TaskHandUrlMap.put("CompeWf", "/claimcar/compensate/compeWriteOffLaunch.do");//理算冲销
		TaskHandUrlMap.put("CompeWfBI", "/claimcar/compensate/compeWriteOffLaunch.do");//理算冲销商业
		TaskHandUrlMap.put("CompeWfCI", "/claimcar/compensate/compeWriteOffLaunch.do");//理算冲销交强
		
		TaskHandUrlMap.put("PadPay", "/claimcar/padPay/padPayEdit.do");//垫付

		TaskHandUrlMap.put("EndCas", "/claimcar/padPay/padPayEdit.do");//结案
		


		TaskHandUrlMap.put("PrePay", "/claimcar/prePay/prePayApplyEdit.do");
		TaskHandUrlMap.put("PrePayBI", "/claimcar/prePay/prePayApplyEdit.do");
		TaskHandUrlMap.put("PrePayCI", "/claimcar/prePay/prePayApplyEdit.do");
		TaskHandUrlMap.put("PrePayWf", "/claimcar/prePay/prePayWf.do");
		TaskHandUrlMap.put("PrePayWfCI", "/claimcar/prePay/prePayWf.do");
		TaskHandUrlMap.put("PrePayWfBI", "/claimcar/prePay/prePayWf.do");
		TaskHandUrlMap.put("EndCas", "/claimcar/endCase/endCaseEdit.do");
		TaskHandUrlMap.put("RecPay", "/claimcar/recPay/recPayEdit.do");

		TaskHandUrlMap.put("CancelVrf_LV1", "/claimcar/claim/claimCancelCheckInit.do");//注销拒赔
		TaskHandUrlMap.put("CancelVrf_LV2", "/claimcar/claim/claimCancelCheckInit.do");//注销拒赔
		TaskHandUrlMap.put("CancelLVrf_LV1", "/claimcar/claim/claimCancelCheckInit.do");//注销拒赔
		TaskHandUrlMap.put("ReCanVrf_LV1", "/claimcar/claim/claimCancelCheckInit.do");//恢复拒赔
		TaskHandUrlMap.put("ReCanVrf_LV2", "/claimcar/claim/claimCancelCheckInit.do");//恢复拒赔
		TaskHandUrlMap.put("ReCanLVrf_LV11", "/claimcar/claim/claimCancelCheckInit.do");//恢复拒赔
		TaskHandUrlMap.put("CancelVrf", "/claimcar/claim/claimCancelCheckInit.do");//注销拒赔
		TaskHandUrlMap.put("CancelLVrf", "/claimcar/claim/claimCancelCheckInit.do");//注销拒赔
		TaskHandUrlMap.put("ReCanVrf", "/claimcar/claimRecover/claimCancelCheckInit.do");//注销拒赔
		TaskHandUrlMap.put("ReCanLVrf", "/claimcar/claimRecover/claimCancelCheckInit.do");
		TaskHandUrlMap.put("ReOpenVrf", "/claimcar/reOpen/reOpenCheckEdit.do");
		TaskHandUrlMap.put("ReOpenApp", "/claimcar/reOpen/reOpenApp.do");
		TaskHandUrlMap.put("CancelApp", "/claimcar/claim/claimCancelInit.do");//申请节点
		TaskHandUrlMap.put("ReCanApp", "/claimcar/claimRecover/claimCancelInit.do");//申请节点
		TaskHandUrlMap.put("CancelAppJuPei", "/claimcar/claim/claimCancelInit.do");//申请节点
	}
	public static String getTaskHandUrl(String nodeCode,String subNodeCode) {
		String url = null;
		if(StringUtils.isNotBlank(subNodeCode)){
			url = TaskHandUrlMap.get(subNodeCode);
			System.out.println("TaskHandUrlMap1"+subNodeCode+url);
		}
		if(url==null) {
			url = TaskHandUrlMap.get(nodeCode);
			System.out.println("TaskHandUrlMap2"+url);
		}

		if(url==null) throw new IllegalArgumentException(subNodeCode+"节点URL未配置："+nodeCode);
		return url;
	}

}
