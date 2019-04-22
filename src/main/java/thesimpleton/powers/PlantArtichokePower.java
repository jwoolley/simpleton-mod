package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Artichokes;
import thesimpleton.powers.utils.Crop;

public class PlantArtichokePower extends AbstractCropPower {
  public static final Crop enumValue = Crop.ARTICHOKES;
  public static final String POWER_ID = "TheSimpletonMod:PlantArtichokePower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantartichoke.png";
  public static final CardRarity cropRarity = CardRarity.RARE;
  private static final AbstractCropPowerCard powerCard = new Artichokes();

  private static final int MATURITY_THRESHOLD = 2;

  private static int BASE_PLATED_ARMOR_PER_STACK = 3;
  private int platedArmorPerStack;

  public PlantArtichokePower(AbstractCreature owner, int amount) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount, false, MATURITY_THRESHOLD);
    this.platedArmorPerStack = BASE_PLATED_ARMOR_PER_STACK;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0] + this.platedArmorPerStack + DESCRIPTIONS[1];
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(
              this.owner, this.owner,
              new PlatedArmorPower(this.owner, platedArmorPerStack), harvestAmount * platedArmorPerStack));
    }
    return harvestAmount;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}