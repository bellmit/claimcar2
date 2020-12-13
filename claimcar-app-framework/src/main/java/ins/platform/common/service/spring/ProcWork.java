package ins.platform.common.service.spring;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.jdbc.Work;

public abstract class ProcWork implements Work{

	private String proSql; //存储过程语句
	private ResultSet rs = null; //返回结果集
	private Map<String,Object> pro = new HashMap<String,Object>(); //存储过程涉及参数
	
	public ProcWork(String proSql,Object... params){
		this.proSql = proSql;
		int i = 1;
		for(Object obj:params){
			pro.put(String.valueOf(i), obj);
			i++;
		}
	}
	
	
	@Override
	public abstract void execute(Connection conn) throws SQLException;
	
	public String getProSql() {
		return proSql;
	}
	public void setProSql(String proSql) {
		this.proSql = proSql;
	}
	public ResultSet getRs() {
		return rs;
	}
	public void setRs(ResultSet rs) {
		this.rs = rs;
	}
	public Map getPro() {
		return pro;
	}
	public void setPro(Map pro) {
		this.pro = pro;
	}
	
	

}
