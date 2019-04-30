package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Spinach;
import thesimpleton.powers.utils.Crop;

public class PlantSpinachPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.SPINACH;
  public static final String POWER_ID = "TheSimpletonMod:PlantSpinachPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantspinach.png";
  public static final CardRarity cropRarity = CardRarity.RARE;
  private static final AbstractCropPowerCard powerCard = new Spinach();

  private static final int MATURITY_THRESHOLD = 2;

  private static int BASE_STRENGTH_PER_STACK = 1;
  private int strengthPerStack;

  public PlantSpinachPower(AbstractCreature owner, int amount) {
    this(owner, amount, false);
  }

  public PlantSpinachPower(AbstractCreature owner, int amount, boolean isFromCard) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount, isFromCard,false, MATURITY_THRESHOLD);
    this.strengthPerStack = BASE_STRENGTH_PER_STACK;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0] + this.strengthPerStack + DESCRIPTIONS[1];
  }

  @Override
  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToTop(
          new ApplyPowerAction(this.owner, this.owner,
              new StrengthPower(this.owner, strengthPerStack), harvestAmount * strengthPerStack));
    }
    return harvestAmount;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
