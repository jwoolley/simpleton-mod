package thesimpleton.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = DungeonMapScreen.class,
    method = "open",
    paramtypez = {
        boolean.class
    }
)
public class DungeonMapScreenOpenAfter {
  public static void Postfix (DungeonMapScreen __instance, boolean doScrollingAnimation) {
//    TheSimpletonMod.logger.debug("DungeonMapScreenOpenAfter invoked post trigger");
    if (!TheSimpletonMod.seasonScreen.isOpen() && !TheSimpletonMod.seasonScreen.wasDismissed()) {
//      TheSimpletonMod.logger.debug("DungeonMapScreenOpenAfter opening season screen");
      TheSimpletonMod.seasonScreen.open();
    } else {
//      TheSimpletonMod.logger.debug("DungeonMapScreenOpenAfter season screen was already dismissed; not opening");
    }
  }
}