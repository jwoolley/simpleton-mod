package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.enums.AbstractCardEnum;

public class SpiceUp extends CustomCard {
  public static final String ID = "TheSimpletonMod:SpiceUp";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/spiceup.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;

  private static final int COST = 1;
  private static final int BURNING_AMOUNT = 10;
  private static final int UPGRADED_BURNING_AMOUNT = 2;
  private static final int DRAW_AMOUNT = 1;
  private static final int UPGRADED_DRAW_AMOUNT = 1;

  private int drawAmount;

  public SpiceUp() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(DRAW_AMOUNT), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = BURNING_AMOUNT;
    this.drawAmount = DRAW_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(m, p, this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.drawAmount));
  }

  @Override
  public AbstractCard makeCopy() {
    return new SpiceUp();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(UPGRADED_BURNING_AMOUNT);
      this.drawAmount += UPGRADED_DRAW_AMOUNT;
      this.rawDescription = getDescription();
      initializeDescription();
    }
  }

  public String getDescription() {
    return getDescription(this.drawAmount);
  }

  public static String getDescription(int drawAmount) {
    return DESCRIPTION + drawAmount + (drawAmount > 1 ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0]);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
