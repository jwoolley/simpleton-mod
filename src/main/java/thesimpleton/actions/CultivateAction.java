package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.ChiliCropOrb;
import thesimpleton.orbs.TurnipCropOrb;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.utils.Crop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CultivateAction extends AbstractGameAction {
  //TODO create "HARVEST/CROP" action type?
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_LONG;

  private AbstractPlayer p;
  private final int numStacksToHarvest;
  private final boolean isFromCard;

  private boolean hasHarvested;
  private boolean firstTickFinished;

  private Map<AbstractCropOrb,Integer> cropCounts;

  public CultivateAction(AbstractPlayer player, int numStacksToHarvest, int numStacksToGain, boolean isFromCard) {
    this.p = player;
    setValues(this.p,  this.p, numStacksToGain);
    this.numStacksToHarvest = numStacksToHarvest;
    this.amount = numStacksToGain;
    this.isFromCard = isFromCard;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.firstTickFinished = false;
    this.hasHarvested = false;

    cropCounts = new HashMap();
  }

  public void update() {
    Logger logger = TheSimpletonMod.logger;
    if (this.duration != ACTION_DURATION) {
      AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
      logger.debug("CultivateAction.update :: second tick");
      if (this.hasHarvested) {
        for(AbstractCropOrb cropOrb : cropCounts.keySet()) {
          logger.debug("CultivateAction.update :: stacking " + cropOrb.name + " for 1");
          AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(cropOrb, 1, true));
        }
      }

      this.tickDuration();
      this.isDone = true;
    }

    if (!this.firstTickFinished) {
      logger.debug("PruningAction.update :: first tick");

      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        List<AbstractCropOrb> cropOrbs = AbstractCropOrb.getActiveCropOrbs();

        for(AbstractCropOrb cropOrb : cropOrbs) {
          final int stacks = cropOrb.getAmount() < numStacksToHarvest ? cropOrb.getAmount() : numStacksToHarvest;
          logger.debug("CultivateAction.update :: player has " + cropOrb.getAmount() + " stacks of " + cropOrb.name);
          logger.debug("CultivateAction.update :: numStacksToHarvest: " + numStacksToHarvest);
          logger.debug("CultivateAction.update :: harvesting " + stacks + " stacks of " + cropOrb.name);
          cropCounts.put(cropOrb.getCrop().getCropOrb(), stacks);

          AbstractDungeon.actionManager.addToTop(new HarvestCropAction(cropOrb, stacks,true));

//          cropOrb.getCrop().harvest(false, stacks);
        }

        this.hasHarvested = true;
      } else {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("CARD_SELECT"));
      }
      this.firstTickFinished = true;
    }

    this.tickDuration();
  }
}