package ins.sino.claimcar.certifyupload.service;

import java.util.ArrayList;
import java.util.List;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.certifyupload.po.ImageFileIndex;
import ins.sino.claimcar.certifyupload.vo.ImageFileIndexVo;

import org.apache.axis.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author dengkk
 * @CreateTime Mar 16, 2016
 */
@Service("certifyUploadService")
public class CertifyUploadService {

	@Autowired
	private DatabaseDao databaseDao;
	
	/**
	 * 查找已经上传的资料
	 * @param registNo
	 * @return
	 */
	public List<ImageFileIndexVo> findImageFileIndexVoList(String registNo){
		List<ImageFileIndexVo> imageFileIndexVoList = new ArrayList<ImageFileIndexVo>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("bussNo",registNo);
		List<ImageFileIndex> imageFileIndexList = databaseDao.findAll(ImageFileIndex.class,queryRule);
		if(imageFileIndexList != null && imageFileIndexList.size() > 0){
			imageFileIndexVoList = Beans.copyDepth().from(imageFileIndexList).toList(ImageFileIndexVo.class);
		}
		return imageFileIndexVoList;
	}
	
	
	public List<ImageFileIndexVo> findImageFileByTypePath(String registNo,String typePath){
		List<ImageFileIndexVo> imageFileIndexVoList = new ArrayList<ImageFileIndexVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("bussNo",registNo);
		qr.addEqual("typePath",typePath);
		List<ImageFileIndex> imageFileIndexList = databaseDao.findAll(ImageFileIndex.class,qr);
		if(imageFileIndexList != null && imageFileIndexList.size() > 0){
			imageFileIndexVoList = Beans.copyDepth().from(imageFileIndexList).toList(ImageFileIndexVo.class);
		}
		return imageFileIndexVoList;
	}
	
	public List<ImageFileIndexVo> findImageFileByUploadNode(String uploadNode,String registNo){
		List<ImageFileIndexVo> imageFileIndexVoList = new ArrayList<ImageFileIndexVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("bussNo",registNo);
		qr.addEqual("uploadNode",uploadNode);
		List<ImageFileIndex> imageFileIndexList = databaseDao.findAll(ImageFileIndex.class,qr);
		if(imageFileIndexList != null && imageFileIndexList.size() > 0){
			imageFileIndexVoList = Beans.copyDepth().from(imageFileIndexList).toList(ImageFileIndexVo.class);
		}
		return imageFileIndexVoList;
	}
	
	public List<ImageFileIndexVo> findImageFileBytypePaths(String registNo,String...typePaths){
		List<ImageFileIndexVo> imageFileIndexVoList = new ArrayList<ImageFileIndexVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("bussNo",registNo);
		for(int i=0;i<typePaths.length;i++){
			String typePath = typePaths[i];
			if(!StringUtils.isEmpty(typePath)){
				qr.addEqual("typePath"+(i+1),typePath);
			}
		}
		List<ImageFileIndex> imageFileIndexList = databaseDao.findAll(ImageFileIndex.class,qr);
		if(imageFileIndexList != null && imageFileIndexList.size() > 0){
			imageFileIndexVoList = Beans.copyDepth().from(imageFileIndexList).toList(ImageFileIndexVo.class);
		}
		return imageFileIndexVoList;
	}
	
	public List<ImageFileIndexVo> findImageFileByTypeName(String registNo,String typeName){
		List<ImageFileIndexVo> imageFileIndexVoList = new ArrayList<ImageFileIndexVo>();
		QueryRule qr = QueryRule.getInstance();
		qr.addEqual("bussNo",registNo);
		qr.addLike("typeName", typeName);
		List<ImageFileIndex> imageFileIndexList = databaseDao.findAll(ImageFileIndex.class,qr);
		if(imageFileIndexList != null && imageFileIndexList.size() > 0){
			imageFileIndexVoList = Beans.copyDepth().from(imageFileIndexList).toList(ImageFileIndexVo.class);
		}
		return imageFileIndexVoList;
	}
	
}
