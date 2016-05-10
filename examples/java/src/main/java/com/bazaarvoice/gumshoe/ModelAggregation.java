package com.bazaarvoice.gumshoe;

public class ModelAggregation extends Aggregation {
    private Model model;

    public ModelAggregation(Model model) {
        this.model = model;
    }

    @Override
    public String getAxis() {
        return String.format("Model: %s", model.toString());
    }

    public Model getModel() {
        return model;
    }
}
