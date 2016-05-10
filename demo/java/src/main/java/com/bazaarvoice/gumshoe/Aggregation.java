package com.bazaarvoice.gumshoe;

import java.math.BigDecimal;
import java.text.NumberFormat;

public abstract class Aggregation {
    private BigDecimal totalCost;
    private BigDecimal totalMSRP;
    private BigDecimal totalSalesPrice;
    
    public Aggregation() {
        totalCost = new BigDecimal(0);
        totalMSRP = new BigDecimal(0);
        totalSalesPrice = new BigDecimal(0);
    }
    
    public abstract String getAxis();
    
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    
    public void addTotalCost(BigDecimal cost) {
        totalCost = totalCost.add(cost);
    }
    
    public BigDecimal getTotalMSRP() {
        return totalMSRP;
    }
    
    public void addTotalMSRP(BigDecimal msrp) {
        totalMSRP = totalMSRP.add(msrp);
    }
    
    public BigDecimal getTotalSalesPrice() {
        return totalSalesPrice;
    }
    
    public void addTotalSalesPrice(BigDecimal salesPrice) {
        totalSalesPrice = totalSalesPrice.add(salesPrice);
    }
    
    public BigDecimal getNetProfit() {
        return totalSalesPrice.subtract(totalCost);
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append(getAxis())
                                  .append("\n")
                                  .append("  totalCost: ")
                                  .append(NumberFormat.getCurrencyInstance().format(totalCost))
                                  .append("\n")
                                  .append("  totalMSRP: ")
                                  .append(NumberFormat.getCurrencyInstance().format(totalMSRP))
                                  .append("\n")
                                  .append("  totalSalesPrice: ")
                                  .append(NumberFormat.getCurrencyInstance().format(totalSalesPrice))
                                  .append("\n")
                                  .append("  netProfit: ")
                                  .append(NumberFormat.getCurrencyInstance().format(getNetProfit()))
                                  .toString();
    }
}
