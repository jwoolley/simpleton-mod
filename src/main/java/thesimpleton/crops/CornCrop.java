package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Corn;
import thesimpleton.orbs.CornCropOrb;

public class CornCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.CORN;

  public CornCrop() {
    super(CROP_ENUM);
    logger.debug("MAKIN' CORN (instantiating Corn).");
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, harvestAmount));
    }
    return harvestAmount;
  }
}