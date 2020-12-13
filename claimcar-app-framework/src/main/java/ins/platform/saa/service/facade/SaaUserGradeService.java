/******************************************************************************
* CREATETIME : 2016年7月5日 上午10:32:34
******************************************************************************/
package ins.platform.saa.service.facade;

import ins.platform.saa.vo.SaaGradeVo;
import ins.platform.saa.vo.SaaUserGradeVo;

import java.util.List;


/**
 * <pre></pre>
 * @author ★LiuPing
 * @CreateTime 2016年7月5日
 */
public interface SaaUserGradeService {

	/**
	 * 查询这个岗位有哪些userCode
	 * @param gradeId
	 * @return
	 * @modified: ☆LiuPing(2016年7月5日 ): <br>
	 */
	public List<String> findGradeUsers(String gradeId);

	/**
	 * 查询一个用户的所有岗位
	 * @param userCode
	 * @return
	 * @modified: ☆LiuPing(2016年7月5日 ): <br>
	 */
	public List<SaaGradeVo> findUserGrade(String userCode);
	
	/**
	 * 根据用户gradeId和comCode查询出用户
	 * <pre></pre>
	 * @param gradeId
	 * @param comCode
	 * @return
	 * @modified:
	 * ☆WLL(2016年8月8日 下午3:37:31): <br>
	 */
	public List<SaaUserGradeVo> findSaaUserGradeListByGradeIdAndComCode(String gradeId,String comCode);

}
