package thesimpleton.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.TheSimpletonCardTags;

import java.util.List;
import java.util.stream.Collectors;

public class BiorefinementPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:BiorefinementPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "biorefinement.png";

  private int timesTriggeredThisTurn;

  private static final int DISCOUNT_AMOUNT = 1;

  public BiorefinementPower(int amount) {
    super(IMG);

    TheSimpletonMod.logger.info("TheSimpletonMod:BiorefinementPower: constructor called");

    this.amount = amount;
    this.owner = AbstractDungeon.player;
    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    this.timesTriggeredThisTurn = 0;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = (this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2])
      + DESCRIPTIONS[3];
  }

  @Override
  public void onUseCard(AbstractCard card, UseCardAction action) {
    TheSimpletonMod.logger.info("TheSimpletonMod:BiorefinementPower: onUseCard called");

    if (timesTriggeredThisTurn < this.amount && card.hasTag(TheSimpletonCardTags.HARVEST)) {
      if (!AbstractDungeon.player.hand.group.isEmpty()) {
        List<AbstractCard> decreasableCards = AbstractDungeon.player.hand.group.stream()
                .filter(c -> c.costForTurn > 0).collect(Collectors.toList());

        if (decreasableCards.size() > 0) {
          discountRandomCardCostForTurn(decreasableCards);
        }
      }
      timesTriggeredThisTurn++;
    }
  }

  public void atStartOfTurn() {
    this.timesTriggeredThisTurn = 0;
  }

  private void discountRandomCardCostForTurn(List<AbstractCard> decreasableCards) {
    TheSimpletonMod.logger.info("TheSimpletonMod:BiorefinementPower: discountRandomCardCostForTurn called");
    AbstractCard card = decreasableCards.get(AbstractDungeon.cardRng.random(decreasableCards.size() - 1));
    card.costForTurn -= this.DISCOUNT_AMOUNT;
    card.cost -= this.DISCOUNT_AMOUNT;
    card.isCostModifiedForTurn = true;

    // TODO: reimplement as an action & wait a tick between power flash and card flash
    this.flash();
    card.superFlash(Color.GOLD.cpy());
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
