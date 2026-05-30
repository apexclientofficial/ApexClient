package com.apex.client.setting;

public class NumberSetting extends Setting {

    private double value;
    private final double min;
    private final double max;
    private final double increment;

    public NumberSetting(String name, double defaultValue, double min, double max, double increment) {
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        double rounded = Math.round(value / increment) * increment;
        this.value = Math.max(min, Math.min(max, rounded));
    }

    public float getFloatValue() {
        return (float) value;
    }

    public int getIntValue() {
        return (int) value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }

    public double getPercentage() {
        return (value - min) / (max - min);
    }

    public void setFromPercentage(double percentage) {
        setValue(min + (max - min) * percentage);
    }
}
