package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.TheSimpletonCardTags;

public class Harvest extends AbstractHarvestCard {
  public static final String ID = "TheSimpletonMod:Harvest";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/harvest.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.SKILL;
  private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
  private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int UPGRADED_COST = 0;
  private static final int BLOCK = 4;

  private static final int HARVEST_AMOUNT = -1;
  private static final int HARVEST_BONUS = 0;
  private static final boolean HARVEST_ALL = true;

  public Harvest() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, TYPE, DESCRIPTION, RARITY, TARGET, HARVEST_AMOUNT, HARVEST_ALL, true);
    this.baseBlock = this.block = BLOCK;
    exhaust = true;
    this.baseMagicNumber = this.magicNumber = HARVEST_AMOUNT;
    this.tags.add(TheSimpletonCardTags.HARVEST);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Harvest();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeBaseCost(UPGRADED_COST);
      upgradeMagicNumber(HARVEST_BONUS);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}