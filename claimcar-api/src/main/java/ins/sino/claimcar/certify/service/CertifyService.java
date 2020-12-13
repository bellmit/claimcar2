package ins.sino.claimcar.certify.service;

import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyCodeVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyDirectVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyItemVo;
import ins.sino.claimcar.certify.vo.PrpLCertifyMainVo;

import java.util.List;
import java.util.Map;

public interface CertifyService {

	/**
	 * 查找所有单证类型
	 * @return
	 */
	public abstract List<PrpLCertifyCodeVo> findAllPrpLCertifyCodeList();

	/**
	 * 根据报案号查找单证主表信息
	 * @param registNo
	 * @return
	 * @throws Exception
	 */
	public abstract PrpLCertifyMainVo findPrpLCertifyMainVo(String registNo);

	/**
	 * 根据主表主键查找单证主表信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public abstract PrpLCertifyMainVo findPrpLCertifyMainVoByPK(Long id);

	/**
	 * 保存索赔单证信息
	 * @param prpLCertifyItemVoList
	 * @throws Exception
	 */
	public abstract void saveAllPrpLCertifyItem(List<PrpLCertifyItemVo> prpLCertifyItemVoList) throws Exception;
	
	/**
	 * 保存索赔单证信息，跳过重复的单证分类代码
	 * @param prpLCertifyItemVoList
	 */
	public abstract void savePrpLCertifyItem(List<PrpLCertifyItemVo> prpLCertifyItemVoList);

	/**
	 * 查找已勾选的单证
	 * @param map
	 * @return
	 */
	public abstract List<PrpLCertifyItemVo> findPrpLCertifyItemVoList(Map<String,String> map);

	/**
	 * 保存已存在单证大类的单证
	 * @param prpLCertifyDirectVoList
	 * @throws Exception 
	 */
	public abstract void saveAllPrpLCertifyDirect(List<PrpLCertifyDirectVo> prpLCertifyDirectVoList) throws Exception;

	/**
	 * 更新单证主表信息
	 * @param prpLCertifyMainVo
	 */
	public abstract void updatePrpLCertifyMainVo(PrpLCertifyMainVo prpLCertifyMainVo);

	/**
	 * 更新单证项信息
	 * @param PrpLCertifyItemVoList
	 */
	public abstract void updatePrpLCertifyItemList(List<PrpLCertifyItemVo> prpLCertifyItemVoList);

	/**
	 * 删除取消的单证项
	 * @param deleteCertifyDirect
	 * @throws Exception 
	 */
	public abstract void deleteAllCertifyDirect(String registNo,List<Long> deleteCertifyDirect) throws Exception;

	/**
	 * 取消单证项的所有单证时删除单证项
	 * @param deleteCertifyDirect
	 * @param deleteCertifyItem
	 * @throws Exception 
	 */
	public abstract void deleteAllCertifyItem(String registNo,List<Long> deleteCertifyDirect,List<String> deleteCertifyItem) throws Exception;

	/**
	 * 查找其他单证项最大itemLossCode
	 * @param typeCode
	 * @return
	 */
	public abstract long findMaxItemLossCodeOfOtherDirect(String typeCode,String registNo);

	/**
	 * 保存其他单证
	 * @param prpLCertifyDirectList
	 * @throws Exception 
	 */
	public abstract void saveOtherPrpLCertifyDirect(List<PrpLCertifyDirectVo> otherPrpLCertifyDirect,SysUserVo userVo) throws Exception;

	/**
	 * 查找上传图片类型
	 * @return
	 */
	/*
	public List<PrpLCertifyCodeVo> findImgPrpLCertifyCodeList(){
	List<PrpLCertifyCodeVo> prpLCertifyCodeVoList= new ArrayList<PrpLCertifyCodeVo>();
	QueryRule queryRule = QueryRule.getInstance();
	queryRule.addEqual("upCertifyCode","P");//单证大类
	List<PrpLCertifyCode> prpLCertifyCodeList = databaseDao.findAll(PrpLCertifyCode.class,queryRule);
	prpLCertifyCodeVoList = Beans.copyDepth().from(prpLCertifyCodeList).toList(PrpLCertifyCodeVo.class);
	return prpLCertifyCodeVoList;
	}*/
	/**
	 * 查找PrpLCertifyDirect
	 * @param map map key为in按,截断第一个为字段，之后的为值 
	 * @return
	 */
	public abstract List<PrpLCertifyDirectVo> findPrpLCertifyDirectVoList(Map<String,String> map);

	/**
	 * 单证主表保存
	 * @param prpLCertifyMainVo
	 * @return
	 */
	public abstract PrpLCertifyMainVo submitCertify(PrpLCertifyMainVo prpLCertifyMainVo);

	/**
	 * 
	 * 根据单证代码查询PrpLCertifyCodeVo
	 * @param mustUpload
	 * @return
	 * @modified:
	 * ☆zhujunde(2017年7月13日 上午10:09:11): <br>
	 */
    public abstract List<PrpLCertifyCodeVo> findCertifyCodeVoByMustUpload(String certifyTypeCode);
    
    /**
	 * 判断单证前的节点是否全部上传平台成功
	 * @param registNo
	 * @return
	 */
	public boolean isPassPlatform(String registNo);
}
