package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Corn;
import thesimpleton.orbs.CornCropOrb;

public class CoffeeCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.COFFEE;

  public CoffeeCrop() {
    super(CROP_ENUM);
    logger.info("MAKIN' COFFEE (instantiating Coffee).");
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(harvestAmount));
    }
    return harvestAmount;
  }
}