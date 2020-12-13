/******************************************************************************
 * CREATETIME : 2009-11-24 09:50:24
 * FILE       : com.sinosoft.webquery.ui.taglib.DataBaseCode
 * MODIFYLIST ：Name       Date            Reason/Contents
 *          --------------------------------------------------------------------
 *
 ******************************************************************************/
package ins.platform.common.web.taglib;

import ins.platform.common.util.CodeTranUtil;
import ins.platform.vo.SysCodeDictVo;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringUtils;

/**
 * 根据数据库读取代码翻译
 * 
 * @author
 * @modified: ANDI(2009-11-24 09:50:24): <br>
 */

public class CodeTransTag extends SimpleTagSupport {
	private static final long serialVersionUID = 5362198234663725704L;
	private String codeType;
	private String codeCode;
	private String upperCode;//参数 过滤字段
	private String showName;// 默认显示名称，fullName 表示显示全称
	private String nullVal = "";// 代码为空时显示的内容
	private String split = ",";// 代码间的分隔符
	private String riskCode;
	private String NewRisk2020 = "1230,1231,1232,1233";

	@Override
	public void doTag() throws JspException {
		
		PageContext pageContext = (PageContext) this.getJspContext();
 		JspWriter out = pageContext.getOut();
		// spring 容器
		try {
			if (codeCode == null || "".equals(codeCode)) {
				out.write(nullVal);
				return;
			}
			if (codeType == null || "".equals(codeType)) {
				out.write(codeCode);
				return;
			}
			StringBuffer rtValue = new StringBuffer();
			Map<String,SysCodeDictVo> codeTypeMap = CodeTranUtil.findCodeDictTransMap(codeType,upperCode);
			if (codeTypeMap == null) {
				rtValue.append(codeCode);
				out.write(rtValue.toString());
				return;
			}
			String[] codeCodes = null;
			if (split != null && !"".equals(split)) {
				codeCodes = codeCode.split(split);
			} else {
				codeCodes = new String[1];
				codeCodes[0] = codeCode;
			}
			String rtCodeName = null;
			SysCodeDictVo dictVo = null;
			for (int i = 0; i < codeCodes.length; i++) {
				dictVo = codeTypeMap.get(codeCodes[i]);
				//riskCode存在，不走缓存
				if (dictVo != null && StringUtils.isBlank(riskCode)) {
					rtCodeName = dictVo.getCodeName();
					if (showName != null && showName.equals("fullName")) {
						rtCodeName = dictVo.getFullName();
					}
				}else{
					//StringUtils.isNotBlank(upperCode)目前给医院翻译标签使用，若需要使用放开即可
					if("HospitalCode".equals(codeType)){
						dictVo = CodeTranUtil.findTransCodeDict(codeType, codeCodes[i], upperCode);
					}else if("KindCode".equals(codeType)){
						if(StringUtils.isNotBlank(riskCode) && NewRisk2020.contains(riskCode)){
							dictVo = CodeTranUtil.findTransCodeDict(codeType, riskCode+codeCodes[i]);
							if(dictVo != null){
								dictVo.setCodeCode(codeCodes[i]);
							}else{
								dictVo = CodeTranUtil.findTransCodeDict(codeType, codeCodes[i]);
							}
						}else{
							dictVo = CodeTranUtil.findTransCodeDict(codeType, codeCodes[i]);
						}
					}else{
						dictVo = CodeTranUtil.findTransCodeDict(codeType, codeCodes[i]);
					}
					
					if (dictVo != null) {
						rtCodeName = dictVo.getCodeName();
						if (showName != null && showName.equals("fullName")) {
							rtCodeName = dictVo.getFullName();
						}
						
						codeTypeMap.put(codeCodes[i], dictVo);
					}
				}

				if (rtCodeName == null) {
					rtValue.append(codeCodes[i]);
				} else {
					rtValue.append(rtCodeName);
				}
				// 使用","分隔
				if (i < codeCodes.length - 1) {
					rtValue.append("、");
				}
			}
			out.write(rtValue.toString());

		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e.getMessage());
		}
	}

	/**
	 * @param codeType
	 *            要设置的 codeType。
	 */
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	/**
	 * @param codeCode
	 *            要设置的 codeCode。
	 */
	public void setCodeCode(String codeCode) {
		this.codeCode = codeCode;
	}

	public void setNullVal(String nullVal) {
		this.nullVal = nullVal;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public void setUpperCode(String upperCode) {
		this.upperCode = upperCode;
	}
	
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
}
