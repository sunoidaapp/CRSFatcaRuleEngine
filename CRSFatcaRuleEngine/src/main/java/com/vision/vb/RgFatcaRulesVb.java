package com.vision.vb;

import java.util.HashSet;
import java.util.Set;

public class RgFatcaRulesVb extends CommonVb {

	private String fatcaPrimaryNationalityFlag = "";
	private String fatcaPrimaryDualNationalityFlag = "";
	private String fatcaPrimaryResidenceFlag = "";
	private String fatcaPrimaryDomicileFlag = "";
	private String fatcaFamilyNationalityFlag = "";
	private String fatcaFamilyDualNationalityFlag = "";
	private Set<String> fatcaCountryList = new HashSet<>();
	private String crsPrimaryNationalityFlag = "";
	private String crsPrimaryDualNationalityFlag = "";
	private String crsPrimaryResidenceFlag = "";
	private String crsPrimaryDomicileFlag = "";
	private String crsFamilyNationalityFlag = "";
	private String crsFamilyDualNationalityFlag = "";
	private Set<String> crsCountryList = new HashSet<>();
	private int ruleStatusNt = 0;
	private int ruleStatus = 0;
	private int fatcaCountryListcnt = 0;
	private int crsCountryListcnt = 0;
	private String runDate = "";
	private int fatcaVersionNumber = 0;
	private int crsVersionNumber = 0;

	public int getFatcaCountryListcnt() {
		return fatcaCountryListcnt;
	}

	public void setFatcaCountryListcnt(int fatcaCountryListcnt) {
		this.fatcaCountryListcnt = fatcaCountryListcnt;
	}

	public int getCrsCountryListcnt() {
		return crsCountryListcnt;
	}

	public void setCrsCountryListcnt(int crsCountryListcnt) {
		this.crsCountryListcnt = crsCountryListcnt;
	}

	private String ruleStatusDesc = "";

	public String getRuleStatusDesc() {
		return ruleStatusDesc;
	}

	public void setRuleStatusDesc(String ruleStatusDesc) {
		this.ruleStatusDesc = ruleStatusDesc;
	}

	public void setFatcaPrimaryNationalityFlag(String fatcaPrimaryNationalityFlag) {
		this.fatcaPrimaryNationalityFlag = fatcaPrimaryNationalityFlag;
	}

	public String getFatcaPrimaryNationalityFlag() {
		return fatcaPrimaryNationalityFlag;
	}

	public void setFatcaPrimaryDualNationalityFlag(String fatcaPrimaryDualNationalityFlag) {
		this.fatcaPrimaryDualNationalityFlag = fatcaPrimaryDualNationalityFlag;
	}

	public String getFatcaPrimaryDualNationalityFlag() {
		return fatcaPrimaryDualNationalityFlag;
	}

	public void setFatcaPrimaryResidenceFlag(String fatcaPrimaryResidenceFlag) {
		this.fatcaPrimaryResidenceFlag = fatcaPrimaryResidenceFlag;
	}

	public String getFatcaPrimaryResidenceFlag() {
		return fatcaPrimaryResidenceFlag;
	}

	public void setFatcaPrimaryDomicileFlag(String fatcaPrimaryDomicileFlag) {
		this.fatcaPrimaryDomicileFlag = fatcaPrimaryDomicileFlag;
	}

	public String getFatcaPrimaryDomicileFlag() {
		return fatcaPrimaryDomicileFlag;
	}

	public void setFatcaFamilyNationalityFlag(String fatcaFamilyNationalityFlag) {
		this.fatcaFamilyNationalityFlag = fatcaFamilyNationalityFlag;
	}

	public String getFatcaFamilyNationalityFlag() {
		return fatcaFamilyNationalityFlag;
	}

	public void setFatcaFamilyDualNationalityFlag(String fatcaFamilyDualNationalityFlag) {
		this.fatcaFamilyDualNationalityFlag = fatcaFamilyDualNationalityFlag;
	}

	public String getFatcaFamilyDualNationalityFlag() {
		return fatcaFamilyDualNationalityFlag;
	}

	public Set<String> getFatcaCountryList() {
		return fatcaCountryList;
	}

	public void setFatcaCountryList(Set<String> fatcaCountryList) {
		this.fatcaCountryList = fatcaCountryList;
	}

	public void setCrsPrimaryNationalityFlag(String crsPrimaryNationalityFlag) {
		this.crsPrimaryNationalityFlag = crsPrimaryNationalityFlag;
	}

	public String getCrsPrimaryNationalityFlag() {
		return crsPrimaryNationalityFlag;
	}

	public void setCrsPrimaryDualNationalityFlag(String crsPrimaryDualNationalityFlag) {
		this.crsPrimaryDualNationalityFlag = crsPrimaryDualNationalityFlag;
	}

	public String getCrsPrimaryDualNationalityFlag() {
		return crsPrimaryDualNationalityFlag;
	}

	public void setCrsPrimaryResidenceFlag(String crsPrimaryResidenceFlag) {
		this.crsPrimaryResidenceFlag = crsPrimaryResidenceFlag;
	}

	public String getCrsPrimaryResidenceFlag() {
		return crsPrimaryResidenceFlag;
	}

	public void setCrsPrimaryDomicileFlag(String crsPrimaryDomicileFlag) {
		this.crsPrimaryDomicileFlag = crsPrimaryDomicileFlag;
	}

	public String getCrsPrimaryDomicileFlag() {
		return crsPrimaryDomicileFlag;
	}

	public void setCrsFamilyNationalityFlag(String crsFamilyNationalityFlag) {
		this.crsFamilyNationalityFlag = crsFamilyNationalityFlag;
	}

	public String getCrsFamilyNationalityFlag() {
		return crsFamilyNationalityFlag;
	}

	public void setCrsFamilyDualNationalityFlag(String crsFamilyDualNationalityFlag) {
		this.crsFamilyDualNationalityFlag = crsFamilyDualNationalityFlag;
	}

	public String getCrsFamilyDualNationalityFlag() {
		return crsFamilyDualNationalityFlag;
	}

	public Set<String> getCrsCountryList() {
		return crsCountryList;
	}

	public void setCrsCountryList(Set<String> crsCountryList) {
		this.crsCountryList = crsCountryList;
	}

	public void setRuleStatusNt(int ruleStatusNt) {
		this.ruleStatusNt = ruleStatusNt;
	}

	public int getRuleStatusNt() {
		return ruleStatusNt;
	}

	public void setRuleStatus(int ruleStatus) {
		this.ruleStatus = ruleStatus;
	}

	public int getRuleStatus() {
		return ruleStatus;
	}

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
	}

	public int getFatcaVersionNumber() {
		return fatcaVersionNumber;
	}

	public void setFatcaVersionNumber(int fatcaVersionNumber) {
		this.fatcaVersionNumber = fatcaVersionNumber;
	}

	public int getCrsVersionNumber() {
		return crsVersionNumber;
	}

	public void setCrsVersionNumber(int crsVersionNumber) {
		this.crsVersionNumber = crsVersionNumber;
	}

}