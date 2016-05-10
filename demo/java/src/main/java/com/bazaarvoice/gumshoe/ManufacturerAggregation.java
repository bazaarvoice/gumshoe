package com.bazaarvoice.gumshoe;

public class ManufacturerAggregation extends Aggregation {
    private Manufacturer manufacturer;

    public ManufacturerAggregation(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String getAxis() {
        return String.format("Manufacturer: %s", manufacturer.toString());
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }
}
