package ins.sino.claimcar.registinter.quickclaim.util;

import ins.platform.common.service.facade.AreaDictService;
import ins.platform.common.util.CodeTranUtil;
import ins.platform.utils.ClaimBaseCoder;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.mobilecheck.vo.HeadReq;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqBody;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqScheduleItem;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqScheduleWF;
import ins.sino.claimcar.mobilecheck.vo.PersonnelInformationReqVo;
import ins.sino.claimcar.regist.service.PolicyViewService;
import ins.sino.claimcar.regist.service.RegistQueryService;
import ins.sino.claimcar.regist.vo.PrpLCMainVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;
import ins.sino.claimcar.schedule.vo.PrpLScheduleItemsVo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="personnelInformationCoder")
public class PersonnelInformationCoder extends ClaimBaseCoder {

	@Autowired
	private PolicyViewService policyViewService;
	@Autowired
	private RegistQueryService registQueryService;
	@Autowired
	AreaDictService areaDictService;
	/**
	 * 拼装请求报文数据
	 * @param registVo
	 * @param itemsVoList
	 * @return
	 */
	public PersonnelInformationReqVo enCode(PrpLRegistVo registVo, List<PrpLScheduleItemsVo> itemsVoList) {
		
	
		
		PersonnelInformationReqVo reqVo = new PersonnelInformationReqVo();
		
		HeadReq head = new HeadReq();
		
		head.setRequestType("PersonnelInformation");
		//head.setUser("");
		//head.setPassword("");
		
		PersonnelInformationReqBody reqBody = new PersonnelInformationReqBody();
		
		List<PersonnelInformationReqScheduleItem> scheduleItemList = new ArrayList<PersonnelInformationReqScheduleItem>();
		
		PersonnelInformationReqScheduleWF reqScheduleWf = new PersonnelInformationReqScheduleWF();
		reqScheduleWf.setRegistNo(registVo.getRegistNo());
		reqScheduleWf.setPolicyNo(registVo.getPolicyNo());
		reqScheduleWf.setType("0");//0-报案环节提交， 1-查勘提交调度
		
		//设置属性值
		this.setReqScheduleWf(reqScheduleWf,registVo);
		
		for (PrpLScheduleItemsVo vo : itemsVoList) {
			//只有标的车和人伤包装报文
			if(StringUtils.equals(vo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON)
					|| StringUtils.equals(vo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_MAINCAR)){
				PersonnelInformationReqScheduleItem reqItem = new PersonnelInformationReqScheduleItem();
				reqItem.setTaskId(vo.getSerialNo());
				if(StringUtils.equals(vo.getItemType(), CodeConstants.ScheduleItemType.SCHEDULEITEMTYPE_PERSON)) {
					reqItem.setNodeType(FlowNode.PLoss.toString());
					reqItem.setItemNo(vo.getSerialNo());//-1:人伤， 0:地面， 1：标的， 234...三者车，目前财产为null
				} else {
					reqItem.setNodeType(FlowNode.Check.toString());
					reqItem.setItemNo(vo.getSerialNo());
				}
				reqItem.setItemName(vo.getItemsName());
				String[] code = areaDictService.findAreaByAreaCode(registVo.getPrpLRegistExt().getCheckAddressCode(),"");
				/*reqItem.setProvinceCode(registVo.getPrpLRegistExt().getCheckAddressCode().substring(0, 2) + "0000");
				reqItem.setCityCode(registVo.getPrpLRegistExt().getCheckAddressCode().substring(0, 4) + "00");*/
				if(code != null && code.length > 0){
					reqItem.setProvinceCode(code[0]);
					reqItem.setCityCode(code[1]);
				}
				reqItem.setRegionCode(registVo.getPrpLRegistExt().getCheckAddressCode());
				reqItem.setDamageAddress(registVo.getPrpLRegistExt().getCheckAddress());
				reqItem.setLngXlatY(registVo.getPrpLRegistExt().getCheckAddressMapCode());
				//自定义区域编码
				if(registVo.getSelfDefinareaCode()!=null){
					reqItem.setSelfDefinareaCode(registVo.getSelfDefinareaCode());
				}
				scheduleItemList.add(reqItem);
			}
			
		}
		
		reqBody.setScheduleWF(reqScheduleWf);
		reqBody.setScheduleItemList(scheduleItemList);
		
		reqVo.setHead(head);
		reqVo.setBody(reqBody);
		
		return reqVo;
	}
	

	private void setReqScheduleWf(PersonnelInformationReqScheduleWF reqScheduleWf,PrpLRegistVo registVo){
		//设置
		List<PrpLCMainVo> prpLCMainVoList = new ArrayList<PrpLCMainVo>();
		prpLCMainVoList = policyViewService.getPolicyAllInfo(registVo.getRegistNo());
		String businessPlate ="";
		
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			businessPlate = CodeTranUtil.transCode("businessPlate", prpLCMainVoList.get(0).getBusinessPlate());
			//代理人编码
			if(prpLCMainVoList.get(0).getAgentCode()!=null){
				reqScheduleWf.setAgentCode(prpLCMainVoList.get(0).getAgentCode());
			}
			//保单归属地编码
			if(prpLCMainVoList.get(0).getComCode() != null){
				String code = areaDictService.findAreaList("areaCode",registVo.getPrpLRegistExt().getCheckAddressCode());
				//承保地区
				String comCode = "";
				if(prpLCMainVoList.size()==2){
					for(PrpLCMainVo vo:prpLCMainVoList){
						if(("12").equals(vo.getRiskCode().substring(0, 2))){
							comCode = vo.getComCode();
						}
					}
				}else{
					comCode = prpLCMainVoList.get(0).getComCode();
				}
				//保单归属地编码
				reqScheduleWf.setComCode(comCode);
				//是否异地案件
				if(code != null && comCode!=""){
					if("0002".equals(code.substring(0, 4))){//深圳
						if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
							reqScheduleWf.setIsElseWhere("1");//是
						}else{
							reqScheduleWf.setIsElseWhere("0");//否
						}
					}else{
						if("0002".equals(comCode.substring(0, 4))){//添加深圳的单这种情况
							if(!code.substring(0, 4).equals(comCode.substring(0, 4))){
								reqScheduleWf.setIsElseWhere("1");//是
							}else{
								reqScheduleWf.setIsElseWhere("0");//否
							}
						}else{
							if(!code.substring(0, 2).equals(comCode.substring(0, 2))){
								reqScheduleWf.setIsElseWhere("1");//是
							}else{
								reqScheduleWf.setIsElseWhere("0");//否
							}
						}
					}
				}else{
					reqScheduleWf.setIsElseWhere("1");//是
				}
			}
		}else{
			reqScheduleWf.setAgentCode("all");
			reqScheduleWf.setIsElseWhere("all");
		}
		if(businessPlate !="" && businessPlate != null){
			reqScheduleWf.setBusinessType(businessPlate);//业务类型
		}else{
			reqScheduleWf.setBusinessType("all");//业务类型
		}
		//TODO 到时取大客户的值
		if(prpLCMainVoList != null && prpLCMainVoList.size() > 0){
			if(StringUtils.isNotBlank(prpLCMainVoList.get(0).getAgentName()) ||
					StringUtils.isNotBlank(prpLCMainVoList.get(0).getAgentCode())){
				reqScheduleWf.setCustomType("2");//客户分类
			}else{
				reqScheduleWf.setCustomType("3");//客户分类
			}
		
		}else{
			//无保单报案
			reqScheduleWf.setCustomType("all");//客户分类
		}
		PrpLRegistVo prpLRegistVo = registQueryService.findByRegistNo(registVo.getRegistNo());
		if("DM04".equals(prpLRegistVo.getDamageCode())){//全车盗抢
			reqScheduleWf.setCaseType("3");
		}else if("DM02".equals(prpLRegistVo.getDamageCode())){//玻璃案件
			reqScheduleWf.setCaseType("2");
		}else if(prpLRegistVo.getPrpLRegistPersonLosses() != null && prpLRegistVo.getPrpLRegistPersonLosses().size() > 0){
			//人伤
			reqScheduleWf.setCaseType("5");
		}else if("2".equals(prpLRegistVo.getPrpLRegistExt().getCheckType())){//快处快赔
			reqScheduleWf.setCaseType("4");
		}else{
			reqScheduleWf.setCaseType("1");//案件类型
		}
		
		
	}
}
