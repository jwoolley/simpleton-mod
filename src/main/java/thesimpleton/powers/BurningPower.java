package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;


public class BurningPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:BurningPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.DEBUFF;
  public static final String IMG = "burning.png";

  private AbstractCreature source;

  public BurningPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(IMG);
    this.owner = owner;
    this.source = source;
    this.amount = amount;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
  }

  @Override
  public void atEndOfTurn(boolean isPlayer) {
    activateBurning(this.amount);
  }

  @Override
  public void atEndOfRound() {
    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
  }

  public void activateBurning(int numStacks) {
    DamageInfo burningDamageInfo = new DamageInfo(this.source, numStacks, DamageInfo.DamageType.NORMAL);
      this.flash();
      DamageAction action = new DamageAction(this.owner, burningDamageInfo, AbstractGameAction.AttackEffect.FIRE);
      AbstractDungeon.actionManager.addToTop(action);
    }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
