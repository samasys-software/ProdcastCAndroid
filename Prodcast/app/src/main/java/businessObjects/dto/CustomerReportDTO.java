package businessObjects.dto;

import java.util.List;

import businessObjects.domain.Order;

/**
 * Created by nandhini on 29/08/17.
 */

public class CustomerReportDTO extends  ProdcastDTO  {

    private String header,attributes,reportName;
    private List<Order> result;
    private Float amount,outstandingBalance,amountPaid;
    private String reportDates;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<Order> getResult() {
        return result;
    }

    public void setResult(List<Order> result) {
        this.result = result;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(Float outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public Float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getReportDates() {
        return reportDates;
    }

    public void setReportDates(String reportDates) {
        this.reportDates = reportDates;
    }
}