package ins.sino.claimcar.claim.calculator.kindCalculator;


import ins.sino.claimcar.CodeConstants;
import ins.sino.claimcar.claim.services.CompensateService;
import ins.sino.claimcar.claim.vo.CompensateVo;
import ins.sino.claimcar.claim.vo.PrpLLossItemVo;
import ins.sino.claimcar.claim.vo.PrpLLossPersonFeeVo;
import ins.sino.claimcar.claim.vo.PrpLLossPropVo;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

public class CalculatorFactory {
	private static final Log logger = LogFactory.getLog(CalculatorFactory.class);
			
	@Deprecated
	private CalculatorFactory(){
		Assert.isTrue(false);
	}

	private static boolean BOOT_SUCCEED = true;

	private final static Class[] CONSTRATOR_PARAMETERS = new Class[]{CompensateVo.class,Object.class,CompensateService.class};

	private final static Map<String,Constructor> KINDCALCULATOR_MAPPER = new HashMap<String,Constructor>();

	// 初始化险别计算器注册表。
	static{
		try{
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_A,KindCalculatorA.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_B,KindCalculatorB.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_G,KindCalculatorG.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_D12,KindCalculatorD12.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_D11,KindCalculatorD11.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_F,KindCalculatorF.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_L,KindCalculatorL.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_Z,KindCalculatorZ.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_X1,KindCalculatorX1.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_X,KindCalculatorX.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_C,KindCalculatorC4.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_D2,KindCalculatorD2.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_R,KindCalculatorR.class.getConstructor(CONSTRATOR_PARAMETERS));
			KINDCALCULATOR_MAPPER.put(CodeConstants.KINDCODE.KINDCODE_BZ,KindCalculatorBz.class.getConstructor(CONSTRATOR_PARAMETERS));
		}catch(Exception e){
			BOOT_SUCCEED = false;
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static List<KindCalculator> makeCalculator(CompensateVo compensateVo,List<Object> lossItemList,CompensateService compensateService) {
		List<KindCalculator> list = new ArrayList<KindCalculator>();
		for(Object lossItem:lossItemList){
			list.add(CalculatorFactory.makeCalculator(compensateVo,lossItem,compensateService));
		}
		return list;
	}

	public static KindCalculator makeCalculator(CompensateVo compensateVo,Object lossItemVo,CompensateService compensateService) {
		Assert.isTrue(BOOT_SUCCEED);
		String kindCode = null;
		if(lossItemVo instanceof PrpLLossItemVo){
			kindCode = ( (PrpLLossItemVo)lossItemVo ).getKindCode();
		}else if(lossItemVo instanceof PrpLLossPropVo){
			kindCode = ( (PrpLLossPropVo)lossItemVo ).getKindCode();
		}else if(lossItemVo instanceof PrpLLossPersonFeeVo){
			kindCode = ( (PrpLLossPersonFeeVo)lossItemVo ).getKindCode();
		}
		logger.info("kindCode = "+kindCode);
//		Constructor constructor = KINDCALCULATOR_MAPPER.get(kindCode.trim());
		Object kindCalculator = null;
		try{
//			kindCalculator = constructor.newInstance(new Object[]{compensateVo,prpLCItemKindVo,compensateService});
			kindCalculator = new AbstractKindCalculator(compensateVo,lossItemVo,compensateService);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return (KindCalculator)kindCalculator;
	}
}
