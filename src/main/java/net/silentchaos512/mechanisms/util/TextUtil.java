package net.silentchaos512.mechanisms.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.silentchaos512.mechanisms.SilentMechanisms;

public class TextUtil {
    private static final String ENERGY_FORMAT = "%,d";

    public static ITextComponent translate(String prefix, String suffix, Object... params) {
        String key = String.format("%s.%s.%s", prefix, SilentMechanisms.MOD_ID, suffix);
        return new TranslationTextComponent(key, params);
    }

    public static ITextComponent energy(int amount) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        return translate("misc", "energy", s1);
    }

    public static ITextComponent energyPerTick(int amount) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        return translate("misc", "energyPerTick", s1);
    }

    public static ITextComponent energyWithMax(int amount, int max) {
         String s1 = String.format(ENERGY_FORMAT, amount);
         String s2 = String.format(ENERGY_FORMAT, max);
        return translate("misc", "energyWithMax", s1, s2);
    }

    public static ITextComponent fluidWithMax(IFluidHandler fluidHandler, int tank) {
        FluidStack stack = fluidHandler.getFluidInTank(tank);
        return fluidWithMax(stack, fluidHandler.getTankCapacity(tank));
    }

    public static ITextComponent fluidWithMax(FluidStack stack, int tankCapacity) {
        ITextComponent fluidName = stack.getDisplayName();
        String s1 = String.format(ENERGY_FORMAT, stack.getAmount());
        String s2 = String.format(ENERGY_FORMAT, tankCapacity);
        return translate("misc", "fluidWithMax", fluidName, s1, s2);
    }
}
