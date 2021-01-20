package com.zjw.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/20 14:32
 */
public class PrintUtils {

    // 传入文件和打印机名称
    public static void Print(InputStream fis, String printerName) throws PrintException {
        if (fis == null) {
            return;
        }
        try {
            // 设置打印格式，如果未确定类型，可选择autosense
            DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;
            // 设置打印参数
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new Copies(1)); //份数
            //aset.add(MediaSize.ISO.A4); //纸张
            // aset.add(Finishings.STAPLE);//装订
            aset.add(Sides.DUPLEX);//单双面
            // 定位打印服务
            PrintService printService = null;

            //获得本台电脑连接的所有打印机
            PrintService[] printServices = PrinterJob.lookupPrintServices();
            if (printServices == null || printServices.length == 0) {
                System.out.print("打印失败，未找到可用打印机，请检查。");
                return;
            }
            //匹配指定打印机
            for (PrintService service : printServices) {
                System.out.println(service.getName());
                if (printerName != null && service.getName().contains(printerName)) {
                    printService = service;
                    break;
                }
            }
            if (printService == null) {
                printService = printServices[0];
            }

            Doc doc = new SimpleDoc(fis, flavor, null);
            DocPrintJob job = printService.createPrintJob(); // 创建打印作业
            job.print(doc, aset);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            // 关闭打印的文件流
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void creatPdf(String text, String path) {
        try {
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream("helloWorld.PDF"));

            document.open();

            document.add(new Paragraph(text));

            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
