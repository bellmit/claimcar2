package ins.sino.claimcar.hnbxrest.service.spring;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.platform.saa.service.facade.SaaUserPowerService;
import ins.platform.saa.vo.SaaUserPowerVo;
import ins.platform.utils.SqlJoinUtils;
import ins.sino.claimcar.flow.constant.FlowNode;
import ins.sino.claimcar.hnbxrest.po.PrplQuickUser;
import ins.sino.claimcar.hnbxrest.service.QuickUserService;
import ins.sino.claimcar.hnbxrest.vo.PrplQuickUserVo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.stringtemplate.v4.compiler.STParser.list_return;

import com.alibaba.dubbo.config.annotation.Service;



@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "quickUserService")
public class QuickUserServiceImpl implements QuickUserService{

    @Autowired
    private DatabaseDao databaseDao;
    @Autowired
    SaaUserPowerService saaUserPowerService;
   
    
    @Override
    public List<PrplQuickUserVo> findAll() {
        List<PrplQuickUserVo> PrplQuickUserVos = new ArrayList<PrplQuickUserVo>();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("validStatus","1");
        List<PrplQuickUser> prplQuickUsers = databaseDao.findAll(PrplQuickUser.class,queryRule);
        if(prplQuickUsers!=null&&prplQuickUsers.size()>0){
            PrplQuickUserVos = Beans.copyDepth().from(prplQuickUsers).toList(PrplQuickUserVo.class);            
        }
        return PrplQuickUserVos;
    }
    

    @Override
    public PrplQuickUserVo findQuickUser() {
        PrplQuickUserVo PrplQuickUserVo = new PrplQuickUserVo();
        QueryRule queryRule = QueryRule.getInstance();
        queryRule.addAscOrder("times");
        List<PrplQuickUser> prplQuickUsers = databaseDao.findAll(PrplQuickUser.class,queryRule);
        if(prplQuickUsers!=null&&prplQuickUsers.size()>0){
            Beans.copy().from(prplQuickUsers.get(0)).to(PrplQuickUserVo);            
        }
        return PrplQuickUserVo;
    }


    @Override
    public PrplQuickUserVo findQuickUserByUserCode(String userCode) {
        PrplQuickUserVo PrplQuickUserVo = new PrplQuickUserVo();
        PrplQuickUser prplQuickUser = databaseDao.findByPK(PrplQuickUser.class,userCode);
        if(prplQuickUser!=null){
            Beans.copy().from(prplQuickUser).to(PrplQuickUserVo);
        }
        return PrplQuickUserVo;
    }


    @Override
    public void updateTimes(PrplQuickUserVo prplQuickUserVo) {
        int times = prplQuickUserVo.getTimes();
        times++;
        PrplQuickUser PrplQuickUser = new PrplQuickUser();
        Beans.copy().from(prplQuickUserVo).to(PrplQuickUser);  
        PrplQuickUser.setTimes(times);
        databaseDao.update(PrplQuickUser.class,PrplQuickUser);
    }


	@Override
	public PrplQuickUserVo findQuickUserByGBFlag(String isGBFlag,String comCode) {
		if(StringUtils.isNotBlank(comCode)){
			if(comCode.startsWith("0002")){
				comCode = "0002";
			}else{
				comCode = comCode.substring(0,2);
			}
		}
		SqlJoinUtils sqluUtils = new SqlJoinUtils();
		sqluUtils.append("From PrplQuickUser pq where ");
		sqluUtils.append("exists (From SaaUserGrade su where  ");
		sqluUtils.append("exists (From SaaGrade sg where sg.id = su.saaGrade.id and pq.userCode = su.userCode and sg.id = ? ) ) ");
		sqluUtils.append("and pq.isGBFlag = ? ");
		sqluUtils.append("and pq.validStatus = ? ");
		sqluUtils.append("and pq.comCode like ? ");
		sqluUtils.append(" or pq.isGBFlag = ? ");
		sqluUtils.append("order by times asc");
		if("2".equals(isGBFlag)){
			//人伤
			sqluUtils.addParamValue(Long.parseLong(FlowNode.PLFirst.getRoleCode()));
		}else{
			//查勘
			sqluUtils.addParamValue(Long.parseLong(FlowNode.Check.getRoleCode()));
		}
		sqluUtils.addParamValue(isGBFlag);
		sqluUtils.addParamValue("1");
		sqluUtils.addParamValue((comCode+"%").trim());
		//3代表人伤和查勘共存
		sqluUtils.addParamValue("3");
		List<PrplQuickUser> prpQuickUsers = databaseDao.findAllByHql(PrplQuickUser.class,sqluUtils.getSql(),sqluUtils.getParamValues());
		List<PrplQuickUser> userVoRes = new ArrayList<PrplQuickUser>();
		PrplQuickUserVo userVo = new PrplQuickUserVo();
        if(prpQuickUsers!=null&&!prpQuickUsers.isEmpty()){
        	for(PrplQuickUser prplQuickUser :prpQuickUsers){
        		userVoRes.add(prplQuickUser);
				if("3".equals(prplQuickUser.getIsGBFlag())){
					//业务设置     调度人员同时拥有查勘和人伤  ，后台校验若人员无人伤和查勘权限，则移除人员
					SaaUserPowerVo userPower = saaUserPowerService.findUserPower(prplQuickUser.getUserCode());
					if(userPower!=null && userPower.getRoleList()!=null && !userPower.getRoleList().isEmpty()){
						if(!userPower.getRoleList().contains(FlowNode.PLFirst.getRoleCode()) || !userPower.getRoleList().contains(FlowNode.Check.getRoleCode())){
							userVoRes.remove(prplQuickUser);
						}
					}
				}
			}
        	if(userVoRes!=null && !userVoRes.isEmpty()){
        		Beans.copy().from(prpQuickUsers.get(0)).to(userVo);            
        	}
        }
        return userVo;
	}


	@Override
	public List<PrplQuickUserVo> findQuickUserVosByGBFlag(String isGBFlag,String comCode) {

		if(StringUtils.isNotBlank(comCode)){
			if(comCode.startsWith("0002")){
				comCode = "0002";
			}else{
				comCode = comCode.substring(0,2);
			}
		}
		SqlJoinUtils sqluUtils = new SqlJoinUtils();
		sqluUtils.append("From PrplQuickUser pq where ");
		sqluUtils.append("exists (From SaaUserGrade su where  ");
		sqluUtils.append("exists (From SaaGrade sg where sg.id = su.saaGrade.id and pq.userCode = su.userCode and sg.id = ? ) ) ");
		sqluUtils.append("and pq.isGBFlag = ? ");
		sqluUtils.append("and pq.validStatus = ? ");
		sqluUtils.append("and pq.comCode like ? ");
		sqluUtils.append(" or pq.isGBFlag = ? ");
		sqluUtils.append("order by times asc");
		if("2".equals(isGBFlag)){
			//人伤
			sqluUtils.addParamValue(Long.parseLong(FlowNode.PLFirst.getRoleCode()));
		}else{
			//查勘
			sqluUtils.addParamValue(Long.parseLong(FlowNode.Check.getRoleCode()));
		}
		sqluUtils.addParamValue(isGBFlag);
		sqluUtils.addParamValue("1");
		sqluUtils.addParamValue((comCode+"%").trim());
		//3代表人伤和查勘共存
		sqluUtils.addParamValue("3");
		List<PrplQuickUser> prplQuickUsers = databaseDao.findAllByHql(PrplQuickUser.class,sqluUtils.getSql(),sqluUtils.getParamValues());
		List<PrplQuickUserVo> userVos = new ArrayList<PrplQuickUserVo>();
		List<PrplQuickUser> userVoRes = new ArrayList<PrplQuickUser>();
		if(prplQuickUsers!=null&& !prplQuickUsers.isEmpty()){
			for(PrplQuickUser prplQuickUser :prplQuickUsers){
				userVoRes.add(prplQuickUser);
				if("3".equals(prplQuickUser.getIsGBFlag())){
					//业务设置     调度人员同时拥有查勘和人伤  ，后台校验若人员无人伤和查勘权限，则移除人员
					SaaUserPowerVo userPower = saaUserPowerService.findUserPower(prplQuickUser.getUserCode());
					if(userPower!=null && userPower.getRoleList()!=null && !userPower.getRoleList().isEmpty()){
						if(!userPower.getRoleList().contains(FlowNode.PLFirst.getRoleCode()) || !userPower.getRoleList().contains(FlowNode.Check.getRoleCode())){
							userVoRes.remove(prplQuickUser);
						}
					}
				}
			}
			if(userVoRes!=null && !userVoRes.isEmpty()){
				userVos = Beans.copyDepth().from(userVoRes).toList(PrplQuickUserVo.class);
			}
		}
		return userVos;  
	} 
}
