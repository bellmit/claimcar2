package ins.platform.common.service.facade;

import ins.platform.schema.SysAreaDict;
import ins.platform.vo.*;

import java.math.BigDecimal;
import java.util.List;


public interface AreaDictService {

	/**
	 * 得到某个地区的下级区域代码
	 * @param upperCode
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public List<SysAreaDictVo> findChildArea(String upperCode,String handlerStatus);
	
	public List<PrpdoldprovincecityAreaVo> findChildOldArea(String upperCode);

	/**
	 * 根据地区代码 得到对应的 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public String[] findAreaByAreaCode(String code);
	
	/**
	 * 重写
	 * @param code
	 * @param handlerStatus
	 * @return
	 */
	public String[] findAreaByAreaCode(String code,String handlerStatus);

	/** 获取省市区名称 */
	public String[] findAreaNameByAreaCode(String code,String handlerStatus);
	/**
	 * 根据地区代码 得到对应的旧理赔 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆yzy(2017年5月18日 ): <br>
	 */
	public String[] findAreaByOldAreaCode(String code);

	/**
	 * 根据 机构代码 得到对应的 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public String[] findAreaByComCode(String code,String handlerStatus);

	/**
	 * 根据 邮编 得到对应的 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public String[] findAreaByPostCode(String code,String handlerStatus);

	/**
	 * 
	 * 根据地区代码得到相应的医院代码
	 * @param upperCode
	 * @return
	 * @modified:
	 * ☆XMSH(2016年3月7日 上午11:55:36): <br>
	 */
	public List<PrpDHospitalVo> findChildHospital(String areaCode);
	/**
	 * 
	 * 根据地区代码得到相应的伤残鉴定机构代码
	 * @param upperCode
	 * @return
	 * @modified:
	 * ☆YZY(2016年12月21日 上午11:55:36): <br>
	 */
	public List<PrpdAppraisaVo> findChildAppraisa(String areaCode);

	
	public String findAreaList(String code,String AreaCode);
	
	/**
	 * 根据 机构代码 得到对应的 省 市 区 三级代码，并依次放到数组
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	public SysAreaDict findAreaByComCode(String code,BigDecimal level); 
	
	
	/**
	 * 得到某个地区的SysAreaDictVo
	 * @param areaCode
	 * @return
	 */
	public List<SysAreaDictVo> findAreaCode(String areaCode);

	/**
	 * 从基础平台获取开户银行省信息
	 * @param areaCode
	 * @return
	 */
	public ProvinceResponseDto findProvinceInfoFromBasicPlatform(String areaCode);

	/**
	 * 从基础平台获取开户银行市信息
	 * @param areaCode
	 * @return
	 */
	public CityResponseDto findCityInfoFromBasicPlatform(String areaCode);

	public CityResponseDto findCityDetailInfoFromBasicPlatform(String provinceCode, String cityCode);

	public ProvinceResponseDto findProvinceDetailInfoFromBasicPlatform(String provinceCode);

	/**
	 * 根据全名称查询code
	 * @param fullName
	 * @return
	 */
	public List<SysAreaDictVo> findAreaCodeByFullName(String fullName);

	/**
	 * 根据地区名称与上级编码查询地区信息
	 * @param name
	 * @param upperCode
	 * @return
	 */
	public SysAreaDictVo findAreaCodeByNameAndUppercode(String name, String upperCode);
}
