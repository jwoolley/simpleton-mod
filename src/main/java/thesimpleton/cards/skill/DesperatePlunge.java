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
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.DeenergizedPower;

public class DesperatePlunge extends CustomCard {
  public static final String ID = "TheSimpletonMod:DesperatePlunge";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/desperateplunge.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 3;
  private static final int BLOCK = 30;
  private static final int UPGRADE_BLOCK_AMOUNT = 10;
  private static final int COST_DISCOUNT_PER_POWER_PLAYED = 1;
  private static final int ENERGY_LOSS_AMOUNT = 1;

  public DesperatePlunge() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DeenergizedPower(p, ENERGY_LOSS_AMOUNT), ENERGY_LOSS_AMOUNT));
  }

  @Override
  public void triggerOnOtherCardPlayed(AbstractCard c) {
    if (c.type == CardType.POWER && this.costForTurn > 0) {
      this.modifyCostForTurn(-COST_DISCOUNT_PER_POWER_PLAYED);
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new DesperatePlunge();
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
