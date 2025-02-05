package com.empman.EmpMan.service;

import com.empman.EmpMan.Entities.Finance;
import com.empman.EmpMan.repository.FinanceRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PayslipService {

    @Autowired
    private FinanceRepository financeRepository;

    public byte[] generatePayslipPdf(Long employeeId) {
        // Fetch last six months payslips
        List<Finance> lastSixMonthsPayslips = financeRepository.findLastSixMonthsByEmployee(employeeId);

        // Create a ByteArrayOutputStream to hold the PDF content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Create Document instance
        Document document = new Document();

        try {
            // Create PdfWriter instance
            PdfWriter.getInstance(document, outputStream);

            // Open the document for writing content
            document.open();

            // Add header information
            document.add(new Paragraph("Payslip Report - Last 6 Months"));
            document.add(new Paragraph("Employee ID: " + employeeId));

            // Loop through the last 6 months payslips and add details
            for (Finance finance : lastSixMonthsPayslips) {
                document.add(new Paragraph("Date: " + finance.getDate()));
                document.add(new Paragraph("Amount: " + finance.getAmount()));
                document.add(new Paragraph("Type: " + finance.getType()));
                document.add(new Paragraph("------------------------------------"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            // Close the document after writing
            document.close();
        }

        // Return the PDF byte array
        return outputStream.toByteArray();
    }
}
