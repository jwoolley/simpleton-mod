package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.orbs.PotatoCropOrb;
import thesimpleton.powers.PhotosynthesisPower;
import thesimpleton.powers.SpudOfTheMartyrPower;

public class
SpudOfTheMartyr extends CustomRelic {
    public static final String ID = "TheSimpletonMod:SpudOfTheMartyr";
    public static final String IMG_PATH = "relics/spudofthemartyr.png";
    public static final String IMG_PATH_LARGE = "relics/spudofthemartyr_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/spudofthemartyr_outline.png";

    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.HEAVY;
    private static final int SPUD_AMOUNT = 2;

    private static final Logger logger = TheSimpletonMod.logger;

    public SpudOfTheMartyr() {
        super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
                new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
        this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));

        Logger logger = TheSimpletonMod.logger;
        logger.debug("Instantiating SpudOfTheMartyr");
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new SpudOfTheMartyr();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        logger.info("SpudOfTheMartyr::onAttacked called");

        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer player = AbstractDungeon.player;
            if ((info.type != DamageInfo.DamageType.THORNS) && (info.type != DamageInfo.DamageType.HP_LOSS) && damageAmount > 0) {
                logger.info("SpudOfTheMartyr::onAttacked took normal damage");
                this.flash();
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(player, player, new SpudOfTheMartyrPower(player, SPUD_AMOUNT, this)));
            }
        }
        return damageAmount;
    }
}
