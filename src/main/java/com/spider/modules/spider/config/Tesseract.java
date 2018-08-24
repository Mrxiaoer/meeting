package com.spider.modules.spider.config;

import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.ptr.PointerByReference;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import net.sourceforge.lept4j.Box;
import net.sourceforge.lept4j.Boxa;
import net.sourceforge.lept4j.Leptonica;
import net.sourceforge.tess4j.ITessAPI.TessBaseAPI;
import net.sourceforge.tess4j.ITessAPI.TessPageIterator;
import net.sourceforge.tess4j.ITessAPI.TessResultIterator;
import net.sourceforge.tess4j.ITessAPI.TessResultRenderer;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.OCRResult;
import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;
import net.sourceforge.tess4j.util.ImageIOHelper;
import net.sourceforge.tess4j.util.LoggHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片字符光学识别
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-30
 */
public class Tesseract implements ITesseract {

	private static final Logger logger = LoggerFactory.getLogger((new LoggHelper()).toString());
	private final Properties prop;
	private final List<String> configList;
	private String language = "eng";
	private String datapath;
	private RenderedFormat renderedFormat;
	private int psm;
	private int ocrEngineMode;
	private TessAPI api;
	private TessBaseAPI handle;

	public Tesseract() {
		this.renderedFormat = RenderedFormat.TEXT;
		this.psm = -1;
		this.ocrEngineMode = 3;
		this.prop = new Properties();
		this.configList = new ArrayList<>();

		try {
			this.datapath = System.getProperty("TESSDATA_PREFIX");
		} finally {
			if (this.datapath == null) {
				this.datapath = "./";
			}

		}

	}

	protected TessAPI getAPI() {
		return this.api;
	}

	protected TessBaseAPI getHandle() {
		return this.handle;
	}

	@Override
	public void setDatapath(String var1) {
		this.datapath = var1;
	}

	@Override
	public void setLanguage(String var1) {
		this.language = var1;
	}

	@Override
	public void setOcrEngineMode(int var1) {
		this.ocrEngineMode = var1;
	}

	@Override
	public void setPageSegMode(int var1) {
		this.psm = var1;
	}

	public void setHocr(boolean var1) {
		this.renderedFormat = var1 ? RenderedFormat.HOCR : RenderedFormat.TEXT;
		this.prop.setProperty("tessedit_create_hocr", var1 ? "1" : "0");
	}

	@Override
	public void setTessVariable(String var1, String var2) {
		this.prop.setProperty(var1, var2);
	}

	@Override
	public void setConfigs(List<String> var1) {
		this.configList.clear();
		if (var1 != null) {
			this.configList.addAll(var1);
		}
	}

	@Override
	public String doOCR(File var1) throws TesseractException {
		return this.doOCR(var1, null);
	}

	@Override
	public String doOCR(File var1, Rectangle var2) throws TesseractException {
		try {
			File var3 = ImageIOHelper.getImageFile(var1);
			String var4 = ImageIOHelper.getImageFileFormat(var3);
			Iterator var5 = ImageIO.getImageReadersByFormatName(var4);
			if (!var5.hasNext()) {
				throw new RuntimeException(
						"Unsupported image format. May need to install JAI Image I/O package.\nhttps://github"
								+ ".com/jai-imageio/jai-imageio-core");
			} else {
				ImageReader var6 = (ImageReader) var5.next();
				StringBuilder var7 = new StringBuilder();

				try {
					ImageInputStream var8 = ImageIO.createImageInputStream(var3);
					Throwable var9 = null;

					try {
						var6.setInput(var8);
						int var10 = var6.getNumImages(true);
						this.init();
						this.setTessVariables();

						for (int var11 = 0; var11 < var10; ++var11) {
							IIOImage var12 = var6.readAll(var11, var6.getDefaultReadParam());
							var7.append(this.doOCR(var12, var1.getPath(), var2, var11 + 1));
						}

						if (this.renderedFormat == RenderedFormat.HOCR) {
							var7.insert(0,
									"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www" + ".w3"
											+ ".org/TR/html4/loose.dtd\">\n<html>\n<head>\n<title></title>\n<meta "
											+ "http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />\n<meta "
											+ "name='ocr-system' content='tesseract'/>\n</head>\n<body>\n")
									.append("</body>\n</html>\n");
						}
					} catch (Throwable var29) {
						var9 = var29;
						throw var29;
					} finally {
						if (var8 != null) {
							if (var9 != null) {
								try {
									var8.close();
								} catch (Throwable var28) {
									var9.addSuppressed(var28);
								}
							} else {
								var8.close();
							}
						}

					}
				} finally {
					this.dispose();
				}

				return var7.toString();
			}
		} catch (Exception var32) {
			logger.error(var32.getMessage(), var32);
			throw new TesseractException(var32);
		}
	}

	@Override
	public String doOCR(BufferedImage var1) throws TesseractException {
		return this.doOCR(var1, null);
	}

	@Override
	public String doOCR(BufferedImage var1, Rectangle var2) throws TesseractException {
		try {
			return this.doOCR(ImageIOHelper.getIIOImageList(var1), var2);
		} catch (Exception var4) {
			logger.error(var4.getMessage(), var4);
			throw new TesseractException(var4);
		}
	}

	@Override
	public String doOCR(List<IIOImage> var1, Rectangle var2) throws TesseractException {
		return this.doOCR(var1, null, var2);
	}

	@Override
	public String doOCR(List<IIOImage> var1, String var2, Rectangle var3) throws TesseractException {
		this.init();
		this.setTessVariables();

		try {
			StringBuilder var4 = new StringBuilder();
			int var5 = 0;
			Iterator var6 = var1.iterator();

			while (var6.hasNext()) {
				IIOImage var7 = (IIOImage) var6.next();
				++var5;

				try {
					this.setImage(var7.getRenderedImage(), var3);
					var4.append(this.getOCRText(var2, var5));
				} catch (IOException var12) {
					logger.error(var12.getMessage(), var12);
				}
			}

			if (this.renderedFormat == RenderedFormat.HOCR) {
				var4.insert(0, "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3"
						+ ".org/TR/html4/loose.dtd\">\n<html>\n<head>\n<title></title>\n<meta "
						+ "http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />\n<meta "
						+ "name='ocr-system' content='tesseract'/>\n</head>\n<body>\n").append("</body>\n</html>\n");
			}

			return var4.toString();
		} finally {
			this.dispose();
		}
	}

	private String doOCR(IIOImage var1, String var2, Rectangle var3, int var4) throws TesseractException {
		String var5 = "";

		try {
			this.setImage(var1.getRenderedImage(), var3);
			var5 = this.getOCRText(var2, var4);
		} catch (IOException var7) {
			logger.warn(var7.getMessage(), var7);
		}

		return var5;
	}

	@Override
	public String doOCR(int var1, int var2, ByteBuffer var3, Rectangle var4, int var5) throws TesseractException {
		return this.doOCR(var1, var2, var3, null, var4, var5);
	}

	@Override
	public String doOCR(int var1, int var2, ByteBuffer var3, String var4, Rectangle var5, int var6)
			throws TesseractException {
		this.init();
		this.setTessVariables();

		String var7;
		try {
			this.setImage(var1, var2, var3, var5, var6);
			var7 = this.getOCRText(var4, 1);
		} catch (Exception var11) {
			logger.error(var11.getMessage(), var11);
			throw new TesseractException(var11);
		} finally {
			this.dispose();
		}

		return var7;
	}

	protected void init() {
		this.api = TessAPI.INSTANCE;
		this.handle = this.api.TessBaseAPICreate();
		StringArray var1 = new StringArray(this.configList.toArray(new String[0]));
		PointerByReference var2 = new PointerByReference();
		var2.setPointer(var1);
		this.api.TessBaseAPIInit1(this.handle, this.datapath, this.language, this.ocrEngineMode, var2,
				this.configList.size());
		if (this.psm > -1) {
			this.api.TessBaseAPISetPageSegMode(this.handle, this.psm);
		}

	}

	protected void setTessVariables() {
		Enumeration var1 = this.prop.propertyNames();

		while (var1.hasMoreElements()) {
			String var2 = (String) var1.nextElement();
			this.api.TessBaseAPISetVariable(this.handle, var2, this.prop.getProperty(var2));
		}

	}

	protected void setImage(RenderedImage var1, Rectangle var2) throws IOException {
		this.setImage(var1.getWidth(), var1.getHeight(), ImageIOHelper.getImageByteBuffer(var1), var2,
				var1.getColorModel().getPixelSize());
	}

	protected void setImage(int var1, int var2, ByteBuffer var3, Rectangle var4, int var5) {
		int var6 = var5 / 8;
		int var7 = (int) Math.ceil((double) (var1 * var5) / 8.0D);
		this.api.TessBaseAPISetImage(this.handle, var3, var1, var2, var6, var7);
		if (var4 != null && !var4.isEmpty()) {
			this.api.TessBaseAPISetRectangle(this.handle, var4.x, var4.y, var4.width, var4.height);
		}

	}

	protected String getOCRText(String var1, int var2) {
		if (var1 != null && !var1.isEmpty()) {
			this.api.TessBaseAPISetInputName(this.handle, var1);
		}

		Pointer var3 = this.renderedFormat == RenderedFormat.HOCR ? this.api.TessBaseAPIGetHOCRText(this.handle, var2 - 1)
				: this.api.TessBaseAPIGetUTF8Text(this.handle);
		String var4 = var3.getString(0L);
		this.api.TessDeleteText(var3);
		return var4;
	}

	private TessResultRenderer createRenderers(String var1, List<RenderedFormat> var2) {
		TessResultRenderer var3 = null;
		Iterator var4 = var2.iterator();

		while (var4.hasNext()) {
			RenderedFormat var5 = (RenderedFormat) var4.next();
			switch (var5) {
				case TEXT:
					if (var3 == null) {
						var3 = this.api.TessTextRendererCreate(var1);
					} else {
						this.api.TessResultRendererInsert(var3, this.api.TessTextRendererCreate(var1));
					}
					break;
				case HOCR:
					if (var3 == null) {
						var3 = this.api.TessHOcrRendererCreate(var1);
					} else {
						this.api.TessResultRendererInsert(var3, this.api.TessHOcrRendererCreate(var1));
					}
					break;
				case PDF:
					String var6 = this.api.TessBaseAPIGetDatapath(this.handle);
					boolean var7 = String.valueOf(1).equals(this.prop.getProperty("textonly_pdf"));
					if (var3 == null) {
						var3 = this.api.TessPDFRendererCreate(var1, var6, var7 ? 1 : 0);
					} else {
						this.api.TessResultRendererInsert(var3, this.api.TessPDFRendererCreate(var1, var6, var7 ? 1 : 0));
					}
					break;
				case BOX:
					if (var3 == null) {
						var3 = this.api.TessBoxTextRendererCreate(var1);
					} else {
						this.api.TessResultRendererInsert(var3, this.api.TessBoxTextRendererCreate(var1));
					}
					break;
				case UNLV:
					if (var3 == null) {
						var3 = this.api.TessUnlvRendererCreate(var1);
					} else {
						this.api.TessResultRendererInsert(var3, this.api.TessUnlvRendererCreate(var1));
					}
			}
		}

		return var3;
	}

	@Override
	public void createDocuments(String var1, String var2, List<RenderedFormat> var3) throws TesseractException {
		this.createDocuments(new String[]{var1}, new String[]{var2}, var3);
	}

	@Override
	public void createDocuments(String[] var1, String[] var2, List<RenderedFormat> var3) throws TesseractException {
		if (var1.length != var2.length) {
			throw new RuntimeException("The two arrays must match in length.");
		} else {
			this.init();
			this.setTessVariables();

			try {
				for (int var4 = 0; var4 < var1.length; ++var4) {
					File var5 = new File(var1[var4]);
					File var6 = null;

					try {
						var6 = ImageIOHelper.getImageFile(var5);
						TessResultRenderer var7 = this.createRenderers(var2[var4], var3);
						this.createDocuments(var6.getPath(), var7);
						this.api.TessDeleteResultRenderer(var7);
					} catch (Exception var16) {
						logger.warn(var16.getMessage(), var16);
					} finally {
						if (var6 != null && var6.exists() && var6 != var5 && var6.getName().startsWith("multipage") && var6
								.getName().endsWith(".tif")) {
							var6.delete();
						}

					}
				}
			} finally {
				this.dispose();
			}

		}
	}

	private int createDocuments(String var1, TessResultRenderer var2) throws TesseractException {
		this.api.TessBaseAPISetInputName(this.handle, var1);
		int var3 = this.api.TessBaseAPIProcessPages(this.handle, var1, null, 0, var2);
		if (var3 == 0) {
			throw new TesseractException("Error during processing page.");
		} else {
			return this.api.TessBaseAPIMeanTextConf(this.handle);
		}
	}

	@Override
	public List<Rectangle> getSegmentedRegions(BufferedImage var1, int var2) throws TesseractException {
		this.init();
		this.setTessVariables();

		ArrayList var16;
		try {
			ArrayList var3 = new ArrayList();
			this.setImage(var1, null);
			Boxa var4 = this.api.TessBaseAPIGetComponentImages(this.handle, var2, 1, null, null);
			Leptonica var5 = Leptonica.INSTANCE;
			int var6 = var5.boxaGetCount(var4);

			for (int var7 = 0; var7 < var6; ++var7) {
				Box var8 = var5.boxaGetBox(var4, var7, 2);
				if (var8 != null) {
					var3.add(new Rectangle(var8.x, var8.y, var8.w, var8.h));
					PointerByReference var9 = new PointerByReference();
					var9.setValue(var8.getPointer());
					var5.boxDestroy(var9);
				}
			}

			PointerByReference var15 = new PointerByReference();
			var15.setValue(var4.getPointer());
			var5.boxaDestroy(var15);
			var16 = var3;
		} catch (IOException var13) {
			logger.warn(var13.getMessage(), var13);
			throw new TesseractException(var13);
		} finally {
			this.dispose();
		}

		return var16;
	}

	@Override
	public List<Word> getWords(BufferedImage var1, int var2) {
		this.init();
		this.setTessVariables();
		ArrayList var3 = new ArrayList();

		try {
			this.setImage(var1, null);
			this.api.TessBaseAPIRecognize(this.handle, null);
			TessResultIterator var4 = this.api.TessBaseAPIGetIterator(this.handle);
			TessPageIterator var5 = this.api.TessResultIteratorGetPageIterator(var4);
			this.api.TessPageIteratorBegin(var5);

			do {
				Pointer var6 = this.api.TessResultIteratorGetUTF8Text(var4, var2);
				String var7 = var6.getString(0L);
				this.api.TessDeleteText(var6);
				float var8 = this.api.TessResultIteratorConfidence(var4, var2);
				IntBuffer var9 = IntBuffer.allocate(1);
				IntBuffer var10 = IntBuffer.allocate(1);
				IntBuffer var11 = IntBuffer.allocate(1);
				IntBuffer var12 = IntBuffer.allocate(1);
				this.api.TessPageIteratorBoundingBox(var5, var2, var9, var10, var11, var12);
				int var13 = var9.get();
				int var14 = var10.get();
				int var15 = var11.get();
				int var16 = var12.get();
				Word var17 = new Word(var7, var8, new Rectangle(var13, var14, var15 - var13, var16 - var14));
				var3.add(var17);
			} while (this.api.TessPageIteratorNext(var5, var2) == 1);
		} catch (Exception var21) {
			logger.warn(var21.getMessage(), var21);
		} finally {
			this.dispose();
		}

		return var3;
	}

	@Override
	public List<OCRResult> createDocumentsWithResults(String[] var1, String[] var2, List<RenderedFormat> var3, int var4)
			throws TesseractException {
		if (var1.length != var2.length) {
			throw new RuntimeException("The two arrays must match in length.");
		} else {
			this.init();
			this.setTessVariables();
			ArrayList var5 = new ArrayList();

			try {
				for (int var6 = 0; var6 < var1.length; ++var6) {
					File var7 = new File(var1[var6]);
					File var8 = null;

					try {
						var8 = ImageIOHelper.getImageFile(var7);
						TessResultRenderer var9 = this.createRenderers(var2[var6], var3);
						int var10 = this.createDocuments(var8.getPath(), var9);
						Object var11 = var10 > 0 ? this.getRecognizedWords(var4) : new ArrayList();
						var5.add(new OCRResult(var10, (List) var11));
						this.api.TessDeleteResultRenderer(var9);
					} catch (Exception var20) {
						logger.warn(var20.getMessage(), var20);
					} finally {
						if (var8 != null && var8.exists() && var8 != var7 && var8.getName().startsWith("multipage") && var8
								.getName().endsWith(".tif")) {
							var8.delete();
						}

					}
				}
			} finally {
				this.dispose();
			}

			return var5;
		}
	}

	private List<Word> getRecognizedWords(int var1) {
		ArrayList var2 = new ArrayList();

		try {
			TessResultIterator var3 = this.api.TessBaseAPIGetIterator(this.handle);
			TessPageIterator var4 = this.api.TessResultIteratorGetPageIterator(var3);
			this.api.TessPageIteratorBegin(var4);

			do {
				Pointer var5 = this.api.TessResultIteratorGetUTF8Text(var3, var1);
				String var6 = var5.getString(0L);
				this.api.TessDeleteText(var5);
				float var7 = this.api.TessResultIteratorConfidence(var3, var1);
				IntBuffer var8 = IntBuffer.allocate(1);
				IntBuffer var9 = IntBuffer.allocate(1);
				IntBuffer var10 = IntBuffer.allocate(1);
				IntBuffer var11 = IntBuffer.allocate(1);
				this.api.TessPageIteratorBoundingBox(var4, var1, var8, var9, var10, var11);
				int var12 = var8.get();
				int var13 = var9.get();
				int var14 = var10.get();
				int var15 = var11.get();
				Word var16 = new Word(var6, var7, new Rectangle(var12, var13, var14 - var12, var15 - var13));
				var2.add(var16);
			} while (this.api.TessPageIteratorNext(var4, var1) == 1);
		} catch (Exception var17) {
			logger.warn(var17.getMessage(), var17);
		}

		return var2;
	}

	protected void dispose() {
		this.api.TessBaseAPIDelete(this.handle);
	}
}

