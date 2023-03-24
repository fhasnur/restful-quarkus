package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class ExportService {
    public Response exportPdf() throws JRException {
        //load template jasper
        File file = new File("src/main/resources/ItemTemplate.jrxml");

        //build jasper object report from load template
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        //create datasource jasper for all item data
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Item.listAll());

        //create object jasperPrint
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), jrBeanCollectionDataSource);

        //export jasperPrint to byte array
        byte[] jasperResult = JasperExportManager.exportReportToPdf(jasperPrint);

        //return response with content type pdf
        return Response.ok().type("application/pdf").entity(jasperResult).build();
    }

    public Response exportExcel() throws IOException {
        //get all data item
        List<Item> ItemList = Item.listAll();

        //create new workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //create sheet
        XSSFSheet sheet = workbook.createSheet("data");

        //set header
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        row.createCell(0).setCellValue("id");
        row.createCell(1).setCellValue("name");
        row.createCell(2).setCellValue("count");
        row.createCell(3).setCellValue("price");
        row.createCell(4).setCellValue("type");
        row.createCell(5).setCellValue("description");
        row.createCell(6).setCellValue("createdAt");
        row.createCell(7).setCellValue("updatedAt");

        //set data
        for(Item peserta : ItemList){
            row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue(peserta.id);
            row.createCell(1).setCellValue(peserta.name);
            row.createCell(2).setCellValue(peserta.count);
            row.createCell(3).setCellValue(peserta.price);
            row.createCell(4).setCellValue(peserta.type);
            row.createCell(5).setCellValue(peserta.description);
            row.createCell(6).setCellValue(peserta.createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
            row.createCell(7).setCellValue(peserta.updatedAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")));
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);

        // Content-Disposition: attachment; filename="name_of_excel_file.xls"
        return Response.ok()
                .type("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .header("Content-Disposition", "attachment; filename=\"item_list_excel.xlsx\"")
                .entity(outputStream.toByteArray()).build();

    }
}