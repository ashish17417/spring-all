package com.ideathon.sign.idver;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.properties")
public class ApplicationProperties {

	private String storageOriginalDir;
	private String storageUploadDir;
	private String storageUnzipPath;
	private String storageLibsX86;
	private String storageLibsX64;
	private String dll;
	private int matchIndexThreshold;
	
	public int getMatchIndexThreshold() {
		return matchIndexThreshold;
	}
	public void setMatchIndexThreshold(int matchIndexThreshold) {
		this.matchIndexThreshold = matchIndexThreshold;
	}
	public String getDll() {
		return dll;
	}
	public void setDll(String dll) {
		this.dll = dll;
	}
	public String getStorageOriginalDir() {
		return storageOriginalDir;
	}
	public void setStorageOriginalDir(String storageOriginalDir) {
		this.storageOriginalDir = storageOriginalDir;
	}
	public String getStorageUploadDir() {
		return storageUploadDir;
	}
	public void setStorageUploadDir(String storageUploadDir) {
		this.storageUploadDir = storageUploadDir;
	}
	public String getStorageUnzipPath() {
		return storageUnzipPath;
	}
	public void setStorageUnzipPath(String storageUnzipPath) {
		this.storageUnzipPath = storageUnzipPath;
	}
	public String getStorageLibsX86() {
		return storageLibsX86;
	}
	public void setStorageLibsX86(String storageLibsX86) {
		this.storageLibsX86 = storageLibsX86;
	}
	public String getStorageLibsX64() {
		return storageLibsX64;
	}
	public void setStorageLibsX64(String storageLibsX64) {
		this.storageLibsX64 = storageLibsX64;
	}
    
	
}
