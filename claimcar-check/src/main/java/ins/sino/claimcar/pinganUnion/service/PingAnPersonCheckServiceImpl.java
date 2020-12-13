package ins.sino.claimcar.pinganUnion.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import ins.sino.claimcar.pinganUnion.vo.PingAnDataNoticeVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.platform.common.service.facade.PingAnDictService;
import ins.platform.vo.PiccCodeDictVo;
import ins.sino.claimcar.check.po.PrpLCheckPerson;
import ins.sino.claimcar.check.po.PrpLCheckTask;
import ins.sino.claimcar.check.service.CheckHandleService;
import ins.sino.claimcar.check.service.CheckTaskService;
import ins.sino.claimcar.check.vo.PrpLCheckCarVo;
import ins.sino.claimcar.check.vo.PrpLCheckVo;
import ins.sino.claimcar.pinganUnion.enums.PingAnCodeTypeEnum;
import ins.sino.claimcar.pinganUnion.vo.ResultBean;
import ins.sino.claimcar.pinganVo.PersonObjectDTO;
import ins.sino.claimcar.pinganVo.RespPersonCheckDataVo;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},group="pingAnPersonCheckService")
@Path("pingAnPersonCheckService")
public class PingAnPersonCheckServiceImpl implements PingAnHandleService{
	private static Logger logger = LoggerFactory.getLogger(PingAnPersonCheckServiceImpl.class);
	@Autowired
	CheckHandleService checkHandleService;
	@Autowired
	CheckTaskService checkTaskService;
	@Autowired
	PingAnDictService pingAnDictService;
	@Autowired
	DatabaseDao databaseDao;
	@Override
	public ResultBean pingAnHandle(String registNo, PingAnDataNoticeVo pingAnDataNoticeVo, String respData) {
		logger.info("人查勘接口信息报文--------------------------------》{}",respData);
		ResultBean resultBean=ResultBean.success();
		Gson gson = new Gson();
		try{
			if(StringUtils.isBlank(registNo)){
				throw new IllegalAccessException("------报案号为空-----");
			}
			RespPersonCheckDataVo respPersonCheckDataVo=gson.fromJson(respData, RespPersonCheckDataVo.class);
			List<PrpLCheckPerson> prpLCheckPersons = new ArrayList<PrpLCheckPerson>();
			if(respPersonCheckDataVo!=null){
				if(respPersonCheckDataVo.getPersonObjectDTOList()!=null && respPersonCheckDataVo.getPersonObjectDTOList().size()>0){
					PrpLCheckVo prpLCheckVo=checkTaskService.findCheckVoByRegistNo(registNo);
					if(prpLCheckVo!=null){
						for(PersonObjectDTO personDto:respPersonCheckDataVo.getPersonObjectDTOList()){
							PrpLCheckPerson prpLCheckPerson=new PrpLCheckPerson();
							prpLCheckPerson.setRegistNo(registNo);
							PrpLCheckTask prpLCheckTask=Beans.copyDepth().from(prpLCheckVo.getPrpLCheckTask()).to(PrpLCheckTask.class);
							prpLCheckPerson.setPrpLCheckTask(prpLCheckTask);
							if(prpLCheckVo.getPrpLCheckTask().getPrpLCheckCars()!=null && prpLCheckVo.getPrpLCheckTask().getPrpLCheckCars().size()>0){
								for(PrpLCheckCarVo prpLCheckCar:prpLCheckVo.getPrpLCheckTask().getPrpLCheckCars()){
									if(StringUtils.isNotBlank(prpLCheckCar.getRemark())){
										if(prpLCheckCar.getRemark().equals(personDto.getLossObjectNo()) && prpLCheckCar.getSerialNo()!=null){
											if(1==prpLCheckCar.getSerialNo()){
												prpLCheckPerson.setLossPartyId(1L);
												prpLCheckPerson.setLossPartyName("标的车("+prpLCheckCar.getPrpLCheckCarInfo().getLicenseNo()+")");
											}else{
												prpLCheckPerson.setLossPartyId(prpLCheckCar.getSerialNo().longValue());
												prpLCheckPerson.setLossPartyName("三者车("+prpLCheckCar.getPrpLCheckCarInfo().getLicenseNo()+")");
											}
											break;
										}
									}
								}
							}
							if(prpLCheckPerson.getLossPartyId()==null){
								prpLCheckPerson.setLossPartyId(0L);
								prpLCheckPerson.setLossPartyName("地面损失");
							}
							prpLCheckPerson.setRemark(personDto.getLossObjectNo());//损失对象号
							prpLCheckPerson.setPersonName(personDto.getPersonName());//伤者姓名
							if("M".equals(personDto.getPersonSex())){
								prpLCheckPerson.setPersonSex("1");
							}else{
								prpLCheckPerson.setPersonSex("2");
							}
			                if(StringUtils.isNotBlank(personDto.getPersonAge())){
			                	prpLCheckPerson.setPersonAge(new BigDecimal(personDto.getPersonAge()));
			                }
							if(respPersonCheckDataVo.getPersonSurveyDTOList()!=null && respPersonCheckDataVo.getPersonSurveyDTOList().size()>0){
								if("1".equals(respPersonCheckDataVo.getPersonSurveyDTOList().get(0).getCureType())){
									prpLCheckPerson.setPersonPayType("0");
								}else if("3".equals(respPersonCheckDataVo.getPersonSurveyDTOList().get(0).getCureType())){
									prpLCheckPerson.setPersonPayType("2");
								}else if("2".equals(respPersonCheckDataVo.getPersonSurveyDTOList().get(0).getCureType())){
									prpLCheckPerson.setPersonPayType("1");
								}
							}
							prpLCheckPerson.setCreateUser("AUTO");
							prpLCheckPerson.setCreateTime(new Date());
							prpLCheckPerson.setUpdateTime(new Date());
							prpLCheckPerson.setUpdateUser("AUTO");
							if("09".equals(personDto.getChannelType())){
								prpLCheckPerson.setPersonProp("2");
							}else if("08".equals(personDto.getChannelType())){
								prpLCheckPerson.setPersonProp("3");
							}else{
								prpLCheckPerson.setPersonProp("1");
							}
							PiccCodeDictVo  piccCodeDictVo2=pingAnDictService.getDictData(PingAnCodeTypeEnum.ZJLX.getCodeType(),personDto.getPersonCertificateType());
							prpLCheckPerson.setIdentifyType(piccCodeDictVo2.getDhCodeCode());
							prpLCheckPerson.setIdNo(personDto.getPersonCertificateNo());
							prpLCheckPersons.add(prpLCheckPerson);
						}
						//保存查勘人伤
						if(prpLCheckPersons!=null && prpLCheckPersons.size()>0){
							databaseDao.saveAll(PrpLCheckPerson.class, prpLCheckPersons);
						}
					}else{
						resultBean.fail("查勘主任务未生成,不能处理车理人查勘任务！");
					}
					
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			logger.info("平安联盟人查勘报错：",e);
			resultBean.fail("平安联盟人查勘报错："+e.getMessage());
		}
		return resultBean;
	}

}
