package ins.sino.claimcar.genilex.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import ins.framework.dao.database.DatabaseDao;
import ins.framework.utils.Beans;
import ins.sino.claimcar.genilex.po.PrpLFraudScore;
import ins.sino.claimcar.genilex.po.PrpLRuleCase;
import ins.sino.claimcar.genilex.vo.scoreVo.FraudScore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "scoreSaveService")
public class ScoreSaveService{

    @Autowired
    DatabaseDao databaseDao;
	public Long saveScore(List<FraudScore> reqFraudScores) throws SQLException {
		Long fraudScoreId = null;
		for(FraudScore fraudScore : reqFraudScores){
            PrpLFraudScore prpLFraudScore = Beans.copyDepth().from(fraudScore).to(PrpLFraudScore.class);
            prpLFraudScore.setCreateTime(new Date());
            for(PrpLRuleCase prpLRuleCase : prpLFraudScore.getSuleCases()){
                prpLRuleCase.setPrpLFraudScore(prpLFraudScore);
            }
            databaseDao.save(PrpLFraudScore.class,prpLFraudScore);
    		fraudScoreId = prpLFraudScore.getFraudScoreID();
        }
		return fraudScoreId;
	}

}
