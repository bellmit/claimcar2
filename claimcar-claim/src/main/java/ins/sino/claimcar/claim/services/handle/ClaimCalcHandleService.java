/**
 * 立案计算
 */
package ins.sino.claimcar.claim.services.handle;

import java.math.BigDecimal;

import ins.sino.claimcar.claim.vo.ClaimCalcKindVo;

/**
 * 立案计算
 * @author lizhaoyang
 *
 */
public class ClaimCalcHandleService {
	
	
	/**
	 * 车损险 非代位计算
	 * 全损：
	 *  车损险赔付   =（保险金额-交强险应赔付本车损失金额 ）*责任比例*（1-责任免赔率）*(1-绝对免赔率之和)-绝对免赔额
     *  车损险不计免赔险赔付   = （保险金额-交强险应赔付本车损失金额 ）*责任比例 * 责任免赔率
     * 部分损失：
     * 	车损险赔付   =（实际修复费用-交强险应赔付本车损失金额 ）*责任比例*（1-责任免赔率）*(1-绝对免赔率之和)-绝对免赔额
     *  车损险不计免赔险赔付   = （实际修复费用-交强险应赔付本车损失金额 ）*责任比例 * 责任免赔率
     * 实际赔付：
     *  本车总赔付 = 车损险赔付 +车损险不计免赔险赔付
	 * */
	private static void calKind_A_1206_Normal(ClaimCalcKindVo kind){
		double calcAmt=0;
		StringBuffer calNote=new StringBuffer("");//险别计算公式
		if( "1".equals(kind.getAllLossFlag())){
			calNote.append("(保险金额-交强险应赔付本车损失金额-残值 )*责任比例*(1-责任免赔率)*(1-绝对免赔率之和)-绝对免赔额");
		}
		else{
			calNote.append("(实际修复费用-交强险应赔付本车损失金额-残值)*责任比例*(1-责任免赔率)*(1-绝对免赔率之和)-绝对免赔额");
		}
		kind.setFormulaNote(calNote.toString());
		
		double lossAmt=kind.getLossAmt();
		if( kind.getLossAmt()>kind.getAmount())
			lossAmt=kind.getAmount();//流程中应该已有控制
		
	//责任比例    对方交强险赔付本车，有可能赔付100（对方无责），也有可能赔付2000（对方有责）.
		calcAmt = new BigDecimal(lossAmt-kind.getBZPaid()-kind.getRestAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*kind.getIndemnityDutyRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*(1-kind.getDutyDeductibleRate())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*(1-kind.getSelectDeductibleRate())-kind.getDeductibleAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		kind.setCalcAmt(calcAmt);
		
		StringBuffer calProc=new StringBuffer("");//险别计算公式
		calProc.append("(").append(lossAmt).append("-").append(kind.getBZPaid()).append("-").append(kind.getRestAmt()).append(")");
		calProc.append("*").append(kind.getIndemnityDutyRate());
		calProc.append("*(1-").append(kind.getIndemnityDutyRate()).append(")");
		calProc.append("*(1-").append(kind.getSelectDeductibleRate()).append(")");
		calProc.append("-").append(kind.getDeductibleAmt());
		kind.setFormulaProc(calProc.toString());
		
	//不计免赔的公式 	
		if("1".equals(kind.getExceptFlag())){
			double exceptCalcAmt=new BigDecimal((lossAmt-kind.getBZPaid()-kind.getRestAmt())*kind.getIndemnityDutyRate()*kind.getExceptDeductibleRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			StringBuffer exceptCalNote=new StringBuffer();
			if( "1".equals(kind.getAllLossFlag())){//全损
				exceptCalNote.append("(保险金额-交强险应赔付本车损失金额-残值)*责任比例*不计免赔率");
			}
			else{
				exceptCalNote.append("(实际修复费用-交强险应赔付本车损失金额-残值)*责任比例*不计免赔率");
			}
			
			StringBuffer exceptCalProc=new StringBuffer();
			exceptCalProc.append("(").append(lossAmt).append("-").append(kind.getBZPaid()).append("-").append(kind.getRestAmt()).append(")");
			exceptCalProc.append("*").append(kind.getIndemnityDutyRate());
			exceptCalProc.append("*").append(kind.getExceptDeductibleRate());
			
			kind.setExceptCalcAmt(exceptCalcAmt);
			kind.setExceptFormulaNote(exceptCalNote.toString());
			kind.setExceptFormulaProc(exceptCalProc.toString());
		}
	
	}
	
	/**
	 * 车损险代位计算
	 * 全损：
	 *  车损险赔付   =（保险金额-被保险人已从第三方获得的车损赔偿金额 ）*责任比例*（1-责任免赔率）*(1-绝对免赔率之和)-绝对免赔额
     *  车损险不计免赔险赔付   = （保险金额-被保险人已从第三方获得的车损赔偿金额 ）*责任比例 * 责任免赔率
     * 部分损失：
     * 	车损险赔付   =（实际修复费用-被保险人已从第三方获得的车损赔偿金额 ）*责任比例*（1-责任免赔率）*(1-绝对免赔率之和)-绝对免赔额
     *  车损险不计免赔险赔付   = （实际修复费用-被保险人已从第三方获得的车损赔偿金额）*责任比例 * 责任免赔率
     * 实际赔付：
     *  本车总赔付 = 车损险赔付 +车损险不计免赔险赔付
	 *  lizy
	 *  2016-03-10
	 * */
	public static void calKind_A_1206_SubrogationAll(ClaimCalcKindVo kind){
		double calcAmt=0;
		StringBuffer calNote=new StringBuffer("");//险别计算公式
		if( "1".equals(kind.getAllLossFlag())){
			calNote.append("(保险金额－被保险人已从第三方获得的车损赔偿金额-残值)*(1-责任免赔率)*(1-绝对免赔率之和)-绝对免赔额");
		}
		else{
			calNote.append("(实际修复费用－被保险人已从第三方获得的车损赔偿金额-残值)*(1-责任免赔率)*(1-绝对免赔率之和)-绝对免赔额");
		}
		kind.setFormulaNote(calNote.toString());
		
		double lossAmt=kind.getLossAmt();
		if( kind.getLossAmt()>kind.getAmount())
			lossAmt=kind.getAmount();//流程中应该已有控制
		
	//责任比例 
		calcAmt = new BigDecimal(lossAmt-kind.getBZPaid()-kind.getRestAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*(1-kind.getDutyDeductibleRate())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*(1-kind.getSelectDeductibleRate())-kind.getDeductibleAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		kind.setCalcAmt(calcAmt);
		
		StringBuffer calProc=new StringBuffer("");//险别计算公式
		calProc.append("(").append(lossAmt).append("-").append(kind.getBZPaid()).append("-").append(kind.getRestAmt()).append(")");
		calProc.append("*(1-").append(kind.getIndemnityDutyRate()).append(")");
		calProc.append("*(1-").append(kind.getSelectDeductibleRate()).append(")");
		calProc.append("-").append(kind.getDeductibleAmt());
		kind.setFormulaProc(calProc.toString());
		
	//不计免赔的公式 	
		if("1".equals(kind.getExceptFlag())){
			double exceptCalcAmt=new BigDecimal((lossAmt-kind.getBZPaid()-kind.getRestAmt())*kind.getExceptDeductibleRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			StringBuffer exceptCalNote=new StringBuffer();
			if( "1".equals(kind.getAllLossFlag())){
				exceptCalNote.append("(保险金额－被保险人已从第三方获得的车损赔偿金额-残值)*不计免赔率");
			}
			else{
				exceptCalNote.append("(实际修复费用－被保险人已从第三方获得的车损赔偿金额-残值)*不计免赔率");
			}
			
			
			StringBuffer exceptCalProc=new StringBuffer();
			exceptCalProc.append("(").append(lossAmt).append("-").append(kind.getBZPaid()).append("-").append(kind.getRestAmt()).append(")");
			exceptCalProc.append("*").append(kind.getExceptDeductibleRate());
			
			kind.setExceptCalcAmt(exceptCalcAmt);
			kind.setExceptFormulaNote(exceptCalNote.toString());
			kind.setExceptFormulaProc(exceptCalProc.toString());
		}		
		
	}
	
	/**
	 * 车损险代位计算
	 * 总赔付，自担，代位
	 *  lizy
	 *  2016-03-10
	 * */
	public static void calKind_A_1206_Subrogation(ClaimCalcKindVo kind){
		calKind_A_1206_Normal(kind);
		double tokenAmt=kind.getCalcAmt();
		double exceptTokenAmt=kind.getExceptCalcAmt();
		//总赔付部分
		calKind_A_1206_SubrogationAll(kind);
		double allAmt=kind.getCalcAmt();
		double exceptAllAmt=kind.getExceptCalcAmt();
		//自担部分
		kind.setTakeonPay(tokenAmt);
		kind.setExceptTakeonPay(exceptTokenAmt);
		//代位部分
		double subrogationPay=new BigDecimal(allAmt-tokenAmt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double exceptSubrogationPay=new BigDecimal(exceptAllAmt-exceptTokenAmt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		kind.setSubrogationPay(subrogationPay);
		kind.setExceptSubrogationPay(exceptSubrogationPay);
	}
	

	/**
	 * 车损险 非代位计算
	 * 全损：
	 *  车损险赔付   =（保险金额-交强险应赔付本车损失金额 ）*责任比例*（1-免赔率之和)-绝对免赔额
     *  车损险不计免赔险赔付   = （保险金额-交强险应赔付本车损失金额 ）*(保险金额/新车购置价)*责任比例 * 责任免赔率
     * 部分损失：
     * 	车损险赔付   =（实际修复费用-交强险应赔付本车损失金额 ）*责任比例*（1-免赔率之和)-绝对免赔额
     *  车损险不计免赔险赔付   = （实际修复费用-交强险应赔付本车损失金额 ）(保险金额/新车购置价)*责任比例 * 责任免赔率
     * 实际赔付：
     *  本车总赔付 = 车损险赔付 +车损险不计免赔险赔付
	 * */
	private static void calKind_A_1201_Normal(ClaimCalcKindVo kind){
		double calcAmt=0;
		double claimRate=1.0;
		if( kind.getAmount()<kind.getPureCarPrice()){//不足额赔付率
			claimRate=new BigDecimal(kind.getAmount()/kind.getPureCarPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if( claimRate>1)
				claimRate=1.0;
		}
		
		StringBuffer calNote=new StringBuffer("");//险别计算公式
		if( "1".equals(kind.getAllLossFlag())){
			calNote.append("(保险金额-交强险应赔付本车损失金额-残值 )*责任比例*(1-免赔率之和)-绝对免赔额");
		}
		else{
			calNote.append("(实际修复费用-交强险应赔付本车损失金额-残值)*(保险金额/新车购置价)*责任比例*(1-免赔率之和)-绝对免赔额");
		}
		kind.setFormulaNote(calNote.toString());
		
		double lossAmt=kind.getLossAmt();
		if( kind.getLossAmt()>kind.getAmount())
			lossAmt=kind.getAmount();//流程中应该已有控制
		
	//责任比例    对方交强险赔付本车，有可能赔付100（对方无责），也有可能赔付2000（对方有责）.
		calcAmt = new BigDecimal(lossAmt-kind.getBZPaid()-kind.getRestAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*claimRate*kind.getIndemnityDutyRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*(1-kind.getDutyDeductibleRate()-kind.getSelectDeductibleRate())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt-kind.getDeductibleAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		kind.setCalcAmt(calcAmt);
		
		StringBuffer calProc=new StringBuffer("");//险别计算公式
		calProc.append("(").append(lossAmt).append("-").append(kind.getBZPaid()).append("-").append(kind.getRestAmt()).append(")");
		calProc.append("*(").append(kind.getAmount()).append("/").append(kind.getPureCarPrice()).append(")");
		if( ! "1".equals(kind.getAllLossFlag())){//非全损
			calProc.append("*(").append(kind.getAmount()).append("/").append(kind.getPureCarPrice()).append(")");
		}
		calProc.append("*").append(kind.getIndemnityDutyRate());
		calProc.append("*(1-").append(kind.getIndemnityDutyRate()+kind.getSelectDeductibleRate()).append(")");
		calProc.append("-").append(kind.getDeductibleAmt());
		kind.setFormulaProc(calProc.toString());
		
	//不计免赔的公式 	
		if("1".equals(kind.getExceptFlag())){
			double exceptCalcAmt=new BigDecimal((lossAmt-kind.getBZPaid()-kind.getRestAmt())*claimRate*kind.getIndemnityDutyRate()*kind.getExceptDeductibleRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			StringBuffer exceptCalNote=new StringBuffer();
			if( "1".equals(kind.getAllLossFlag())){//全损
				exceptCalNote.append("(保险金额-交强险应赔付本车损失金额-残值)*责任比例*不计免赔率");
			}
			else{
				exceptCalNote.append("(实际修复费用-交强险应赔付本车损失金额-残值)*(保险金额/新车购置价)*责任比例*不计免赔率");
			}
			
			StringBuffer exceptCalProc=new StringBuffer();
			exceptCalProc.append("(").append(lossAmt).append("-").append(kind.getBZPaid()).append("-").append(kind.getRestAmt()).append(")");
			if( !"1".equals(kind.getAllLossFlag())){//非全损
				exceptCalProc.append("*(").append(kind.getAmount()).append("/").append(kind.getPureCarPrice()).append(")");
			}
			exceptCalProc.append("*").append(kind.getIndemnityDutyRate());
			exceptCalProc.append("*").append(kind.getExceptDeductibleRate());
			
			kind.setExceptCalcAmt(exceptCalcAmt);
			kind.setExceptFormulaNote(exceptCalNote.toString());
			kind.setExceptFormulaProc(exceptCalProc.toString());
		}
	
	}
	
	/**
	 * 车损险代位计算
	 * 全损：
	 *  车损险赔付   =（保险金额-被保险人已从第三方获得的车损赔偿金额 ）*责任比例*（1-免赔率之和)-绝对免赔额
     *  车损险不计免赔险赔付   = （保险金额-被保险人已从第三方获得的车损赔偿金额 ）*(保险金额/新车购置价)*责任比例 * 责任免赔率
     * 部分损失：
     * 	车损险赔付   =（实际修复费用-被保险人已从第三方获得的车损赔偿金额 ）*责任比例*（1-免赔率之和)-绝对免赔额
     *  车损险不计免赔险赔付   = （实际修复费用-被保险人已从第三方获得的车损赔偿金额）*(保险金额/新车购置价)*责任比例 * 责任免赔率
     * 实际赔付：
     *  本车总赔付 = 车损险赔付 +车损险不计免赔险赔付
	 *  lizy
	 *  2016-03-10
	 * */
	public static void calKind_A_1201_SubrogationAll(ClaimCalcKindVo kind){
		double calcAmt=0;
		StringBuffer calNote=new StringBuffer("");//险别计算公式
		
		double claimRate=1.0;
		if( kind.getAmount()<kind.getPureCarPrice()){//不足额赔付率
			claimRate=new BigDecimal(kind.getAmount()/kind.getPureCarPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if( claimRate>1)
				claimRate=1.0;
		}
		
		if( "1".equals(kind.getAllLossFlag())){
			calNote.append("(保险金额－被保险人已从第三方获得的车损赔偿金额-残值)*(1-免赔率之和)-绝对免赔额");
		}
		else{
			calNote.append("(实际修复费用－被保险人已从第三方获得的车损赔偿金额-残值)*(保险金额/新车购置价)*(1-免赔率之和)-绝对免赔额");
		}
		kind.setFormulaNote(calNote.toString());
		
		double lossAmt=kind.getLossAmt();
		if( kind.getLossAmt()>kind.getAmount())
			lossAmt=kind.getAmount();//流程中应该已有控制
		
	//责任比例 
		calcAmt = new BigDecimal(lossAmt-kind.getBZPaid()-kind.getRestAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*claimRate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt*(1-kind.getDutyDeductibleRate()-kind.getSelectDeductibleRate())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		calcAmt = new BigDecimal(calcAmt-kind.getDeductibleAmt()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		kind.setCalcAmt(calcAmt);
		
		StringBuffer calProc=new StringBuffer("");//险别计算公式
		calProc.append("(").append(lossAmt).append("-").append(kind.getBZPaid()).append("-").append(kind.getRestAmt()).append(")");
		if( ! "1".equals(kind.getAllLossFlag())){//非全损
			calProc.append("*(").append(kind.getAmount()).append("/").append(kind.getPureCarPrice()).append(")");
		}
		calProc.append("*(1-").append(kind.getIndemnityDutyRate()+kind.getSelectDeductibleRate()).append(")");
		calProc.append("-").append(kind.getDeductibleAmt());
		kind.setFormulaProc(calProc.toString());
		
	//不计免赔的公式 	
		if("1".equals(kind.getExceptFlag())){
			double exceptCalcAmt=new BigDecimal((lossAmt-kind.getBZPaid()-kind.getRestAmt())*claimRate*kind.getExceptDeductibleRate()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			
			StringBuffer exceptCalNote=new StringBuffer();
			if( "1".equals(kind.getAllLossFlag())){
				exceptCalNote.append("(保险金额－被保险人已从第三方获得的车损赔偿金额-残值)*不计免赔率");
			}
			else{
				exceptCalNote.append("(实际修复费用－被保险人已从第三方获得的车损赔偿金额-残值)*(保险金额/新车购置价)*不计免赔率");
			}
			
			
			StringBuffer exceptCalProc=new StringBuffer();
			exceptCalProc.append("(").append(lossAmt).append("-").append(kind.getBZPaid()).append("-").append(kind.getRestAmt()).append(")");
			if( !"1".equals(kind.getAllLossFlag())){//非全损
				exceptCalProc.append("*(").append(kind.getAmount()).append("/").append(kind.getPureCarPrice()).append(")");
			}
			exceptCalProc.append("*").append(kind.getExceptDeductibleRate());
			
			kind.setExceptCalcAmt(exceptCalcAmt);
			kind.setExceptFormulaNote(exceptCalNote.toString());
			kind.setExceptFormulaProc(exceptCalProc.toString());
		}		
		
	}
	
	/**
	 * 车损险代位计算
	 * 总赔付，自担，代位
	 *  lizy
	 *  2016-03-10
	 * */
	public static void calKind_A_1201_Subrogation(ClaimCalcKindVo kind){
		calKind_A_1201_Normal(kind);
		double tokenAmt=kind.getCalcAmt();
		double exceptTokenAmt=kind.getExceptCalcAmt();
		//总赔付部分
		calKind_A_1201_SubrogationAll(kind);
		double allAmt=kind.getCalcAmt();
		double exceptAllAmt=kind.getExceptCalcAmt();
		//自担部分
		kind.setTakeonPay(tokenAmt);
		kind.setExceptTakeonPay(exceptTokenAmt);
		//代位部分
		double subrogationPay=new BigDecimal(allAmt-tokenAmt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		double exceptSubrogationPay=new BigDecimal(exceptAllAmt-exceptTokenAmt).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		kind.setSubrogationPay(subrogationPay);
		kind.setExceptSubrogationPay(exceptSubrogationPay);
	}
	
	
}
