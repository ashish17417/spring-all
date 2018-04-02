package com.ideathon.sign.idver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.springframework.stereotype.Service;

import com.ideathon.sign.idver.domain.OriginalBlueprint;
import com.ideathon.sign.idver.domain.VerificationResult;

@Service
public class ValidateSignaturesService {

	public ValidateSignaturesService() {
		File lib = null;
		String os = System.getProperty("os.name");
		String bitness = System.getProperty("sun.arch.data.model");

		if (os.toUpperCase().contains("WINDOWS")) {
			if (bitness.endsWith("64")) {
				lib = new File("libs//" + System.mapLibraryName("opencv_java2411"));
			} else {
				lib = new File("libs//x86//" + System.mapLibraryName("opencv_java2411"));
			}
		}
		System.out.println(lib.getAbsolutePath());
		System.load(lib.getAbsolutePath());

		featureDetector = FeatureDetector.create(FeatureDetector.SURF);
		descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
		descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);

	}

	private FeatureDetector featureDetector;
	private DescriptorExtractor descriptorExtractor;
	private DescriptorMatcher descriptorMatcher;

	private final static String ORIGINAL_PATH = "originals/";
	private final static String TEST_SUBJECT_PATH = "upload-dir/";
	private float nndrRatio = 0.7f;

	public List<VerificationResult> verifyOriginalVsTestSubjects(String originalPath, String testSubjectPath) {
		OriginalBlueprint originalBlueprint = createBlueprintForInput(ORIGINAL_PATH + originalPath);
		List<VerificationResult> verificationResult = null;
		try {
			verificationResult = verifyTestSubjects(TEST_SUBJECT_PATH + testSubjectPath, originalBlueprint);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return verificationResult;
	}

	private List<VerificationResult> verifyTestSubjects(String string, OriginalBlueprint originalBlueprint)
			throws FileNotFoundException, IOException {
		List<VerificationResult> returnList = new ArrayList<>();
		File dir = null;
		if (new ZipInputStream(new FileInputStream(string)).getNextEntry() != null) {
			System.out.println("Incoming is zip file" + string);
			String id = UUID.randomUUID().toString();
			// isConpresed
			unzip(string, "./tmp/" + id + "/");

			// dir = new File("./tmp/" + id + "/");

			Files.walk(Paths.get(new File("./tmp/" + id + "/").toURI())).forEach(file -> {
				matchIndividualFIle(originalBlueprint, returnList, file);
			});

		} else {
			System.out.println("Incoming is not zip file" + string);
			returnList.add(matchEachFile(originalBlueprint, string));
		}

		if (dir != null)
			dir.delete();

		return returnList;
	}

	private boolean matchIndividualFIle(OriginalBlueprint originalBlueprint, List<VerificationResult> returnList,
			Path filePath) {
		System.out.println(filePath.toFile().getAbsolutePath());
		if (filePath.toFile().isDirectory()) {
			return false;
		}
		return returnList.add(matchEachFile(originalBlueprint, filePath.toFile().getAbsolutePath()));
	}

	private VerificationResult matchEachFile(OriginalBlueprint originalBlueprint, String bookScene) {

		System.out.println("Now Matching " + bookScene);
		Mat sceneImage = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);
		MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
		MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
		featureDetector.detect(sceneImage, sceneKeyPoints);
		descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);
		Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
		Scalar matchestColor = new Scalar(0, 255, 0);

		List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
		descriptorMatcher.knnMatch(originalBlueprint.getObjectDescriptors(), sceneDescriptors, matches, 2);

		System.out.println("Calculating good match list...");
		LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

		for (int i = 0; i < matches.size(); i++) {
			MatOfDMatch matofDMatch = matches.get(i);
			DMatch[] dmatcharray = matofDMatch.toArray();
			DMatch m1 = dmatcharray[0];
			DMatch m2 = dmatcharray[1];

			if (m1.distance <= m2.distance * nndrRatio) {
				goodMatchesList.addLast(m1);

			}

		}

		if (goodMatchesList.size() >= 7) {
			System.out.println("Object Found!!! for" + bookScene + " with match count " + goodMatchesList.size());
			return new VerificationResult(originalBlueprint.getOriginalPath(),
					bookScene.substring(bookScene.lastIndexOf("\\")), true, goodMatchesList.size());

		} else {
			// System.out.println("Object Not Found");
			return new VerificationResult(originalBlueprint.getOriginalPath(), bookScene, false,
					goodMatchesList.size());
		}

	}

	private OriginalBlueprint createBlueprintForInput(String originalPath) {

		Mat objectImage = Highgui.imread(originalPath, Highgui.CV_LOAD_IMAGE_COLOR);
		MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
		featureDetector.detect(objectImage, objectKeyPoints);
		KeyPoint[] keypoints = objectKeyPoints.toArray();
		MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
		descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);
		Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
		Scalar newKeypointColor = new Scalar(255, 0, 0);
		Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

		return new OriginalBlueprint(originalPath, objectImage, objectKeyPoints, objectDescriptors, newKeypointColor);
	}

	private void unzip(String zipFilePath, String destDir) {
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
				// System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
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

}
