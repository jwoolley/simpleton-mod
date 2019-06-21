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
    if (TheSimpletonMod.seasonScreen.isOpen()) {
      TheSimpletonMod.seasonScreen.update();
    }
  }
}