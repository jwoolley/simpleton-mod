package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.Optional;
import java.util.stream.Collectors;


public class CropSpawnAction extends AbstractGameAction {
    private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
    private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;
    private final int amount;
    private AbstractCropOrb cropOrb;

    private boolean secondTick = false;

    // TODO: increment existing cropOrb instead if it
    public CropSpawnAction(AbstractCropOrb cropOrb) {
        this(cropOrb, -1);
    }

    public CropSpawnAction(AbstractCropOrb cropOrb, int stacks) {
        Logger logger = TheSimpletonMod.logger;

        logger.debug("CropSpawnAction() constructor: " + cropOrb.getClass().getSimpleName() + "; amount: " + stacks + "; cropOrb.getAmount: "  + cropOrb.getAmount());

        this.duration = ACTION_DURATION;
        this.actionType = ACTION_TYPE;
        this.amount = stacks >= 0 ? stacks : cropOrb.passiveAmount;
        this.cropOrb = cropOrb;
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
                    cropOrb.update();
                    // (trigger cropOrb crap}
                } else {
                    logger.debug("CropSpawnAction::update : Player doesn't have cropOrb "
                        + this.cropOrb.getClass().getSimpleName() + ". Adding " + this.amount);

                    this.cropOrb.gainCropEffectBefore();
                    this.cropOrb.passiveAmount = this.amount;
                    AbstractDungeon.player.channelOrb(this.cropOrb);
                    AbstractCropOrb.getCropOrb(this.cropOrb).gainCropEffectAfter();
                }
                this.secondTick = true;
            }
            tickDuration();
        }
    }

}



