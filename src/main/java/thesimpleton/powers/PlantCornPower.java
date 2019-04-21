package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Corn;
import thesimpleton.powers.utils.Crop;

public class PlantCornPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.CORN;

  public static final String POWER_ID = "TheSimpletonMod:PlantCornPower";
  private static final PowerStrings powerStrings;

  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantcorn.png";
  public static final CardRarity cropRarity = CardRarity.UNCOMMON;
  private static final AbstractCropPowerCard powerCard = new Corn();

  private static final int MATURITY_THRESHOLD = 2;

  public PlantCornPower(AbstractCreature owner, int amount) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard,  amount, false, MATURITY_THRESHOLD);
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0];
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, harvestAmount));
    }
    return harvestAmount;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
