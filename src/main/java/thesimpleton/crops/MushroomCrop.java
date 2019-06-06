package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Mushrooms;
import thesimpleton.orbs.MushroomCropOrb;

public class MushroomCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.MUSHROOMS;

  public MushroomCrop() {
    super(CROP_ENUM);
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