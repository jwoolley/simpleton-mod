package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DoubleDigPower extends AbstractTheSimpletonPower {
  public static final String IMG = "doubledig.png";

  public static final String POWER_ID = "TheSimpletonMod:DoubleDigPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final int MAX_STACKS = 999;

  public static final int MAX_CARD_COST = 1;

  public DoubleDigPower(AbstractCreature owner, int amount)
  {
    super(IMG);
    this.name = NAME;
    this.ID = POWER_ID;
    this.owner = owner;
    this.amount = amount;
    this.type = POWER_TYPE;

    if (this.amount >= MAX_STACKS) {
      this.amount = MAX_STACKS;
    }

    updateDescription();
  }

  public void stackPower(int stackAmount) {
    super.stackPower(stackAmount);
    if (this.amount >= MAX_STACKS) {
      this.amount = MAX_STACKS;
    }
  }

  public void updateDescription() {
    if (this.amount == 1) {
      this.description = DESCRIPTIONS[0];
    } else {
      this.description = (DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2]);
    }
  }


  public void onUseCard(AbstractCard card, UseCardAction action) {
    if ((!card.purgeOnUse) && (card.costForTurn <= MAX_CARD_COST)
        && (card.type == AbstractCard.CardType.ATTACK
         || card.type == AbstractCard.CardType.SKILL
         || card.type == AbstractCard.CardType.POWER)
        && (this.amount > 0)) {

      flash();

      AbstractMonster m = null;
      if (action.target != null) {
        m = (AbstractMonster)action.target;
      }

      AbstractCard tmp = card.makeSameInstanceOf();
      AbstractDungeon.player.limbo.addToBottom(tmp);
      tmp.current_x = card.current_x;
      tmp.current_y = card.current_y;
      tmp.target_x = (Settings.WIDTH / 2.0F - 300.0F * Settings.scale);
      tmp.target_y = (Settings.HEIGHT / 2.0F);
      tmp.freeToPlayOnce = true;
      if (m != null) {
        tmp.calculateCardDamage(m);
      }
      tmp.purgeOnUse = true;
      AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse));

      this.amount--;
      if (this.amount == 0) {
        AbstractDungeon.actionManager.addToBottom(
            new RemoveSpecificPowerAction(this.owner, this.owner, "TheSimpletonMod:DoubleDigPower"));
      }
    }
  }

  public void atEndOfTurn(boolean isPlayer) {
    if (isPlayer) {
      AbstractDungeon.actionManager.addToBottom(
          new RemoveSpecificPowerAction(this.owner, this.owner, "TheSimpletonMod:DoubleDigPower"));
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
