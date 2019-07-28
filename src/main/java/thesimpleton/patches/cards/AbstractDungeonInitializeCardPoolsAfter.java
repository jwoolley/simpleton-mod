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
        TheSimpletonMod.logger.info("AbstractDungeonInitializeCardPoolsAfter called");
        if (TheSimpletonMod.isSeasonInitialized()) {
          TheSimpletonMod.logger.info("AbstractDungeonInitializeCardPoolsAfter removing cards from pool");
          TheSimpletonMod.removeUnseasonalCardsFromPool();
        }
      }
    }
}