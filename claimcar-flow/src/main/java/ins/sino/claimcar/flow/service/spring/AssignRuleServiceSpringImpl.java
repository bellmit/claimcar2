package ins.sino.claimcar.flow.service.spring;

import ins.sino.claimcar.flow.service.AssignRuleService;
import ins.sino.claimcar.flow.service.AssignUserAssignTimeComparator;
import ins.sino.claimcar.flow.util.AssignConstants;
import ins.sino.claimcar.flow.vo.AssignUserVo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;


/**
 * 统一分配方式实现类
 * @author zhouyanbin
 */
@Service(value = "AssignRuleService")
public class AssignRuleServiceSpringImpl  implements AssignRuleService {

	public AssignUserVo assignTask(String assignType,List<AssignUserVo> assignUserVolist) {
		AssignUserVo assignUserVo = null;
		if(AssignConstants.LOOP.equals(assignType)){
			assignUserVo = loopAssignRule(assignUserVolist);
		}
		return assignUserVo;
	}
	
	/**
	 * 循环分配
	 * @param assignUserVolist
	 * @return
	 */
	private static AssignUserVo loopAssignRule(List<AssignUserVo> assignUserVolist){
		//组装每个轮询人员最后一次接受任务的记录
		Map<String,AssignUserVo> userMap=new HashMap<String,AssignUserVo>();
		//此次参加轮询的所有人员
		Set<String> haseset=new HashSet<String>();
		if(assignUserVolist!=null && assignUserVolist.size()>0){
			for(AssignUserVo userVo:assignUserVolist){
				if(haseset.add(userVo.getUserCode())){
					userMap.put(userVo.getUserCode(), userVo);
				}else{//当map里有一条该人员的记录，比较最后的接受任务的时间，则取时间大的记录
					if(userVo.getAssignTime().getTime()>userMap.get(userVo.getUserCode()).getAssignTime().getTime()){
						userMap.put(userVo.getUserCode(),userVo);
					}
				}
			}
		}
		//将map里的记录转移到list集合中
		AssignUserVo assignUserVo = null;
		List<AssignUserVo> assignUserVos=new ArrayList<AssignUserVo>();
		if(userMap!=null && userMap.size()>0){
			for(String usercode:userMap.keySet()){
				assignUserVos.add(userMap.get(usercode));
			}
		}
		if(assignUserVos != null && !assignUserVos.isEmpty()){
			AssignUserVo[] obj = new AssignUserVo[assignUserVos.size()];
			assignUserVos.toArray(obj);
			Arrays.sort(obj, new AssignUserAssignTimeComparator());
		    //最早分配任务的人员
		    assignUserVo =(AssignUserVo) obj[0];
		  }
		return assignUserVo;
	}
	
}
