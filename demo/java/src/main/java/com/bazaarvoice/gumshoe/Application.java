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
        Configuration configuration = new SimpleConfiguration("auto sales aggregator", "log/gumshoe.log");
        Gumshoe.configure(configuration);
        Gumshoe.get().context("session").start();

        List<Vehicle> vehicles = Vehicle.generateRandom(getVehicleCount());
        AutoSalesAggregator aggregator = new AutoSalesAggregator(vehicles);
        List<Aggregation> aggregations = aggregator.aggregate();

        Gumshoe.get().context("output").start();
        System.out.println(String.format("Aggregating sales data for %s vehicles...", vehicles.size()));
        for (Aggregation aggregation : aggregations) {
            System.out.println(aggregation);
        }
        Gumshoe.get().context().finish();
    }

    private static int getVehicleCount() {
        try {
            String envValue = System.getenv("MAX_VEHICLES");
            int max = Integer.parseInt(envValue);
            return (int)((max / 2) + (Math.random() * (max / 2)));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

}
