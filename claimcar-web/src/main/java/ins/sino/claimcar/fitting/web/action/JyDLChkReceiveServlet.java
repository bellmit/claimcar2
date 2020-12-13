package ins.sino.claimcar.fitting.web.action;

import ins.framework.lang.Springs;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.claimjy.vo.DlossReceiveResVo;
import ins.sino.claimcar.claimjy.vo.JyDLChkEvalLossInfoVo;
import ins.sino.claimcar.claimjy.vo.JyDLChkLossFitInfoItemVo;
import ins.sino.claimcar.claimjy.vo.JyDLChkLossFitInfoVo;
import ins.sino.claimcar.claimjy.vo.JyDLChkReturnBodyVo;
import ins.sino.claimcar.claimjy.vo.JyDLChkReturnVo;
import ins.sino.claimcar.claimjy.vo.JyResHeadVo;
import ins.sino.claimcar.claimjy.vo.JyReturnReqHead;
import ins.sino.claimcar.losscar.service.LossCarService;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarCompVo;
import ins.sino.claimcar.losscar.vo.PrpLDlossCarMainVo;
import ins.sino.claimcar.regist.service.PolicyViewService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.xstream.XStreamException;

/**
 * <pre></pre>
 * @author ★LiYi
 */
public class JyDLChkReceiveServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(JyDlossReceiveServlet.class);

	@Autowired
	PolicyViewService policyViewService;
	@Autowired
	LossCarService lossCarService;

	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
		this.initService();
		JyDLChkReturnVo jyDLChkReturnVo = new JyDLChkReturnVo();
		JyReturnReqHead jyReturnReqHead = null;
		JyDLChkReturnBodyVo jyDLChkReturnBodyVo = null;
		try{
			jyDLChkReturnVo = this.requestData(req);
		}catch(IOException e){
			this.responseData(resp,"409");
			e.printStackTrace();
			return;
		}catch(XStreamException e){
			this.responseData(resp,"401");
			e.printStackTrace();
			return;
			
		}
		// 拿到由报文转换成的JyDLChkReturnVo
		jyReturnReqHead = jyDLChkReturnVo.getHead();
		jyDLChkReturnBodyVo = jyDLChkReturnVo.getBody();

		if(jyReturnReqHead==null||jyDLChkReturnBodyVo==null){
			this.responseData(resp,"402");
			return;
		}
		if( !"017".equals(jyReturnReqHead.getRequestType())){
			this.responseData(resp,"403");
			return;
		}
		if(( !"jy".equals(jyReturnReqHead.getUserCode())&& !"jy".equals(jyReturnReqHead.getPassWord()) )){
			this.responseData(resp,"498");
			return;
		}

		try{
			PrpLDlossCarMainVo prpLDlossCarMainVo = this.organizeInfo(jyDLChkReturnVo);
			// 保存数据
			lossCarService.updateJyDlossCarMain(prpLDlossCarMainVo);
		}
		// catch(SQLException e){
		// this.responseData(resp,"405");
		// logger.info("精友复检完成返回接口数据保存异常"+e.getStackTrace());
		// }
		catch(Exception e){
			this.responseData(resp,"499");
			e.printStackTrace();
			return;
		}
		this.responseData(resp,"000");
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
		doGet(req,resp);
	}

	private JyDLChkReturnVo requestData(HttpServletRequest httpServletRequest) throws IOException,XStreamException {
		JyDLChkReturnVo jyDLChkReturnVo = new JyDLChkReturnVo();
		String xmlData = "";
		InputStream inputStream = httpServletRequest.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"GBK");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String read = "";
		StringBuffer stringBuffer = new StringBuffer();
		// 转换数据流
		while(( read = bufferedReader.readLine() )!=null){
			stringBuffer.append(read);
		}
		inputStreamReader.close();
		bufferedReader.close();
		inputStream.close();
		xmlData = stringBuffer.toString();
		logger.info("精友复检结束返回接口接收xml：\n"+xmlData);
		jyDLChkReturnVo = ClaimBaseCoder.xmlToObj(xmlData,JyDLChkReturnVo.class);
		return jyDLChkReturnVo;
	}

	private void responseData(HttpServletResponse httpServletResponse,String responseCode) throws IOException {
		DlossReceiveResVo jyDlossResVo = new DlossReceiveResVo();
		JyResHeadVo jyResHeadVo = new JyResHeadVo();
		Writer out = new OutputStreamWriter(httpServletResponse.getOutputStream(), "GBK");
		String resultXml = "";

		jyResHeadVo.setResponseCode(responseCode);
		jyResHeadVo.setErrorMessage(this.getMsgByCode(responseCode));
		jyDlossResVo.setJyResHeadVo(jyResHeadVo);
		resultXml = ClaimBaseCoder.objToXml(jyDlossResVo);
		logger.info("精友复检结束返回接口返回xml：\n"+resultXml);
		out.write(resultXml);
		out.flush();
		out.close();
	}

	private PrpLDlossCarMainVo organizeInfo(JyDLChkReturnVo jyDLChkReturnVo) {
		PrpLDlossCarMainVo lossCarMainVo = new PrpLDlossCarMainVo();
		JyDLChkReturnBodyVo returnBodyVo = jyDLChkReturnVo.getBody();
		JyDLChkEvalLossInfoVo evalLossInfoVo = returnBodyVo.getEvalLossInfoVo();
		String id = evalLossInfoVo.getLossNo();
		lossCarMainVo = lossCarService.findLossCarMainById(Long.parseLong(id));
		lossCarMainVo = getEvalLossInfo(lossCarMainVo,returnBodyVo);

		return lossCarMainVo;
	}

	// 定损单信息
	private PrpLDlossCarMainVo getEvalLossInfo(PrpLDlossCarMainVo lossCarMainVo,JyDLChkReturnBodyVo returnBodyVo) {
		JyDLChkEvalLossInfoVo evalLossInfoVo = returnBodyVo.getEvalLossInfoVo();
		lossCarMainVo.setId(Long.parseLong(evalLossInfoVo.getDmgVhclId()));// 车损标的主键
		lossCarMainVo.setdLChkAuditPartSum(NullToZero(evalLossInfoVo.getAuditPartSum()));// 复检换件合计
		lossCarMainVo.setdLChkHandlerCode(evalLossInfoVo.getHandlerCode());// 复检员代码
		lossCarMainVo.setdLChkTotalSum(NullToZero(evalLossInfoVo.getTotalSum()));// 复检整单合计
		this.getLossFitInfo(lossCarMainVo,returnBodyVo);
		return lossCarMainVo;
	}

	// 换件信息
	private void getLossFitInfo(PrpLDlossCarMainVo lossCarMainVo,JyDLChkReturnBodyVo returnBodyVo) {
		List<PrpLDlossCarCompVo> prpLDlossCarComps = lossCarMainVo.getPrpLDlossCarComps();
		JyDLChkLossFitInfoVo lossFitInfoVo = returnBodyVo.getLossFitInfoVo();
		List<JyDLChkLossFitInfoItemVo> itemVos = lossFitInfoVo.getItemVo();
		if(itemVos!=null&&!itemVos.isEmpty()){
			for(JyDLChkLossFitInfoItemVo itemVo:itemVos){
				for (PrpLDlossCarCompVo prpLDlossCarCompVo : prpLDlossCarComps) {
					if(prpLDlossCarCompVo.getIndId().equals(itemVo.getPartId())){// 定损系统零件唯一ID
						prpLDlossCarCompVo.setdLChkAuditMaterialFee(NullToZero(itemVo.getAuditMaterialFee()));// 复检材料费
						prpLDlossCarCompVo.setdLChkAuditCount(NullToZero(itemVo.getAuditCount()));// 复检数量
						prpLDlossCarCompVo.setdLChkApprPartSum(NullToZero(itemVo.getApprPartSum()));// 复检换件小计
						prpLDlossCarCompVo.setdLChkApprRemainsPrice(NullToZero(itemVo.getApprRemainsPrice()));// 复检残值
						prpLDlossCarCompVo.setdLChkRemark(itemVo.getRemark());// 复检备注
					}
				}
			}
		}
	}


	private String getMsgByCode(String code) {
		String msg = "";
		if("000".equals(code)){
			msg = "成功";
		}
		if("400".equals(code)){
			msg = "系统认证错误";
		}
		if("401".equals(code)){
			msg = "数据包格式错误";
		}
		if("402".equals(code)){
			msg = "数据完整性错误";
		}
		if("403".equals(code)){
			msg = "请求类型错误";
		}
		if("405".equals(code)){
			msg = "理赔系统因内部权限等各种原因，无法保存本次数据";
		}
		if("408".equals(code)){
			msg = "安全验证错误";
		}
		if("409".equals(code)){
			msg = "其它异常错误";
		}
		return msg;
	}

	private static BigDecimal NullToZero(String strNum) {
		if(strNum==null||strNum.equals("")){
			strNum = "0";
		}
		return new BigDecimal(strNum);
	}

	private void initService() {
		if(policyViewService==null){
			policyViewService = (PolicyViewService)Springs.getBean(PolicyViewService.class);
		}
		if(lossCarService==null){
			lossCarService = (LossCarService)Springs.getBean(LossCarService.class);
		}
	}
}
