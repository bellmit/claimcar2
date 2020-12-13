package ins.sino.claimcar.base.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.AjaxResult;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.vo.SysUserVo;
import ins.sino.claimcar.CodeConstants.JobStatus;
import ins.sino.claimcar.claim.service.ClaimTaskService;
import ins.sino.claimcar.claim.vo.PrpLClaimVo;
import ins.sino.claimcar.other.service.QuartzLogService;
import ins.sino.claimcar.other.vo.PrplQuartzLogVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/quartz")
public class QuartzFailAction {
    
    @Autowired
    QuartzLogService quartzLogService;
    @Autowired
    ClaimTaskService claimTaskService;
    @Autowired
    RegistService registService;
    @Autowired
    PolicyViewService policyViewService;

    /**
     * 强制立案分页查询页面
     * <pre></pre>
     * @return
     * @modified:
     * ☆LinYi(2018年3月23日 下午5:00:27): <br>
     */
    @RequestMapping("/claimForceFailQueryList.do")
    public ModelAndView claimForceFailQueryList() {
        ModelAndView modelAndView = new ModelAndView();
        Date date = new Date();
        //默认前三天
        modelAndView.addObject("createTimeStart",DateUtils.addDays(date, -3));
        modelAndView.addObject("createTimeEnd",date);
        modelAndView.setViewName("/base/claimForce/ClaimForceFailQueryList");
        return modelAndView;
    }

    /**
     * 强制立案分页查询
     * <pre></pre>
     * @param prplQuartzLogVo
     * @param start
     * @param length
     * @return
     * @throws Exception
     * @modified:
     * ☆LinYi(2018年3月23日 下午5:00:46): <br>
     */
    @RequestMapping("/claimForceFailSearch.do")
    @ResponseBody
    public String claimForceFailSearch(PrplQuartzLogVo prplQuartzLogVo,
                                       @RequestParam(value = "start", defaultValue = "0") Integer start,
                                       @RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
        ResultPage<PrplQuartzLogVo> page = quartzLogService.findClaimForceForPage(prplQuartzLogVo,start,length);
        String jsonData = ResponseUtils.toDataTableJson(page,"id","registNo","reportTime","exception","failReason","status",
                "createUser","createTime","updateUser","updateTime");
        return jsonData;
    }
    
    /**
     * 强制立案
     * <pre></pre>
     * @param ids
     * @return
     * @modified:
     * ☆LinYi(2018年3月23日 下午5:01:05): <br>
     */
    @RequestMapping("/claimForce.ajax")
    @ResponseBody
    public AjaxResult claimForce(Long ids[]) throws Exception{
        SysUserVo sysUserVo = WebUserUtils.getUser();
        AjaxResult ajaxResult = new AjaxResult();
        for(int i = 0; i<ids.length; i++ ){
            //定时任务日志表
            PrplQuartzLogVo prplQuartzLogVo = quartzLogService.findQuartzLogById(ids[i]);
            String registNo = prplQuartzLogVo.getRegistNo();
            if(JobStatus.SUCCEED.equals(prplQuartzLogVo.getStatus())){
                throw new IllegalArgumentException("存在已立案数据！");
            }
            //报案表
            PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(registNo);
            String flowId = prpLRegistVo.getFlowId();
            //保单表
            List<PrpLCMainVo> prpLCMainVoList = policyViewService.findPrpLCMainVoListByRegistNo(registNo);
            if(prpLCMainVoList != null && prpLCMainVoList.size()>0){
                for(PrpLCMainVo prpLCMainVo:prpLCMainVoList){
                    //立案表
                    List<PrpLClaimVo> prpLClaimVoList = claimTaskService.findprpLClaimVoListByRegistAndPolicyNo(registNo,prpLCMainVo.getPolicyNo(),null);
                    if(prpLClaimVoList == null || prpLClaimVoList.isEmpty()){
                        //强制立案
                        claimTaskService.claimForceJob(registNo,prpLCMainVo.getPolicyNo(),flowId);
                    }
                }
            }
            prplQuartzLogVo.setStatus(JobStatus.SUCCEED);
            //更新状态
            quartzLogService.saveOrUpdateQuartzLog(prplQuartzLogVo,sysUserVo);
        }
        ajaxResult.setStatus(HttpStatus.SC_OK);
        return ajaxResult;
    }
    
}
