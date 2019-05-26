package thesimpleton.orbs;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.powers.utils.Crop;
import thesimpleton.relics.HotPotato;

public class PotatoCrop extends AbstractCrop {

    public static final Logger logger = LogManager.getLogger(PotatoCrop.class.getName());

    public static final String ID = "TheSimpletonMod:PotatoCrop";

    public static final Crop enumValue = Crop.POTATOES;

    private static final OrbStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static final String IMG = "TheSimpletonMod/img/orbs/plantpotato.png";
    public static final AbstractCard.CardRarity cropRarity = AbstractCard.CardRarity.COMMON;
    private static final AbstractCropPowerCard powerCard = new Potatoes();

    public PotatoCrop(int amount) {
        super(enumValue, NAME, ID, DESCRIPTIONS, IMG, cropRarity, powerCard, amount);
        this.name = NAME;
        updateDescription();
        logger.debug("PLANTIN' POTATOES (instantiating {}). name: {}", ID, NAME);
    }

    @Override
    public void updateDescription() {
        this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0];
    }

    protected int harvestAction(int harvestAmount) {
        if (harvestAmount > 0) {
            if (SimpletonUtil.getPlayer().hasRelic(HotPotato.ID)) {
                SimpletonUtil.getPlayer().getRelic(HotPotato.ID).flash();
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.FLAMING_SPUD, harvestAmount));
            } else {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.SPUD_MISSILE, harvestAmount));
            }
        }
        return harvestAmount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getOrbString(ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTION;
    }

    @Override
    public AbstractOrb makeCopy() {
        return new PotatoCrop(1);
    }
}
