package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.CurseUtil;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.AbstractCropPower;

public class SoilCommunion extends CustomCard {
  public static final String ID = "TheSimpletonMod:SoilCommunion";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/soilcommunion.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK = 7;
  private static final int UPGRADE_BLOCK_AMOUNT = 3;
  private static final int CROP_INCREASE_AMOUNT = 1;

  public SoilCommunion() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = CROP_INCREASE_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    if (AbstractCropPower.playerHasAnyActiveCropPowers()) {
      AbstractCropPower.getNewestPower().stackPower(this.magicNumber);
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new SoilCommunion();
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
