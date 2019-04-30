package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.WeakPower;

public class VolatileFumesPower extends AbstractTheSimpletonPower  {
  public static final String POWER_ID = "TheSimpletonMod:VolatileFumesPower";
  private static final PowerStrings powerStrings;

  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "volatilefumes.png";

  private AbstractCreature source;

  public VolatileFumesPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(IMG);
    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;
    this.owner = owner;
    this.source = source;
    this.amount = amount;

    updateDescription();
  }

  public void updateDescription() {
    this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
  }

  public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
  {
    if (target.hasPower(WeakPower.POWER_ID) && (target != this.owner) && (info.type == DamageInfo.DamageType.NORMAL)) {
      flashWithoutSound();
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
          target, this.owner, new BurningPower(target, this.owner, this.amount), this.amount, true));
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}

