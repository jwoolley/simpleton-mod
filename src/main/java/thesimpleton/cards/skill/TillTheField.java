package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.AbstractCropPower;

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
  private static final int CROP_INCREASE_AMOUNT = 3;

  public TillTheField() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false, true), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CROP_INCREASE_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    if (AbstractCropPower.playerHasAnyActiveCropPowers()) {
      AbstractCropPower.getNewestCropPower().stackPower(this.magicNumber, true);
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new TillTheField();
  }

  public void updateDescription(boolean extendedDescription) {
    this.rawDescription = getDescription(extendedDescription, this.exhaust);
    this.initializeDescription();
  }

  private static String getDescription(boolean extendedDescription, boolean mustExhaust) {
    String description = DESCRIPTION;

    if (extendedDescription)
      if (AbstractCropPower.playerHasAnyActiveCropPowers()) {
        AbstractCropPower crop = AbstractCropPower.getNewestCropPower();
        description += EXTENDED_DESCRIPTION[3] + crop.name + EXTENDED_DESCRIPTION[4];
      } else {
        description += EXTENDED_DESCRIPTION[2];
      }

    description += (mustExhaust ? EXTENDED_DESCRIPTION[0] : "" ) + EXTENDED_DESCRIPTION[1];
    return description;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.exhaust = false;
      this.rawDescription = getDescription(false, false);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
