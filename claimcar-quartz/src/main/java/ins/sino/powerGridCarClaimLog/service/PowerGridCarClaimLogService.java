package ins.sino.powerGridCarClaimLog.service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.DateUtils;
import ins.platform.utils.ObjectUtils;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claim.powerGridCarClaimLog.po.GDPowerGridCarClaimLog;
import ins.sino.claim.powerGridCarClaimLog.po.GDPowerGridCarClaimLogSub;
import ins.sino.claim.powerGridCarClaimLog.vo.GDPowerGridCarClaimLogSubVo;
import ins.sino.claim.powerGridCarClaimLog.vo.GDPowerGridCarClaimLogVo;
import ins.sino.claim.powerGridCarClaimLog.vo.GDPowerGridCarClaimMessageVo;
import ins.sino.claim.powerGridCarClaimLog.vo.GDPowerGridCarClaimRequestVo;
import ins.sino.claim.powerGridCarClaimLog.vo.GDPowerGridCarClaimVo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.alibaba.fastjson.JSON;

@Service(value = "powerGridCarClaimLogService")
//@WebService(targetNamespace="http://interf/powerGridCarClaimLogService/",serviceName="powerGridCarClaimLogService",endpointInterface = "ins.sino.powerGridCarClaimLog.service.PowerGridCarClaimLogService")
public class PowerGridCarClaimLogService extends SpringBeanAutowiringSupport{
	
	private static Logger logger = LoggerFactory.getLogger(PowerGridCarClaimLogService.class);
	@Autowired
	private DatabaseDao databaseDao;
	@Autowired
    private BaseDaoService baseDaoService;
	
	/**
	 * 电网理赔信息同步
	 * 
	 * @throws Exception 
	 * @author CSVC
	 */
	public void doClaimInfoSynchronous() throws Exception{
		logger.info(this.getClass().getSimpleName()+"-doClaimInfoSynchronous is run at:"+new Date());
		
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -1);
		
		String url = SpringProperties.getProperty("CARCLAIMSYNCHRO");//请求地址
		//String url = "http://213a351k71.iask.in:17869/dwcars1/web/insure/carClaimSynchro";//请求地址
		
		//zhe
		List<GDPowerGridCarClaimVo> gdPowerGridCarClaimVoList = this.findGDPowerGridCarClaimVoList(startDate, endDate);
		
		if(!ObjectUtils.isEmpty(gdPowerGridCarClaimVoList) && gdPowerGridCarClaimVoList.size() > 0){
			
			int pointsDataLimit = Integer.parseInt(SpringProperties.getProperty("POINTSDATALIMIT"));
			//int pointsDataLimit = 500;//限制条数
			
			//拆分List
			List<List<GDPowerGridCarClaimVo>> gdPowerGridCarClaimVoListV = this.split(gdPowerGridCarClaimVoList, pointsDataLimit);
			
			Map<String, String> damageCodeMap = CodeTranUtil.findCodeNameMap("DamageCode");//出险原因
			Map<String, String> lflagMap = CodeTranUtil.findCodeNameMap("Lflag");//理赔类型
			
			//开始分批发送数据
			for(List<GDPowerGridCarClaimVo> listV : gdPowerGridCarClaimVoListV){
				
				GDPowerGridCarClaimLogVo gdPowerGridCarClaimLogVo = new GDPowerGridCarClaimLogVo();
				gdPowerGridCarClaimLogVo.setId(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
				
				List<GDPowerGridCarClaimLogSubVo> gdPowerGridCarClaimLogSubVoList = new ArrayList<GDPowerGridCarClaimLogSubVo>();
				
				//报文集合
				List<GDPowerGridCarClaimRequestVo> gdPowerGridCarClaimRequestVoList = new ArrayList<GDPowerGridCarClaimRequestVo>();
				
				for(GDPowerGridCarClaimVo gdPowerGridCarClaimVo : listV){
					GDPowerGridCarClaimRequestVo gdPowerGridCarClaimRequestVo = new GDPowerGridCarClaimRequestVo();
					GDPowerGridCarClaimLogSubVo gdPowerGridCarClaimLogSubVo = new GDPowerGridCarClaimLogSubVo();
					
					gdPowerGridCarClaimRequestVo.setCarNum(gdPowerGridCarClaimVo.getCarNum());
					gdPowerGridCarClaimRequestVo.setBeThreatenedTime(gdPowerGridCarClaimVo.getBeThreatenedTime());
					gdPowerGridCarClaimRequestVo.setBeThreatenedAddress(gdPowerGridCarClaimVo.getBeThreatenedAddress());
					gdPowerGridCarClaimRequestVo.setBeThreatenedReason(damageCodeMap.get(gdPowerGridCarClaimVo.getBeThreatenedReason()));
					gdPowerGridCarClaimRequestVo.setReportTime(gdPowerGridCarClaimVo.getReportTime());
					gdPowerGridCarClaimRequestVo.setCaseNumber(gdPowerGridCarClaimVo.getCaseNumber());
					gdPowerGridCarClaimRequestVo.setCaseTime(gdPowerGridCarClaimVo.getCaseTime());
					gdPowerGridCarClaimRequestVo.setClosingTime(gdPowerGridCarClaimVo.getClosingTime());
					gdPowerGridCarClaimRequestVo.setClaimType(lflagMap.get(gdPowerGridCarClaimVo.getClaimType()));
					gdPowerGridCarClaimRequestVo.setFactory(gdPowerGridCarClaimVo.getFactory());
					gdPowerGridCarClaimRequestVo.setClaimAmount(gdPowerGridCarClaimVo.getClaimAmount());
					
					gdPowerGridCarClaimRequestVoList.add(gdPowerGridCarClaimRequestVo);
					
					gdPowerGridCarClaimLogSubVo.setId(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
					gdPowerGridCarClaimLogSubVo.setGdClaimLogId(gdPowerGridCarClaimLogVo.getId());
					gdPowerGridCarClaimLogSubVo.setRegistNo(gdPowerGridCarClaimVo.getRegistNo());//报案号
					gdPowerGridCarClaimLogSubVo.setClaimNo(gdPowerGridCarClaimVo.getCaseNumber());//立案号(理赔唯一案件号)
					gdPowerGridCarClaimLogSubVo.setCaseNo(gdPowerGridCarClaimVo.getEndCaseNo());//结案号
					
					gdPowerGridCarClaimLogSubVoList.add(gdPowerGridCarClaimLogSubVo);
					
				}
				
				String requestInfo = "";//请求报文
				Date sendTime = new Date();//请求时间
				
				String token = AES128Utils.buildOnlyCode();
				String dataSource = JSON.toJSONString(gdPowerGridCarClaimRequestVoList);//数据源
				requestInfo = "token="+token+"&dataSource="+dataSource;
				
				gdPowerGridCarClaimLogVo.setSendTime(sendTime);
				gdPowerGridCarClaimLogVo.setSendJson(requestInfo);
		
				
				try {
					//请求平台
					String responseInfo = this.sendPost(requestInfo, url);//返回报文
					GDPowerGridCarClaimMessageVo carMessage = JSON.parseObject(responseInfo, GDPowerGridCarClaimMessageVo.class);//报文详情
					
					gdPowerGridCarClaimLogVo.setResponseJson(responseInfo);
					gdPowerGridCarClaimLogVo.setResponseCode(carMessage.getCode());
					gdPowerGridCarClaimLogVo.setErrorMessage(carMessage.getMsg());
					
				} catch (Exception e) {
					//e.printStackTrace();
					gdPowerGridCarClaimLogVo.setErrorMessage(e.getMessage());
					gdPowerGridCarClaimLogVo.setResponseCode("1");
				} finally {
					Date returnTime = new Date();//返回时间
					Long cost = returnTime.getTime() - sendTime.getTime();//请求耗时
					
					gdPowerGridCarClaimLogVo.setReturnTime(returnTime);
					gdPowerGridCarClaimLogVo.setCost(cost);
					
					GDPowerGridCarClaimLog claimLogPo = new GDPowerGridCarClaimLog();
					Beans.copy().from(gdPowerGridCarClaimLogVo).to(claimLogPo);
					databaseDao.save(GDPowerGridCarClaimLog.class, claimLogPo);
					
					List<GDPowerGridCarClaimLogSub> gdPowerGridCarClaimLogSubList = Beans.copyDepth().from(gdPowerGridCarClaimLogSubVoList).toList(GDPowerGridCarClaimLogSub.class);
					databaseDao.saveAll(GDPowerGridCarClaimLogSub.class, gdPowerGridCarClaimLogSubList);
				}
			}
		}else{
			System.out.println("没有查询到GDPowerGridCarClaimVo数据!!!");
		}
	}
	
	//备选
	public void doClaimInfo() throws Exception{
		logger.info(this.getClass().getSimpleName()+"-doClaimInfo is run at:"+new Date());
		
		Date endDate = new Date();
		Date startDate = DateUtils.addDays(endDate, -1);
		
		String url = SpringProperties.getProperty("CARCLAIMSYNCHRO");//请求地址
		//String url = "http://213a351k71.iask.in:17869/dwcars1/web/insure/carClaimSynchro";//请求地址
		
		//zhe
		List<GDPowerGridCarClaimVo> gdPowerGridCarClaimVoList = this.findGDPowerGridCarClaimInfo(startDate, endDate);
		
		if(!ObjectUtils.isEmpty(gdPowerGridCarClaimVoList) && gdPowerGridCarClaimVoList.size() > 0){
			
			int pointsDataLimit = Integer.parseInt(SpringProperties.getProperty("POINTSDATALIMIT"));//限制条数
			//int pointsDataLimit = 100;//限制条数
			
			//拆分List
			List<List<GDPowerGridCarClaimVo>> gdPowerGridCarClaimVoListV = this.split(gdPowerGridCarClaimVoList, pointsDataLimit);
			
			Map<String, String> damageCodeMap = CodeTranUtil.findCodeNameMap("DamageCode");//出险原因
			Map<String, String> lflagMap = CodeTranUtil.findCodeNameMap("Lflag");//理赔类型
			
			//开始分批发送数据
			for(List<GDPowerGridCarClaimVo> listV : gdPowerGridCarClaimVoListV){
				
				GDPowerGridCarClaimLogVo gdPowerGridCarClaimLogVo = new GDPowerGridCarClaimLogVo();
				gdPowerGridCarClaimLogVo.setId(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
				
				List<GDPowerGridCarClaimLogSubVo> gdPowerGridCarClaimLogSubVoList = new ArrayList<GDPowerGridCarClaimLogSubVo>();
				
				//报文集合
				List<GDPowerGridCarClaimRequestVo> gdPowerGridCarClaimRequestVoList = new ArrayList<GDPowerGridCarClaimRequestVo>();
				
				for(GDPowerGridCarClaimVo gdPowerGridCarClaimVo : listV){
					GDPowerGridCarClaimRequestVo gdPowerGridCarClaimRequestVo = new GDPowerGridCarClaimRequestVo();
					GDPowerGridCarClaimLogSubVo gdPowerGridCarClaimLogSubVo = new GDPowerGridCarClaimLogSubVo();
					
					gdPowerGridCarClaimRequestVo.setCarNum(gdPowerGridCarClaimVo.getCarNum());
					gdPowerGridCarClaimRequestVo.setBeThreatenedTime(gdPowerGridCarClaimVo.getBeThreatenedTime());
					gdPowerGridCarClaimRequestVo.setBeThreatenedAddress(gdPowerGridCarClaimVo.getBeThreatenedAddress());
					gdPowerGridCarClaimRequestVo.setBeThreatenedReason(damageCodeMap.get(gdPowerGridCarClaimVo.getBeThreatenedReason()));
					gdPowerGridCarClaimRequestVo.setReportTime(gdPowerGridCarClaimVo.getReportTime());
					gdPowerGridCarClaimRequestVo.setCaseNumber(gdPowerGridCarClaimVo.getCaseNumber());
					gdPowerGridCarClaimRequestVo.setCaseTime(gdPowerGridCarClaimVo.getCaseTime());
					gdPowerGridCarClaimRequestVo.setClosingTime(gdPowerGridCarClaimVo.getClosingTime());
					gdPowerGridCarClaimRequestVo.setClaimType(lflagMap.get(gdPowerGridCarClaimVo.getClaimType()));
					gdPowerGridCarClaimRequestVo.setFactory(gdPowerGridCarClaimVo.getFactory());
					gdPowerGridCarClaimRequestVo.setClaimAmount(gdPowerGridCarClaimVo.getClaimAmount());
					
					gdPowerGridCarClaimRequestVoList.add(gdPowerGridCarClaimRequestVo);
					
					gdPowerGridCarClaimLogSubVo.setId(UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
					gdPowerGridCarClaimLogSubVo.setGdClaimLogId(gdPowerGridCarClaimLogVo.getId());
					gdPowerGridCarClaimLogSubVo.setRegistNo(gdPowerGridCarClaimVo.getRegistNo());//报案号
					gdPowerGridCarClaimLogSubVo.setClaimNo(gdPowerGridCarClaimVo.getCaseNumber());//立案号(理赔唯一案件号)
					gdPowerGridCarClaimLogSubVo.setCaseNo(gdPowerGridCarClaimVo.getEndCaseNo());//结案号
					
					gdPowerGridCarClaimLogSubVoList.add(gdPowerGridCarClaimLogSubVo);
					
				}
				
				String requestInfo = "";//请求报文
				Date sendTime = new Date();//请求时间
				
				String token = AES128Utils.buildOnlyCode();
				String dataSource = JSON.toJSONString(gdPowerGridCarClaimRequestVoList);//数据源
				requestInfo = "token="+token+"&dataSource="+dataSource;
				
				gdPowerGridCarClaimLogVo.setSendTime(sendTime);
				gdPowerGridCarClaimLogVo.setSendJson(requestInfo);
		
				try {
					//请求平台
					String responseInfo = this.sendPost(requestInfo, url);//返回报文
					GDPowerGridCarClaimMessageVo carMessage = JSON.parseObject(responseInfo, GDPowerGridCarClaimMessageVo.class);//报文详情
					
					gdPowerGridCarClaimLogVo.setResponseJson(responseInfo);
					gdPowerGridCarClaimLogVo.setResponseCode(carMessage.getCode());
					gdPowerGridCarClaimLogVo.setErrorMessage(carMessage.getMsg());
					
				} catch (Exception e) {
					//e.printStackTrace();
					gdPowerGridCarClaimLogVo.setErrorMessage(e.getMessage());
					gdPowerGridCarClaimLogVo.setResponseCode("1");
				} finally{
					Date returnTime = new Date();//返回时间
					Long cost = returnTime.getTime() - sendTime.getTime();//请求耗时
					
					gdPowerGridCarClaimLogVo.setReturnTime(returnTime);
					gdPowerGridCarClaimLogVo.setCost(cost);
					
					GDPowerGridCarClaimLog claimLogPo = new GDPowerGridCarClaimLog();
					Beans.copy().from(gdPowerGridCarClaimLogVo).to(claimLogPo);
					databaseDao.save(GDPowerGridCarClaimLog.class, claimLogPo);
					
					List<GDPowerGridCarClaimLogSub> gdPowerGridCarClaimLogSubList = Beans.copyDepth().from(gdPowerGridCarClaimLogSubVoList).toList(GDPowerGridCarClaimLogSub.class);
					databaseDao.saveAll(GDPowerGridCarClaimLogSub.class, gdPowerGridCarClaimLogSubList);
				}
			}
		}else{
			System.out.println("没有查询到GDPowerGridCarClaimVo数据!!!");
		}
		
		List<GDPowerGridCarClaimLogVo> gdPowerGridCarClaimLogVoList = this.findGDPowerGridCarClaimLogVoList("1");
		
		for(GDPowerGridCarClaimLogVo log : gdPowerGridCarClaimLogVoList){
			String sendJson = log.getSendJson();
			if(sendJson.indexOf("dataSource=") > 0){
				String[] split = sendJson.split("dataSource=");
				String requestInfo = "";//请求报文
				String token = AES128Utils.buildOnlyCode();
				
				Date sendTime = new Date();//请求时间
				requestInfo = "token="+token+"&dataSource="+split[1];
				
				try {
					String responseInfo = this.sendPost(requestInfo, url);//返回报文
					GDPowerGridCarClaimMessageVo carMessage = JSON.parseObject(responseInfo, GDPowerGridCarClaimMessageVo.class);//报文详情
					
					if("0".equals(carMessage.getCode())){
						log.setSendTime(sendTime);
						log.setSendJson(requestInfo);
						log.setResponseCode(carMessage.getCode());
						log.setErrorMessage(carMessage.getMsg());
						databaseDao.update(GDPowerGridCarClaimLog.class, log);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 等量拆分list集合
	 * 
	 * @author CSVC
	 */
	public <T> List<List<T>> split(List<T> resList,int count) {
	    if(resList==null ||count<1)
	        return  null ;
	    List<List<T>> ret=new ArrayList<List<T>>();
	    int size=resList.size();
	    if(size<=count){ //数据量不足count指定的大小
	        ret.add(resList);
	    }else{
	        int pre=size/count;
	        int last=size%count;
	        //前面pre个集合，每个大小都是count个元素
	        for(int i=0;i<pre;i++){
	            List<T> itemList=new ArrayList<T>();
	            for(int j=0;j<count;j++){
	                itemList.add(resList.get(i*count+j));
	            }
	            ret.add(itemList);
	        }
	        //last的进行处理
	        if(last>0){
	            List<T> itemList=new ArrayList<T>();
	            for(int i=0;i<last;i++){
	                itemList.add(resList.get(pre*count+i));
	            }
	            ret.add(itemList);
	        }
	    }
	    return ret;
	}
	
	/**
	 * 连接方法
	 * 
	 * @author CSVC
	 */
	public String sendPost(String request, String urlStr) throws Exception {
		String charset = "UTF-8";
		StringBuffer buffer = new StringBuffer();
		String strMessage = "";
		InputStream inputStream = null;
		OutputStream outputStream = null;
		BufferedReader reader = null;
		OutputStreamWriter writer = null;
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true); // 设置可以进行输入操作
			connection.setDoOutput(true); // 设置可以进行输出操作
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setConnectTimeout(10000);// 设置连接主机的时间
			connection.setReadTimeout(10000);// 设置从主机读取文件的时间
			connection.setAllowUserInteraction(true); // 设置可以进行交互操作
			connection.connect();
			outputStream = connection.getOutputStream();
			writer = new OutputStreamWriter(outputStream);
			writer.write(request);
			writer.flush(); // 立即输出
			writer.close();
			inputStream = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream)); // 为了进行一行读取操作
			while ((strMessage = reader.readLine()) != null) {
				buffer.append(strMessage);
			}
		} catch (Exception ex) {
			throw new Exception("与平台连接失败，请稍候再试", ex);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
		return new String(buffer.toString().getBytes(), charset);
	}
	
	public List<GDPowerGridCarClaimVo> findGDPowerGridCarClaimVoList(Date startDate, Date endDate) throws Exception {//对状态没有查询条件
		String sqlStr = "select r.registno, "
				+ "d.licenseno, "
				+ "r.damagetime, "
				+ "r.damageaddress, "
				+ "r.damagecode, "
				+ "c.reporttime, "
				+ "c.claimno, "
				+ "c.claimtime, "
				+ "s.endcasedate, "
				+ "s.endcaseno, "
				+ "d.lflag, "
				+ "d.repairfactoryname, " 
				+ "SuM(NVL(m.sumamt, 0)) + SuM(NVL(m.sumfee, 0)) "
				+ "FROM "
				+ "PrpLRegist r, "
				+ "PrpLDlossCarMain d, "
				+ "PrpLClaim c, "
				+ "PrpLCompensate m, "
				+ "PrpLEndCase s "
				+ "where r.registno = d.registno "
				+ "and r.registno = c.registno "
				+ "and r.registno = m.registno "
//				+ "and c.claimno = m.claimno "
//				+ "and c.claimno = s.claimno "
				+ "and r.registno = s.registno "
				+ "and s.endcaseno = m.caseno "
				
				+ "and s.endcasedate = (select max(endcasedate) "
					+ "from PrpLEndCase B "
						+ "where c.claimno = B.claimno) "
					
				+ "and s.endcasedate >= to_date('"+DateUtils.dateToStr(startDate,"yyyy-MM-dd")+"', 'yyyy/MM/dd') "
				+ "and s.endcasedate < to_date('"+DateUtils.dateToStr(endDate,"yyyy-MM-dd")+"', 'yyyy/MM/dd') "
					
				+ "and d.deflosscartype = '1' "
				+ "and m.underwriteflag in ('1', '3') "
				+ "and exists "
					+ "(select 1 "
						+ "from prplcmain a "
							+ "where a.registno = r.registno "
							+ "and exists "
								+ "(select 1 "
									+ "from PrpLCItemCar t "
										+ "where a.policyno = t.policyno "
										+ "and exists (select 1 "
											+ "from GDPowerGridCarInfo ci "
												+ "where ci.engineno = t.engineno "
												+ "and ci.frameno = t.frameno))) "
				+ "and not exists "
					+ "(select 1 "
						+ "from GDPowerGridCarClaimLogSub cls "
							+ "where r.registno = cls.registno "
							+ "and c.claimno = cls.claimno "
							+ "and s.endcaseno = cls.caseno "
							+ "and exists (select 1 "
								+ "from GDPowerGridCarClaimLog cl "
									+ "where cls.gdClaimLogId = cl.id "
									+ "and cl.responsecode = '0')) "
				+ "group by r.registno, d.licenseno, r.damagetime, r.damageaddress, r.damagecode, c.reporttime, c.claimno,"
				+ "c.claimtime, s.endcasedate, s.endcaseno, d.lflag, d.repairfactoryname";
          
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		
		sqlUtil.append(sqlStr);
		
		String sql = sqlUtil.getSql();
		logger.info("findGDPowerGridCarClaimVo:" + sql);
		//List<Object[]> result = databaseDao.findAllBySql(sql,sqlUtil.getParamValues());
		List<Object[]> result = baseDaoService.findListBySql(sql,sqlUtil.getParamValues());
		// 对象转换
		List<GDPowerGridCarClaimVo> resultList = new ArrayList<GDPowerGridCarClaimVo>();
		
		for(int i = 0; i < result.size(); i++ ){
			GDPowerGridCarClaimVo resultVo = new GDPowerGridCarClaimVo();
			Object[] obj = (Object[])result.get(i);
			resultVo.setRegistNo((String)obj[0]);
			resultVo.setCarNum((String)obj[1]);
			resultVo.setBeThreatenedTime((Date)obj[2]);
			resultVo.setBeThreatenedAddress((String)obj[3]);
			resultVo.setBeThreatenedReason((String)obj[4]);
            resultVo.setReportTime((Date)obj[5]);
            resultVo.setCaseNumber((String)obj[6]);
            resultVo.setCaseTime((Date)obj[7]);
            resultVo.setClosingTime((Date)obj[8]);      
            resultVo.setEndCaseNo((String)obj[9]);
            resultVo.setClaimType((String)obj[10]);
            resultVo.setFactory((String)obj[11]);
            resultVo.setClaimAmount((BigDecimal)obj[12]);
			resultList.add(resultVo);
		}
		return resultList;
	}
	
	public List<GDPowerGridCarClaimVo> findGDPowerGridCarClaimInfo(Date startDate, Date endDate) throws Exception {//对状态没有查询条件
		String sqlStr = "select r.registno, "
				+ "d.licenseno, "
				+ "r.damagetime, "
				+ "r.damageaddress, "
				+ "r.damagecode, "
				+ "c.reporttime, "
				+ "c.claimno, "
				+ "c.claimtime, "
				+ "s.endcasedate, "
				+ "s.endcaseno, "
				+ "d.lflag, "
				+ "d.repairfactoryname, " 
				//+ "(select SuM(NVL(msate.sumamt, 0)) + SuM(NVL(msate.sumfee, 0)) from prplcompensate msate where msate.underwriteflag in ('1', '3')  and msate.claimno = c.claimno ) "
				+ "(select SuM(NVL(msate.sumamt, 0)) + SuM(NVL(msate.sumfee, 0)) from prplcompensate msate where msate.underwriteflag in ('1', '3')  and msate.registno = r.registno and msate.caseno = s.endcaseno) "
				+ "FROM "
				+ "PrpLRegist r, "
				+ "PrpLDlossCarMain d, "
				+ "PrpLClaim c, "
				+ "PrpLEndCase s "
				+ "where r.registno = d.registno "
				+ "and r.registno = c.registno "
				//+ "and c.claimno = s.claimno "
				+ "and r.registno = s.registno "
				
				+ "and s.endcasedate = (select max(endcasedate) "
				+ "from PrpLEndCase B "
				+ "where c.claimno = B.claimno) "
				
				+ "and s.endcasedate >= to_date('"+DateUtils.dateToStr(startDate,"yyyy-MM-dd")+"', 'yyyy/MM/dd') "
				+ "and s.endcasedate < to_date('"+DateUtils.dateToStr(endDate,"yyyy-MM-dd")+"', 'yyyy/MM/dd') "
				
				+ "and d.deflosscartype = '1' "
				+ "and exists "
				+ "(select 1 "
				+ "from prplcmain a "
				+ "where a.registno = r.registno "
				+ "and exists "
				+ "(select 1 "
				+ "from PrpLCItemCar t "
				+ "where a.policyno = t.policyno "
				+ "and exists (select 1 "
				+ "from GDPowerGridCarInfo ci "
				+ "where ci.engineno = t.engineno "
				+ "and ci.frameno = t.frameno))) ";
		
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		
		sqlUtil.append(sqlStr);
		
		String sql = sqlUtil.getSql();
		logger.info("findGDPowerGridCarClaimVo:" + sql);
		//List<Object[]> result = databaseDao.findAllByHql(sql,sqlUtil.getParamValues());
		List<Object[]> result = baseDaoService.findListBySql(sql,sqlUtil.getParamValues());
		
		// 对象转换
		List<GDPowerGridCarClaimVo> resultList = new ArrayList<GDPowerGridCarClaimVo>();
		
		for(int i = 0; i < result.size(); i++ ){
			GDPowerGridCarClaimVo resultVo = new GDPowerGridCarClaimVo();
			Object[] obj = (Object[])result.get(i);
			resultVo.setRegistNo((String)obj[0]);
			resultVo.setCarNum((String)obj[1]);
			resultVo.setBeThreatenedTime((Date)obj[2]);
			resultVo.setBeThreatenedAddress((String)obj[3]);
			resultVo.setBeThreatenedReason((String)obj[4]);
			resultVo.setReportTime((Date)obj[5]);
			resultVo.setCaseNumber((String)obj[6]);
			resultVo.setCaseTime((Date)obj[7]);
			resultVo.setClosingTime((Date)obj[8]);      
			resultVo.setEndCaseNo((String)obj[9]);
			resultVo.setClaimType((String)obj[10]);
			resultVo.setFactory((String)obj[11]);
			resultVo.setClaimAmount((BigDecimal)obj[12]);
			resultList.add(resultVo);
		}
		return resultList;
	}
	
	public List<GDPowerGridCarClaimLogVo> findGDPowerGridCarClaimLogVoList(String responseCode) throws Exception {
		List<GDPowerGridCarClaimLogVo> gdPowerGridCarClaimLogVoList = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("responseCode", responseCode);
		List<GDPowerGridCarClaimLog> gdPowerGridCarClaimLogList = databaseDao.findAll(GDPowerGridCarClaimLog.class, queryRule);
		if(gdPowerGridCarClaimLogList != null){
			gdPowerGridCarClaimLogVoList = Beans.copyDepth().from(gdPowerGridCarClaimLogList).toList(GDPowerGridCarClaimLogVo.class);
		}
		return gdPowerGridCarClaimLogVoList;
	}
}
