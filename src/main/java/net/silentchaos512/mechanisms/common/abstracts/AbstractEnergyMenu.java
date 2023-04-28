package net.silentchaos512.mechanisms.common.abstracts;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.utils.MathUtils;

public abstract class AbstractEnergyMenu extends BaseMenuContainer {
    protected final Inventory inventory;
    private final ContainerData data;

    protected AbstractEnergyMenu(MenuType<?> pMenuType, int pContainerId, Inventory inventory, Container container, Block blockAccess, ContainerData data) {
        super(pMenuType, pContainerId, inventory, container, blockAccess, data);

        this.inventory = inventory;
        this.data = data;
        super.addDataSlots(data);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    public ContainerData getData() {
        return data;
    }

    public int getEnergyStored() {
        int upper = data.get(1) & 0xFFFF;
        int lower = data.get(0) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getMaxEnergyStored() {
        int upper = data.get(3) & 0xFFFF;
        int lower = data.get(2) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getEnergyBarHeight() {
        int max = getMaxEnergyStored();
        int energyClamped = MathUtils.clamp(getEnergyStored(), 0, max);
        return max > 0 ? 50 * energyClamped / max : 0;
    }
}
