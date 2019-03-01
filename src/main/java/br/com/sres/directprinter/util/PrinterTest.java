/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sres.directprinter.util;

import java.awt.print.PrinterJob;
import java.io.File;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

/**
 *
 * @author tiago.teixeira
 */
public class PrinterTest {

    public static void main(String[] args) {
        PrinterTest pt = new PrinterTest();
        File file = new File("C:/Users/tiago.teixeira/Desktop/VW_PROCED_SINONIMIA_NOVO.pdf");
        pt.printPdfDocument(pt.getDefaultPrinter(), file);
    }

    public PrinterTest() {
    }

    public PrintService getDefaultPrinter() {
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        System.out.println(service.getName());
        return service;
    }

    public void printPdfDocument(PrintService service, File file) {
        try {
            if (service != null) {
                PrinterJob job = PrinterJob.getPrinterJob();
                PDDocument document = PDDocument.load(file);
                job.setPageable(new PDFPageable(document));
                job.setPrintService(service);
                job.print();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
