package thesimpleton.actions;


import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.NumbPower;

import java.util.List;
import java.util.stream.Collectors;

public class NumbAction extends AbstractGameAction {
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;

  private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("TheSimpletonMod:MulchAction");
  public static final String[] TEXT = uiStrings.TEXT;

  private final NumbPower power;
  private final AbstractPlayer p;

  public NumbAction(int costIncreaseAmount, NumbPower power) {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.p = AbstractDungeon.player;
    this.duration = ACTION_DURATION;
    this.amount = costIncreaseAmount;
    this.power = power;
  }

  public void update() {
    TheSimpletonMod.traceLogger.trace("TheSimpletonMod:NumbAction: update called");

    if (this.duration == ACTION_DURATION) {
      if (!this.p.hand.isEmpty()) {
        List<AbstractCard> increasableCards =  AbstractDungeon.player.hand.group.stream()
            .filter(c -> c.costForTurn >= 0).collect(Collectors.toList());

        if (increasableCards.size() > 0) {
          updateRandomCardCostForTurn(increasableCards);
          power.flash();
        }
      }
      this.isDone = true;
      return;
    }
    tickDuration();
  }

  private void updateRandomCardCostForTurn(List<AbstractCard> increasableCards) {
    TheSimpletonMod.traceLogger.trace("TheSimpletonMod:NumbAction: updateRandomCardCostForTurn called");
    AbstractCard card = increasableCards.get(AbstractDungeon.cardRng.random(increasableCards.size() - 1));
    card.costForTurn += this.amount;
    card.isCostModifiedForTurn = true;
    card.superFlash(Color.BLUE.cpy());
  }
}