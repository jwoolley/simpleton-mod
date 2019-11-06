package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.Potatoes;

public class
SpudOfTheMartyr extends CustomRelic implements CustomSavable<Integer> {
    public static final String ID = "TheSimpletonMod:SpudOfTheMartyr";
    public static final String IMG_PATH = "relics/spudofthemartyr.png";
    public static final String IMG_PATH_LARGE = "relics/spudofthemartyr_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/spudofthemartyr_outline.png";

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.HEAVY;
    private static final int TRIGGER_THRESHOLD = 3;
    private static final int STR_AMOUNT = 1;
    private static final int DEX_AMOUNT = 1;

    private static final AbstractCard CARD_TO_GAIN = new Potatoes();

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
        return this.DESCRIPTIONS[0] + TRIGGER_THRESHOLD + DESCRIPTIONS[1] + STR_AMOUNT + DESCRIPTIONS[2]
            + DEX_AMOUNT + DESCRIPTIONS[3] + CARD_TO_GAIN.name + DESCRIPTIONS[4];
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
                this.counter++;
                if (this.counter >= TRIGGER_THRESHOLD) {
                    this.flash();

                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player,
                        new GainStrengthPower(player, STR_AMOUNT), STR_AMOUNT));

                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player,
                        new DexterityPower(player, DEX_AMOUNT),DEX_AMOUNT));

                    AbstractDungeon.actionManager.addToBottom(
                        new MakeTempCardInHandAction(CARD_TO_GAIN.makeStatEquivalentCopy()));
                    this.counter = 0;
                }
            }
        }
        return damageAmount;
    }

    @Override
    public Integer onSave() {
        return this.counter;
    }

    @Override
    public void onLoad(Integer counter) {
        this.counter = counter;
    }

    static {
        CARD_TO_GAIN.upgrade();
    }
}
