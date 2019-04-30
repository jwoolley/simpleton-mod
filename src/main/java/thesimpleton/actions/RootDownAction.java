package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.PlantTurnipPower;

import java.util.Optional;

public class RootDownAction extends AbstractGameAction {
  public RootDownAction(AbstractCreature target, int numStacks) {
    this.actionType = ActionType.SPECIAL;
    this.target = target;
    this.amount = numStacks;
  }

  @Override
  public void update() {
    Optional<AbstractCropPower> matureTurnipPower = AbstractCropPower.getActiveCropPowers().stream()
        .filter(p -> p.ID == PlantTurnipPower.POWER_ID && p.isMature())
        .findFirst();

    if (matureTurnipPower.isPresent()) {
      (matureTurnipPower.get()).harvest(true, 1);
    } else {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(this.target, this.target, new PlantTurnipPower(this.target, this.amount), this.amount)
      );
    }

    this.isDone = true;
  }
}