package it.cgmconsulting.mspost.service;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PostService postService;
    public InputStream createPdf(PostResponse postResponse) {

        String title = postResponse.getTitle();
        String content = postResponse.getContent();
        String author = postResponse.getAuthor();
        String publishedAt = postResponse.getPublishedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        Document document = new Document(pdf, PageSize.A4, false);

        addMetaData(postResponse.getCategories(), author, pdf);

        Paragraph pTitle = new Paragraph(title).setFontSize(20).setBold().setFontColor(new DeviceRgb(100, 149,247), 100);
        document.add(pTitle);

        Paragraph pContent = new Paragraph(content).setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(pContent);

        Paragraph pAuthor = new Paragraph(author).setTextAlignment(TextAlignment.RIGHT).setFontSize(10).setItalic().setFontColor(new DeviceRgb(211, 211,211), 100);
        document.add(pAuthor);

        Paragraph pPublishedAt = new Paragraph(publishedAt).setTextAlignment(TextAlignment.RIGHT).setFontSize(10).setItalic().setFontColor(new DeviceRgb(211, 211,211), 100);
        document.add(pPublishedAt);


        int pageNumbers = pdf.getNumberOfPages();
        for(int i = 1; i <= pageNumbers; i++){
            document.showTextAligned(new Paragraph(String.format("page %s of %s", i, pageNumbers)).setFontSize(8).setItalic(),
                    560, 20, i, TextAlignment.RIGHT, VerticalAlignment.BOTTOM, 0);
        }
        document.close();
        InputStream in = new ByteArrayInputStream(out.toByteArray());

        return in;
    }

    private void addMetaData(Set<String> categories, String author, PdfDocument pdfDocument){
        PdfDocumentInfo info = pdfDocument.getDocumentInfo();
        info.setKeywords(categories.toString());
        info.setAuthor(author);

    }
}
