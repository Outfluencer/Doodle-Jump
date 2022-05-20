package xyz.doodlejump;

public class ColorChanger
{
    public static int r;
    public static int g;
    public static int b;
    static boolean rUp;
    static boolean gUp;
    static boolean bUp;
    
    public static void changeColor() {
        if (rUp) {
            if (r != 255 && ++r == 255) {
                bUp = false;
            }
        }
        else if (r != 0 && --r == 0) {
            bUp = true;
        }
        if (gUp) {
            if (g != 255 && ++g == 255) {
                rUp = false;
            }
        }
        else if (g != 0 && --g == 0) {
            rUp = true;
        }
        if (bUp) {
            if (b != 255 && ++b == 255) {
                gUp = false;
            }
        }
        else if (b != 0 && --b == 0) {
            gUp = true;
        }
    }
    
    static {
        r = 255;
        g = 0;
        b = 0;
        rUp = true;
        gUp = true;
        bUp = false;
    }
}
