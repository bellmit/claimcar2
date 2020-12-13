package ins.sino.claimcar.common.web.action;

import ins.platform.common.util.AreaSelectUtil;
import ins.platform.vo.PrpDHospitalVo;
import ins.platform.vo.PrpdAppraisaVo;
import ins.platform.vo.PrpdoldprovincecityAreaVo;
import ins.platform.vo.SysAreaDictVo;






import java.util.ArrayList;
import java.util.List;




import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/areaSelect")
public class AreaSelectAction {

	/**
	 * 获取一个地区的下级地区
	 * @param upperCode
	 * @return
	 * @modified: ☆LiuPing(2016年1月12日 ): <br>
	 */
	@ResponseBody
	@RequestMapping(value = "/getChlidArea.do", method = RequestMethod.GET)
	public JSONArray getChlidArea(@RequestParam String upperCode,String handlerStatus) {

		JSONArray jsonArr = new JSONArray();

		if(StringUtils.isNotBlank(upperCode)){
			AreaSelectUtil areaUtil = new AreaSelectUtil();
			List<SysAreaDictVo> areaLvList = areaUtil.findChildArea(upperCode,handlerStatus);
			for(SysAreaDictVo vo:areaLvList){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("areaCode",vo.getAreaCode());
				jsonObj.put("areaName",vo.getAreaName());
				jsonArr.add(jsonObj);
			}
		}
		return jsonArr;
	}
	
	/**
	 * 获取一个地区的下级地区(收款人账户信息维护页面的银行地区)
	 * @param upperCode
	 * @return
	 * @modified: ☆yzy(2017年5月18日 ): <br>
	 */
	@ResponseBody
	@RequestMapping(value = "/getChlidOldArea.do", method = RequestMethod.GET)
	public JSONArray getChlidOldArea(@RequestParam String upperCode) {

		JSONArray jsonArr = new JSONArray();

		if(StringUtils.isNotBlank(upperCode)){
			AreaSelectUtil areaUtil = new AreaSelectUtil();
			List<PrpdoldprovincecityAreaVo> areaLvList = areaUtil.findChildOldArea(upperCode);
			for(PrpdoldprovincecityAreaVo vo:areaLvList){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("areaCode",vo.getCode());
				jsonObj.put("areaName",vo.getName());
				jsonArr.add(jsonObj);
			}
		}
		return jsonArr;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getChlidHospital.do", method = RequestMethod.GET)
	public JSONArray getChlidHospital(@RequestParam String areaCode) {

		JSONArray jsonArr = new JSONArray();

		if(StringUtils.isNotBlank(areaCode)){
			AreaSelectUtil areaUtil = new AreaSelectUtil();
			List<PrpDHospitalVo> hospitalList = areaUtil.findChildHospital(areaCode);
			for(PrpDHospitalVo vo:hospitalList){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("hospitalCode",vo.getHospitalCode());
				jsonObj.put("hospitalName",vo.getHospitalCName());
				jsonArr.add(jsonObj);
			}
		}
		return jsonArr;
	}
	
	//伤残鉴定机构
	@ResponseBody 
	@RequestMapping(value = "/getChlidAppraisa.do")
	public JSONArray getChlidAppraisa(@RequestParam String areaCode) {

		JSONArray jsonArr = new JSONArray();

		if(StringUtils.isNotBlank(areaCode)){
			AreaSelectUtil areaUtil = new AreaSelectUtil();
			List<PrpdAppraisaVo> appraisaList = areaUtil.findChildAppraisa(areaCode);
			for(PrpdAppraisaVo vo:appraisaList){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("appraisaCode",vo.getAppraisaCode());
				jsonObj.put("appraisaName",vo.getAppraisaName());
				jsonArr.add(jsonObj);
			}
		}
		return jsonArr;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAllArea.do", method = RequestMethod.GET)
	public JSONArray getAllArea(@RequestParam String areaCode,int showLevel,String targetElmId,String clazz,String disabled,String handlerStatus) {
			String[] areaPath = new String[]{null};
			AreaSelectUtil areaUtil = new AreaSelectUtil();
			StringBuffer selectHtml = new StringBuffer();

			// 先处理要显示的默认值，找到需要显示的 [省,市,区]
			if(StringUtils.isNotBlank(areaCode)){
				areaPath = areaUtil.findAreaByAreaCode(areaCode,handlerStatus);
			}

			// 得到第一级 省份数据
			List<SysAreaDictVo> areaLv1List = areaUtil.findChildArea(null,handlerStatus);// 第一级地区 省 是必须的
			selectHtml.append(bulidSelectHtml(areaLv1List,1,areaPath[0],targetElmId,clazz,disabled));

			// 第二级
			List<SysAreaDictVo> areaLv2List = new ArrayList<SysAreaDictVo>();// 第二级地区
			String upperCode = null;
			String defValue = "";

			if(showLevel>=2){// 要显示两级以上
				if(areaPath.length>=2){// 已经确定了省,找到这个省下面的市
					upperCode = areaPath[0];
					areaLv2List = areaUtil.findChildArea(upperCode,handlerStatus);
					defValue = areaPath[1];
				}
				selectHtml.append(bulidSelectHtml(areaLv2List,2,defValue,targetElmId,clazz,disabled));
			}

			// 第三级
			List<SysAreaDictVo> areaLv3List = new ArrayList<SysAreaDictVo>();// 第三级地区
			if(showLevel>=3){// 要显示3级
				defValue = "";
				if(areaPath.length>=3){// 已经确定了市，,找到这个市下面的区县
					upperCode = areaPath[1];
					areaLv3List = areaUtil.findChildArea(upperCode,handlerStatus);
					defValue = areaPath[2];
				}
				selectHtml.append(bulidSelectHtml(areaLv3List,3,defValue,targetElmId,clazz,disabled));
			}



		
		JSONArray jsonArr = new JSONArray();

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("html",selectHtml.toString());
		jsonArr.add(jsonObj);
		
		return jsonArr;
	}
	
	private StringBuffer bulidSelectHtml(List<SysAreaDictVo> areaList,int level,String defValue,String targetElmId,String clazz,String disabled) {
		StringBuffer sb = new StringBuffer();

		String other = " class=' areaselect "+clazz+"' "+disabled;

		sb.append("<select   id='"+targetElmId+"_lv"+level+"' onchange=\"changeArea(this,'"+targetElmId+"',"+level+") \" "+other+" >");

		if(clazz==null||( !clazz.contains("required")&& !clazz.contains("must") )){
			sb.append("<option value='' ></option>");
		}

		for(SysAreaDictVo dictVo:areaList){
			sb.append("<option value='"+dictVo.getAreaCode()+"' ");
			if(dictVo.getAreaCode().equals(defValue)){
				sb.append(" selected='selected' ");
			}
			sb.append(" >");
			sb.append(dictVo.getAreaName());
			sb.append("</option>");
		}

		sb.append("</select>");

		return sb;
	}
}
