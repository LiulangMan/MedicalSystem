package com.zjw.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.log4j.Log4j2;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * @program: medical_sales_management_system
 * @author: 一树
 * @data: 2021/1/20 14:32
 */
@Log4j2
public class PrintUtils {

    private static Font font;

    static {
        try {
            //jar包
            BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            font = new Font(bfChinese, 14, Font.BOLD);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

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

            log.info("本机打印机:" + Arrays.toString(printServices));
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

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            //打开文档
            document.open();
            Paragraph element = new Paragraph(text, font);
            document.add(element);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
