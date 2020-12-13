package ins.sino.claimcar.hnbxrest;

import ins.framework.dao.database.DatabaseDao;
import ins.sino.claimcar.check.vo.PrplcaseStateinfoVo;
import ins.sino.claimcar.hnbxrest.service.HnfastPayInfoService;
import ins.sino.claimcar.hnbxrest.service.SubmitcaseinforService;
import ins.sino.claimcar.hnbxrest.vo.RespondMsg;
import ins.sino.claimcar.hnbxrest.vo.RspCode;
import ins.sino.claimcar.hnbxrest.vo.SubmitcasestateVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 4.3.快处快赔系统发送案件状态给保险公司
 */
public class Submitcasestate extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(Submitcasestate.class);
	@Autowired 
	HnfastPayInfoService hnfastPayInfoService;
	@Autowired
	private DatabaseDao databaseDao;
    public Submitcasestate() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    SubmitcasestateVo submitcasestateVo = new SubmitcasestateVo();
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String jsonStr = "";
        try{
            InputStreamReader read = new InputStreamReader(request.getInputStream(),"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String temp = "";
            while(( temp = bufferedReader.readLine() )!=null){
                jsonStr += temp;
            }
            read.close();
            JSONObject rejson = JSONObject.fromObject(jsonStr);
            submitcasestateVo = (SubmitcasestateVo)JSONObject.toBean(rejson,SubmitcasestateVo.class);
            checkRequest(submitcasestateVo);
            out.print(JSONObject.fromObject(RespondMsg.SUCCESS(RspCode.SUCCESS,"成功")));
        }catch(Exception e){
        	 out.print(JSONObject.fromObject(RespondMsg.SUCCESS(RspCode.FAIL,"错误信息:"+e.getMessage())));
             e.printStackTrace();
        }finally{
        	out.flush();
            out.close();
        }
       
        try{
        	checkRequest(submitcasestateVo);
        	save(submitcasestateVo);
        	logger.info("保存成功");
        }catch(Exception e){
        	logger.info("保存失败："+e.getMessage());
        	e.printStackTrace();
        }
       
        
	}

	private void checkRequest(SubmitcasestateVo submitcasestateVo)throws Exception{
        if(StringUtils.isBlank(submitcasestateVo.getCasenumber())){
            throw new IllegalArgumentException("快赔报案号不能为空");
        }
        if(StringUtils.isBlank(submitcasestateVo.getInscaseno())){
            throw new IllegalArgumentException("保险报案号不能为空");
        }    
        if(StringUtils.isBlank(submitcasestateVo.getState())){
            throw new IllegalArgumentException("案件状态不能为空");
        }
    }
	
	private void save(SubmitcasestateVo submitcasestateVo)throws Exception{
		//通过报案号查询案件状态表
		PrplcaseStateinfoVo caseInfoVo=hnfastPayInfoService.findPrplcaseStateinfoVoByRegistNo(submitcasestateVo.getInscaseno());
		if(caseInfoVo!=null && caseInfoVo.getId()!=null){//更新案件状态表
			hnfastPayInfoService.updateOfPrplcaseStateinfo(caseInfoVo, submitcasestateVo);
		}else{//保存案件状态表
			PrplcaseStateinfoVo Vo=new PrplcaseStateinfoVo();
			hnfastPayInfoService.saveOfPrplcaseStateinfo(Vo, submitcasestateVo);
		}
		
		
		
	}
	
}
