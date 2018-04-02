package com.ideathon.sign.idver.domain;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;

public class OriginalBlueprint {
	String originalPath;
	Mat objectImage;
	MatOfKeyPoint objectKeyPoints;
	MatOfKeyPoint objectDescriptors;
	Scalar newKeypointColor;
	
	public OriginalBlueprint(String originalPath, Mat objectImage, MatOfKeyPoint objectKeyPoints, MatOfKeyPoint objectDescriptors, Scalar newKeypointColor) {
		this.objectImage = objectImage;
		this.objectKeyPoints = objectKeyPoints;
		this.objectDescriptors = objectDescriptors;
	}
	public Mat getObjectImage() {
		return objectImage;
	}
	public void setObjectImage(Mat objectImage) {
		this.objectImage = objectImage;
	}
	public MatOfKeyPoint getObjectKeyPoints() {
		return objectKeyPoints;
	}
	public void setObjectKeyPoints(MatOfKeyPoint objectKeyPoints) {
		this.objectKeyPoints = objectKeyPoints;
	}
	public MatOfKeyPoint getObjectDescriptors() {
		return objectDescriptors;
	}
	public void setObjectDescriptors(MatOfKeyPoint objectDescriptors) {
		this.objectDescriptors = objectDescriptors;
	}
	public Scalar getNewKeypointColor() {
		return newKeypointColor;
	}
	public void setNewKeypointColor(Scalar newKeypointColor) {
		this.newKeypointColor = newKeypointColor;
	}
	public String getOriginalPath() {
		return originalPath;
	}
	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}
	
	
}
