/******************************************************************************
 * Copyright 2010-2011 the original author or authors.
 * CREATETIME : 2011-3-18 上午11:41:08
 ******************************************************************************/
package ins.platform.common.web.taglib;

import ins.platform.common.util.AreaSelectUtil;
import ins.platform.vo.PrpDHospitalVo;
import ins.platform.vo.PrpdAppraisaVo;
import ins.platform.vo.PrpdoldprovincecityAreaVo;
import ins.platform.vo.SysAreaDictVo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

/**
 * 地区代码选择标签 本标签许关联 common.js（changeArea(obj,targetElmId,level)）、AreaSelectAction
 * @Copyright Copyright (c) 2011
 * @Company www.sinosoft.com.cn
 * @author LiuPing
 * @since 2011-3-18 上午11:41:08
 */
public class AreaSelectTag extends SimpleTagSupport {

	// private static Logger log = Logger.getLogger(AreaSelectTag.class);

	private String targetElmId;// 将选中的value放到哪个input里面
	private Integer showLevel = 3;// 要显示几个等级的地区，默认是3个

	private String areaCode = null;// 默认地区编码
	private String comCode = null;// 默认机构代码
	private String postCode = null;// 默认邮编
	private String isHospital = null;//是否是医疗机构
	private String hospitalCode = null;//默认医院
    private String isAppraisa=null;//是否是伤残鉴定机构
    private String appraisaCode=null;//默认伤残鉴定机构
    private String isAreaCode=null;//是否是收款人账户维护银行地区
    private String handlerStatus=null;//页面处理状态
	

	private String clazz = "";// html class 属性
	private String style = "";// html style 属性
	private boolean disabled = false;// html disabled 属性


	@Override
	public void doTag() {
		PageContext pageContext = (PageContext) this.getJspContext();
		JspWriter out = pageContext.getOut();
		String[] areaPath = new String[]{null};
		AreaSelectUtil areaUtil = new AreaSelectUtil();
		StringBuffer selectHtml = new StringBuffer();
     
		// 先处理要显示的默认值，找到需要显示的 [省,市,区]
		if(StringUtils.isNotBlank(areaCode)){
			if("Y".equals(isAreaCode)){
				areaPath=areaUtil.findAreaByOldAreaCode(areaCode);
			}else{
				areaPath = areaUtil.findAreaByAreaCode(areaCode,handlerStatus);
			}
			
		}else if(StringUtils.isNotBlank(comCode)){
			areaPath = areaUtil.findAreaByComCode(comCode,handlerStatus);
		}else if(StringUtils.isNotBlank(postCode)){
			areaPath = areaUtil.findAreaByPostCode(postCode,handlerStatus);
		}
     
		// 得到第一级 省份数据
		List<SysAreaDictVo> areaLv1List=new ArrayList<SysAreaDictVo>();
		List<PrpdoldprovincecityAreaVo> oldAreaLv1List=new ArrayList<PrpdoldprovincecityAreaVo>();
		if("Y".equals(isAreaCode)){
			oldAreaLv1List=areaUtil.findChildOldArea(null);
			selectHtml.append(bulidOldSelectHtml(oldAreaLv1List,1,areaPath[0]));
		}else{
			 areaLv1List = areaUtil.findChildArea(null,handlerStatus);// 第一级地区 省 是必须的
			 if(areaPath!=null && areaPath.length>0){
				 selectHtml.append(bulidSelectHtml(areaLv1List,1,areaPath[0])); 
			 }else{
				 selectHtml.append(bulidSelectHtml(areaLv1List,1,""));
			 }
			 
		}
		
		
		
		// 第二级
		List<SysAreaDictVo> areaLv2List = new ArrayList<SysAreaDictVo>();// 第二级地区
		List<PrpdoldprovincecityAreaVo> oldAreaLv2List=new ArrayList<PrpdoldprovincecityAreaVo>();
		String upperCode = null;
		String defValue = "";

		if(showLevel>=2){// 要显示两级以上
			if("Y".equals(isAreaCode)){
				if(areaPath!=null && areaPath.length>=2){// 已经确定了省,找到这个省下面的市
					upperCode = areaPath[0];
					oldAreaLv2List = areaUtil.findChildOldArea(upperCode);
					defValue = areaPath[1];
				}
		        selectHtml.append(bulidOldSelectHtml(oldAreaLv2List,2,defValue));
			}else{
				if( areaPath!=null && areaPath.length>=2){// 已经确定了省,找到这个省下面的市
					upperCode = areaPath[0];
					areaLv2List = areaUtil.findChildArea(upperCode,handlerStatus);
					defValue = areaPath[1];
				}
				
			   selectHtml.append(bulidSelectHtml(areaLv2List,2,defValue));
			}
			
		}

		// 第三级
		List<SysAreaDictVo> areaLv3List = new ArrayList<SysAreaDictVo>();// 第三级地区
		List<PrpDHospitalVo> hospitalList = new  ArrayList<PrpDHospitalVo>();// 医院
		List<PrpdAppraisaVo> appraisaList = new  ArrayList<PrpdAppraisaVo>();// 伤残鉴定机构
		if(showLevel>=3 && isHospital==null && isAppraisa==null){// 要显示3级
			defValue = "";
			if( areaPath!=null && (areaPath.length>=3 || (showLevel ==3 && areaPath.length>=2))){// 已经确定了市，,找到这个市下面的区县
				upperCode = areaPath[1];
				areaLv3List = areaUtil.findChildArea(upperCode,handlerStatus);
				if(areaPath.length>=3){
					defValue = areaPath[2];
				}
			}
			selectHtml.append(bulidSelectHtml(areaLv3List,3,defValue));
		}else if(showLevel>=3 && isHospital!=null){
			if( areaPath!=null && areaPath.length>=2){// 已经确定了省市
				upperCode = areaPath[1];
				hospitalList = areaUtil.findChildHospital(upperCode);
			}
			selectHtml.append(bulidHospitalSelectHtml(hospitalList,3));
		}else if(showLevel>=3 && isAppraisa!=null){
			if( areaPath!=null && areaPath.length>=2){// 已经确定了省市
				upperCode = areaPath[1];
				appraisaList = areaUtil.findChildAppraisa(upperCode);
			}
			selectHtml.append(bulidAppraisaSelectHtml(appraisaList,3));
		}

		try{
			out.write(selectHtml.toString());
		}catch(IOException e){
			e.printStackTrace();
		}

	}

	
	private StringBuffer bulidSelectHtml(List<SysAreaDictVo> areaList,int level,String defValue) {
		StringBuffer sb = new StringBuffer();

		String other = " class=' areaselect "+clazz+"' "+getStyle()+getDisabled()+getIsHospital();

		sb.append("<select   id='"+targetElmId+"_lv"+level+"' onchange=\"changeArea(this,'"+targetElmId+"',"+level+",'"+handlerStatus+"') \" "+other+" >");

		if(clazz==null||( !clazz.contains("required")&& !clazz.contains("must") )){
			sb.append("<option value='' ></option>");
		}
		String status="0";//defValue 是否在areaList匹配到值0未匹配到，1匹配到
		for(SysAreaDictVo dictVo:areaList){
			
			sb.append("<option value='"+dictVo.getAreaCode()+"' ");
			if(dictVo.getAreaCode().equals(defValue)){
				sb.append(" selected='selected' ");
				status="1";
			}
			
			sb.append(" title='"+dictVo.getAreaName()+"'");
			
			sb.append(" >");
			sb.append(dictVo.getAreaName());
			sb.append("</option>");
		}
        if(!"0".equals(handlerStatus) && !"1".equals(handlerStatus) && !"2".equals(handlerStatus) && "0".equals(status)){
        	AreaSelectUtil areaSelectUtil = new AreaSelectUtil();
        	List<SysAreaDictVo> sysAreaDictVos=areaSelectUtil.findAreaCode(defValue);
        	if(sysAreaDictVos!=null && sysAreaDictVos.size()>0){
        		SysAreaDictVo dictVo=sysAreaDictVos.get(0);
        		sb.append("<option value='"+dictVo.getAreaCode()+"' ");
    			sb.append(" selected='selected' ");
    		    sb.append(" title='"+dictVo.getAreaName()+"'");
    			sb.append(" >");
    			sb.append(dictVo.getAreaName());
    			sb.append("</option>");
        		
        	}
        	
        }
		sb.append("</select>");

		return sb;
	}
	/**
	 * 收款人账户信息页面银行账户地区
	 * @param areaList
	 * @param level
	 * @param defValue
	 * @return
	 */
	private StringBuffer bulidOldSelectHtml(List<PrpdoldprovincecityAreaVo> areaList,int level,String defValue) {
		StringBuffer sb = new StringBuffer();

		String other = " class=' areaselect "+clazz+"' "+getStyle()+getDisabled()+getIsHospital();

		sb.append("<select   id='"+targetElmId+"_lv"+level+"' onchange=\"changeArea(this,'"+targetElmId+"',"+level+") \" "+other+" >");

		if(clazz==null||( !clazz.contains("required")&& !clazz.contains("must") )){
			sb.append("<option value='' ></option>");
		}

		for(PrpdoldprovincecityAreaVo dictVo:areaList){
			sb.append("<option value='"+dictVo.getCode()+"' ");
			if(dictVo.getCode().equals(defValue)){
				sb.append(" selected='selected' ");
			}
			
			sb.append(" title='"+dictVo.getName()+"'");
			
			sb.append(" >");
			sb.append(dictVo.getName());
			sb.append("</option>");
		}

		sb.append("</select>");

		return sb;
	}
	
	private StringBuffer bulidHospitalSelectHtml(List<PrpDHospitalVo> hospitalList,int level) {
		StringBuffer sb = new StringBuffer();

		String other = " class=' areaselect "+clazz+"' "+getStyle()+getDisabled()+getIsHospital();

		sb.append("<select   id='"+targetElmId+"_hospital' onchange=\"changeHospital(this,'"+targetElmId+"') \" "+other+" >");

		if(clazz==null||( !clazz.contains("required")&& !clazz.contains("must") )){
			sb.append("<option value='' ></option>");
		}

		for(PrpDHospitalVo dictVo:hospitalList){
			sb.append("<option value='"+dictVo.getHospitalCode()+"' ");
			if(dictVo.getHospitalCode().equals(hospitalCode)){
				sb.append(" selected='selected' ");
			}
			sb.append(" >");
			sb.append(dictVo.getHospitalCName());
			sb.append("</option>");
		}

		sb.append("</select>");

		return sb;
	}
	
   //伤残鉴定机构PrpdAppraisaVo
	private StringBuffer bulidAppraisaSelectHtml(List<PrpdAppraisaVo> appraisaList,int level) {
		StringBuffer sb = new StringBuffer();

		String other = " class=' areaselect "+clazz+"' "+getStyle()+getDisabled()+getIsHospital();

		sb.append("<select   id='"+targetElmId+"_appraisa' onchange=\"changeAppraisa(this,'"+targetElmId+"') \" "+other+" >");

		if(clazz==null||( !clazz.contains("required")&& !clazz.contains("must") )){
			sb.append("<option value='' ></option>");
		}

		for(PrpdAppraisaVo dictVo:appraisaList){
			sb.append("<option value='"+dictVo.getAppraisaCode()+"' ");
			if(dictVo.getAppraisaCode().equals(appraisaCode)){
				sb.append(" selected='selected' ");
			}
			sb.append(" >");
			sb.append(dictVo.getAppraisaName());
			sb.append("</option>");
		}

		sb.append("</select>");

		return sb;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	
	
	public String getHospitalCode() {
		return hospitalCode;
	}


	
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}


	public String getIsHospital() {
		
		if(!StringUtils.isBlank(isHospital)){
			return " isHospital='"+isHospital+"' ";
		}else if(!StringUtils.isBlank(isAppraisa)){
			return " isAppraisa='"+isAppraisa+"' ";
		}else if(!StringUtils.isBlank(isAreaCode)){
			return " isAreaCode='"+isAreaCode+"' ";
		}else{
			return "";
		}
	}
	
	public void setIsHospital(String isHospital) {
		this.isHospital = isHospital;
	}

	public void setIsAppraisa(String isAppraisa) {
		this.isAppraisa = isAppraisa;
	}


	public String getAppraisaCode() {
		return appraisaCode;
	}


	public void setAppraisaCode(String appraisaCode) {
		this.appraisaCode = appraisaCode;
	}

	public void setTargetElmId(String targetElmId) {
		this.targetElmId = targetElmId;
	}

	private String getDisabled() {
		if(disabled==true){
			return " disabled ";
		}else{
			return "";
		}
	}

	public String getStyle() {
		if(StringUtils.isBlank(style)){
			return " style='vertical-align:middle' ";
		}else{
			return " style='vertical-align:middle;"+style+";' ";
		}
	}


	public void setStyle(String style) {
		this.style = style;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public void setShowLevel(Integer showLevel) {
		this.showLevel = showLevel;
	}
    public void setIsAreaCode(String isAreaCode) {
		this.isAreaCode = isAreaCode;
	}


	public String getHandlerStatus() {
		return handlerStatus;
	}


	public void setHandlerStatus(String handlerStatus) {
		this.handlerStatus = handlerStatus;
	}

    
}
