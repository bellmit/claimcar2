package ins.sino.claimcar.hnbxrest.service.spring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.check.vo.PrpLcaseImageinfoMainVo;
import ins.sino.claimcar.check.vo.PrpLcasebankBaseinfoVo;
import ins.sino.claimcar.check.vo.PrpLcaseimageBaseinfoVo;
import ins.sino.claimcar.check.vo.PrplcaseStateinfoVo;
import ins.sino.claimcar.hnbxrest.po.PrpLcaseImageinfoMain;
import ins.sino.claimcar.hnbxrest.po.PrpLcasebankBaseinfo;
import ins.sino.claimcar.hnbxrest.po.PrpLcaseimageBaseinfo;
import ins.sino.claimcar.hnbxrest.po.PrplcaseStateinfo;
import ins.sino.claimcar.hnbxrest.service.HnfastPayInfoService;
import ins.sino.claimcar.hnbxrest.service.SubmitcaseinforService;
import ins.sino.claimcar.hnbxrest.vo.CasebankVo;
import ins.sino.claimcar.hnbxrest.vo.CaseimageVo;
import ins.sino.claimcar.hnbxrest.vo.SubmitcaseimageinforVo;
import ins.sino.claimcar.hnbxrest.vo.SubmitcasestateVo;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"})
@Path(value = "hnfastPayInfoService")
public class HnfastPayInfoServiceImpl implements HnfastPayInfoService{
	@Autowired
    private DatabaseDao databaseDao;
	
	@Override
	public PrpLcaseImageinfoMainVo saveOfPrplCaseImageInfoMain(SubmitcaseimageinforVo submitcaseimageinforVo) {
		        //河南快赔返回照片主表
				PrpLcaseImageinfoMainVo mainVo=new PrpLcaseImageinfoMainVo();
				List<PrpLcaseimageBaseinfoVo> imagevos= new ArrayList<PrpLcaseimageBaseinfoVo>();
				List<PrpLcasebankBaseinfoVo> bankvos=new ArrayList<PrpLcasebankBaseinfoVo>();
				mainVo.setCaseNumber(submitcaseimageinforVo.getCasenumber());
				mainVo.setCreateTime(new Date());
				mainVo.setDataType(submitcaseimageinforVo.getDatatype());
				mainVo.setRegistNo(submitcaseimageinforVo.getInscaseno());
				//mainVo.setUploadFlag("1");//照片是否上传成功标志
				if(submitcaseimageinforVo.getCaseimagelist()!=null && submitcaseimageinforVo.getCaseimagelist().size()>0){
					for(CaseimageVo vo:submitcaseimageinforVo.getCaseimagelist()){
						PrpLcaseimageBaseinfoVo imageVo=new PrpLcaseimageBaseinfoVo();
						imageVo.setCasecarNo(vo.getCasecarno());
						imageVo.setCreateTime(new Date());
						imageVo.setDutyType(vo.getDutytype());
						imageVo.setImagetype(vo.getImagetype());
						imageVo.setImageUrl(vo.getImageurl());
						imagevos.add(imageVo);
					}
				}
				
				if(submitcaseimageinforVo.getCasebanklist()!=null && submitcaseimageinforVo.getCasebanklist().size()>0){
					for(CasebankVo vo:submitcaseimageinforVo.getCasebanklist()){
						PrpLcasebankBaseinfoVo baseVo=new PrpLcasebankBaseinfoVo();
						baseVo.setBankcardAddress(vo.getBankcardaddress());
						baseVo.setBankcardNo(vo.getBankcardno());
						baseVo.setBankcardType(vo.getBankcardtype());
						baseVo.setCasecarNo(vo.getCasecarno());
						baseVo.setCreateTime(new Date());
						baseVo.setDutyType(vo.getDutytype());
						bankvos.add(baseVo);
					}
				}
				
				PrpLcaseImageinfoMain mainpo=new PrpLcaseImageinfoMain();
				Beans.copy().from(mainVo).to(mainpo);
				List<PrpLcaseimageBaseinfo> imagepos= new ArrayList<PrpLcaseimageBaseinfo>();
				imagepos=Beans.copyDepth().from(imagevos).toList(PrpLcaseimageBaseinfo.class);
				List<PrpLcasebankBaseinfo> bankpos=new ArrayList<PrpLcasebankBaseinfo>();
				bankpos=Beans.copyDepth().from(bankvos).toList(PrpLcasebankBaseinfo.class);
				if(imagepos!=null && imagepos.size()>0){
					for(PrpLcaseimageBaseinfo po:imagepos){
						po.setPrpLcaseImageinfoMain(mainpo);
					}
				}
				if(bankpos!=null && bankpos.size()>0){
					for(PrpLcasebankBaseinfo po:bankpos){
						 po.setPrpLcaseImageinfoMain(mainpo);
					}
				}
				mainpo.setPrpLcaseimageBaseinfos(imagepos);
				mainpo.setPrpLcasebankBaseinfos(bankpos);
				databaseDao.save(PrpLcaseImageinfoMain.class,mainpo);
				mainVo=Beans.copyDepth().from(mainpo).to(PrpLcaseImageinfoMainVo.class);
				return mainVo;
	}

	@Override
	public void updateOfPrplCaseImageInfoMain(PrpLcaseImageinfoMainVo mainVo) {
		QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",mainVo.getRegistNo());
        queryRule.addDescOrder("createTime");
        List<PrpLcaseImageinfoMain> PrpLcaseImageinfoMainList = databaseDao.findAll(PrpLcaseImageinfoMain.class,queryRule);
        if(PrpLcaseImageinfoMainList!=null && PrpLcaseImageinfoMainList.size()>0){
        	PrpLcaseImageinfoMainList.get(0).setUploadFlag(mainVo.getUploadFlag());
        }
		
	}

	@Override
	public PrpLcaseImageinfoMainVo findPrpLcaseImageinfoMainByRegistNo(String registNo) {
		PrpLcaseImageinfoMainVo prpLcaseImageinfoMainVo=new PrpLcaseImageinfoMainVo();
		QueryRule queryRule = QueryRule.getInstance();
        queryRule.addEqual("registNo",registNo);
        queryRule.addDescOrder("createTime");
        List<PrpLcaseImageinfoMain> PrpLcaseImageinfoMainList = databaseDao.findAll(PrpLcaseImageinfoMain.class,queryRule);
        if(PrpLcaseImageinfoMainList!=null && PrpLcaseImageinfoMainList.size()>0){
        	prpLcaseImageinfoMainVo = Beans.copyDepth().from(PrpLcaseImageinfoMainList.get(0)).to(PrpLcaseImageinfoMainVo.class);

        }
        return prpLcaseImageinfoMainVo;
	}

	@Override
	public void updateOfPrplcaseStateinfo(PrplcaseStateinfoVo caseInfoVo,SubmitcasestateVo submitcasestateVo)throws Exception {
		caseInfoVo.setCaseNumber(submitcasestateVo.getCasenumber());
		caseInfoVo.setRegistNo(submitcasestateVo.getInscaseno());
		caseInfoVo.setUpdateTime(new Date());
		caseInfoVo.setStatus(submitcasestateVo.getState());
		PrplcaseStateinfo caseInfo=new PrplcaseStateinfo();
		Beans.copy().from(caseInfoVo).to(caseInfo);
		databaseDao.update(PrplcaseStateinfo.class,caseInfo);
		
	}

	@Override
	public void saveOfPrplcaseStateinfo(PrplcaseStateinfoVo caseInfoVo,SubmitcasestateVo submitcasestateVo) throws Exception {
		// TODO Auto-generated method stub
		caseInfoVo.setCaseNumber(submitcasestateVo.getCasenumber());
		caseInfoVo.setRegistNo(submitcasestateVo.getInscaseno());
		caseInfoVo.setCreateTime(new Date());
		caseInfoVo.setStatus(submitcasestateVo.getState());
		PrplcaseStateinfo caseInfo=new PrplcaseStateinfo();
		Beans.copy().from(caseInfoVo).to(caseInfo);
		databaseDao.save(PrplcaseStateinfo.class,caseInfo);
		
	}

	@Override
	public PrplcaseStateinfoVo findPrplcaseStateinfoVoByRegistNo(String registNo) {
		QueryRule rule=QueryRule.getInstance();
		rule.addEqual("registNo",registNo);
		PrplcaseStateinfoVo caseInfoVo=new PrplcaseStateinfoVo();
		List<PrplcaseStateinfo> cases=databaseDao.findAll(PrplcaseStateinfo.class, rule);
		if(cases!=null && cases.size()>0){
			PrplcaseStateinfo caseInfoPo=cases.get(0);
			Beans.copy().from(caseInfoPo).to(caseInfoVo);
			}
		
		return caseInfoVo;
	}
}
