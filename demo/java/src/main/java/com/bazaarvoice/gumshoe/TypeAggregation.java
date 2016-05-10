package com.bazaarvoice.gumshoe;

public class TypeAggregation extends Aggregation {
    private Type type;

    public TypeAggregation(Type type) {
        this.type = type;
    }

    @Override
    public String getAxis() {
        return String.format("Type: %s", type.toString());
    }

    public Type getType() {
        return type;
    }
}
