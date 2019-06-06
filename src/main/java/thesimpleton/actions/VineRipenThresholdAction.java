package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BlurPower;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.crops.Crop;

public class VineRipenThresholdAction  extends AbstractGameAction {

  public VineRipenThresholdAction() {
    this.actionType = AbstractGameAction.ActionType.SPECIAL;
  }

  @Override
  public void update() {
    AbstractPlayer player = SimpletonUtil.getPlayer();

    if (AbstractCropOrb.isMature(Crop.SQUASH)) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new BlurPower(player, 1), 1));
    }

    this.isDone = true;
  }
}