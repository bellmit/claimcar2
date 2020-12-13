package ins.sino.claimcar.realtimequery.web.servlet;

import ins.platform.utils.ClaimBaseCoder;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.carYxImage.service.CarXyImageService;
import ins.sino.claimcar.court.dlclaim.xyvo.*;
import ins.sino.claimcar.jiangxicourt.web.action.BaseServlet;
import ins.sino.claimcar.realtimequery.web.servlet.vo.ImageDateVo;
import ins.sino.claimcar.realtimequery.web.servlet.vo.ImageInfoVo;
import ins.sino.claimcar.realtimequery.web.servlet.vo.ImageReqVo;
import ins.sino.claimcar.realtimequery.web.servlet.vo.ImageResVo;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ins.sino.claimcar.regist.service.RegistTmpService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import org.springframework.core.SpringProperties;
import sun.misc.BASE64Encoder;

public class ImageQueryServlet extends BaseServlet {
    @Autowired
    RegistTmpService registTmpService;
    @Autowired
    CarXyImageService carXyImageService;

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(ImageQueryServlet.class);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ImageResVo imageResVo = new ImageResVo();
		String path = "";
		File file = null;
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
            StringBuilder responseBuilder = new StringBuilder();
            String inputStr = null;
            while ((inputStr = streamReader.readLine()) != null) {
                responseBuilder.append(inputStr);
            }
            logger.info("反欺诈影像查询报文：{} ", responseBuilder.toString());
            if (StringUtils.isBlank(responseBuilder.toString())) {
                throw new IllegalArgumentException("接收参数为空");
            }
            ImageReqVo imageReqVo = JSON.parseObject(responseBuilder.toString(), ImageReqVo.class);
            if (StringUtils.isBlank(imageReqVo.getClaimCode())) {
                throw new IllegalArgumentException("参数理赔编号为空");
            }
            if (StringUtils.isBlank(imageReqVo.getPituresNo())) {
                throw new IllegalArgumentException("参数图片序号为空");
            }
            if (StringUtils.isBlank(imageReqVo.getClaimCompany())) {
                throw new IllegalArgumentException("参数公司代码为空");
            }
            if (StringUtils.isBlank(imageReqVo.getInsurerUuid())) {
                throw new IllegalArgumentException("参数流水号为空");
            }
			String registNo = null;
			List<PrpLCMainVo> prpLCMainVoList = registTmpService.queryByClaimsequenceNo(imageReqVo.getClaimCode());
            if (prpLCMainVoList != null && prpLCMainVoList.size() > 0) {
				//一般来说，一个报案号对应一个理赔编号信息，所以默认取第一条
				registNo = prpLCMainVoList.get(0).getRegistNo();
            }else{
				throw new IllegalArgumentException("无法查询到理赔编号对应的保单信息！");
			}
            if(StringUtils.isBlank(registNo)){
				throw new IllegalArgumentException("无法查询到理赔编号对应的保单信息！");
			}
			int pituresNo = Integer.parseInt(imageReqVo.getPituresNo());
            SysUserVo userVo = new SysUserVo();
			userVo.setComCode("00000000");
			userVo.setUserCode("0000000000");
			userVo.setUserName("管理员");
            String url = SpringProperties.getProperty("YX_QUrl");
			List<ImageInfoVo> claimPhotosVos=new ArrayList<ImageInfoVo>();
            String resXml = carXyImageService.reqResourceFromXy(userVo, registNo, url);
            if (StringUtils.isNotBlank(resXml) && resXml.contains("<RESPONSE_CODE>200</RESPONSE_CODE>")) {
                String imgXml = changeXml(resXml);
                if (org.apache.commons.lang.StringUtils.isNotBlank(imgXml)) {
                    ImageUrlbaseVo imageUrlbaseVo = ClaimBaseCoder.xmlToObj(imgXml, ImageUrlbaseVo.class);
					if(imageUrlbaseVo!=null && imageUrlbaseVo.getSydVo()!=null && imageUrlbaseVo.getSydVo().getDocVo()!=null &&
							imageUrlbaseVo.getSydVo().getDocVo().getDocInfoVo()!=null && imageUrlbaseVo.getSydVo().getDocVo().getVtreeImageVo()!=null){
						List<NodeImageVo> nodeImageVos0=imageUrlbaseVo.getSydVo().getDocVo().getVtreeImageVo().getNodeImageVos();
						if(nodeImageVos0!=null && nodeImageVos0.size()>0){
							for(NodeImageVo imageVo0:nodeImageVos0){
								if("claim-picture".equals(imageVo0.getId())){
									List<NodeImageVo> nodeImageVos1=imageVo0.getNodeImageVos();
									if(nodeImageVos1!=null && nodeImageVos1.size()>0){
										for(NodeImageVo imageVo1:nodeImageVos1){
											//取出所有图片中属于理赔的，并且是现场的目录下的图片
											if("scenePicture".equals(imageVo1.getId())){
												List<NodeImageVo> nodeImageVos2=imageVo1.getNodeImageVos();
												if(nodeImageVos2!=null && nodeImageVos2.size()>0){
													for(NodeImageVo imageVo2:nodeImageVos2){
														if(org.apache.commons.lang.StringUtils.isNotBlank(imageVo2.getName())){
															List<LeafVo> leafVos=imageVo2.getLeafVos();
															if(leafVos!=null && leafVos.size()>0){
																for(LeafVo lfVo:leafVos){
																	ImageInfoVo claimPhotosVo=new ImageInfoVo();
																	List<PageVo> pagevos=imageUrlbaseVo.getPagesVo();
																	if(pagevos!=null && pagevos.size()>0){
																		for(PageVo pageVo:pagevos){
																			if(pageVo.getPageId().equals(lfVo.getValues())){
																				claimPhotosVo.setImgurl(pageVo.getPageUrl());
																			}
																		}
																	}

																	List<PageSonNodeVo> pageSonNodeVos = imageUrlbaseVo.getSydVo().getDocVo().getPageInfoVo().getPageSonVos();
																	if(pageSonNodeVos!=null && pageSonNodeVos.size()>0){
																		for(PageSonNodeVo pageSonNodeVo:pageSonNodeVos){
																			if(pageSonNodeVo.getPageid().equals(lfVo.getValues())){
																				claimPhotosVo.setPageSize(pageSonNodeVo.getPage_size());
																				claimPhotosVo.setModifyTime(pageSonNodeVo.getModify_time());
																			}
																		}
																	}
																	claimPhotosVos.add(claimPhotosVo);
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
			//组织返回报文
			if(claimPhotosVos.size() > 0){
				int sum = claimPhotosVos.size();
				if(pituresNo > sum){
					imageResVo.setRetMessage("查询图片序号大于图片总数！");
					throw new IllegalArgumentException("查询图片序号(pituresNo)大于图片总数:"+sum);
				}
				ImageDateVo imageDateVo = new ImageDateVo();
				imageDateVo.setPcturesCount(sum+"");
				//如果图片数量大于出入编号，则将集合根据时间排序
				List<ImageInfoVo> stream = claimPhotosVos.stream().sorted((e1, e2) -> {
					if(e1.getModifyTime().equals(e2.getModifyTime())){
						return e1.getPageSize().compareTo(e2.getPageSize());
					}else{
						return -e1.getModifyTime().compareTo(e2.getModifyTime());
					}
				}).collect(Collectors.toList());
				//取对应编号的图片
				if(stream != null && stream.size() > 0){
					Random ran1 = new Random(10);
					int num1 = ran1.nextInt(100);
					ImageInfoVo imageInfoVo = stream.get(pituresNo-1);
					//获取到图片地址，后下载文件，获取图片大小，并且转换成base64
					String fileName = new Date().getTime()+""+ +num1+".jpg";
					path = req.getSession().getServletContext().getRealPath("/");//获取当前路径
					logger.info("为文件添加下级目录地址为path1: {}", path);
					//为文件添加下级目录地址
					path= path+File.separator+"pictures";
					logger.info("为文件添加下级目录地址为path5: {}", path);
					String imageSize = null;
					String baseString = null;
					String urlString = imageInfoVo.getImgurl();
					//获取文件名，文件名实际上在URL中可以找到
					file = download(urlString, path,fileName);
					if(file.exists()){
						String paths = file.getPath() +File.separator+ fileName;
						logger.info("图片路径为: {}", paths);
						imageSize = getResolution2(paths);
						//图片压缩
						byte[] fileSize = fileConvertToByteArray(paths);
						logger.info("图片大小为: {}", fileSize.length);
						if(fileSize.length/1024 > 50){
							fileSize = compressPicForScale(fileSize,50l,"1");
							//压缩后画出来
							logger.info("图片压缩后大小为: {}", fileSize.length);
							String newImage = new Date().getTime()+""+ran1.nextInt(100)+".jpg";
							getFileByBytes(fileSize,file.getPath(),newImage);
							paths = file.getPath()+File.separator+newImage;
							logger.info("压缩后图片大小为: {}", paths);
							imageSize = getResolution2(paths);
							logger.info("图片大小为: {}", imageSize);
						}
						baseString = getBase64(paths);
                        logger.info("图片转换成base64为: {}", baseString);
					}else{
						System.err.println("指定的目录或者文件不存在！");
					}
					if(StringUtils.isBlank(baseString)){
						throw new IllegalArgumentException("图片转base64失败！");
					}
					if(StringUtils.isBlank(imageSize)){
						throw new IllegalArgumentException("获取图片大小失败！");
					}
					imageDateVo.setPictures(baseString);
					imageDateVo.setPicturesSize(imageSize);
					imageResVo.setRetMessage("查询成功！");
					imageResVo.setData(imageDateVo);
					imageResVo.setRetCode("01");
					imageResVo.setInsurerUuid(imageReqVo.getInsurerUuid());
				}else{
					throw new IllegalArgumentException("返回集合为空");
				}
			}else{
				imageResVo.setRetCode("02");
				imageResVo.setRetMessage("查询结果不存在！");
				imageResVo.setData(null);
			}
        } catch (Exception e) {
			logger.info("图片接口报错:", e);
            imageResVo.setRetCode("02");
            imageResVo.setRetMessage(e.toString());
            imageResVo.setData(null);
        } finally {
        	if(file.exists()){
				deleteFile(file);
			}
            logger.info("图片接口响应报文: {}", JSON.toJSONString(imageResVo));
        }
        PrintWriter out = null;
        try {
            out = resp.getWriter();
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            out.print(JSON.toJSONString(imageResVo));
        } catch (IOException e) {
			logger.info("图片接口报错:", e.toString());
            e.printStackTrace();
        } finally {
        	if(out != null){
				out.close();
			}
        }


    }

    /**
     * 请求报文部分截取替换
	 * 替换前
     *<PAGES>
	 *     <PAGE PAGEID="0495c48639f748c2a347f4910ba1fdfb" ----></PAGE>
	 *     <PAGE PAGEID="0495c48639f748c2a347f4910ba1fdfb" ----></PAGE>
     *</PAGES>
	 *替换后
	 *<PAGES>
	 *    <UAGE PAGEID="0495c48639f748c2a347f4910ba1fdfb" ----></PAGE>
	 *    <UAGE PAGEID="0495c48639f748c2a347f4910ba1fdfb" ----></PAGE>
	 *</PAGES>
	 *  解释：  stra到strc  拿到需要替换后的新的
	 *  strd   从原来报文中 拿到旧的
	 *  endStr  把整个报文用新的替换旧的
     * @param reqXml
     * @return
     */
    private String changeXml(String reqXml) {
        int str1 = reqXml.indexOf("<PAGES>");
        int str2 = reqXml.indexOf("</PAGES>");
        String endStr = "";
        if (str2 > 1) {
            String stra = reqXml.substring(str1 + 7, str2 + 8);
            String strb = stra.replace("<PAGE", "<UAGE");
            String strc = "<PAGES>".concat(strb);
            String strd = reqXml.substring(str1, str2 + 8);
            endStr = reqXml.replace(strd, strc);
        }
        return endStr;
    }

	/**
	 * 文件下载，返回下载的文件File对象,用于删除文件的时候调用，保持下载和删除是同一对象
	 * @param fileUrl
	 * @param path
	 * @return
	 */
	public static File download(String fileUrl, String path, String fileName) {
		File file = new File(path);
		if (!file.exists()) {
			System.out.println("文件是否创建成功"+file.mkdirs());
			logger.info("文件下载路径为: {}",file.getAbsolutePath());
		}
		//这里服务器上要将此图保存的路径
		DataInputStream in = null;
		DataOutputStream out = null;
		HttpURLConnection connection = null;
		InputStream input = null;
		OutputStream output = null;
		try {
			logger.info("为文件添加下级目录地址为fileUrl: {}", fileUrl);
			URL url = new URL(fileUrl);//将网络资源地址传给,即赋值给url
			//此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流
			connection = (HttpURLConnection) url.openConnection();
			input = connection.getInputStream();
			in = new DataInputStream(input);
			//此处也可用BufferedInputStream与BufferedOutputStream
			output = new FileOutputStream(path + File.separator + fileName);
			out = new DataOutputStream(output);
			//将参数savePath，即将截取的图片的存储在本地地址赋值给out输出流所指定的地址
			byte[] buffer = new byte[4096];
			int count = 0;
			//将输入流以字节的形式读取并写入buffer中
			while((count = in.read(buffer)) > 0){
				out.write(buffer,0,count);
			}
			connection.disconnect();
			//返回内容是保存后的完整的URL
			return file;
		} catch (Exception e) {
			logger.info("文件下载报错,地址fileUrl: {}",e);
			return null;
		} finally {
			try {
				if (input != null) {
					input.close();
				}
				if (in != null) {
					in.close();
				}
				if (output != null) {
					output.flush();
					output.close();
				}
				if (out != null) {
					out.flush();
					out.close();//后面三行为关闭输入输出流以及网络资源的固定格式
				}
			} catch (IOException e) {
				logger.info("文件下载报错: {}",e);
			}
		}
	}

	/**
	 * 获取图片大小
	 * @param path
	 * @return
	 */
	public static String getResolution2(String path) {
		String size = "";
		FileInputStream in = null;
		try {
			in = new FileInputStream(path);
			BufferedImage imagebuffer = ImageIO.read(in);
			int width = imagebuffer.getWidth();
			int height = imagebuffer.getHeight();
			size = width+"*"+height;
		} catch (Exception e) {
			logger.info("获取图片大小失败: {}",e);
		}finally {
			try {
				if(in != null){
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return size;
	}

	//删除单个文件或空的文件夹
	public static boolean deleteFile(File file) {
		//如果文件路径对应的文件存在，并且是一个文件，则直接删除
		if(file.isDirectory()){
			deleteDirectory(file);
		}else{
			if (file.delete()) {
				System.err.println("文件" + file.getName() + "删除成功！");
				return true;
			} else {
				System.err.println("文件" + file.getName() + "删除失败！");
				return false;
			}
		}
		return true;
	}

	//删除文件夹及里面的文件
	public static boolean deleteDirectory(File dirFile) {
		//如果dir对应的问件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			System.err.println("文件夹" + dirFile.getPath() + "不存在！");
			return false;
		}
		boolean flag = true;
		//删除问价夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i]);
				if (!flag) {
					break;
				}
			} else {
				deleteDirectory(files[i]);
			}
		}
		//删除当前目录
		if (dirFile.delete()) {
			System.err.println("目录" + dirFile.getPath() + "删除成功！");
			return true;
		} else {
			return false;
		}
	}

	public static String getBase64(String path) {
		String imgStr = "";
		FileInputStream fis = null;
		File file = null;
		try {
			file = new File(path);
			fis = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			int offset = 0;
			int numRead = 0;
			while (offset < buffer.length && (numRead = fis.read(buffer, offset, buffer.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset != buffer.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}
			BASE64Encoder encoder = new BASE64Encoder();
			imgStr = encoder.encode(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			//将file对象释放，不让其继续占有资源，影响后续文件删不掉
			file = null;
			try {
				if(fis != null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "data:image/jpeg;base64," + imgStr;
	}

	/**
	 * 根据指定大小压缩图片
	 *
	 * @param imageBytes  源图片字节数组
	 * @param desFileSize 指定图片大小，单位kb
	 * @param imageId     影像编号
	 * @return 压缩质量后的图片字节数组
	 */
	public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize, String imageId) throws Exception {
		if (imageBytes == null || imageBytes.length <= 0 || imageBytes.length < desFileSize * 1024) {
			return imageBytes;
		}
		long srcSize = imageBytes.length;
		double accuracy = getAccuracy(srcSize / 1024);
		ByteArrayInputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;
		try {
			while (imageBytes.length > desFileSize * 1024) {
				inputStream = new ByteArrayInputStream(imageBytes);
				outputStream = new ByteArrayOutputStream(imageBytes.length);
				Thumbnails.of(inputStream).scale(accuracy).outputQuality(accuracy).toOutputStream(outputStream);
				imageBytes = outputStream.toByteArray();
			}
		} catch (Exception e) {
			logger.error("【图片压缩】msg=图片压缩失败!", e);
		}finally {
			try {
				if(inputStream != null){
					inputStream.close();
				}
				if(outputStream != null){
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imageBytes;
	}

	/**
	 * 自动调节精度(经验数值)
	 *
	 * @param size 源图片大小
	 * @return 图片压缩质量比
	 */
	private static double getAccuracy(long size) {
		double accuracy;
		if (size < 900) {
			accuracy = 0.85;
		} else if (size < 2047) {
			accuracy = 0.6;
		} else if (size < 3275) {
			accuracy = 0.44;
		} else {
			accuracy = 0.4;
		}
		return accuracy;
	}

	/**
	 * 把一个文件转化为byte字节数组。
	 *
	 * @return
	 */
	private static byte[] fileConvertToByteArray(String path) {
		byte[] data = null;
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try {
			File file = new File(path);
			fis = new FileInputStream(file);
			baos = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[1024];
			while ((len = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			data = baos.toByteArray();
			file = null;
		} catch (Exception e) {
			logger.info("文件转化为byte字节数组错误: {}", e);
		}finally {
			try {
				if(fis != null){
					fis.close();
				}
				if(baos != null){
					baos.close();
				}
			} catch (IOException e) {
				logger.info("关闭流错误: {}", e);
			}
		}

		return data;
	}

	/**
	 * 把byte字节数组转化为一个文件。
	 *
	 * @return
	 */
	public static void getFileByBytes(byte[] bytes, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists()) {// 判断文件目录是否存在
				dir.mkdirs();
			}

			fos = new FileOutputStream(dir.getAbsolutePath()+File.separator+fileName);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
		} catch (Exception e) {
			logger.info("文件转化为byte字节数组错误: {}", e);
		} finally {
			if (bos != null) {
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					logger.info("关闭流错误: {}", e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.info("关闭流错误: {}", e);
				}
			}
		}
	}

}
