package com.ideathon.sign.idver.domain;

public class VerificationResult {

	private String originalSignature;
	private String testSubjectDocument;
	private boolean matched;
	private double matchCount;
	
	public double getMatchCount() {
		return matchCount;
	}
	public void setMatchCount(double matchCount) {
		this.matchCount = matchCount;
	}
	public VerificationResult(String originalSignature, String testSubjectDocument, boolean matched, double matchCount) {
		this.originalSignature = originalSignature;
		this.testSubjectDocument = testSubjectDocument;
		this.matched = matched;
		this.matchCount = matchCount;
	}
	public String getOriginalSignature() {
		return originalSignature;
	}
	public void setOriginalSignature(String originalSignature) {
		this.originalSignature = originalSignature;
	}
	public String getTestSubjectDocument() {
		return testSubjectDocument;
	}
	public void setTestSubjectDocument(String testSubjectDocument) {
		this.testSubjectDocument = testSubjectDocument;
	}
	public boolean isMatched() {
		return matched;
	}
	public void setMatched(boolean matched) {
		this.matched = matched;
	}
	
	
}
