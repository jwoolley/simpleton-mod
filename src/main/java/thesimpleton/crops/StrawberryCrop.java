package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.attack.GiantTurnip;

public class StrawberryCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.STRAWBERRIES;

  public StrawberryCrop() {
    super(CROP_ENUM);
  }

  protected int calculateHarvestAmount(int amount, int maxAmount, boolean harvestAll) { return this.getAmount();  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.GNAWBERRY, harvestAmount));
    }
    return harvestAmount;
  }
}