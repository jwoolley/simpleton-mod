package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.SlowPower;

public class LoseSlowPower extends AbstractTheSimpletonPower {

  public static final String POWER_ID = "TheSimpletonMod:LoseSlowPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "loseslow.png";

  public LoseSlowPower(AbstractCreature owner) {
    super(IMG);
    this.owner = owner;
    this.amount = -1;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0];
  }

  public void atStartOfTurn() {
      this.flash();
      AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, SlowPower.POWER_ID));
      AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
