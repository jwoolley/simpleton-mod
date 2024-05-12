package thesimpleton.patches.cards;

import basemod.abstracts.CustomPlayer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.devtools.debugging.DebugLoggers;
import thesimpleton.utilities.ModLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@SpirePatch(
    clz = CustomPlayer.class,
    method = "getCardPool"
)

public class GetCardPoolPatchBefore {
  private static ModLogger logger = TheSimpletonMod.traceLogger;
  @SpirePrefixPatch
  public static SpireReturn<ArrayList<AbstractCard>> Prefix(CustomPlayer __instance, ArrayList<AbstractCard> tmpPool) {
    DebugLoggers.LEAKY_CURSES_LOGGER.log(GetCardPoolPatchBefore.class, "SpireReturn() called");
    if (!TheSimpletonMod.isPlayingAsSimpleton()) {
      DebugLoggers.LEAKY_CURSES_LOGGER.log(GetCardPoolPatchBefore.class, "Not playing as Simpleton, returning");
      return SpireReturn.Continue();
    }

    List<AbstractCard> saveCardPool = TheSimpletonMod.getSaveCardPool();
    if (!saveCardPool.isEmpty()) {
      logger.trace("GetCardPoolPatchBefore :: Card pool for " + AbstractDungeon.player.chosenClass + " is about to be replaced.");
      logger.trace("GetCardPoolPatchBefore :: Original tmpPool: " + tmpPool.toString());
      logger.trace("GetCardPoolPatchBefore :: Cards to replace with: " + saveCardPool.toString());
      logger.trace("GetCardPoolPatchBefore::getSaveCardPool using save data");

      DebugLoggers.LEAKY_CURSES_LOGGER.log(GetCardPoolPatchBefore.class, "replacing card pool");

      tmpPool.clear();
      tmpPool.addAll(saveCardPool);

      // TODO: reinitialize SeasonInfo
      // TODO: move this logic to TSM class or elsewhere
//      TheSimpletonMod.setSeasonalCropCards();

      if (TheSimpletonMod.isSeasonInitialized()) {
        // NEW GAME CASE
      } else {
        // LOAD-FROM-SAVE CASE
        // SeasonInfo isn't instantiated yet, but it's needed by getSeasonalCurseCards
        // We should have the save data we need by now for SeasonInfo though, so create it just in time
        // (getSeasonalCropCards doesn't need it because it's using data that's set by the custom save onLoad call.)
        // (this inconsistent weirdness is from the fact that we can't guarantee the order of custom save loads)

        TheSimpletonMod.initializeSeasonInfoIfNeeded();
        logger.trace("GetCardPoolPatchBefore :: Didn't add any seasonal crop cards to pool; season not initialized. Calling static method TheSimpletonMod.setSeasonalCropCards");
        DebugLoggers.LEAKY_CURSES_LOGGER.log(GetCardPoolPatchBefore.class, "GetCardPoolPatchBefore :: Didn't add any seasonal crop cards to pool; season not initialized. Calling static method TheSimpletonMod.setSeasonalCropCards()");

        TheSimpletonMod.setSeasonalCropCards(tmpPool
            .stream().filter(c -> c instanceof AbstractCropPowerCard).map(c -> (AbstractCropPowerCard)c).collect(Collectors.toList()));
      }

      DebugLoggers.LEAKY_CURSES_LOGGER.log(GetCardPoolPatchBefore.class, "Adding seasonal crop and curse card");

      tmpPool.addAll(TheSimpletonMod.getSeasonalCropCards());
      tmpPool.addAll(TheSimpletonMod.getSeasonalCurseCards());

      logger.trace("GetCardPoolPatchBefore :: Saved card pool contains: " + tmpPool.size() + " cards.");
      logger.trace("GetCardPoolPatchBefore :: cards: " + tmpPool.toString());

      // TODO: this does not belong here
      logger.trace("GetCardPoolPatchBefore :: disabling season screen");
      TheSimpletonMod.seasonScreen.dismiss();

      return SpireReturn.Return(tmpPool);
    } else {
      logger.trace("GetCardPoolPatchBefore :: saveCardPool is empty");
    }

    return SpireReturn.Continue();
  }
}