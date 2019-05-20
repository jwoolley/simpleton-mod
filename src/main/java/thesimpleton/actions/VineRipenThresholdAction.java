package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BlurPower;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.PlantSquashPower;

import java.util.Optional;

public class VineRipenThresholdAction  extends AbstractGameAction {

  public VineRipenThresholdAction() {
    this.actionType = AbstractGameAction.ActionType.SPECIAL;
  }

  @Override
  public void update() {
    AbstractPlayer player = SimpletonUtil.getPlayer();
    Optional<AbstractCropPower> matureSquashPower = AbstractCropPower.getActiveCropPowers().stream()
        .filter(p -> p.ID == PlantSquashPower.POWER_ID && p.isMature())
        .findFirst();

    if (matureSquashPower.isPresent()) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new BlurPower(player, 1), 1));
    }

    this.isDone = true;
  }
}