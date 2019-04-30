package thesimpleton.actions;


import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class MulchingAction extends AbstractGameAction {
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;

  private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("MulchingAction");
  public static final String[] TEXT = uiStrings.TEXT;

  private final AbstractPlayer p;
  private final int exhaustAmount;
  private final int blockAmount;
  private final int burningAmount;
  private final int cardDrawAmount;

  public MulchingAction(int exhaustAmount, int blockAmount, int burningAmount, int cardDrawAmount) {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.p = AbstractDungeon.player;
    this.duration = ACTION_DURATION;

    this.exhaustAmount = exhaustAmount;
    this.blockAmount = blockAmount;
    this.burningAmount = burningAmount;
    this.cardDrawAmount = cardDrawAmount;
  }

  private void processCard(AbstractCard card) {
    switch (card.type) {
      case ATTACK:
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.blockAmount));
        return;
      case SKILL:
          AbstractDungeon.getMonsters().monsters.stream()
              .filter(mo -> !mo.isDeadOrEscaped())
              .forEach(mo -> AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(mo, this.p, burningAmount)));
        return;
      case POWER:
      case CURSE:
      case STATUS:
      default:
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.p, this.cardDrawAmount));
    }
  }

  public void update() {
    if (this.duration == Settings.ACTION_DUR_FAST) {
      if (this.p.hand.isEmpty()) {
        this.isDone = true;
        return;
      }
      if (this.p.hand.size() == 1) {
        AbstractCard c = this.p.hand.getBottomCard();
        this.p.hand.moveToExhaustPile(c);
        processCard(c);
        tickDuration();
        return;
      }
      AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
      tickDuration();
      return;
    }
    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
      for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
        this.p.hand.moveToExhaustPile(c);
        processCard(c);
      }
      AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
      AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
    }
    tickDuration();
  }
}