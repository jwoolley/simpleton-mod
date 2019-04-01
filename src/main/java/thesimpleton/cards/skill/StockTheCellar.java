package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.ShuffleTriggeredCard;
import thesimpleton.enums.AbstractCardEnum;

public class StockTheCellar extends CustomCard implements ShuffleTriggeredCard  {
  public static final String ID = "TheSimpletonMod:StockTheCellar";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/stockthecellar.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK_AMOUNT = 6;
  private static final int BLOCK_UPGRADE_AMOUNT = 0;
  private static final int BLOCK_PER_SHUFFLE_AMOUNT = 2;
  private static final int BLOCK_PER_SHUFFLE_UPGRADE_AMOUNT = 1;

  public StockTheCellar() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK_AMOUNT;
    this.baseMagicNumber = this.magicNumber = BLOCK_PER_SHUFFLE_AMOUNT;
    this.isEthereal = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
  }

  @Override
  public AbstractCard makeCopy() {
    return new StockTheCellar();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeBlock(BLOCK_UPGRADE_AMOUNT);
      upgradeMagicNumber(BLOCK_PER_SHUFFLE_UPGRADE_AMOUNT);
      initializeDescription();
    }
  }

  public void willBeShuffledTrigger() {
    this.flash(Color.BLUE);
    upgradeBlock(this.magicNumber);
    TheSimpletonMod.logger.debug(this.name + " upgraded block:" + this.block);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}