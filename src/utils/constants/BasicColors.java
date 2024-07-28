package utils.constants;

public class BasicColors {

    public static final int BLACK = 0x000000;
    public static final int WHITE = 0xFFFFFF;
    public static final int RED = 0xFF0000;
    public static final int GREEN = 0x00FF00;
    public static final int BLUE = 0x3498eb;//#3498eb
    public static final int YELLOW = 0xFFFF00;
    public static final int CYAN = 0x00FFFF;
    public static final int MAGENTA = 0xFF00FF;
    public static final int GRAY = 0x808080;
    public static final int ORANGE = 0xFFA500;
    public static final int PINK = 0xFFC0CB;
    public static final int BROWN = 0xA52A2A;
    public static final int PURPLE = 0x6134eb;//#6134eb
    public static final String backgroundColor = String.format("#%06X", BLUE);
    public static final String borderColorHex = String.format("#%06X", WHITE);
    public static final String textColorHex = String.format("#%06X", WHITE);

    public static String generateStyle(
            int borderWidth,
            int borderRadius,
            int backgroundRadius,
            int fontSize
    ) {
        return String.format(
                "-fx-background-color: %s; "
                + "-fx-border-color: %s; "
                + "-fx-border-width: %dpx; "
                + "-fx-border-radius: %dpx; "
                + "-fx-background-radius: %dpx; "
                + "-fx-font-size: %dpx; "
                + "-fx-text-fill: %s; "
                + "-fx-font-weight: bold;",
                backgroundColor, borderColorHex, borderWidth, borderRadius, backgroundRadius, fontSize, textColorHex
        );
    }
}
   




