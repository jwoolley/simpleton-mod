package thesimpleton.utilities;

import com.badlogic.gdx.graphics.Color;

public class SimpletonColorUtil {
    public static final Color RED_BORDER_GLOW_COLOR;
    public static final Color SEASONAL_CURSE_GLOW_COLOR;
    public static final Color INVISIBLE_GLOW_COLOR;
    static {
        RED_BORDER_GLOW_COLOR = new Color(1.0F, 0.0F, 0.0F, 0.25F);
        SEASONAL_CURSE_GLOW_COLOR = RED_BORDER_GLOW_COLOR;
        INVISIBLE_GLOW_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.0F);
    }
}
