package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.attack.BabyTurnip;
import thesimpleton.cards.attack.GiantTurnip;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Turnips;
import thesimpleton.orbs.TurnipCropOrb;

public class TurnipCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.TURNIPS;

  public TurnipCrop() {
    super(CROP_ENUM);
  }

  protected int calculateHarvestAmount(int amount, int maxAmount, boolean harvestAll) { return this.getAmount();  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      if (harvestAmount == 1) {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new BabyTurnip(), 1));
      } else {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new GiantTurnip(harvestAmount), 1));
      }
    }
    return harvestAmount;
  }
}