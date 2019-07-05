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

    TheSimpletonMod.logger.info("============> CropReduceAction::constructor =====");
    this.duration = ACTION_DURATION;
    this.actionType = ACTION_TYPE;
    this.cropOrb = cropOrb;
    this.amount = amount;
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;
//    logger.info("============> CropReduceAction::update =====");
    AbstractCropOrb orb = AbstractCropOrb.getCropOrb(this.cropOrb);
    if (orb != null) {
      if (orb.passiveAmount > amount) {
//        logger.info("============> CropReduceAction::reducing " + this.cropOrb.name + " by " + this.amount + " from " + cropOrb.passiveAmount + " =====");
        cropOrb.passiveAmount -= this.amount;
        cropOrb.update();
      } else if (orb.passiveAmount == amount) {
//        TheSimpletonMod.logger.info("============> CropReduceAction::update queueing CropRemoveAction =====");

        AbstractDungeon.actionManager.addToTop(new CropRemoveAction(this.cropOrb));
      } else {
//        logger.info("CropReduceAction::update : Player has less " + cropOrb.name
//            + " than requested reduce amount." + " Actual " + orb.getAmount() + " Requested: "  + amount
//            + " <======================================================");
      }
    } else {
//      logger.info("CropReduceAction::update : Player doesn't have " + cropOrb.name
//          + " <======================================================");
    }
    this.isDone = true;
    tickDuration();
  }

}



