package ins.sino.claimcar.certify.service;

import ins.sino.claimcar.certifyupload.service.CertifyUploadService;
import ins.sino.claimcar.certifyupload.vo.ImageFileIndexVo;

import java.util.List;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 
 * @author dengkk
 *
 */
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path("certifyHandleService")
public class CertifyHandleServiceImpl implements CertifyHandleService {

	@Autowired
	private CertifyUploadService certifyUploadService;

	@Override
	public List<ImageFileIndexVo> findImageFileIndexVoList(String bussNo) {
		List<ImageFileIndexVo> imageFileIndexVoList = certifyUploadService
				.findImageFileIndexVoList(bussNo);
		return imageFileIndexVoList;
	}
	
	@Override
	public List<ImageFileIndexVo> findImageFileByTypePath(String registNo,String typePath){
		List<ImageFileIndexVo> imageFileIndexVoList = certifyUploadService.findImageFileByTypePath(registNo, typePath);
		return imageFileIndexVoList;
	}
	
	@Override
	public List<ImageFileIndexVo> findImageFileByUploadNode(String uploadNode,String registNo){
		List<ImageFileIndexVo> imageFileIndexVoList = certifyUploadService.findImageFileByUploadNode(uploadNode, registNo);
		return imageFileIndexVoList;
	}
	
	@Override
	public List<ImageFileIndexVo> findImageFileBytypePaths(String registNo,String...typePaths){
		List<ImageFileIndexVo> imageFileIndexVoList = certifyUploadService.findImageFileBytypePaths(registNo, typePaths);
		return imageFileIndexVoList;
	}
	
	@Override
	public List<ImageFileIndexVo> findImageFileByTypeName(String registNo,String typeName){
		List<ImageFileIndexVo> imageFileIndexVoList = certifyUploadService.findImageFileByTypeName(registNo, typeName);
		return imageFileIndexVoList;
	}
}
