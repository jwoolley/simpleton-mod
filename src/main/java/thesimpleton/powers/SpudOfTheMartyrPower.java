package thesimpleton.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.relics.SpudOfTheMartyr;

public class SpudOfTheMartyrPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:SpudOfTheMartyrPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "spudofthemartyr.png";

  private static final int CROP_AMOUNT = 1;
  private static SpudOfTheMartyr relic;

  public SpudOfTheMartyrPower(AbstractCreature owner, int amount, SpudOfTheMartyr relic) {
    super(IMG);

    logger.debug("Instantiating SpudOfTheMartyrPower");

    this.owner = owner;
    this.amount = amount;
    this.relic = relic;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + CROP_AMOUNT + DESCRIPTIONS[1];
  }

  @Override
  public int onAttacked(DamageInfo info, int damageAmount) {
    logger.debug("SpudOfTheMartyrPower.onAttacked damage: " + damageAmount);
    logger.debug("SpudOfTheMartyrPower.onAttacked damageType: " + info.type.name());

    if ((info.type != DamageInfo.DamageType.THORNS) && (info.type != DamageInfo.DamageType.HP_LOSS) && damageAmount > 0)
    {
      logger.debug("SpudOfTheMartyrPower.onAttacked took normal damage");
      this.relic.flash();
      SpudOfTheMartyr.addPotatoStack(CROP_AMOUNT);
    }
    return damageAmount;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}