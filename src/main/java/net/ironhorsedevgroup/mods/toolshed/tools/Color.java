package net.ironhorsedevgroup.mods.toolshed.tools;

public class Color {
    public static int getIntFromRGB(Integer red, Integer green, Integer blue) {
        return Integer.parseInt(String.valueOf(((red.byteValue() & 255) << 16) + ((green.byteValue() & 255) << 8) + (blue.byteValue() & 255)));
    }
}
