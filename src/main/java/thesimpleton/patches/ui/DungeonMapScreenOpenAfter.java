package thesimpleton.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
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
  static int counter = 0;

  static final int LOG_INTERVAL = 10;

  static void log(String msg) {
//    if (counter < LOG_INTERVAL) {
//      counter++;
//    } else {
//      counter = 0;
//    }
//
//    if (counter == 0) {
//      TheSimpletonMod.logger.debug(msg);
//    }
      TheSimpletonMod.logger.debug(msg);
  }

//  public static SpireReturn Prefix (DungeonMapScreen __instance, boolean doScrollingAnimation) {
//    if (TheSimpletonMod.isPlayingAsSimpleton()) {
//      log("DungeonMapScreenOpenBefore invoked pre trigger.");
//
//      log("DungeonMapScreenOpenBefore seasonScreen.isOpen: "
//          + TheSimpletonMod.seasonScreen.isOpen()
//          + " seasonScreen.wasDismissed: "
//          + TheSimpletonMod.seasonScreen.wasDismissed());
//
//      if (!TheSimpletonMod.seasonScreen.isOpen() && !TheSimpletonMod.seasonScreen.wasDismissed()) {
//        return SpireReturn.Return(null);
//      } else {
//        log("DungeonMapScreenOpenBefore season screen  not open, continuing");
//      }
//    }
//    return SpireReturn.Continue();
//  }

  public static void Postfix (DungeonMapScreen __instance, boolean doScrollingAnimation) {
    if (TheSimpletonMod.isPlayingAsSimpleton()) {
      log("DungeonMapScreenOpenAfter invoked post trigger.");

      log("DungeonMapScreenOpenAfter seasonScreen.isOpen: "
          + TheSimpletonMod.seasonScreen.isOpen()
          + " seasonScreen.wasDismissed: "
          + TheSimpletonMod.seasonScreen.wasDismissed());

      if (!TheSimpletonMod.seasonScreen.isOpen() && !TheSimpletonMod.seasonScreen.wasDismissed()) {
        log("DungeonMapScreenOpenAfter opening season screen");
        TheSimpletonMod.seasonScreen.open();
      } else {
        log("DungeonMapScreenOpenAfter season screen was already dismissed; not opening");
      }
    }
  }
}