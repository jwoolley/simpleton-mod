package thesimpleton.orbs.utilities;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

  public static boolean hasHoveredOrb() {
    return AbstractDungeon.player != null
            && AbstractDungeon.player.orbs.size() > 0
            && AbstractDungeon.player.orbs.stream().anyMatch(orb -> orb.hb.hovered);
  }
}
