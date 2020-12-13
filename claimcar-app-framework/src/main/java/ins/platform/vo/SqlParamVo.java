/******************************************************************************
* CREATETIME : 2016年6月28日 上午11:12:31
******************************************************************************/
package ins.platform.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年6月28日
 */
public class SqlParamVo {

	private StringBuffer sql;
	private List<Object> params;

	public SqlParamVo(){
		this.sql = new StringBuffer();
		this.params = new ArrayList<Object>();
	}

	public SqlParamVo(String sql,List<Object> params){
		this.sql = new StringBuffer(sql);;
		this.params = params;
	}

	public boolean isEmpty() {
		return params.isEmpty();
	}

	public void addSql(String sql,Object... param) {
		this.sql.append(sql);
		for(Object obj:param){
			this.params.add(obj);
		}
	}

	public void addParams(Object... param) {
		for(Object obj:param){
			this.params.add(obj);
		}
	}
	public void addOr(SqlParamVo sqlVo) {
		if(this.sql.length()==0){
			this.sql.append(sqlVo.getSql());
		}else{
			this.sql.append(" OR ").append(sqlVo.getSql());
		}
		this.params.addAll(sqlVo.getParams());
	}

	public void addAnd(SqlParamVo sqlVo) {
		if(this.sql.length()==0){
			this.sql.append(sqlVo.getSql());
		}else{
			this.sql.append(" AND ").append(sqlVo.getSql());
		}
		this.params.addAll(sqlVo.getParams());
	}

	public StringBuffer getSql() {
		return sql;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setSql(String sql) {
		this.sql = new StringBuffer(sql);
	}

	/**
	 * 在sql语句前后加上一对括号
	 * @return
	 * @modified: ☆LiuPing(2016年6月28日 ): <br>
	 */
	public SqlParamVo bracket() {
		this.sql.insert(0,'(');
		this.sql.append(')');
		return this;
	}

}
