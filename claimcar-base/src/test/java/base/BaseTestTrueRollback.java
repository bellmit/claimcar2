package base;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 基本测试类，用于被继承，当测试方法配置@Transactional时，测试方法执行完后回滚事务
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-6-24
 * @since (2014-6-24 上午09:12:54): <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/applicationContext.xml","/spring/applicationContext-hibernate.xml","/spring/dataAccessContext-test.xml"})
public class BaseTestTrueRollback {

	@Autowired
	private SessionFactory sessionFactory;
	private Session session;

	@Before
	public void openSession() throws Exception {
		session = sessionFactory.openSession();// SessionFactoryUtils. getSession(sessionFactory,true);
		session.setFlushMode(FlushMode.MANUAL);
		TransactionSynchronizationManager.bindResource(sessionFactory,new SessionHolder(session));
	}

	@After
	public void closeSession() throws Exception {
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(session);
	}

}
