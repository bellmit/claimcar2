package ins.sino.claimcar.regist.web.utils;

import ins.sino.claimcar.flow.constant.FlowNode;

import java.util.HashMap;
import java.util.Map;


public class PositionMapUtils {
	private static final Map<String,String> BackUrlMap = new HashMap<String,String>();

	static{
		BackUrlMap.put(FlowNode.Regis.name(),"REGISLOCATION_BACKURL");
		BackUrlMap.put(FlowNode.Sched.name(),"SCHEDLOCATION_BACKURL");
	}

	public static String getMapBackUrl(String nodeCode) {
		String url = null;
		if(url==null) {
			url = BackUrlMap.get(nodeCode);
		}

		if(url==null) throw new IllegalArgumentException("节点BACKURL未配置："+nodeCode);
		return url;
	}
}
