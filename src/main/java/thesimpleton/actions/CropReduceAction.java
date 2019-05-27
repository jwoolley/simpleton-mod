package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;


public class CropReduceAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;

  private final int amount;
  private AbstractCropOrb cropOrb;
  private final boolean secondApplication = false;

  public CropReduceAction(AbstractCropOrb cropOrb, int amount) {
    this.duration = ACTION_DURATION;
    this.actionType = ACTION_TYPE;
    this.cropOrb = cropOrb;
    this.amount = amount;
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;

    if (AbstractCropOrb.hasCropOrb(this.cropOrb)) {
      AbstractCropOrb orb = AbstractCropOrb.getCropOrb(this.cropOrb);
      if (orb.getAmount() > amount) {
        cropOrb.passiveAmount -= this.amount;
        cropOrb.update();
      } else if (orb.getAmount() == amount) {
        AbstractDungeon.actionManager.addToBottom(new CropRemoveAction(this.cropOrb));

      } else {
        logger.debug("CropReduceAction::update : Player has less " + cropOrb.name
            + " than requested reduce amount." + " Actual " + orb.getAmount() + " Requested: "  + amount
            + " <======================================================");
      }
    } else {
      logger.debug("CropReduceAction::update : Player doesn't have " + cropOrb.name
          + " <======================================================");
    }
    this.isDone = true;
    tickDuration();
  }

}



