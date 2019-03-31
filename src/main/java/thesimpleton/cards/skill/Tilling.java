package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.TillingAction;
import thesimpleton.enums.AbstractCardEnum;

public class Tilling extends CustomCard {
  public static final String ID = "TheSimpletonMod:Tilling";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/tilling.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int NUM_POWERS = 2;
  private static final int NUM_POWERS_UPGRADE = 1;

  public Tilling() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.exhaust = true;
    this.baseMagicNumber = this.magicNumber = NUM_POWERS;

    TheSimpletonMod.logger.debug("Tilling::constructor NUM_POWERS:" + NUM_POWERS);
    TheSimpletonMod.logger.debug("Tilling::constructor this.baseMagicNumber:" + this.baseMagicNumber);

  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    TheSimpletonMod.logger.debug("Tilling.use: creating TillingAction with " + this.magicNumber + " random powers");

    AbstractDungeon.actionManager.addToBottom(new TillingAction(this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Tilling();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_POWERS_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}