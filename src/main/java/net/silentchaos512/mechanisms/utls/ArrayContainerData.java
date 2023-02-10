package net.silentchaos512.mechanisms.utls;

import net.minecraft.world.inventory.ContainerData;

public class ArrayContainerData implements ContainerData {
    private final int[] values;

    public ArrayContainerData(int... values) {
        if (values.length == 0) throw new IllegalStateException("ContainerData is missing values");
        this.values = values;
    }

    @Override
    public int get(int pIndex) {
        return values[pIndex];
    }

    @Override
    public void set(int pIndex, int pValue) {
        this.values[pIndex] = pValue;
    }

    @Override
    public int getCount() {
        return values.length;
    }
}
