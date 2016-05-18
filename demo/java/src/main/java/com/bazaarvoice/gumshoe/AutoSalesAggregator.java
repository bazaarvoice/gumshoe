package com.bazaarvoice.gumshoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoSalesAggregator {
    private List<Vehicle> vehicles;
    private List<String> errorMessages;

    public AutoSalesAggregator(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
        this.errorMessages = Arrays.asList(
            "Insufficient data",
            "Malformed data",
            "Dependent service unavailable",
            "Unknown"
        );
    }

    public List<Aggregation> aggregate() {
        Map<Manufacturer, ManufacturerAggregation> manufacturerAggs = buildManufacturerAggregations();
        Map<Type, TypeAggregation> typeAggs = buildTypeAggregations();
        Map<Model, ModelAggregation> modelAggs = buildModelAggregations();

        aggregateVehicleData(manufacturerAggs, typeAggs, modelAggs);
        return collectAggregations(manufacturerAggs, typeAggs, modelAggs);
    }

    private List<Aggregation> collectAggregations(
            Map<Manufacturer, ManufacturerAggregation> manufacturerAggs,
            Map<Type, TypeAggregation> typeAggs, Map<Model, ModelAggregation> modelAggs) {
        List<Aggregation> aggregations = new ArrayList<Aggregation>();

        Gumshoe.get().context("aggregating aggregations").start();
        aggregations.addAll(manufacturerAggs.values());
        aggregations.addAll(typeAggs.values());
        aggregations.addAll(modelAggs.values());
        Gumshoe.get().context().finish();

        return aggregations;
    }

    private void aggregateVehicleData(Map<Manufacturer, ManufacturerAggregation> manufacturerAggs,
            Map<Type, TypeAggregation> typeAggs, Map<Model, ModelAggregation> modelAggs) {
        Gumshoe.get().context("processing vehicles").start();

        for (Vehicle vehicle : vehicles) {
            pauseAMoment();
            Gumshoe.get().context().put("manufacturer", vehicle.getManufacturer())
                                   .put("vehicle_type", vehicle.getType())
                                   .put("model", vehicle.getModel())
                                   .put("cost", vehicle.getCost())
                                   .put("MSRP", vehicle.getMSRP())
                                   .put("purchase_price", vehicle.getPurchasePrice());

            try {
                addToAggregation(vehicle, manufacturerAggs.get(vehicle.getManufacturer()));
                addToAggregation(vehicle, typeAggs.get(vehicle.getType()));
                addToAggregation(vehicle, modelAggs.get(vehicle.getModel()));
                Gumshoe.get().emit("processed_vehicle");
            } catch (RuntimeException e) {
                Gumshoe.get().context().put("error_message", e.getMessage())
                                       .emit("failed_vehicle");
            }
        }

        Gumshoe.get().context().finish();
    }

    private void addToAggregation(Vehicle vehicle, Aggregation modelAgg) {
        // Simulate some errors
        if (shouldError()) {
            throw new RuntimeException(randomErrorMessage());
        }

        modelAgg.addTotalCost(vehicle.getCost());
        modelAgg.addTotalMSRP(vehicle.getMSRP());
        modelAgg.addTotalSalesPrice(vehicle.getPurchasePrice());
    }

    private void pauseAMoment() {
        int duration = (int)(Math.random() * 10);
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // do nothing
        }
    }

    private boolean shouldError() {
        // Maintain ~2% error rate
        return ((Math.random() * 100) < 2);
    }

    private String randomErrorMessage() {
        int index = (int)(Math.random() * errorMessages.size());
        return errorMessages.get(index);
    }

    private Map<Model, ModelAggregation> buildModelAggregations() {
        Gumshoe.get().context("initializing model aggregations").start();

        Map<Model, ModelAggregation> modelAggs = new HashMap<Model, ModelAggregation>();

        for (Model model : Model.values()) {
            modelAggs.put(model, new ModelAggregation(model));
        }

        Gumshoe.get().context().finish();

        return modelAggs;
    }

    private Map<Manufacturer, ManufacturerAggregation> buildManufacturerAggregations() {
        Gumshoe.get().context("initializing model aggregations").start();

        Map<Manufacturer, ManufacturerAggregation> manufacturerAggs = new HashMap<Manufacturer, ManufacturerAggregation>();

        for (Manufacturer manufacturer : Manufacturer.values()) {
            manufacturerAggs.put(manufacturer, new ManufacturerAggregation(manufacturer));
        }

        Gumshoe.get().context().finish();

        return manufacturerAggs;
    }

    private Map<Type, TypeAggregation> buildTypeAggregations() {
        Gumshoe.get().context("initializing type aggregations").start();

        Map<Type, TypeAggregation> typeAggs = new HashMap<Type, TypeAggregation>();

        for (Type type : Type.values()) {
            typeAggs.put(type, new TypeAggregation(type));
        }

        Gumshoe.get().context().finish();

        return typeAggs;
    }
}
