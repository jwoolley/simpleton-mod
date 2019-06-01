package thesimpleton.cards.power.crop;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCropPowerCard extends CustomCard {

  public AbstractCropPowerCard(java.lang.String id, java.lang.String name, java.lang.String img, int cost,
                               java.lang.String rawDescription, com.megacrit.cardcrawl.cards.AbstractCard.CardType type,
                               com.megacrit.cardcrawl.cards.AbstractCard.CardColor color,
                               com.megacrit.cardcrawl.cards.AbstractCard.CardRarity rarity,
                               com.megacrit.cardcrawl.cards.AbstractCard.CardTarget target) {
    super(id, name, img, cost, rawDescription, type, color, rarity, target);
  }

  public static List<AbstractCropPowerCard> getRandomCropPowerCards(int number, boolean withRarityDistribution) {
    try {
      return AbstractCrop.getRandomCrops(AbstractDungeon.player, number, 0, withRarityDistribution)
          .stream()
          .map(crop -> crop.getPowerCard())
          .collect(Collectors.toList());
    } catch (UnsupportedOperationException e){
      TheSimpletonMod.logger.warn("Unexpectedly received crop power with no powerCard");
      return null;
    }
  }


//  public static List<AbstractCropPowerCard> getRandomCropPowerCards(int number, boolean withRarityDistribution) {
//    try {
//      return AbstractCropPower
//          .getRandomCropPowers(AbstractDungeon.player, number, 0, withRarityDistribution, p -> p.hasPowerCard())
//          .stream()
//          .map(power -> power.getPowerCard())
//          .collect(Collectors.toList());
//    } catch (UnsupportedOperationException e){
//      TheSimpletonMod.logger.warn("Unexpectedly received crop power with no powerCard");
//      return null;
//    }
//  }

  public static AbstractCropPowerCard getRandomCropPowerCard(boolean withRarityDistribution) {
    return getRandomCropPowerCards(1, withRarityDistribution).get(0);
  }
}
