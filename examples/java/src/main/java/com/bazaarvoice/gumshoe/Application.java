package com.bazaarvoice.gumshoe;

import java.util.List;

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
        Configuration configuration = new SimpleConfiguration("auto sales aggregator", "gumshoe.log");
        Gumshoe.configure(configuration);
        Gumshoe.get().context("session").start();

        List<Vehicle> vehicles = Vehicle.generateRandom(1);
        AutoSalesAggregator aggregator = new AutoSalesAggregator(vehicles);
        List<Aggregation> aggregations = aggregator.aggregate();

        Gumshoe.get().context("output").start();
        for (Aggregation aggregation : aggregations) {
            System.out.println(aggregation);
        }
        Gumshoe.get().context().finish();
    }


}
