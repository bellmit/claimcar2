/******************************************************************************
 * CREATETIME : 2016年10月17日 下午6:12:04
 ******************************************************************************/
package ins.sino.claimcar.linkage.service;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.lang.Springs;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.utils.XstreamFactory;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.linkage.po.PrpLLinkCase;
import ins.sino.claimcar.linkage.po.PrpLLinkCaseCar;
import ins.sino.claimcar.linkage.po.PrpLLinkCaseImg;
import ins.sino.claimcar.linkage.po.PrpLLinkCaseInfo;
import ins.sino.claimcar.linkage.po.PrpLLinkCaseInjured;
import ins.sino.claimcar.linkage.po.PrpLLinkCaseMain;
import ins.sino.claimcar.linkage.po.PrpLLinkVersion;
import ins.sino.claimcar.linkage.util.DateUtil;
import ins.sino.claimcar.linkage.util.LinkageUtil;
import ins.sino.claimcar.policyLinkage.service.LinkageService;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailBaseInfoVo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailDataVo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailImgList1Vo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailImgList2Vo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailImgList3Vo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailImgList4Vo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailImgList5Vo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailImgList6Vo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailImgList7Vo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailImgList8Vo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailInjuredInfosVo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailResultVo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailTargetCarInfoVo;
import ins.sino.claimcar.policyLinkage.vo.CaseDetailThirdCarInfosVo;
import ins.sino.claimcar.policyLinkage.vo.CaseInfosDataListVo;
import ins.sino.claimcar.policyLinkage.vo.CaseInfosResultVo;
import ins.sino.claimcar.policyLinkage.vo.CurrentVersionVo;
import ins.sino.claimcar.policyLinkage.vo.PolicyLinkQueryVo;
import ins.sino.claimcar.policyLinkage.vo.PolicyLinkResultVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseCarVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseImgVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseInfoVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseInjuredVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseMainVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkCaseVo;
import ins.sino.claimcar.policyLinkage.vo.PrpLLinkVersionVo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * <pre></pre>
 * 
 * @author Niuqiang
 */
@Service(protocol = { "dubbo" }, validation = "true", registry = { "default" })
@Path("linkageService")
public class LinkageServiceImpl implements LinkageService {

	@Autowired
	ClaimInterfaceLogService interfaceLogService;
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;


	private static Logger logger = LoggerFactory.getLogger(LinkageServiceImpl.class);
	
	

	/*
	 * @see ins.sino.claimcar.linkage.service.LinkageService#getCurrentVersion()
	 * 
	 * @throws Exception
	 */
	/*
	 * @see ins.sino.claimcar.linkage.service.LinkageService#getCurrentVersion()
	 * 
	 * @throws Exception
	 */
	/*
	 * @see ins.sino.claimcar.linkage.service.LinkageService#getCurrentVersion()
	 * 
	 * @throws Exception
	 */
	@Override
	public CurrentVersionVo searchCurrentVersion() {
		String rootUrl=SpringProperties.getProperty("policyLink_versionUrl");
		String APPID=SpringProperties.getProperty("policyLink_appid");
		String SIGN_KEY=SpringProperties.getProperty("policyLink_sign");
	
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", APPID);
		map.put("time", DateUtil.getToDate(DateUtil.PATTERN_ALL).getTime() + "");
		String sign = getSign(map, SIGN_KEY);
		String url = "";             //----/v1/getcurrentversion?format=xml   //--/JinBaoGetcurrentversion?format=xml
		url += "&appid=" + map.get("appid");
		url += "&time=" + map.get("time");
		url += "&sign=" + sign;

		//String returnXml = LinkageUtil.requestLinkage(url, 10);
		String returnXml =LinkageUtil.loadJSON(rootUrl, url);
		logger.info("警保报文"+returnXml);
		// 解析返回的数据
		CurrentVersionVo versionVo = new CurrentVersionVo();
		if(returnXml!=null && !"".equals(returnXml)){
			versionVo = XstreamFactory.xmlToObj(returnXml,
					CurrentVersionVo.class);
		}

		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();// 保存日志
		logVo.setBusinessType(BusinessType.PolicyLinkAge_currentVersion.name());
		logVo.setBusinessName(BusinessType.PolicyLinkAge_currentVersion
				.getName());
		logVo.setRequestUrl(url);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logVo.setRegistNo(sdf.format(new Date()));
		logVo.setResponseXml(returnXml);
		if (versionVo != null && "0".equals(versionVo.getCode())) {
			logVo.setStatus("1");
		} else {
			logVo.setStatus("0");
		}
		logVo.setOperateNode("Version");
		logVo.setErrorCode(versionVo.getCode());
		logVo.setErrorMessage(versionVo.getResultMsg());
		this.savePolicyLinkAgeInfos(logVo);
		if (versionVo != null && !"0".equals(versionVo.getCode())) {
			throw new IllegalArgumentException("请求服务错误，"
					+ versionVo.getResultMsg());
		} else {
		    logger.info(versionVo.getResultMsg());
		}

		return versionVo;
	}

	/*
	 * @see
	 * ins.sino.claimcar.linkage.service.LinkageService#savePolicyLinkAgeInfo
	 * (java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * @param businessType
	 * 
	 * @param caseCode
	 * 
	 * @param xml
	 */

	/*
	 * @see
	 * ins.sino.claimcar.linkage.service.LinkageService#savePolicyLinkAgeInfo
	 * (java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * @param businessType
	 * 
	 * @param caseCode
	 * 
	 * @param xml
	 */

	/*
	 * @see
	 * ins.sino.claimcar.linkage.service.LinkageService#savePolicyLinkAgeInfo
	 * (java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * @param businessType
	 * 
	 * @param caseCode
	 * 
	 * @param xml
	 */
	@Override
	public void savePolicyLinkAgeInfos(ClaimInterfaceLogVo logVo) {
		Date date = new Date();
		logVo.setServiceType("link");
		logVo.setCreateUser("自动");
		logVo.setCreateTime(date);
		logVo.setRequestTime(date);
		init();
		interfaceLogService.save(logVo);
	}

	private void init() {
		if (interfaceLogService == null) {
			interfaceLogService = (ClaimInterfaceLogService) Springs
					.getBean("interfaceLogService");
		}

	}

	@Override
	public CaseInfosResultVo searchCaseInfos(Long startTime, Long endTime,
			String childAppid, int pageNumber) {
		String rootUrl=SpringProperties.getProperty("policyLink_caseInfoUrl");
		String APPID=SpringProperties.getProperty("policyLink_appid");
		String SIGN_KEY=SpringProperties.getProperty("policyLink_sign");
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", APPID);
		map.put("time", DateUtil.getToDate(DateUtil.PATTERN_ALL).getTime() + "");
		String sign = getSign(map, SIGN_KEY);
		String url = "" ;   //--- /v1/getcaseinfos?format=xml
		url += "&appid=" + map.get("appid");
		url += "&time=" + map.get("time");
		url += "&sign=" + sign;
		url += "&pagenumber=" + pageNumber;
		url += "&pagesize=" + "100";
		if (startTime != null) {
			url += "&starttime=" + startTime;
		}
		if (endTime != null) {
			url += "&endtime=" + endTime;
		}
		if (childAppid != null) {
			url += "&childappid=" + childAppid;
		}

		String returnXml = LinkageUtil.loadJSON(rootUrl, url);
		logger.info("警保报文"+returnXml);
		// 解析返回的数据
		CaseInfosResultVo resultVo = XstreamFactory.xmlToObj(returnXml,
				CaseInfosResultVo.class);

		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();// 保存日志
		logVo.setBusinessType(BusinessType.PolicyLinkAge_caseInfos.name());
		logVo.setBusinessName(BusinessType.PolicyLinkAge_caseInfos.getName());
		logVo.setRequestUrl(url);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logVo.setRegistNo(sdf.format(new Date()));
		logVo.setResponseXml(returnXml);
		if (resultVo != null && "0".equals(resultVo.getCode())) {
			logVo.setStatus("1");
		} else {
			logVo.setStatus("0");
		}
		logVo.setOperateNode("CaseList");
		logVo.setErrorCode(resultVo.getCode());
		logVo.setErrorMessage(resultVo.getMsg());
		this.savePolicyLinkAgeInfos(logVo);

		if (resultVo != null && !"0".equals(resultVo.getCode())) {
			throw new IllegalArgumentException("请求服务错误，" + resultVo.getMsg());
		} else {
		    if(resultVo.getInfo() != null){
		        logger.info("resultVo.getInfo().getDataList()"+resultVo.getInfo().getDataList());
		        logger.info("resultVo.getInfo().getData()"+resultVo.getInfo().getData());
		    }
		    logger.info(resultVo.getMsg());
		    logger.info(resultVo.getCode());
		}

		return resultVo;
	}

	/*
	 * @see
	 * ins.sino.claimcar.linkage.service.LinkageService#getCaseDetail(java.lang
	 * .String, java.lang.String, int)
	 * 
	 * @param caseNumber
	 * 
	 * @param childappid
	 * 
	 * @param imageType
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	@Override
	public CaseDetailResultVo searchCaseDetail(String caseNumber,
			String childappid, int imageType) {
		String rootUrl=SpringProperties.getProperty("policyLink_caseDetailUrl");
		String APPID=SpringProperties.getProperty("policyLink_appid");
		String SIGN_KEY=SpringProperties.getProperty("policyLink_sign");
		
		if (caseNumber == null) {
			throw new IllegalArgumentException("参数错误  caseNumber ："
					+ caseNumber + "为空");
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", APPID);
		map.put("time", DateUtil.getToDate(DateUtil.PATTERN_ALL).getTime() + "");
		map.put("casenumber", caseNumber);
		String sign = getSign(map, SIGN_KEY);
		String url = rootUrl ;   //---/v1/getcasedetail?format=xml
		url += "&appid=" + map.get("appid");   
		url += "&time=" + map.get("time");
		url += "&casenumber=" + map.get("casenumber");
		url += "&sign=" + sign;
		if (childappid != null) {
			url += "&childappid=" + childappid;
		}
		if (imageType == 1 || imageType == 2) {
			url += "&imageType=" + imageType;
		}

		String returnXml = LinkageUtil.loadJSON(rootUrl, url);
		

		logger.info("警保报文"+returnXml);
		// 解析返回的数据
		CaseDetailResultVo resultVo = XstreamFactory.xmlToObj(returnXml,
				CaseDetailResultVo.class);

		ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();// 保存日志
		logVo.setBusinessType(BusinessType.PolicyLinkAge_caseDetails.name());
		logVo.setBusinessName(BusinessType.PolicyLinkAge_caseDetails.getName());
		logVo.setRequestUrl(url);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logVo.setRegistNo(sdf.format(new Date()));
		logVo.setResponseXml(returnXml);
		if (resultVo != null && "0".equals(resultVo.getCode())) {
			logVo.setStatus("1");
		} else {
			logVo.setStatus("0");
		}
		logVo.setOperateNode("CaseDetails");
		logVo.setErrorCode(resultVo.getCode());
		logVo.setErrorMessage(resultVo.getMsg());
		this.savePolicyLinkAgeInfos(logVo);
		if (resultVo != null && !"0".equals(resultVo.getCode())) {
			throw new IllegalArgumentException("请求服务错误，" + resultVo.getMsg());
		} else {
		    logger.info(resultVo.getCode());
		    logger.info(resultVo.getMsg());
		}

		return resultVo;
	}

	public static void main(String[] args) throws Exception {

		LinkageServiceImpl linkAge = new LinkageServiceImpl();
//		List<PrplOldClaimRiskInfoVo> old = new ArrayList<PrplOldClaimRiskInfoVo>();
//		System.out.println(old);
		// linkAge.searchCurrentVersion();
		// Date endTime = new Date();
		// endTime = DateUtils.addDays(endTime, -30*2);
		// Date startTime = DateUtils.addDays(endTime, -30*4);
		// linkAge.searchCaseInfos(startTime.getTime(), endTime.getTime(),
		// null,1);
//		String picUrl = "http://wx.chexiaoy.com/hbjb-mng/fileinmix/getimage?time=205732&model=976133d4b2db7cb71b281284db7a638d&imageId=24893&type=0";
//		System.out.println(linkAge.downloadToLocal(picUrl,"4"));
		//linkAge.updateSmallPic();
		String path = Class.class.getClass().getResource("/").getPath();
		logger.info(path);
	
	}

	private String getSign(Map<String, String> map, String ASSURANCE_SIGN_KEY) {
		return BuildMysign(map, ASSURANCE_SIGN_KEY);
	}

	/**
	 * 功能：生成签名结果
	 * 
	 * @param sArray
	 *            要签名的数组
	 * @param key
	 *            安全校验码
	 * @return 签名结果字符串
	 */
	@SuppressWarnings("rawtypes")
	public String BuildMysign(Map sArray, String key) {
		String prestr = CreateLinkString(sArray); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		prestr = prestr + key;
		// 把拼接后的字符串再与安全校验码直接连接起来
		return DigestUtils.md5Hex(prestr);
	}

	/**
	 * 功能：把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String CreateLinkString(Map params) {
		List keys = new ArrayList(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/* 
	 * 1 . 请求警保版本信息接口 2. 检查版本有无更新 更新则请求 警保案件列表接口 3. 根据请求列表接口 请求警保案件明细 接口
	 * 
	 * @see
	 * ins.sino.claimcar.policyLinkage.service.LinkageService#autoSaveLinkInfo()
	 * 
	 * @throws Exception
	 */

	@Override
	public void autoSaveLinkInfo(){

        CurrentVersionVo versionVo = this.searchCurrentVersion(); // 检查当前版本
        if(versionVo != null && versionVo.getResultInfo() !=null && versionVo.getResultInfo().getCurrentVersionDataVo() != null
                && StringUtils.isNotBlank(versionVo.getResultInfo().getCurrentVersionDataVo().getVersion())){
            PrpLLinkVersionVo versionInfo = this.findVersionInfo(versionVo); // 检查历史版本
            if (versionInfo == null && versionVo != null
                    && "0".equals(versionVo.getCode())) {
                String versionNumber = versionVo.getResultInfo().getCurrentVersionDataVo().getVersion(); // 版本号
                this.saveVersionInfo(versionVo); // save versionVo 保存版本信息
                // String startTime =
                // versionVo.getResultInfo().getCurrentVersionDataVo().getLastupdatetime();
                //String startTime = "2016-7-02T15:45:46+08:00";
                //Date start = this.toDate(startTime);
                CaseInfosResultVo caseInfo = this.searchCaseInfos(null,null, null, 1);
                if (caseInfo != null && "0".contains(caseInfo.getCode())) {
                	List<PrpLLinkCaseVo> caseVo = this.findLinkCase(versionNumber);
                    if (caseVo == null || caseVo.size() == 0) { // 检查是否已经被保存
                        this.saveLinkcase(caseInfo, versionNumber);// 保存 案件列表
                        this.saveCaseListInfo(caseInfo, versionNumber); // 保存案件子表
                    }
                    //判是否为null
                    int pageData = 0;
                    int size = 0;
                    List<CaseInfosDataListVo> list = caseInfo.getInfo().getDataList();
                    if(caseInfo.getInfo() != null && caseInfo.getInfo().getData() != null){
                        pageData = Integer.parseInt(caseInfo.getInfo().getData()); 
                    }
                    
                    int times = pageData % 100 == 0 ? pageData / 100: pageData / 100 + 1;
                    for(int i = 1; i <= times; i++){
                    	caseInfo = this.searchCaseInfos(null, null,null, i);
                        if (caseInfo != null&& "0".contains(caseInfo.getCode())) {
                            this.saveCaseListInfo(caseInfo, versionNumber); // 保存案件列表
                            size = caseInfo.getInfo().getDataList().size();
                            list = caseInfo.getInfo().getDataList();
                            for (int j = 0; j < size; j++) {
                                String caseNumber = list.get(j).getCaseNumber();
                                CaseDetailResultVo caseDetail = this.searchCaseDetail(caseNumber, null, 1);
                                if (caseDetail != null&& "0".equals(caseDetail.getCode())) {
                                    List<PrpLLinkCaseMainVo> mainList = this.findLinkCarMain(caseDetail);
                                    if (mainList == null|| mainList.size() == 0) { // 检查是否被保存
                                        this.saveCaseDatail(caseDetail,versionNumber); // 保存案件明细
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
	public Date toDate(String str) {

		if (StringUtils.isNotEmpty(str)) {
			String[] strAttr = str.split("\\+");
			if (strAttr != null) {
				str = strAttr[0].replace("T", " ");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date start = sdf.parse(str);
				return start;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public PrpLLinkVersionVo findVersionInfo(CurrentVersionVo versionVo){
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("versionNumber", versionVo.getResultInfo()
				.getCurrentVersionDataVo().getVersion());
		List<PrpLLinkVersion> versionPoList = new ArrayList<PrpLLinkVersion>();
		versionPoList = databaseDao.findAll(PrpLLinkVersion.class, queryRule);
		List<PrpLLinkVersionVo> versionVoList = new ArrayList<PrpLLinkVersionVo>();
		if (versionPoList != null && versionPoList.size() > 0) {
			for (PrpLLinkVersion po : versionPoList) {
				PrpLLinkVersionVo vo = Beans.copyDepth().from(po)
						.to(PrpLLinkVersionVo.class);
				versionVoList.add(vo);
			}
			return versionVoList.get(0);
		}
		return null;
	}

	@Override
	public PrpLLinkVersionVo saveVersionInfo(CurrentVersionVo versionVo){
		PrpLLinkVersionVo linkVersion = new PrpLLinkVersionVo();

		linkVersion.setVersionNumber(versionVo.getResultInfo()
				.getCurrentVersionDataVo().getVersion());
		linkVersion.setLastUpdateTime(versionVo.getResultInfo()
				.getCurrentVersionDataVo().getLastupdatetime());
		linkVersion.setCreateTime(new Date());

		PrpLLinkVersion versionPo = Beans.copyDepth().from(linkVersion)
				.to(PrpLLinkVersion.class);
		databaseDao.save(PrpLLinkVersion.class, versionPo);
		Beans.copyDepth().from(versionPo).to(PrpLLinkVersionVo.class);

		linkVersion.setId(versionPo.getId());
		return linkVersion;
	}

	@Override
	public void saveLinkcase(CaseInfosResultVo caseInfos, String versionNumber){
		PrpLLinkCaseVo caseVo = new PrpLLinkCaseVo();
		Date createTime = new Date();

		caseVo.setData(Long.parseLong(caseInfos.getInfo().getData()));
		caseVo.setVersionNUmber(versionNumber);
		caseVo.setCreateTime(createTime);
		PrpLLinkCase casePo = Beans.copyDepth().from(caseVo)
				.to(PrpLLinkCase.class); // 保存主表
		databaseDao.save(PrpLLinkCase.class, casePo);

	}

	@Override
	public void saveCaseListInfo(CaseInfosResultVo caseInfos,
			String versionNumber){

		List<CaseInfosDataListVo> listVo = caseInfos.getInfo().getDataList();
		if (listVo != null && listVo.size() > 0) {
			for (CaseInfosDataListVo data : listVo) {
				List<PrpLLinkCaseInfoVo> oldCaseInfo = this
						.findLinkCaseInfo(data.getCaseNumber());
				if (oldCaseInfo == null || oldCaseInfo.size() == 0) { // 检查历史案件
					PrpLLinkCaseInfoVo caseInfoVo = new PrpLLinkCaseInfoVo();
					caseInfoVo.setCaseNumber(data.getCaseNumber());
					caseInfoVo.setCaseType(data.getCaseType());
					caseInfoVo.setVersionNumber(versionNumber);
					caseInfoVo.setAccidentTime(this.toDate(data
							.getAccidentTime()));
					caseInfoVo.setExternalType(data.getExternaltype());
					caseInfoVo.setHphm(data.getHphm());
					caseInfoVo.setStatus(data.getStatus());
					caseInfoVo.setCreateTime(new Date());

					PrpLLinkCaseInfo caseInfoPo = Beans.copyDepth()
							.from(caseInfoVo).to(PrpLLinkCaseInfo.class); // 保存子表
					databaseDao.save(PrpLLinkCaseInfo.class, caseInfoPo);
				}
			}
		}

	}

	@Override
	public void saveCaseList(List<CaseInfosDataListVo> caseList,
			String versionNumber) throws Exception {

		if (caseList != null && caseList.size() > 0) { // 保存案件信息表
			Date createTime = new Date();
			for (CaseInfosDataListVo data : caseList) {
				PrpLLinkCaseInfoVo caseInfoVo = new PrpLLinkCaseInfoVo();
				caseInfoVo.setCaseNumber(data.getCaseNumber());
				caseInfoVo.setCaseType(data.getCaseType());
				caseInfoVo.setVersionNumber(versionNumber);
				caseInfoVo.setAccidentTime(this.toDate(data.getAccidentTime()));
				caseInfoVo.setExternalType(data.getExternaltype());
				caseInfoVo.setHphm(data.getHphm());
				caseInfoVo.setStatus(data.getStatus());
				caseInfoVo.setCreateTime(createTime);

				PrpLLinkCaseInfo caseInfoPo = Beans.copyDepth()
						.from(caseInfoVo).to(PrpLLinkCaseInfo.class);
				databaseDao.save(PrpLLinkCaseInfo.class, caseInfoPo);
			}
		}

	}

	@Override
	public void saveCaseDatail(CaseDetailResultVo caseDetail,String versionNumber) {
		Date createTime = new Date();
		CaseDetailDataVo dataVo = caseDetail.getInfo().getData();
		CaseDetailBaseInfoVo baseInfo = dataVo.getBaseInfo();
		String caseId = dataVo.getCaseId();
		String caseNumber = "";
		StringBuffer itemsName = new StringBuffer();
		String itemsNames = "";
		if (dataVo.getLossInfos() != null && dataVo.getLossInfos().size() > 0) {
			for (int i = 0; i < dataVo.getLossInfos().size(); i++) {
				itemsName.append(dataVo.getLossInfos().get(i).getItemsName());
				itemsName.append(",");
			}
			itemsNames = itemsName.substring(0, itemsName.length() - 1);
		}

		if (baseInfo != null) {
			// 保存主表
			PrpLLinkCaseMainVo mainVo = new PrpLLinkCaseMainVo();

			mainVo.setCaseId(caseId);
			caseNumber = baseInfo.getCaseNumber();
			mainVo.setCaseNumber(caseNumber);
			mainVo.setAccdientAddress(baseInfo.getAccidentAddress());
			mainVo.setAccidentTime(this.toDate(baseInfo.getAccidentTime()));
			mainVo.setArriveTime(this.toDate(baseInfo.getArriveTime()));
			mainVo.setAssidentDescribe(baseInfo.getAccidentDescribe());
			mainVo.setCaseType(baseInfo.getCaseType());
			mainVo.setCreateTime(createTime);
			mainVo.setDistrict(baseInfo.getDistrict());
			mainVo.setItemsName(itemsNames);
			mainVo.setLat(new BigDecimal(baseInfo.getLat()==null?"0":baseInfo.getLat()));
			mainVo.setLeaveTime(this.toDate(baseInfo.getLeaveTime()));
			mainVo.setLng(new BigDecimal(baseInfo.getLng()==null?"0":baseInfo.getLng()));
			mainVo.setResptime(this.toDate(baseInfo.getRespTime()));
			mainVo.setRespUserName(baseInfo.getRespUserName());
			mainVo.setRespUserPhone(baseInfo.getRespUserPhone());
			mainVo.setStartTime(this.toDate(baseInfo.getStartTime()));
			mainVo.setStatus(baseInfo.getStatus());
			mainVo.setSurveyMembers(baseInfo.getSurveyMembers());
			mainVo.setSurveyMembersPhone(baseInfo.getSurveyMembersPhone());

			PrpLLinkCaseMain mainPo = Beans.copyDepth().from(mainVo)
					.to(PrpLLinkCaseMain.class);
			databaseDao.save(PrpLLinkCaseMain.class, mainPo);
		}

		List<CaseDetailInjuredInfosVo> injuredInfos = dataVo.getInjuredInfos();

		if (injuredInfos != null && injuredInfos.size() > 0) { // 保存人伤表
			for (CaseDetailInjuredInfosVo injuredInfo : injuredInfos) {
				PrpLLinkCaseInjuredVo injuredVo = new PrpLLinkCaseInjuredVo();

				injuredVo.setCaseId(caseId);
				injuredVo.setCaseNumber(caseNumber);
				injuredVo.setAge(injuredInfo.getAge());
				injuredVo.setCreateTime(createTime);
				injuredVo.setHj(injuredInfo.getHj());
				injuredVo.setName(injuredInfo.getName());
				injuredVo.setOrders(injuredInfo.getOrders());
				injuredVo.setSex(injuredInfo.getSex());
				injuredVo.setSfzmhm(injuredInfo.getSfzmhm());

				PrpLLinkCaseInjured injuredPo = Beans.copyDepth()
						.from(injuredVo).to(PrpLLinkCaseInjured.class);
				databaseDao.save(PrpLLinkCaseInjured.class, injuredPo);
			}
		}

		CaseDetailTargetCarInfoVo targetCarVo = dataVo.getTargetCarInfo();
		if (targetCarVo != null) { // 保存标的车
			PrpLLinkCaseCarVo carVo = new PrpLLinkCaseCarVo();

			carVo.setCaseNumber(caseNumber);
			carVo.setCaseId(caseId);
			carVo.setCreateTime(createTime);
			carVo.setDriverName(targetCarVo.getDriverName());
			carVo.setHphm(targetCarVo.getHphm());
			carVo.setInjureLocation(targetCarVo.getInjureLocation());
			carVo.setIsResp(targetCarVo.getIsResp());
			carVo.setJqx(new BigDecimal(targetCarVo.getJqx()));
			carVo.setJqxOther(targetCarVo.getSyxOther());
			carVo.setOrders(targetCarVo.getOrders());
			carVo.setPhone(targetCarVo.getPhone());
			carVo.setSyx(new BigDecimal(targetCarVo.getSyx()));
			carVo.setSyxOther(targetCarVo.getSyxOther());
			carVo.setTargetOrThirdCarType("0");
			carVo.setType(targetCarVo.getType());

			PrpLLinkCaseCar carPo = Beans.copyDepth().from(carVo)
					.to(PrpLLinkCaseCar.class);
			databaseDao.save(PrpLLinkCaseCar.class, carPo);
		}

		List<CaseDetailThirdCarInfosVo> thirdCarVos = dataVo.getThirdCarInfos();
		if (thirdCarVos != null && thirdCarVos.size() > 0) { // 保存三者车
			for (CaseDetailThirdCarInfosVo thirdCarVo : thirdCarVos) {

				PrpLLinkCaseCarVo carVo = new PrpLLinkCaseCarVo();

				carVo.setCaseNumber(caseNumber);
				carVo.setCaseId(caseId);
				carVo.setCreateTime(createTime);
				carVo.setDriverName(thirdCarVo.getDriverName());
				carVo.setHphm(thirdCarVo.getHphm());
				carVo.setInjureLocation(thirdCarVo.getInjureLocation());
				carVo.setIsResp(thirdCarVo.getIsResp());
				carVo.setJqx(new BigDecimal(thirdCarVo.getJqx()));
				carVo.setJqxOther(thirdCarVo.getSyxOther());
				carVo.setOrders(thirdCarVo.getOrders());
				carVo.setPhone(thirdCarVo.getPhone());
				carVo.setSyx(new BigDecimal(thirdCarVo.getSyx()));
				carVo.setSyxOther(thirdCarVo.getSyxOther());
				carVo.setTargetOrThirdCarType("1");
				carVo.setType(thirdCarVo.getType());

				PrpLLinkCaseCar carPo = Beans.copyDepth().from(carVo)
						.to(PrpLLinkCaseCar.class);
				databaseDao.save(PrpLLinkCaseCar.class, carPo);
			}
		}

		List<CaseDetailImgList1Vo> imgList1 = dataVo.getImgList1();
		if (imgList1 != null && imgList1.size() > 0) { // 人车合影
			for (CaseDetailImgList1Vo imgList : imgList1) {
				PrpLLinkCaseImgVo imgVo = new PrpLLinkCaseImgVo();

				imgVo.setCaseId(caseId);
				imgVo.setCaseNumber(caseNumber);
				imgVo.setCreateTime(createTime);
				imgVo.setImgId(imgList.getImgId());
				imgVo.setOrders(new BigDecimal(imgList.getOrders()));
				imgVo.setPicUrl(imgList.getPicUrl());
				String localAddr = "";
				localAddr = this.downloadToLocal(imgList.getPicUrl(), imgList.getImgId());
				imgVo.setSmallPicUrl(localAddr); // 图片保存方式待处理
				imgVo.setTags(imgList.getTags());
				imgVo.setType(imgList.getType());

				PrpLLinkCaseImg imgPo = Beans.copyDepth().from(imgVo)
						.to(PrpLLinkCaseImg.class);
				databaseDao.save(PrpLLinkCaseImg.class, imgPo);
			}
		}

		List<CaseDetailImgList2Vo> imgList2 = dataVo.getImgList2();
		if (imgList2 != null && imgList2.size() > 0) { // 车驾号钢印照片
			for (CaseDetailImgList2Vo imgList : imgList2) {
				PrpLLinkCaseImgVo imgVo = new PrpLLinkCaseImgVo();

				imgVo.setCaseId(caseId);
				imgVo.setCaseNumber(caseNumber);
				imgVo.setCreateTime(createTime);
				imgVo.setImgId(imgList.getImgId());
				imgVo.setOrders(new BigDecimal(imgList.getOrders()));
				imgVo.setPicUrl(imgList.getPicUrl());
				String localAddr = "";
				localAddr = this.downloadToLocal(imgList.getPicUrl(), imgList.getImgId());
				imgVo.setSmallPicUrl(localAddr); // 图片保存方式待处理
				imgVo.setTags(imgList.getTags());
				imgVo.setType(imgList.getType());

				PrpLLinkCaseImg imgPo = Beans.copyDepth().from(imgVo)
						.to(PrpLLinkCaseImg.class);
				databaseDao.save(PrpLLinkCaseImg.class, imgPo);
			}
		}

		List<CaseDetailImgList3Vo> imgList3 = dataVo.getImgList3();
		if (imgList3 != null && imgList3.size() > 0) { // 环境照片
			for (CaseDetailImgList3Vo imgList : imgList3) {
				PrpLLinkCaseImgVo imgVo = new PrpLLinkCaseImgVo();

				imgVo.setCaseId(caseId);
				imgVo.setCaseNumber(caseNumber);
				imgVo.setCreateTime(createTime);
				imgVo.setImgId(imgList.getImgId());
				imgVo.setOrders(new BigDecimal(imgList.getOrders()));
				imgVo.setPicUrl(imgList.getPicUrl());
				String localAddr = "";
				localAddr = this.downloadToLocal(imgList.getPicUrl(), imgList.getImgId());
				imgVo.setSmallPicUrl(localAddr); // 图片保存方式待处理
				imgVo.setTags(imgList.getTags());
				imgVo.setType(imgList.getType());

				PrpLLinkCaseImg imgPo = Beans.copyDepth().from(imgVo)
						.to(PrpLLinkCaseImg.class);
				databaseDao.save(PrpLLinkCaseImg.class, imgPo);
			}
		}

		List<CaseDetailImgList4Vo> imgList4 = dataVo.getImgList4();
		if (imgList4 != null && imgList4.size() > 0) { // 碰撞照片
			for (CaseDetailImgList4Vo imgList : imgList4) {
				PrpLLinkCaseImgVo imgVo = new PrpLLinkCaseImgVo();

				imgVo.setCaseId(caseId);
				imgVo.setCaseNumber(caseNumber);
				imgVo.setCreateTime(createTime);
				imgVo.setImgId(imgList.getImgId());
				imgVo.setOrders(new BigDecimal(imgList.getOrders()));
				imgVo.setPicUrl(imgList.getPicUrl());
				String localAddr = "";
				localAddr = this.downloadToLocal(imgList.getPicUrl(), imgList.getImgId());
				imgVo.setSmallPicUrl(localAddr); // 图片保存方式待处理
				imgVo.setTags(imgList.getTags());
				imgVo.setType(imgList.getType());

				PrpLLinkCaseImg imgPo = Beans.copyDepth().from(imgVo)
						.to(PrpLLinkCaseImg.class);
				databaseDao.save(PrpLLinkCaseImg.class, imgPo);
			}
		}

		List<CaseDetailImgList5Vo> imgList5 = dataVo.getImgList5();
		if (imgList5 != null && imgList5.size() > 0) { // 人物/物损照片
			for (CaseDetailImgList5Vo imgList : imgList5) {
				PrpLLinkCaseImgVo imgVo = new PrpLLinkCaseImgVo();

				imgVo.setCaseId(caseId);
				imgVo.setCaseNumber(caseNumber);
				imgVo.setCreateTime(createTime);
				imgVo.setImgId(imgList.getImgId());
				imgVo.setOrders(new BigDecimal(imgList.getOrders()));
				imgVo.setPicUrl(imgList.getPicUrl());
				String localAddr = "";
				localAddr = this.downloadToLocal(imgList.getPicUrl(), imgList.getImgId());
				imgVo.setSmallPicUrl(localAddr); // 图片保存方式待处理
				imgVo.setTags(imgList.getTags());
				imgVo.setType(imgList.getType());

				PrpLLinkCaseImg imgPo = Beans.copyDepth().from(imgVo)
						.to(PrpLLinkCaseImg.class);
				databaseDao.save(PrpLLinkCaseImg.class, imgPo);
			}
		}

		List<CaseDetailImgList6Vo> imgList6 = dataVo.getImgList6();
		if (imgList6 != null && imgList6.size() > 0) { // 证件照片照片
			for (CaseDetailImgList6Vo imgList : imgList6) {
				PrpLLinkCaseImgVo imgVo = new PrpLLinkCaseImgVo();

				imgVo.setCaseId(caseId);
				imgVo.setCaseNumber(caseNumber);
				imgVo.setCreateTime(createTime);
				imgVo.setImgId(imgList.getImgId());
				imgVo.setOrders(new BigDecimal(imgList.getOrders()));
				imgVo.setPicUrl(imgList.getPicUrl());
				String localAddr = "";
				localAddr = this.downloadToLocal(imgList.getPicUrl(), imgList.getImgId());
				imgVo.setSmallPicUrl(localAddr); // 图片保存方式待处理
				imgVo.setTags(imgList.getTags());
				imgVo.setType(imgList.getType());

				PrpLLinkCaseImg imgPo = Beans.copyDepth().from(imgVo)
						.to(PrpLLinkCaseImg.class);
				databaseDao.save(PrpLLinkCaseImg.class, imgPo);
			}
		}

		List<CaseDetailImgList7Vo> imgList7 = dataVo.getImgList7();
		if (imgList7 != null && imgList7.size() > 0) { // 交强险标识照片
			for (CaseDetailImgList7Vo imgList : imgList7) {
				PrpLLinkCaseImgVo imgVo = new PrpLLinkCaseImgVo();

				imgVo.setCaseId(caseId);
				imgVo.setCaseNumber(caseNumber);
				imgVo.setCreateTime(createTime);
				imgVo.setImgId(imgList.getImgId());
				imgVo.setOrders(new BigDecimal(imgList.getOrders()));
				imgVo.setPicUrl(imgList.getPicUrl());
				String localAddr = "";
				localAddr = this.downloadToLocal(imgList.getPicUrl(), imgList.getImgId());
				imgVo.setSmallPicUrl(localAddr); // 图片保存方式待处理
				imgVo.setTags(imgList.getTags());
				imgVo.setType(imgList.getType());

				PrpLLinkCaseImg imgPo = Beans.copyDepth().from(imgVo)
						.to(PrpLLinkCaseImg.class);
				databaseDao.save(PrpLLinkCaseImg.class, imgPo);
			}
		}

		List<CaseDetailImgList8Vo> imgList8 = dataVo.getImgList8();
		if (imgList8 != null && imgList8.size() > 0) { // 定责书/协议书照片
			for (CaseDetailImgList8Vo imgList : imgList8) {
				PrpLLinkCaseImgVo imgVo = new PrpLLinkCaseImgVo();

				imgVo.setCaseId(caseId);
				imgVo.setCaseNumber(caseNumber);
				imgVo.setCreateTime(createTime);
				imgVo.setImgId(imgList.getImgId());
				imgVo.setOrders(new BigDecimal(imgList.getOrders()));
				imgVo.setPicUrl(imgList.getPicUrl());
				String localAddr = "";
				localAddr = this.downloadToLocal(imgList.getPicUrl(), imgList.getImgId());
				imgVo.setSmallPicUrl(localAddr); // 图片保存方式待处理
				imgVo.setTags(imgList.getTags());
				imgVo.setType(imgList.getType());

				PrpLLinkCaseImg imgPo = Beans.copyDepth().from(imgVo)
						.to(PrpLLinkCaseImg.class);
				databaseDao.save(PrpLLinkCaseImg.class, imgPo);
			}
		}
	}

	@Override
	public ResultPage<PolicyLinkResultVo> findCaseList(PolicyLinkQueryVo queryVo)
			throws Exception {
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append(" select cmain.caseId,cmain.accidentTime,cmain.respUserName,cmain.respUserPhone,cmain.status,cmain.caseType,car.hphm,car.driverName,car.phone,car.isResp ");
		sqlUtil.append(" from PRPLLINKCASEMAIN cmain,PRPLLINKCASECAR car where cmain.caseId = car.caseId  ");
		sqlUtil.append(" AND car.targetOrThirdCarType = ? ");
		sqlUtil.addParamValue("0");
		if (StringUtils.isNotBlank(queryVo.getRespUserName())) {
			sqlUtil.append("AND cmain.respUserName like ? ");
			sqlUtil.addParamValue("%"+queryVo.getRespUserName()+"%");
		}
		if (StringUtils.isNotBlank(queryVo.getRespUserPhone())) {
			sqlUtil.append("AND cmain.respUserPhone like ? ");
			sqlUtil.addParamValue("%"+queryVo.getRespUserPhone()+"%");
		}
		if (StringUtils.isNotBlank(queryVo.getDriverName())) {
			sqlUtil.append("AND car.driverName like ? ");
			sqlUtil.addParamValue("%"+queryVo.getDriverName()+"%");
		}
		if (StringUtils.isNotBlank(queryVo.getHphm())) {
			sqlUtil.append("AND car.hphm like ? ");
			sqlUtil.addParamValue("%"+queryVo.getHphm()+"%");
		}
		sqlUtil.andDate(queryVo, "cmain", "accidentTime");
		sqlUtil.append(" order by cmain.createTime desc ");
		// 开始记录数
		int start = queryVo.getStart();
		// 查询记录数量
		int length = queryVo.getLength();

		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		logger.info("taskQrySql=" + sql);
		logger.info("ParamValues=" + ArrayUtils.toString(values));

		Page<Object[]> page = baseDaoService.pagedSQLQuery(sql, start, length,values);

		List<PolicyLinkResultVo> resultVoList = new ArrayList<PolicyLinkResultVo>();
		for (int i = 0; i < page.getResult().size(); i++) {
			Object[] obj = page.getResult().get(i);
			PolicyLinkResultVo resultVo = new PolicyLinkResultVo();
			resultVo.setCaseId(obj[0] == null ? "" : obj[0].toString());
			resultVo.setRespUserName(obj[2] == null ? "" : obj[2].toString());
			resultVo.setRespUserPhone(obj[3] == null ? "" : obj[3].toString());
			if (obj[5] == null) { // 翻译案件状态
				resultVo.setCaseType("");
			} else {
				if ("1".equals(obj[5].toString())) {
					resultVo.setCaseType("单方事故");
				} else if ("3".equals(obj[5].toString())) {
					resultVo.setCaseType("双方事故");
				} else {
					resultVo.setCaseType("多方事故");
				}
			}
			// resultVo.setCaseType(obj[5]==null? "":obj[5].toString());
			if (obj[1] != null) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date date = format.parse(obj[1].toString());
				resultVo.setAccidentTime(date);
			}
			resultVo.setDriverName(obj[7] == null ? "" : obj[7].toString());
			resultVo.setPhone(obj[8] == null ? "" : obj[8].toString());
			resultVo.setHphm(obj[6] == null ? "" : obj[6].toString());
			resultVo.setStatus(obj[4] == null ? "" : obj[4].toString());

			if (obj[9] == null) {
				resultVo.setIsResp("");
			} else {
				if ("1".equals(obj[9].toString())) {
					resultVo.setIsResp("是");
				} else {
					resultVo.setIsResp("否");
				}
			}
			// resultVo.setIsResp(obj[9]==null? "":obj[9].toString());

			resultVoList.add(resultVo);
		}
		ResultPage<PolicyLinkResultVo> resultPage = new ResultPage<PolicyLinkResultVo>(
				start, length, page.getTotalCount(), resultVoList);
		return resultPage;
	}

	@Override
	public PrpLLinkCaseMainVo findMainVo(String caseId) throws Exception {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("caseId", caseId);
		List<PrpLLinkCaseMain> mainPo = databaseDao.findAll(
				PrpLLinkCaseMain.class, queryRule);
		if (mainPo != null && mainPo.size() > 0) {
			PrpLLinkCaseMainVo mainVo = Beans.copyDepth().from(mainPo.get(0))
					.to(PrpLLinkCaseMainVo.class);
			return mainVo;
		}
		return null;
	}

	@Override
	public List<PrpLLinkCaseCarVo> findCarList(String caseId) throws Exception {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("caseId", caseId);
		// queryRule.addDescOrder(propertyName);
		// queryRule.addAscOrder("orders");
		List<PrpLLinkCaseCar> carPo = databaseDao.findAll(
				PrpLLinkCaseCar.class, queryRule);
		if (carPo != null && carPo.size() > 0) {
			List<PrpLLinkCaseCarVo> carList = new ArrayList<PrpLLinkCaseCarVo>();
			for (int i = 0; i < carPo.size(); i++) {
				// PrpLLinkCaseCarVo carVo = new PrpLLinkCaseCarVo();
				PrpLLinkCaseCarVo carVo = Beans.copyDepth().from(carPo.get(i))
						.to(PrpLLinkCaseCarVo.class);
				carList.add(carVo);
			}
			return carList;

		}
		return null;
	}

	@Override
	public List<PrpLLinkCaseImgVo> findImgList(String caseId) throws Exception {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("caseId", caseId);
		queryRule.addAscOrder("tags");
		List<PrpLLinkCaseImg> imgPo = databaseDao.findAll(
				PrpLLinkCaseImg.class, queryRule);
		if (imgPo != null && imgPo.size() > 0) {
			List<PrpLLinkCaseImgVo> imgList = new ArrayList<PrpLLinkCaseImgVo>();
			for (int i = 0; i < imgPo.size(); i++) {
				// PrpLLinkCaseCarVo carVo = new PrpLLinkCaseCarVo();
				PrpLLinkCaseImgVo carVo = Beans.copyDepth().from(imgPo.get(i))
						.to(PrpLLinkCaseImgVo.class);
				imgList.add(carVo);
			}
			return imgList;

		}
		return null;
	}

	@Override
	public List<PrpLLinkCaseInjuredVo> findInjuredList(String caseId)
			throws Exception {
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("caseId", caseId);

		List<PrpLLinkCaseInjured> injuredPo = databaseDao.findAll(
				PrpLLinkCaseInjured.class, queryRule);
		if (injuredPo != null && injuredPo.size() > 0) {
			List<PrpLLinkCaseInjuredVo> injuredList = new ArrayList<PrpLLinkCaseInjuredVo>();
			for (int i = 0; i < injuredPo.size(); i++) {
				// PrpLLinkCaseCarVo carVo = new PrpLLinkCaseCarVo();
				PrpLLinkCaseInjuredVo carVo = Beans.copyDepth()
						.from(injuredPo.get(i)).to(PrpLLinkCaseInjuredVo.class);
				injuredList.add(carVo);
			}
			return injuredList;

		}
		return null;
	}

	@Override
	public List<PrpLLinkCaseVo> findLinkCase(String versionNumber) {
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("versionNumber", versionNumber);
		List<PrpLLinkCase> linkcasePos = databaseDao.findAll(
				PrpLLinkCase.class, rule);
		if (linkcasePos != null && linkcasePos.size() != 0) {
			List<PrpLLinkCaseVo> linkcaseList = new ArrayList<PrpLLinkCaseVo>();
			for (PrpLLinkCase linkcasePo : linkcasePos) {
				PrpLLinkCaseVo linkCaseVo = new PrpLLinkCaseVo();
				linkCaseVo = Beans.copyDepth().from(linkcasePo)
						.to(PrpLLinkCaseVo.class);
				linkcaseList.add(linkCaseVo);
			}
			return linkcaseList;
		}
		return null;
	}

	@Override
	public List<PrpLLinkCaseMainVo> findLinkCarMain(CaseDetailResultVo caseDetailVo){
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("caseId", caseDetailVo.getInfo().getData().getCaseId());
		List<PrpLLinkCaseMain> mainPos = databaseDao.findAll(
				PrpLLinkCaseMain.class, rule);
		if (mainPos != null && mainPos.size() != 0) {
			List<PrpLLinkCaseMainVo> linkcaseList = new ArrayList<PrpLLinkCaseMainVo>();
			for (PrpLLinkCaseMain mainPo : mainPos) {
				PrpLLinkCaseMainVo mainVo = new PrpLLinkCaseMainVo();
				mainVo = Beans.copyDepth().from(mainPo)
						.to(PrpLLinkCaseMainVo.class);
				linkcaseList.add(mainVo);
			}
			return linkcaseList;
		}
		return null;
	}

	@Override
	public List<PrpLLinkCaseInfoVo> findLinkCaseInfo(String caseNumber){
		QueryRule rule = QueryRule.getInstance();
		rule.addEqual("caseNumber", caseNumber);
		List<PrpLLinkCaseInfo> caseInfoPos = databaseDao.findAll(
				PrpLLinkCaseInfo.class, rule);
		if (caseInfoPos != null && caseInfoPos.size() != 0) {
			List<PrpLLinkCaseInfoVo> caseInfoList = new ArrayList<PrpLLinkCaseInfoVo>();
			for (PrpLLinkCaseInfo caseInfoPo : caseInfoPos) {
				PrpLLinkCaseInfoVo mainVo = new PrpLLinkCaseInfoVo();
				mainVo = Beans.copyDepth().from(caseInfoPo)
						.to(PrpLLinkCaseInfoVo.class);
				caseInfoList.add(mainVo);
			}
			return caseInfoList;
		}
		return null;
	}

	@Override
	public void testQuartz() {
		System.out.println("啊啊！！好饿啊！我被调用了---------------------------------");

	}

	public String downloadToLocal(String picUrl,String imgId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String file1 = sdf.format( new Date());
		String fileDir=SpringProperties.getProperty("policyLink_fileDir");
		File dir = new File(fileDir+file1);
		if(!dir.exists()){
			dir.mkdirs();  // 创建目录
		}
		String fileName = imgId +".jpg";
		File file = new File(dir,fileName);
		if(!file.exists()){
			try {
				file.createNewFile();  //创建文件
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			URL url = new URL(picUrl);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			byte[] data = new byte[1024];
			int len = 0;
			while ((len = is.read(data)) != -1) {
				out.write(data, 0, len);// 将数据通过输出流输出到本地．
				/*
				 * //如果是用字节数组的话 bos.write(data,0,len);
				 */
				// 要输出到的本地文件路径
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file.getPath();

	}
	@Override
	public void updateSmallPic(){
		// this.getServletContext().getRealPath("/WEB-INF/upload")
		String smallPic = "";
		List<PrpLLinkCaseImg> imgPoList = databaseDao.findAll(PrpLLinkCaseImg.class);
		for(PrpLLinkCaseImg imgPo : imgPoList){
			smallPic = this.downloadToLocal(imgPo.getPicUrl(),imgPo.getImgId());
			System.out.println();
			imgPo.setSmallPicUrl(smallPic);
			databaseDao.update(PrpLLinkCaseImg.class, imgPo);
		}
	}
	
}
