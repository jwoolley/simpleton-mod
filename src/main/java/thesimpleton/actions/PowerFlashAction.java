package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerFlashAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private final AbstractPower power;

  public PowerFlashAction(AbstractPower power) {
    this.power = power;
  }

  @Override
  public void update() {
    if (this.duration != ACTION_DURATION) {
      power.flash();
      tickDuration();
      this.isDone = true;
    }
    tickDuration();
  }
}