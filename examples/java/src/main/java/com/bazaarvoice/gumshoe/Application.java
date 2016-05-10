package com.bazaarvoice.gumshoe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application  {
    /**
     * Build some random vehicles
     *
     * Aggregate the total cost, MSRP, sales price and profit by Manufacturer, Type and Model.
     * Aggregation results are displayed to standard out.
     *
     * @param args
     */
    public static void main( String[] args ) {
        List<Vehicle> vehicles = Vehicle.generateRandom(100000);
        Map<Manufacturer, ManufacturerAggregation> manufacturerAggs = buildManufacturerAggregations();
        Map<Type, TypeAggregation> typeAggs = buildTypeAggregations();
        Map<Model, ModelAggregation> modelAggs = buildModelAggregations();

        for (Vehicle vehicle : vehicles) {
            addToAggregation(vehicle, manufacturerAggs.get(vehicle.getManufacturer()));
            addToAggregation(vehicle, typeAggs.get(vehicle.getType()));
            addToAggregation(vehicle, modelAggs.get(vehicle.getModel()));
        }

        List<Aggregation> aggregations = new ArrayList<Aggregation>();
        aggregations.addAll(manufacturerAggs.values());
        aggregations.addAll(typeAggs.values());
        aggregations.addAll(modelAggs.values());

        for (Aggregation aggregation : aggregations) {
            System.out.println(aggregation);
        }
    }

    private static void addToAggregation(Vehicle vehicle, Aggregation modelAgg) {
        modelAgg.addTotalCost(vehicle.getCost());
        modelAgg.addTotalMSRP(vehicle.getMSRP());
        modelAgg.addTotalSalesPrice(vehicle.getPurchasePrice());
    }

    private static Map<Model, ModelAggregation> buildModelAggregations() {
        Map<Model, ModelAggregation> modelAggs = new HashMap<Model, ModelAggregation>();

        for (Model model : Model.values()) {
            modelAggs.put(model, new ModelAggregation(model));
        }

        return modelAggs;
    }

    private static Map<Manufacturer, ManufacturerAggregation> buildManufacturerAggregations() {
        Map<Manufacturer, ManufacturerAggregation> manufacturerAggs = new HashMap<Manufacturer, ManufacturerAggregation>();

        for (Manufacturer manufacturer : Manufacturer.values()) {
            manufacturerAggs.put(manufacturer, new ManufacturerAggregation(manufacturer));
        }

        return manufacturerAggs;
    }

    private static Map<Type, TypeAggregation> buildTypeAggregations() {
        Map<Type, TypeAggregation> typeAggs = new HashMap<Type, TypeAggregation>();

        for (Type type : Type.values()) {
            typeAggs.put(type, new TypeAggregation(type));
        }

        return typeAggs;
    }
}
