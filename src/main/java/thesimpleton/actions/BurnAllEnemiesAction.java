package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BurnAllEnemiesAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  public BurnAllEnemiesAction(int burningAmount) {
    this.amount = burningAmount;
  }

  @Override
  public void update() {
    if (this.duration != ACTION_DURATION) {
      AbstractDungeon.getMonsters().monsters.stream()
          .filter(mo -> !mo.isDeadOrEscaped())
          .forEach(mo -> AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(mo, AbstractDungeon.player,
              this.amount)));
      this.isDone = true;
    }
    this.tickDuration();
  }
}
