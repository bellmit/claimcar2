package ins.platform.common.service.facade;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.Page;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service(value = "baseDaoService")
public class BaseDaoService extends HibernateDaoSupport {

	@Autowired
	DatabaseDao databaseDao;

	public Page<Object[]> findPageBySql(String sql,int pageNo,int pageSize,Object[] values) {
		return findPageBySql(Object[].class,sql,pageNo,pageSize,values);
	}

	public <T> Page<T> findPageBySql(Class<T> entityClass,String sql,int pageNo,int pageSize,Object[] values) {
		Assert.hasText(sql);
		// InjectionCheckUtils.checkValidSql(sql);
		if(pageNo<=0){
			pageNo = 1;
		}
		if(pageSize<=0){
			pageSize = 10;
		}

		int startIndex = Page.getStartOfPage(pageNo,pageSize);

		if(startIndex<0){
			return new Page();
		}

		List list = findRangeBySql(entityClass,sql,startIndex,pageSize,values);
		long totalCount = getCountBySql(sql,values);

		return new Page(startIndex,totalCount,pageSize,list);
	}

	public long getCountBySql(final String sql,final Object[] values) {
		Assert.hasText(sql,"sql must have value.");
		List countList = (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
					throws HibernateException {
				Query query = createSQLQuery(sess, sql, values);
				return query.list();
			}
		});
		
		int totalCount = 0;
		if (!StringUtils.isEmpty(countList)) {
			totalCount = new Integer(countList.get(0).toString());
		}
		
		return totalCount;
	}

	protected static String processQL(String ql,Object[] values) {
		String newQL = ql;
		int pos = 0;
		for(int i = 0; i<values.length; ++i){
			pos = newQL.indexOf(63,pos);
			if(pos== -1){
				throw new IllegalArgumentException("params and values must match.");
			}

			if(values[i] instanceof Collection){
				newQL = newQL.substring(0,pos)+":queryParam"+i+newQL.substring(pos+1);
			}

			pos += 1;
		}

		return newQL;
	}

	public List<Object[]> getAllBySql(String sql,Object[] values) {
		return getAllBySql(Object[].class,sql,values);
	}

	public <T> List<T> getAllBySql(Class<T> entityClass,String sql,Object[] values) {
		return findRangeBySql(entityClass,sql,Integer.valueOf(0),Integer.valueOf(2147483647),values);
	}

	public List<Object[]> findRangeBySql(String sql,int start,int length,Object[] values) {
		return findRangeBySql(Object[].class,sql,start,length,values);
	}

	public <T> List<T> findRangeBySql(Class<T> entityClass,String sql,int start,int length,Object[] values) {
		Assert.hasText(sql,"sql must have value.");
		// InjectionCheckUtils.checkValidSql(sql);
		// String fnSql = processQL(sql,values);
		List list = findByPage(sql,values,start,length);
		return list;
	}

	public List findByPage(final String hql,final Object[] values,final int offset,final int pageSize) {
		List list = (List) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(hql);
				for(int i = 0; i<values.length; i++ ){
					query.setParameter(i,values[i]);
				}
				List result = query.setFirstResult(offset).setMaxResults(pageSize).list();
				return result;
			}
		});
		return list;
	}
	
	/**
	 * 根据SQL语句查询，并返回集合
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param values
	 *            可变参数列表
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List findListBySql(final String sql, final Object... values)
			throws Exception {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess)
					throws HibernateException {
				Query query = createSQLQuery(sess, sql, values);
				return query.list();
			}
		});
	}
	/**
	 * 根据SQL语句查询，并返回集合
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List findListBySql(final String sql) throws Exception {
		return findListBySql(sql, new Object[0]);
	}
	/**
	 * 分页查询(SQL)
	 * 
	 * @param resultSql
	 *            查询结果集的SQL
	 * @param countSql
	 *            统计结果集数量的SQL
	 * @param pageNo
	 *            当前页，从1开始
	 * @param pageSize
	 *            每页显示的记录数
	 * @param values
	 *            可变参数列表
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Page pagedSQLQuery(String resultSql, String countSql, int start,
			int pageSize, Object... values) throws Exception {
		List listCount = findListBySql(countSql, values);
		int totalCount = 0;
		if (!StringUtils.isEmpty(listCount)) {
			totalCount = new Integer(listCount.get(0).toString());
		}
		if (totalCount < 1) {
			return new Page();
		}
		Session sess = currentSession();
		Query query = createSQLQuery(sess, resultSql, values);
		List list = query.setFirstResult(start).setMaxResults(pageSize)
				.list();
		return new Page(start, totalCount, pageSize, list);
	}
	/**
	 * 分页查询(SQL)
	 * 
	 * @param sql
	 *            查询结果集的SQL
	 * @param pageNo
	 *            当前页，从1开始
	 * @param pageSize
	 *            每页显示的记录数
	 * @param values
	 *            可变参数列表
	 * @return
	 * @throws Exception
	 */
	public Page pagedSQLQuery(String sql, int start, int pageSize,
			Object... values) throws Exception {
		String countSql = "select count(*) from (" + sql;
		countSql += ") pagedSQLQuery";
		return pagedSQLQuery(sql, countSql, start, pageSize, values);
	}
	/**
	 * 创建SQLQuery对象
	 * 
	 * @param sess
	 *            会话连接
	 * @param sql
	 *            SQL语句
	 * @param values
	 *            可变参数列表
	 * @return
	 */
	protected Query createSQLQuery(Session sess, String sql, Object... values) {
		Query query = sess.createSQLQuery(sql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}
	
	public void executeSQL(String sql){
		final String tempsql = sql;
		this.getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)throws HibernateException{
				session.createSQLQuery(tempsql).executeUpdate();
				return null;
			}
		});
	}

}
