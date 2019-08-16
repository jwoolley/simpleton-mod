package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.TurnipCropOrb;
import thesimpleton.crops.Crop;

public class RootDownAction extends AbstractGameAction {
  public RootDownAction(AbstractCreature target, int numStacks) {
    this.actionType = ActionType.SPECIAL;
    this.target = target;
    this.amount = numStacks;
  }

  @Override
  public void update() {
    if (AbstractCropOrb.isMature(Crop.TURNIPS)) {
     AbstractCropOrb.getCropOrb(Crop.TURNIPS).getCrop().harvest(true, 1);
    } else {
      AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new TurnipCropOrb(), this.amount, true));
    }

    this.isDone = true;
  }
}