package ins.sino.claimcar.verifyclaim.web.action;

import ins.framework.common.ResultPage;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.utils.DateUtils;
import ins.platform.utils.ExportExcelUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.verifyclaim.service.VerifyClaimService;
import ins.sino.claimcar.verifyclaim.vo.VerifyClaimPassVo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 核赔通过清单
 * 
 * <pre></pre>
 * @author ★LinYi
 */
@Controller
@RequestMapping("/verifyClaimPass")
public class VerifyClaimPassAction {

    @Autowired
    VerifyClaimService verifyClaimService;

    // 初始化核赔通过清单查询界面
    @RequestMapping(value = "/initList.do")
    public ModelAndView initList() {
        ModelAndView mvAndView = new ModelAndView();
        Date endDate = new Date();
        Date startDate = DateUtils.addDays(endDate, -15);
        mvAndView.addObject("claimPassTimeStart",startDate);
        mvAndView.addObject("claimPassTimeEnd",endDate);
        mvAndView.addObject("taskInTimeStart",startDate);
        mvAndView.addObject("taskInTimeEnd",endDate);
        mvAndView.setViewName("verifyClaim/VerifyClaimPassQueryList");
        return mvAndView;
    }

    // 核赔通过清单查询
    @RequestMapping(value = "/search.do")
    @ResponseBody
    public String search(VerifyClaimPassVo verifyClaimPassVo,
                         @RequestParam(value = "start", defaultValue = "0") Integer start,
                         @RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
        String comCode = WebUserUtils.getComCode();
        verifyClaimPassVo.setComCode(comCode);
        ResultPage<VerifyClaimPassVo> page = verifyClaimService.findVerifyClaimPassList(verifyClaimPassVo,start,length);
        String jsonData = ResponseUtils.toDataTableJson(page,"registNo","claimNo","compensateNo","insuredName","compensateType:CompensateType","compensateType","createTime",
                "handleTime","policyNo","sumRealPay","payeeName","bankOutLets","accountNo","payStatus:PayStatus","payStatus","handleUser");
        System.out.println(jsonData);
        return jsonData;
    }
    
    //导出
    @RequestMapping("/exportExcel.do")
    @ResponseBody
    public String exportExcel(HttpServletResponse response,VerifyClaimPassVo verifyClaimPassVo) throws Exception {
        String comCode = WebUserUtils.getComCode();
        verifyClaimPassVo.setComCode(comCode);
        List<VerifyClaimPassVo> results = verifyClaimService.getDatas(verifyClaimPassVo);

        // 填充projects数据
        List<Map<String,Object>> list = verifyClaimService.createExcelRecord(results);
        
        String fileDir = "ins/sino/claimcar/other/files/verifyClaimPassTemplate.xlsx";
        String keys[] = {"registNo","claimNo","compensateNo","compensateType","createTime",
                         "handleTime","policyNo","insuredName","sumRealPay","payeeName","bankOutLets","accountNo","payStatus"};// map中的key
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try{
            File file = ExportExcelUtils.getExcelDemoFile(fileDir);
            ExportExcelUtils.writeNewExcel(file,"Sheet1",list,keys,CodeConstants.IsSingleAccident.NOT).write(os);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();

        String fileName = "核赔通过清单.xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition","attachment;filename="+URLEncoder.encode(fileName,"utf-8"));

        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try{
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while( -1!=( bytesRead = bis.read(buff,0,buff.length) )){
                bos.write(buff,0,bytesRead);
            }
        }
        catch(final IOException e){
            throw e;
        }
        finally{
            if(bis!=null) bis.close();
            if(bos!=null) bos.close();
        }
        return null;
    }
}
