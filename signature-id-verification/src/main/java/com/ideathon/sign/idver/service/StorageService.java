package com.ideathon.sign.idver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ideathon.sign.idver.ApplicationProperties;
 
@Service
@EnableConfigurationProperties(value = {ApplicationProperties.class})
public class StorageService {
 
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation;
	private final Path originalLocation;
    
    public StorageService(ApplicationProperties applicationProperties) {
		rootLocation = Paths.get(applicationProperties.getStorageUploadDir());
		originalLocation = Paths.get(applicationProperties.getStorageOriginalDir());
	}

	public void store(MultipartFile file) {
		deleteAll();
		init();
		try {
			Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("FAIL!");
		}
	}
 
	public void storeOriginal(MultipartFile file) {
		
		try {
			Files.copy(file.getInputStream(), this.originalLocation.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("FAIL!");
		}
	}
	
	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}
 
	public void deleteAll() {
		log.info("Deleting all previous files");
		try {
			deleteDirectory(rootLocation.toFile());
		} catch (Exception e) {
			log.warn("Error occurred while deleting existing files !!", e.getMessage());
		}
	}
 
	public void init() {
		try {
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unzip(String zipFilePath, String destDir) {
		File dir = new File(destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	static public boolean deleteDirectory(File path) {
	    if (path.exists()) {
	        File[] files = path.listFiles();
	        for (int i = 0; i < files.length; i++) {
	            if (files[i].isDirectory()) {
	                deleteDirectory(files[i]);
	            } else {
	                files[i].delete();
	            }
	        }
	    }
	    return (path.delete());
	}
}