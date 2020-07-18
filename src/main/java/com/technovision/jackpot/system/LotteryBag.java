package com.technovision.jackpot.system;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class LotteryBag<UUID> {

    private class Entry {
        double accumulatedWeight;
        UUID object;
    }

    private List<Entry> entries = new ArrayList<Entry>();
    private double accumulatedWeight;
    private Random rand = new Random();

    public void addEntry(UUID object, double weight) {
        accumulatedWeight += weight;
        Entry e = new Entry();
        e.object = object;
        e.accumulatedWeight = accumulatedWeight;
        entries.add(e);
    }

    public UUID getRandom() {
        double r = rand.nextDouble() * accumulatedWeight;
        for (Entry entry: entries) {
            if (entry.accumulatedWeight >= r) {
                return entry.object;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }
}
