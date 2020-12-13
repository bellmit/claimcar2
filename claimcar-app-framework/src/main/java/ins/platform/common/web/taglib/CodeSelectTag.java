/******************************************************************************
 * Copyright 2010-2011 the original author or authors.
 * CREATETIME : 2011-3-18 上午11:41:08
 ******************************************************************************/
package ins.platform.common.web.taglib;

import ins.framework.exception.DataVerifyException;
import ins.framework.lang.Springs;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.vo.SysCodeDictVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 代码下拉选择<br>
 * 翻译结果返回为option的选项
 * 
 * @Copyright Copyright (c) 2011
 * @Company www.sinosoft.com.cn
 * @author ANDI
 * @since 2011-3-18 上午11:41:08
 */
public class CodeSelectTag extends SimpleTagSupport {

	private static Logger log = Logger.getLogger(CodeSelectTag.class);

	private static final long serialVersionUID = -8204448901846380293L;
	private String type = null;// 类型 radio checkbox select
	private String codeType = null;// 代码类型
	private String upperCode = null;// 上级代码
	private String comCode = null;// 机构代码
	private String riskCode = null;// 险种代码(可传多个，以","隔开)
	private String clazz = "";// html class 属性
	private String lableType = null;// select框显示格式，name code-name
	private String value = null;// 默认值
	Map<String, String> valueMap = null;
	private String nullToVal = null;// 默认值为null时，使用那个作为默认值
	private String split = null;
	private String originValue = null;
	private Object dataSource = null;
	private String exceptValue="";
	private boolean dictFlag = true;//默认是否数据字典值
	private String datatype="";//校验类型
	// html部分
	private String id = ""; // html id 属性
	private String name = "";// html name属性
	private String onclick = "";// 事件
	private String onchange = "";// 事件
	private String style = "";// html style 属性
	private boolean disabled = false;// html disabled 属性
	private boolean readonly = false;// html readonly 属性
	private boolean title=false;

	@Override
	public void doTag() {

		PageContext pageContext = (PageContext) this.getJspContext();
		JspWriter out = pageContext.getOut();
		Map<String, SysCodeDictVo> typeMap = null;
		
		if(dataSource!=null){
			typeMap=getMapFromDataSource(dataSource,codeType);
		}else{
			if(comCode !=null || riskCode != null){
				typeMap = CodeTranUtil.findCodeMapByRiskCom(codeType, riskCode, comCode);
			}else{
				CodeTranService codeTranService = (CodeTranService) Springs.getBean("codeTranService");
				if ("Disasterone".equals(codeType)) {// 巨灾一级代码
					typeMap = codeTranService.findLevelOneDisasterInfo();
				} else if ("Disastertwo".equals(codeType)) { // 巨灾二级代码

				} else if ("BankCode".equals(codeType)){
					typeMap = codeTranService.findAccBankCode();
				}else {
					typeMap = CodeTranUtil.findCodeDictTransMap(codeType,upperCode);
				}
			}
		}
		
		if (StringUtils.isBlank(value) && !StringUtils.isBlank(nullToVal)) {
			value = nullToVal;
		}
		if(StringUtils.isBlank(originValue)){
			originValue = "";
		}
		
		StringBuffer sb = new StringBuffer();
		if ("radio".equals(type)) {
			generateRadioHtml(sb, typeMap);
		} else if ("checkbox".equals(type)) {
			generateCheckBoxHtml(sb, typeMap);
		} else if ("select".equals(type)) {
			generateSelectHtml(sb, typeMap);
		}
//		System.out.println(sb.toString());
		try {
			out.write(sb.toString());
		} catch (Exception e) {
			log.error("标签解析错误 " + codeType, e);
			e.printStackTrace();
			throw new DataVerifyException();
		}
	}

	private Map<String,SysCodeDictVo> getMapFromDataSource(Object dataSource,String codeType) {
		Map<String, SysCodeDictVo> typeMap = new LinkedHashMap<String,SysCodeDictVo>();
		if(dataSource instanceof List){
			List<Object> dataSourceList=(List<Object> )dataSource;
			for(Object obj:dataSourceList){
				if(obj instanceof SysCodeDictVo){
					SysCodeDictVo dictVo=(SysCodeDictVo) obj;
					typeMap.put(dictVo.getCodeCode(),dictVo);
				}else{
					throw new IllegalStateException("List中数据必须为 SysCodeDictVo");
				}
			}
		}else if(dataSource instanceof Map){
			Map<String,Object> dataSourceMap=(Map<String,Object>)dataSource;
			for(String dataKey:dataSourceMap.keySet()){
				Object dataObj=dataSourceMap.get(dataKey);
				if(dataObj instanceof String){
					SysCodeDictVo dictVo=new SysCodeDictVo();
					dictVo.setCodeCode(dataKey);
					dictVo.setCodeType(codeType);
					dictVo.setCodeName((String)dataObj);
					typeMap.put(dataKey,dictVo);
				}else if(dataObj instanceof SysCodeDictVo){
					SysCodeDictVo dictVo=(SysCodeDictVo) dataObj;
					typeMap.put(dictVo.getCodeCode(),dictVo);
				}else{
					throw new IllegalStateException(
							"Map中数据必须为 SysCodeDictVo或String");
				}
						
			}
			
		}
		
		return typeMap;
	}

	private void generateSelectHtml(StringBuffer sb,Map<String,SysCodeDictVo> typeMap) {
		if(typeMap==null||typeMap.size()<1) return;
		if(typeMap.size()>20){
			clazz += " select2 ";// 如果列表数据过多，使用select2插件显示
		}else{
			clazz += " select";
		}
		String other = " "+getOnchange()+" "+getOnclick()+" class='  "+clazz+"' "+getStyle()+getDisabled()+" originValue = '"+originValue+"'";

		sb.append("<select   id='"+id+"' name='"+name+"' "+other+" >");

		if(clazz==null||"".equals(clazz)||( !clazz.contains("required")&& !clazz.contains("must") )){
			sb.append("<option value='' ></option>");
		}
		
		List<String> exceptList = new ArrayList<String>();
		if(StringUtils.isNotBlank(exceptValue)){
			if(exceptValue.indexOf(",")==-1){
				exceptList.add(exceptValue);
			}else{
				String[] exceptStr = exceptValue.split(",");
				for(String val : exceptStr){
					exceptList.add(val);
				}
			}
		}
		for(String key:typeMap.keySet()){
			SysCodeDictVo codeDictVo = typeMap.get(key);
			if(exceptList!=null && !exceptList.isEmpty()){
				if(exceptList.contains(codeDictVo.getCodeCode())){
					continue;
				}
			}
			String titleValue ="";
			if(title){
				if("code-name".equals(lableType)){
					titleValue = codeDictVo.getCodeCode()+"-"+codeDictVo.getCodeName();
				}else if("code".equals(lableType)){
					titleValue = codeDictVo.getCodeCode();
				}else{
					titleValue = codeDictVo.getCodeName();
				}
			}
			if(codeDictVo.getCodeCode().equals(value)){
				sb.append("<option selected='selected' value='"+codeDictVo.getCodeCode()+"'");
				if(title){
					sb.append("title='"+titleValue+"'");
				}
				sb.append(">");
			}else{
				sb.append("<option value='"+codeDictVo.getCodeCode()+"'");
				if(title){
					sb.append("title='"+titleValue+"'");
				}
				sb.append(">");
			}
			if("code-name".equals(lableType)){
				sb.append(codeDictVo.getCodeCode()+"-"+codeDictVo.getCodeName());
			}else if("code".equals(lableType)){
				sb.append(codeDictVo.getCodeCode());
			}else{
				sb.append(codeDictVo.getCodeName());
			}
			sb.append("</option>");
		}
		sb.append("</select>");
	}

	private void generateCheckBoxHtml(StringBuffer sb,
			Map<String,SysCodeDictVo> typeMap) {
		if (typeMap == null || typeMap.size() < 1)
			return;
		// sb.append("<div id='" + id + "' >");
		int index = 0;
		for (String key : typeMap.keySet()) {
			
			SysCodeDictVo codeDictVo = typeMap.get(key);

			
			sb.append("<label class='check-box f1' "+getStyle()+">");
			
			String other = " " + getOnchange() + " " + getOnclick()+" "+getReadonly()+" "+getDisabled();
			if (valueMap.containsKey(codeDictVo.getCodeCode())) {
				other = other + " checked='checked' ";
				valueMap.remove(codeDictVo.getCodeCode());
			}
			
			sb.append("<input type='checkbox' " + other + " name='" + name
					+ "' value='" + codeDictVo.getCodeCode()
					+ "' class='" + clazz + "' originValue = '"+ originValue +"'");
			
			if(index ==0 && StringUtils.isNotBlank(datatype)){
				sb.append(" datatype='"+getDatatype()+"'");
			}		
			sb.append("  />");
			if ("code-name".equals(lableType)) {
				sb.append("<span>" + codeDictVo.getCodeCode()+ 
						"-" + codeDictVo.getCodeName() + "</span>");
			} else {
				sb.append("<span>" + codeDictVo.getCodeName() + "</span>");
			}
			
			sb.append("</label>");
			index++;
		}
		
		if(dictFlag){
			for (String defValue : valueMap.keySet()) {
				sb.append("<label class='check-box f1' " + getStyle() + ">");
	
				String other = " " + getOnchange() + " " + getOnclick() + " "
						+ getReadonly() + " " + getDisabled();
	
				other = other + " checked='checked' ";
	
				sb.append("<input type='checkbox' " + other + " name='" + name
						+ "' value='" + defValue + "' class='" + clazz
						+ "' originValue = '" + originValue + "'  />");
	
				sb.append(defValue);
	
				sb.append("</label>");
			}
		}
	}

	private void generateRadioHtml(StringBuffer sb,
			Map<String,SysCodeDictVo> typeMap) {

		if (typeMap == null || typeMap.size() < 1)
			return;
		// sb.append("<div id='" + id + "' >");
		for (String key : typeMap.keySet()) {
			SysCodeDictVo codeDictVo = typeMap.get(key);
			sb.append("<label class='radio-box'  "+getStyle()+">");
			String other = " " + getOnchange() + " " + getOnclick()+" "+getReadonly()+" "+getDisabled();
			if(codeDictVo.getCodeCode().equals(value)){
				other = other + " checked='checked' ";
			}

			sb.append("<input type='radio' " + other + " name='" + name
					+"' value='"+codeDictVo.getCodeCode()
					+ "' class='" + clazz + "'   originValue = '"+ originValue +"'/>");
			if ("code-name".equals(lableType)) {
				sb.append(codeDictVo.getCodeCode()+"-"
						+ codeDictVo.getCodeName());
			} else {
				sb.append(codeDictVo.getCodeName());
			}
			sb.append("</label>");
		}
		// sb.append("</div>");
	}

	private String getDisabled(){
		if(disabled == true){
			return "disabled";
		}else{
			return "";
		}
	}
	
	private String getReadonly(){
		if(readonly == true){
			return "readonly";
		}else{
			return "";
		}
	}
	
	/**
	 * @param lableType
	 *            要设置的 lableType。
	 */
	public void setLableType(String lableType) {
		this.lableType = lableType;
	}

	public void setValue(String value) {
		valueMap = new HashMap<String, String>();
		if (value == null || "".equals(value) || (value.trim().equals("null"))) {
			this.value = null;
		} else {
			this.value = value;

			if (value.contains(",")) {
				String[] defVals = value.split(",");
				for (String defVal : defVals) {
					valueMap.put(defVal, null);
				}
			}else if(StringUtils.isNotBlank(value)){
				valueMap.put(value,null);
			}

		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getOnclick() {
		if (onclick == null || "".equals(onclick)
				|| (onclick.trim().equals("null"))) {
			return "";
		} else {
			return "onclick=" + onclick;
		}
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOnchange() {
		if (onchange == null || "".equals(onchange)
				|| (onchange.trim().equals("null"))) {
			return "";
		} else {
			return "onchange=\"" + onchange+"\"";
		}
	}

	public void setOnchange(String onchange) {

		this.onchange = onchange;
	}

	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public String getValue() {
		return value;
	}

	public String getLableType() {
		return lableType;
	}

	public String getStyle() {
		if (style == null || "".equals(style) || (style.trim().equals("null"))) {
			return " style='' ";
		} else {
			return " style='" + style + ";' ";
		}
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getRiskCode() {
		return riskCode;
	}
	
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public void setNullToVal(String nullToVal) {
		this.nullToVal = nullToVal;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public String getOriginValue() {
		return originValue;
	}

	public void setOriginValue(String originValue) {
		this.originValue = originValue;
	}

	
	public Object getDataSource() {
		return dataSource;
	}

	
	public void setDataSource(Object dataSource) {
		this.dataSource = dataSource;
	}

	public void setUpperCode(String upperCode) {
		this.upperCode = upperCode;
	}

	public boolean isDictFlag() {
		return dictFlag;
	}

	public void setDictFlag(boolean dictFlag) {
		this.dictFlag = dictFlag;
	}

	public String getExceptValue() {
		return exceptValue;
	}

	public void setExceptValue(String exceptValue) {
		this.exceptValue = exceptValue;
	}

	public boolean getTitle() {
		return title;
	}

	public void setTitle(boolean title) {
		this.title = title;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
}
