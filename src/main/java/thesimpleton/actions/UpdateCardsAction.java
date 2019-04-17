package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.utilities.CropUtil;

public class UpdateCardsAction extends AbstractGameAction {
  private static AbstractGameAction.ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static float ACTION_DURATION = Settings.ACTION_DUR_FASTER;

  private AbstractPlayer p;

  public UpdateCardsAction() {
    this.p = AbstractDungeon.player;
    setValues(this.p, this.p);
    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
  }

  @Override
  public void update() {
    if (this.duration != ACTION_DURATION) {
      CropUtil.triggerCardUpdates();
      this.tickDuration();
      this.isDone = true;
      return;
    }
    this.tickDuration();
  }
}
