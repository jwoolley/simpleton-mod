package thesimpleton.orbs.utilities;

import thesimpleton.orbs.AbstractCropOrb;

public class CropOrbHelper {
  static AbstractCropOrb highlightedOrb = null;

  public static boolean hasHighlightedOrb() {
    return highlightedOrb != null;
  }

  public static AbstractCropOrb getHighlightedOrb() {
    return highlightedOrb;
  }

  public static void setHighlightedOrb(AbstractCropOrb orb) {
    highlightedOrb = orb;
  }

  public static void clearHighlightedOrb() {
    highlightedOrb = null;
  }
}
