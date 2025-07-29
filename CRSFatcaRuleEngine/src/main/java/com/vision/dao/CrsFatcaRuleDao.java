package com.vision.dao;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.vision.exception.ExceptionCode;
import com.vision.util.Constants;
import com.vision.util.ValidationUtil;
import com.vision.vb.CustomersVb;
import com.vision.vb.RgFatcaRulesVb;

@Component
public class CrsFatcaRuleDao extends CommonDao {

	@Value("${crs.nationality.table.name}")
	String tableName = "";

	@Value("${crs.dual.nationality.table.name}")
	String dualTableName = "";

	public void getRgFatcaRules(RgFatcaRulesVb vb) {
		String query = "SELECT FATCA_PRIMARY_NATIONALITY_FLAG, FATCA_PRIMARY_DUAL_NATIONALITY_FLAG, "
				+ " FATCA_PRIMARY_RESIDENCE_FLAG, FATCA_PRIMARY_DOMICILE_FLAG, "
				+ " FATCA_FAMILY_NATIONALITY_FLAG, FATCA_FAMILY_DUAL_NATIONALITY_FLAG, "
				+ getDbFunction(Constants.CLOB_FORMAT, null) + " (FATCA_COUNTRY_LIST) FATCA_COUNTRY_LIST "
				+ "  FROM RG_FATCA_RULES WHERE RULE_STATUS = 0 AND COUNTRY = ? AND LE_BOOK = ? ";
		Object[] args = { vb.getCountry(), vb.getLeBook() };
		getJdbcTemplate().query(query, new ResultSetExtractor<RgFatcaRulesVb>() {
			@Override
			public RgFatcaRulesVb extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					vb.setFatcaPrimaryNationalityFlag(rs.getString("FATCA_PRIMARY_NATIONALITY_FLAG"));
					vb.setFatcaPrimaryDualNationalityFlag(rs.getString("FATCA_PRIMARY_DUAL_NATIONALITY_FLAG"));
					vb.setFatcaPrimaryResidenceFlag(rs.getString("FATCA_PRIMARY_RESIDENCE_FLAG"));
					vb.setFatcaPrimaryDomicileFlag(rs.getString("FATCA_PRIMARY_DOMICILE_FLAG"));
					vb.setFatcaFamilyNationalityFlag(rs.getString("FATCA_FAMILY_NATIONALITY_FLAG"));
					vb.setFatcaFamilyDualNationalityFlag(rs.getString("FATCA_FAMILY_DUAL_NATIONALITY_FLAG"));
					// rgFatcaRulesVb.setFatcaCountryList(rs.getString("FATCA_COUNTRY_LIST"));
					String fatcaClob = rs.getString("FATCA_COUNTRY_LIST");
					vb.setFatcaCountryList(parseCountryList(fatcaClob));
				}
				return vb;
			}
		}, args);
	}

	public void getRgCrsRules(RgFatcaRulesVb vb) {
		String query = "SELECT CRS_PRIMARY_NATIONALITY_FLAG,  CRS_PRIMARY_DUAL_NATIONALITY_FLAG, CRS_PRIMARY_RESIDENCE_FLAG, "
				+ " CRS_PRIMARY_DOMICILE_FLAG, CRS_FAMILY_NATIONALITY_FLAG, CRS_FAMILY_DUAL_NATIONALITY_FLAG, "
				+ getDbFunction(Constants.CLOB_FORMAT, null) + " (CRS_COUNTRY_LIST) CRS_COUNTRY_LIST "
				+ "  FROM RG_FATCA_RULES WHERE RULE_STATUS = 0 AND COUNTRY = ? AND LE_BOOK = ? ";
		Object[] args = { vb.getCountry(), vb.getLeBook() };
		getJdbcTemplate().query(query, new ResultSetExtractor<RgFatcaRulesVb>() {
			@Override
			public RgFatcaRulesVb extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					vb.setCrsPrimaryNationalityFlag(rs.getString("CRS_PRIMARY_NATIONALITY_FLAG"));
					vb.setCrsPrimaryDualNationalityFlag(rs.getString("CRS_PRIMARY_DUAL_NATIONALITY_FLAG"));
					vb.setCrsPrimaryResidenceFlag(rs.getString("CRS_PRIMARY_RESIDENCE_FLAG"));
					vb.setCrsPrimaryDomicileFlag(rs.getString("CRS_PRIMARY_DOMICILE_FLAG"));
					vb.setCrsFamilyNationalityFlag(rs.getString("CRS_FAMILY_NATIONALITY_FLAG"));
					vb.setCrsFamilyDualNationalityFlag(rs.getString("CRS_FAMILY_DUAL_NATIONALITY_FLAG"));
//					rgFatcaRulesVb.setCrsCountryList(rs.getString("CRS_COUNTRY_LIST"));
					String crsClob = rs.getString("CRS_COUNTRY_LIST");
					vb.setCrsCountryList(parseCountryList(crsClob));
				}
				return vb;
			}
		}, args);
	}

	/*
	 * public ExceptionCode getCustomersList(String dateLastExtraction,
	 * RgFatcaRulesVb vb, String ruleFlag) { ExceptionCode exceptionCode = new
	 * ExceptionCode(); int batchSize = 1000; String baselineCondition = ""; if
	 * (ValidationUtil.isValid(dateLastExtraction) &&
	 * !"01-Jan-1900".equalsIgnoreCase(dateLastExtraction)) { baselineCondition =
	 * " AND T1.DATE_LAST_MODIFIED >= '" + dateLastExtraction + "'"; } String sql =
	 * "SELECT T1.COUNTRY, T1.LE_BOOK, T1.CUSTOMER_ID, T1.CB_DOMICILE, " +
	 * "T1.CB_NATIONALITY, T1.CB_RESIDENCE, T1.CRS_FLAG, T1.FATCA_FLAG, " +
	 * "T1.FATCA_OVERRIDE, T1.CRS_OVERRIDE, T2.DUAL_NATIONALITY_1, " +
	 * "T2.DUAL_NATIONALITY_2, T2.DUAL_NATIONALITY_3 FROM " + tableName +
	 * " T1 INNER JOIN " + dualTableName + " T2 " +
	 * "ON T1.COUNTRY = T2.COUNTRY AND T1.LE_BOOK = T2.LE_BOOK " +
	 * "AND T1.CUSTOMER_ID = T2.CUSTOMER_ID " +
	 * "WHERE T1.CUSTOMER_STATUS = 0 AND T1.COUNTRY = ? AND T1.LE_BOOK = ? " +
	 * baselineCondition; Object[] args = { vb.getCountry(), vb.getLeBook() }; try {
	 * getJdbcTemplate().query(sql, new ResultSetExtractor<Void>() {
	 * 
	 * @Override public Void extractData(ResultSet rs) throws SQLException {
	 * List<CustomersVb> currentBatch = new ArrayList<>(); int recordCount = 0; int
	 * batchNumber = 1; while (rs.next()) { CustomersVb custVb = new CustomersVb();
	 * custVb.setCountry(rs.getString("COUNTRY"));
	 * custVb.setLeBook(rs.getString("LE_BOOK"));
	 * custVb.setCustomerId(rs.getString("CUSTOMER_ID"));
	 * custVb.setCbDomicile(rs.getString("CB_DOMICILE"));
	 * custVb.setCbNationality(rs.getString("CB_NATIONALITY"));
	 * custVb.setCbResidence(rs.getString("CB_RESIDENCE"));
	 * custVb.setCrsFlag(rs.getString("CRS_FLAG"));
	 * custVb.setFatcaFlag(rs.getString("FATCA_FLAG"));
	 * custVb.setFatcaOverRide(rs.getString("FATCA_OVERRIDE"));
	 * custVb.setCrsOverRide(rs.getString("CRS_OVERRIDE"));
	 * custVb.setDualNationality1(rs.getString("DUAL_NATIONALITY_1"));
	 * custVb.setDualNationality2(rs.getString("DUAL_NATIONALITY_2"));
	 * custVb.setDualNationality3(rs.getString("DUAL_NATIONALITY_3"));
	 * currentBatch.add(custVb); recordCount++; if (recordCount % batchSize == 0) {
	 * processCustomerBatch(currentBatch, batchNumber, vb, ruleFlag);
	 * currentBatch.clear(); batchNumber++; } } if (!currentBatch.isEmpty()) {
	 * processCustomerBatch(currentBatch, batchNumber, vb, ruleFlag); } return null;
	 * } }, args); exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION); }
	 * catch (Exception e) { logger.error(e.getMessage());
	 * exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
	 * exceptionCode.setErrorMsg(e.getMessage()); } return exceptionCode; }
	 */

	public ExceptionCode getCustomersList(String dateLastExtraction, RgFatcaRulesVb vb, String ruleFlag) {
		ExceptionCode exceptionCode = new ExceptionCode();
		int batchSize = 5000;
		String baselineCondition = "";
		if (ValidationUtil.isValid(dateLastExtraction) && !"01-Jan-1900".equalsIgnoreCase(dateLastExtraction)) {
			baselineCondition = " AND (T1.DATE_LAST_MODIFIED >= ? OR T2.DATE_LAST_MODIFIED >= ?)";
		}
		String sql = "SELECT T1.COUNTRY, T1.LE_BOOK, T1.CUSTOMER_ID, T1.CB_DOMICILE, "
				+ " T1.CB_NATIONALITY, T1.CB_RESIDENCE, T1.CRS_FLAG, T1.FATCA_FLAG, "
				+ " T1.FATCA_OVERRIDE, T1.CRS_OVERRIDE, T2.DUAL_NATIONALITY_1, "
				+ " T2.DUAL_NATIONALITY_2, T2.DUAL_NATIONALITY_3 " + " FROM " + tableName + " T1 INNER JOIN "
				+ dualTableName + " T2 " + " ON T1.COUNTRY = T2.COUNTRY AND T1.LE_BOOK = T2.LE_BOOK "
				+ " AND T1.CUSTOMER_ID = T2.CUSTOMER_ID " + " WHERE T1.COUNTRY = ? AND T1.LE_BOOK = ? "
				+ baselineCondition;
		try {
			getJdbcTemplate().query(con -> {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setFetchSize(batchSize);
				int index = 1;
				ps.setString(index++, vb.getCountry());
				ps.setString(index++, vb.getLeBook());
				if (ValidationUtil.isValid(dateLastExtraction) && !"01-Jan-1900".equalsIgnoreCase(dateLastExtraction)) {
					ps.setString(index++, dateLastExtraction);
					ps.setString(index, dateLastExtraction);
				}
				return ps;
			}, (ResultSetExtractor<Void>) rs -> {
				List<CustomersVb> currentBatch = new ArrayList<>();
				int batchNumber = 1;
				while (rs.next()) {
					CustomersVb custVb = new CustomersVb();
					custVb.setCountry(rs.getString("COUNTRY"));
					custVb.setLeBook(rs.getString("LE_BOOK"));
					custVb.setCustomerId(rs.getString("CUSTOMER_ID"));
					custVb.setCbDomicile(rs.getString("CB_DOMICILE"));
					custVb.setCbNationality(rs.getString("CB_NATIONALITY"));
					custVb.setCbResidence(rs.getString("CB_RESIDENCE"));
					custVb.setCrsFlag(rs.getString("CRS_FLAG"));
					custVb.setFatcaFlag(rs.getString("FATCA_FLAG"));
					custVb.setFatcaOverRide(rs.getString("FATCA_OVERRIDE"));
					custVb.setCrsOverRide(rs.getString("CRS_OVERRIDE"));
					custVb.setDualNationality1(rs.getString("DUAL_NATIONALITY_1"));
					custVb.setDualNationality2(rs.getString("DUAL_NATIONALITY_2"));
					custVb.setDualNationality3(rs.getString("DUAL_NATIONALITY_3"));
					currentBatch.add(custVb);
					if (currentBatch.size() == batchSize) {
						processCustomerBatch(currentBatch, batchNumber++, vb, ruleFlag);
						currentBatch.clear();
					}
				}
				if (!currentBatch.isEmpty()) {
					processCustomerBatch(currentBatch, batchNumber, vb, ruleFlag);
				}
				return null;
			});
			exceptionCode.setErrorCode(Constants.SUCCESSFUL_OPERATION);
		} catch (Exception e) {
			logger.error(e.getMessage());
			exceptionCode.setErrorCode(Constants.ERRONEOUS_OPERATION);
			exceptionCode.setErrorMsg(e.getMessage());
		}
		return exceptionCode;
	}

	private void processCustomerBatch(List<CustomersVb> currentBatch, int batchNumber, RgFatcaRulesVb vb,
			String ruleFlag) {
		logger.info("Batch processing number " + batchNumber + " current batch size " + currentBatch.size());
		if ("FATCA".equalsIgnoreCase(ruleFlag)) {
			applyFatcaRules(currentBatch, vb);
			updateFinalFatcaFlagInDb(vb, currentBatch, batchNumber);
		} else {
			applyCrsRules(currentBatch, vb);
			updateFinalCrsFlagInDb(vb, currentBatch, batchNumber);
		}
	}

	private static Set<String> parseCountryList(String clob) {
		Set<String> result = new HashSet<>();
		if (clob != null) {
			for (String s : clob.split(",")) {
				result.add(s.trim());
			}
		}
		return result;
	}

	private void updateFinalFatcaFlagInDb(RgFatcaRulesVb vb, List<CustomersVb> customers, int batchNumber) {
		try {
			String sql = "UPDATE CUSTOMERS SET FATCA_FLAG = ?, DATE_LAST_MODIFIED =  "+getDbFunction(Constants.SYSDATE, null)
					+ " WHERE COUNTRY = ? AND LE_BOOK = ? AND CUSTOMER_ID = ? ";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					CustomersVb vObject = customers.get(i);
					ps.setString(1, vObject.getFinalFatcaFlag());
					ps.setString(2, vObject.getCountry());
					ps.setString(3, vObject.getLeBook());
					ps.setString(4, vObject.getCustomerId());
				}

				@Override
				public int getBatchSize() {
					return customers.size();
				}
			});
			logger.info("Batch update completed for " + customers.size() + " customers.");
			batchInsertIntoFatcaRulesAudit(vb, customers);
			logger.info("Audit insert completed for the batch number " + batchNumber);
		} catch (Exception e) {
			logger.error("Error during final update: " + e.getMessage(), e);
		}
	}

	private void updateFinalCrsFlagInDb(RgFatcaRulesVb vb, List<CustomersVb> customers, int batchNumber) {
		try {
			String sql = " UPDATE CUSTOMERS SET CRS_FLAG = ?, DATE_LAST_MODIFIED =  "+getDbFunction(Constants.SYSDATE, null)
					+ " WHERE COUNTRY = ? AND LE_BOOK = ? AND CUSTOMER_ID = ? ";
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					CustomersVb vObject = customers.get(i);
					ps.setString(1, vObject.getFinalCrsFlag());
					ps.setString(2, vObject.getCountry());
					ps.setString(3, vObject.getLeBook());
					ps.setString(4, vObject.getCustomerId());
				}

				@Override
				public int getBatchSize() {
					return customers.size();
				}
			});
			logger.info("Batch update completed for " + customers.size() + " customers.");
//			batchInsertIntoFatcaRulesAudit(vb, customers);
			batchInsertIntoCrsRulesAudit(vb, customers);
			logger.info("Audit insert completed for the batch number " + batchNumber);
		} catch (Exception e) {
			logger.error("Error during final update: " + e.getMessage(), e);
		}
	}

	public int getFatcaMaxVersionNo(RgFatcaRulesVb vb) {
		try {
			String sql = "select " + getDbFunction(Constants.NVL, null)
					+ " (MAX(VERSION_NO), 0) VERSION_NO FROM RG_FATCA_RULES_AUDIT "
					+ " WHERE COUNTRY = ? AND  LE_BOOK = ? AND RUN_DATE = ? ";
			Object[] args = { vb.getCountry(), vb.getLeBook(), vb.getRunDate() };
			return getJdbcTemplate().queryForObject(sql, Integer.class, args);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return -1;
		}
	}

	public int getCrsMaxVersionNo(RgFatcaRulesVb vb) {
		try {
			String sql = "select " + getDbFunction(Constants.NVL, null)
					+ " (MAX(VERSION_NO), 0) VERSION_NO FROM RG_CRS_RULES_AUDIT "
					+ " WHERE COUNTRY = ? AND  LE_BOOK = ? AND RUN_DATE = ? ";
			Object[] args = { vb.getCountry(), vb.getLeBook(), vb.getRunDate() };
			return getJdbcTemplate().queryForObject(sql, Integer.class, args);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return -1;
		}
	}

	/*
	 * private void insertIntoFatcaRulesAudit(RgFatcaRulesVb vb, CustomersVb
	 * customersVb) { Connection conn = null; PreparedStatement ps = null; try {
	 * String sql =
	 * "INSERT INTO RG_FATCA_RULES_AUDIT (COUNTRY, LE_BOOK, RUN_DATE, VERSION_NO, CUSTOMER_ID, "
	 * +
	 * "NATIONALITY, RESIDENCE, DOMICILE, DUAL_NATIONALITY_1, DUAL_NATIONALITY_2, DUAL_NATIONALITY_3, "
	 * + "FATCA_PRIMARY_NATIONALITY_FLAG, FATCA_PRIMARY_DUAL_NATIONALITY_FLAG, " +
	 * "FATCA_PRIMARY_RESIDENCE_FLAG, FATCA_PRIMARY_DOMICILE_FLAG, " +
	 * "FATCA_FLAG, FATCA_OVERRIDE, FINAL_FATCA_FLAG, " +
	 * "RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, " +
	 * "DATE_LAST_MODIFIED, DATE_CREATION, FATCA_COUNTRY_LIST) VALUES (" +
	 * "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
	 * getDbFunction(Constants.SYSDATE, null) + ", " +
	 * getDbFunction(Constants.SYSDATE, null) + ", ?)"; Object[] args = {
	 * vb.getCountry(), vb.getLeBook(), vb.getRunDate(), vb.getFatcaVersionNumber(),
	 * customersVb.getCustomerId(), customersVb.getCbNationality(),
	 * customersVb.getCbResidence(), customersVb.getCbDomicile(),
	 * customersVb.getDualNationality1(), customersVb.getDualNationality2(),
	 * customersVb.getDualNationality3(), vb.getFatcaPrimaryNationalityFlag(),
	 * vb.getFatcaPrimaryDualNationalityFlag(), vb.getFatcaPrimaryResidenceFlag(),
	 * vb.getFatcaPrimaryDomicileFlag(), customersVb.getFatcaFlag(),
	 * customersVb.getFatcaOverRide(), customersVb.getFatcaFlag(),
	 * vb.getRecordIndicatorNt(), vb.getRecordIndicator(), vb.getMaker(),
	 * vb.getVerifier(), vb.getInternalStatus() }; conn =
	 * getJdbcTemplate().getDataSource().getConnection(); ps =
	 * conn.prepareStatement(sql); for (int i = 0; i < args.length; i++) {
	 * ps.setObject(i + 1, args[i]); } String fatcaCountries = String.join(",",
	 * vb.getFatcaCountryList()); if (fatcaCountries == null) fatcaCountries = "";
	 * ps.setCharacterStream(args.length + 1, new StringReader(fatcaCountries),
	 * fatcaCountries.length()); ps.executeUpdate(); } catch (Exception e) {
	 * logger.info("Exception in inserting Fatca Audit " + e.getMessage()); }
	 * finally { try { if (ps != null) ps.close(); if (conn != null) conn.close(); }
	 * catch (SQLException e) { logger.error(e.getMessage()); } } }
	 */

	public void batchInsertIntoFatcaRulesAudit(RgFatcaRulesVb vb, List<CustomersVb> customers) {
		String sql = "INSERT INTO RG_FATCA_RULES_AUDIT (COUNTRY, LE_BOOK, RUN_DATE, VERSION_NO, CUSTOMER_ID, "
				+ "NATIONALITY, RESIDENCE, DOMICILE, DUAL_NATIONALITY_1, DUAL_NATIONALITY_2, DUAL_NATIONALITY_3, "
				+ "FATCA_PRIMARY_NATIONALITY_FLAG, FATCA_PRIMARY_DUAL_NATIONALITY_FLAG, "
				+ "FATCA_PRIMARY_RESIDENCE_FLAG, FATCA_PRIMARY_DOMICILE_FLAG, "
				+ "FATCA_FLAG, FATCA_OVERRIDE, FINAL_FATCA_FLAG, "
				+ "RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, MATCH_REFERENCE, "
				+ "DATE_LAST_MODIFIED, DATE_CREATION, FATCA_COUNTRY_LIST) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ getDbFunction(Constants.SYSDATE, null) + ", " + getDbFunction(Constants.SYSDATE, null) + ", ?)";
		try {
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					CustomersVb customersVb = customers.get(i);
					Object[] args = { vb.getCountry(), vb.getLeBook(), vb.getRunDate(), vb.getFatcaVersionNumber(),
							customersVb.getCustomerId(), customersVb.getCbNationality(), customersVb.getCbResidence(),
							customersVb.getCbDomicile(), customersVb.getDualNationality1(),
							customersVb.getDualNationality2(), customersVb.getDualNationality3(),
							vb.getFatcaPrimaryNationalityFlag(), vb.getFatcaPrimaryDualNationalityFlag(),
							vb.getFatcaPrimaryResidenceFlag(), vb.getFatcaPrimaryDomicileFlag(),
							customersVb.getFatcaFlag(), customersVb.getFatcaOverRide(), customersVb.getFinalFatcaFlag(),
							vb.getRecordIndicatorNt(), vb.getRecordIndicator(), vb.getMaker(), vb.getVerifier(),
							vb.getInternalStatus(), customersVb.getFatcaMatchRef() };
					for (int j = 0; j < args.length; j++) {
						ps.setObject(j + 1, args[j]);
					}
					String fatcaCountries = String.join(",", vb.getFatcaCountryList());
					if (fatcaCountries == null)
						fatcaCountries = "";
					ps.setCharacterStream(args.length + 1, new StringReader(fatcaCountries), fatcaCountries.length());
				}
				@Override
				public int getBatchSize() {
					return customers.size();
				}
			});
		} catch (Exception e) {
			logger.error("Exception in batchInsertIntoFatcaRulesAudit: " + e.getMessage(), e);
		}
	}

	public void batchInsertIntoCrsRulesAudit(RgFatcaRulesVb vb, List<CustomersVb> customers) {
		String sql = "INSERT INTO RG_CRS_RULES_AUDIT (COUNTRY, LE_BOOK, RUN_DATE, VERSION_NO, CUSTOMER_ID, "
				+ "NATIONALITY, RESIDENCE, DOMICILE, DUAL_NATIONALITY_1, DUAL_NATIONALITY_2, DUAL_NATIONALITY_3, "
				+ "CRS_PRIMARY_NATIONALITY_FLAG, CRS_PRIMARY_DUAL_NATIONALITY_FLAG, "
				+ "CRS_PRIMARY_RESIDENCE_FLAG, CRS_PRIMARY_DOMICILE_FLAG, CRS_FLAG, CRS_OVERRIDE, FINAL_CRS_FLAG, "
				+ "RECORD_INDICATOR_NT, RECORD_INDICATOR, MAKER, VERIFIER, INTERNAL_STATUS, MATCH_REFERENCE, "
				+ "DATE_LAST_MODIFIED, DATE_CREATION, CRS_COUNTRY_LIST) VALUES ("
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ getDbFunction(Constants.SYSDATE, null) + ", " + getDbFunction(Constants.SYSDATE, null) + ", ?)";
		try {
			getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					CustomersVb customersVb = customers.get(i);
					Object[] args = { vb.getCountry(), vb.getLeBook(), vb.getRunDate(), vb.getCrsVersionNumber(),
							customersVb.getCustomerId(), customersVb.getCbNationality(), customersVb.getCbResidence(),
							customersVb.getCbDomicile(), customersVb.getDualNationality1(),
							customersVb.getDualNationality2(), customersVb.getDualNationality3(),
							vb.getCrsPrimaryNationalityFlag(), vb.getCrsPrimaryDualNationalityFlag(),
							vb.getCrsPrimaryResidenceFlag(), vb.getCrsPrimaryDomicileFlag(), customersVb.getCrsFlag(),
							customersVb.getCrsOverRide(), customersVb.getFinalCrsFlag(), vb.getRecordIndicatorNt(),
							vb.getRecordIndicator(), vb.getMaker(), vb.getVerifier(), vb.getInternalStatus(),
							customersVb.getCrsMatchRef()};
					for (int j = 0; j < args.length; j++) {
						ps.setObject(j + 1, args[j]);
					}
					String crsCountries = String.join(",", vb.getCrsCountryList());
					if (crsCountries == null)
						crsCountries = "";
					ps.setCharacterStream(args.length + 1, new StringReader(crsCountries), crsCountries.length());
				}

				@Override
				public int getBatchSize() {
					return customers.size();
				}
			});
		} catch (Exception e) {
			logger.error("Exception in batchInsertIntoCrsRulesAudit: " + e.getMessage(), e);
		}
	}

	private void applyFatcaRules(List<CustomersVb> customers, RgFatcaRulesVb rule) {
		boolean match = false;
		
		for (CustomersVb customerVb : customers) {
			StringJoiner matchRef =new StringJoiner(",");
			match = false;
			
			if ("Y".equalsIgnoreCase(rule.getFatcaPrimaryNationalityFlag())
					&& rule.getFatcaCountryList().contains(customerVb.getCbNationality())) {
				matchRef.add("Nationality");
				match = true;
			}
			if ("Y".equalsIgnoreCase(rule.getFatcaPrimaryDomicileFlag())
					&& rule.getFatcaCountryList().contains(customerVb.getCbDomicile())) {
				matchRef.add("Domicile");
				match = true;
			}
			if ("Y".equalsIgnoreCase(rule.getFatcaPrimaryResidenceFlag())
					&& rule.getFatcaCountryList().contains(customerVb.getCbResidence())) {
				matchRef.add("Residence");
				match = true;
				
			}
			if ("Y".equalsIgnoreCase(rule.getFatcaPrimaryDualNationalityFlag())) {
				if (rule.getFatcaCountryList().contains(customerVb.getDualNationality1())){
					matchRef.add("DualNationality1");
					match = true;
				}
				
				if (rule.getFatcaCountryList().contains(customerVb.getDualNationality2())){
					matchRef.add("DualNationality2");
					match = true;
				}

				if (rule.getFatcaCountryList().contains(customerVb.getDualNationality3())){
					matchRef.add("DualNationality3");
					match = true;
				}
			}
			
			customerVb.setFatcaMatchRef(matchRef.toString());

			customerVb.setFatcaFlag(match ? "Y" : "N");
			if ("I".equalsIgnoreCase(customerVb.getFatcaOverRide())) {
				customerVb.setFinalFatcaFlag("Y");
			} else if ("E".equalsIgnoreCase(customerVb.getFatcaOverRide())) {
				customerVb.setFinalFatcaFlag("N");
			}
		}
	}

	private void applyCrsRules(List<CustomersVb> customers, RgFatcaRulesVb rule) {
		boolean match = false;

		for (CustomersVb customerVb : customers) {
			StringJoiner matchRef =new StringJoiner(",");
			
			match = false;
			if ("Y".equalsIgnoreCase(rule.getCrsPrimaryNationalityFlag())
					&& rule.getCrsCountryList().contains(customerVb.getCbNationality())) {
				match = true;
				matchRef.add("Nationality");
			}
			if ("Y".equalsIgnoreCase(rule.getCrsPrimaryDomicileFlag())
					&& rule.getCrsCountryList().contains(customerVb.getCbDomicile())) {
				matchRef.add("Domicile");
				match = true;
			}
			if ("Y".equalsIgnoreCase(rule.getCrsPrimaryResidenceFlag())
					&& rule.getCrsCountryList().contains(customerVb.getCbResidence())) {
				matchRef.add("Residence");
				match = true;
			}
			if ("Y".equalsIgnoreCase(rule.getCrsPrimaryDualNationalityFlag())) {
				if (rule.getCrsCountryList().contains(customerVb.getDualNationality1())) {
					matchRef.add("DualNationality1");
					match = true;
				}

				if (rule.getCrsCountryList().contains(customerVb.getDualNationality2())) {
					matchRef.add("DualNationality2");
					match = true;
				}

				if (rule.getCrsCountryList().contains(customerVb.getDualNationality3())) {
					matchRef.add("DualNationality3");
					match = true;
				}
			}
			
			customerVb.setCrsMatchRef(matchRef.toString());
			
			customerVb.setCrsFlag(match ? "Y" : "N");
			if ("I".equalsIgnoreCase(customerVb.getCrsOverRide())) {
				customerVb.setFinalCrsFlag("Y");
			} else if ("E".equalsIgnoreCase(customerVb.getCrsOverRide())) {
				customerVb.setFinalCrsFlag("N");
			}
		}
	}

}
