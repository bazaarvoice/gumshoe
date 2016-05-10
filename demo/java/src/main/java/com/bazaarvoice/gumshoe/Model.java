package com.bazaarvoice.gumshoe;

import java.math.BigDecimal;

public enum Model {
    RAV4(Manufacturer.TOYOTA, Type.SUV, 10000.0, 20000.0),
    CAMRY(Manufacturer.TOYOTA, Type.SEDAN, 13000.0, 24000.0),
    COROLLA(Manufacturer.TOYOTA, Type.SEDAN, 15000.0, 26000.0),
    TACOMA(Manufacturer.TOYOTA, Type.PICKUP, 13500.0, 25000.0),
    PRIUS(Manufacturer.TOYOTA, Type.HYBRID, 22000.0, 33000.0),
    PILOT(Manufacturer.HONDA, Type.SUV, 16000.0, 30000.0),
    ACCORD(Manufacturer.HONDA, Type.SEDAN, 15000.0, 28000.0),
    RIDGELINE(Manufacturer.HONDA, Type.PICKUP, 13000.0, 22000.0),
    CLARITY(Manufacturer.HONDA, Type.HYBRID, 22000.0, 34000.0),
    F150(Manufacturer.FORD, Type.PICKUP, 16500.0, 28000.0),
    FUSION(Manufacturer.FORD, Type.SEDAN, 11000.0, 19000.0),
    FOCUS_EL(Manufacturer.FORD, Type.HYBRID, 20000.0, 30000.0);

    private Manufacturer manufacturer;
    private Type type;
    private BigDecimal cost;
    private BigDecimal msrp;

    private Model(Manufacturer manufacturer, Type type, double cost, double msrp) {
        this.manufacturer = manufacturer;
        this.type = type;
        this.cost = new BigDecimal(cost);
        this.msrp = new BigDecimal(msrp);
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Type getType() {
        return type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public BigDecimal getMSRP() {
        return msrp;
    }

    public BigDecimal generateSalesPrice() {
        BigDecimal profit = new BigDecimal(Math.random() * msrp.subtract(cost).doubleValue());
        return cost.add(profit);
    }
}
