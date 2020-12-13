/******************************************************************************
 * CREATETIME : 2015年12月29日 下午12:39:56
 ******************************************************************************/
package ins.sino.claimcar.check.service.sdservice;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.CodeConstants.ResponseCode;
import ins.sino.claimcar.carinterface.service.ClaimInterfaceLogService;
import ins.sino.claimcar.carinterface.util.BusinessType;
import ins.sino.claimcar.carinterface.vo.ClaimInterfaceLogVo;
import ins.sino.claimcar.check.po.PrpLCheckCarInfo;
import ins.sino.claimcar.check.vo.PrpLCheckCarInfoVo;
import ins.sino.claimcar.image.service.impl.ImagesDownLoadServiceImpl;
import ins.sino.claimcar.image.util.XstreamFactory;
import ins.sino.claimcar.platform.util.SendPlatformUtil;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.sunyardimage.service.ImagesDownLoadService;
import ins.sino.claimcar.sunyardimage.vo.common.BaseDataVo;
import ins.sino.claimcar.sunyardimage.vo.common.BatchVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqGetChildNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqGetFatherNodeVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqGetImageBaseRootVo;
import ins.sino.claimcar.sunyardimage.vo.request.ReqGetMetaDataVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResPonsePageVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResRootVo;
import ins.sino.claimcar.sunyardimage.vo.response.ResSonNodeVo;
import ins.sino.claimcar.sunyardimage.vo.response.RespageVo;
import ins.sino.claimcar.trafficplatform.service.CarRiskImagesUpdateService;
import ins.sino.claimcar.trafficplatform.vo.CarRiskImagesResponseVo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.Path;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 图片上传接口
 * <pre></pre>
 * @author ★zhujunde
 */
@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"} , timeout = 1000*60*10)
@Path("carRiskImagesUpdateService")
public class CarRiskImagesUpdateServiceImpl implements CarRiskImagesUpdateService {
    private Logger logger = LoggerFactory.getLogger(CarRiskImagesUpdateServiceImpl.class);
    
    private CloseableHttpClient httpclient  = null;
    private CloseableHttpResponse response = null;
    private HttpEntity reqEntity = null;
    private HttpEntity resEntity = null;
    private HttpPost httpost = null;
    private ContentType strContent = null;
    private ContentType streamContent = null;

    @Autowired
    DatabaseDao databaseDao;
    @Autowired
    PolicyViewService policyViewService;
    @Autowired
    private ClaimInterfaceLogService interfaceLogService;
    @Autowired
    private RegistService registService;
    @Autowired
    private CodeTranService codeTranService;
    @Autowired
    ImagesDownLoadService imagesDownLoadService;
    
    @Override
    public void carRiskImagesUpdate(String registNo,SysUserVo userVo,String riskCode,String claimSequenceNo) {
        // 初始化数据
        try {
            httpclient = HttpClients.createDefault();
            // Post请求
            String UPLOAD_URL = SpringProperties.getProperty("UPLOADRISKPIC_URL");
            httpost = new HttpPost(UPLOAD_URL);
            
            //首先创建一个contentType声明上传字段的数据类型和编码
            strContent = ContentType.create("text/plain", Charset.forName("GBK"));
            streamContent = ContentType.create("application/octet-stream", Charset.forName("GBK"));

            String biClaimSequenceNo = "";
            String ciClaimSequenceNo = "";
            List<PrpLCMainVo> prpLCMains = policyViewService.getPolicyAllInfo(registNo);
            if(StringUtils.isNotBlank(claimSequenceNo)){
                biClaimSequenceNo = claimSequenceNo;
            }else{
                if(prpLCMains != null && prpLCMains.size() > 0){//有商业取商业
                    for(PrpLCMainVo vo : prpLCMains){
                        if(riskCode != null && !riskCode.equals(vo.getRiskCode())){
                            // 只需要上传单交强或商业节点时增加判断
                            continue;
                        }
                        if(vo.getComCode().startsWith("62") && SendPlatformUtil.isMor(vo)){
                            if("12".equals(vo.getRiskCode().substring(0, 2))){
                                biClaimSequenceNo = vo.getClaimSequenceNo();
                            }else{
                                ciClaimSequenceNo = vo.getClaimSequenceNo();
                            }
                        }
                    }  
                }
            }
            Map<String, List<String>> threeMap = new HashMap<String,List<String>>();
            Map<String, List<String>> map = new HashMap<String,List<String>>();
            /*final String ImgDataUrl = SpringProperties.getProperty("IMG_MANAGER_URL_IN");//从配置文件读取影像数据地址
            ImgRemoteDataAction imgDataAction= new ImgRemoteDataAction(ImgDataUrl);
            fileAtcion= new ImgRemoteFileAction(ImgDataUrl);*/
            String licenseNo = "";
            //List<ImgViewVo> imgList= imgDataAction.getImgListByBussNo(registNo,"1",null);
            //获取影像图片资源
            ReqGetImageBaseRootVo reqVo = new ReqGetImageBaseRootVo();
    		BaseDataVo baseDataVo = new BaseDataVo();
    		ReqGetMetaDataVo metaDataVo = new ReqGetMetaDataVo();
    		BatchVo batchVo = new BatchVo();
    		batchVo.setAppCode(CodeConstants.APPCODECLAIM);
    		batchVo.setAppName(CodeConstants.APPNAMECLAIM);
    		batchVo.setBusiNo(registNo);
    		metaDataVo.setBatchVo(batchVo);
    		baseDataVo.setComCode("00000000");
    		PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
    		if(prpLRegistVo!=null){
    			  baseDataVo.setOrgCode(prpLRegistVo.getComCode());
    			  baseDataVo.setOrgName(codeTranService.transCode("ComCode",prpLRegistVo.getComCode()));
    		}else{
    			  baseDataVo.setOrgCode(userVo.getComCode());
    			  baseDataVo.setOrgName(codeTranService.transCode("ComCode",userVo.getComCode()));
    		}
			baseDataVo.setUserCode(userVo.getUserCode());
			baseDataVo.setUserName(userVo.getUserName());
			baseDataVo.setRoleCode(CodeConstants.APPROLE);
    		reqVo.setBaseDataVo(baseDataVo);
    		reqVo.setMetaDataVo(metaDataVo);
            String xmlToSend = XstreamFactory.objToXmlUtf(reqVo);
            logger.info("报案信息提交send---------------------------"+xmlToSend);
            ResRootVo resRootVo = new ResRootVo();
            try {
    			
    			String Qurl=SpringProperties.getProperty("YX_QUrl");
    			//Qurl= "";
    			resRootVo = imagesDownLoadService.imagesDownLoad(reqVo, Qurl+"?", "");
            } catch (Exception e) {
    			logger.info("获取图片资源异常==========", e);
    		}
			if (ResponseCode.RESPONSESUCCESSCODE.equals(resRootVo.getResponseCode())) {//返回成功标识
				if (resRootVo.getSydVo() != null && resRootVo.getSydVo().getDocVo() != null){
					//pageInfoHashMap存影像id跟对应图片大小，排除大于1m的
					Map<String, String> pageInfoHashMap = new HashMap<String, String>();
					if(resRootVo.getSydVo().getDocVo().getPageInfoVo() != null){
						List<ResPonsePageVo> pageVos = resRootVo.getSydVo().getDocVo().getPageInfoVo().getPageVos();
						if(pageVos != null && pageVos.size() > 0){
							for(ResPonsePageVo pageVo : pageVos){
								if(StringUtils.isNotBlank(pageVo.getPageSize()) 
										&& Integer.valueOf(pageVo.getPageSize())<(1024*1024)){//上传小于1m的图片
									pageInfoHashMap.put(pageVo.getPageId(), pageVo.getPageSize());
								}
							}
						}
					}
					//respageHashMap存影像id跟对应图片url
					Map<String, String> respageHashMap = new HashMap<String, String>();
					
					if(resRootVo.getPagesVo() != null && resRootVo.getPagesVo().getResPageVos() !=null
							&& resRootVo.getPagesVo().getResPageVos().size() > 0){
						for(RespageVo respageVo : resRootVo.getPagesVo().getResPageVos()){
							respageHashMap.put(respageVo.getPageId(), respageVo.getPageUrl());
						}
					}
					
					//map获取标的车牌号跟对应所有车的图片地址fileViewUrl，threeMap获取三者车牌号跟对应所有车的图片地址fileViewUrl
					if(resRootVo.getSydVo().getDocVo().getVtreeVo() != null) {
						List<ReqGetFatherNodeVo> fatherNodeVos = resRootVo.getSydVo().getDocVo().getVtreeVo().getFatherNodeVo();
						if (fatherNodeVos != null && fatherNodeVos.size() > 0) {
							for (ReqGetFatherNodeVo fatherNodeVo : fatherNodeVos) {
								if(fatherNodeVo.getId().equals("claim-picture")){
									List<ReqGetChildNodeVo> childNodeVoList = fatherNodeVo.getSonNodes();
									for(ReqGetChildNodeVo childNodeVo : childNodeVoList){
										if(childNodeVo.getId().equals("carLoss")){
											List<ResSonNodeVo> sonNodeVoList = childNodeVo.getSonNodes();
											for (ResSonNodeVo sonNodeVo : sonNodeVoList) {
												if(sonNodeVo.getName().contains("标的车")){//来筛选标的车
													List<String> leafs = sonNodeVo.getLeafs();
													for(String leaf : leafs){
														if(pageInfoHashMap.containsKey(leaf)
																&& respageHashMap.containsKey(leaf)){
															String[] typeNames = sonNodeVo.getName().split("\\(");
												            if (typeNames.length > 1)
												            {
												            	licenseNo = typeNames[1].replaceAll("\\)", "");
												            	if (map.containsKey(licenseNo)) {
																	List<String> fileViewUrl = map.get(licenseNo);
																	fileViewUrl.add(respageHashMap.get(leaf));
																	map.put(licenseNo, fileViewUrl);
																} else {
																	List<String> fileViewUrl = new ArrayList<String>();
																	fileViewUrl.add(respageHashMap.get(leaf));
																	map.put(licenseNo, fileViewUrl);
																}
												            }
															
														}
													}
												}else if(sonNodeVo.getName().contains("三者车")){
													List<String> leafs = sonNodeVo.getLeafs();
													String threeLicenseNo ="";
													for(String leaf : leafs){
														if(pageInfoHashMap.containsKey(leaf)
																&& respageHashMap.containsKey(leaf)){
															threeLicenseNo = sonNodeVo.getId();
															String[] typeNames = sonNodeVo.getName().split("\\(");
												            if (typeNames.length > 1)
												            {
												            	threeLicenseNo = typeNames[1].replaceAll("\\)", "");
												            	if (threeMap.containsKey(threeLicenseNo)) {
																	List<String> fileViewUrl = threeMap.get(threeLicenseNo);
																	fileViewUrl.add(respageHashMap.get(leaf));
																	threeMap.put(threeLicenseNo, fileViewUrl);
																} else {
																	List<String> fileViewUrl = new ArrayList<String>();
																	fileViewUrl.add(respageHashMap.get(leaf));
																	threeMap.put(threeLicenseNo, fileViewUrl);
																}
												            }
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}

				}
			}
            if(StringUtils.isNotBlank(biClaimSequenceNo)){
                sendCarRiskImages(registNo,licenseNo,biClaimSequenceNo,map,threeMap,userVo,riskCode);
                }
            if(StringUtils.isNotBlank(ciClaimSequenceNo)){
                sendCarRiskImages(registNo,licenseNo,ciClaimSequenceNo,map,threeMap,userVo,riskCode);
            }
        } catch (UnsupportedEncodingException e) {
        	logger.info("图片上传接口报错信息=============", e);
        }catch (ClientProtocolException e) {
        	logger.info("图片上传接口报错信息=============", e);
        } catch (IOException e) {
        	logger.info("图片上传接口报错信息=============", e);
        } catch(Exception e){
        	logger.info("图片上传接口报错信息=============", e);
        } 
    }
    public void sendCarRiskImages(String registNo,String licenseNo,String claimSequenceNo,
                                  Map<String, List<String>> map,Map<String, List<String>> threeMap,SysUserVo userVo,String riskCode) throws UnsupportedEncodingException,ClientProtocolException,IOException,Exception{
        int count = 20;
        String userName = SpringProperties.getProperty("SDWARN_YMUSER_UP");
        String passWord = SpringProperties.getProperty("SDWARN_YMPW_UP");
        //标的车
        String responseXml = "";
        String requestXml = "";
        List<String> imagePaths = new ArrayList<String>();
        CarRiskImagesResponseVo responseVo = new CarRiskImagesResponseVo();
        List<PrpLCheckCarInfoVo> prpLCheckCarInfoVoList = findPrpLCheckCarInfoVoListByOther(registNo,licenseNo);
        try{
            if(prpLCheckCarInfoVoList != null && prpLCheckCarInfoVoList.size() > 0){
                StringBuilder result = new StringBuilder();
                PrpLCheckCarInfoVo checkCarInfoVo = prpLCheckCarInfoVoList.get(0);
                // 创建混合模式表单实体
                MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
                //设置混编格式
                multipartBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                multipartBuilder.addTextBody("username", userName, strContent);
                multipartBuilder.addTextBody("password", passWord, strContent);
                multipartBuilder.addTextBody("requestType", "C04", strContent);
                multipartBuilder.addTextBody("format", "JSON", strContent);
                multipartBuilder.addTextBody("v", "1.0", strContent);
                multipartBuilder.addTextBody("claimSequenceNo", claimSequenceNo, strContent);
                multipartBuilder.addTextBody("vehicleProperty", "1", strContent);
                multipartBuilder.addTextBody("licensePlateNo", checkCarInfoVo.getLicenseNo(), strContent);
                multipartBuilder.addTextBody("licensePlateType",checkCarInfoVo.getLicenseType(), strContent);
                multipartBuilder.addTextBody("vin", checkCarInfoVo.getVinNo(), strContent);
                File file = null;
                if(map.size() > 0){
                	for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                		 List<String> fileViewURLList = new ArrayList<String>();
                         fileViewURLList = entry.getValue();
                         if(fileViewURLList.size() <= 20){
                             count = fileViewURLList.size();
                         }
                         for(int i=0;i < count;i++){
                             //下载图片到file
                         	file = downloadImage(fileViewURLList.get(i), file);
                             multipartBuilder.addBinaryBody("images", file, streamContent, file.getName());
                             imagePaths.add(file.getCanonicalPath());
                         }
    		        }
                }
                reqEntity = multipartBuilder.build();
                //设置post提交表单entity
                httpost.setEntity(reqEntity);
                // 发送请求
                response = httpclient.execute(httpost);
                
                // 获取返回数据
                int httpCode = response.getStatusLine().getStatusCode();
                if (httpCode == HttpURLConnection.HTTP_OK&&response!=null) {
                    Header[] headers = response.getAllHeaders();
                    resEntity = response.getEntity();
                    Header header = response.getFirstHeader("content-type");
                    //读取服务器返回的json数据（接受json服务器数据）
                    InputStream inputStream = resEntity.getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"GBK");
                    // 读字符串用的。
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                                    String s;
                    while (((s = reader.readLine()) != null)) {
                        result.append(s);
                    }
                    // 关闭输入流
                    reader.close();
                }
                // 此处会打印上传后返回信息，为JSON格式
                logger.info(result.toString());
                //请求报文不存，有时候会Content length is too long
                //requestXml = EntityUtils.toString(reqEntity);
                responseXml = result.toString();
                logger.info(requestXml);
            }
        } catch(Exception e){
        	logger.info("标的车请求山东图片上传接口报错信息=============", e);
        }finally{
        	//删除文件
        	if(imagePaths.size() > 0){
        		for(int i=0;i<imagePaths.size();i++){
        			File imagefile = new File(imagePaths.get(i));
        			 if(imagefile.exists() == true){//删除下载的图片
        				 imagefile.delete();
                     }
        		}
        	}
            try{
                if(StringUtils.isNotBlank(responseXml)){
                    JSONObject rejson = JSONObject.fromObject(responseXml);
                    responseVo = (CarRiskImagesResponseVo) JSONObject.toBean(rejson,CarRiskImagesResponseVo.class);
                }
            }
            catch(Exception e2){
                e2.printStackTrace();
            }finally{
                saveLogVo(registNo,responseVo,userVo,requestXml,responseXml,claimSequenceNo,riskCode); 
            }
        }
        //三者车
        Iterator<Entry<String,List<String>>> threeIter = threeMap.entrySet().iterator();
        List<String> threeImagePaths = new ArrayList<String>();
        while(threeIter.hasNext()){
            Entry<String,List<String>> entry = threeIter.next();
           // List<PrpLCheckCarInfoVo> pthreeCheckCarInfoVoList = findPrpLCheckCarInfoVoListByOther(registNo,entry.getKey());
            List<PrpLCheckCarInfoVo> pthreeCheckCarInfoVoList = findPrpLCheckCarInfoVoListByOther(registNo,licenseNo);
            File file = null;
            try{
                if(pthreeCheckCarInfoVoList != null && pthreeCheckCarInfoVoList.size() > 0){
                    StringBuilder result = new StringBuilder();
                    PrpLCheckCarInfoVo checkCarInfoVo = pthreeCheckCarInfoVoList.get(0);
                    // 创建混合模式表单实体
                    MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create();
                    //设置混编格式
                    multipartBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    multipartBuilder.addTextBody("username", userName, strContent);
                    multipartBuilder.addTextBody("password", passWord, strContent);
                    multipartBuilder.addTextBody("requestType", "C04", strContent);
                    multipartBuilder.addTextBody("format", "JSON", strContent);
                    multipartBuilder.addTextBody("v", "1.0", strContent);
                    multipartBuilder.addTextBody("claimSequenceNo", claimSequenceNo, strContent);
                    multipartBuilder.addTextBody("vehicleProperty", "2", strContent);
                    multipartBuilder.addTextBody("licensePlateNo", checkCarInfoVo.getLicenseNo(), strContent);
                    multipartBuilder.addTextBody("licensePlateType",checkCarInfoVo.getLicenseType(), strContent);
                    multipartBuilder.addTextBody("vin", checkCarInfoVo.getVinNo(), strContent);
                    List<String> fileViewURLList = new ArrayList<String>();
                    fileViewURLList = entry.getValue();
                    if(fileViewURLList.size() <= 20){
                        count = fileViewURLList.size();
                    }
                    
                    for(int i=0;i < count;i++){
                    	//下载图片到file
                    	file = downloadImage(fileViewURLList.get(i), file);
                        multipartBuilder.addBinaryBody("images", file, streamContent, file.getName());
                        threeImagePaths.add(file.getCanonicalPath());
                    }
                    reqEntity = multipartBuilder.build();
                    //设置post提交表单entity
                    httpost.setEntity(reqEntity);
                    // 发送请求
                    response = httpclient.execute(httpost);
                    
                    // 获取返回数据
                    int httpCode = response.getStatusLine().getStatusCode();
                    if (httpCode == HttpURLConnection.HTTP_OK&&response!=null) {
                        Header[] headers = response.getAllHeaders();
                        resEntity = response.getEntity();
                        Header header = response.getFirstHeader("content-type");
                        //读取服务器返回的json数据（接受json服务器数据）
                        InputStream inputStream = resEntity.getContent();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"GBK");
                        // 读字符串用的。
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                                        String s;
                        while (((s = reader.readLine()) != null)) {
                            result.append(s);
                        }
                        // 关闭输入流
                        reader.close();
                    }
                    // 此处会打印上传后返回信息，为JSON格式
                    logger.info(result.toString());
                    //请求报文不存，有时候会Content length is too long
                    //requestXml = EntityUtils.toString(reqEntity);
                    responseXml = result.toString();
                }
            } catch(Exception e){
            	logger.info("三者车请求山东图片上传接口报错信息=============", e);
            }finally{
            	//删除文件
            	if(threeImagePaths.size() > 0){
            		for(int i=0;i<threeImagePaths.size();i++){
            			File imagefile = new File(threeImagePaths.get(i));
            			 if(imagefile.exists() == true){//删除下载的图片
            				 imagefile.delete();
                         }
            		}
            	}
                try{
                    if(StringUtils.isNotBlank(responseXml)){
                        JSONObject rejson = JSONObject.fromObject(responseXml);
                        responseVo = (CarRiskImagesResponseVo) JSONObject.toBean(rejson,CarRiskImagesResponseVo.class);
                    }
                }
                catch(Exception e2){
                    e2.printStackTrace();
                }finally{
                    saveLogVo(registNo,responseVo,userVo,requestXml,responseXml,claimSequenceNo,riskCode); 
                }
            }
        }
       
    }
    public void saveLogVo(String registNo,CarRiskImagesResponseVo responseVo,SysUserVo sysUserVo,
                          String requestXml,String responseXml,String claimSequenceNo,String riskCode){
        ClaimInterfaceLogVo logVo = new ClaimInterfaceLogVo();
        try{
            logVo.setBusinessType(BusinessType.SDEW_imgUpload.name());
            logVo.setBusinessName(BusinessType.SDEW_imgUpload.getName());
            logVo.setCompensateNo(claimSequenceNo);
            logVo.setRemark(riskCode);
            XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
            stream.autodetectAnnotations(true);
            stream.setMode(XStream.NO_REFERENCES);
            stream.aliasSystemAttribute(null,"class");// 去掉 class属性
            if("0000".equals(responseVo.getCode())){//成功
                logVo.setStatus("1");
                logVo.setErrorCode("true");
                logVo.setErrorMessage(responseVo.getMessage());
            }else{
                logVo.setStatus("0");
                logVo.setErrorCode("false");
                logVo.setErrorMessage(responseVo.getMessage());
            }
        }
        catch(Exception e){
            logVo.setStatus("0");
            logVo.setErrorCode("false");
            e.printStackTrace();
        }
        finally{
            logVo.setRegistNo(registNo);
            logVo.setOperateNode("核损");
            logVo.setComCode(sysUserVo.getComCode());
            Date date = new Date();
            logVo.setRequestTime(date);
            logVo.setRequestUrl("");
            logVo.setCreateUser(sysUserVo.getUserCode());
            logVo.setCreateTime(date);
            logVo.setRequestXml(requestXml);
            logVo.setResponseXml(responseXml);
            interfaceLogService.save(logVo);
        }
    }
    @Override
    public List<PrpLCheckCarInfoVo> findPrpLCheckCarInfoVoListByOther(String registNo,String licenseNo) {
        List<PrpLCheckCarInfoVo> prpLCheckCarInfoVoList = new ArrayList<PrpLCheckCarInfoVo>();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addEqual("licenseNo",licenseNo);
        List<PrpLCheckCarInfo> prpLCheckCarInfoList = databaseDao.findAll(PrpLCheckCarInfo.class,queryRule);
        if(prpLCheckCarInfoList != null && prpLCheckCarInfoList.size() > 0){
            prpLCheckCarInfoVoList = Beans.copyDepth().from(prpLCheckCarInfoList).toList(PrpLCheckCarInfoVo.class);
        }
        return prpLCheckCarInfoVoList;
                
    }
    
	public File downloadImage(String imageUrl, File file) throws Exception {
		URL url = new URL(imageUrl);
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式为"POST"
		conn.setRequestMethod("POST");
		// 超时响应时间为20秒
		conn.setConnectTimeout(20 * 1000);
		// 通过输入流获取图片数据
		InputStream inStream = conn.getInputStream();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 1K的数据缓冲
		byte[] fileBuffer = new byte[1024];
		// 读取到的数据长度
		int len = 0;
		// 开始读取
		while ((len = inStream.read(fileBuffer)) != -1) {
			outStream.write(fileBuffer, 0, len);
		}
		byte[] fileData = outStream.toByteArray();
		file = File.createTempFile("temp", ".jpg");//创建临时文件
        logger.info(fileData.length+"imageUrl"+imageUrl+"临时文件所在的本地路径：" + file.getCanonicalPath());
        FileUtils.writeByteArrayToFile(file,fileData);
        
		// 完毕，关闭所有链接
		if (inStream != null) {
			inStream.close();
		}
		if (outStream != null) {
			outStream.close();
		}
		return file;
	}
	public static void main(String[] args) {
		/*	ImageBaseRootVo reqVo = new ImageBaseRootVo();
			BaseDataVo baseDataVo = new BaseDataVo();
			MetaDataVo metaDataVo = new MetaDataVo();
			BatchVo batchVo = new BatchVo();
			batchVo.setAppCode("claim-certify");
			batchVo.setAppName("车险理赔");
			batchVo.setBusiNo("20170242423442316001");
			metaDataVo.setBatchVo(batchVo);
			baseDataVo.setComCode("00000000");
			baseDataVo.setOrgCode("00000000");
			baseDataVo.setOrgName("张三");
			baseDataVo.setRoleCode("admin");
			baseDataVo.setUserCode("00000000");
			baseDataVo.setUserName("管理员");
			reqVo.setBaseDataVo(baseDataVo);
			reqVo.setMetaDataVo(metaDataVo);
	        String xmlToSend = XstreamFactory.objToXmlUtf(reqVo);
	        String param ="";
	        try {
				ImagesDownLoadServiceImpl a = new ImagesDownLoadServiceImpl();
				ResRootVo resRootVo = a.imagesDownLoad(reqVo, "http://10.248.103.90:9001/SunICMS/servlet/RouterServlet?", "");
	        } catch (Exception e) {
				e.printStackTrace();
			}*/
			try {
				URL url = new URL("http://10.248.103.90:9002/SunTRM/servlet/GetImage?UhFOUNO1mC5zmC2r8dUgpPEfcuzaVP67JuaRVP6RDuA4Vh7ipPmRNvIaSuEfJOt-UhMnUcDaXg6RpPtaUu6RpPtaUuAeScMaJufGSPDfX17BVhF-VdtBUcIOpPUzJsHrmNeRmCmRmN5R_C5RmsZs_BIemhUa8h2tmsDg_CezmNF4_NDfUg6sUhI4mNIxmdLEUNmvmN5OUQOEUCygJN2zmgmG8Naf8dt4UNFf8hIf_hmrUP6qpiyiIgU-VhAxUPzBXifrDCOrIiMRpuAqKPnOpvMdMvMOSuaESOaO_CyHp5a48PI4XcFdDTMADCf1");
				// 打开链接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				// 设置请求方式为"POST"
				conn.setRequestMethod("POST");
				// 超时响应时间为20秒
				conn.setConnectTimeout(20 * 1000);
				// 通过输入流获取图片数据
				InputStream inStream = conn.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				File file = null;
				file = File.createTempFile("temp", ".jpg");//创建临时文件
		        
				// 1K的数据缓冲
				byte[] fileData = new byte[1024];
				// 读取到的数据长度
				int len = 0;
				;
				// 开始读取
				while ((len = inStream.read(fileData)) != -1) {
					outStream.write(fileData, 0, len);
				}
				byte[] fileData1 = outStream.toByteArray();
		        System.out.println(fileData.length+"imageUrl"+url+"临时文件所在的本地路径：" + file.getCanonicalPath()+fileData1.length);
				FileUtils.writeByteArrayToFile(file,fileData1);
				// 完毕，关闭所有链接
				if (inStream != null) {
					inStream.close();
				}
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
			
		}
}
