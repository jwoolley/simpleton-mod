package thesimpleton.orbs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.ToughSkinPower;
import thesimpleton.powers.utils.Crop;
import thesimpleton.relics.CashCrop;
import thesimpleton.relics.GrassPellets;
import thesimpleton.relics.TheHarvester;
import thesimpleton.utilities.Trigger;

import java.util.*;

import static thesimpleton.cards.SimpletonUtil.getPlayer;

public abstract class AbstractCrop extends AbstractOrb {

    public static final Logger logger = LogManager.getLogger(AbstractCrop.class.getName());

    public float NUM_X_OFFSET = 1.0F * Settings.scale;
    public float NUM_Y_OFFSET = -35.0F * Settings.scale;

    public boolean upgraded = false;
    public boolean activatedThisTurn = false;
    public int UniqueFocus;
    public float scale = 1F;
    private Texture img;

    public AbstractPlayer p;

    // CROP STUFF
    private static boolean IS_HARVEST_ALL = false;
    private static int AUTO_HARVEST_THRESHOLD = 5;
    private static int CROP_POWER_ID_COUNTER = 0;
    private static int STACKS_PER_TRIGGER = 1;
    private static boolean hasHarvestedThisTurn = false;

    private static final String ORB_DESCRIPTION_ID = "TheSimpletonMod:AbstractCrop";
    private static final OrbStrings orbStrings;
    public static final String[] PASSIVE_DESCRIPTIONS;
    private static final List<AbstractCropPower> referencePowers = new ArrayList<>();;

    public final Crop enumValue;
    public final String[] descriptions;
    private final int cropPowerId;
    private final boolean isHarvestAll;
    private int autoHarvestThreshold;

    private int amount = 0;
    private AbstractCard.CardRarity cropRarity;

    public static Map<AbstractCard.CardRarity, Integer> CROP_RARITY_DISTRIBUTION;

    AbstractCrop(Crop enumValue, String name, String id, String[] descriptions, String imgName, AbstractCard.CardRarity rarity,
                      AbstractCropPowerCard powerCard, int amount) {
        this(enumValue, name, id, descriptions, imgName, rarity, powerCard, amount, IS_HARVEST_ALL, AUTO_HARVEST_THRESHOLD);
        logger.debug("Instantiating CropPower:  enumValue:" + enumValue + ",  name:" + name+ ",  id:" + id);
    }

    AbstractCrop(Crop enumValue, String name, String id, String[] descriptions, String imgName, AbstractCard.CardRarity rarity,
                 AbstractCropPowerCard powerCard, int amount, boolean isHarvestAll) {
        this(enumValue, name, id, descriptions, imgName, rarity, powerCard, amount, isHarvestAll, AUTO_HARVEST_THRESHOLD);
    }

    AbstractCrop(Crop enumValue, String name, String id, String[] descriptions, String imgName, AbstractCard.CardRarity rarity,
                 AbstractCropPowerCard powerCard, int amount, boolean isHarvestAll,
                 int autoHarvestThreshold) {
        super();
        this.img = new Texture(imgName);
        this.enumValue = enumValue;
        this.ID = id;
        this.name = name;
        this.descriptions = descriptions;
        this.amount = amount;
        this.cropRarity = rarity;
        this.isHarvestAll = isHarvestAll;
        this.autoHarvestThreshold = autoHarvestThreshold;
        this.cropPowerId = CROP_POWER_ID_COUNTER++;
        getPlayer().getCropUtil().onCropOrbGained(this);

        logger.debug(this.name + ": gained " + amount + " stacks.");
    }

    // The actual crop needs to implement this
    abstract protected int harvestAction(int harvestAmount);

    public void onEndOfTurn() {
        this.activatedThisTurn = false;
    }

    public void flash() {
        // I hope this works!!!!
        this.triggerEvokeAnimation();
    }

    @Override
    public void onStartOfTurn() {
        logger.debug("Checking for auto-harvest triggers (amount: " + this.amount + "; hasHarvester:  " +  getPlayer().hasRelic(TheHarvester.ID)+ ")");
        if (this.amount >= autoHarvestThreshold &&  getPlayer().hasRelic(TheHarvester.ID)) {
            logger.debug("Triggering Harvester");
            getPlayer().getRelic(TheHarvester.ID).flash();
            this.flash();
            this.harvest(isHarvestAll, STACKS_PER_TRIGGER);
        } else { logger.debug("Not triggered"); }
    }

    public void harvest(boolean harvestAll, int maxHarvestAmount) {
        onHarvest();
        final int harvestAmount = calculateHarvestAmount(this.amount, maxHarvestAmount, harvestAll);
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            if (harvestAmount > 0) {
                this.flash();
                this.amount -= harvestAction(harvestAmount);
            }
        }
    }


    protected int calculateHarvestAmount(int amount, int maxAmount, boolean harvestAll) {
        return Math.min(amount, harvestAll ? amount : maxAmount);
    }

    public void applyFocus() {
        super.applyFocus();
        int bonus = 0;
        this.passiveAmount = this.basePassiveAmount + this.UniqueFocus + bonus;
        updateDescription();
    }

    public void onEvoke() {
        triggerEvokeAnimation();
    }

    public void triggerEvokeAnimation() {
        // Nothing yet
    }

    protected String getPassiveDescription() {
        return PASSIVE_DESCRIPTIONS[0] + this.autoHarvestThreshold + PASSIVE_DESCRIPTIONS[1];
    }

    void onHarvest() {
        hasHarvestedThisTurn = true;
        logger.debug(this.name + ": harvested. Set hasHarvestedThisTurn");

        if (getPlayer().hasPower(ToughSkinPower.POWER_ID)) {
            ((ToughSkinPower) getPlayer().getPower(ToughSkinPower.POWER_ID)).applyPower(getPlayer());
        }

        if (getPlayer().hasRelic(GrassPellets.ID)) {
            ((GrassPellets) getPlayer().getRelic(GrassPellets.ID)).increaseCountAndMaybeActivate();
        }

        if (getPlayer().hasRelic(CashCrop.ID)) {
            ((CashCrop) getPlayer().getRelic(CashCrop.ID)).onHarvest(this.enumValue);
        }
    }

    public void activateEffect() {
        // Nothing
    }

    public void playChannelSFX() {
        // Nothing
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.c);
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.c.a / 3.0F));
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.2F, this.scale * 1.2F, this.angle / 1.2F, 0, 0, 96, 96, false, false);
        sb.draw(this.img, this.cX - 48.0F, this.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale * 1.5F, this.scale * 1.5F, this.angle / 1.4F, 0, 0, 96, 96, false, false);
        sb.setBlendFunction(770, 771);
        this.renderText(sb);
        this.hb.render(sb);
    }

    protected void renderText(SpriteBatch sb) {
        FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.amount), this.cX + NUM_X_OFFSET, this.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
    }


    static {
        orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_DESCRIPTION_ID);
        PASSIVE_DESCRIPTIONS = orbStrings.DESCRIPTION;

        // define rarity distribution
        Map<AbstractCard.CardRarity, Integer> rarityDistribution = new HashMap<>();
        rarityDistribution.put(AbstractCard.CardRarity.BASIC, 27);
        rarityDistribution.put(AbstractCard.CardRarity.COMMON, 18);
        rarityDistribution.put(AbstractCard.CardRarity.UNCOMMON, 12);
        rarityDistribution.put(AbstractCard.CardRarity.RARE, 8);
        CROP_RARITY_DISTRIBUTION = Collections.unmodifiableMap(rarityDistribution);

        // reset hasHarvestedThisTurn at start of combat and at end of turn
        Trigger resetHasHarvested = () -> {
            logger.debug("Resetting hasHarvestedThisTurn");
            hasHarvestedThisTurn = false;
        };
        TheSimpletonCharacter.addPrecombatPredrawTrigger(resetHasHarvested);
        TheSimpletonCharacter.addStartOfTurnTrigger(resetHasHarvested);
        TheSimpletonCharacter.addEndOfTurnTrigger(resetHasHarvested);

    }
}
