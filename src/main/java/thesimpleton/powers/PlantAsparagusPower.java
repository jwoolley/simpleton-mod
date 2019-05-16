package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;

import thesimpleton.cards.power.crop.Asparagus;
import thesimpleton.powers.utils.Crop;

public class PlantAsparagusPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.ASPARAGUS;
  public static final String POWER_ID = "TheSimpletonMod:PlantAsparagusPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantasparagus.png";
  public static final CardRarity cropRarity = CardRarity.UNCOMMON;
  private static final AbstractCropPowerCard powerCard = new Asparagus();

  private static final int MATURITY_THRESHOLD = 3;

  private static int BASE_DEXTERITY_PER_STACK = 1;
  private int dexPerStack;

  public PlantAsparagusPower(AbstractCreature owner, int amount) {
    this(owner, amount, false);
  }

  public PlantAsparagusPower(AbstractCreature owner, int amount, boolean isFromCard) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount, isFromCard,false, MATURITY_THRESHOLD);
    this.dexPerStack = BASE_DEXTERITY_PER_STACK;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0] + this.dexPerStack + DESCRIPTIONS[1];
  }

  @Override
  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToTop(
          new ApplyPowerAction(this.owner, this.owner,
              new DexterityPower(this.owner, dexPerStack), harvestAmount * dexPerStack));
    }
    return harvestAmount;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
