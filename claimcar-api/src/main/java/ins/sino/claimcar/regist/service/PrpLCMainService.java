package ins.sino.claimcar.regist.service;

import ins.sino.claimcar.flow.vo.AMLVo;
import ins.sino.claimcar.regist.vo.PrpCiInsureValidVo;
import ins.sino.claimcar.regist.vo.PrpLCItemCarVo;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.Date;
import java.util.List;

public interface PrpLCMainService {

	/**
	 * 初始化报案编辑页面时，调用保单检查存储过程，得到PrpLCMain相关数据
	 * @param policyNo
	 * @param damageDate
	 * @return
	 * @modified: ☆LiuPing(2016年7月1日 ): <br>
	 */
	public PrpLCMainVo findRegistPolicy(String policyNo, Date damageDate);

	public PrpLCMainVo findPrpLCMain(String registNo, String policyNo);

	/**
	 * 报案保存时，掉用存储过程 PolicyCheck_Package.saveRegistPolicy 保存PrpLCMain相关表数据
	 * @param registNo
	 * @param policyNo
	 * @param damageDate
	 * @return
	 * @modified: ☆LiuPing(2016年7月1日 ): <br>
	 */
	public PrpLCMainVo saveRegistPolicy(String registNo, String policyNo,
			Date damageDate);

	/**
	 * 
	 * @param prpLCMains
	 */
	public void saveOrUpdate(List<PrpLCMainVo> prpLCMains, String registNo);

	/*
	 * private List<PrpLCInsured> prpCInsureds = new ArrayList<PrpLCInsured>(0);
	private List<PrpLCItemCar> prpCItemCars = new ArrayList<PrpLCItemCar>(0);
	private List<PrpLCengage> prpCengages = new ArrayList<PrpLCengage>(0);
	private List<PrpLCItemKind> prpCItemKinds = new ArrayList<PrpLCItemKind>(0); 
	 */

	public List<PrpLCMainVo> findPrpLCMainsByRegistNo(String registNo);
	//理赔编码查询
	public List<PrpLCMainVo> findPrpLCMainsByClaimSequenceNo(String claimSequenceNo);

	public List<PrpLCMainVo> updateValidFlag(PrpLCMainVo prpLCMainVo);

	public void deleteByRegistNo(PrpLRegistVo registVo);

	public void updateItemCar(PrpLCItemCarVo vo);
	public void updateItemKind(PrpLCItemKindVo vo);

	//查询
	public List<PrpCiInsureValidVo> findPrpCiInsureValidByPolicyNo(
			String policyNo);
	
	/**
	 * 通过报案号查询保单表以及被保险人表优先获取商业
	 * <pre>反洗钱</pre>
	 * @param registNo
	 * @return
	 * @modified:
	 * ☆LinYi(2017年7月6日 下午4:43:41): <br>
	 */
	public AMLVo findAMLInfoByRegistNo(String registNo);
	
	public PrpLCMainVo updatePrpLCMain(PrpLCMainVo prpLCMainVo);
	
	public PrpLCMainVo findPrpLCMainByClaimSequenceNo(String claimSequenceNo);
}