package com.spooky.pvpbot;

public class AdaptiveMemory {
    private int attempts = 1;
    private int hits = 0;

    public double getAggression() {
        double base = 0.4;
        double acc = getAccuracy();
        return Math.min(1.0, base + acc * 0.9);
    }

    public double getAccuracy() {
        return hits / (double) attempts;
    }

    public void recordHit(boolean hit) {
        attempts++;
        if (hit) hits++;
        if (attempts > 10000) {
            attempts /= 2;
            hits /= 2;
        }
    }

    public void adjustParameters() {
        // Extend adaptive behavior here
    }
}
