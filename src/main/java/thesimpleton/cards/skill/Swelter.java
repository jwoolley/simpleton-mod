package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.enums.AbstractCardEnum;

public class Swelter extends CustomCard {
  public static final String ID = "TheSimpletonMod:Swelter";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/swelter.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 0;
  private static final int BLOCK = 4;
  private static final int BLOCK_UPGRADE = 2;
  private static final int BURNING = 4;
  private static final int BURNING_UPGRADE = 2;

  private static final int WEAK = 1;
  private static final int WEAK_UPGRADE = 1;

  private int weak;

  public Swelter() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = BURNING;
    this.baseBlock = this.block = BLOCK;
    this.weak = WEAK;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {

    AbstractDungeon.actionManager.addToBottom(
      new ApplyPowerAction(m, p, new WeakPower(m, this.weak, false), this.weak));

    AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(m, p, this.magicNumber));

    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Swelter();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.weak += WEAK_UPGRADE;
      upgradeMagicNumber(BURNING_UPGRADE);
      upgradeBlock(BLOCK_UPGRADE);
      this.rawDescription = UPGRADE_DESCRIPTION;
      initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}