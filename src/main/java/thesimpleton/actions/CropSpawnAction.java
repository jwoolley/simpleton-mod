package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;

import java.util.Optional;
import java.util.stream.Collectors;


public class CropSpawnAction extends AbstractGameAction {
    private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
    private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;
    private final int amount;
    private AbstractCropOrb cropOrb;
    private final boolean secondApplication = false;

    // TODO: increment existing cropOrb instead if it
    public CropSpawnAction(AbstractCropOrb cropOrb) {
        this(cropOrb, -1, false);
    }

    public CropSpawnAction(AbstractCropOrb cropOrb, int spawnAmount) {
        this(cropOrb, spawnAmount, false);
    }

    public CropSpawnAction(AbstractCropOrb cropOrb, int spawnAmount, boolean secondApplication) {
        this.duration = ACTION_DURATION;
        this.actionType = ACTION_TYPE;

        this.amount = spawnAmount > 0 ? spawnAmount : cropOrb.getAmount();
        if (cropOrb != null) {
            this.cropOrb = cropOrb;
        }
    }

    public void update() {
        Logger logger = TheSimpletonMod.logger;
        if (AbstractDungeon.player.maxOrbs > 0) {
            logger.debug("CropSpawnAction::update : Player can have orbs: " + AbstractDungeon.player.maxOrbs);
            if (AbstractCropOrb.hasCropOrb(this.cropOrb)) {
                logger.debug("CropSpawnAction::update : Player has cropOrb " + this.cropOrb.name);

                AbstractCropOrb cropOrb = AbstractCropOrb.getCropOrb(this.cropOrb);
                cropOrb.passiveAmount += this.amount;
                cropOrb.update();
                this.isDone = true;
                // trigger cropOrb crap
            } else if (secondApplication) {
                logger.debug("CropSpawnAction::update : Second tick but player doesn't have cropOrb " + this.cropOrb.name);

                this.isDone = true;
            } else {
                logger.debug("CropSpawnAction::update : Player doesn't have cropOrb " + this.cropOrb.name + ". Adding " + this.cropOrb.getAmount());

                AbstractDungeon.player.channelOrb(this.cropOrb);
            }
        }
        this.isDone = true;
        tickDuration();
    }

}



