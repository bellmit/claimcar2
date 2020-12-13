/******************************************************************************
 * CREATETIME : 2014年6月24日 上午11:32:09
 ******************************************************************************/
package ins.platform.common.service.facade;

import ins.framework.dao.support.sequence.SeqGenerator;
import ins.framework.lang.Springs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 生成单证序列的服务
 * 
 * @author ★LiuPing
 */
@Service(value = "billNoService")
public class BillNoService {

	private static Logger log = LoggerFactory.getLogger(BillNoService.class);

	/** * 定义单证类型属性 */
	public enum BillType {

		RegistNo("4",7),
		FlowId("F",6),
		CompensateNo("7",9),
		PolicyTmp("T2",6),
		ClaimNo("5",7),
		RecLossId("H",6),
		RecLossMainId("R",6),
		RecLossNo("",10),
		PadPayNo("D",6),
		PrePayNo("Y",7),
		EndCaseNo("6",6),
		RecPayNo("Z",6),
		RegressNo("G",8),
		AssessorNo("F",6),
		CheckFeeNo("C",8),//查勘费
		ILogRule("R",10), // ilog规则号
		BillId("B",8),//一般实赔与预付发票Id
		AssBillId("F",8),//公估费发票Id
		CheBillId("C",8),//查勘费发票id
		BatchNo("P",8),//批次号
		RealTimeQueryNo("",10);//反欺诈uuid 10位

		private String prefix;// 单号的前缀，第一个字母
		private int length;// 单号的长度

		private BillType(String prefix,int length){
			this.prefix = prefix;
			this.length = length;
		}
	}

	/**
	 * 单号使用出错时回收一个单号
	 * 
	 * @param billNo 完整单号
	 * @param billType 单号类型，来自枚举BillType
	 * @modified: ☆LiuPing(2014年6月24日 下午12:33:42): <br>
	 */
	public void recoveredBillNo(String billNo,BillType billType) {
		if(StringUtils.isBlank(billNo)) return;
		String key = billNo.substring(0,billNo.length()-billType.length);
		String sequence = billNo.substring(billNo.length()-billType.length);
		recoveredSeq(key,sequence);
	}

	/**
	 * 生成报案号
	 * 
	 * @param comCode 机构代码
	 * @param riskCode 险种
	 * @return
	 * @modified: ☆XMSH(2015年12月15日 下午4:13:00): <br>
	 */
	public String getRegistNo(String comCode,String riskCode) {
		String key = BillType.RegistNo.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.RegistNo);
		return strBillNo;
	}

	/**
	 * 生成赔款计算书号号
	 * 
	 * @param comCode
	 * @param riskCode
	 * @return
	 */
	public String getCompensateNo(String comCode,String riskCode) {
		String key = BillType.CompensateNo.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.CompensateNo);
		return strBillNo;
	}

	/**
	 * 生成临时保单号
	 * 
	 * @param comCode 机构代码
	 * @param riskCode 险种
	 * @return
	 * @modified: ☆XMSH(2015年12月15日 下午4:13:00): <br>
	 */
	public String getPolicyTmp(String comCode,String riskCode) {
		String key = BillType.PolicyTmp.prefix+cutComCode(comCode,4)+getDateStr("yyyyMM")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.PolicyTmp);
		return strBillNo;
	}

	/**
	 * 生成立案号
	 * 
	 * @param comCode
	 * @param riskCode
	 * @return
	 * @modified: ☆XMSH(2015年12月15日 下午4:15:35): <br>
	 */
	public String getClaimNo(String comCode,String riskCode) {
		String key = BillType.ClaimNo.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.ClaimNo);
		return strBillNo;
	}

	/**
	 * 生成垫付计算书号
	 * 
	 * @param comCode 机构代码
	 * @param riskCode 险种
	 */
	public String getPadPayNo(String comCode,String riskCode) {
		String key = BillType.PadPayNo.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.PadPayNo);
		return strBillNo;
	}

	/**
	 * 生成预付计算书号
	 * 
	 * @param comCode 机构代码
	 * @param riskCode 险种
	 */
	public String getPrePayNo(String comCode,String riskCode) {
		String key = BillType.PrePayNo.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.PrePayNo);
		return strBillNo;
	}

	/**
	 * 生成追偿计算书号
	 * 
	 * @param comCode
	 * @param riskCode
	 * @return
	 */
	public String getRecPayNo(String comCode,String riskCode) {
		String key = BillType.RecPayNo.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.RecPayNo);
		return strBillNo;
	}

	/**
	 * 生成结案号
	 * 
	 * @param comCode 机构代码
	 * @param riskCode 险种
	 */
	public String getEndCaseNo(String comCode,String riskCode) {
		String key = BillType.EndCaseNo.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.EndCaseNo);
		return strBillNo;
	}

	/**
	 * 生成归档号
	 * 
	 * @param comCode 机构代码
	 * @param riskCode 险种
	 */
	public String getRegressNo(String comCode) {
		//String key = BillType.RegressNo.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;//旧的归档号生成规则
		//新的归档号生成规则，G+9+前两位机构代码+4位年份代码+8位流水号
		String key=BillType.RegressNo.prefix+"9"+cutComCode(comCode,2)+getDateStr("yyyy");
		String strBillNo = key+generateSeq(key,BillType.RegressNo);
		return strBillNo;
	}

	/**
	 * 生成工作流号
	 * 
	 * <pre></pre>
	 * 
	 * @param comCode
	 * @param riskCode
	 * @return
	 * @modified: ☆XMSH(2015年12月15日 下午4:26:51): <br>
	 */
	public String getFlowId(String comCode,String riskCode) {
		String key = BillType.FlowId.prefix+cutComCode(comCode,6)+getDateStr("yyyy")+riskCode;
		String strBillNo = key+generateSeq(key,BillType.FlowId);
		return strBillNo;
	}

	/**
	 * 生成损余回收计算号
	 * 
	 * @param type
	 * @param comCode
	 * @return
	 */
	public String getRecLossId(String type,String comCode) {
		String key = BillType.RecLossId.prefix+type+getDateStr("yyyy")+cutComCode(comCode,6);
		String strBillNo = key+generateSeq(key,BillType.RecLossId);
		return strBillNo;
	}
	/**
	 * 生成损余回收主Id
	 * @param type
	 * @param comCode
	 * @return
	 */
	public String getRecLossMainId(String comCode) {
		String key = BillType.RecLossMainId.prefix+getDateStr("yyyy")+cutComCode(comCode,6);
		String strBillNo = key+generateSeq(key,BillType.RecLossMainId);
		return strBillNo;
	}
	
	public String getAssessorNo(String comCode,String taskType) {
		String key = BillType.AssessorNo.prefix+taskType+getDateStr("yyyy")+cutComCode(comCode,6);
		String strBillNo = key+generateSeq(key,BillType.AssessorNo);
		return strBillNo;
	}
	/**
	 * 查勘费
	 * @param comCode
	 * @param taskType
	 * @return
	 */
	public String getCheckFeeNo(String comCode,String taskType) {
		String key = BillType.CheckFeeNo.prefix+taskType+getDateStr("yyyy")+cutComCode(comCode,6);
		String strBillNo = key+generateSeq(key,BillType.CheckFeeNo);
		return strBillNo;
	}
	

	/**
	 * 回收编号(年份+流水号(10位))
	 * 
	 * @return
	 */
	public String getRecLossNo() {
		String key = getDateStr("yyyy");
		String strBillNo = key+generateSeq(key,BillType.RecLossNo);
		return strBillNo;
	}
	
	/**
	 * 反欺诈查询接口生成序列号 (流水号(10位))
	 * 
	 * @return
	 */
	public String getRealTimeQueryNo(String key) {
		String strBillNo = key+generateSeq(key,BillType.RealTimeQueryNo);
		return strBillNo;
	}

	/**
	 * 根据序列名称得到下一个序列号
	 * 
	 * @param sequence 序列名称
	 * @param length 序列固定长度，长度不够前面会自动补0，如果从数据库得到的数字长度>length,则不补0
	 * @return
	 * @modified: ☆LiuPing(2014年6月24日 下午12:15:13): <br>
	 */
	/*
	 * public String getSeqNextVal(String sequence, int length) {
	 * 
	 * String sql = "Select " + sequence + ".nextval  From dual"; Query query = em.createNativeQuery(sql); Object obj = query.getSingleResult(); String strSeqNo = obj.toString(); if (strSeqNo.length()
	 * < length) { int seqNo = new Integer(strSeqNo); strSeqNo = String.format("%1$0" + length + "d", seqNo); } return strSeqNo; }
	 */

	/**
	 * 生成一个序列号
	 * 
	 * @param key 前缀
	 * @param billType 单号类型
	 * @return
	 * @modified: ☆LiuPing(2014年6月24日 上午11:43:03): <br>
	 */
	private String generateSeq(String key,BillType billType) {
		SeqGenerator seqGenerator = (SeqGenerator)Springs.getBean("seqGenerator");
		log.debug(billType.name()+","+key+".hashCode="+generateKey(key));
		String seqNo = seqGenerator.generateSequence(key,billType.length);
		return seqNo;
	}

	private long generateKey(String name) {
		return 10000000000L+name.trim().hashCode();
	}

	/**
	 * 回收一个序列号
	 * 
	 * @param key 前缀
	 * @param length 序列
	 * @return
	 * @modified: ☆LiuPing(2014年6月24日 上午11:43:03): <br>
	 */
	private void recoveredSeq(String key,final String sequence) {
		SeqGenerator seqGenerator = (SeqGenerator)Springs.getBean("seqGenerator");
		seqGenerator.recoveredSequence(key,sequence);
	}

	private String cutComCode(String comCode,int length) {
		if(comCode.length()>length){
			comCode = comCode.substring(0,length);
		}
		return comCode;
	}

	private String getDateStr(String format) {
		DateFormat myFormat = new SimpleDateFormat(format);
		String dateStr = myFormat.format(new Date());
		return dateStr;
	}
	
	
	public String getRuleBill(String comCode) {
		String key = BillType.ILogRule.prefix+cutComCode(comCode,6)+getDateStr("yyyy");
		String strBillNo = key+generateSeq(key,BillType.ILogRule);
		return strBillNo;
	}
	/**
	 * 生成发票id
	 * 
	 * @param payid
	 * @param type--0一般实赔与预付，1--公估费，2--查勘费
	 * @return
	 */
	public String getBillId(String payid,String type) {
        Long subtime=new Date().getTime();
        String substr=subtime.toString().substring(subtime.toString().length()-5, subtime.toString().length());
        String key="";
        String strBillNo="";
        if("0".equals(type)){
        	key = BillType.BillId.prefix+getDateStr("yyyy")+payid+substr;
        	strBillNo = key+generateSeq(key,BillType.BillId);
        }else if("1".equals(type)){
        	key = BillType.AssBillId.prefix+getDateStr("yyyy")+payid+substr;
        	strBillNo = key+generateSeq(key,BillType.AssBillId);
        }else{
        	key = BillType.CheBillId.prefix+getDateStr("yyyy")+payid+substr;
        	strBillNo = key+generateSeq(key,BillType.CheBillId);
        }
		
		
		return strBillNo;
	}
	
}
