package net.ironhorsedevgroup.mods.toolshed.tools;

import java.util.List;

public class Color {
    public static int getIntFromRGB(Integer red, Integer green, Integer blue) {
        return Integer.parseInt(String.valueOf(((red.byteValue() & 255) << 16) + ((green.byteValue() & 255) << 8) + (blue.byteValue() & 255)));
    }

    public static int getIntFromRGB(List<Integer> color) {
        return getIntFromRGB(color.get(0), color.get(1), color.get(2));
    }
}
