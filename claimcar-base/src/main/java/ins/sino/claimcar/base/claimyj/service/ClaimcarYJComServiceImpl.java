package ins.sino.claimcar.base.claimyj.service;

import java.util.List;

import javax.ws.rs.Path;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.dao.database.support.QueryRule;
import ins.framework.utils.Beans;
import ins.sino.claimcar.base.claimyj.po.PrpLyjlosscarComp;
import ins.sino.claimcar.base.claimyj.po.PrplyjPartoffers;
import ins.sino.claimcar.claimcarYJ.service.ClaimcarYJComService;
import ins.sino.claimcar.claimcarYJ.vo.PrpLyjlosscarCompVo;
import ins.sino.claimcar.claimcarYJ.vo.PrplyjPartoffersVo;
@Service(protocol = {"dubbo"}, validation = "true", registry = {"default"},timeout = 1000000)
@Path("claimcarYJComService")
public class ClaimcarYJComServiceImpl implements ClaimcarYJComService{
	@Autowired
    private DatabaseDao databaseDao;
	@Override
	public void savePrpLyjlosscarComp(PrpLyjlosscarCompVo prpLyjlosscarCompVo) {
		PrpLyjlosscarComp po=new PrpLyjlosscarComp();
		if(prpLyjlosscarCompVo!=null){
			po=Beans.copyDepth().from(prpLyjlosscarCompVo).to(PrpLyjlosscarComp.class);
			databaseDao.save(PrpLyjlosscarComp.class,po);
		}
	}
	@Override
	public PrplyjPartoffersVo findPrplyjPartoffersBypartId(String partenquiryId) {
		if(StringUtils.isBlank(partenquiryId)){
			return null;
		}
		QueryRule query=QueryRule.getInstance();
		query.addEqual("partenquiryId", partenquiryId);
		query.addDescOrder("createTime");
		List<PrplyjPartoffers> pos=databaseDao.findAll(PrplyjPartoffers.class, query);
		PrplyjPartoffersVo prplyjPartoffersVo=new PrplyjPartoffersVo();
		if(pos!=null && pos.size()>0){
			prplyjPartoffersVo=Beans.copyDepth().from(pos.get(0)).to(PrplyjPartoffersVo.class);
		}
		return prplyjPartoffersVo;
	}
	@Override
	public PrpLyjlosscarCompVo findPrpLyjlosscarCompBypartId(String carId) {
		if(StringUtils.isBlank(carId)){
			return null;
		}
		QueryRule query=QueryRule.getInstance();
		query.addEqual("carId",carId);
		query.addDescOrder("createTime");
		List<PrpLyjlosscarComp> pos=databaseDao.findAll(PrpLyjlosscarComp.class, query);
		PrpLyjlosscarCompVo compVo=null;
		if(pos!=null && pos.size()>0){
			compVo=new PrpLyjlosscarCompVo();
			compVo=Beans.copyDepth().from(pos.get(0)).to(PrpLyjlosscarCompVo.class);
		}
		return compVo;
	}
	@Override
	public void updatePrpLyjlosscarComp(PrpLyjlosscarCompVo prpLyjlosscarCompVo) {
		PrpLyjlosscarComp po=new PrpLyjlosscarComp();
		if(prpLyjlosscarCompVo!=null){
			po=Beans.copyDepth().from(prpLyjlosscarCompVo).to(PrpLyjlosscarComp.class);
			databaseDao.update(PrpLyjlosscarComp.class,po);
		}
		
	}
	

	
}
