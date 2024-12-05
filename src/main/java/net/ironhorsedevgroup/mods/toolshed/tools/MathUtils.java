package net.ironhorsedevgroup.mods.toolshed.tools;

public class MathUtils {
    public static Double getBellCurveAtX(double height, double width, double center, double x) {
        return height * java.lang.Math.pow(2, java.lang.Math.pow(x - center, 2) / (-width));
    }
}
