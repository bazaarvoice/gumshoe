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
        AutoSalesAggregator aggregator = new AutoSalesAggregator(vehicles);
        List<Aggregation> aggregations = aggregator.aggregate();

        for (Aggregation aggregation : aggregations) {
            System.out.println(aggregation);
        }
    }


}
