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
    final Logger logger = TheSimpletonMod.logger;
    List<AbstractCard> saveCardPool = TheSimpletonMod.getSaveCardPool();
    if (!saveCardPool.isEmpty()) {

      logger.debug("GetCardPoolPatchBefore :: Card pool for " + AbstractDungeon.player.chosenClass + " is about to be replaced.");
      logger.debug("GetCardPoolPatchBefore :: Original tmpPool: " + tmpPool.toString());
      logger.debug("GetCardPoolPatchBefore :: Cards to replace with: " + saveCardPool.toString());
      TheSimpletonMod.logger.debug("GetCardPoolPatchBefore::getSaveCardPool using save data");

      tmpPool.clear();
      tmpPool.addAll(saveCardPool);

      // TODO: reinitialize SeasonInfo
      // TODO: move this logic to TSM class or elsewhere
//      TheSimpletonMod.setSeasonalCropCards();

      if (TheSimpletonMod.isSeasonInitialized()) {
        tmpPool.addAll(TheSimpletonMod.getSeasonalCropCards());
        tmpPool.addAll(TheSimpletonMod.getSeasonalCurseCards());
      } else {
        logger.debug("GetCardPoolPatchBefore :: Didn't add any seasonal crop cards to pool; season not initialized. Calling static method TheSimpletonMod.setSeasonalCropCards");
        TheSimpletonMod.setSeasonalCropCards();
      }

      logger.debug("GetCardPoolPatchBefore :: Saved card pool contains: " + tmpPool.size() + " cards.");
      logger.debug("GetCardPoolPatchBefore :: cards: " + tmpPool.toString());

      // TODO: this does not belong here
      logger.debug("GetCardPoolPatchBefore :: disabling season screen");
      TheSimpletonMod.seasonScreen.dismiss();

      return SpireReturn.Return(tmpPool);
    } else {
      logger.debug("GetCardPoolPatchBefore :: saveCardPool is empty");
    }

    return SpireReturn.Continue();
  }
}
