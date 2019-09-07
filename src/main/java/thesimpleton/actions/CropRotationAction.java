package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CropRotationAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_LONG;

  private AbstractPlayer p;
  private final int numCropsToHarvest;
  private final int numCardsToGain;
  private final boolean reduceCost;

  private boolean secondTick;

  public CropRotationAction(AbstractPlayer player, int numCropsToHarvest, int numCardsToGain, boolean reduceCost) {
    this.p = player;
    setValues(this.p,  this.p, numCropsToHarvest);
    this.numCropsToHarvest = numCropsToHarvest;
    this.numCardsToGain = numCardsToGain;
    this.amount = numCropsToHarvest;
    this.reduceCost = reduceCost;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
    this.secondTick = false;
  }

  public void update() {
    if (this.duration != ACTION_DURATION) {
      if (this.secondTick) {
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));

        final AbstractCropPowerCard randomCropPowerCard = TheSimpletonCharacter.getCropUtil().getRandomCropCardInSeason();
        if (this.reduceCost) {
          randomCropPowerCard.modifyCostForTurn(-1);
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(randomCropPowerCard, numCardsToGain));
      }
      this.tickDuration();
      this.isDone = true;
    }


    //TODO: make "getActiveCropPowers" a helper method on e.g. Util class
    // the logic exists - see Aerate / playerHasAnyActiveCropPowers / getOldestCropPower
    // harvest existing stacks
    final ArrayList<AbstractPower> activePowers =  new ArrayList<>(p.powers);
    Collections.shuffle(activePowers);

    List<AbstractCropOrb> activeOrbs = AbstractCropOrb.getActiveCropOrbs();
    AbstractCropOrb randomOrb = null;

    if (!activeOrbs.isEmpty()) {
      Collections.shuffle(activeOrbs);
      randomOrb = activeOrbs.get(0);
    }

    if (!this.secondTick) {
      if (randomOrb != null) {
        AbstractDungeon.actionManager.addToTop(new HarvestCropAction(randomOrb, numCropsToHarvest, true, true));
      } else {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("CARD_SELECT"));
      }
    }
    this.secondTick = true;


    this.tickDuration();
  }
}