package ins.framework.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.SpringProperties;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 配置中心数据获取工具类 nacos server currentVersion：1.1.3
 * <p>nacos server version 升级至1.2.0版本之后，请求数据时需添加身份认证信息</p>
 * @author maofengning
 */
public class NacosProperties implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(NacosProperties.class);

    /** Nacos服务 */
    private ConfigService configService = null;
    private String server = SpringProperties.getProperty("nacos_server");
    private String namespace = SpringProperties.getProperty("nacos_namespace");
    private String group = SpringProperties.getProperty("nacos_group");
    private String dataAll = SpringProperties.getProperty("nacos_dataall");
    private String dataIn = SpringProperties.getProperty("nacos_datain");
    private String dataOut = SpringProperties.getProperty("nacos_dataout");
    /** 当前服务对象类型 in-内网 out-外网 */
    private static String NETTYPE_IN = "in";
    private static String NETTYPE_OUT = "out";

    @Override
    public void afterPropertiesSet() throws Exception {
        initNacosProperties();
    }

    /**
     * 初始化系统配置信息
     * @throws Exception
     */
    private void initNacosProperties() throws Exception {
        getNacosService();
        handleNacosContent();
    }

    /**
     * 获取Nacos服务
     */
    private void getNacosService() throws Exception {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, server);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        configService = NacosFactory.createConfigService(properties);
    }

    /**
     * 获取Nacos配置数据
     */
    private void handleNacosContent() throws Exception {
        logger.info("NacosProperties配置中心数据初始化SpringProperties开始……");
        String content = configService.getConfig(dataAll, group, 5000);
        if (StringUtils.isBlank(content)) {
            throw new Exception("NacosProperties配置中心获取nettype==all的数据");
        }
        loadSpringProperties(content);
        logger.info("\nNacosProperties配置中心获取nettype==all的数据完成\n{}", content);
        // 获取当前服务对象类型（内网？外网？）
        String nettype = System.getProperty("nettype");
        logger.info("NacosProperties 理赔系统当前网络环境为：{}", nettype);
        if (StringUtils.isNotBlank(nettype)) {
            if (NETTYPE_IN.equals(nettype)) {
                content = configService.getConfig(dataIn, group, 5000);
                logger.info("\nNacosProperties配置中心获取nettype==in的数据完成\n{}", content);
            }
            if (NETTYPE_OUT.equals(nettype)) {
                content = configService.getConfig(dataOut, group, 5000);
                logger.info("\nNacosProperties配置中心获取nettype==out的数据完成\n{}", content);
            }
            loadSpringProperties(content);
        }
        logger.info("NacosProperties配置中心数据初始化SpringProperties结束……");
    }

    /**
     * 将配置数据存入SpringProperties对象中
     */
    private void loadSpringProperties(String content) throws IOException {
        if (StringUtils.isNotBlank(content)) {
            Properties properties = new Properties();
            InputStream is = new ByteArrayInputStream(content.getBytes());
            properties.load(is);

            Set<Map.Entry<Object, Object>> propSet = properties.entrySet();
            for (Map.Entry<Object, Object> map : propSet) {
                if (map.getKey() != null && map.getKey() != "" && map.getValue() != null) {
                    String key = map.getKey().toString();
                    String value = map.getValue().toString();
                    SpringProperties.setProperty(key, value);
                }
            }
        }
    }
}
