package com.spider.tests;

/**
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-30
 */

import com.spider.modules.spider.config.Tesseract;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OCRTest {

	public static void main(String[] args) throws TesseractException {
		System.setProperty("TESSDATA_PREFIX",Thread.currentThread().getContextClassLoader().getResource("").getPath()+"Tessdata");
		System.out.println(System.getProperty("TESSDATA_PREFIX"));
		ITesseract instance = new Tesseract();
		File imgDir = new File("F:/JavaSource/image");
		//对img_data文件夹中的每个验证码进行识别
		//文件名即正确的结果
		for (File imgFile : imgDir.listFiles()) {
			//该例子输入的是文件，也可输入BufferedImage
			String ocrResult = instance.doOCR(imgFile);
			//输出图片文件名，即正确识别结果
			System.out.println("ImgFile: " + imgFile.getAbsolutePath());
			//输出识别结果
			System.out.println("OCR Result: " + ocrResult);
		}
	}

	@Test
	public void IdentifyCode() {
		File imgDir = new File("F:/JavaSource/image");
		//对img_data文件夹中的每个验证码进行识别
		//文件名即正确的结果
		for (File imgFile : imgDir.listFiles()) {
			File imageFile = imgFile;
			ITesseract instance = new Tesseract();
			//图片二值化，增加识别率
			BufferedImage grayImage = null;
			try {
				grayImage = ImageHelper.convertImageToBinary(ImageIO.read(imageFile));
			} catch (IOException e2) {

				e2.printStackTrace();
			}
			try {
//				ImageIO.write(grayImage, "png", new File(System.getProperty("user.dir") + "/img", "vc1.png"));
				ImageIO.write(grayImage, "png", new File("F:/JavaSource/image", "vc1.png"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String path1 = "F:/JavaSource/image/vc1.png";

			File imageFile1 = new File(path1);
			String result = null;
			try {
				result = instance.doOCR(imageFile1);
			} catch (TesseractException e1) {
				e1.printStackTrace();
			}
			result = result.replaceAll("[^a-z^A-Z^0-9]", "");
			System.out.println(result);
		}
	}
}
