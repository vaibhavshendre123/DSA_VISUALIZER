import java.awt.*;

public class ThemeManager {
    public static final String MIDNIGHT_BLUE = "Midnight Blue";
    public static final String LIGHT_PRO = "Light Pro";
    public static final String TOKYO_NIGHT = "Tokyo Night";
    public static final String SOLARIZED_DARK = "Solarized Dark";
    public static final String HIGH_CONTRAST = "High Contrast";

    private String currentTheme = MIDNIGHT_BLUE;

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setTheme(String theme) {
        this.currentTheme = theme;
    }

    public Theme getTheme() {
        return getTheme(currentTheme);
    }

    public static Theme getTheme(String themeName) {
        switch (themeName) {
            case LIGHT_PRO:
                return new Theme(
                    new Color(250, 250, 250),
                    new Color(40, 40, 40),
                    new Color(255, 255, 255),
                    new Color(240, 240, 240),
                    new Color(70, 130, 180),
                    new Color(34, 139, 34),
                    new Color(220, 20, 60),
                    new Color(100, 149, 237),
                    new Color(128, 128, 128),
                    "Light"
                );
            case TOKYO_NIGHT:
                return new Theme(
                    new Color(26, 27, 38),
                    new Color(200, 200, 200),
                    new Color(32, 33, 45),
                    new Color(39, 40, 55),
                    new Color(137, 180, 250),
                    new Color(49, 194, 124),
                    new Color(248, 73, 96),
                    new Color(137, 180, 250),
                    new Color(96, 103, 123),
                    "Tokyo Night"
                );
            case SOLARIZED_DARK:
                return new Theme(
                    new Color(0, 43, 54),
                    new Color(238, 232, 213),
                    new Color(7, 54, 66),
                    new Color(0, 43, 54),
                    new Color(38, 139, 210),
                    new Color(133, 153, 0),
                    new Color(203, 75, 22),
                    new Color(38, 139, 210),
                    new Color(101, 123, 131),
                    "Solarized Dark"
                );
            case HIGH_CONTRAST:
                return new Theme(
                    new Color(0, 0, 0),
                    new Color(255, 255, 0),
                    new Color(20, 20, 20),
                    new Color(30, 30, 30),
                    new Color(0, 255, 255),
                    new Color(0, 255, 0),
                    new Color(255, 0, 0),
                    new Color(0, 255, 255),
                    new Color(255, 255, 255),
                    "High Contrast"
                );
            case MIDNIGHT_BLUE:
            default:
                return new Theme(
                    new Color(35, 45, 60),
                    new Color(255, 255, 255),
                    new Color(45, 55, 70),
                    new Color(50, 60, 75),
                    new Color(100, 149, 237),
                    new Color(100, 255, 100),
                    new Color(255, 100, 100),
                    new Color(70, 130, 180),
                    new Color(150, 150, 150),
                    "Midnight Blue"
                );
        }
    }

    public static String[] getAllThemes() {
        return new String[] {
            MIDNIGHT_BLUE,
            LIGHT_PRO,
            TOKYO_NIGHT,
            SOLARIZED_DARK,
            HIGH_CONTRAST
        };
    }

    public static class Theme {
        public Color background;
        public Color foreground;
        public Color panelBackground;
        public Color panelBackgroundSecondary;
        public Color accent;
        public Color success;
        public Color error;
        public Color buttonBackground;
        public Color textSecondary;
        public String name;

        public Theme(Color background, Color foreground, Color panelBackground, 
                     Color panelBackgroundSecondary, Color accent, Color success,
                     Color error, Color buttonBackground, Color textSecondary, String name) {
            this.background = background;
            this.foreground = foreground;
            this.panelBackground = panelBackground;
            this.panelBackgroundSecondary = panelBackgroundSecondary;
            this.accent = accent;
            this.success = success;
            this.error = error;
            this.buttonBackground = buttonBackground;
            this.textSecondary = textSecondary;
            this.name = name;
        }
    }
}