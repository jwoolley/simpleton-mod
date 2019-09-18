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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@SpirePatch(
    clz = CustomPlayer.class,
    method = "getCardPool"
)

public class GetCardPoolPatchBefore {
  @SpirePrefixPatch
  public static SpireReturn<ArrayList<AbstractCard>> Prefix(CustomPlayer __instance, ArrayList<AbstractCard> tmpPool) {
    if (!TheSimpletonMod.isPlayingAsSimpleton()) {
      return SpireReturn.Continue();
    }

      final Logger logger = TheSimpletonMod.logger;
    List<AbstractCard> saveCardPool = TheSimpletonMod.getSaveCardPool();
    if (!saveCardPool.isEmpty()) {
      logger.info("GetCardPoolPatchBefore :: Card pool for " + AbstractDungeon.player.chosenClass + " is about to be replaced.");
      logger.info("GetCardPoolPatchBefore :: Original tmpPool: " + tmpPool.toString());
      logger.info("GetCardPoolPatchBefore :: Cards to replace with: " + saveCardPool.toString());
      TheSimpletonMod.logger.debug("GetCardPoolPatchBefore::getSaveCardPool using save data");

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
        logger.info("GetCardPoolPatchBefore :: Didn't add any seasonal crop cards to pool; season not initialized. Calling static method TheSimpletonMod.setSeasonalCropCards");
        TheSimpletonMod.setSeasonalCropCards(tmpPool
            .stream().filter(c -> c instanceof AbstractCropPowerCard).map(c -> (AbstractCropPowerCard)c).collect(Collectors.toList()));
      }
      tmpPool.addAll(TheSimpletonMod.getSeasonalCropCards());
      tmpPool.addAll(TheSimpletonMod.getSeasonalCurseCards());

      logger.info("GetCardPoolPatchBefore :: Saved card pool contains: " + tmpPool.size() + " cards.");
      logger.info("GetCardPoolPatchBefore :: cards: " + tmpPool.toString());

      // TODO: this does not belong here
      logger.debug("GetCardPoolPatchBefore :: disabling season screen");
      TheSimpletonMod.seasonScreen.dismiss();

      return SpireReturn.Return(tmpPool);
    } else {
      logger.info("GetCardPoolPatchBefore :: saveCardPool is empty");
    }

    return SpireReturn.Continue();
  }
}