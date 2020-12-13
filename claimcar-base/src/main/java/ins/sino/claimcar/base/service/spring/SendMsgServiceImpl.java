package ins.sino.claimcar.base.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.utils.DateUtils;
import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.base.po.PrpdAddressee;
import ins.sino.claimcar.base.po.SysMsgModel;
import ins.sino.claimcar.other.service.SendMsgService;
import ins.sino.claimcar.other.vo.SendMsgParamVo;
import ins.sino.claimcar.other.vo.SysMsgModelVo;
import ins.sino.claimcar.regist.service.RegistService;
import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;
import ins.sino.claimcar.regist.vo.PrpLRegistExtVo;
import ins.sino.claimcar.regist.vo.PrpLRegistVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringProperties;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true" , registry = {"default"})
@Path(value = "sendMsgService")
public class SendMsgServiceImpl implements SendMsgService{
	
	@Autowired
	DatabaseDao databaseDao;
	@Autowired
	RegistService registService;

	/**
	 * 根据模板类型,发送节点和机构查询短信模板Vo
	 * @param modelType
	 * @param systemNode
	 * @return
	 */
	@Override
	public SysMsgModelVo findmsgModelVo(String modelType,String systemNode,String comCode,String caseType){
		if(comCode == null || "".equals(comCode)){
			comCode = "00000000";
		}
		String subComcode = comCode.substring(0, 2);
		if("00".equals(subComcode)){
			subComcode = comCode.substring(0, 4);
		}
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("modelType", modelType);
		queryRule.addEqual("systemNode", systemNode);
		queryRule.addEqual("caseType", caseType);
		queryRule.addSql("  comcode like '"+subComcode+"%' ");
		queryRule.addEqual("validFlag", "1");
		queryRule.addDescOrder("updateTime");
		List<SysMsgModel> sysMsgModelList = databaseDao.findAll(SysMsgModel.class,queryRule);
		if(sysMsgModelList.size()<=0&& !"00000000".equals(comCode)){// 如果分公司找不到模板，就查总公司的模板
			QueryRule qr = QueryRule.getInstance();
			qr.addEqual("modelType", modelType);
			qr.addEqual("systemNode", systemNode);
			qr.addEqual("caseType",caseType);
			qr.addEqual("comCode", "00000000");
			qr.addEqual("validFlag", "1");
			qr.addDescOrder("updateTime");
			sysMsgModelList = databaseDao.findAll(SysMsgModel.class,qr);
		}
		SysMsgModelVo sysMsgModeVo = null;
		if(sysMsgModelList!=null&&sysMsgModelList.size()>0){
			sysMsgModeVo = Beans.copyDepth().from(sysMsgModelList.get(0)).to(SysMsgModelVo.class);
		}
		return sysMsgModeVo;
	}
	@Override
	public List<String> getMobile(String comCode,String flag){
		if(comCode == null || comCode == ""){
			return null;
		}
		String subComcode = comCode.substring(0, 2);
		if("00".equals(subComcode)){
			subComcode = comCode.substring(0, 4);
		}
		List<String> mobiles = new ArrayList<String>();
		QueryRule queryRule = QueryRule.getInstance();
		queryRule.addEqual("validFlag", "1");
		queryRule.addEqual("flag", flag);
		queryRule.addSql("  comcode like '"+subComcode+"%' ");
		queryRule.addDescOrder("createTime");
		List<PrpdAddressee> AddresseeList =  databaseDao.findAll(PrpdAddressee.class,queryRule);
		if(AddresseeList != null){
			for(PrpdAddressee addressee:AddresseeList){
				mobiles.add(addressee.getMobileNo());
			}
		}
		return mobiles;
	}
	
	@Override
	public String getLossPart(String code){
		String lossPart = "";
		String[] temp = code.split(",");
		for(int i=0;i<temp.length;i++){
			if("1".equals(temp[i])){
				lossPart = lossPart+"前、";
			}else if("2".equals(temp[i])){
				lossPart = lossPart+"后、";
			}else if("3".equals(temp[i])){
				lossPart = lossPart+"顶、";
			}else if("4".equals(temp[i])){
				lossPart = lossPart+"底、";
			}else if("5".equals(temp[i])){
				lossPart = lossPart+"左前、";
			}else if("8".equals(temp[i])){
				lossPart = lossPart+"右前、";
			}else if("6".equals(temp[i])){
				lossPart = lossPart+"左后、";
			}else if("9".equals(temp[i])){
				lossPart = lossPart+"右后、";
			}else if("7".equals(temp[i])){
				lossPart = lossPart+"左中、";
			}else if("10".equals(temp[i])){
				lossPart = lossPart+"右中、";
			}else if("11".equals(temp[i])){
				lossPart = lossPart+"全部、";
			}
		}
		if(lossPart != "" || !"".equals(lossPart)){
			lossPart = lossPart.substring(0, lossPart.length()-1);
		}
		return lossPart;
	}
	
	@Override
	public String getMessage(String message,SendMsgParamVo msgParamVo){
		int firstLocation = message.indexOf("{");
		int secondLocation = message.indexOf("}");
		if(firstLocation >= 0 && secondLocation > firstLocation){
			// TODO 如果secondLocation == message.length()
			System.out.println("++++++++++++++++++++++++++++++message短息信息:"+message);
			String param = message.substring(firstLocation, secondLocation+1);
			System.out.println("++++++++++++++++++++++++++++++param短息信息:"+param);
			message = this.reParam(message, param, msgParamVo);
			message = this.getMessage(message, msgParamVo);
		}
		return message;
	}
	
	// 替换模板中的变量
		public String reParam(String message,String param,SendMsgParamVo msgParamVo){
		System.out.println("++++++++++++++++++++++++++++++reParam()短息信息:"+param);
		if("{报案号}".equals(param)){
			// 发给查勘员的报案号取前三位+后六位
				if("5".equals(msgParamVo.getModelType())&&"2".equals(msgParamVo.getSystemNode())){
					String registNo = msgParamVo.getRegistNo();
					registNo = msgParamVo.getRegistNo().substring(0, 3)+"*"+registNo.substring(registNo.length()-6, registNo.length());
					message = message.replace(param, registNo);
				}else{
					message = message.replace(param, msgParamVo.getRegistNo());
				}
		}else if("{被保险人名称}".equals(param)){
				message = message.replace(param, msgParamVo.getInsuredName());
		}else if("{被保险人手机}".equals(param)){
				message = message.replace(param, msgParamVo.getMobile());
		}else if("{车牌号码}".equals(param)){
				message = message.replace(param, msgParamVo.getLicenseNo());
		}else if("{车辆类型}".equals(param)){
				message = message.replace(param, msgParamVo.getBrandName());
		}else if("{出险时间}".equals(param)){
				message = message.replace(param, DateUtils.dateToStr(msgParamVo.getDamageTime(), DateUtils.YToSec));
		}else if("{出险地点}".equals(param)){
				message = message.replace(param, msgParamVo.getDamageAddress());
		}else if("{联系人姓名}".equals(param)){
				message = message.replace(param, msgParamVo.getLinkerName());
		}else if("{联系人电话}".equals(param)){
				message = message.replace(param, msgParamVo.getLinkerMobile());
		}else if("{现场查勘员}".equals(param)){
				if(msgParamVo.getScheduledUsername() != null && 
						!"".equals(msgParamVo.getScheduledUsername())){
					message = message.replace(param, msgParamVo.getScheduledUsername());
				}else if(msgParamVo.getPersonScheduledUsername() != null && 
						!"".equals(msgParamVo.getPersonScheduledUsername())){
					message = message.replace(param, msgParamVo.getPersonScheduledUsername());
				}else{
					message = message.replace(param, "");
				}
		}else if("{现场查勘员电话}".equals(param)){
				if(msgParamVo.getScheduledMobile() != null && 
						!"".equals(msgParamVo.getScheduledMobile())){
					message = message.replace(param, msgParamVo.getScheduledMobile());
				}else if(msgParamVo.getPersonScheduledMobile() != null && 
						!"".equals(msgParamVo.getPersonScheduledMobile())){
					message = message.replace(param, msgParamVo.getPersonScheduledMobile());
				}else{
					message = message.replace(param, "");
				}
		}else if("{约定查勘地点}".equals(param)){
				message = message.replace(param, msgParamVo.getCheckAddress());
		}else if("{承保险种}".equals(param)){
				message = message.replace(param, msgParamVo.getKindCode());
		}else if("{修理厂名称}".equals(param)){
				message = message.replace(param, msgParamVo.getRepairName());
		}else if("{修理厂地址}".equals(param)){
				message = message.replace(param, msgParamVo.getRepairAddress());
		}else if("{修理厂联系人}".equals(param)){
				message = message.replace(param, msgParamVo.getRepairLinker());
		}else if("{修理厂电话}".equals(param)){
				message = message.replace(param, msgParamVo.getRepairMobile());
		}else if("{约定定损地点}".equals(param)){
				message = message.replace(param, msgParamVo.getdLossAddress());
		}else if("{损失项}".equals(param)){
				message = message.replace(param, msgParamVo.getLosspart());
		}else if("{人伤跟踪员}".equals(param)){
				message = message.replace(param, msgParamVo.getPersonScheduledUsername());
		}else if("{人伤跟踪员电话}".equals(param)){
				message = message.replace(param, msgParamVo.getPersonScheduledMobile());
		}else if("{保险期间}".equals(param)){
				message = message.replace(param, msgParamVo.getInsuredDate());
		}else if("{*伤*亡}".equals(param)){
			String personLoss = msgParamVo.getInjuredcount()+"伤"+msgParamVo.getDeathcount()+"亡";
				message = message.replace(param, personLoss);
		}else if("{标的*伤*亡}".equals(param)){
			String personLoss = "标的"+msgParamVo.getItemInjuredcount()+"伤"+msgParamVo.getItemDeathcount()+"亡";
				message = message.replace(param, personLoss);
		}else if("{三者*伤*亡}".equals(param)){
			String personLoss = "三者"+msgParamVo.getThridInjuredcount()+"伤"+msgParamVo.getThridDeathcount()+"亡";
				message = message.replace(param, personLoss);
		}else if("{出险原因}".equals(param)){
				message = message.replace(param, msgParamVo.getDamageReason());
		}else if("{报案时间}".equals(param)){
				message = message.replace(param, msgParamVo.getReportTime());
		}else if("{当前时间}".equals(param)){
				message = message.replace(param, DateUtils.dateToStr(new Date(), DateUtils.YToSec));
		}else if("{保险单号码}".equals(param)){
				message = message.replace(param, msgParamVo.getPolicyNo());
		}else if("{出险经过}".equals(param)){
				message = message.replace(param, msgParamVo.getDangerRemark());
		}else if("{报案三者车牌号}".equals(param)){
				message = message.replace(param, msgParamVo.getOtherLicenseNo());
		}else if("{报案人名称}".equals(param)){
				message = message.replace(param, msgParamVo.getReportorName());
		}else if("{报案人电话}".equals(param)){
				message = message.replace(param, msgParamVo.getReportoMobile());
		}else if("{车架号}".equals(param)){
				message = message.replace(param, msgParamVo.getFrameNo());
		}else if("{调度时间}".equals(param)){
				message = message.replace(param, msgParamVo.getScheduledTime());
		}else if("{商业/交强}".equals(param)){
				message = message.replace(param, msgParamVo.getRiskType());
		}else if("{赔款金额}".equals(param)){
				message = message.replace(param, msgParamVo.getSumAmt());
		}else if("{银行账户}".equals(param)){
				message = message.replace(param, msgParamVo.getAccountNo());;
		}else if("{收款人名称}".equals(param)){
				message = message.replace(param, msgParamVo.getPayeeName());;
		}else if("{驾驶人名称}".equals(param)){
				message = message.replace(param, msgParamVo.getDriverName());
		}else if("{驾驶人电话}".equals(param)){
				message = message.replace(param, msgParamVo.getDriverPhone());
		}else if("{交强出险次数}".equals(param)){
				message = message.replace(param, msgParamVo.getRegistTimes_CI());
		}else if("{商业出险次数}".equals(param)){
				message = message.replace(param, msgParamVo.getRegistTimes_BI());
		}else if("{业务板块}".equals(param)){
				message = message.replace(param, msgParamVo.getBusinessPlate());
		}else if("{会员分类}".equals(param)){
					message = message.replace(param, msgParamVo.getBusinessClassCheckMsg());
		}else if("{核心客户}".equals(param)){
			message = message.replace(param, (msgParamVo.getIsCoreCustomer() == null ? "否" : msgParamVo.getIsCoreCustomer()));
		}else if("{主险保额}".equals(param)){
			    StringBuffer stringBuffer = new StringBuffer();
			    if(msgParamVo.getPrpCItemKinds() != null&&msgParamVo.getPrpCItemKinds().size() > 0){	        
                    for(PrpLCItemKindVo prpLCItemKindVo:msgParamVo.getPrpCItemKinds()){
					// 车上人员险（乘客，司机）, 盗抢险,第三者责任险 ,车损险
                        if("Y".equals(prpLCItemKindVo.getCalculateFlag())
                        		&&("D11".equals(prpLCItemKindVo.getKindCode())||"D12".equals(prpLCItemKindVo.getKindCode())
                        				||"G".equals(prpLCItemKindVo.getKindCode())||"B".equals(prpLCItemKindVo.getKindCode())
                        				||"A".equals(prpLCItemKindVo.getKindCode()))){
                        	BigDecimal amount = prpLCItemKindVo.getAmount();
							BigDecimal tenThousand = new BigDecimal(10000);
							amount = amount!=null ? amount.divide(tenThousand, 2, RoundingMode.HALF_UP):BigDecimal.ZERO;
						stringBuffer.append(prpLCItemKindVo.getKindName()+":"+amount+"（万）,");
                        }
                    }
                }
			    if(stringBuffer.length()>0){
			    	stringBuffer = new StringBuffer(stringBuffer.substring(0, stringBuffer.length()-1));
			    }
                message = message.replace(param, stringBuffer.toString());
		}else if("{三者责任险保额（万）}".equals(param)){
				if(msgParamVo.getPrpCItemKinds() != null&&msgParamVo.getPrpCItemKinds().size() > 0){
					for(PrpLCItemKindVo prpLCItemKindVo:msgParamVo.getPrpCItemKinds()){
						if("Y".equals(prpLCItemKindVo.getCalculateFlag())&&"B".equals(prpLCItemKindVo.getKindCode())
								&& prpLCItemKindVo.getAmount()!=null){
							BigDecimal amount = prpLCItemKindVo.getAmount();
							BigDecimal tenThousand = new BigDecimal(10000);
							amount = amount.divide(tenThousand, 2, RoundingMode.HALF_UP);
						message = message.replace(param,"三者责任险保额"+amount.toString()+"（万）");
						}
					}
				}else{
					message = message.replace(param, "");
				}
		}else if("{车上人员责任险保额（万）}".equals(param)){
				StringBuffer stringBuffer = new StringBuffer();
				if(msgParamVo.getPrpCItemKinds() != null&&msgParamVo.getPrpCItemKinds().size() > 0){
					for(PrpLCItemKindVo prpLCItemKindVo:msgParamVo.getPrpCItemKinds()){
						if("Y".equals(prpLCItemKindVo.getCalculateFlag())&&prpLCItemKindVo.getAmount()!=null
								&&("D11".equals(prpLCItemKindVo.getKindCode())||"D12".equals(prpLCItemKindVo.getKindCode()))){
							BigDecimal amount = prpLCItemKindVo.getAmount();
							BigDecimal tenThousand = new BigDecimal(10000);
							amount = amount.divide(tenThousand, 2, RoundingMode.HALF_UP);
						stringBuffer = stringBuffer.append(prpLCItemKindVo.getKindName()+"保额"+amount.toString()+"（万）,");
						}
					}
				}
				if(stringBuffer.length()>0){
			    	stringBuffer = new StringBuffer(stringBuffer.substring(0, stringBuffer.length()-1));
			    }
				message = message.replace(param, stringBuffer.toString());
		}else if("{盗抢险保额（万）}".equals(param)){
				if(msgParamVo.getPrpCItemKinds() != null&&msgParamVo.getPrpCItemKinds().size() > 0){
					for(PrpLCItemKindVo prpLCItemKindVo:msgParamVo.getPrpCItemKinds()){
						if("Y".equals(prpLCItemKindVo.getCalculateFlag()) && !CodeConstants.NEW2020KIND.kind1230.equals(prpLCItemKindVo.getRiskCode()) &&  "G".equals(prpLCItemKindVo.getKindCode())
								&& prpLCItemKindVo.getAmount()!=null){
							BigDecimal amount = prpLCItemKindVo.getAmount();
							BigDecimal tenThousand = new BigDecimal(10000);
							amount = amount.divide(tenThousand, 2, RoundingMode.HALF_UP);
						message = message.replace(param,"盗抢险保额"+amount.toString()+"（万）");
						}else if("Y".equals(prpLCItemKindVo.getCalculateFlag()) && CodeConstants.NEW2020KIND.kind1230.equals(prpLCItemKindVo.getRiskCode()) && "A".equals(prpLCItemKindVo.getKindCode()) && prpLCItemKindVo.getAmount()!=null) {
							BigDecimal amount = prpLCItemKindVo.getAmount();
							BigDecimal tenThousand = new BigDecimal(10000);
							amount = amount.divide(tenThousand, 2, RoundingMode.HALF_UP);
						message = message.replace(param,"盗抢险保额"+amount.toString()+"（万）");
						}
					}
				}
		}else if("{自助视频链接}".equals(param)){
					if(StringUtils.isNotBlank(msgParamVo.getRegistNo())){
						PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(msgParamVo.getRegistNo());
							PrpLRegistExtVo prpLRegistExtVo=prpLRegistVo.getPrpLRegistExt();
							if(prpLRegistExtVo!=null){
					String orderNo = prpLRegistExtVo.getOrderNo();// 订单号
					String url = SpringProperties.getProperty("SelfClaimVideo");// 自助视频链接
								url=url+orderNo;
								message = message.replace(param,url);
						    }
					}
		}else if("{案件评价链接}".equals(param)){
				if(StringUtils.isNotBlank(msgParamVo.getRegistNo())){
					PrpLRegistVo prpLRegistVo = registService.findRegistByRegistNo(msgParamVo.getRegistNo());
					PrpLRegistExtVo prpLRegistExtVo=prpLRegistVo.getPrpLRegistExt();
					if(prpLRegistExtVo!=null){
					String orderNo = prpLRegistExtVo.getOrderNo();// 订单号
					String url = SpringProperties.getProperty("EvaluateLink");// 自助视频链接
						url=url+orderNo;
						message = message.replace(param,url);
					}
				}
		}else if("{业务员}".equals(param)){
			message = message.replace(param, msgParamVo.getHandler());
		}else{
				message = message.replace(param, "");
			}
			return message;
		}
		
	@Override
	public Date getSendTime(String timeType){
		Date date = new Date();
		if(timeType!=null&&"2".equals(timeType)){
			Calendar d=Calendar.getInstance();
			// 短信接口会提前5分钟发送
			d.add(Calendar.MINUTE, +20);
			date = d.getTime();
		}
		return date;
	}
	
	@Override
	public String getKindName(String registNo){
		String kindName = "";
		List<PrpLCItemKindVo> CItemKindVoList = registService.findCItemKindByPolicyNo(registNo);
		if(CItemKindVoList != null && CItemKindVoList.size() > 0){
			Boolean bl = true;
			for(PrpLCItemKindVo prpLCItemKindVo:CItemKindVoList){
				if("BZ".equals(prpLCItemKindVo.getKindCode())){
					kindName = kindName+"交强、";
				}else if("A".equals(prpLCItemKindVo.getKindCode())){
					kindName = kindName+"车损、";
				}else if("B".equals(prpLCItemKindVo.getKindCode())){
					kindName = kindName+"三者、";
				}else if("F".equals(prpLCItemKindVo.getKindCode())){
					kindName = kindName+"玻璃、";
				}else if("L".equals(prpLCItemKindVo.getKindCode())){
					kindName = kindName+"划痕、";
				}else if("G".equals(prpLCItemKindVo.getKindCode())){
					kindName = kindName+"盗抢、";
				}else if(("D11".equals(prpLCItemKindVo.getKindCode())||"D12".equals(prpLCItemKindVo.getKindCode()))&&bl){
					kindName = kindName+"车上人员、";
					bl = false;
				}
			}
		}
		if(!"".equals(kindName)){
			kindName = kindName.substring(0, kindName.length()-1);
		}
		return kindName;
	}
}
