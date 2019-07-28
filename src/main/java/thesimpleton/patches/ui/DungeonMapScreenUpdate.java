package thesimpleton.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = DungeonMapScreen.class,
    method = "update"
)
public class DungeonMapScreenUpdate {
  public static SpireReturn Prefix (DungeonMapScreen __instance) {
    if (TheSimpletonMod.isPlayingAsSimpleton()) {
      if (TheSimpletonMod.seasonScreen.isOpen()) {
        return SpireReturn.Return(null);
      }
    }
    return SpireReturn.Continue();
  }

  public static void Postfix (DungeonMapScreen __instance) {
    if (TheSimpletonMod.isPlayingAsSimpleton()) {
      if (TheSimpletonMod.seasonScreen.isOpen()) {
        TheSimpletonMod.seasonScreen.update();
      }
    }
  }
}