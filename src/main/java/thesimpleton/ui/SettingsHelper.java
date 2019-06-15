package thesimpleton.ui;

import com.megacrit.cardcrawl.core.Settings;
public class SettingsHelper {
  public static float getScaleX() {
    return Settings.scale;
  }

  public static float getScaleY() {
    return Settings.HEIGHT / 1200.0F;
  }
}
