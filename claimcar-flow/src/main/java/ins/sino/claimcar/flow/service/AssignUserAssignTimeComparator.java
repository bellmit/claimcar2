package ins.sino.claimcar.flow.service;

import ins.sino.claimcar.flow.vo.AssignUserVo;

import java.util.Comparator;

/**
 * 比较器
 * @author zhouyanbin
 *
 */
public class AssignUserAssignTimeComparator implements Comparator<AssignUserVo> {
	
	/**
	 * 返回分配时间最早的对象，null对象直接返回
	 */
	@Override
	public int compare(AssignUserVo vo1, AssignUserVo vo2) {
		if(vo1.getAssignTime() == null){
			return -1;
		}else if(vo2.getAssignTime() == null){
			return 1;
		}else if(vo1.getAssignTime().before(vo2.getAssignTime())){
			return -1;
		}else{
			return 1;
		}
	}

}

