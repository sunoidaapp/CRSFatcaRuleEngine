package com.vision;

import java.lang.management.ManagementFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vision.wb.CrsFatcaRuleWb;

@SpringBootApplication
public class CrsFatcaRuleEngineApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CrsFatcaRuleEngineApplication.class, args);
	}

	@Autowired
	CrsFatcaRuleWb crsFatcaRuleWb;

	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void run(String... args) throws Exception {
		String serviceName = ManagementFactory.getRuntimeMXBean().getName();
		serviceName = serviceName.substring(0, serviceName.lastIndexOf("@"));
		System.out.println(serviceName);
		int returnValue = 0;
		logger.info("\n");
		logger.info("\n");
		logger.info("************************************************************************************************");
		if (args.length == 5) {
			logger.info(args[0]);
			logger.info(args[1]);
			logger.info(args[2]);
			logger.info(args[3]);
			logger.info(args[4]);
//			returnValue = crsFatcaRuleWb.mainMethod(KE, 01, DATE_LAST_EXTRACTION, FATCA/CRS, Y, serviceName);
			returnValue = crsFatcaRuleWb.mainMethod(args[0], args[1], args[2], args[3], args[4], serviceName);
			logger.info("returnValue " + returnValue);
			System.out.println(returnValue);
			System.exit(returnValue);
		} else {
			logger.info("Args Length is not properly maintained !!");
			System.exit(1);
		}
	}

}
