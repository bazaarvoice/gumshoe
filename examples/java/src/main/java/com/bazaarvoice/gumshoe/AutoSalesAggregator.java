package com.bazaarvoice.gumshoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoSalesAggregator {
    private List<Vehicle> vehicles;

    public AutoSalesAggregator(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Aggregation> aggregate() {
        Map<Manufacturer, ManufacturerAggregation> manufacturerAggs = buildManufacturerAggregations();
        Map<Type, TypeAggregation> typeAggs = buildTypeAggregations();
        Map<Model, ModelAggregation> modelAggs = buildModelAggregations();
        List<Aggregation> aggregations = new ArrayList<Aggregation>();

        for (Vehicle vehicle : vehicles) {
            addToAggregation(vehicle, manufacturerAggs.get(vehicle.getManufacturer()));
            addToAggregation(vehicle, typeAggs.get(vehicle.getType()));
            addToAggregation(vehicle, modelAggs.get(vehicle.getModel()));
        }

        aggregations.addAll(manufacturerAggs.values());
        aggregations.addAll(typeAggs.values());
        aggregations.addAll(modelAggs.values());

        return aggregations;
    }

    private void addToAggregation(Vehicle vehicle, Aggregation modelAgg) {
        modelAgg.addTotalCost(vehicle.getCost());
        modelAgg.addTotalMSRP(vehicle.getMSRP());
        modelAgg.addTotalSalesPrice(vehicle.getPurchasePrice());
    }

    private Map<Model, ModelAggregation> buildModelAggregations() {
        Map<Model, ModelAggregation> modelAggs = new HashMap<Model, ModelAggregation>();

        for (Model model : Model.values()) {
            modelAggs.put(model, new ModelAggregation(model));
        }

        return modelAggs;
    }

    private Map<Manufacturer, ManufacturerAggregation> buildManufacturerAggregations() {
        Map<Manufacturer, ManufacturerAggregation> manufacturerAggs = new HashMap<Manufacturer, ManufacturerAggregation>();

        for (Manufacturer manufacturer : Manufacturer.values()) {
            manufacturerAggs.put(manufacturer, new ManufacturerAggregation(manufacturer));
        }

        return manufacturerAggs;
    }

    private Map<Type, TypeAggregation> buildTypeAggregations() {
        Map<Type, TypeAggregation> typeAggs = new HashMap<Type, TypeAggregation>();

        for (Type type : Type.values()) {
            typeAggs.put(type, new TypeAggregation(type));
        }

        return typeAggs;
    }
}
