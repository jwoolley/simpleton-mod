package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErosionControlAction extends AbstractGameAction {
  //TODO create "HARVEST/CROP" action type?
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_LONG;

  private AbstractPlayer p;
  private final boolean isFromCard;

  private boolean firstTickFinished;

  public ErosionControlAction(AbstractPlayer player, int numIntangibleStacks, boolean isFromCard) {
    this.p = player;
    setValues(this.p,  this.p, numIntangibleStacks);
    this.amount = numIntangibleStacks;
    this.isFromCard = isFromCard;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.firstTickFinished = false;
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;
    if (this.duration != ACTION_DURATION) {
      AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
      logger.debug("ErosionControlAction.update :: second tick. Applying " + this.amount + " Intangible");
          AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, this.amount), this.amount));
      this.tickDuration();
      this.isDone = true;
    }

    if (!this.firstTickFinished) {
      logger.debug("ErosionControlAction.update :: first tick");

      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        List<AbstractCropOrb> cropOrbs = AbstractCropOrb.getActiveCropOrbs();

        for(AbstractCropOrb cropOrb : cropOrbs) {
          logger.debug("ErosionControlAction.update :: harvesting all of " + cropOrb.name);

          AbstractDungeon.actionManager.addToTop(new HarvestCropAction(cropOrb, 1,isFromCard, true));
        }
      } else {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("CARD_SELECT"));
      }
      this.firstTickFinished = true;
    }

    this.tickDuration();
  }
}