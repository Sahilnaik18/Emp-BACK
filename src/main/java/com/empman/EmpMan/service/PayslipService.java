package com.empman.EmpMan.service;

import com.empman.EmpMan.Entities.Employee;
import com.empman.EmpMan.Entities.Finance;
import com.empman.EmpMan.repository.FinanceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PayslipService {

    @Autowired
    private FinanceRepository financeRepository;

    public byte[] generatePayslipPdf(Employee employee) {
        List<Finance> lastSixMonthsPayslips = financeRepository.findLastSixMonthsByEmployee(employee.getId());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 50, 50); // Margins

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // 🔷 Add Company Logo & Name
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 3});

            try {
                Image logo = Image.getInstance(getClass().getClassLoader().getResource("static/logo.png"));
                logo.scaleAbsolute(80, 80);
                PdfPCell logoCell = new PdfPCell(logo);
                logoCell.setBorder(Rectangle.NO_BORDER);
                headerTable.addCell(logoCell);
            } catch (Exception e) {
                System.out.println("⚠ Logo not found, skipping image.");
                headerTable.addCell("");
            }

            Font companyFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(0, 102, 204));
            PdfPCell companyCell = new PdfPCell(new Phrase("EMP2\nEmployee Payslip", companyFont));
            companyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            companyCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(companyCell);

            document.add(headerTable);
            document.add(new Paragraph("\n"));
            document.add(new LineSeparator());

            // 🔷 Employee Details Section (with background color)
            PdfPTable empTable = new PdfPTable(2);
            empTable.setWidthPercentage(100);
            empTable.setSpacingBefore(10f);
            empTable.setSpacingAfter(10f);
            empTable.setWidths(new float[]{2, 3});

            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            BaseColor lightGray = new BaseColor(230, 230, 230);

            empTable.addCell(createCell("Employee Code:", boldFont, lightGray));
            empTable.addCell(createCell(employee.getEmpCode(), boldFont, lightGray));

            empTable.addCell(createCell("Name:", boldFont, lightGray));
            empTable.addCell(createCell(employee.getFullName(), normalFont, BaseColor.WHITE));

            empTable.addCell(createCell("Address:", boldFont, lightGray));
            empTable.addCell(createCell(employee.getAddress(), normalFont, BaseColor.WHITE));

            empTable.addCell(createCell("Mobile No:", boldFont, lightGray));
            empTable.addCell(createCell(employee.getMobile(), normalFont, BaseColor.WHITE));

            empTable.addCell(createCell("Email:", boldFont, lightGray));
            empTable.addCell(createCell(employee.getEmail(), normalFont, BaseColor.WHITE));

            document.add(empTable);
            document.add(new LineSeparator());

            // 🔷 Payslip Table with Alternating Row Colors
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2, 2, 2, 4});

            // 🔷 Table Headers
            BaseColor headerColor = new BaseColor(0, 102, 204);
            PdfPCell[] headers = {
                    new PdfPCell(new Phrase("Date", boldFont)),
                    new PdfPCell(new Phrase("Amount", boldFont)),
                    new PdfPCell(new Phrase("Type", boldFont)),
                    new PdfPCell(new Phrase("Description", boldFont))
            };
            for (PdfPCell header : headers) {
                header.setBackgroundColor(headerColor);
                header.setPadding(8);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setPhrase(new Phrase(header.getPhrase().getContent(), new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE)));
                table.addCell(header);
            }

            // 🔷 Add Payslip Data with Alternating Row Colors
            boolean alternateColor = false;
            for (Finance finance : lastSixMonthsPayslips) {
                BaseColor rowColor = alternateColor ? new BaseColor(245, 245, 245) : BaseColor.WHITE;
                table.addCell(createCell(finance.getDate() != null ? finance.getDate().toString() : "N/A", normalFont, rowColor));
                table.addCell(createCell("₹" + finance.getAmount(), normalFont, rowColor));
                table.addCell(createCell(finance.getType(), normalFont, rowColor));
                table.addCell(createCell(finance.getDescription(), normalFont, rowColor));
                alternateColor = !alternateColor;
            }

            document.add(table);

            // 🔷 Footer Section with Signature Line
            document.add(new Paragraph("\n"));
            PdfPTable footerTable = new PdfPTable(2);
            footerTable.setWidthPercentage(100);
            footerTable.setWidths(new float[]{3, 1});

            PdfPCell thankYouCell = new PdfPCell(new Phrase("Thank you for being a valued employee!\nGenerated by EMP2 Payroll System",
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.DARK_GRAY)));
            thankYouCell.setBorder(Rectangle.NO_BORDER);
            footerTable.addCell(thankYouCell);

            // 🟢 Add Signature Section with Stylish Font
            Font stylishFont = new Font(Font.FontFamily.COURIER, 14, Font.BOLDITALIC);
            Paragraph signature = new Paragraph("\n\nAuthorized Signatory\n", stylishFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            try {
                // 🟢 Add a Signature Image (if available)
                Image signatureImage = Image.getInstance(getClass().getClassLoader().getResource("static/signature.png"));
                signatureImage.scaleAbsolute(120, 50); // Adjust size
                signatureImage.setAlignment(Element.ALIGN_RIGHT);
                document.add(signatureImage);
            } catch (Exception e) {
                // 🟢 If Signature Image is Missing, Use a Stylish Line
                Paragraph line = new Paragraph("__________________________", stylishFont);
                line.setAlignment(Element.ALIGN_RIGHT);
                document.add(line);
            }

// 🟢 Add Founder Name in Stylish Font
            Font founderFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLDITALIC, new GrayColor(0.3f)); // Dark Gray for elegance
            Paragraph founderName = new Paragraph("Sahil Naik\nFounder", founderFont);
            founderName.setAlignment(Element.ALIGN_RIGHT);
            document.add(founderName);


            document.add(footerTable);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    // 🔷 Helper Method to Create Table Cells with Background Colors
    private PdfPCell createCell(String text, Font font, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}
