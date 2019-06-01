package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.status.Depletion;
import thesimpleton.enums.AbstractCardEnum;

public class DigIn extends CustomCard {
  public static final String ID = "TheSimpletonMod:DigIn";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/digin.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int BLOCK = 10;
  private static final int UPGRADE_BLOCK_AMOUNT = 4;

  public DigIn() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Depletion(), 1));
  }

  @Override
  public AbstractCard makeCopy() {
    return new DigIn();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(UPGRADE_BLOCK_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
