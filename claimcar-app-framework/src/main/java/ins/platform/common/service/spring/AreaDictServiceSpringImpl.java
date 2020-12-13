package ins.platform.common.service.spring;

import com.google.gson.Gson;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.schema.PrpDHospital;
import ins.platform.schema.PrpdAppraisa;
import ins.platform.schema.PrpdoldprovincecityArea;
import ins.platform.schema.SysAreaDict;
import ins.platform.vo.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;

@Service(value = "areaDictService")
public class AreaDictServiceSpringImpl implements AreaDictService {

	private static Logger logger = LoggerFactory.getLogger(AreaDictServiceSpringImpl.class);

	@Autowired
	private DatabaseDao databaseDao;


	@Override
	public List<SysAreaDictVo> findChildArea(String upperCode,String handlerStatus) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("upperCode",upperCode);
		if("0".equals(handlerStatus) || "1".equals(handlerStatus) || "2".equals(handlerStatus)){
		query.addEqual("isValid","Y");//案件工作流
		}
		List<SysAreaDict> areaPoList = databaseDao.findAll(SysAreaDict.class,query);
		List<SysAreaDictVo> areaVoList = Beans.copyDepth().from(areaPoList).toList(SysAreaDictVo.class);
		return areaVoList;
	}
    
	@Override
	public List<PrpdoldprovincecityAreaVo> findChildOldArea(String upperCode) {
		QueryRule query = QueryRule.getInstance();
		
		if("000000".equals(upperCode)){
			query.addSql(" parent is null ");
			query.addEqual("validStatus","1");
		}else{
		    query.addEqual("parent",upperCode);
			query.addEqual("validStatus","1");
			
		}
		
		List<PrpdoldprovincecityArea> areaPoList = databaseDao.findAll(PrpdoldprovincecityArea.class,query);
		List<PrpdoldprovincecityAreaVo> areaVoList = Beans.copyDepth().from(areaPoList).toList(PrpdoldprovincecityAreaVo.class);
		return areaVoList;
	}
	
	@Override
	public String[] findAreaByAreaCode(String code) {
		return findAreaByCode("areaCode",code);
	}
	
	@Override
	public String[] findAreaByAreaCode(String code, String handlerStatus) {
		return findAreaByCode("areaCode",code,handlerStatus);
	}

	@Override
	public String[] findAreaNameByAreaCode(String code,String handlerStatus) {
		return findAreaNameByCode("areaCode",code,handlerStatus);
	}
	
	@Override
	public String[] findAreaByOldAreaCode(String code) {
		return findOldAreaByCode("code",code);
	}
	
	@Override
	public String[] findAreaByComCode(String code,String handlerStatus) {
		return findAreaByCode("comCode",code,handlerStatus);
	}

	@Override
	public String[] findAreaByPostCode(String code,String handlerStatus) {
		return findAreaByCode("postCode",code,handlerStatus);
	}

	private String[] findAreaByCode(String propertyName,String code) {
		List<SysAreaDict> areaList = new ArrayList<SysAreaDict>();
		findUpperAreaList(areaList,propertyName,code);

		// areaList 中当前保存的顺序是 区，市 ，省份 ，需要倒叙为 省，市 区
		int maxIdx = areaList.size()-1;
		String[] areas = new String[areaList.size()];
		for(SysAreaDict dictPo:areaList){
			areas[maxIdx] = dictPo.getAreaCode();
			maxIdx-- ;
		}
		return areas;
	}
	
	private String[] findAreaByCode(String propertyName,String code,String handlerStatus) {
		List<SysAreaDict> areaList = new ArrayList<SysAreaDict>();
		findUpperAreaList(areaList,propertyName,code,handlerStatus);

		// areaList 中当前保存的顺序是 区，市 ，省份 ，需要倒叙为 省，市 区
		int maxIdx = areaList.size()-1;
		String[] areas = new String[areaList.size()];
		for(SysAreaDict dictPo:areaList){
			areas[maxIdx] = dictPo.getAreaCode();
			maxIdx-- ;
		}
		return areas;
	}

	private String[] findAreaNameByCode(String propertyName,String code,String handlerStatus) {
		List<SysAreaDict> areaList = new ArrayList<SysAreaDict>();
		findUpperAreaList(areaList,propertyName,code,handlerStatus);

		// areaList 中当前保存的顺序是 区，市 ，省份 ，需要倒叙为 省，市 区
		int maxIdx = areaList.size()-1;
		String[] areas = new String[areaList.size()];
		for(SysAreaDict dictPo:areaList){
			areas[maxIdx] = dictPo.getAreaName();
			maxIdx-- ;
		}
		return areas;
	}
    
	private String[] findOldAreaByCode(String propertyName,String code) {
		List<PrpdoldprovincecityArea> areaList = new ArrayList<PrpdoldprovincecityArea>();
		findUpperOldAreaList(areaList,propertyName,code);

		// areaList 中当前保存的顺序是 区，市 ，省份 ，需要倒叙为 省，市 区
		int maxIdx = areaList.size()-1;
		String[] areas = new String[areaList.size()];
		for(PrpdoldprovincecityArea dictPo:areaList){
			areas[maxIdx] = dictPo.getCode();
			maxIdx-- ;
		}
		return areas;
	}
	
	private void findUpperAreaList(List<SysAreaDict> areaList,String propertyName,String code) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual(propertyName,code);
		SysAreaDict areaPo = databaseDao.findUnique(SysAreaDict.class,query);
		if(areaPo!=null){
			areaList.add(areaPo);

			if(areaPo.getAreaLevel().doubleValue()>1){// 不是最顶层，/还有上级地区，继续递归查询上级
				findUpperAreaList(areaList,propertyName,areaPo.getUpperCode());
			}
		}
	}
	
	private void findUpperAreaList(List<SysAreaDict> areaList,String propertyName,String code,String handlerStatus) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual(propertyName,code);
		if("0".equals(handlerStatus) || "1".equals(handlerStatus) || "2".equals(handlerStatus)){
		query.addEqual("isValid","Y");//案件工作流
		}
		SysAreaDict areaPo = databaseDao.findUnique(SysAreaDict.class,query);
		if(areaPo!=null){
			areaList.add(areaPo);

			if(areaPo.getAreaLevel().doubleValue()>1){// 不是最顶层，/还有上级地区，继续递归查询上级
				findUpperAreaList(areaList,propertyName,areaPo.getUpperCode(),handlerStatus);
			}
		}
	}
	
	private void findUpperOldAreaList(List<PrpdoldprovincecityArea> areaList,String propertyName,String code) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual(propertyName,code);
		PrpdoldprovincecityArea areaPo = databaseDao.findUnique(PrpdoldprovincecityArea.class,query);
		if(areaPo!=null){
			areaList.add(areaPo);

			if(StringUtils.isNotBlank(areaPo.getParent())){// 不是最顶层，/还有上级地区，继续递归查询上级
				findUpperOldAreaList(areaList,propertyName,areaPo.getParent());
			}
		}
	}
	
	public List<PrpDHospitalVo> findChildHospital(String areaCode) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("areaCode",areaCode);
		List<PrpDHospital> hospitalPoList = databaseDao.findAll(PrpDHospital.class,query);
		List<PrpDHospitalVo> areaVoList = Beans.copyDepth().from(hospitalPoList).toList(PrpDHospitalVo.class);
		return areaVoList;
	}
	
	//伤残鉴定机构
	public List<PrpdAppraisaVo> findChildAppraisa(String areaCode) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("areaCode",areaCode);
		query.addEqual("validStatus","1");
		List<PrpdAppraisa> appraisaPoList = databaseDao.findAll(PrpdAppraisa.class,query);
		List<PrpdAppraisaVo> areaVoList = Beans.copyDepth().from(appraisaPoList).toList(PrpdAppraisaVo.class);
		return areaVoList;
	}
	public String findAreaList(String code,String AreaCode) {
		QueryRule query = QueryRule.getInstance();
		QueryRule query1 = QueryRule.getInstance();
		query.addEqual(code,AreaCode);
		SysAreaDict areaPo = databaseDao.findUnique(SysAreaDict.class,query);
		if(areaPo!=null){
			if(areaPo.getComCode()!=null && areaPo.getComCode()!=""){
				return areaPo.getComCode();
			}else{
				query1.addEqual(code,areaPo.getUpperCode());
				SysAreaDict areaPo1 = databaseDao.findUnique(SysAreaDict.class,query1);
				return areaPo1.getComCode();
			}
		} 
		return null;
	}

	@Override
	public SysAreaDict findAreaByComCode(String code, BigDecimal level) {
		return findAreaByCode("comCode",code,level);
	}

	
	private SysAreaDict findAreaByCode(String propertyName,String code,BigDecimal level) {
		SysAreaDict areaPo = findAreaDict(propertyName,code,level);
		return areaPo;
	}

	private SysAreaDict findAreaDict(String propertyName,String code,BigDecimal level) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual(propertyName,code);
		query.addEqual("areaLevel",level);
		SysAreaDict areaPo = databaseDao.findUnique(SysAreaDict.class,query);
		if(areaPo==null){
			return null;
		}
		return areaPo;
	}

	@Override
	public List<SysAreaDictVo> findAreaCode(String areaCode) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("areaCode",areaCode);
		List<SysAreaDict> areaPoList = databaseDao.findAll(SysAreaDict.class,query);
		List<SysAreaDictVo> areaVoList = Beans.copyDepth().from(areaPoList).toList(SysAreaDictVo.class);
		return areaVoList;
	}

	@Override
	public ProvinceResponseDto findProvinceInfoFromBasicPlatform(String areaCode) {
		ProvinceResponseDto responseDto = new ProvinceResponseDto();
		ProvinceRequestDto requestDto = new ProvinceRequestDto();
		String responseString = "";
		Gson gson = new Gson();
		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("基础平台地址为空，无法获取数据！");
			return responseDto;
		}
		// 查询所有省级信息
		platformUrl = platformUrl + "/area/queryProvince";
		try {
			String requestString = gson.toJson(requestDto);
			responseString = getBasicAreaData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "基础平台省数据获取为空！");
				return responseDto;
			}
			logger.info("基础平台返回数据为：" + responseString);
		} catch (Exception e) {
			logger.info("理赔获取基础平台省数据异常！", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, ProvinceResponseDto.class);
		} catch (Exception e) {
			logger.info("理赔解析基础平台省数据异常！基础平台省数据为：" + responseString, e);
			return responseDto;
		}

		return responseDto;
	}

	/**
	 * 查询省详细信息
	 * @param provinceCode 省代码
	 * @return 省详细信息对象
	 */
	@Override
	public ProvinceResponseDto findProvinceDetailInfoFromBasicPlatform(String provinceCode) {
		ProvinceResponseDto responseDto = new ProvinceResponseDto();
		ProvinceRequestDto requestDto = new ProvinceRequestDto();
		String responseString = "";
		Gson gson = new Gson();
		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("基础平台地址为空，无法获取数据！");
			return responseDto;
		}
		platformUrl = platformUrl + "/area/queryProvince";
		// 查询市级信息
		requestDto.setProvinceCode(provinceCode);
		logger.info("市详细信息查询，查询地址:" + platformUrl + "查询条件：省代码: {}", provinceCode);
		try {
			String requestString = gson.toJson(requestDto);
			responseString = getBasicAreaData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("基础平台请求报文: " + requestString + "基础平台省详细数据获取为空！");
				return responseDto;
			}
			logger.info("基础平台返回省详细数据为：" + responseString);
		} catch (Exception e) {
			logger.info("理赔获取基础平台省详细数据异常！", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, ProvinceResponseDto.class);
		} catch (Exception e) {
			logger.info("理赔解析基础平台省数据异常！基础平台省数据为：" + responseString, e);
			return responseDto;
		}

		return responseDto;
	}

	/**
	 * 查询市详细信息
	 * @param provinceCode 省代码
	 * @param cityCode 市代码
	 * @return 市详细信息对象
	 */
	@Override
	public CityResponseDto findCityDetailInfoFromBasicPlatform(String provinceCode, String cityCode) {
		CityResponseDto responseDto = new CityResponseDto();
		CityRequestDto requestDto = new CityRequestDto();
		String responseString = "";
		Gson gson = new Gson();
		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("基础平台地址为空，无法获取市详细数据！");
			return responseDto;
		}
		platformUrl = platformUrl + "/area/queryCity";
		// 查询市级信息
		requestDto.setProvinceCode(provinceCode);
		requestDto.setCityCode(cityCode);
		logger.info("市详细信息查询，查询地址:" + platformUrl + " 查询条件：省代码: {} 市代码: {}", provinceCode, cityCode);
		try {
			String requestString = gson.toJson(requestDto);
			responseString = getBasicAreaData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "基础平台市详细数据获取为空！");
				return responseDto;
			}
			logger.info("基础平台返回市详细数据为：" + responseString);
		} catch (Exception e) {
			logger.info("理赔获取基础平台市详细数据异常！", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, CityResponseDto.class);
		} catch (Exception e) {
			logger.info("理赔解析基础平台市详细数据异常！基础平台市详细数据为：" + responseString, e);
			return responseDto;
		}

		return responseDto;
	}

	@Override
	public CityResponseDto findCityInfoFromBasicPlatform(String areaCode) {
		CityResponseDto responseDto = new CityResponseDto();
		CityRequestDto requestDto = new CityRequestDto();
		String responseString = "";
		Gson gson = new Gson();
		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			logger.info("基础平台地址为空，无法获取市数据！");
			return responseDto;
		}
		platformUrl = platformUrl + "/area/queryCity";
		if (!StringUtils.isBlank(areaCode) && !"000000".equals(areaCode)) {
			// 查询市级信息
			requestDto.setProvinceCode(areaCode);
		}
		try {
			String requestString = gson.toJson(requestDto);
			responseString = getBasicAreaData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				logger.info("requestString: " + requestString + "基础平台市数据获取为空！");
				return responseDto;
			}
			logger.info("基础平台返回数据为：" + responseString);
		} catch (Exception e) {
			logger.info("理赔获取基础平台市数据异常！", e);
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, CityResponseDto.class);
		} catch (Exception e) {
			logger.info("理赔解析基础平台市数据异常！基础平台市数据为：" + responseString, e);
			return responseDto;
		}

		return responseDto;
	}

	/**
	 * 从基础平台获取数据
	 * @param platformUrl 基础平台地址
	 * @return 返回基础平台数据
	 * @throws Exception 数据获取异常
	 */
	private String getBasicAreaData(String requestParams, String platformUrl) throws Exception {
		BufferedReader bfreader = null;
		HttpURLConnection httpURLConnection = null;
		OutputStream outputStream = null;
		DataOutputStream out = null;
		StringBuilder buffer = new StringBuilder();

		try {
			URL url = new URL(platformUrl);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			// post方式不能使用缓存
			httpURLConnection.setUseCaches(false);
			// 配置本次连接的Content-Type，配置为text/xml
			httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			// 维持长连接
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setConnectTimeout(20 * 1000);
			httpURLConnection.setReadTimeout(20 * 1000);
			httpURLConnection.setAllowUserInteraction(true);
			httpURLConnection.connect();
		} catch (Exception ex) {
			throw new Exception("请求基础平台服务失败，请稍候再试！", ex);
		}
		try {
			outputStream = httpURLConnection.getOutputStream();
			out = new DataOutputStream(outputStream);
			out.write(requestParams.getBytes("utf-8"));
			out.flush();
			bfreader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
			String strLine = "";
			while ((strLine = bfreader.readLine()) != null) {
				buffer.append(strLine);
			}
		} catch (Exception e) {
			throw new Exception("读取基础平台返回数据失败", e);
		} finally {
			try {
				if (bfreader != null) {
					bfreader.close();
				}

				if (out != null) {
					out.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}
    
	@Override
	public List<SysAreaDictVo> findAreaCodeByFullName(String fullName) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("fullName",fullName);
		List<SysAreaDict> areaPoList = databaseDao.findAll(SysAreaDict.class,query);
		List<SysAreaDictVo> areaVoList = Beans.copyDepth().from(areaPoList).toList(SysAreaDictVo.class);
		return areaVoList;
	} 

	/**
	 * 根据地区名称与上级编码查询地区信息
	 *
	 * @param name 地区名称
	 * @param upperCode 所属上级地区编码
	 * @return 返回当前地区完整信息
	 */
	@Override
	public SysAreaDictVo findAreaCodeByNameAndUppercode(String name, String upperCode) {
		QueryRule query = QueryRule.getInstance();
		query.addEqual("areaName", name);
		query.addEqual("upperCode", upperCode);
		query.addEqual("isValid", "Y");
		List<SysAreaDict> areaPoList = databaseDao.findAll(SysAreaDict.class, query);
		SysAreaDictVo areaDictVo = null;
		if (areaPoList != null && areaPoList.size() > 0) {
			areaDictVo = Beans.copyDepth().from(areaPoList.get(0)).to(SysAreaDictVo.class);
		}

		return areaDictVo;
	}


}
