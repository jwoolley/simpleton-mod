package thesimpleton.cards.status;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.DeenergizedPower;
import thesimpleton.powers.DrawDownPower;
import thesimpleton.utilities.SimpletonColorUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Depletion extends CustomCard {
  public static final String ID = "TheSimpletonMod:Depletion";
  private static final CardStrings cardStrings;
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/depletion.png";

  private static final CardType TYPE = CardType.STATUS;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;

  private static final int NUM_STATUS_CARDS = 2;

  public Depletion() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY,
        TARGET);
    this.baseMagicNumber = this.magicNumber = NUM_STATUS_CARDS;
    this.exhaust = true;
  }

  private static ArrayList<AbstractCard> getStatusCardList() {
    return new ArrayList<AbstractCard>() {{
      add(new Burn());
      add(new Dazed());
      add(new Slimed());
      add(new VoidCard());
      add(new Wound());
    }};
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    if ((!this.dontTriggerOnUseCard)) {
      this.exhaust = true;
      if (p.hasRelic("Medical Kit")) {
        useMedicalKit(p);
      } else {
        List<AbstractCard> statusCards =  getRandomStatusCards(this.magicNumber);
        statusCards.forEach(card -> AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDrawPileAction(card, 1, false, false)));
      }
    } else {
      this.dontTriggerOnUseCard = false;

//      List<AbstractCard> statusCards =  getRandomStatusCards(this.magicNumber);
//      statusCards.forEach(card -> AbstractDungeon.actionManager.addToBottom(
//              new MakeTempCardInDiscardAction(card, 1)));

      AbstractDungeon.actionManager.addToBottom(
              new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), 1));

      this.exhaust = false;
    }
  }

  public static List<AbstractCard> getRandomStatusCards(int numCards) {
    List<AbstractCard> generatedStatusCards = new ArrayList<>();

    if (numCards > 0) {
      List<AbstractCard> possibleStatusCards = getStatusCardList();
      for (int i = 0; i < numCards; i++) {
        Collections.shuffle(possibleStatusCards, AbstractDungeon.cardRng.random);
        generatedStatusCards.add(possibleStatusCards.get(0));
      }
    }

    return generatedStatusCards;
  }

  public void triggerWhenDrawn()
  {
    if ((AbstractDungeon.player.hasPower("Evolve")) && (!AbstractDungeon.player.hasPower("No Draw")))
    {
      AbstractDungeon.player.getPower("Evolve").flash();
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player,
          AbstractDungeon.player.getPower("Evolve").amount));
    }
  }

  public void triggerOnEndOfTurnForPlayingCard() {
    this.dontTriggerOnUseCard = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
  }

  public AbstractCard makeCopy() {
    return new Depletion();
  }

  public void upgrade() {
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    return true;
  }

  public void triggerOnGlowCheck() {
    this.glowColor = SimpletonColorUtil.RED_BORDER_GLOW_COLOR.cpy();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
