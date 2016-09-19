package net.logkeeper.managedController;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ConvertPdf implements Serializable {
    public static boolean convertTextfileToPDF(File file) {
	FileInputStream iStream = null;
	DataInputStream in = null;
	InputStreamReader is = null;
	BufferedReader br = null;
	try {
	    Document pdfDoc = new Document();
	    // String text_file_name =file.getParent()+"\\"+"texttpPDF.pdf";
	    String text_file_name = App.getServlet("files.pdf")
		    + file.getName().substring(0,
			    file.getName().lastIndexOf('.')) + ".pdf";
	    PdfWriter writer = PdfWriter.getInstance(pdfDoc,
		    new FileOutputStream(text_file_name));
	    pdfDoc.open();
	    pdfDoc.setMarginMirroring(true);
	    pdfDoc.setMargins(36, 72, 108, 180);
	    pdfDoc.topMargin();
	    Font normal_font = new Font();
	    Font bold_font = new Font();
	    bold_font.setStyle(Font.BOLD);
	    bold_font.setSize(10);
	    normal_font.setStyle(Font.NORMAL);
	    normal_font.setSize(10);
	    pdfDoc.add(new Paragraph("\n"));
	    if (file.exists()) {
		iStream = new FileInputStream(file);
		in = new DataInputStream(iStream);
		is = new InputStreamReader(in);
		br = new BufferedReader(is);
		String strLine;
		while ((strLine = br.readLine()) != null) {
		    Paragraph para = new Paragraph(strLine + "\n", normal_font);
		    para.setAlignment(Element.ALIGN_JUSTIFIED);
		    pdfDoc.add(para);
		}
	    } else {
		System.out.println("file does not exist");
		return false;
	    }
	    pdfDoc.close();
	} catch (Exception e) {
	    System.out.println("FileUtility.covertEmailToPDF(): exception = "
		    + e.getMessage());
	} finally {

	    try {
		if (br != null) {
		    br.close();
		}
		if (is != null) {
		    is.close();
		}
		if (in != null) {
		    in.close();
		}
		if (iStream != null) {
		    iStream.close();
		}
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
	return true;
    }
}
