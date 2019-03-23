package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.*;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.CurseUtil;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.PlantPotatoPower;

public class ReapAndSow extends CustomCard {
  public static final String ID = "TheSimpletonMod:ReapAndSow";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/reapandsow.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.SKILL;
  private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
  private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
//  private static final int BLOCK = 2;
  private static final int BLOCK_UPGRADE_BONUS = 2;
  private static final int PLANT_AMOUNT = 1;
  private static final int PLANT_AMOUNT_UPGRADE = 1;

  public ReapAndSow() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
//    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = PLANT_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    final AbstractCard HARVEST_CARD = CurseUtil.SHIV;
//    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    //TODO: need to add card-discard-discard pile vfx/sfx?
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(CurseUtil.HARVEST, 1));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new PlantPotatoPower(p, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new ReapAndSow();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
//      upgradeBlock(BLOCK_UPGRADE_BONUS);
      upgradeMagicNumber(PLANT_AMOUNT_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}