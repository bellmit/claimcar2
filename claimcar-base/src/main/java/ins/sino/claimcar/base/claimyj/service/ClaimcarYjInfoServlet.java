package ins.sino.claimcar.base.claimyj.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ins.framework.lang.Springs;
import ins.sino.claimcar.base.claimyj.po.PrpLyjlosscarComp;
import ins.sino.claimcar.base.claimyj.vo.ClaimYJComInfo;
import ins.sino.claimcar.base.claimyj.vo.ClaimYJComInfoBodyVo;
import ins.sino.claimcar.base.claimyj.vo.ClaimYJComInfoRes;
import ins.sino.claimcar.base.claimyj.vo.OfferVo;
import ins.sino.claimcar.base.claimyj.vo.PartOffersVo;
import ins.sino.claimcar.base.claimyj.vo.ReqHeadYJVo;
import ins.sino.claimcar.base.claimyj.vo.ResHeadYJVo;
import ins.sino.claimcar.claimcarYJ.service.ClaimcarYJComService;
import ins.sino.claimcar.claimcarYJ.vo.PrpLyjlosscarCompVo;
import ins.sino.claimcar.claimcarYJ.vo.PrplyjPartoffersVo;
import ins.sino.claimcar.hnbxrest.BaseServlet;

import com.alibaba.dubbo.common.utils.Assert;
import com.sinosoft.api.service.ServiceInterface;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class ClaimcarYjInfoServlet implements ServiceInterface{
	private static Logger logger = LoggerFactory.getLogger(ClaimcarYjInfoServlet.class);
	@Autowired
	ClaimcarYJComService claimcarYJComService;
	@Override
	public Object service(String arg0, Object arg1) {
		init();
		XStream stream = new XStream(new XppDriver(new XmlFriendlyNameCoder("-_","_")));
        stream.autodetectAnnotations(true);
        stream.setMode(XStream.NO_REFERENCES);
        stream.aliasSystemAttribute(null,"class");// 去掉 class属性
        stream.processAnnotations(ClaimYJComInfo.class);
        ClaimYJComInfo reqPacket = (ClaimYJComInfo)arg1; 
        String xml = stream.toXML(reqPacket);
        logger.info("阳杰询价通知接口接收报文: \n"+xml);
        Assert.notNull(reqPacket," 请求信息为空  ");
        ReqHeadYJVo head = reqPacket.getHeadVo();
        if( (!"CLAIMYJINFO".equals(head.getRequestType())) && (!"yangjie_user".equals(head.getUser()))|| !"yangjie_psd".equals(head.getPassword())){
            throw new IllegalArgumentException("请求头参数错误  ");
        }
        ResHeadYJVo resHeadVo = new ResHeadYJVo();
        ClaimYJComInfoRes resComVo=new ClaimYJComInfoRes();
        try{
        	ClaimYJComInfoBodyVo body = reqPacket.getBodyVo();
        	PrpLyjlosscarCompVo compVo=new PrpLyjlosscarCompVo();
        	compVo=assgin(compVo,body);
        	PrpLyjlosscarCompVo carCompVo=claimcarYJComService.findPrpLyjlosscarCompBypartId(compVo.getCarId());
        	/*List<String> pjIds=new ArrayList<String>();//防止同一辆车，同一配件重复询价
        	if(carCompVo!=null && carCompVo.getPrplyjPartoffers()!=null && carCompVo.getPrplyjPartoffers().size()>0){
        		for(PrplyjPartoffersVo vo:carCompVo.getPrplyjPartoffers()){
        			pjIds.add(vo.getPartenquiryId());
        		}
        	}*/
        	if(carCompVo!=null && compVo!=null && compVo.getPrplyjPartoffers()!=null && compVo.getPrplyjPartoffers().size()>0){
        		for(PrplyjPartoffersVo vo:compVo.getPrplyjPartoffers()){
        			if(carCompVo.getPrplyjPartoffers()!=null){
        				vo.setPrpLyjlosscarComp(carCompVo);
        				carCompVo.getPrplyjPartoffers().add(vo);
        			}
        		}
        		carCompVo.setAutonoStock(compVo.getAutonoStock());
        		carCompVo.setEnquiryId(compVo.getEnquiryId());
        		carCompVo.setEnquiryno(compVo.getEnquiryno());
        		carCompVo.setOperateTime(compVo.getOperateTime());
        		carCompVo.setUpdateTime(new Date());
        		claimcarYJComService.updatePrpLyjlosscarComp(carCompVo);
        	}else{
        		claimcarYJComService.savePrpLyjlosscarComp(compVo);
        	}
        	
        	resHeadVo.setData("");
        	resHeadVo.setError("000000");
        	resHeadVo.setMsg("成功");
        	resHeadVo.setSuccess("1");
        	resHeadVo.setTotal("");
        }catch(Exception e){
        	resHeadVo.setData("");
        	resHeadVo.setError("900402");
        	resHeadVo.setMsg("失败原因："+e.getMessage());
        	resHeadVo.setSuccess("0");
        	resHeadVo.setTotal("");
            logger.info("====================阳杰询价通知接口报错信息："+e.getMessage());
            e.printStackTrace();
        }finally{
        	resComVo.setResHeadYJVo(resHeadVo);
        	stream.processAnnotations(ClaimYJComInfoRes.class);
            logger.info("阳杰询价通知接口返回报文=========：\n"+stream.toXML(resComVo));
        }
        
        return resComVo;
	}

  private PrpLyjlosscarCompVo assgin(PrpLyjlosscarCompVo compVo,ClaimYJComInfoBodyVo body){
	  compVo.setAutonoStock(body.getAutoNoStock());
	  compVo.setBrandName(body.getBrandName());
	  compVo.setCreateTime(new Date());
	  compVo.setEnquiryId(body.getEnquiryNo());
	  compVo.setEnquiryno(body.getThirdinquiryNo());
	  compVo.setModelName(body.getModelName());
	  compVo.setOperateTime(StringFormatDate(body.getOperateTime()));
	  compVo.setOperatorName(body.getOperatorName());
	  compVo.setUpdateTime(new Date());
	  compVo.setVin(body.getVin());
	  compVo.setPlateno(body.getPlateNo());
	  compVo.setCarId(body.getCarId());
	  compVo.setRegistNo(body.getReportNo());
	  List<PrplyjPartoffersVo> offersVo=new ArrayList<PrplyjPartoffersVo>();
	  body.getPartOffersVo();
	  if(body.getPartOffersVo()!=null && body.getPartOffersVo().size()>0){
		  for(PartOffersVo Vo:body.getPartOffersVo()){
			  PrplyjPartoffersVo PartofferVo=new PrplyjPartoffersVo();
			  PartofferVo.setPartenquiryId(Vo.getPartEnquiryId());
			  List<OfferVo> offerVos=Vo.getOfferList();
			  if(offerVos!=null && offerVos.size()>0){
				  OfferVo offerVo=offerVos.get(0);
				PartofferVo.setBrand(offerVo.getBrand());
				PartofferVo.setCreateTime(new Date());
				PartofferVo.setLeadDay(offerVo.getLeadDay());
				PartofferVo.setLeadDay2(offerVo.getLeadDay2());
				PartofferVo.setNosTock(offerVo.getNoStock());
				PartofferVo.setPartenquirypriceId(offerVo.getPartEnquiryPriceId());
				PartofferVo.setPartenquiryPriceid2(offerVo.getPartEnquiryPriceId2());
				PartofferVo.setPriceWithouttax(offerVo.getPriceWithoutTax());
				PartofferVo.setPriceWithouttax2(offerVo.getPriceWithoutTax2());
				PartofferVo.setPriceWithtax(offerVo.getPriceWithTax());
				PartofferVo.setPricewithtax2(offerVo.getPriceWithTax2());
				PartofferVo.setQuality(offerVo.getQuality());
				PartofferVo.setQuality2(offerVo.getQuality2());
				PartofferVo.setQualityassurancePeriod(offerVo.getQualityAssurancePeriod());
				PartofferVo.setQualityassurancePeriod2(offerVo.getQualityAssurancePeriod2());
				PartofferVo.setRemark(offerVo.getRemark());
				PartofferVo.setRemark2(offerVo.getRemark2());
				PartofferVo.setUpdateTime(new Date());
				PartofferVo.setPrpLyjlosscarComp(compVo);
				PartofferVo.setSupplyFlag("0");
				offersVo.add(PartofferVo);
			  }
		  }
	 }
	  if(offersVo!=null && offersVo.size()>0){
		  compVo.setPrplyjPartoffers(offersVo);
	  }
	  return compVo;
  }
	
  /**
	 * 时间转换方法
	 * String 类型转换 Date类型
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	private Date StringFormatDate(String strDate){
		Date date=null;
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  if(StringUtils.isNotBlank(strDate)){
			try {
				date=format.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return date;
	}
	private void init(){
		if(claimcarYJComService==null){
			 claimcarYJComService=(ClaimcarYJComService)Springs.getBean(ClaimcarYJComService.class);
		}
		
	}
}
