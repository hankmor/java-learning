package com.belonk.pdf.itext;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.font.FontProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by sun on 2021/6/4.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class Html2PdfDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		URL url = Html2PdfDemo.class.getResource("/");
		// IO
		File htmlSource = new File(url.getPath() + "/input.html");
		File pdfDest = new File(url.getPath() + "/output.pdf");
		if (pdfDest.exists())
			pdfDest.delete();
		// pdfHTML specific code
		ConverterProperties converterProperties = new ConverterProperties();
		converterProperties.setCharset(StandardCharsets.UTF_8.name());
		converterProperties.setBaseUri(url.getPath());
		// 字体
		FontProvider fontProvider = new FontProvider();
		// 添加标准字体
		fontProvider.addStandardPdfFonts();
		// 添加系统字体，支持中文
		int size = fontProvider.addSystemFonts();
		System.out.println("添加了" + size + "个字体"); // 添加了20个字体
		converterProperties.setFontProvider(fontProvider);
		HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), converterProperties);
	}
}