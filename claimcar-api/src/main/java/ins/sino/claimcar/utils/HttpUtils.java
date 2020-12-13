package ins.sino.claimcar.utils;

import ins.sino.claimcar.CodeConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringProperties;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * HTTP请求类
 */
public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * http请求
     *
     * @param urlString     请求地址
     * @param requestParams 请求参数
     * @param caller        请求发起方
     * @return
     * @throws Exception
     */
    public static String commonRequest(String urlString, String requestParams, String caller) throws Exception {
        logger.info("\n" + caller + " 请求地址为：" + urlString + "  请求参数为：" + requestParams + "\n");
        if (caller == null) {
            caller = "";
        }

        // 如果请求地址为空或者请求参数为空，直接返回空字符串
        if (urlString == null || urlString.trim().length() == 0) {
            logger.info(caller+"请求地址为空！");
            return "";
        }
        if (requestParams == null || requestParams.trim().length() == 0) {
            return "";
        }

        BufferedReader bfreader = null;
        HttpURLConnection httpURLConnection = null;
        StringBuffer buffer = new StringBuffer();
        //获取超时时间
        String timeout = SpringProperties.getProperty("HTTP_TIMEOUT");
        if (StringUtils.isBlank(timeout)) {
            timeout = "20";
        }

        try {
            URL url = new URL(urlString + requestParams);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");
            // post方式不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 配置本次连接的Content-Type，配置为text/xml
            httpURLConnection.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            // 维持长连接
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setConnectTimeout(Integer.valueOf(timeout) * 1000);
            httpURLConnection.setReadTimeout(Integer.valueOf(timeout) * 1000);
            httpURLConnection.setAllowUserInteraction(true);
            httpURLConnection.connect();
        } catch (Exception ex) {
            logger.info(caller + "连接失败", ex);
            throw new Exception(caller + "连接失败，请稍候再试", ex);
        }
        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            out.write(requestParams.getBytes("utf-8"));
            out.flush();
            out.close();
            bfreader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            String strLine = "";
            while ((strLine = bfreader.readLine()) != null) {
                buffer.append(strLine);
            }
        } catch (Exception e) {
            logger.info(caller + "读取接口返回数据失败！", e);
            throw new Exception("读取接口返回数据失败", e);
        } finally {
            if (bfreader != null) {
                bfreader.close();
            }
        }
        return buffer.toString();
    }

    /**
     * 获取上海标准路名唯一标识
     * @param areaCode 区域代码
     * @param damageAddress 出险地址
     * @param registNo 报案号
     */
    public static String getSHAccidentPlaceMark(String areaCode, String damageAddress, String registNo) {

        logger.info(registNo + " " + damageAddress + " 开始获取上海标准地名信息！");
        String accidentPlaceMark = "9999999";
        if (StringUtils.isNotBlank(areaCode) && areaCode.startsWith("31") && StringUtils.isNotBlank(damageAddress)) {
            try {
                String url = SpringProperties.getProperty("SH_STANDARD_ROAD");
                String roadInfo = HttpUtils.commonRequest(url,
                        URLEncoder.encode(URLEncoder.encode(damageAddress, "UTF-8"), "UTF-8"),
                        "上海标准地名信息");
                logger.info(damageAddress + " 获取到的上海标准地名信息为：" + roadInfo);
                if (StringUtils.isNotBlank(roadInfo)) {
                    String[] roadInfoArray = roadInfo.split("\\*");
                    if (roadInfoArray.length > 0) {
                        accidentPlaceMark = roadInfoArray[0];
                    }
                    logger.info(registNo + " " + damageAddress + " 获取上海标准地名信息结束！");
                }
            } catch (Exception e) {
                logger.info(registNo + " " + damageAddress + " 获取上海标准地名信息失败！", e);
            }
        }

        return accidentPlaceMark;
    }

    public static String getSHRoadInfo(String areaCode, String damageAddress, String registNo) {

        logger.info("\n" + registNo + " " + damageAddress + " 开始获取上海标准地名信息！");
        StringBuilder damageAddressLocal = new StringBuilder();
        if (StringUtils.isNotBlank(areaCode) && areaCode.startsWith("31") && StringUtils.isNotBlank(damageAddress)) {
            try {
                String url = SpringProperties.getProperty("SH_STANDARD_ROAD");
                String roadInfo = HttpUtils.commonRequest(url,
                        URLEncoder.encode(URLEncoder.encode(damageAddress, "UTF-8"), "UTF-8"),
                        "上海标准地名信息");
                logger.info(damageAddress + " 获取到的上海标准地名信息为：" + roadInfo);
                int len = 0; // 记录级数
                if (StringUtils.isNotBlank(roadInfo)) {
                    roadInfo = roadInfo.replace(",", "");
                    String[] roadInfoArray = roadInfo.split("\\*");
                    if (roadInfoArray.length > 0) {
                        for (int i = 1; i < roadInfoArray.length; i++) {
                            if (StringUtils.isNotEmpty(roadInfoArray[i])) {
                                if (i == 1) {
                                    damageAddressLocal.append(roadInfoArray[i]);
                                } else {
                                    damageAddressLocal.append("-").append(roadInfoArray[i]);
                                }
                                len++;
                            }
                        }
                        // 出险地点分级，最低五级
                        if (len < 5) {
                            damageAddressLocal = new StringBuilder("上海市-").append(damageAddressLocal);
                            for (int j = 1; j < 5-len; j++){
                                damageAddressLocal.append("-").append("字段"+j);
                            }
                        }
                    }
                }
                logger.info(registNo + " " + damageAddress + " 获取上海标准地名信息结束！");
            } catch (Exception e) {
                logger.info(registNo + " " + damageAddress + " 获取上海标准地名信息失败！", e);
            }
        }
        logger.info("\n" + registNo + " " + damageAddress + " 分级之后的数据：" + damageAddressLocal.toString());
        return damageAddressLocal.toString();
    }

    /**
     * 获取上海出险地址经纬度（符合传平台标准）
     * @param damageMapCode 出险地址经纬度
     * @return
     */
    public static String getCoordinate(String damageMapCode) {
        if (StringUtils.isBlank(damageMapCode)) {
            return "-1,-1";
        }
        String coordinate = "";
        if (StringUtils.isNotBlank(damageMapCode)) {
            // 上海平台要求经纬度精确到小数点后6位，精度不够需要补0
            String[] array = damageMapCode.split(",");
            for (String str : array) {
                if (str.split("\\.").length == 1) {
                    str = str + ".0";
                }
                while (str.split("\\.")[1].length() < 6) {
                    str = str + "0";
                }
                coordinate = coordinate + str + ",";
            }
            coordinate = coordinate.substring(0, coordinate.length() - 1);
        }

        return coordinate;
    }

    /**
     * 获取国标省市编码
     * @param areaCode 修理厂所在省市区编码（国标）
     * @param model 编码等级（省级、市级）
     * @return
     */
    public static String getGBCode(String areaCode, int model) {
        String result = "";
        if (StringUtils.isNotBlank(areaCode)) {
            switch (model){
                case CodeConstants.AreaCodeModel.GB_PROVINCECODE:
                    if (areaCode.length() >= 2) {
                        result = areaCode.substring(0, 2) + "0000";
                    }
                    break;
                case CodeConstants.AreaCodeModel.GB_CITYCODE:
                    if (areaCode.length() >= 4) {
                        result = areaCode.substring(0, 4) + "00";
                    }
                    break;
                default:
                    return "";
            }
        }
        return result;
    }
}

