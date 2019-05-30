package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.orbs.AbstractCropOrb;


public class CropRemoveAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_MED;

  private AbstractCropOrb cropOrb;
  private final boolean secondApplication = false;

  public CropRemoveAction(AbstractCropOrb cropOrb) {
    this.duration = ACTION_DURATION;
    this.actionType = ACTION_TYPE;
    this.cropOrb = cropOrb;
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;

    if (AbstractCropOrb.hasCropOrb(this.cropOrb)) {
        SimpletonUtil.getPlayer().removeOrb(this.cropOrb);
        cropOrb.update();
    } else {
      logger.debug("CropRemoveAction::update : Player doesn't have " + cropOrb.name
          + " <======================================================");
    }
    this.isDone = true;
    tickDuration();
  }
}



