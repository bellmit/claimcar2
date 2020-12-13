/******************************************************************************
* Copyright 2010-2011 the original author or authors.
* CREATETIME : 2011-10-9 ����09:23:34
******************************************************************************/
package ins.sino.claimcar;

import java.util.ArrayList;
import java.util.List;


/**
 * Ӱ���������ڵ�
 * @Copyright Copyright (c) 2011
 * @Company www.sinosoft.com.cn
 * @author ��<a href="mailto:liuping-gz@sinosoft.com.cn">LiuPing</a> 
 * @since  2011-10-9 ����09:23:34
 */
public class TypeNode {
	
	private List<TypeNode> childNodes;
	private String typeCode;
	private String typeName;
	private String falg;
	private boolean isLast=true;//�Ƿ�Ϊĩ�ڵ�
	
	
	/**
	 * @param typeCode ���ʹ��� ������ʹ�ü��š�-��
	 * @param typeName ��������
	 */
	public TypeNode(String typeCode,String typeName){
		super();
		if(typeCode.indexOf("-")>-1) throw new IllegalStateException("���ʹ��벻����ʹ�ü��š�-����"+typeCode);
		this.typeCode = typeCode;
		this.typeName = typeName;
	}
	
	/**
	 * @param typeCode ���ʹ��� ������ʹ�ü��š�-��
	 * @param typeName ��������
	 * @param falg ������־�����ã�
	 */
	public TypeNode(String typeCode,String typeName,String falg){
		super();
		if(typeCode.indexOf("-")>-1) throw new IllegalStateException("���ʹ��벻����ʹ�ü��š�-����"+typeCode);
		this.typeCode = typeCode;
		this.typeName = typeName;
		this.falg = falg;
	}

	/**
	 * ��ӽڵ�
	 * @param node
	 * @modified:
	 * ��LiuPing(2011-10-9 ����10:18:23): <br>
	 */
	public void addNode(TypeNode node){
		 if(childNodes==null)childNodes=new ArrayList<TypeNode>();
		 childNodes.add(node);
		 this.isLast=false;
	}

	
	/**
	 * @return ���� childNodes��
	 */
	public List<TypeNode> getChildNodes() {
		return childNodes;
	}
	
	/**
	 * @param childNodes Ҫ���õ� childNodes��
	 */
	public void setChildNodes(List<TypeNode> childNodes) {
		this.childNodes = childNodes;
		if(childNodes!=null&&childNodes.size()>0){
			 this.isLast=false;
		}else{
			this.isLast=true;
		}
	}
	
	/**
	 * @return ���� typeCode��
	 */
	public String getTypeCode() {
		return typeCode;
	}
	
	/**
	 * @param typeCode Ҫ���õ� typeCode��
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	/**
	 * @return ���� typeName��
	 */
	public String getTypeName() {
		return typeName;
	}
	
	/**
	 * @param typeName Ҫ���õ� typeName��
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	/**
	 * @return ���� falg��
	 */
	public String getFalg() {
		return falg;
	}
	
	/**
	 * @param falg Ҫ���õ� falg��
	 */
	public void setFalg(String falg) {
		this.falg = falg;
	}
	
	/**
	 * @return ���� isLast��
	 */
	public boolean isLast() {
		return isLast;
	}

}
