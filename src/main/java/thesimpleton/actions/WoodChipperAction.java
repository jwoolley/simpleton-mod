package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class WoodChipperAction extends AbstractGameAction {

  private static final ActionType ACTION_TYPE = ActionType.EXHAUST;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;

  public static final String UI_NAME = "TheSimpletonMod:WoodChipperAction";
  public static final UIStrings uiStrings;
  private static final String[] TEXT;

  private AbstractPlayer p;

  public WoodChipperAction(AbstractPlayer player, int numCardsToChoose) {
    this.p = player;
    setValues(this.p,  this.p, numCardsToChoose);
    this.amount = numCardsToChoose;

    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION;
  }

  public void update() {
    if (this.duration == Settings.ACTION_DUR_FAST) {
      AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount,
          false, true, false, false, true);
      AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));

      if (p.hand.isEmpty()) {
        this.isDone = true;
      }
      tickDuration();
      return;
    }

    if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
      if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.p, AbstractDungeon.handCardSelectScreen.selectedCards.group
            .size()));
        for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
          this.p.hand.moveToExhaustPile(c);
          c.triggerOnExhaust();
          CardCrawlGame.dungeon.checkForPactAchievement();
        }
      }
      AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
      this.isDone = true;
    }
    tickDuration();
  }
  static {
    uiStrings = CardCrawlGame.languagePack.getUIString(UI_NAME);
    TEXT = uiStrings.TEXT;
  }
}