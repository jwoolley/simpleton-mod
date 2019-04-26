package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.MulchingAction;
import thesimpleton.enums.AbstractCardEnum;

public class Mulching extends CustomCard {
  public static final String ID = "TheSimpletonMod:Mulching";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/mulching.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int UPGRADED_COST = 0;
  private static final int EXHAUST_AMOUNT = 1;
  private static final int BLOCK = 6;
  private static final int BURNING_AMOUNT = 4;
  private static final int HP_GAIN = 4;
  private static final int CARD_DRAW  = 2;

  public Mulching() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(HP_GAIN, CARD_DRAW), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = HP_GAIN;
    this.baseBlock = this.block = BLOCK;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new MulchingAction(EXHAUST_AMOUNT, this.block, BURNING_AMOUNT, HP_GAIN, CARD_DRAW));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Mulching();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBaseCost(UPGRADED_COST);
      initializeDescription();
    }
  }

  public static String getDescription(int burningAmount, int cardDrawAmount) {
    return DESCRIPTION + burningAmount + EXTENDED_DESCRIPTION[0] + cardDrawAmount + EXTENDED_DESCRIPTION[1];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}