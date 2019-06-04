package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.orbs.AbstractCropOrb;

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

      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        final AbstractCropOrb newestCropOrb = AbstractCropOrb.getNewestCropOrb();
        if (newestCropOrb.isMature(true)) {
          AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, this.blockAmount));
        } else {
          AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(newestCropOrb, this.amount, true));
        }
        this.isDone = true;
      }
    }
    this.tickDuration();
  }
}