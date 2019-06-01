package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.TillTheFieldAction;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.AbstractCropOrb;

public class TillTheField extends AbstractDynamicTextCard {
  public static final String ID = "TheSimpletonMod:TillTheField";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/tillthefield.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int CROP_AMOUNT = 2;
  private static final int UPGRADE_CROP_AMOUNT = 1;

  private static final int BLOCK_AMOUNT = 15;

  public TillTheField() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CROP_AMOUNT;
    this.baseBlock = this.block = BLOCK_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new TillTheFieldAction(this.magicNumber, this.block));
  }

  @Override
  public AbstractCard makeCopy() {
    return new TillTheField();
  }

  public void updateDescription(boolean extendedDescription) {
    this.rawDescription = getDescription(extendedDescription);
    this.initializeDescription();
  }

  private static String getDescription(boolean extendedDescription) {
    String description = DESCRIPTION;

    if (extendedDescription) {
      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        AbstractCropOrb newestCropOrb = AbstractCropOrb.getNewestCropOrb();
        description += EXTENDED_DESCRIPTION[2] + newestCropOrb.name + (newestCropOrb.isMature() ? EXTENDED_DESCRIPTION[3] : EXTENDED_DESCRIPTION[4]);
      } else {
        description += EXTENDED_DESCRIPTION[1];
      }
      description += EXTENDED_DESCRIPTION[0];
    }

    return description;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(UPGRADE_CROP_AMOUNT);
      this.initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
