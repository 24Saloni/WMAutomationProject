package org.wm.oneframework.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckbox;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextbox;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

public class PDFUtils {

	/**
	 * 
	 * @param filepth
	 * @param startPage
	 * @param endPage
	 * @return
	 * @throws IOException
	 */

	public String getPdfContent(String filepth, String startPage, String endPage) throws IOException {

		int sPage = Integer.parseInt(startPage);
		int ePage = Integer.parseInt(endPage);
		StringBuilder out = new StringBuilder();

		PDDocument document = PDDocument.load(new File(filepth));
		try {

			document.getClass();

			if (!document.isEncrypted()) {

				PDFTextStripper tStripper = new PDFTextStripper();
				tStripper.setSortByPosition(true);
				tStripper.setStartPage(sPage);
				tStripper.setEndPage(ePage);

				String pdfFileInText = tStripper.getText(document);
				String lines[] = pdfFileInText.split("\\r?\\n");

				for (String line : lines) {

					Thread.sleep(100);

					out.append(line.trim());
					out.append(System.getProperty("line.separator"));

				}

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			document.close();

		}

		return out.toString();
	}

	/**
	 * 
	 * @param fileContent
	 * @param matchContent
	 * @return
	 * @throws IOException
	 */

	public boolean matchFileContent(String fileContent, String matchContent) throws IOException {

		boolean flag = false;
		InputStream stream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));

		for (String line = br.readLine(); line != null; line = br.readLine()) {

			if (line.trim().contains(matchContent.trim()) == true) {

				flag = true;
				break;

			}

		}

		return flag;

	}

	/**
	 * 
	 * @param filePath
	 * @param startPage
	 * @param endPage
	 * @return
	 * @throws IOException
	 */

	public String[] getFontLineByLineFromPdf(String filePath, String startPage, String endPage) throws IOException {

		int sPage = Integer.parseInt(startPage);
		int ePage = Integer.parseInt(endPage);

		PDDocument doc = PDDocument.load(filePath);
		doc.getClass();
		doc.getDocumentCatalog();

		PDFTextStripper stripper = new PDFTextStripper() {

			String add1 = "|Size|";
			String add2 = "|Color|";
			String add3 = "|Alignment|";

			String preBaseFont = "";
			float preBaseFontSize = 0;

			@Override
			protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
				StringBuilder builder = new StringBuilder();

				for (TextPosition position : textPositions) {

					String baseFont = position.getFont().getBaseFont();
					float size = position.getFontSize();
					float textPosition = position.getTextPos().getXPosition();

					if (baseFont != null && !baseFont.equals(preBaseFont)) {

						builder.append("[Font|").append(baseFont).append(add1).append(size).append(add2).append("|").append(add3).append(textPosition).append("]^");

						preBaseFont = baseFont;
						preBaseFontSize = size;

					}
					if (size != preBaseFontSize) {

						builder.append("[Font|").append(baseFont).append(add1).append(size).append(add2).append("|").append(add3).append(textPosition).append("]^");

						preBaseFont = baseFont;
						preBaseFontSize = size;

					}

					builder.append(position.getCharacter());

				}

				writeString(builder.toString());

			}
		};

		stripper.setSortByPosition(true);
		stripper.setStartPage(sPage);
		stripper.setEndPage(ePage);
		String content = stripper.getText(doc);
		doc.close();
		String pdfLinesWithFont[] = content.split("\\r?\\n");
		return pdfLinesWithFont;

	}

	public boolean validateTextAttribute(String[] pdfContent, String matchValue, boolean bold, boolean italic) {

		boolean validationFlag = false;
		String baseLine = null;

		String condition = String.valueOf(bold) + "|" + String.valueOf(italic);

		for (int i = 0; i < pdfContent.length; i++) {

			String line = pdfContent[i];

			if (line.trim().toLowerCase().contains(matchValue.trim().toLowerCase()) == true) {

				if (line.trim().contains("Font") == true) {

					baseLine = line.toLowerCase();

				}

				switch (condition) {

				case "true|true":

					if (baseLine.trim().contains("bold") & baseLine.contains("italic")) {
						validationFlag = true;
					} else {
						validationFlag = false;
					}

					break;

				case "true|false":

					if (baseLine.trim().contains("bold") & !baseLine.contains("italic")) {
						validationFlag = true;
					} else {
						validationFlag = false;
					}

					break;

				case "false|true":

					if (!baseLine.trim().contains("bold") & baseLine.contains("italic")) {
						validationFlag = true;
					} else {
						validationFlag = false;
					}

					break;

				case "false|false":

					if (!baseLine.trim().contains("bold") & !baseLine.contains("italic")) {
						validationFlag = true;
					} else {
						validationFlag = false;
					}

					break;

				}

				break;

			}
		}

		return validationFlag;

	}

	public boolean validateFontName(String[] pdfContent, String matchValue, String font) {

		boolean validationFlag = false;
		String baseLine = null;

		for (int i = 0; i < pdfContent.length; i++) {

			String line = pdfContent[i];

			if (line.trim().toLowerCase().contains("font")) {
				baseLine = line;

			}

			if (line.trim().toLowerCase().contains(matchValue.trim().toLowerCase())) {

				if (baseLine.trim().toLowerCase().contains(font.toLowerCase())) {

					validationFlag = true;

				} else {

					validationFlag = false;
				}

				break;

			}

		}

		return validationFlag;

	}

	public boolean validateFontSize(String[] pdfContent, String matchValue, String size) {

		boolean validationFlag = false;
		String baseLine = null;

		for (int i = 0; i < pdfContent.length; i++) {

			String line = pdfContent[i];

			if (line.trim().toLowerCase().contains("font")) {
				baseLine = line;

			}

			if (line.trim().toLowerCase().contains(matchValue.trim().toLowerCase())) {

				if (baseLine.trim().toLowerCase().contains(size.toLowerCase())) {

					validationFlag = true;

				} else {

					validationFlag = false;
				}

				break;

			}

		}

		return validationFlag;

	}

	public String PDFFormESign(String filePath, String fieldName) throws IOException {

		String eSign = null;
		PDDocument doc = PDDocument.load(filePath);

		try {

			PDAcroForm form = doc.getDocumentCatalog().getAcroForm();

			PDField flds;

			flds = form.getField(fieldName);

			if (flds instanceof PDSignatureField) {

				PDSignatureField sign = (PDSignatureField) form.getField(fieldName);
				eSign = sign.getAlternateFieldName();

			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			doc.close();
		}

		return eSign;

	}

	public boolean PDFontCheckBoxVal(String filePath, String fieldName) throws IOException {

		PDDocument doc = PDDocument.load(filePath);
		boolean boxIsChecked = false;

		try {

			PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
			PDField checkBoxVal = form.getField(fieldName);

			boxIsChecked = ((PDCheckbox) checkBoxVal).isChecked();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			doc.close();

		}

		return boxIsChecked;

	}

	public String PDFormTextBoxVal(String filePath, String fieldName) throws IOException {

		PDDocument doc = PDDocument.load(filePath);
		String textBoxVal = null;

		try {

			PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
			PDField textBoxField = form.getField(fieldName);
			textBoxVal = ((PDTextbox) textBoxField).getValue();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			doc.close();

		}

		return textBoxVal;

	}

}
