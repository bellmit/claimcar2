package ins.platform.common.service.spring;

import com.google.gson.Gson;
import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.BaseDaoService;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.schema.SysCodeDict;
import ins.platform.schema.SysTransConfig;
import ins.platform.utils.SqlJoinUtils;
import ins.platform.vo.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;
@Service(value = "codeTranService")

public class CodeTranServiceSpringImpl implements CodeTranService {

	/**
	 * code-name 的缓存
	 */
	private static CacheService codeNameCache = CacheManager.getInstance("T_CodeNameTrans_Map");
	/**
	 * code-SysCodeDict 的缓存
	 */
	private static CacheService codeDictCache = CacheManager.getInstance("T_CodeDictTrans_Map");

	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	BaseDaoService baseDaoService;
	
	/**
	 * 代码翻译
	 * 
	 * @param codeType
	 * @param code
	 * @return
	 * @modified: ☆hezheng(May 9, 2014 4:35:25 PM): <br>
	 */
	public String transCode(String codeType, String code) {
		String codeName = code;
		Map<String, String> codeMap = findCodeNameMap(codeType);
		if (codeMap == null)
			return codeName;

		codeName = codeMap.get(code);
		if (codeName == null || "".equals(codeName)){
			SysCodeDictVo dictVo = this.findCodeDictVo(codeType, code);
			if(dictVo!=null){
				codeName = dictVo.getCodeName();
				codeMap.put(code, codeName);
			}else{
				codeName = code;
			}
		}
			
		return codeName;
	}
	
	/**
	 * 获取类型的所有翻译
	 * 
	 * @param transType
	 * @return
	 * @modified: ☆hezheng(May 9, 2014 4:40:45 PM): <br>
	 */
	public Map<String, String> findCodeNameMap(String transType) {
		Map<String, String> codeNameMap = null;
		// 缓存处理
		codeNameMap = (Map<String, String>) codeNameCache.getCache(transType);
		if(codeNameMap!=null&&codeNameMap.size()>0) return codeNameMap;

		codeNameMap = new LinkedHashMap<String,String>();
			// 优先查询字典表
		List<SysCodeDict> dictList = null;
		String queryString = " from SysCodeDict where 1=1 ";
		List<String> paramValues = new ArrayList<String>();

		if(transType.indexOf(",")>0){
			String[] str = transType.split(",");
			String maskStr = SqlJoinUtils.arrayToMask(str);
			queryString += " AND codeType in ("+maskStr+")";
			paramValues.addAll(SqlJoinUtils.arrayToList(str));
		}else{
			queryString += " AND  codeType = ? ";
			paramValues.add(transType);
		}
		queryString += " order by serialNo";
		dictList = databaseDao.findAllByHql(SysCodeDict.class,queryString,paramValues.toArray());

		for(SysCodeDict po:dictList){
			SysCodeDictVo vo = Beans.copyDepth().from(po).to(SysCodeDictVo.class);
			codeNameMap.put(vo.getCodeCode(),vo.getCodeName());
		}
			
		if(codeNameMap.size()>0){
			codeNameCache.putCache(transType,codeNameMap);
			return codeNameMap;
		}

		// 最后去找翻译配置中的代码
		SysTransConfig transCfg = databaseDao.findByPK(SysTransConfig.class,transType);
		if(transCfg==null) return null;

		String selectCode = transCfg.getSelectCode();
		String selectName = transCfg.getSelectName();
		StringBuffer querySb = new StringBuffer();
		querySb.append(" SELECT ").append(selectCode).append(",").append(selectName);
		querySb.append(" FROM ").append(transCfg.getSelectTable());
		if(StringUtils.isNotBlank(transCfg.getSelectWhere())){
			querySb.append(" WHERE ").append(transCfg.getSelectWhere());
		}
		if(StringUtils.isNotBlank(transCfg.getOrderBy())){
			querySb.append(" ORDER BY ").append(transCfg.getOrderBy());
		}

		paramValues = new ArrayList<String>();

		String paramStr = transCfg.getPararmValue();
		if(StringUtils.isNotBlank(paramStr)){
			if(paramStr.contains(",")){
				String[] str = paramStr.split(",");
				paramValues.addAll(SqlJoinUtils.arrayToList(str));
			}else{
				paramValues.add(paramStr);
			}
		}
		List<Object[]> objList = databaseDao.findRangeBySql(querySb.toString(),0,1000,paramValues.toArray());

		for(Object[] result:objList){
			codeNameMap.put((String)result[0],(String)result[1]);
		}

		codeNameCache.putCache(transType,codeNameMap);


		return codeNameMap;
	}

	/**
	 * 获取类型的所有翻译
	 * 
	 * @param transType
	 * @return
	 * @modified: ☆hezheng(May 9, 2014 4:40:45 PM): <br>
	 */
	public Map<String,SysCodeDictVo> findCodeDictTransMap(String transType,String upperCode) {
		String key = transType+"_"+upperCode;
		// 缓存处理
		Map<String,SysCodeDictVo> codeDictMap = (Map<String,SysCodeDictVo>)codeDictCache.getCache(key);
		if(codeDictMap!=null&&codeDictMap.size()>0) return codeDictMap;
		codeDictMap = new LinkedHashMap<String,SysCodeDictVo>();

		List<SysCodeDict> dictList = null;
		String queryString = " from SysCodeDict where ";
		List<String> paramValues = new ArrayList<String>();
		if(StringUtils.isNotBlank(upperCode)){
			queryString += " upperCode = ? ";
			paramValues.add(upperCode);
		}else{
			queryString += " 1=1 ";
		}

		if(transType.indexOf(",")>0){
			String[] str = transType.split(",");
			String maskStr = SqlJoinUtils.arrayToMask(str);
			queryString += " AND codeType in ("+maskStr+")";
			paramValues.addAll(SqlJoinUtils.arrayToList(str));
		}else{
			queryString += " AND  codeType = ? ";
			paramValues.add(transType);
		}
		queryString += " AND ISVALID = ? ";
		paramValues.add("1");
		queryString += " order by serialNo";
		dictList = databaseDao.findAllByHql(SysCodeDict.class,queryString,paramValues.toArray());

		for(SysCodeDict po:dictList){
			SysCodeDictVo vo = Beans.copyDepth().from(po).to(SysCodeDictVo.class);
			codeDictMap.put(vo.getCodeCode(),vo);
		}

		if(codeDictMap.size()>0){
			codeDictCache.putCache(key,codeDictMap);
			return codeDictMap;
		}

		// 最后去找翻译配置中的代码
		SysTransConfig transCfg = databaseDao.findByPK(SysTransConfig.class,transType);
		if(transCfg==null) return null;

		String selectCode = transCfg.getSelectCode();
		String selectName = transCfg.getSelectName();
		StringBuffer querySb = new StringBuffer();
		querySb.append(" SELECT ").append(selectCode).append(",").append(selectName);
		querySb.append(" FROM ").append(transCfg.getSelectTable());
		if(StringUtils.isNotBlank(transCfg.getSelectWhere())){
		querySb.append(" WHERE ").append(transCfg.getSelectWhere());
		}
		if(StringUtils.isNotBlank(transCfg.getOrderBy())){
		querySb.append(" ORDER BY ").append(transCfg.getOrderBy());
		}

		paramValues = new ArrayList<String>();

		String paramStr = transCfg.getPararmValue();
		if(StringUtils.isBlank(paramStr) && StringUtils.isNotBlank(upperCode)){
			paramStr = upperCode;
		}
		
		if(StringUtils.isNotBlank(paramStr)){
			if(paramStr.contains(",")){
				String[] str = paramStr.split(",");
				paramValues.addAll(SqlJoinUtils.arrayToList(str));
			}else{
				paramValues.add(paramStr);
			}
		}
		List<Object[]> objList = databaseDao.findRangeBySql(querySb.toString(),0,1000,paramValues.toArray());

		for(Object[] result:objList){
			SysCodeDictVo codeDictVo = new SysCodeDictVo();
			codeDictVo.setCodeCode((String)result[0]);
			codeDictVo.setCodeName((String)result[1]);
			codeDictMap.put((String)result[0],codeDictVo);
		}

		codeDictCache.putCache(key,codeDictMap);

		return codeDictMap;
	}
	
	/**
	 * 根据code类型和code代码查找SysCodeDictVo
	 * @param codeType
	 * @param code
	 * @return
	 */
	public SysCodeDictVo findTransCodeDictVo(String codeType, String code) {
		SysCodeDictVo sysCodeDictVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("codeType", codeType);
		queryRule.addEqual("codeCode", code);
		
		SysCodeDict sysCodeDict = databaseDao.findUnique(SysCodeDict.class, queryRule);
		if(sysCodeDict != null){
			sysCodeDictVo = new SysCodeDictVo();
			Beans.copy().from(sysCodeDict).to(sysCodeDictVo);
		}
		return sysCodeDictVo;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String findCodeName(String transType,String code) {
		Map<String, String> codeNameMap = null;
		// 缓存处理
		codeNameMap = (Map<String, String>) codeNameCache.getCache(transType);
		if(codeNameMap !=null && StringUtils.isNotBlank(codeNameMap.get(code))) {
			return codeNameMap.get(code);
		}else{
			//codeNameMap = new HashMap<String, String>();
			codeNameMap = new LinkedHashMap<String,String>();
			// 优先查询字典表
			String queryString = " from SysCodeDict where 1=1 ";
			List<String> paramValues = new ArrayList<String>();

			if(transType.indexOf(",")>0){
				String[] str = transType.split(",");
				String maskStr = SqlJoinUtils.arrayToMask(str);
				queryString += " AND codeType in ("+maskStr+")";
				paramValues.addAll(SqlJoinUtils.arrayToList(str));
			}else{
				queryString += " AND  codeType = ? ";
				paramValues.add(transType);
			}
			queryString += " AND  codeCode = ? ";
			paramValues.add(code);
			SysCodeDict sysCodeDict = databaseDao.findUniqueByHql(SysCodeDict.class, queryString, paramValues.toArray());
			
			if(sysCodeDict != null){
				SysCodeDictVo vo = Beans.copyDepth().from(sysCodeDict).to(SysCodeDictVo.class);
				codeNameMap.put(vo.getCodeCode(),vo.getCodeName());
				codeNameCache.putCache(transType,codeNameMap);
				return codeNameMap.get(code);
			}

			// 最后去找翻译配置中的代码
			SysTransConfig transCfg = databaseDao.findByPK(SysTransConfig.class,transType);
			if(transCfg==null) return null;

			String selectCode = transCfg.getSelectCode();
			String selectName = transCfg.getSelectName();
			StringBuffer querySb = new StringBuffer();
			paramValues = new ArrayList<String>();
			querySb.append(" SELECT ").append(selectCode).append(",").append(selectName);
			querySb.append(" FROM ").append(transCfg.getSelectTable());
			querySb.append(" WHERE ").append(selectCode + "=?");//所查代码
			paramValues.add(code);
			if(StringUtils.isNotBlank(transCfg.getSelectWhere())){
				querySb.append(" AND ").append(transCfg.getSelectWhere());
			}
			if(StringUtils.isNotBlank(transCfg.getOrderBy())){
				querySb.append(" ORDER BY ").append(transCfg.getOrderBy());
			}

			String paramStr = transCfg.getPararmValue();
			if(StringUtils.isNotBlank(paramStr)){
				if(paramStr.contains(",")){
					String[] str = paramStr.split(",");
					paramValues.addAll(SqlJoinUtils.arrayToList(str));
				}else{
					paramValues.add(paramStr);
				}
			}
			Object[] obj = databaseDao.findUniqueBySql(querySb.toString(),paramValues.toArray());

			if(obj == null){
				return null;
			}
		    codeNameMap.put((String)obj[0],(String)obj[1]);
			codeNameCache.putCache(transType,codeNameMap);

			return codeNameMap.get(code);
		}
	}
	
	public SysCodeDictVo findCodeDictVo (String transType,String code) {

		// 优先查询字典表
		String queryString = " from SysCodeDict where 1=1 ";
		List<String> paramValues = new ArrayList<String>();

		if(transType.indexOf(",")>0){
			String[] str = transType.split(",");
			String maskStr = SqlJoinUtils.arrayToMask(str);
			queryString += " AND codeType in ("+maskStr+")";
			paramValues.addAll(SqlJoinUtils.arrayToList(str));
		}else{
			queryString += " AND  codeType = ? ";
			paramValues.add(transType);
		}
		queryString += " AND  codeCode = ? ";
		paramValues.add(code);
		SysCodeDict sysCodeDict = databaseDao.findUniqueByHql(SysCodeDict.class, queryString, paramValues.toArray());
		
		if(sysCodeDict != null){
			SysCodeDictVo vo = Beans.copyDepth().from(sysCodeDict).to(SysCodeDictVo.class);
			
			return vo;
		}

		// 最后去找翻译配置中的代码
		SysTransConfig transCfg = databaseDao.findByPK(SysTransConfig.class,transType);
		if(transCfg==null) return null;

		String selectCode = transCfg.getSelectCode();
		String selectName = transCfg.getSelectName();
		StringBuffer querySb = new StringBuffer();
		paramValues = new ArrayList<String>();
		querySb.append(" SELECT ").append(selectCode).append(",").append(selectName);
		querySb.append(" FROM ").append(transCfg.getSelectTable());
		querySb.append(" WHERE ").append(selectCode + "=?");//所查代码
		paramValues.add(code);
		if(StringUtils.isNotBlank(transCfg.getSelectWhere())){
			querySb.append(" AND ").append(transCfg.getSelectWhere());
		}
		if(StringUtils.isNotBlank(transCfg.getOrderBy())){
			querySb.append(" ORDER BY ").append(transCfg.getOrderBy());
		}

		String paramStr = transCfg.getPararmValue();
		if(StringUtils.isNotBlank(paramStr)){
			if(paramStr.contains(",")){
				String[] str = paramStr.split(",");
				paramValues.addAll(SqlJoinUtils.arrayToList(str));
			}else{
				paramValues.add(paramStr);
			}
		}
		Object[] obj = databaseDao.findUniqueBySql(querySb.toString(),paramValues.toArray());

		if(obj == null){
			return null;
		}
		
		SysCodeDictVo codeDictVo = new SysCodeDictVo();
		codeDictVo.setCodeCode((String)obj[0]);
		codeDictVo.setCodeName((String)obj[1]);

		return codeDictVo;
	}
	
	
	public SysCodeDictVo findCodeDictVoByParams (String transType,String code,String upperCode) {
		List<String> paramValues = new ArrayList<String>();
		// 最后去找翻译配置中的代码
		SysTransConfig transCfg = databaseDao.findByPK(SysTransConfig.class,transType);
		if(transCfg==null) return null;

		String selectCode = transCfg.getSelectCode();
		String selectName = transCfg.getSelectName();
		StringBuffer querySb = new StringBuffer();
		paramValues = new ArrayList<String>();
		querySb.append(" SELECT ").append(selectCode).append(",").append(selectName);
		querySb.append(" FROM ").append(transCfg.getSelectTable());
		querySb.append(" WHERE ").append(selectCode + "=?");//所查代码
		paramValues.add(code);
		if(StringUtils.isNotBlank(transCfg.getSelectWhere())){
			querySb.append(" AND ").append(transCfg.getSelectWhere());
		}
		if(StringUtils.isNotBlank(transCfg.getOrderBy())){
			querySb.append(" ORDER BY ").append(transCfg.getOrderBy());
		}

		String paramStr = transCfg.getPararmValue();
		if(StringUtils.isNotBlank(upperCode)){
			if(StringUtils.isBlank(paramStr)){//|| "null".equals(paramStr)
				paramStr = upperCode;
			}else{
				paramStr = paramStr+","+upperCode;
			}
		}
		
		if(StringUtils.isNotBlank(paramStr)){
			if(paramStr.contains(",")){
				String[] str = paramStr.split(",");
				paramValues.addAll(SqlJoinUtils.arrayToList(str));
			}else{
				paramValues.add(paramStr);
			}
		}
		Object[] obj = databaseDao.findUniqueBySql(querySb.toString(),paramValues.toArray());

		if(obj == null){
			return null;
		}
		
		SysCodeDictVo codeDictVo = new SysCodeDictVo();
		codeDictVo.setCodeCode((String)obj[0]);
		codeDictVo.setCodeName((String)obj[1]);

		return codeDictVo;
	}
     /**
      * 清除缓存
      */
	@Override
	public void clearCache(){
		codeNameCache.clearAllCacheManager();
		codeDictCache.clearAllCacheManager();
	}

	@Override
	public boolean validEffictiveValue(String value, String type) {
		// TODO Auto-generated method stub
		Map<String, String> codeValuesMap = this.findCodeNameMap(type);
		if (codeValuesMap != null && codeValuesMap.size()>0) {
			if(codeValuesMap.containsKey(value)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Map<String, SysCodeDictVo> findLevelOneDisasterInfo() {
		Map<String, SysCodeDictVo> codeDictMap = new HashMap<String, SysCodeDictVo>();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("select codecode, codecname from ywuser.PrpDcode where CodeType = ?");
		sqlUtil.addParamValue("CatastropheCode");

		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		List<Object[]> objects = databaseDao.findRangeBySql(sql, 0, 1000, values);
		for (Object[] object : objects) {
			SysCodeDictVo codeDictVo = new SysCodeDictVo();
			String codecode = (String) object[0];
			String codename = (String) object[1];

			codeDictVo.setCodeCode(codecode);
			codeDictVo.setCodeName(codename);
			codeDictMap.put(codecode, codeDictVo);
		}

		return codeDictMap;
	}

	@Override
	public Map<String, SysCodeDictVo> findLevelTwoDisasterInfo() {
		Map<String, SysCodeDictVo> codeDictMap = new HashMap<String, SysCodeDictVo>();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("select b.catastrophetype, b.name from ywuser.prpdcatastrophe b ");
		sqlUtil.append(" where b.catastrophetype in (select a.codecode from ywuser.prpdcode a where a.codetype = ?) ");
		sqlUtil.append(" order by b.catastrophetype asc ");
		sqlUtil.addParamValue("CatastropheCode");

		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		List<Object[]> objects = databaseDao.findRangeBySql(sql, 0, 1000, values);
		for (Object[] object : objects) {
			SysCodeDictVo codeDictVo = new SysCodeDictVo();
			String codecode = (String) object[0];
			String codename = (String) object[1];

			codeDictVo.setCodeCode(codecode);
			codeDictVo.setCodeName(codename);
			codeDictMap.put(codecode, codeDictVo);
		}

		return codeDictMap;
	}

	/**
	 * 获取所有费用类型
	 */
	@Override
	public Map<String, String> findFeeTypes() {
		Map<String, String> codeDictMap = new HashMap<String, String>();
		SqlJoinUtils sqlUtil = new SqlJoinUtils();
		sqlUtil.append("select flag, codecode from ywuser.PrpDcode  where CodeType = ? and Flag is not null");
		sqlUtil.addParamValue("NewChargeCode");

		// 查询参数
		String sql = sqlUtil.getSql();
		Object[] values = sqlUtil.getParamValues();

		List<Object[]> objects = databaseDao.findRangeBySql(sql, 0, 1000, values);
		for (Object[] object : objects) {
			String payreason = (String) object[0];
			String codecode = (String) object[1];
			codeDictMap.put(payreason, codecode);
		}

		return codeDictMap;
	}

	/**
	 * 获取所有银行大类信息
	 */
	@Override
	public Map<String, SysCodeDictVo> findAccBankCode() {
		Map<String, SysCodeDictVo> codeDictMap = null;
        // 缓存处理，由于是查询全量数据所以需要设置一个CACHE类型便于取值
        String transType = "AllBankCode";
        codeDictMap = (Map<String, SysCodeDictVo>) codeNameCache.getCache(transType);
        if(codeDictMap!=null&&codeDictMap.size()>0) {
            return codeDictMap;
        }
        //如果缓存中未取到对应的值，则需要调用接口进行查询
        codeDictMap = new HashMap<String, SysCodeDictVo>();
        //调用基础数据平台查询所有银行大类信息
        BankClassResponseDto dto = findBankKindsFromBasicPlatform();
        //对查出来的数据进行封装处理
		if(dto != null && dto.getData() != null && dto.getData().size()>0){
			for (BankClassDetailDto dateDetail : dto.getData()) {
				SysCodeDictVo codeDictVo = new SysCodeDictVo();
				String codecode = dateDetail.getBankCode();
				String codename = dateDetail.getBankName();
				codeDictVo.setCodeCode(codecode);
				codeDictVo.setCodeName(codename);
				codeDictMap.put(codecode, codeDictVo);
			}
		}
		//存入缓存，下次取值直接从缓存取
        codeNameCache.putCache(transType,codeDictMap);
		return codeDictMap;
	}
	/**
	 * 从基础平台获取开户行信息
	 * @date 2020年7月4日 14:47:13
	 * @return 基础平台数据转换为对象
	 */
	private BankClassResponseDto findBankKindsFromBasicPlatform() {
		BankClassResponseDto responseDto = new BankClassResponseDto();
		BankClassRequestDto requestDto = new BankClassRequestDto();
		String responseString = "";
		Gson gson = new Gson();
		String platformUrl = SpringProperties.getProperty("basicdataUrl");
		if (platformUrl == null) {
			return responseDto;
		}
		// 查询所有省级信息
		platformUrl = platformUrl + "/bank/kinds";
		try {
			String requestString = gson.toJson(requestDto);
			responseString = getBasicPlatformData(requestString, platformUrl);
			if (StringUtils.isBlank(responseString)) {
				return responseDto;
			}
		} catch (Exception e) {
			return responseDto;
		}

		try {
			responseDto = gson.fromJson(responseString, BankClassResponseDto.class);
		} catch (Exception e) {
			return responseDto;
		}
		return responseDto;
	}
	/**
	 * 获取基础平台数据
	 * @param requestParams 请求参数
	 * @param platformUrl 平台地址
	 * @return 基础平台数据
	 * @throws Exception 数据获取异常
	 */
	private String getBasicPlatformData(String requestParams, String platformUrl) throws Exception {
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
}
