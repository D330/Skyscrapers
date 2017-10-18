package ru.flippy.skyscrapers.api.helper;

import java.util.ArrayList;

public class FixedSizeList<T> extends ArrayList<T> {

    public FixedSizeList(int initialCapacity) {
        super(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            super.add(null);
        }
    }

    public boolean isFilled() {
        for (int i = 0; i < super.size(); i++) {
            if (super.get(i) == null) {
                return false;
            }
        }
        return true;
    }
}
