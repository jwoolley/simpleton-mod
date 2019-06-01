package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CultivateAction;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.enums.AbstractCardEnum;

public class Cultivate extends CustomCard {
  public static final String ID = "TheSimpletonMod:Cultivate";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;

  public static final String IMG_PATH = "cards/cultivate.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.SKILL;
  private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
  private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK = 6;
  private static final int BLOCK_UPGRADE_BONUS = 3;
  private static final int HARVEST_AMOUNT = 3;
  private static final int PLANT_AMOUNT = 1;
  private static final int PLANT_AMOUNT_UPGRADE = 1;


  public Cultivate() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);

    this.baseBlock = this.block = BLOCK;
    exhaust = true;
    this.baseMagicNumber = this.magicNumber = PLANT_AMOUNT;
    this.tags.add(TheSimpletonCardTags.HARVEST);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    AbstractDungeon.actionManager.addToBottom(
        new CultivateAction(p, HARVEST_AMOUNT, this.magicNumber, true));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Cultivate();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeBlock(BLOCK_UPGRADE_BONUS);
      upgradeMagicNumber(PLANT_AMOUNT_UPGRADE);
    }
  }

  public static String getDescription() {
    return DESCRIPTION + HARVEST_AMOUNT + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}