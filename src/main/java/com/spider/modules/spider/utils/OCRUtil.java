package com.spider.modules.spider.utils;

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

import java.io.File;

public class OCRUtil {

	public static String identifyCode(File imgFile) throws TesseractException {
		ITesseract instance = new Tesseract();
		//该例子输入的是文件，也可输入BufferedImage
		String ocrResult = instance.doOCR(imgFile);
		//"/"换成7，因为总是出错，又不想去训练它
		ocrResult = ocrResult.replaceAll("/", "7");
		ocrResult = ocrResult.replaceAll("[^a-zA-Z0-9]", "");
		//输出识别结果
		System.out.println("OCR Result: " + ocrResult);
		return ocrResult;
	}
}
