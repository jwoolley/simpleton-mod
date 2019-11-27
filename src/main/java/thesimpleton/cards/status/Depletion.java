package thesimpleton.cards.status;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.DeenergizedPower;
import thesimpleton.powers.DrawDownPower;

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

  private static final int DEBUFF_AMOUNT = 1;

  public Depletion() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY,
        TARGET);
    this.baseMagicNumber = this.magicNumber = DEBUFF_AMOUNT;
    this.exhaust = true;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    if ((!this.dontTriggerOnUseCard)) {
      this.exhaust = true;
      if (p.hasRelic("Medical Kit")) {
        useMedicalKit(p);
      } else {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new DeenergizedPower(p, DEBUFF_AMOUNT), DEBUFF_AMOUNT));
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new DrawDownPower(p, DEBUFF_AMOUNT), DEBUFF_AMOUNT));
      }
    } else {
      this.dontTriggerOnUseCard = false;
      AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), 1));
      this.exhaust = false;
    }
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

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
