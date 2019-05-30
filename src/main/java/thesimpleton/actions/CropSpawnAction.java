package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.powers.AbundancePower;


public class CropSpawnAction extends AbstractGameAction {
    private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
    private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;
    private final boolean isFromCard;
    private final int amount;
    private AbstractCropOrb cropOrb;

    private boolean secondTick = false;

    // TODO: increment existing cropOrb instead if it
    public CropSpawnAction(AbstractCropOrb cropOrb, boolean isFromCard) {
        this(cropOrb, -1, isFromCard);
    }

    public CropSpawnAction(AbstractCropOrb cropOrb, int stacks, boolean isFromCard) {
        final int rawAmount = stacks >= 0 ? stacks : cropOrb.passiveAmount;

        this.duration = ACTION_DURATION;
        this.actionType = ACTION_TYPE;
        this.isFromCard = isFromCard;
        this.amount = calculateCropStacks(rawAmount, this.isFromCard);
        this.cropOrb = cropOrb;

        Logger logger = TheSimpletonMod.logger;
        logger.debug("CropSpawnAction() constructor: " + cropOrb.getClass().getSimpleName() + "; rawAmount: " + rawAmount + " calculated amount: " + this.amount);
    }

    public void update() {
        Logger logger = TheSimpletonMod.logger;

        if (AbstractDungeon.player.maxOrbs <= 0) {
            this.isDone = true;
            return;
        } else {
            if (secondTick) {
                int newAmount =  AbstractCropOrb.getCropOrb(this.cropOrb).getAmount();
                logger.debug("CropSpawnAction::update secondTick. newAmount: " + newAmount);

                AbstractCrop crop = cropOrb.getCrop();
                if (newAmount > crop.getMaturityThreshold()) {
                    crop.flash();
                    crop.harvest(false, newAmount - crop.getMaturityThreshold());
                }
                this.isDone = true;
            } else {
                logger.debug("CropSpawnAction::update : Player can have orbs: " + AbstractDungeon.player.maxOrbs);
                if (AbstractCropOrb.hasCropOrb(this.cropOrb)) {
                    logger.debug("CropSpawnAction::update : Player has cropOrb " + this.cropOrb.name);

                    AbstractCropOrb cropOrb = AbstractCropOrb.getCropOrb(this.cropOrb);
                    cropOrb.passiveAmount += this.amount;
                    AbstractCropOrb.getCropOrb(this.cropOrb).stackCropEffect();
                    cropOrb.update();
                    // (trigger cropOrb crap}
                } else {
                    logger.debug("CropSpawnAction::update : Player doesn't have cropOrb "
                        + this.cropOrb.getClass().getSimpleName() + ". Adding " + this.amount);

                    if (AbstractCropOrb.getActiveCropOrbs().size() < AbstractDungeon.player.maxOrbs) {
                        this.cropOrb.gainCropEffectBefore();
                        this.cropOrb.passiveAmount = this.amount;
                        AbstractDungeon.player.channelOrb(this.cropOrb);
                        AbstractCropOrb.getCropOrb(this.cropOrb).gainCropEffectAfter();
                    } else {
                        // TODO: handle havest all of oldest orb / shift all remaining orbs by 1, then apply new orb
                        logger.debug("CropSpawnAction::update not adding new crop orb: player already has max orbs (" + AbstractDungeon.player.maxOrbs + ")");
                    }
                }
                this.secondTick = true;
            }
            tickDuration();
        }
    }

    public static int calculateCropStacks(int amount, boolean isFromCard) {
        AbstractPlayer player = SimpletonUtil.getPlayer();

        Logger logger = TheSimpletonMod.logger;

        logger.debug("ApplyCropAction:calculateCropStacks");

        int adjustedAmount = amount;
        if (player.hasPower(AbundancePower.POWER_ID) && isFromCard) {
            AbstractPower power = player.getPower(AbundancePower.POWER_ID);
            if (power.amount > 0) {
                power.flashWithoutSound();
                adjustedAmount += power.amount;
            }
        }
        return adjustedAmount;
    }
}



