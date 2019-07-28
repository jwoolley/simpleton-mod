package thesimpleton.patches.ui;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
    clz = DungeonTransitionScreen.class,
    method = "update"
)

public class DungeonTransitionScreenUpdateAfter {
  public static void Postfix (DungeonTransitionScreen __instance) {
    if (TheSimpletonMod.isPlayingAsSimpleton()) { }

  }
}