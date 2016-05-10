package com.bazaarvoice.gumshoe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Vehicle {
    private Model model;
    private BigDecimal purchasePrice;

    public Vehicle(Model model) {
        this.model = model;
        purchasePrice = model.generateSalesPrice();
    }

    public Manufacturer getManufacturer() {
        return model.getManufacturer();
    }

    public Type getType() {
        return model.getType();
    }

    public Model getModel() {
        return model;
    }

    public BigDecimal getCost() {
        return model.getCost();
    }

    public BigDecimal getMSRP() {
        return model.getMSRP();
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public static Vehicle generateRandom() {
        Model[] models = Model.values();
        Model model = models[(int)(Math.random() * models.length)];
        return new Vehicle(model);
    }

    public static List<Vehicle> generateRandom(int count) {
        List<Vehicle> results = new ArrayList<Vehicle>();

        for (int i = 0; i < count; i++) {
            results.add(generateRandom());
        }

        return results;
    }
}
