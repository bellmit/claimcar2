package ins.sino.claimcar.manager.web.action;

import ins.framework.common.ResultPage;
import ins.framework.dao.database.support.Page;
import ins.framework.web.AjaxResult;
import ins.framework.web.bind.annotation.FormModel;
import ins.framework.web.util.ResponseUtils;
import ins.platform.common.service.facade.CodeTranService;
import ins.platform.common.web.util.WebUserUtils;
import ins.platform.saa.service.facade.SaaUserGradeService;
import ins.platform.saa.vo.SaaGradeVo;
import ins.platform.utils.ExportExcelUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claimjy.vo.JyResVo;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.flow.service.RepairFactoryService;
import ins.sino.claimcar.manager.vo.PrpLAgentFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLInsuredFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLRepairBrandVo;
import ins.sino.claimcar.manager.vo.PrpLRepairFactoryVo;
import ins.sino.claimcar.manager.vo.PrpLRepairPhoneVo;
import ins.sino.claimcar.manager.vo.PrplSysAgentfactoryVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;




@Controller
@RequestMapping("/manager")
public class RepairFactoryAction {
	
	private static Logger logger = LoggerFactory.getLogger(RepairFactoryAction.class);
	
	@Autowired
	private RepairFactoryService repairFactoryService;
	@Autowired
	private CodeTranService codeTranService;
	@Autowired
    private RegistService registService;
	
	private Map<String,String>  resultMap;

	@Autowired
	SaaUserGradeService saaUserGradeService;
	/*
	 * 请求修理厂主页面
	 */
	@RequestMapping("/repairFactoryList.do")
	public ModelAndView checkList(){
		ModelAndView mav=new ModelAndView();
		mav.setViewName("manager/RepairFactoryList");
		return mav;
	}
	
	
	/** 通过id查询修理厂并展示
	 * @param request
	 * @param session
	 * @return
	 * @modified:
	 * ☆Luwei(2016年6月22日 上午9:51:35): <br>
	 */
	@RequestMapping("/factoryId.do")
	public ModelAndView findId(HttpServletRequest request,HttpSession session){
		long startTime = System.currentTimeMillis();
		ModelAndView mav = new ModelAndView();
		String repairId = request.getParameter("Id");
		String index =request.getParameter("index");
		PrpLRepairFactoryVo repairFactoryVo = repairFactoryService.findFactoryById(repairId);
		if(resultMap == null || resultMap.isEmpty()){
			 resultMap = repairFactoryService.findAgentInfoFromPrpLCmain();
		}
		 
		List<PrpLAgentFactoryVo> agentFactoryList = repairFactoryService.findAgentFactoryByFactoryId(Long.parseLong(repairId));
		List<PrpLRepairPhoneVo> repairPhoneVos = repairFactoryVo.getPrpLRepairPhones();
		//判断当前工号有无处理权限
		List<SaaGradeVo> saaGradeVoList = saaUserGradeService.findUserGrade(WebUserUtils.getUserCode());
		String powerFlag = "0";
		if(saaGradeVoList != null && saaGradeVoList.size() > 0){
			for(SaaGradeVo saaGradeVo:saaGradeVoList){
				if("5194".equals(saaGradeVo.getId().toString())){//修理厂的ID是
					powerFlag = "1";
					break;
				}
			}
		}
		mav.addObject("powerFlag",powerFlag);
		mav.addObject("repairPhoneList",repairPhoneVos);
		mav.addObject("agentFactoryList",agentFactoryList);
		mav.addObject("resultMap", resultMap);
		mav.addObject("repairFactoryVo",repairFactoryVo);
		mav.addObject("repairSize",repairFactoryVo.getPrpLRepairBrands().size());
		mav.addObject("repairBrandVos",repairFactoryVo.getPrpLRepairBrands());
		mav.addObject("index",index);
		mav.setViewName("manager/RepairFactoryEdit");
		long endTime = System.currentTimeMillis(); 	
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
		return mav;
	}
	
	/**
	 * 修理厂查询
	 * @param PrpLRepairFactoryVo、start、length
	 * @modified:☆Luwei(2016年6月21日 下午3:34:47): <br>
	 */
	@RequestMapping("/repairFactoryFind.do")
	@ResponseBody
	public String search(
			@FormModel(value = "repairFactoryVo") PrpLRepairFactoryVo repairFactoryVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) {
		Page<PrpLRepairFactoryVo> page = repairFactoryService.findAllFactory(
				repairFactoryVo, start, length);
		String jsonData = ResponseUtils.toDataTableJson(page, "id", "areaCode:AreaCode", 
				"factoryType", "factoryCode", "factoryName", "address","validStatus:validFlag");
		logger.debug(jsonData);
		return jsonData;
	}
	
	/*
	 * 请求新增页面
	 */
	@RequestMapping(value="/repairFactoryEdit.do")
	public ModelAndView edit(String repairSize){
		ModelAndView mav = new ModelAndView();
		List<PrpLRepairBrandVo> repairBrandVos = new ArrayList<PrpLRepairBrandVo>();

		Map<String,String>  resultMap = repairFactoryService.findAgentCode(WebUserUtils.getComCode());
		mav.addObject("resultMap", resultMap);

//		Map<String,String>  resultMap = repairFactoryService.findAgentCode(WebUserUtils.getComCode());
//		mav.addObject("resultMap", resultMap);

		//判断当前工号有无处理权限
		List<SaaGradeVo> saaGradeVoList = saaUserGradeService.findUserGrade(WebUserUtils.getUserCode());
		String powerFlag = "0";
		if(saaGradeVoList != null && saaGradeVoList.size() > 0){
			for(SaaGradeVo saaGradeVo:saaGradeVoList){
				if("5194".equals(saaGradeVo.getId().toString())){//修理厂的ID是
					powerFlag = "1";
					break;
				}
			}
		}
		mav.addObject("powerFlag",powerFlag);
		mav.addObject("repairSize",repairSize);
		mav.addObject("repairBrandVos",repairBrandVos);
		mav.addObject("repairFactoryVo",new PrpLRepairFactoryVo());
		mav.setViewName("manager/RepairFactoryEdit");
		return mav;
	}
	
	/**
	 * @param repairFactory
	 * @modified:☆Luwei(2016年6月20日 下午6:21:54): <br>
	 */
	@RequestMapping(value = "/addRepairItem.ajax")
	@ResponseBody
	public ModelAndView addRepairItem(int repairSize){
		ModelAndView mav = new ModelAndView();
		PrpLRepairBrandVo repairBrandVo = new PrpLRepairBrandVo();
		mav.addObject("Idx",repairSize);
		mav.addObject("repairBrandVo",repairBrandVo);
		mav.setViewName("manager/RepairFactoryEdit_Item");
		return mav;
	}
	
	
	@RequestMapping(value = "/addAgentFactoryItem.ajax")
	@ResponseBody
	public ModelAndView addAgentFactoryItem(int agentSize,String factoryid,String factoryCode,String factoryName,String agentName,String agentCode){
		ModelAndView mav = new ModelAndView();
		PrpLAgentFactoryVo agentFactoryVo = new PrpLAgentFactoryVo();
		agentFactoryVo.setFactoryId(Long.parseLong(factoryid));
		agentFactoryVo.setFactoryCode(factoryCode);
		agentFactoryVo.setFactoryName(factoryName);
		agentFactoryVo.setAgentName(agentName);
		agentFactoryVo.setAgentCode(agentCode);
		//修改
		/*if(resultMap == null || resultMap.isEmpty()){
			resultMap = repairFactoryService.findAgentInfoFromPrpLCmain();
		}
		mav.addObject("resultMap", resultMap);*/
		mav.addObject("agentId",agentSize);
		mav.addObject("agentFactoryVo",agentFactoryVo);
		mav.setViewName("manager/AgentFactoryEdit_item");
		return mav;
	}

	/**
	 * 添加一行可修品牌
	 *
	 * @param brandCode
	 * @param brandName
	 * @return
	 */
	@RequestMapping(value = "/addBrandItem.ajax")
	@ResponseBody
	public ModelAndView addBrandItem(int repairSize, String brandCode, String brandName) {
		ModelAndView mav = new ModelAndView();
		PrpLRepairBrandVo repairBrandVo = new PrpLRepairBrandVo();
		repairBrandVo.setBrandCode(brandCode);
		repairBrandVo.setBrandName(brandName);
		mav.addObject("Idx", repairSize);
		mav.addObject("repairBrandVo", repairBrandVo);
		mav.setViewName("manager/RepairFactoryEdit_Item");
		return mav;
	}


	/*
	 * 前端发送ajax请求，保存新增维修厂信息
	 * 
	 *朱彬 2018.09.06  修改，在保存新增修理厂的数据之前需要校验 修理厂代码（factorycode）是否存在
	 *根据factoryid判断，是否首次增加，，是的话，将ajaxresult的statusText设置为空，用于前端的js校验判断，弹出提示
	 */
	@RequestMapping(value = "/repairFactorySava.do")
	@ResponseBody
	public AjaxResult addFactory(@FormModel(value = "repairFactoryVo") PrpLRepairFactoryVo repairFactory,
		@FormModel(value = "repairBrandVo") List<PrpLRepairBrandVo> repairBrandVos,
		@FormModel(value = "agentFactoryVo") List<PrpLAgentFactoryVo> agentFactoryVos,
		@FormModel(value = "repairPhoneVo") List<PrpLRepairPhoneVo> repairPhoneVos
		) {
		String userCode = WebUserUtils.getUserCode();
		AjaxResult ajaxResult = new AjaxResult();
		//朱彬添加，用于判断 状态
		String status = repairFactory.getId() == null?"A":"U";
		
		try{
			Map<String, String> map = new HashMap<String, String>();
			Map<String, String> idMap = new HashMap<String, String>();
			List<PrpLRepairPhoneVo> repairPhoneVoList = new ArrayList<PrpLRepairPhoneVo>();
			List<PrpLRepairPhoneVo> prpLrepairPhoneVoList = new ArrayList<PrpLRepairPhoneVo>();
			if(repairPhoneVos != null && repairPhoneVos.size() > 0){
				for(PrpLRepairPhoneVo vo : repairPhoneVos){
					if(vo.getId() == null){
						repairPhoneVoList.add(vo);
					}else{
						prpLrepairPhoneVoList.add(vo);
					}
				}
			}
			if(repairPhoneVoList != null && repairPhoneVoList.size() > 0 ){
				map = repairFactoryService.findAllPhone(repairPhoneVoList);
			}
			if(prpLrepairPhoneVoList != null && prpLrepairPhoneVoList.size() > 0 ){//有id
				idMap = repairFactoryService.findAllPhone(prpLrepairPhoneVoList);
			}
			ajaxResult.setStatus(HttpStatus.SC_OK);
			StringBuffer buffer = new StringBuffer();
			if(map.size() > 0 || idMap.size() > 0 ){//有重复的
				ajaxResult.setData("2");
				for(Object key : map.keySet()) {
					 buffer.append("推送修手机号码:"+key+"已经维护至ID为"+map.get(key)+"的修理厂,不能重复录入!<br>");
				}
				
				for(Object key : idMap.keySet()) {
					if((repairFactory.getId() != null) && (!repairFactory.getId().toString().equals(idMap.get(key)+""))){
						buffer.append("推送修手机号码:"+key+"已经维护至ID为"+idMap.get(key)+"的修理厂,不能重复录入!<br>");
					}
				}
				ajaxResult.setStatusText(buffer.toString());
			}
			if(StringUtils.isEmpty(buffer.toString())){
				repairFactory.setAccountNo(StringUtils.trim(repairFactory.getAccountNo()));
				repairFactory.setPayeeName(StringUtils.trim(repairFactory.getPayeeName()));
				repairFactory.setFactoryCode(repairFactory.getFactoryCode().replace(" ", "")); //去掉修理厂代码中的空格
				if(status.equals("A")){
					boolean code = repairFactoryService.findFactoryCode(repairFactory.getFactoryCode(),repairFactory.getId());
					if(code == false){
						ajaxResult.setData("1");
						ajaxResult.setStatusText("");
						ajaxResult.setStatus(HttpStatus.SC_OK);
						return ajaxResult;
					}
				}
				repairFactory = repairFactoryService.savaOrUpData(repairFactory,repairBrandVos,repairPhoneVos,userCode);
				logger.info(repairFactory.getId().toString());
				repairFactoryService.saveOrUpdateAgentFactory(agentFactoryVos,repairFactory);
				ajaxResult.setData("1");
				ajaxResult.setStatusText(String.valueOf(repairFactory.getId()));
				ajaxResult.setStatus(HttpStatus.SC_OK);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			ajaxResult.setData("2");
			ajaxResult.setStatusText("操作失败！请联系系统管理员！"+e.getMessage());
		}
		//修理厂同步精友 （朱彬）
		repairFactoryService.factorySyncJy(repairFactory, repairBrandVos, agentFactoryVos,status);
		return ajaxResult;
	}
	
	/**
	 * 导出数据
	 * @throws Exception 
	 * @modified:
	 * 牛强
	 */
	@RequestMapping(value = "/importExcel.do")
	@ResponseBody
	public ModelAndView importExcel(HttpServletResponse response,HttpServletRequest request) throws Exception{
	//	ModelAndView mav = new ModelAndView();
		//去数据库搜索要组织业务数据   
		PrpLRepairFactoryVo repairFactory = new PrpLRepairFactoryVo();
		repairFactory.setFactoryType(request.getParameter("type"));
		repairFactory.setFactoryCode(request.getParameter("code"));
		repairFactory.setValidStatus(request.getParameter("status"));
		repairFactory.setFactoryName(request.getParameter("name"));
		repairFactory.setAddress(request.getParameter("address"));
		try{
			Date d1 = new Date();
			List<PrpLRepairFactoryVo> results = repairFactoryService.findAllFactory(
					repairFactory);
			Date d2 = new Date();
			System.out.println("查询耗时：     "+(d2.getTime() - d1.getTime())/1000);
			if(results.size() > 1000){
				response.setCharacterEncoding("UTF-8");
				response.setContentType("Text/plain");
				PrintWriter out = response.getWriter();				
				out.print("查询的结果超过一千条，导出时间过长，请缩小查询范围！");
				out.close();
				
				return null;
			}
			List<Map<String,Object>> list = createExcelRecord(results);
			String fileDir = "ins/sino/claimcar/other/files/repairFactoryTempLate.xlsx";
			String keys[] = {"id","comCode","FactoryType","FactoryCode","FactoryName","Address","ValidStatus","City"};// map中的key
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try{
				File file = ExportExcelUtils.getExcelDemoFile(fileDir);
				ExportExcelUtils.writeNewExcel(file,"Sheet1",list,keys,CodeConstants.IsSingleAccident.NOT).write(os);
				Date d3 = new Date();
				System.out.println("设置excel格式耗时："   +(d3.getTime()-d2.getTime())/1000);
			}
			catch(IOException e){
				e.printStackTrace();
			}
			byte[] content = os.toByteArray();
			InputStream is = new ByteArrayInputStream(content);
			// 设置response参数，可以打开下载页面
			response.reset();

			String fileName = "修理厂.xlsx";
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
		}catch(Exception e){
			throw new Exception(e.getMessage());
		}
		//mav.setViewName("manager/RepairFactoryImport");
		
		
		return null;
	}
	
	private List<Map<String,Object>> createExcelRecord(List<PrpLRepairFactoryVo> results) {
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sheetName","sheet1");
		listmap.add(map);
		for(PrpLRepairFactoryVo resultVo:results){
			Map<String,Object> mapValue = new HashMap<String,Object>();
			mapValue.put("id",resultVo.getId());
			mapValue.put("comCode",resultVo.getComCode());
			mapValue.put("FactoryType",resultVo.getFactoryType());
			mapValue.put("FactoryCode",resultVo.getFactoryCode());
			mapValue.put("FactoryName",resultVo.getFactoryName());
			mapValue.put("Address",resultVo.getAddress());
			mapValue.put("ValidStatus",resultVo.getValidStatus());
			mapValue.put("City",resultVo.getCity());
			listmap.add(mapValue);
		}
		return listmap;
	}
	
	@RequestMapping(value = "/excelImport.do")
	public ModelAndView excelImport(HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("manager/ImportExcel");
		return mav;
	}
	
	/*
	 * 前端发送异步请求，校验维修厂代码是否存在
	 */
	@RequestMapping(value="/repairFactoryVerify.do")
	@ResponseBody
	public AjaxResult findFactoryCode(String factoryCode,Long id){
		//去掉修理厂代码中的空格再校验
		String factoryCodes = factoryCode.replace(" ", "");
		boolean  exists=repairFactoryService.findFactoryCode(factoryCodes,id);
		AjaxResult ajaxResult=new AjaxResult();
		if(exists){
			//factorycode可用
			ajaxResult.setData("1");
			ajaxResult.setStatus(200);
		}else{
			//factorycode不可用
			ajaxResult.setData("2");
		}
		return ajaxResult;
	}
	/*
	 * 前端发送ajax请求，更新维修厂信息
	 */
	@RequestMapping(value="/repairFactoryUpData.do")
	@ResponseBody
	public AjaxResult upDataFactory(@FormModel(value = "repairFactoryVo") PrpLRepairFactoryVo repairfactoryVo,
			HttpServletRequest request){
		AjaxResult ar=new AjaxResult();
		try{
			//repairFactoryService.savaOrUpData(repairfactoryVo,userCode);
			ar.setData("1");
			ar.setStatus(200);
			
		}catch(Exception e){
			e.printStackTrace();
			ar.setData("2");
			ar.setStatusText("操作失败！请联系系统管理员！"+e.getMessage());
		}
		return ar;
	}
	/*
	 * 请求更新数据
	 */
	@RequestMapping(value="/modificationFactoryEdit.do")
	@ResponseBody
	public ModelAndView upDataFactoryById(HttpServletRequest request,HttpSession session){
		String id=(String)request.getParameter("Id");
		PrpLRepairFactoryVo PrpLRepairFactoryVo=repairFactoryService.findFactoryById(id);
		session.setAttribute("PrpLRepairFactoryVo",PrpLRepairFactoryVo);
		ModelAndView mav=new ModelAndView();
		mav.setViewName("manager/ModificationFactoryEdit");
		mav.addObject("Id",id);
		return mav;
	}
	/*
	 * 激活或注销(有效无效值)
	 * 
	 */
	@RequestMapping(value = "/repairFactoryCancelled.do")
	@ResponseBody
	public AjaxResult validStatus(HttpServletRequest request) {
		AjaxResult ajaxResult = new AjaxResult();
		String userCode = WebUserUtils.getUserCode();
		try{
			String id = (String)request.getParameter("Id");
			repairFactoryService.updateRepairFactory(Long.parseLong(id),userCode);
			ajaxResult.setData("1");
		}
		catch(Exception e){
			e.printStackTrace();
			ajaxResult.setData("2");
			ajaxResult.setStatusText("操作失败！请联系系统管理员！"+e.getMessage());
		}
		return ajaxResult;
	}
	
	
	/**
     * 查询指定目录中电子表格中所有的数据
     * @param file 文件完整路径
     * @return
     */
    public List<PrpLRepairFactoryVo> getAllByExcel(String file){
		List<PrpLRepairFactoryVo> list = new ArrayList<PrpLRepairFactoryVo>();
        try {
			Workbook rwb = Workbook.getWorkbook(new File(file));
			Sheet rs = rwb.getSheet(0);// 或者rwb.getSheet("Test Shee 1")
			int clos = rs.getColumns();// 得到所有的列
			int rows = rs.getRows();// 得到所有的行
            
            System.out.println(clos+" rows:"+rows);
            for (int i = 1; i < rows; i++) {
				for(int j = 0; j < clos; j++ ){
					PrpLRepairFactoryVo repairVo = new PrpLRepairFactoryVo();
					// 第一个是列数，第二个是行数
					repairVo.setComCode(rs.getCell(j++ ,i).getContents());
					repairVo.setFactoryType(rs.getCell(j++ ,i).getContents());
					repairVo.setFactoryCode(rs.getCell(j++ ,i).getContents());
					repairVo.setFactoryName(rs.getCell(j++ ,i).getContents());
					repairVo.setAddress(rs.getCell(j++ ,i).getContents());
					repairVo.setTelNo(rs.getCell(j++ ,i).getContents());
					repairVo.setLinker(rs.getCell(j++ ,i).getContents());
					repairVo.setMobile(rs.getCell(j++ ,i).getContents());
					repairVo.setHourRate(new BigDecimal(rs.getCell(j++ ,i).getContents()));
					repairVo.setValidStatus(rs.getCell(j++ ,i).getContents());
					repairVo.setRemark(rs.getCell(j++ ,i).getContents());
					
					list.add(repairVo);
				}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return list;
    }

    
	@RequestMapping(value="/searchPersonalInsurInfo.do")
	@ResponseBody
	public AjaxResult searchPersonalInsurInfo(String agentCode){
		AjaxResult ajaxResult = new AjaxResult();
		Map<String, String> InsuredMap = repairFactoryService.findByAgentCode("",agentCode);
		ajaxResult.setData(InsuredMap);
		ajaxResult.setStatus(200);
		return ajaxResult;
	}
	
	@RequestMapping(value="/showInsuredInfo.do")
	@ResponseBody
	public ModelAndView showInsuredInfo(String Id,String agentCode){
		long startTime = System.currentTimeMillis();
		ModelAndView mav = new ModelAndView();
		List<PrpLInsuredFactoryVo> InsuredFactoryList = repairFactoryService.findInsuredFactoryByAgentId(Id);
		if (InsuredFactoryList != null && InsuredFactoryList.size() > 0) {
			for (PrpLInsuredFactoryVo insuredVo : InsuredFactoryList) {
			//	insuredVo.setInsuredMap(repairFactoryService.findByAgentCode(insuredVo.getInsuredCode(), agentCode));
				insuredVo.setInsuredMap(insuredVo.getInsuredCode(),insuredVo.getInsuredName());
			}
		}

		mav.addObject("agentCode", agentCode);
		mav.addObject("agentId", Id);
		mav.addObject("InsuredFactoryList", InsuredFactoryList);
		mav.setViewName("/manager/InsuredFactoryEdit");
		long endTime = System.currentTimeMillis();
		System.out.println("页面渲染时间："+(endTime-startTime)+"ms");
		return mav;
	
	}
	
	@RequestMapping(value = "/addInsuredFactoryItem.ajax")
	@ResponseBody
	public ModelAndView addInsuredFactoryItem(int insuredSize,String agentCode,Long agentId,String insuredName,String insuredCode){
		ModelAndView mav = new ModelAndView();
		PrpLInsuredFactoryVo insuredFactoryVo  = new PrpLInsuredFactoryVo();
		insuredFactoryVo.setAgentCode(agentCode);
		insuredFactoryVo.setFactoryId(agentId);
		insuredFactoryVo.setInsuredCode(insuredCode);
		insuredFactoryVo.setInsuredName(insuredName);
		//	Map<String, String> InsuredMap = repairFactoryService.findByAgentCode("",agentCode);
		mav.addObject("insuredId",insuredSize);
		mav.addObject("insuredFactoryVo",insuredFactoryVo);
	//	mav.addObject("resultMap",InsuredMap);
		mav.setViewName("manager/InsuredFactoryEdit_RedictItem");
		return mav;
	}
	
	@RequestMapping(value = "/insuredFactorySava.ajax")//insuredFactorySava
	@ResponseBody
	public AjaxResult insuredFactorySava(@FormModel(value = "insuredFactoryVo") List<PrpLInsuredFactoryVo> InsuredFactoryList,Long agentId){
		AjaxResult ajaxResult = new AjaxResult();
		try {
			repairFactoryService.saveInsuredFactory(InsuredFactoryList, agentId);
			ajaxResult.setStatus(HttpStatus.SC_OK);
		} catch (Exception e) {
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
			e.printStackTrace();
		}
		return ajaxResult;
	}
	
	@RequestMapping(value = "/getUserCode.do")
	@ResponseBody
	public JSONArray getUserCode(@RequestParam String userInfo,String agentCode) throws UnsupportedEncodingException  {
		JSONArray jsonArr = new JSONArray();
		String inputer = URLDecoder.decode(userInfo, "UTF-8");
		logger.info("被保险人查询关键字:"+inputer);
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
	    resultMap =  repairFactoryService.findByAgentCode(userInfo,agentCode);
		
		//select2插件返回的json格式一定要是[{id:1,text:'text'},{id:2,text:'text'}]这种格式，包含id和text
		if(resultMap.size() > 0){
			for(String key:resultMap.keySet()){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id",key);
				jsonObj.put("text",key+"-"+resultMap.get(key));
				jsonArr.add(jsonObj);
			}
		}
		logger.info("被保险人查询返回人员:"+jsonArr);
		return jsonArr;
	}
	
	
	@RequestMapping(value = "/showPrpLAgentFactory.ajax")
    @ResponseBody
    public ModelAndView showPrpLAgentFactory(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/PrpLAgentFactoryList");
        return mav;
    }
	
	 /**
	  * 
	  * <pre>代理人查询</pre>
	  * @param prpLAgentFactoryVo
	  * @param start
	  * @param length
	  * @return
	  * @modified:
	  * ☆zhujunde(2017年6月7日 上午11:53:27): <br>
	  */
    @RequestMapping("/prpLAgentFactoryFind.do")
    @ResponseBody
    public String prpLAgentFactoryFind(
            @FormModel(value = "prpLAgentFactoryVo") PrpLAgentFactoryVo prpLAgentFactoryVo,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "10") Integer length) {
        
        Page<PrplSysAgentfactoryVo> page = repairFactoryService.findLAgentFactory(prpLAgentFactoryVo,start,length);
        String jsonData = ResponseUtils.toDataTableJson(page, "agentName", "agentCode");
        return jsonData;
    }

	/**
	 * 打开可修品牌
	 * @return
	 */
	@RequestMapping(value = "/showRepair.ajax")
	@ResponseBody
	public ModelAndView showRepair(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("manager/AddRepairBrand");
		return mav;
	}


	/**
	 * 可修品牌查询
	 * @param prpLRepairBrandVo
	 * @param start
	 * @param length
	 * @return
	 */
	@RequestMapping("/prpLRepairBrandFind.do")
	@ResponseBody
	public String prpLAgentFactoryFind(
			@FormModel(value = "prpLRepairBrandVo") PrpLRepairBrandVo prpLRepairBrandVo,
			@RequestParam(value = "start", defaultValue = "0") Integer start,
			@RequestParam(value = "length", defaultValue = "10") Integer length) throws Exception {
		ResultPage<PrpLRepairBrandVo> page = repairFactoryService.findRepairBrand(prpLRepairBrandVo,start,length);
		String jsonData = ResponseUtils.toDataTableJson(page, "brandName", "brandCode");
		return jsonData;
	}


    @RequestMapping(value = "/showInsured.ajax")
    @ResponseBody
    public ModelAndView showInsured(String agentCode,Long agentId){
        ModelAndView mav = new ModelAndView();
        List<PrplSysAgentfactoryVo> voList = repairFactoryService.findLAgentFactoryByOther(agentCode);
        if(voList != null && voList.size() > 0){
            PrplSysAgentfactoryVo vo = voList.get(0);
            mav.addObject("agentCode",vo.getAgentCode());
            mav.addObject("agentName",vo.getAgentName());
        }
        mav.addObject("agentId",agentId);
        mav.setViewName("manager/PrpLInsuredList");
        return mav;
    }
    
    
    @RequestMapping("/findPrpcmain.do")
    @ResponseBody
    public String findPrpcmain(
            @FormModel(value = "prpLInsuredFactoryVo") PrpLInsuredFactoryVo prpLInsuredFactoryVo,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            @RequestParam(value = "length", defaultValue = "10") Integer length) {
        ResultPage<PrpLCMainVo> page = registService.findPrpLCMainVo(prpLInsuredFactoryVo,start,length);
        String jsonData = ResponseUtils.toDataTableJson(page, "insuredName", "insuredCode");
        return jsonData;
    }
   
	/**
	 * 添加手机号码
	 * @param phoneSize
	 * @return
	 */
	@RequestMapping(value = "/addPhoneItem.ajax")
	@ResponseBody
	public ModelAndView addPhoneItem(int phoneSize){
		ModelAndView mav = new ModelAndView();
		PrpLRepairPhoneVo repairPhoneVo = new PrpLRepairPhoneVo();
		mav.addObject("phoneId",phoneSize);
		mav.addObject("repairPhoneVo",repairPhoneVo);
		mav.setViewName("manager/PhoneFactoryEdit_item");
		System.out.println("12213");
		return mav;
	}
	
	/**
	 * 添加手机号码
	 * @param phoneSize
	 * @return
	 */
	@RequestMapping(value = "/saveBeforeCheck.ajax")
	@ResponseBody
	public AjaxResult saveBeforeCheck(@FormModel(value = "repairPhoneVo") List<PrpLRepairPhoneVo> repairPhoneVos){
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData("1");
		try {
			Map<String, String> map = repairFactoryService.findAllPhone(repairPhoneVos);
			ajaxResult.setStatus(HttpStatus.SC_OK);
			if(map.size() > 0){//有重复的
				ajaxResult.setData("0");
				ajaxResult.setStatusText("修理厂手机号码内容只能维护至一个修理厂");
			}
		} catch (Exception e) {
			ajaxResult.setData("0");
			ajaxResult.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			ajaxResult.setStatusText(e.getMessage());
		}
		
		
		return ajaxResult;
	}
	
}
