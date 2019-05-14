package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.utils.Crop;

public class TillTheFieldAction extends AbstractGameAction {
  private static final float ACTION_DURATION = Settings.ACTION_DUR_MED;
  private final int blockAmount;
  private final boolean isFromCard;

  public TillTheFieldAction(int cropAmount, int blockAmount) {
    this(cropAmount, blockAmount, true);
  }


  public TillTheFieldAction(int cropAmount, int blockAmount, boolean isFromCard) {
    this.actionType = ActionType.BLOCK;
    this.amount = cropAmount;
    this.blockAmount = blockAmount;
    this.isFromCard = isFromCard;
    this.duration = ACTION_DURATION;
  }

  @Override
  public void update() {
    Logger logger = TheSimpletonMod.logger;

    logger.debug("TillTheFieldAction.update called");
    if (this.duration != ACTION_DURATION) {
      AbstractPlayer player = SimpletonUtil.getPlayer();

      if (AbstractCropPower.playerHasAnyActiveCropPowers()) {
        final AbstractCropPower newestCrop = AbstractCropPower.getNewestCropPower();
        if (newestCrop.isMature()) {
          AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, this.blockAmount));
        } else {
          AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(player, player, Crop.getCrop(newestCrop.enumValue, player, this.amount, this.isFromCard), this.amount));
        }
        this.isDone = true;
      }
    }
    this.tickDuration();
  }
}