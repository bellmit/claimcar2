package ins.sino.claimcar.hnbxrest.service;

import ins.sino.claimcar.hnbxrest.vo.PrplQuickCaseInforVo;





public interface SubmitcaseinforService {

	/**
	 * 保存快赔案件信息
	 * <pre></pre>
	 * @param prplQuickCaseInforVo
	 * @modified:
	 * ☆LinYi(2017年9月19日 下午2:45:04): <br>
	 */
	public void save(PrplQuickCaseInforVo prplQuickCaseInforVo);
	/**
	 * 通过快赔报案号查询快赔信息
	 * <pre></pre>
	 * @param casenumber
	 * @modified:
	 * ☆LinYi(2017年9月18日 下午4:57:47): <br>
	 */
	public PrplQuickCaseInforVo findByCasenumber(String casenumber);
	
	/**
	 * 通过报案号查询快赔信息
	 * @param registNo
	 * @return
	 */
	public PrplQuickCaseInforVo findByRegistNo(String registNo);
	
}
