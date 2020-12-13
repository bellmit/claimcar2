package ins.sino.claimcar.common.web.action;

import ins.sino.claimcar.common.service.SelectService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/select")
public class SelectAction {
	
	private static Logger logger = LoggerFactory.getLogger(SelectAction.class);
	@Autowired
	private SelectService selectService;
	
	@RequestMapping(value = "/getUserCode.do")
	@ResponseBody
	public JSONArray getUserCode(@RequestParam String comCode,String userInfo,String gradeId) throws UnsupportedEncodingException  {
		JSONArray jsonArr = new JSONArray();
		String inputer = URLDecoder.decode(userInfo, "UTF-8");
		logger.info("平级移交人员查询关键字:"+inputer);
		if(StringUtils.isNotBlank(comCode)){
			if(comCode.equals("00000000")){// 全公司， 不用处理
			}else if(comCode.endsWith("000000")){// 分公司
				comCode = comCode.substring(0,2)+"%";
			}else if(comCode.endsWith("0000")){// 分公司
				comCode = comCode.substring(0,4)+"%";
			}//其他具体机构下的  不做处理
		}
		Map<String,String> resultMap = new HashMap<String,String>();
		
	    resultMap = selectService.findUserCode(comCode,inputer,gradeId);
		
		//select2插件返回的json格式一定要是[{id:1,text:'text'},{id:2,text:'text'}]这种格式，包含id和text
		if(resultMap.size() > 0){
			for(String key:resultMap.keySet()){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id",key);
				jsonObj.put("text",key+"-"+resultMap.get(key));
				jsonArr.add(jsonObj);
			}
		}
		logger.info("平级移交查询返回人员:"+jsonArr);
		return jsonArr;
	}
}
