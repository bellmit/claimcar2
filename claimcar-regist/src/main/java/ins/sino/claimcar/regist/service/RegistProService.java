package ins.sino.claimcar.regist.service;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.spring.ProcWork;
import ins.sino.claimcar.regist.po.PrpLCMain;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "registProService")
public class RegistProService {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	DatabaseDao databaseDao;
	
	/**
	 * 报案保存提交调用存储过程存储PrpLCmainVo信息
	 * @param registNo
	 * @param policyNo
	 */
	public void insertPrpLCmainVoPro(String registNo, String policyNo) {
		Session session = sessionFactory.getCurrentSession();
		session.doWork(new ProcWork("{call REGIST_PACKAGE.registSubmitSaveLCmain(?,?)}",policyNo,registNo){
			@Override
			public void execute(Connection conn) throws SQLException {
				CallableStatement statement = conn.prepareCall(this.getProSql());
				//为存储过程设置参数
				statement.setString(1, this.getPro().get("1").toString());
				statement.setString(2, this.getPro().get("2").toString());
				statement.execute();
			}
		});
	}
	
	public PrpLCMainVo findPolicyInfoByPaltform(String registNo,String policyNo) {
		PrpLCMainVo prpLCMainVo = null;
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("registNo",registNo);
		queryRule.addEqual("policyNo",policyNo);
		List<PrpLCMain> prpLCMain = databaseDao.findAll(PrpLCMain.class,queryRule);
		if(prpLCMain!=null&&prpLCMain.size()>0){
			prpLCMainVo = new PrpLCMainVo();
			prpLCMainVo = Beans.copyDepth().from(prpLCMain.get(0)).to(PrpLCMainVo.class);
		}
		return prpLCMainVo;
	}
}
