package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.NumbPower;

public class Frostbite extends CustomCard {
  public static final String ID = TheSimpletonMod.makeID("Frostbite");
  private static final CardStrings cardStrings;
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/frostbite.png";

  private static final CardType TYPE = CardType.CURSE;
  private static final CardRarity RARITY = CardRarity.CURSE;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = -2;
  private static final int NUMB_AMOUNT = 1;

  public Frostbite() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.CURSE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = NUMB_AMOUNT;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    if (this.dontTriggerOnUseCard) {
      AbstractDungeon.actionManager.addToTop(
          new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
              new NumbPower(AbstractDungeon.player , NUMB_AMOUNT), NUMB_AMOUNT));
    }
  }

  public void triggerOnEndOfTurnForPlayingCard() {
    this.dontTriggerOnUseCard = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
  }

  public AbstractCard makeCopy() { return new Frostbite(); }

  public void upgrade() {}

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}