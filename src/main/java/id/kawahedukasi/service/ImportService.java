package id.kawahedukasi.service;

import id.kawahedukasi.dto.FileFormDTO;
import id.kawahedukasi.model.Item;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class ImportService {
    @Transactional
    public Response importExcel(FileFormDTO request) throws IOException {
        //create object array input
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(request.file);

        //create new workbook by byteArrayInputStream
        XSSFWorkbook workbook = new XSSFWorkbook(byteArrayInputStream);

        //get sheet "data"
        XSSFSheet sheet = workbook.getSheetAt(0);

        //remove header excel
        sheet.removeRow(sheet.getRow(0));

        List<Item> toPersist = new ArrayList<>();
        //for each row
        for (Row row : sheet) {
            Item item = new Item();
            item.name = row.getCell(0).getStringCellValue();
            item.count = (int) row.getCell(1).getNumericCellValue();
            item.price = (int) row.getCell(2).getNumericCellValue();
            item.type = row.getCell(3).getStringCellValue();
            item.description = row.getCell(4).getStringCellValue();
            toPersist.add(item);
        }
        Item.persist(toPersist);
        return Response.status(Response.Status.CREATED).entity(new HashMap<>()).build();
    }
}