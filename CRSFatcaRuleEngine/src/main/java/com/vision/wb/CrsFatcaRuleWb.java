package com.vision.wb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vision.dao.CrsFatcaRuleDao;
import com.vision.vb.CustomersVb;
import com.vision.vb.RgFatcaRulesVb;

@Component
public class CrsFatcaRuleWb {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CrsFatcaRuleDao crsFatcaRuleDao;

	public CrsFatcaRuleDao getCrsFatcaRuleDao() {
		return crsFatcaRuleDao;
	}

	public int mainMethod(String country, String leBook, String dateLastExtraction, String ruleFlag, String debugMode,
			String serviceName) {
		int returnValue = 0;
		RgFatcaRulesVb vb = new RgFatcaRulesVb();
		vb.setCountry(country);
		vb.setLeBook(leBook);
		String runDate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
		vb.setRunDate(runDate.toUpperCase());
		if ("FATCA".equalsIgnoreCase(ruleFlag)) {
			int versionNumber = crsFatcaRuleDao.getFatcaMaxVersionNo(vb);
			if (versionNumber == -1) {
				logger.info("Exception in getting new Version Number");
				System.exit(1);
			}
			vb.setFatcaVersionNumber(versionNumber + 1);
			crsFatcaRuleDao.getRgFatcaRules(vb);
		} else {
			int versionNumber = crsFatcaRuleDao.getCrsMaxVersionNo(vb);
			if (versionNumber == -1) {
				logger.info("Exception in getting new Version Number");
				System.exit(1);
			}
			vb.setCrsVersionNumber(versionNumber + 1);
			crsFatcaRuleDao.getRgCrsRules(vb);
		}
		crsFatcaRuleDao.getCustomersList(dateLastExtraction, vb, ruleFlag);
		return returnValue;
	}

}
