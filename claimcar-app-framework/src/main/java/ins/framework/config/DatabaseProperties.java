package ins.framework.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.SpringProperties;

/**
 * 从数据库里读取配置
 */
public class DatabaseProperties implements InitializingBean, FactoryBean<Properties> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseProperties.class);
	private Properties props = new Properties();
	private DataSource datasource; // 数据源
	private String query; // 读取的sql
	private CustomPropertyConfigurer propertyConfigurer;

	public DatabaseProperties(DataSource datasource, String query) {
		this.datasource = datasource;
		this.query = query;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initProperties();
	}

	@Override
	public Properties getObject() throws Exception {
		return props;
	}

	@Override
	public Class<Properties> getObjectType() {
		return Properties.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	private void initProperties() {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = datasource.getConnection();
			query = query + "where nettype = 'nacos'";
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				String key = rs.getString(1);
				String value = rs.getString(2);
				if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
					SpringProperties.setProperty(key,value);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
	}

	public CustomPropertyConfigurer getPropertyConfigurer() {
		return propertyConfigurer;
	}

	public void setPropertyConfigurer(CustomPropertyConfigurer propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}
}