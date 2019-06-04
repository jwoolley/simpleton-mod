package thesimpleton.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = DungeonMapScreen.class,
    method = "update"
)
public class DungeonMapScreenUpdateAfter {
  public static void Postfix (DungeonMapScreen __instance) {
    TheSimpletonMod.logger.debug("DungeonMapScreenUpdateAfter invoked post trigger");
    if (TheSimpletonMod.seasonScreen.isOpen()) {
      TheSimpletonMod.logger.debug("DungeonMapScreenUpdateAfter season screen open; updating");
      TheSimpletonMod.seasonScreen.update();
    } else {
      TheSimpletonMod.logger.debug("DungeonMapScreenUpdateAfter season screen not open; not updating");
    }
  }
}