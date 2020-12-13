package ins.platform.common.util;

import ins.framework.cache.CacheManager;
import ins.framework.cache.CacheService;
import ins.framework.lang.Springs;
import ins.platform.common.service.facade.AreaDictService;
import ins.platform.utils.ObjectUtils;
import ins.platform.vo.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年1月12日
 */
public class AreaSelectUtil {

	/** 上级代码对应下级的代码 */
	private static CacheService childArcaCodeMap = CacheManager.getInstance("AreaCode_Map");
	private AreaDictService areaSelectService = null;

	public AreaSelectUtil(){
		super();
		areaSelectService = (AreaDictService)Springs.getBean("areaDictService");
	}

	/**
	 * 查询一个地区的下级地区，支持从缓存获取数据
	 * @param upperCode
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	@SuppressWarnings("unchecked")
	public List<SysAreaDictVo> findChildArea(String upperCode,String handlerStatus) {
		if(StringUtils.isBlank(upperCode)){
			upperCode = "000000";// 最顶层地区代码是000000；
		}
		String cacheKey = "area_"+upperCode;
		List<SysAreaDictVo> areaDictList = (List<SysAreaDictVo>)childArcaCodeMap.getCache(cacheKey);
		//如果是处理页面，则去掉缓存中的无效地址
		List<SysAreaDictVo> areaList=new ArrayList<SysAreaDictVo>();
        if("0".equals(handlerStatus) ||  "1".equals(handlerStatus) || "2".equals(handlerStatus)){
        	if(areaDictList!=null && areaDictList.size()>0){
        		for(SysAreaDictVo vo:areaDictList){
        			if(!"N".equals(vo.getIsValid())){
        				areaList.add(vo);
        			}
        		}
        	}
        }
		if("0".equals(handlerStatus) ||  "1".equals(handlerStatus) || "2".equals(handlerStatus)){
			if(areaList==null||areaList.size()<1){
				areaList = areaSelectService.findChildArea(upperCode,handlerStatus);
				if( !ObjectUtils.isEmpty(areaList)){
					childArcaCodeMap.putCache(cacheKey,areaList);
				}
			}
			return areaList;
		}else{
			if(areaDictList==null||areaDictList.size()<1){
				areaDictList = areaSelectService.findChildArea(upperCode,handlerStatus);
				if( !ObjectUtils.isEmpty(areaDictList)){
					childArcaCodeMap.putCache(cacheKey,areaDictList);
				}
			}
			return areaDictList;
		}
		
	}
	
	/**
	 * 收款人账号维护页面的银行账号地区
	 * @param upperCode
	 * @return
	 *  @modified: ☆yzy(2017年5月18日 ): <br>
	 */
	@SuppressWarnings("unchecked")
	public List<PrpdoldprovincecityAreaVo> findChildOldArea(String upperCode) {
		if (StringUtils.isBlank(upperCode)) {
			// 最顶层地区代码是000000
			upperCode = "000000";
		}
		String cacheKey = "oldarea_" + upperCode;
		List<PrpdoldprovincecityAreaVo> areaDictList = (List<PrpdoldprovincecityAreaVo>) childArcaCodeMap.getCache(cacheKey);

		if (areaDictList == null || areaDictList.size() < 1) {
			// areaDictList = areaSelectService.findChildOldArea(upperCode);
			// 2151 对接新收付改成从基础平台获取开户银行省市信息
			if ("000000".equals(upperCode)) {
				// 获取省信息
				ProvinceResponseDto responseDto = areaSelectService.findProvinceInfoFromBasicPlatform(upperCode);
				areaDictList = transProvinceDataToLocalData(responseDto);
			} else {
				// 获取市信息
				CityResponseDto responseDto = areaSelectService.findCityInfoFromBasicPlatform(upperCode);
				areaDictList = transCityDataToLocalData(responseDto);
			}
			if (!ObjectUtils.isEmpty(areaDictList)) {
				childArcaCodeMap.putCache(cacheKey, areaDictList);
			}
		}
		return areaDictList;
	}
	
	/**
	 * 查询一个地区的医院
	 * @param areaCode
	 * @return
	 * @modified:
	 * ☆XMSH(2016年3月7日 下午12:11:11): <br>
	 */
	@SuppressWarnings("unchecked")
	public List<PrpDHospitalVo> findChildHospital(String areaCode) {
		if(StringUtils.isBlank(areaCode)){
			areaCode = "000000";// 最顶层地区代码是000000；
		}
		String cacheKey = "hospital_"+areaCode;
		List<PrpDHospitalVo> hospotalList = (List<PrpDHospitalVo>)childArcaCodeMap.getCache(cacheKey);

		if(hospotalList==null||hospotalList.size()<1){
			hospotalList = areaSelectService.findChildHospital(areaCode);
			if( !ObjectUtils.isEmpty(hospotalList)){
				childArcaCodeMap.putCache(cacheKey,hospotalList);
			}
		}
		return hospotalList;
	}
	
	/**
	 * 查询一个地区的伤残鉴定机构
	 * @param areaCode
	 * @return
	 * @modified:
	 * ☆yezy(2016年3月7日 下午12:11:11): <br>
	 */
	
	public List<PrpdAppraisaVo> findChildAppraisa(String areaCode) {
		if(StringUtils.isBlank(areaCode)){
			areaCode = "000000";// 最顶层地区代码是000000；
		}
		
		List<PrpdAppraisaVo> appraisaList = new ArrayList<PrpdAppraisaVo>();
          appraisaList= areaSelectService.findChildAppraisa(areaCode);
			
		return appraisaList;
	}
    
	/**
	 * 根据地区代码 得到对应的 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public String[] findAreaByAreaCode(String areaCode,String handlerStatus) {
		return areaSelectService.findAreaByAreaCode(areaCode,handlerStatus);
	}
	/**
	 * 根据地区代码 得到对应的旧理赔 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆yzy(2017年5月18日 ): <br>
	 */
	public String[] findAreaByOldAreaCode(String areaCode) {
		//return areaSelectService.findAreaByOldAreaCode(areaCode);
		String[] areas = new String[2];
		if (areaCode == null || areaCode.length() < 2) {
			areas[0] = "";
			areas[1] = "";
		} else {
			areas[0] = areaCode.substring(0, 2)+"00";
			areas[1] = areaCode;
		}

		return areas;
	}
	/**
	 * 根据 机构代码 得到对应的 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public String[] findAreaByComCode(String comCode,String handlerStatus) {
		return areaSelectService.findAreaByComCode(comCode,handlerStatus);
	}

	/**
	 * 根据 邮编 得到对应的 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public String[] findAreaByPostCode(String postCode,String handlerStatus) {
		return areaSelectService.findAreaByPostCode(postCode,handlerStatus);
	}
	public void clearCache(){
		childArcaCodeMap.clearAllCacheManager();
	}
	
	/**
	 * 得到某个地区的SysAreaDictVo
	 * @param areaCode
	 * @return
	 */
	public List<SysAreaDictVo> findAreaCode(String areaCode){
		return areaSelectService.findAreaCode(areaCode);
	}

	/**
	 * 将基础平台数据转换为本系统省数据
	 * @return
	 */
	public List<PrpdoldprovincecityAreaVo> transProvinceDataToLocalData(ProvinceResponseDto responseDto) {
		List<PrpdoldprovincecityAreaVo> oldAreaVoList = new ArrayList<PrpdoldprovincecityAreaVo>();

		if (responseDto != null && responseDto.getData() != null) {
			for (ProvinceDetailDto detailDto : responseDto.getData()) {
				PrpdoldprovincecityAreaVo oldAreaVo = new PrpdoldprovincecityAreaVo();
				oldAreaVo.setCode(detailDto.getProvinceCode());
				oldAreaVo.setName(detailDto.getProvinceName());
				oldAreaVoList.add(oldAreaVo);
			}
		}

		return oldAreaVoList;
	}

	/**
	 * 将基础平台数据转换为本系统市数据
	 * @return
	 */
	public List<PrpdoldprovincecityAreaVo> transCityDataToLocalData(CityResponseDto responseDto) {
		List<PrpdoldprovincecityAreaVo> oldAreaVoList = new ArrayList<PrpdoldprovincecityAreaVo>();

		if (responseDto != null && responseDto.getData() != null) {
			for (CityDetailDto detailDto : responseDto.getData()) {
				PrpdoldprovincecityAreaVo oldAreaVo = new PrpdoldprovincecityAreaVo();
				oldAreaVo.setCode(detailDto.getCityCode());
				oldAreaVo.setName(detailDto.getCityName());
				oldAreaVoList.add(oldAreaVo);
			}
		}

		return oldAreaVoList;
	}

}
