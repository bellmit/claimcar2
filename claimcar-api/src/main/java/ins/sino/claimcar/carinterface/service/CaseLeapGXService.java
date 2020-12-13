package ins.sino.claimcar.carinterface.service;

import ins.sino.claimcar.carinterface.vo.GXCaseBean;

import java.text.ParseException;
import java.util.List;


/**
 * 广西消保
 * @author WURH
 *
 */
public interface CaseLeapGXService {

	/**
	 * 送广西消保
	 * @param caseBeans
	 * @param url
	 * @param user
	 * @param pwd
	 * @throws ParseException
	 */
	public  void importCaseData(List<GXCaseBean> caseBeans,String url,String user,String pwd) throws ParseException;
	
}
