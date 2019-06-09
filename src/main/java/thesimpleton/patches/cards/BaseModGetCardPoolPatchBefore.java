package thesimpleton.patches.cards;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.List;


@SpirePatch(
    clz = CustomPlayer.class,
    method = "getCardPool"
)

public class BaseModGetCardPoolPatchBefore {
  @SpirePrefixPatch
  public static SpireReturn<ArrayList<AbstractCard>> Prefix(CustomPlayer __instance, ArrayList<AbstractCard> tmpPool) {
    final Logger logger = TheSimpletonMod.logger;
    List<AbstractCard> saveCardPool = TheSimpletonMod.getSaveCardPool();
    if (!saveCardPool.isEmpty()) {
      logger.debug("BasemodCardGetCardPoolPatchBefore :: Card pool for " + AbstractDungeon.player.chosenClass + " is about to be replaced.");
      logger.debug("BasemodCardGetCardPoolPatchBefore :: Original tmpPool: " + tmpPool.toString());
      logger.debug("BasemodCardGetCardPoolPatchBefore :: Cards to replace with: " + saveCardPool.toString());
      TheSimpletonMod.logger.debug("BasemodCardGetCardPoolPatchBefore::getSaveCardPool using save data");

//      ArrayList<AbstractCard> finalPool = new ArrayList<>();
//      finalPool.addAll(saveCardPool.stream().distinct().filter(tmpPool::contains).collect(Collectors.toSet()));
//      tmpPool.clear();
//      tmpPool.addAll(finalPool);

      tmpPool.clear();
      tmpPool.addAll(saveCardPool);

      // Save the card pools
      CustomSavable<List<String>> cardPoolSavable = new CardPoolSavable(saveCardPool);

      if (BaseMod.getSaveFields().get(AbstractDungeon.player.chosenClass + "CardpoolSave") == null) {
        BaseMod.addSaveField(AbstractDungeon.player.chosenClass + "CardpoolSave", cardPoolSavable);
      }
      cardPoolSavable.onSave();

      logger.debug("BasemodCardGetCardPoolPatchBefore ::  You are playing with: " + tmpPool.size() + " cards.");
      logger.debug("BasemodCardGetCardPoolPatchBefore :: cards: " + tmpPool.toString());

      return SpireReturn.Return(tmpPool);
    }
    return SpireReturn.Continue();
  }
}
