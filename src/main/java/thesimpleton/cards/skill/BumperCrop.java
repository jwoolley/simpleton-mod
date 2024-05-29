package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.HarvestCropAction;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.interfaces.AbstractDynamicCropOrbHighlighterCard;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.AbstractCropOrb;

public class BumperCrop extends AbstractDynamicCropOrbHighlighterCard {
  public static final String ID = "TheSimpletonMod:BumperCrop";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/bumpercrop.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK = 7;
  private static final int BLOCK_UPGRADE_AMOUNT = 5;
  private static final int HARVEST_AMOUNT = 1;

  public BumperCrop() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(false),
        TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = HARVEST_AMOUNT;
    this.tags.add(TheSimpletonCardTags.HARVEST);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // TODO: ensure this works as expect with Asparagus
    if (AbstractCropOrb.playerHasAnyCropOrbs()) {
      AbstractCropOrb newestOrb = findCropOrb();
      AbstractDungeon.actionManager.addToTop(new HarvestCropAction(newestOrb,  this.magicNumber,true));
    }

    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
  }

  @Override
  public AbstractCard makeCopy() {
    return new BumperCrop();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_UPGRADE_AMOUNT);
    }
  }

  @Override
  public AbstractCropOrb findCropOrb() {
    return AbstractCropOrb.getNewestCropOrb();
  }

  public void updateDescription(boolean extendedDescription) {
    this.rawDescription = getDescription(extendedDescription);
    this.initializeDescription();
  }

  private static String getDescription(boolean extendedDescription) {
    String description = DESCRIPTION;

    if (extendedDescription)
      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        AbstractCropOrb cropOrb = AbstractCropOrb.getNewestCropOrb();
        description += EXTENDED_DESCRIPTION[2] + cropOrb.name + EXTENDED_DESCRIPTION[3];
      } else {
        description += EXTENDED_DESCRIPTION[1];
      }

    description += EXTENDED_DESCRIPTION[0];
    return description;
  }


  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
