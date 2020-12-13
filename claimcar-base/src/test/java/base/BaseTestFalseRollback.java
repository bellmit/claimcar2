package base;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 * 基本测试类，用于被继承，当测试方法配置@Transactional时，测试方法执行完后提交事务
 * 
 * @author ★<a href="mailto:huangyi@sinosoft.com.cn">HuangYi</a>
 * @Company www.sinosoft.com.cn
 * @Copyright Copyright (c) 2014-6-24
 * @since (2014-6-24 上午09:12:54): <br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/applicationContext.xml","/spring/applicationContext-hibernate.xml","/spring/dataAccessContext-test.xml"})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,TransactionalTestExecutionListener.class})
// defaultRollback = false 为不回滚事物，默认是 测试完成之后 回滚 事务，数据部会 insert 到数据库中。
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class BaseTestFalseRollback {

}
