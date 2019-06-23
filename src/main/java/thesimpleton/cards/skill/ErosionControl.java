package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ErosionControlAction;
import thesimpleton.enums.AbstractCardEnum;

public class ErosionControl extends CustomCard {
  public static final String ID = "TheSimpletonMod:ErosionControl";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/erosioncontrol.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
  private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
  private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

  private static final int COST = 3;
  private static final int INTANGIBLE_AMOUNT = 1;
  private static final int INTANGIBLE_UPGRADE_AMOUNT = 1;

  public ErosionControl() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = INTANGIBLE_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ErosionControlAction(p, this.magicNumber,true));
  }

  @Override
  public AbstractCard makeCopy() {
    return new ErosionControl();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(INTANGIBLE_UPGRADE_AMOUNT);
      initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}