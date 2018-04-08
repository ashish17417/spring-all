package com.ideathon.sign.idver.domain;

public class VerificationResult {

	private String originalSignature;
	private String testSubjectDocument;
	private boolean matched;
	private double matchCount;
    private String base64representation;
    private String ragStatus;
    
	public VerificationResult(
			String originalSignature, 
			String testSubjectDocument, 
			boolean matched,
			double matchCount,
			String base64representation,
			String ragStatus) {
		this.originalSignature = originalSignature;
		this.testSubjectDocument = testSubjectDocument;
		this.matched = matched;
		this.matchCount = matchCount;
		this.base64representation = base64representation;
		this.ragStatus = ragStatus;
	}

	public double getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(double matchCount) {
		this.matchCount = matchCount;
	}

	
	public String getRagStatus() {
		return ragStatus;
	}

	public void setRagStatus(String ragStatus) {
		this.ragStatus = ragStatus;
	}

	
	public String getBase64representation() {
		return base64representation;
	}

	public void setBase64representation(String base64representation) {
		this.base64representation = base64representation;
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

	@Override
	public String toString() {
		return "VerificationResult [originalSignature=" + originalSignature + ", testSubjectDocument="
				+ testSubjectDocument + ", matched=" + matched + ", matchCount=" + matchCount + "]";
	}

	
}
