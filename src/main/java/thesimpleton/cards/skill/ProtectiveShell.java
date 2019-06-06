package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ModifyBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.enums.AbstractCardEnum;

public class ProtectiveShell  extends AbstractDynamicTextCard  {
  public static final String ID = "TheSimpletonMod:ProtectiveShell";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/protectiveshell.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK = 12;
  private static final int BLOCK_REDUCTION = 6;
  private static final int BLOCK_REDUCTION_UPGRADE = -3;

  public ProtectiveShell() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(BLOCK_REDUCTION), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = BLOCK_REDUCTION;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    int blockReduction = getBlockReduction(this.magicNumber);

    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    if (blockReduction >= this.block) {
      AbstractDungeon.actionManager.addToBottom(new ModifyBlockAction(this.uuid, -this.block));
      this.exhaust = true;
    } else {
      AbstractDungeon.actionManager.addToBottom(new ModifyBlockAction(this.uuid, -blockReduction));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new ProtectiveShell();
  }

  private static int getBlockReduction(int initialReduction) {
    return SimpletonUtil.isPlayerInCombat() ? AbstractCard.applyPowerOnBlockHelper(initialReduction) : initialReduction;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(BLOCK_REDUCTION_UPGRADE);
    }
  }

  @Override
  protected void updateDescription(boolean extendedDescription) {
    this.rawDescription = getDescription(this.magicNumber);
    this.initializeDescription();
  }

  private static String getDescription(int reductionAmount) {
    return DESCRIPTION + getBlockReduction(reductionAmount) + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
