/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sres.directprinter.util;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.File;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.print.JRPrinterAWT;
import net.sf.jasperreports.engine.type.OrientationEnum;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

/**
 *
 * @author tiago.teixeira
 */
public class PrinterTest {

    public static void main(String[] args) {
        PrinterTest pt = new PrinterTest();
        //File file = new File("C:/Users/tiago.teixeira/Desktop/VW_PROCED_SINONIMIA_NOVO.pdf");
        File file = new File("C:/Users/tiago.teixeira/Desktop/Relação de Procedimentos Mês.pdf");
        //File file = new File("C:/Users/tiago.teixeira/Desktop/atr.txt");
        pt.printPdfDocument(pt.getDefaultPrinter(), file, 1);
    }

    public PrinterTest() {
    }

    private PageFormat getPageFormat(JasperPrint jPrint) {
        Paper paper = new Paper();
        PageFormat format = new PageFormat();
        if (jPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) {
            format.setOrientation(PageFormat.LANDSCAPE);
            paper.setImageableArea(0, 0, jPrint.getPageHeight(), jPrint.getPageWidth());
            paper.setSize(jPrint.getPageHeight(), jPrint.getPageWidth());
        } else {
            format.setOrientation(PageFormat.PORTRAIT);
            paper.setImageableArea(0, 0, jPrint.getPageWidth(), jPrint.getPageHeight());
            paper.setSize(jPrint.getPageWidth(), jPrint.getPageHeight());
        }
        return format;
    }

    public PrintService getDefaultPrinter() {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        System.out.println(service.getName());
        //Arrays.asList(service.getSupportedAttributeCategories()).forEach(c -> System.out.println(c.getName()));
        return service;
    }

    public void printFromJasperPrint(PrintService service, JasperPrint jasperPrint, int copies) {
        try {
            if (service != null) {
                PrinterJob job = PrinterJob.getPrinterJob();
                //job.setPageable(new DefaultPageable(jasperPrint));
                PDDocument document = PDDocument.load(JasperExportManager.exportReportToPdf(jasperPrint));
                PDFPrintable printable = new PDFPrintable(document, Scaling.SHRINK_TO_FIT);
                job.setPrintable(printable);
                job.setPrintService(service);
                job.setCopies(copies);
                job.print(getAttributes(jasperPrint));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /*javax.print.attribute.standard.JobName
          javax.print.attribute.standard.RequestingUserName;
          javax.print.attribute.standard.Copies;
          javax.print.attribute.standard.Destination;
          javax.print.attribute.standard.OrientationRequested;
          javax.print.attribute.standard.PageRanges;
          javax.print.attribute.standard.Media;
          javax.print.attribute.standard.MediaPrintableArea;
          javax.print.attribute.standard.Fidelity;
          javax.print.attribute.standard.SheetCollate;
          sun.print.SunAlternateMedia;
          javax.print.attribute.standard.Chromaticity;
          javax.print.attribute.standard.Sides;
          javax.print.attribute.standard.PrinterResolution;*/
    public void printPdfDocument(PrintService service, File file, int copies) {
        try {
            if (service != null) {
                PrinterJob job = PrinterJob.getPrinterJob();
                PDDocument document = PDDocument.load(file);
                job.setPageable(new PDFPageable(document));
                job.setPrintService(service);
                PDFPrintable printable = new PDFPrintable(document, Scaling.SHRINK_TO_FIT);
                job.setPrintable(printable);
                job.setCopies(copies);
                job.print(getAttributes());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private PrintRequestAttributeSet getAttributes() {
        PrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
        set.add(OrientationRequested.LANDSCAPE);
        set.add(new MediaPrintableArea(0, 0, 575, 792, MediaPrintableArea.MM));
        return set;
    }

    private PrintRequestAttributeSet getAttributes(JasperPrint jasperPrint) {
        PrintRequestAttributeSet set = new HashPrintRequestAttributeSet();
        if (jasperPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) {
            set.add(OrientationRequested.LANDSCAPE);
        } else {
            set.add(OrientationRequested.PORTRAIT);
        }
        int height = jasperPrint.getPageHeight();
        int width = jasperPrint.getPageWidth();
        int imgHeight = height - (jasperPrint.getTopMargin() + jasperPrint.getBottomMargin());
        int imgWidth = width - (jasperPrint.getRightMargin() + jasperPrint.getLeftMargin());
        set.add(new MediaPrintableArea(0, 0, imgWidth, imgHeight, MediaPrintableArea.MM));
        return set;
    }

    public void printPdfDocument(PrintService service, JasperPrint jasperPrint, int copies) {
        try {
            if (service != null) {
                PrinterJob job = PrinterJob.getPrinterJob();
                PDDocument document = PDDocument.load(JasperExportManager.exportReportToPdf(jasperPrint));
                job.setPageable(new PDFPageable(document));
                job.setCopies(copies);
                job.print(getAttributes(jasperPrint));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class DefaultPageable implements Pageable {

        private final JasperPrint jasperPrint;

        public DefaultPageable(JasperPrint jasperPrint) {
            this.jasperPrint = jasperPrint;
        }

        @Override
        public int getNumberOfPages() {
            return jasperPrint.getPages().size();
        }

        @Override
        public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
            Paper paper = new Paper();
            PageFormat format = new PageFormat();
            int rl = jasperPrint.getRightMargin() + jasperPrint.getLeftMargin();
            int tb = jasperPrint.getTopMargin() + jasperPrint.getBottomMargin();
            int height = jasperPrint.getPageHeight();
            int width = jasperPrint.getPageWidth();
            int imgHeight = height - tb;
            int imgWidth = width - rl;
            if (jasperPrint.getOrientationValue() == OrientationEnum.LANDSCAPE) {
                format.setOrientation(PageFormat.LANDSCAPE);
                paper.setImageableArea(0, 0, imgHeight, imgWidth);
                paper.setSize(height, width);
            } else {
                format.setOrientation(PageFormat.PORTRAIT);
                paper.setImageableArea(0, 0, imgWidth, imgHeight);
                paper.setSize(width, height);
            }
            format.setPaper(paper);
            return format;
        }

        @Override
        public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
            try {
                return new JRPrinterAWT(DefaultJasperReportsContext.getInstance(), jasperPrint);
            } catch (Exception e) {
            }
            return null;
        }

    }

}
