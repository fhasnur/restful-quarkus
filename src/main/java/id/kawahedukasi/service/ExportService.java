package id.kawahedukasi.service;

import id.kawahedukasi.model.Item;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;

@ApplicationScoped
public class ExportService {
    public Response exportItem() throws JRException {
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
}