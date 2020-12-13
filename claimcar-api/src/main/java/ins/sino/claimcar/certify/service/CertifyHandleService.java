package ins.sino.claimcar.certify.service;

import ins.sino.claimcar.certifyupload.vo.ImageFileIndexVo;

import java.util.List;

public interface CertifyHandleService {

	/**
	 * 通过业务号 查找已上传资料信息
	 * @param bussNo
	 * @return
	 */
	public abstract List<ImageFileIndexVo> findImageFileIndexVoList(String bussNo);
	
	/**
	 * 通过报案号和照片路径查询
	 * @param registNo
	 * @param typePath
	 * @return
	 */
	public List<ImageFileIndexVo> findImageFileByTypePath(String registNo,String typePath);

	/**
	 * 根据上传节点查询
	 * @param uploadNode
	 * @param registNo
	 * @return
	 */
	public List<ImageFileIndexVo> findImageFileByUploadNode(String uploadNode,String registNo);
	
	/**
	 * 根据报案号和子路径查询
	 * @param registNo
	 * @param typePaths
	 * @return
	 */
	public List<ImageFileIndexVo> findImageFileBytypePaths(String registNo,String...typePaths);
	
	/**
	 * 根据报案号和typeName模糊查询
	 * @param registNo
	 * @param typeName
	 * @return
	 */
	public List<ImageFileIndexVo> findImageFileByTypeName(String registNo,String typeName);
}