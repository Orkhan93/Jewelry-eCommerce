package az.spring.ecommerce.service;

import az.spring.ecommerce.constant.CommerceConstant;
import az.spring.ecommerce.model.Bill;
import az.spring.ecommerce.repository.BillRepository;
import az.spring.ecommerce.security.JwtRequestFilter;
import az.spring.ecommerce.utils.CommerceUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final JwtRequestFilter jwtRequestFilter;

    public ResponseEntity<String> generateReport(Map<String, Object> billRequest) {
        log.info("Inside generateReport {}", billRequest);
        try {
            String fileName;
            if (validateBillRequest(billRequest)) {
                if (billRequest.containsKey("isGenerate") && !(Boolean) billRequest.get("isGenerate")) {
                    fileName = (String) billRequest.get("uuid");
                } else {
                    fileName = CommerceUtil.getUUID();
                    billRequest.put("uuid", fileName);
                    insertBill(billRequest);
                }
                String data = "Name: " + billRequest.get("name") + "\n"
                        + "Contact Number: " + billRequest.get("contactNumber") + "\n"
                        + "Email: " + billRequest.get("email") + "\n"
                        + "Payment Method: " + billRequest.get("paymentMethod");
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream
                        (CommerceConstant.STORE_LOCATION + "\\" + fileName + ".pdf"));
                document.open();
                setRectangleInPdf(document);

                Paragraph chunk = new Paragraph("E-commerce", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = CommerceUtil.getJsonArrayFromString((String) billRequest.get("productDetail"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    addRows(table, CommerceUtil.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total : " + billRequest.get("totalAmount") + "\n"
                        + "Thank you for visiting.PLease visit again !", getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);

            } else
                return CommerceUtil.getResponseMessage("Required data not found.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommerceUtil.getResponseMessage(CommerceConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            header.setBackgroundColor(BaseColor.YELLOW);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf {}", document);
        Rectangle rectangle = new Rectangle(577, 825, 18, 15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    private void insertBill(Map<String, Object> billRequest) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) billRequest.get("uuid"));
            bill.setName((String) billRequest.get("name"));
            bill.setEmail((String) billRequest.get("email"));
            bill.setContactNumber((String) billRequest.get("contactNumber"));
            bill.setPaymentMethod((String) billRequest.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) billRequest.get("totalAmount")));
            bill.setProductDetail((String) billRequest.get("productDetail"));
            bill.setCreatedBy(jwtRequestFilter.getCurrentUser());
            billRepository.save(bill);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateBillRequest(Map<String, Object> billRequest) {
        return billRequest.containsKey("name") && billRequest.containsKey("contactNumber")
                && billRequest.containsKey("email") && billRequest.containsKey("paymentMethod")
                && billRequest.containsKey("productDetail") && billRequest.containsKey("totalAmount");
    }

    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jwtRequestFilter.isAdmin()) {
            list = billRepository.getAllBills();
        } else {
            list = billRepository.getBillByUserName(jwtRequestFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> getPdf(Map<String, Object> billRequest) {
        log.info("Inside getPdf : billRequest {}", billRequest);
        try {
            byte[] byteArray = new byte[0];
            if (!billRequest.containsKey("uuid") && validateBillRequest(billRequest))
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            String filePath = CommerceConstant.STORE_LOCATION + "\\" + (String) billRequest.get("uuid") + ".pdf";
            if (CommerceUtil.isFileExist(filePath)) {
                byteArray = getByArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            } else {
                billRequest.put("isGenerate", false);
                generateReport(billRequest);
                byteArray = getByArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] getByArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    public ResponseEntity<String> deleteBill(Long id) {
            Optional<Bill> optionalBill = billRepository.findById(id);
            if (!optionalBill.isEmpty()) {
                billRepository.deleteById(id);
                return CommerceUtil.getResponseMessage("Bill Deleted Successfully.", HttpStatus.OK);
            }
            return CommerceUtil.getResponseMessage("Bill id does not exist.", HttpStatus.OK);
    }

}