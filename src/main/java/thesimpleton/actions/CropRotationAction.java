package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.powers.AbstractCropPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class CropRotationAction extends AbstractGameAction {
  private static final ActionType ACTION_TYPE = ActionType.ENERGY;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_LONG;

  private AbstractPlayer p;
  private final int numCropsToHarvest;
  private final int numCardsToGain;
  private final boolean reduceCost;

  public CropRotationAction(AbstractPlayer player, int numCropsToHarvest, int numCardsToGain, boolean reduceCost) {
    this.p = player;
    setValues(this.p,  this.p, numCropsToHarvest);
    this.numCropsToHarvest = numCropsToHarvest;
    this.numCardsToGain = numCardsToGain;
    this.amount = numCropsToHarvest;
    this.reduceCost = reduceCost;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
  }

  public void update() {
    if (this.duration != ACTION_DURATION) {
      final AbstractCropPowerCard randomCropPowerCard = AbstractCropPowerCard.getRandomCropPowerCard(true);
      if (this.reduceCost) {
        randomCropPowerCard.modifyCostForTurn(-1);
      }
      AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(randomCropPowerCard, numCardsToGain));

      this.tickDuration();
      this.isDone = true;
    }

    //TODO: make "getActiveCropPowers" a helper method on e.g. Util class
    // harvest existing stacks
    final ArrayList<AbstractPower> activePowers =  new ArrayList<>(p.powers);
    Collections.shuffle(activePowers);
    Optional<AbstractPower> oldPower = activePowers.stream()
        .filter(pow -> pow instanceof AbstractCropPower && !((AbstractCropPower) pow).finished)
        .findFirst();

    if (oldPower.isPresent()) {
      ((AbstractCropPower) oldPower.get()).harvest(false, this.numCropsToHarvest );
    } else {
      //TODO: add "fizzle" effect if there are no stacks to harvest
    }

    this.tickDuration();
  }
}