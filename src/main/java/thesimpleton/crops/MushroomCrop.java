package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Mushrooms;
import thesimpleton.orbs.MushroomCropOrb;
import thesimpleton.powers.utils.Crop;

public class MushroomCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.MUSHROOMS;
  private static final String ORB_ID = MushroomCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Mushrooms();

  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;

  public static final int MATURITY_THRESHOLD = 2;

  public MushroomCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      for (int i = 0; i < harvestAmount; i++) {
        AbstractCard card = AbstractDungeon.returnTrulyRandomColorlessCardInCombat().makeCopy();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, 1));
      }
    }
    return harvestAmount;
  }
}