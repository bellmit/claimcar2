/******************************************************************************
* CREATETIME : 2016年1月9日 上午10:21:23
******************************************************************************/
package ins.sino.claimcar.flow.constant;

import java.util.ArrayList;
import java.util.List;


/**
 * 工作流节点
 * @author ★LiuPing
 * @CreateTime 2016年1月9日
 */
public enum FlowNode {

	END("完成",null),

	// 以下节点将prpdnode数据粘贴到flowNode.xlsx,然后复制代码过来
	// Java代码(公式生成）
	Regis("报案",null,"5002"),
	Sched("调度",null,"5003"),
	Check("查勘",null,"5006"),
	Chk("查勘","Check","5006"),
	ChkRe("复勘","Check","5006"),
	ChkBig("大案审核",null),
	ChkBig_LV1("大案审核一级","ChkBig","5122"),
	ChkBig_LV2("大案审核二级","ChkBig","5123"),
	ChkBig_LV3("大案审核三级","ChkBig","5124"),
	ChkBig_LV4("大案审核四级","ChkBig","5125"),
	ChkBig_LV5("大案审核五级","ChkBig","5126"),
	Survey("调查",null,"5024"),
	Claim("立案",null,"5014"),
	ClaimBI("立案(商业)","Claim","5015"),
	ClaimCI("立案(交强)","Claim","5015"),
	DLoss("定损",null),
	DLProp("财产定损","DLoss","5007"),
	DLPropMod("财产定损修改","DLProp","5007"),
	DLPropAdd("财产定损追加","DLProp","5007"),
	DLCar("车辆定损","DLoss","5008"),
	DLCars("车辆定损","DLoss","5008"),
	DLCarMod("车辆定损修改","DLCar","5008"),
	DLCarAdd("车辆定损追加","DLCar","5008"),
	DLChk("复检","DLoss","5009"),
	VLoss("核损",null),
	VLCar("车辆核损","VLoss"),
	VLCar_LV0("自动核损","VLCar"),
	VLCar_LV1("车辆核损_一级","VLCar","5052"),
	VLCar_LV2("车辆核损_二级","VLCar","5053"),
	VLCar_LV3("车辆核损_三级","VLCar","5054"),
	VLCar_LV4("车辆核损_四级","VLCar","5055"),
	VLCar_LV5("车辆核损_五级","VLCar","5056"),
	VLCar_LV6("车辆核损_六级","VLCar","5057"),
	VLCar_LV7("车辆核损_七级","VLCar","5058"),
	VLCar_LV8("车辆核损_八级","VLCar","5059"),
	VLCar_LV9("车辆核损_九级","VLCar","5060"),
	VLCar_LV10("车辆核损_十级","VLCar","5061"),
	VLCar_LV11("车辆核损_十一级","VLCar","5062"),
	VLCar_LV12("车辆核损_十二级","VLCar","5063"),
	VLProp("财产核损","VLoss"),
	VLProp_LV0("自动核损","VLProp"),
	VLProp_LV1("财产核损_一级","VLProp","5064"),
	VLProp_LV2("财产核损_二级","VLProp","5065"),
	VLProp_LV3("财产核损_三级","VLProp","5066"),
	VLProp_LV4("财产核损_四级","VLProp","5067"),
	VLProp_LV5("财产核损_五级","VLProp","5068"),
	VLProp_LV6("财产核损_六级","VLProp","5069"),
	VLProp_LV7("财产核损_七级","VLProp","5070"),
	VLProp_LV8("财产核损_八级","VLProp","5071"),
	VLProp_LV9("财产核损_九级","VLProp","5072"),
	VLProp_LV10("财产核损_十级","VLProp","5073"),
	VLProp_LV11("财产核损_十一级","VLProp","5074"),
	VLProp_LV12("财产核损_十二级","VLProp","5075"),
	VPrice("核价",null),
	VPCar("车辆核价","VPrice"),
	VPCar_LV0("自动核价","VPCar"),
	VPCar_LV1("车辆核价_一级","VPCar","5039"),
	VPCar_LV2("车辆核价_二级","VPCar","5040"),
	VPCar_LV3("车辆核价_三级","VPCar","5041"),
	VPCar_LV4("车辆核价_四级","VPCar","5042"),
	VPCar_LV5("车辆核价_五级","VPCar","5043"),
	VPCar_LV6("车辆核价_六级","VPCar","5044"),
	VPCar_LV7("车辆核价_七级","VPCar","5045"),
	VPCar_LV8("车辆核价_八级","VPCar","5046"),
	VPCar_LV9("车辆核价_九级","VPCar","5047"),
	VPCar_LV10("车辆核价_十级","VPCar","5048"),
	VPCar_LV11("车辆核价_十一级","VPCar","5049"),
	VPCar_LV12("车辆核价_十二级","VPCar","5050"),
	PLoss("人伤",null),
	PLFirst("人伤首次跟踪","PLoss","5012"),
	PLNext("人伤后续跟踪","PLoss","5012"),
	PLVerify("人伤跟踪审核","PLoss"),
	PLVerify_LV1("人伤跟踪审核_一级","PLVerify","5076"),
	PLVerify_LV2("人伤跟踪审核_二级","PLVerify","5077"),
	PLVerify_LV3("人伤跟踪审核_三级","PLVerify","5078"),
	PLVerify_LV4("人伤跟踪审核_四级","PLVerify","5079"),
	PLVerify_LV5("人伤跟踪审核_五级","PLVerify","5080"),
	PLVerify_LV6("人伤跟踪审核_六级","PLVerify","5081"),
	PLVerify_LV7("人伤跟踪审核_七级","PLVerify","5082"),
	PLVerify_LV8("人伤跟踪审核_八级","PLVerify","5083"),
	PLVerify_LV9("人伤跟踪审核_九级","PLVerify","5084"),
	PLVerify_LV10("人伤跟踪审核_十级","PLVerify","5085"),
	PLVerify_LV11("人伤跟踪审核_十一级","PLVerify","5086"),
	PLVerify_LV12("人伤跟踪审核_十二级","PLVerify","5087"),
	PLCharge("人伤费用审核","PLoss"),
	PLCharge_LV0("人伤费用自动审核","PLCharge","5186"),
	PLCharge_LV1("人伤费用审核_一级","PLCharge","5088"),
	PLCharge_LV2("人伤费用审核_二级","PLCharge","5089"),
	PLCharge_LV3("人伤费用审核_三级","PLCharge","5090"),
	PLCharge_LV4("人伤费用审核_四级","PLCharge","5091"),
	PLCharge_LV5("人伤费用审核_五级","PLCharge","5092"),
	PLCharge_LV6("人伤费用审核_六级","PLCharge","5093"),
	PLCharge_LV7("人伤费用审核_七级","PLCharge","5094"),
	PLCharge_LV8("人伤费用审核_八级","PLCharge","5095"),
	PLCharge_LV9("人伤费用审核_九级","PLCharge","5096"),
	PLCharge_LV10("人伤费用审核_十级","PLCharge","5097"),
	PLCharge_LV11("人伤费用审核_十一级","PLCharge","5098"),
	PLCharge_LV12("人伤费用审核_十二级","PLCharge","5099"),
	PLChargeMod("费用审核修改","PLoss","5013"),
	PLBig("大案审核",null),
	PLBig_LV1("大案审核一级","PLBig","5127"),
	PLBig_LV2("大案审核二级","PLBig","5128"),
	PLBig_LV3("大案审核三级","PLBig","5129"),
	PLBig_LV4("大案审核四级","PLBig","5130"),
	PLBig_LV5("大案审核五级","PLBig","5131"),
	Certi("单证",null,"5017"),
	Compe("理算",null,"5019"),
	CompeBI("理算(商业)","Compe","5019"),
	CompeCI("理算(交强)","Compe","5019"),
	CompeWf("理算冲销",null,"5019"),
	CompeWfBI("理算冲销(商业)","CompeWf","5019"),
	CompeWfCI("理算冲销(交强)","CompeWf","5019"),
	VClaim("核赔",null),
	VClaim_LV1("核赔_一级","VClaim","5106"),
	VClaim_LV2("核赔_二级","VClaim","5107"),
	VClaim_LV3("核赔_三级","VClaim","5108"),
	VClaim_LV4("核赔_四级","VClaim","5109"),
	VClaim_LV5("核赔_五级","VClaim","5110"),
	VClaim_LV6("核赔_六级","VClaim","5111"),
	VClaim_LV7("核赔_七级","VClaim","5112"),
	VClaim_LV8("核赔_八级","VClaim","5113"),
	VClaim_LV9("核赔_九级","VClaim","5114"),
	VClaim_LV10("核赔_十级","VClaim","5115"),
	VClaim_LV11("核赔_十一级","VClaim","5116"),
	VClaim_LV12("核赔_十二级","VClaim","5117"),
	EndCas("结案",null,"5021"),
	ReOpen("重开赔案",null),
	ReOpenApp("重开赔案登记","ReOpen","5021"),
	ReOpenVrf("重开赔案审核","ReOpen"),
	ReOpenVrf_LV1("重开赔案审核_一级","ReOpenVrf","5118"),
	ReOpenVrf_LV2("重开赔案审核_二级","ReOpenVrf","5119"),
	ReOpenVrf_LV3("重开赔案审核_三级","ReOpenVrf","5120"),
	ReOpenVrf_LV4("重开赔案审核_四级","ReOpenVrf","5121"),
	Cancel("注销/拒赔",null),
	VCalClaim("核赔注销？",null),
	CancelApp("注销/拒赔申请","Cancel","5015"),
	CancelAppJuPei("拒赔申请","Cancel"),
	CancelVrf("注销审核","Cancel"),
	CancelVrf_LV("注销发起","Cancel"),
	CancelVrf_LV1("注销分公司审核","Cancel","5103"),
	CancelVrf_LV2("注销分公司二级审核","Cancel","5104"),
	CancelVrf_LV3("注销分公司审核","Cancel"),
	CancelLVrf("注销审核","Cancel"),
	CancelLVrf_LV1("注销总公司审核","Cancel","5102"),
	CancelLVrf_LV2("注销总公司审核","Cancel"),
	CancelLVrf_LV3("注销总公司审核","Cancel"),
	ReCanApp("注销/拒赔恢复申请","Cancel"),
	ReCanVrf("注销/拒赔恢复审核","Cancel"),
	ReCanVrf_LV1("注销恢复分公司审核","Cancel","5103"),
	ReCanVrf_LV2("注销/拒赔恢复二级审核","Cancel","5104"),
	ReCanVrf_LV3("注销/拒赔恢复审核","Cancel"),
	ReCanLVrf("注销/拒赔恢复审核","Cancel"),
	ReCanLVrf_LV11("注销恢复总公司审核","Cancel","5105"),
	ReCanLVrf_LV12("注销/拒赔恢复审核","Cancel"),
	ReCanLVrf_LV13("注销/拒赔恢复审核","Cancel"),
	PrePay("预付",null,"5019"),
	PrePayBI("预付(商业)","PrePay","5019"),
	PrePayCI("预付(交强)","PrePay","5019"),
	PrePayWf("预付冲销",null,"5019"),
	PrePayWfBI("预付冲销(商业)","PrePayWf","5019"),
	PrePayWfCI("预付冲销(交强)","PrePayWf","5019"),
	PadPay("垫付",null,"5019"),
	RecPay("追偿",null,"5023"),
	RecLoss("损余回收",null),
	RecLossCar("车辆损余回收","RecLoss","5028"),
	RecLossProp("财产损余回收","RecLoss","5028"),
	Handover("平级移交","null","5027"),
	SHandover("超级平级移交","null","5170"),
	Interm("公估费",null,"5133"),
	CheckFee("查勘费",null,"5132"),
	Refund("退票",null,"5177"),
	IntermQuery("公估费查询","Interm","5133"),
	IntermTaskQuery("公估费任务查询","Interm","5133"),
	IntermCheckQuery("公估费审核查询","Interm","5133"),
	CheckFeeQuery("查勘费查询","CheckFee","5132"),
	CheckFeeTaskQuery("查勘费任务查询","CheckFee","5132"),
	CheckFeeCheckQuery("查勘费审核查询","CheckFee","5132"),
	VClaim_CI_LV0("交强自动核赔","VClaim"),
	VClaim_CI_LV1("交强核赔一级","VClaim","5134"),
	VClaim_CI_LV2("交强核赔二级","VClaim","5135"),
	VClaim_CI_LV3("交强核赔三级","VClaim","5136"),
	VClaim_CI_LV4("交强核赔四级","VClaim","5137"),
	VClaim_CI_LV5("交强核赔五级","VClaim","5138"),
	VClaim_CI_LV6("交强核赔六级","VClaim","5139"),
	VClaim_CI_LV7("交强核赔七级","VClaim","5140"),
	VClaim_CI_LV8("交强核赔八级","VClaim","5141"),
	VClaim_CI_LV9("交强核赔九级","VClaim","5142"),
	VClaim_CI_LV10("交强核赔十级","VClaim","5143"),
	VClaim_CI_LV11("交强核赔十一级","VClaim","5144"),
	VClaim_CI_LV12("交强核赔十二级","VClaim","5145"),
	VClaim_BI_LV0("商业自动核赔","VClaim"),
	VClaim_BI_LV1("商业核赔一级","VClaim","5146"),
	VClaim_BI_LV2("商业核赔二级","VClaim","5147"),
	VClaim_BI_LV3("商业核赔三级","VClaim","5148"),
	VClaim_BI_LV4("商业核赔四级","VClaim","5149"),
	VClaim_BI_LV5("商业核赔五级","VClaim","5150"),
	VClaim_BI_LV6("商业核赔六级","VClaim","5151"),
	VClaim_BI_LV7("商业核赔七级","VClaim","5152"),
	VClaim_BI_LV8("商业核赔八级","VClaim","5153"),
	VClaim_BI_LV9("商业核赔九级","VClaim","5154"),
	VClaim_BI_LV10("商业核赔十级","VClaim","5155"),
	VClaim_BI_LV11("商业核赔十一级","VClaim","5156"),
	VClaim_BI_LV12("商业核赔十二级","VClaim","5157"),
	VClaim_Padpay_LV1("垫付核赔一级","VClaim","5158"),
	VClaim_Padpay_LV2("垫付核赔二级","VClaim","5159"),
	VClaim_Padpay_LV3("垫付核赔三级","VClaim","5160"),
	VClaim_Padpay_LV4("垫付核赔四级","VClaim","5161"),
	VClaim_Padpay_LV5("垫付核赔五级","VClaim","5162"),
	VClaim_Padpay_LV6("垫付核赔六级","VClaim","5163"),
	VClaim_Padpay_LV7("垫付核赔七级","VClaim","5164"),
	VClaim_Padpay_LV8("垫付核赔八级","VClaim","5165"),
	VClaim_Padpay_LV9("垫付核赔九级","VClaim","5166"),
	VClaim_Padpay_LV10("垫付核赔十级","VClaim","5167"),
	VClaim_Padpay_LV11("垫付核赔十一级","VClaim","5168"),
	VClaim_Padpay_LV12("垫付核赔十二级","VClaim","5169"),
	PLTrack("查勘跟踪审核","PLoss"),
	PLTrack_LV1("查勘跟踪审核_一级","PLTrack","5187"),
	PLTrack_LV2("查勘跟踪审核_二级","PLTrack","5188"),
	PLInjured("人伤跟踪审核","PLoss"),
	PLInjured_LV1("人伤跟踪审核_一级","PLInjured","5189"),
	PLInjured_LV2("人伤跟踪审核_二级","PLInjured","5190"),

	// 新增代码时请，先到prpdnode添加数据数据，再粘贴到flowNode.xlsx,然后复制代码过来

	;

	private String name;
	private String upperNode;
	private String roleCode;// 这个节点的角色代码

	private FlowNode(String name,String upperNode){
		this.name = name;
		this.upperNode = upperNode;
	}

	private FlowNode(String name,String upperNode,String roleCode){
		this.name = name;
		this.upperNode = upperNode;
		this.roleCode = roleCode;
	}

	public String getName() {
		return name;
	}

	public String getUpperNode() {
		return upperNode;
	}

	/**
	 * 重载equals方法
	 * @param nodeCode
	 *  ☆LiuPing(2016年1月10日 下午10:14:37): <br>
	 */
	public boolean equals(String nodeCode) {
		return this.name().equals(nodeCode);
	}
	
	/**
	 * 通过当前字节对应父节点下所以的子节点
	 * @param nodeCode
	 * @return
	 * @author ☆luows(2018年1月30日 下午3:13:34): <br>
	 */
	public List<FlowNode> getNextLevelNodes() {
		FlowNode[] all = FlowNode.values();
		List<FlowNode> flowNodeList = new ArrayList<FlowNode>();
		for(FlowNode node : all){
			if(node.upperNode!=null&&!"".equals(node.upperNode)){
				if(node.upperNode.equals(this.getUpperNode())){
					flowNodeList.add(node);
				}
			}
		}
		return flowNodeList;
	}

	public List<FlowNode> getChildrenNodes() {
		FlowNode[] all = FlowNode.values();
		List<FlowNode> flowNodeList = new ArrayList<FlowNode>();
		for(FlowNode node : all){
			if(node.upperNode!=null&&!"".equals(node.upperNode)){
				if(node.upperNode.equals(this.name())){
					flowNodeList.add(node);
				}
			}
		}
		return flowNodeList;
	}
	
	/**
	 * 获取这个节点最顶层节点
	 * @return
	 * @modified: ☆LiuPing(2016年2月2日 ): <br>
	 */
	public FlowNode getRootNode() {
		if(this.getUpperNode()==null){
			return this;
		}else{
			FlowNode node = FlowNode.valueOf(this.getUpperNode());
			return node.getRootNode();
		}
	}

	public String getRoleCode() {
		return roleCode;
	}

}
