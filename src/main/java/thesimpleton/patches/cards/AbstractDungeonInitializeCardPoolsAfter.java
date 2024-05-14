package thesimpleton.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.devtools.debugging.DebugLoggers;
import thesimpleton.enums.TheSimpletonCharEnum;

@SpirePatch(
      clz = AbstractDungeon.class,
      method = "initializeCardPools"
  )
  public class AbstractDungeonInitializeCardPoolsAfter {
    public static void Postfix (AbstractDungeon __instance) {
      DebugLoggers.LEAKY_CURSES_LOGGER.log(AbstractDungeonInitializeCardPoolsAfter.class, "Postfix() called");
      if (TheSimpletonMod.isPlayingAsSimpleton()) {
        TheSimpletonMod.traceLogger.debug("AbstractDungeonInitializeCardPoolsAfter called");
        if (TheSimpletonMod.isSeasonInitialized()) {
          TheSimpletonMod.traceLogger.debug("AbstractDungeonInitializeCardPoolsAfter removing cards from pool");
          TheSimpletonMod.removeUnseasonalCardsFromPool();
        }
      } else if (!TheSimpletonMod.ConfigData.enableCursesForAllCharacters) {
        // TODO: we really want to know that we're playing as simpleton here, but it seems like player isn't initialized yet
        DebugLoggers.LEAKY_CURSES_LOGGER.log("isPlayingAsSimpleton(): " + TheSimpletonMod.isPlayingAsSimpleton()
            + " [player.chosenClass: " + (AbstractDungeon.player != null ? AbstractDungeon.player.chosenClass : AbstractDungeon.player) + " vs. " + TheSimpletonCharEnum.THE_SIMPLETON + "]");

        // remove custom curses if config is disabled
        DebugLoggers.LEAKY_CURSES_LOGGER.log(AbstractDungeonInitializeCardPoolsAfter.class, "removing custom curses from the card pool");
        TheSimpletonMod.removeCustomCurseCardsFromCardPoolAndCardLibrary();
      }
    }
}