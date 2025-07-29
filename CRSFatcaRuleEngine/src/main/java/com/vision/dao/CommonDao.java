package com.vision.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommonDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public Connection getConnection() throws SQLException {
		return jdbcTemplate.getDataSource().getConnection();
	}
	
	public static String databaseType;

	@Value("${app.databaseType}")
	public void setDatabaseType(String privateName) {
		CommonDao.databaseType = privateName;
	}
	
	public static final Logger logger = LoggerFactory.getLogger(CommonDao.class);
	
	public static String getDbFunction(String reqFunction, String val) {

		String functionName = "";
		try {
			if ("MSSQL".equalsIgnoreCase(databaseType)) {
				switch (reqFunction) {
				case "DATEFUNC":
					functionName = "FORMAT";
					break;
				case "SYSDATE":
					functionName = "GetDate()";
					break;
				case "NVL":
					functionName = "ISNULL";
					break;
				case "TIME":
					functionName = "HH:mm:ss";
					break;
				case "DD_Mon_RRRR":
					functionName = "dd-MMM-yyyy";
					break;
				case "DD_MM_YYYY":
					functionName = "dd-MM-yyyy";
					break;
				case "CONVERT":
					functionName = "CONVERT";
					break;
				case "TYPE":
					functionName = "varchar,";
					break;
				case "TIMEFORMAT":
					functionName = "108";
					break;
				case "PIPELINE":
					functionName = "+";
					break;
				case "TO_DATE":
					functionName = "CONVERT (datetime,'" + val + "', 103) ";
					break;
				case "LENGTH":
					functionName = "len";
					break;
				case "SUBSTR":
					functionName = "SUBSTRING";
					break;
				case "TO_NUMBER":
					functionName = "cast(" + val + " AS INT)";
					break;
				case "TO_CHAR":
					functionName = "cast(" + val + " AS varchar(4000))";
					break;
				case "DUAL":
					functionName = null;
					break;
				case "SYSTIMESTAMP":
					functionName = "SYSDATETIME()";
					break;
				case "SYSTIMESTAMP_FORMAT":
					functionName = "yyyyMMddHHmmssffffff";
					break;
				case "TO_DATE_NO_TIMESTAMP":
					functionName = "CONVERT(VARCHAR, " + val + ", 105)";
					break;
				case "TO_DATE_NO_TIMESTAMP_VAL":
					functionName = "CONVERT(VARCHAR, '" + val + "', 105) ";
					break;
				case "FN_UNIX_TIME_TO_DATE":
					functionName = "[dbo].[FN_UNIX_TIME_TO_DATE]";
					break;
				case "CLOB_FORMAT":
					functionName = "";
					break;
				}
			} else if ("ORACLE".equalsIgnoreCase(databaseType)) {
				switch (reqFunction) {
				case "DATEFUNC":
					functionName = "TO_CHAR";
					break;
				case "SYSDATE":
					functionName = "SYSDATE";
					break;
				case "NVL":
					functionName = "NVL";
					break;
				case "TIME":
					functionName = "HH24:MI:SS";
					break;
				case "DD_Mon_RRRR":
					functionName = "DD-Mon-RRRR";
					break;
				case "DD_MM_YYYY":
					functionName = "DD-MM-YYYY";
					break;
				case "CONVERT":
					functionName = "TO_CHAR";
					break;
				case "TYPE":
					functionName = "";
					break;
				case "TIMEFORMAT":
					functionName = "'HH:MM:SS'";
					break;
				case "PIPELINE":
					functionName = "||";
					break;
				case "TO_DATE":
					functionName = "TO_DATE ('" + val + "', 'DD-Mon-YYYY HH24:MI:SS') ";
					break;
				case "LENGTH":
					functionName = "LENGTH";
					break;
				case "SUBSTR":
					functionName = "SUBSTR";
					break;
				case "TO_NUMBER":
					functionName = "TO_NUMBER(" + val + ")";
					break;
				case "TO_CHAR":
					functionName = "to_char (" + val + ")";
					break;
				case "DUAL":
					functionName = "FROM DUAL";
					break;
				case "SYSTIMESTAMP":
					functionName = "SYSTIMESTAMP";
					break;
				case "SYSTIMESTAMP_FORMAT":
					functionName = "yyyymmddhh24missff";
					break;
				case "TO_DATE_NO_TIMESTAMP":
					functionName = "TO_DATE(" + val + ", 'DD-MON-RRRR')";
					break;
				case "TO_DATE_NO_TIMESTAMP_VAL":
					functionName = "TO_DATE('" + val + "', 'DD-MM-RRRR') ";
					break;
				case "FN_UNIX_TIME_TO_DATE":
					functionName = "FN_UNIX_TIME_TO_DATE";
					break;
				case "CLOB_FORMAT":
					functionName = "TO_CHAR";
					break;
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return functionName;
	}

}
