package thesimpleton.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import thesimpleton.TheSimpletonMod;

  @SpirePatch(
      clz = DungeonMapScreen.class,
      method = "update"
  )
  public class DungeonMapScreenRenderBefore {
    public static void Prefix (DungeonMapScreen __instance) {
    }
  }