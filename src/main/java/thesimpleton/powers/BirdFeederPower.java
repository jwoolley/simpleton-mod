package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;


public class BirdFeederPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:BirdFeederPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "birdfeeder.png";

  private AbstractCreature source;

  private final int hpPerStack;

  public BirdFeederPower(AbstractCreature owner, AbstractCreature source, int amount, int hpPerStack) {
    super(IMG);
    this.owner = owner;
    this.source = source;
    this.amount = amount;
    this.hpPerStack = hpPerStack;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + hpPerStack + DESCRIPTIONS[2];
  }

  @Override
  public void onUseCard(AbstractCard c, UseCardAction action) {
    if (c.type == AbstractCard.CardType.POWER) {
      flash();
      AbstractDungeon.actionManager.addToBottom(new HealAction(owner, source, hpPerStack));
      AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, source, this, 1));
      updateDescription();
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}