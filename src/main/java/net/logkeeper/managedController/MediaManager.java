package net.logkeeper.managedController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "mediaManager")
@ApplicationScoped
public class MediaManager implements Serializable {

    private StreamedContent pdfDocument;
    String previewPath = "";

    public String getIdFile() {
	return java.util.UUID.randomUUID().toString();
    }

    public String getPreviewPath() {
	return previewPath;
    }

    public void setPreviewPath(String previewPath) {
	this.previewPath = previewPath;
    }

    public StreamedContent getPdfDocument() {
	return pdfDocument;
    }

    public void setPdfDocument(StreamedContent pdfDocument) {
	this.pdfDocument = pdfDocument;
    }

    public void getPDF() throws FileNotFoundException {
	FileInputStream fis = new FileInputStream(new File(previewPath));
	pdfDocument = new DefaultStreamedContent(fis, "application/pdf");
    }

    public void previewFile(AdiAlani pFile) throws FileNotFoundException {
	File file = new File("" + App.getServlet("files") + pFile.getPath());
	previewPath = ""
		+ App.getServlet("files.pdf")
		+ pFile.getPath()
			.substring(0, pFile.getPath().lastIndexOf('.'))
		+ ".pdf";
	ConvertPdf.convertTextfileToPDF(file);
	getPDF();
    }

}
