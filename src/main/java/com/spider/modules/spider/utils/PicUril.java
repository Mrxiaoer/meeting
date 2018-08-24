package com.spider.modules.spider.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片工具
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-30
 */
public class PicUril {
	private static Logger logger = LoggerFactory.getLogger(PicUril.class);

	/**
	 * @param sfile   需要去噪的图像
	 * @param destDir 去噪后的图像保存地址
	 */
	public static File cleanLinesInImage(File sfile, String destDir) throws IOException {
		File destF = new File(destDir);
		if (!destF.exists()) {
			boolean m = destF.mkdirs();
			if(!m){
				logger.error("创建文件夹失败！");
			}
		}

		BufferedImage bufferedImage = ImageIO.read(sfile);
		int h = bufferedImage.getHeight();
		int w = bufferedImage.getWidth();

		// 灰度化
		int[][] gray = new int[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int argb = bufferedImage.getRGB(x, y);
				// 图像加亮（调整亮度识别率非常高）
				int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);
				int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);
				int b = (int) (((argb) & 0xFF) * 1.1 + 30);
				if (r >= 255) {
					r = 255;
				}
				if (g >= 255) {
					g = 255;
				}
				if (b >= 255) {
					b = 255;
				}
				gray[x][y] = (int) Math
						.pow((Math.pow(r, 2.2) * 0.2973 + Math.pow(g, 2.2) * 0.6274 + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);
			}
		}

		// 二值化
		int threshold = ostu(gray, w, h);
		BufferedImage binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (gray[x][y] > threshold) {
					gray[x][y] |= 0x00FFFF;
				} else {
					gray[x][y] &= 0xFF0000;
				}
				binaryBufferedImage.setRGB(x, y, gray[x][y]);
			}
		}

		int czw = w / 200 < h / 100 ? w / 200 : h / 100;
		for (int n = 0; n < 3; n++) {
			//去除干扰点 或 干扰线（运用八领域，即像素周围八个点判定，根据实际需要判定）
			for (int i = czw; i >= 1; i--) {
				for (int y = i; y < h - i; y++) {
					for (int x = i; x < w - i; x++) {
						removeInterfere(binaryBufferedImage, x, y, i);
					}
				}
				for (int y = h - i; y > i; y--) {
					for (int x = w - i; x > i; x--) {
						removeInterfere(binaryBufferedImage, x, y, i);
					}
				}
			}
		}

		File resultFile = new File(destDir, sfile.getName());
		ImageIO.write(binaryBufferedImage, "jpg", resultFile);
		return resultFile;
	}

	private static boolean isBlack(int colorInt) {
		Color color = new Color(colorInt);
		return color.getRed() + color.getGreen() + color.getBlue() <= 300;
	}

	private static boolean isWhite(int colorInt) {
		Color color = new Color(colorInt);
		return color.getRed() + color.getGreen() + color.getBlue() > 300;
	}

	public static int isBlackOrWhite(int colorInt) {
		if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730) {
			return 1;
		}
		return 0;
	}

	private static int getColorBright(int colorInt) {
		Color color = new Color(colorInt);
		return color.getRed() + color.getGreen() + color.getBlue();
	}

	private static int ostu(int[][] gray, int w, int h) {
		int[] histData = new int[w * h];
		// Calculate histogram
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int red = 0xFF & gray[x][y];
				histData[red]++;
			}
		}

		// Total number of pixels
		int total = w * h;

		float sum = 0;
		for (int t = 0; t < 256; t++) {
			sum += t * histData[t];
		}

		float sumB = 0;
		int wB = 0;
		int wF;

		float varMax = 0;
		int threshold = 0;

		for (int t = 0; t < 256; t++) {
			// Weight Background
			wB += histData[t];
			if (wB == 0) {
				continue;
			}

			// Weight Foreground
			wF = total - wB;
			if (wF == 0) {
				break;
			}

			sumB += (float) (t * histData[t]);

			// Mean Background
			float mB = sumB / wB;
			// Mean Foreground
			float mF = (sum - sumB) / wF;

			// Calculate Between Class Variance
			float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

			// Check if new maximum found
			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = t;
			}
		}

		return threshold;
	}

	private static BufferedImage removeInterfere(BufferedImage binaryBufferedImage, int x, int y, int i) {
		boolean lineFlag = false;
		int pointflagNum = 0;

		if (isBlack(binaryBufferedImage.getRGB(x, y))) {
			//左右像素点为"白"即空时，去掉此点
			if (isWhite(binaryBufferedImage.getRGB(x - i, y)) && isWhite(binaryBufferedImage.getRGB(x + i, y))) {
				lineFlag = true;
			}
			//上下像素点为"白"即空时，去掉此点
			if (isWhite(binaryBufferedImage.getRGB(x, y + i)) && isWhite(binaryBufferedImage.getRGB(x, y - i))) {
				lineFlag = true;
			}
			//斜上像素点为"白"即空时，去掉此点
			if (isWhite(binaryBufferedImage.getRGB(x - i, y + i)) && isWhite(binaryBufferedImage.getRGB(x + i, y - i))) {
				lineFlag = true;
			}
			if (isWhite(binaryBufferedImage.getRGB(x + i, y + i)) && isWhite(binaryBufferedImage.getRGB(x - i, y - i))) {
				lineFlag = true;
			}
			if (isWhite(binaryBufferedImage.getRGB(x + i, y + i)) && isWhite(binaryBufferedImage.getRGB(x - i, y + i)) &&
					isWhite(binaryBufferedImage.getRGB(x, y - i))) {
				lineFlag = true;
			}
			if (isWhite(binaryBufferedImage.getRGB(x + i, y - i)) && isWhite(binaryBufferedImage.getRGB(x - i, y - i)) &&
					isWhite(binaryBufferedImage.getRGB(x, y + i))) {
				lineFlag = true;
			}
			if (isWhite(binaryBufferedImage.getRGB(x + i, y - i)) && isWhite(binaryBufferedImage.getRGB(x + i, y + i)) &&
					isWhite(binaryBufferedImage.getRGB(x - i, y))) {
				lineFlag = true;
			}
			if (isWhite(binaryBufferedImage.getRGB(x - i, y - i)) && isWhite(binaryBufferedImage.getRGB(x - i, y + i)) &&
					isWhite(binaryBufferedImage.getRGB(x + i, y))) {
				lineFlag = true;
			}
			//去除干扰
			if (lineFlag) {
				binaryBufferedImage.setRGB(x, y, -i);
			}
		}
		return binaryBufferedImage;
	}

}
