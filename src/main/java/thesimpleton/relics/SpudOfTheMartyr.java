package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.orbs.PotatoCropOrb;
import thesimpleton.powers.SpudOfTheMartyrPower;

public class
SpudOfTheMartyr extends CustomRelic {
    public static final String ID = "TheSimpletonMod:SpudOfTheMartyr";
    public static final String IMG_PATH = "relics/spudofthemartyr.png";
    public static final String IMG_PATH_LARGE = "relics/spudofthemartyr_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/spudofthemartyr_outline.png";

    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.HEAVY;

    private static final int CROP_AMOUNT = 2;

    public SpudOfTheMartyr() {
        super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
                new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
        this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));

        Logger logger = TheSimpletonMod.logger;
        logger.info("Instantiating SpudOfTheMartyr");
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + CROP_AMOUNT + this.DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        final AbstractPlayer player = AbstractDungeon.player;
        this.flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(player, player,
            new SpudOfTheMartyrPower(player, -1, this), -1));
        addPotatoStack(CROP_AMOUNT);
    }

    @Override

    public AbstractRelic makeCopy() {
        return new SpudOfTheMartyr();
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(SpudOfTheInnocent.ID);
    }


    //TODO: move this to potato power class
    public static void addPotatoStack(int amount) {
        Logger logger = TheSimpletonMod.logger;
        logger.debug("SpudOfTheMartyr: Adding potato stack");
        final AbstractPlayer p = AbstractDungeon.player;

        AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new PotatoCropOrb(amount), false));
    }

    @Override
    public void obtain() {
        this.flash();
        if (AbstractDungeon.player.hasRelic(SpudOfTheInnocent.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(SpudOfTheInnocent.ID)) {
                    this.instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }
}
