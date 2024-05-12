package thesimpleton.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;

@SpirePatch(
      clz = AbstractDungeon.class,
      method = "initializeCardPools"
  )
  public class AbstractDungeonInitializeCardPoolsAfter {
    public static void Postfix (AbstractDungeon __instance) {
      if (TheSimpletonMod.isPlayingAsSimpleton()) {
        TheSimpletonMod.traceLogger.debug("AbstractDungeonInitializeCardPoolsAfter called");
        if (TheSimpletonMod.isSeasonInitialized()) {
          TheSimpletonMod.traceLogger.debug("AbstractDungeonInitializeCardPoolsAfter removing cards from pool");
          TheSimpletonMod.removeUnseasonalCardsFromPool();
        }
      } else if (!TheSimpletonMod.ConfigData.enableCursesForAllCharacters) {
        // remove custom curses if config is disabled
        TheSimpletonMod.removeCustomCurseCardsFromPool();
      }
    }
}