package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;

public class Gnats extends CustomCard implements SeasonalCurse {
  public static final String ID = "TheSimpletonMod:Gnats";
  private static final CardStrings cardStrings;
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/gnats.png";

  private static final AbstractCard.CardType TYPE = AbstractCard.CardType.CURSE;
  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.CURSE;
  private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.NONE;

  private static final int THORNS_DAMAGE = 1;

  private static final int COST = -2;

  public Gnats() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCard.CardColor.CURSE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = THORNS_DAMAGE;
  }


  public boolean canPlay(AbstractCard card) {
    if (card.costForTurn == 0) {
      AbstractDungeon.actionManager.addToBottom(new SFXAction("BLUNT_FAST", 0.1f));

      card.cantUseMessage = EXTENDED_DESCRIPTION[0];
      return false;
    }
    return true;
  }


  // I guess this is how you do it
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (p.hasRelic("Blue Candle")) {
      this.useBlueCandle(p);
    } else {
      AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
    }

  }

  public AbstractCard makeCopy() {
    return new Gnats();
  }

  public void upgrade() {
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
