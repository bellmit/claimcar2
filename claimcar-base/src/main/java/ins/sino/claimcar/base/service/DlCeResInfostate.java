package ins.sino.claimcar.base.service;

import ins.framework.lang.Springs;
import ins.sino.claimcar.claim.service.ClaimDLInfoService;
import ins.sino.claimcar.claim.vo.PrplFraudriskVo;
import ins.sino.claimcar.claim.vo.PrplLaborVo;
import ins.sino.claimcar.claim.vo.PrplcecheckResultVo;
import ins.sino.claimcar.claim.vo.PrplnoOperationVo;
import ins.sino.claimcar.claim.vo.PrplpriceSummaryVo;
import ins.sino.claimcar.claim.vo.PrplsmallSparepartVo;
import ins.sino.claimcar.claim.vo.PrplspaRepartVo;
import ins.sino.claimcar.dlclaim.vo.CeCheckResultVo;
import ins.sino.claimcar.dlclaim.vo.CeresponseBaseVo;
import ins.sino.claimcar.dlclaim.vo.ClaimVo;
import ins.sino.claimcar.dlclaim.vo.FraudRisksVo;
import ins.sino.claimcar.dlclaim.vo.LaborResVo;
import ins.sino.claimcar.dlclaim.vo.NonStandardOperationsVo;
import ins.sino.claimcar.dlclaim.vo.PriceSummaryVo;
import ins.sino.claimcar.dlclaim.vo.SmallSparepartResVo;
import ins.sino.claimcar.dlclaim.vo.SparepartResVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



public class DlCeResInfostate extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(DlCeResInfostate.class);
	@Autowired
	ClaimDLInfoService claimDLInfoService;
	
	
	@Override
	 protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
	          doPost(request,response);
	    }
	
    @Override
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException { 
    	init();
    	String successFlag="1";//1-表示成功，0-表示失败
    	PrplcecheckResultVo prplcecheckResultVo=null;
    	CeCheckResultVo ceCheckResultVo = new CeCheckResultVo();
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
             Map<String,Object> classMap = new HashMap<String,Object>();
             classMap.put("sparepart",SparepartResVo.class);
             classMap.put("labor",LaborResVo.class);
             classMap.put("smallSparepart",SmallSparepartResVo.class);
             classMap.put("fraudRisks",FraudRisksVo.class);
             classMap.put("nonStandardOperations",NonStandardOperationsVo.class);
             classMap.put("remark",String.class);
             ceCheckResultVo = (CeCheckResultVo)JSONObject.toBean(rejson,CeCheckResultVo.class,classMap);
             prplcecheckResultVo=exXchange(ceCheckResultVo);
     	     out.print(JSONObject.fromObject(CeresponseBaseVo.SUCCESS("10000","Success")));
     	  }catch(Exception e){
     		  successFlag="0";
         	  out.print(JSONObject.fromObject(CeresponseBaseVo.SUCCESS("10004","错误信息:"+e.getMessage())));
         	 logger.info("德联结果反馈接口错误信息：",e);
         }finally{
        	 out.flush();
 	         out.close();
        	 if("1".equals(successFlag) && prplcecheckResultVo!=null){
        		 claimDLInfoService.saveCeInfo(prplcecheckResultVo);
        		 logger.info("------------------>德联结果反馈接口信息保存成功");
        	 }
        	 
 	         
         }
        
    	
    }
    @Override
    public void init() throws ServletException {
		if(claimDLInfoService==null){
			claimDLInfoService=(ClaimDLInfoService)Springs.getBean(ClaimDLInfoService.class);
		}
	}
    /**
	 * 时间转换方法
	 *  String 类型转换类型Date
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private static Date StringtoDate(String strDate){
		Date str=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(strDate!=null){
			  try {
				str=format.parse(strDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return str;
	}
    
    /**
     * 将反馈的结果值，赋予数据库对应表
     * @param ceCheckResultVo
     * @return
     */
	private PrplcecheckResultVo exXchange(CeCheckResultVo ceCheckResultVo){
		PrplcecheckResultVo prplcecheckResultVo=null;
		if(ceCheckResultVo!=null && StringUtils.isNotBlank(ceCheckResultVo.getClaim_notification_no())){
			prplcecheckResultVo=new PrplcecheckResultVo();
			prplcecheckResultVo.setClaimNotificationNo(ceCheckResultVo.getClaim_notification_no());
			prplcecheckResultVo.setEstimateTaskNo(ceCheckResultVo.getEstimate_task_no());
			prplcecheckResultVo.setIsBigCase(ceCheckResultVo.getIsBigCase());
			prplcecheckResultVo.setAnticipationLoss(ceCheckResultVo.getAnticipation_loss());
			prplcecheckResultVo.setOperateType(ceCheckResultVo.getOperate_type());
			if("01".equals(ceCheckResultVo.getOperate_type())){
				prplcecheckResultVo.setOperateName("报案");
			}else if("03".equals(ceCheckResultVo.getOperate_type())){
				prplcecheckResultVo.setOperateName("查勘");
			}else if("04".equals(ceCheckResultVo.getOperate_type())){
				prplcecheckResultVo.setOperateName("定损");
			}else if("15".equals(ceCheckResultVo.getOperate_type())){
				prplcecheckResultVo.setOperateName("核价");
			}else{
				prplcecheckResultVo.setOperateName("核损");
			}
			ClaimVo claimVo= ceCheckResultVo.getClaim();
			if(claimVo!=null){
				prplcecheckResultVo.setLicensePlateNo(claimVo.getLicense_plate_no());
				prplcecheckResultVo.setSubjectThird(claimVo.getSubject_third());
				prplcecheckResultVo.setVehicleBrandCode(claimVo.getVehicle_brand_code());
				prplcecheckResultVo.setVehicleBrandName(claimVo.getVehicle_brand_name());
				prplcecheckResultVo.setModelName(claimVo.getModel_name());
				prplcecheckResultVo.setVin(claimVo.getVin());
				prplcecheckResultVo.setCreatetime(new Date());
				if(claimVo.getDetection_start_time()!=null){
					String stry=claimVo.getDetection_start_time().replace("'T'", " ");
					prplcecheckResultVo.setDetectionEndTime(StringtoDate(stry));
				}
				if(claimVo.getDetection_end_time()!=null){
					String stry=claimVo.getDetection_end_time().replace("'T'", " ");
					prplcecheckResultVo.setDetectionEndTime(StringtoDate(stry));
				}
				prplcecheckResultVo.setClaimResult(claimVo.getClaimResult());
				prplcecheckResultVo.setReportURL(claimVo.getReportURL());
				prplcecheckResultVo.setTotalRestValue(claimVo.getTotalRestValue());
				if("1".equals(claimVo.getClaimResult())){
					prplcecheckResultVo.setClaimResultName("正常");
				}else if("2".equals(claimVo.getClaimResult())){
					prplcecheckResultVo.setClaimResultName("案件有异常，请按报告内容进行处理");
				}
				//减损统计汇总
				PriceSummaryVo priceSummaryVo=claimVo.getPriceSummary();
				if(priceSummaryVo!=null){
					PrplpriceSummaryVo prplpriceSummaryVo=new PrplpriceSummaryVo();
					prplpriceSummaryVo.setConfirmLossPrice(priceSummaryVo.getConfirmLossPrice());
					prplpriceSummaryVo.setFraudRiskHit(priceSummaryVo.getFraudRiskHit());
					prplpriceSummaryVo.setPartTotalPrice(priceSummaryVo.getPartTotalPrice());
					prplpriceSummaryVo.setCePartTotalPrice(priceSummaryVo.getCePartTotalPrice());
					prplpriceSummaryVo.setSavingPartTotalPrice(priceSummaryVo.getSavingPartTotalPrice());
					prplpriceSummaryVo.setLaborTotalPrice(priceSummaryVo.getLaborTotalPrice());
					prplpriceSummaryVo.setCeLaborTotalPrice(priceSummaryVo.getCeLaborTotalPrice());
					prplpriceSummaryVo.setSavingLaborTotalPrice(priceSummaryVo.getSavingLaborTotalPrice());
					prplpriceSummaryVo.setSmallPartTotalPrice(priceSummaryVo.getSmallPartTotalPrice());
					prplpriceSummaryVo.setCeSmallPartTotalPrice(priceSummaryVo.getCeSmallPartTotalPrice());
					prplpriceSummaryVo.setSavingSmallPartTotalPrice(priceSummaryVo.getSavingSmallPartTotalPrice());
					prplpriceSummaryVo.setTotalPrice(priceSummaryVo.getTotalPrice());
					prplpriceSummaryVo.setCeTotalPrice(priceSummaryVo.getCeTotalPrice());
					prplpriceSummaryVo.setSavingTotalPrice(priceSummaryVo.getSavingTotalPrice());
					prplpriceSummaryVo.setRescueFee(priceSummaryVo.getRescueFee());
					prplpriceSummaryVo.setCeRescueFee(priceSummaryVo.getCeRescueFee());
					prplpriceSummaryVo.setSavingRescueFee(priceSummaryVo.getSavingRescueFee());
					prplpriceSummaryVo.setCreateTime(new Date());
					prplcecheckResultVo.setPrplpriceSummary(prplpriceSummaryVo);
			    }
				//配件减损情况
				List<SparepartResVo> sparepartResVoList=claimVo.getSparepart();
				List<PrplspaRepartVo> prplspaRepartVoList=new ArrayList<PrplspaRepartVo>();
				if(sparepartResVoList!=null && sparepartResVoList.size()>0){
					for(SparepartResVo sparepartResVo:sparepartResVoList){
						PrplspaRepartVo prplspaRepartVo=new PrplspaRepartVo();
						prplspaRepartVo.setInsuranceFitId(sparepartResVo.getInsurance_fit_id());
						prplspaRepartVo.setFittingName(sparepartResVo.getFitting_name());
						prplspaRepartVo.setInputPrice(sparepartResVo.getInput_price());
						prplspaRepartVo.setInputQuantity(sparepartResVo.getInput_quantity());
						prplspaRepartVo.setInputRemnant(sparepartResVo.getInput_remnant());
						prplspaRepartVo.setInputTotalprice(sparepartResVo.getInput_totalPrice());
						prplspaRepartVo.setFeedbackPrice(sparepartResVo.getFeedback_price());
						prplspaRepartVo.setFeedbackQuantity(sparepartResVo.getFeedback_quantity());
						prplspaRepartVo.setFeedbackRemnant(sparepartResVo.getFeedback_remnant());
						prplspaRepartVo.setFeedbackTotal(sparepartResVo.getFeedback_total());
						prplspaRepartVo.setSavingPrice(sparepartResVo.getSaving_price());
						List<String> remarkList=sparepartResVo.getRemark();
						String remark="";
						int index=0;
						if(remarkList!=null && remarkList.size()>0){
							for(String str:remarkList){
								index++;
								if(remarkList.size()==1){
									remark=remark+str;
								}else{
									if(index!=remarkList.size()){
										remark=remark+str+"-";
									}else{
										remark=remark+str;
									}
									
								}
								
							}
						}
						prplspaRepartVo.setRemark(remark);
						prplspaRepartVo.setCeSaving(sparepartResVo.getCeSaving());
						prplspaRepartVo.setOemCode(sparepartResVo.getOem_code());
						prplspaRepartVo.setCreateTime(new Date());
						prplspaRepartVoList.add(prplspaRepartVo);
					}
					prplcecheckResultVo.setPrplspaReparts(prplspaRepartVoList);
				}
				
				//工时减损情况
				List<LaborResVo> laborResVoList= claimVo.getLabor();
				List<PrplLaborVo> prplLaborVoList=new ArrayList<PrplLaborVo>();
				if(laborResVoList!=null && laborResVoList.size()>0){
					for(LaborResVo laborResVo:laborResVoList){
						PrplLaborVo prplLaborVo=new PrplLaborVo();
						prplLaborVo.setInsuranceLaborId(laborResVo.getInsurance_labor_id());
                        if("01".equals(laborResVo.getRepair_type())){
                        	prplLaborVo.setRepairType("喷漆");	
                        }else if("02".equals(laborResVo.getRepair_type())){
                        	prplLaborVo.setRepairType("钣金");
                        }else if("0201".equals(laborResVo.getRepair_type())){
                        	prplLaborVo.setRepairType("大板金");
                        }else if("0202".equals(laborResVo.getRepair_type())){
                        	prplLaborVo.setRepairType("中钣金");
                        }else if("0203".equals(laborResVo.getRepair_type())){
                        	prplLaborVo.setRepairType("小钣金");
                        }else if("03".equals(laborResVo.getRepair_type())){
                        	prplLaborVo.setRepairType("机修");
                        }else if("04".equals(laborResVo.getRepair_type())){
                        	prplLaborVo.setRepairType("拆装");
                        }else if("08".equals(laborResVo.getRepair_type())){
                        	prplLaborVo.setRepairType("电工");
                        }else{
                        	prplLaborVo.setRepairType("其它");
                        }
                        prplLaborVo.setRepairName(laborResVo.getRepair_name());
                        prplLaborVo.setInputPrice(laborResVo.getInput_price());
                        prplLaborVo.setInputCount(laborResVo.getInput_count());
                        prplLaborVo.setInputTotalprice(laborResVo.getInput_totalPrice());
                        prplLaborVo.setFeedbackUnitPrice(laborResVo.getFeedback_unit_price());
                        prplLaborVo.setFeedbackQuantity(laborResVo.getFeedback_quantity());
                        prplLaborVo.setFeedbackPrice(laborResVo.getFeedback_price());
                        prplLaborVo.setSavingPrice(laborResVo.getSaving_price());
                        List<String> remarkList=laborResVo.getRemark();
						String remark="";
						int index=0;
						if(remarkList!=null && remarkList.size()>0){
							for(String str:remarkList){
								index++;
								if(remarkList.size()==1){
									remark=remark+str;
								}else{
									if(index!=remarkList.size()){
										remark=remark+str+"-";
									}else{
										remark=remark+str;
									}
									
								}
								
							}
						}
                        prplLaborVo.setRemark(remark);
                        prplLaborVo.setCeSaving(laborResVo.getCeSaving());
                        prplLaborVo.setInsuranceFitId(laborResVo.getInsurance_fit_id());
                        prplLaborVo.setCreateTime(new Date());
                        prplLaborVoList.add(prplLaborVo);
					}
					prplcecheckResultVo.setPrplLabors(prplLaborVoList);
				}
				
				//辅料减损
				List<SmallSparepartResVo> smallSparepartResVoList=claimVo.getSmallSparepart();
				List<PrplsmallSparepartVo> prplsmallSparepartVos=new ArrayList<PrplsmallSparepartVo>();
				if(smallSparepartResVoList!=null && smallSparepartResVoList.size()>0){
					for(SmallSparepartResVo smallSparepartResVo:smallSparepartResVoList){
						PrplsmallSparepartVo prplsmallSparepartVo=new PrplsmallSparepartVo();
						prplsmallSparepartVo.setInsuranceMaterialId(smallSparepartResVo.getInsurance_material_id());
						prplsmallSparepartVo.setMaterialName(smallSparepartResVo.getMaterial_name());
						prplsmallSparepartVo.setInputPrice(smallSparepartResVo.getInput_price());
						prplsmallSparepartVo.setInputCount(smallSparepartResVo.getInput_count());
						prplsmallSparepartVo.setInputTotalprice(smallSparepartResVo.getInput_totalPrice());
						prplsmallSparepartVo.setFeedbackPrice(smallSparepartResVo.getFeedback_price());
						prplsmallSparepartVo.setFeedbackQuantity(smallSparepartResVo.getFeedback_quantity());
						prplsmallSparepartVo.setFeedbackTotal(smallSparepartResVo.getFeedback_total());
						prplsmallSparepartVo.setSavingPrice(smallSparepartResVo.getSaving_price());
						 List<String> remarkList=smallSparepartResVo.getRemark();
						String remark="";
						int index=0;
						if(remarkList!=null && remarkList.size()>0){
							for(String str:remarkList){
								index++;
								if(remarkList.size()==1){
									remark=remark+str;
								}else{
									if(index!=remarkList.size()){
										remark=remark+str+"-";
									}else{
										remark=remark+str;
									}
									
								}
								
							}
						}
						prplsmallSparepartVo.setRemark(remark);
						prplsmallSparepartVo.setCeSaving(smallSparepartResVo.getCeSaving());
						prplsmallSparepartVo.setCreateTime(new Date());
						prplsmallSparepartVos.add(prplsmallSparepartVo);
					}
					prplcecheckResultVo.setPrplsmallSpareparts(prplsmallSparepartVos);
				}
				
				//欺诈风险列表
				List<FraudRisksVo> fraudRisksVoList=claimVo.getFraudRisks();
				List<PrplFraudriskVo> prplFraudriskVoList=new ArrayList<PrplFraudriskVo>();
				if(fraudRisksVoList!=null && fraudRisksVoList.size()>0){
					for(FraudRisksVo fraudRisksVo:fraudRisksVoList){
						PrplFraudriskVo prplFraudriskVo=new PrplFraudriskVo();
						prplFraudriskVo.setFactCode(fraudRisksVo.getFactCode());
						prplFraudriskVo.setRiskDesc(fraudRisksVo.getRiskDesc());
						prplFraudriskVo.setSuggest(fraudRisksVo.getSuggest());
						prplFraudriskVo.setCreateTime(new Date());
						prplFraudriskVoList.add(prplFraudriskVo);
					}
					prplcecheckResultVo.setPrplFraudrisks(prplFraudriskVoList);
				}
				
				//操作不规范
				List<NonStandardOperationsVo> nonStandardOperationsVoList=claimVo.getNonStandardOperations();
				List<PrplnoOperationVo> prplnoOperationVoList=new ArrayList<PrplnoOperationVo>();
				if(nonStandardOperationsVoList!=null && nonStandardOperationsVoList.size()>0){
					for(NonStandardOperationsVo nonStandardOperationsVo:nonStandardOperationsVoList){
						PrplnoOperationVo prplnoOperationVo=new PrplnoOperationVo();
						prplnoOperationVo.setFactCode(nonStandardOperationsVo.getFactCode());
						prplnoOperationVo.setRiskDesc(nonStandardOperationsVo.getRiskDesc());
						prplnoOperationVo.setSuggest(nonStandardOperationsVo.getSuggest());
						prplnoOperationVo.setCreateTime(new Date());
						prplnoOperationVoList.add(prplnoOperationVo);
					}
					prplcecheckResultVo.setPrplnoOperations(prplnoOperationVoList);
				}
			}
			
		}
		
		return prplcecheckResultVo;
	}
	
}
