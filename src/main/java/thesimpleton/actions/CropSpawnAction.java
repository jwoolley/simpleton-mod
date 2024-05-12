package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.powers.AbundancePower;
import thesimpleton.relics.Honeycomb;
import thesimpleton.utilities.ModLogger;


public class CropSpawnAction extends AbstractGameAction {
    private static ModLogger logger = TheSimpletonMod.traceLogger;

    private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
    private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
    private final boolean isFromCard;
    private final int amount;
    private final int rawAmount;
    private AbstractCropOrb cropOrb;

    private boolean secondTick = false;

    public CropSpawnAction(AbstractCropOrb cropOrb, int stacks, boolean isFromCard) {
//        logger.trace("============> CropSpawnAction::constructor =====");

        final int rawAmount = stacks >= 0 ? stacks : cropOrb.passiveAmount;

        this.duration = ACTION_DURATION;
        this.actionType = ACTION_TYPE;
        this.isFromCard = isFromCard;
        this.amount = calculateCropStacks(rawAmount, this.isFromCard);
        this.rawAmount = rawAmount;
        this.cropOrb = cropOrb;

        logger.trace("CropSpawnAction::constructor: " + cropOrb.getClass().getSimpleName() + "; rawAmount: " + rawAmount + " calculated amount: " + this.amount + " cropOrb.amount " + cropOrb.getAmount() + " cropOrb.passiveAmount " + cropOrb.passiveAmount);
    }

    public void update() {
//        logger.trace("CropSpawnAction::update duration: " + this.duration);

        if (!secondTick && AbstractDungeon.player.maxOrbs <= 0 && !SimpletonUtil.playerHasStartingOrbs()) {
            AbstractDungeon.player.increaseMaxOrbSlots(1, true);
        }

        if (secondTick) {
            if (this.duration != ACTION_DURATION) {

                if (AbstractDungeon.player.hasRelic(Honeycomb.ID)) {
                    final Honeycomb honeycomb = (Honeycomb) AbstractDungeon.player.getRelic(Honeycomb.ID);
                    honeycomb.onPlantCrop(this.cropOrb.getCrop().enumValue);
                }

                AbstractCropOrb cropOrb = AbstractCropOrb.getCropOrb(this.cropOrb);
                if (cropOrb != null) {
                    int newAmount = cropOrb.passiveAmount;

                    AbstractCrop crop = cropOrb.getCrop();
                    if (newAmount > crop.getMaturityThreshold()) {
                        crop.flash();
                        crop.harvest(false, newAmount - crop.getMaturityThreshold());
                    }
                }
                this.isDone = true;
                return;
            }
            tickDuration();
        } else {
//                logger.trace("CropSpawnAction::update : Player can have orbs: " + AbstractDungeon.player.maxOrbs);
            AbstractCropOrb orb = AbstractCropOrb.getCropOrb(this.cropOrb);
            if (orb != null) {
//                    logger.trace("CropSpawnAction::update : Player has cropOrb " + this.cropOrb.name);
                orb.passiveAmount += this.amount;
                orb.stackCropEffect();
                orb.update();
                // (trigger cropOrb crap}
            } else {
//                    logger.trace("CropSpawnAction::update : Player doesn't have cropOrb "
//                        + this.cropOrb.getClass().getSimpleName() + ". Adding " + this.amount);

                if (SimpletonUtil.getActiveOrbs().size() >= AbstractDungeon.player.maxOrbs) {
//                        logger.trace("CropSpawnAction::update player has no free orb slots. Queueing CropOrbCycleAction with " + this.cropOrb.name + " for " + this.cropOrb.passiveAmount + " stacks");
                    AbstractDungeon.actionManager.addToBottom(new CropOrbCycleAction(this.cropOrb, this.rawAmount, this.isFromCard));
                }  else {
//                        logger.trace("CropSpawnAction::update player has " +  (AbstractDungeon.player.maxOrbs - SimpletonUtil.getActiveOrbs().size()) + " free orb slots");
//                        logger.trace("CropSpawnAction::update # of " + this.cropOrb.name + " before: " + this.cropOrb.passiveAmount);

                    this.cropOrb.gainCropEffectBefore();
                    this.cropOrb.passiveAmount = this.amount;

                    logger.trace("CropSpawnAction::update # of " + this.cropOrb.name + " passiveAmount after (1): " + this.cropOrb.passiveAmount + "; this.amount: " + this.amount);

                    AbstractCropOrb newOrb = this.cropOrb.makeCopy(this.amount);
                    AbstractDungeon.player.channelOrb(newOrb);
                    newOrb.update();
//                        logger.trace("CropSpawnAction::update # of " + newOrb.name + " passiveAmount after (2): " + newOrb.passiveAmount + " getAmount amount after: " + AbstractCropOrb.getCropOrb(newOrb).getAmount());
                    AbstractCropOrb.getCropOrb(newOrb).gainCropEffectAfter();
                }
            }
            this.secondTick = true;
        }
        tickDuration();
    }

    public static int calculateCropStacks(int amount, boolean isFromCard) {
        return calculateCropStacks(amount, isFromCard, true);
    }

    public static int calculateCropStacks(int amount, boolean isFromCard, boolean isPlantingCrop) {
        AbstractPlayer player = AbstractDungeon.player;

        logger.trace("ApplyCropAction::calculateCropStacks | amount: " + amount + " isFromCard: " + isFromCard + " isPlantingCrop: " + isPlantingCrop);

        int adjustedAmount = amount;
        if (isPlantingCrop && player.hasPower(AbundancePower.POWER_ID) && isFromCard) {
            AbstractPower power = player.getPower(AbundancePower.POWER_ID);
            if (power.amount > 0) {
                power.flashWithoutSound();
                adjustedAmount += power.amount;
            }

            logger.trace("ApplyCropAction::calculateCropStacks | adjusting amount by abundance value: " + power.amount);

        }

        logger.trace("ApplyCropAction::calculateCropStacks | adjustedAmount: " + adjustedAmount);

        return adjustedAmount;
    }
}



