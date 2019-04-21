package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.PruningAction;
import thesimpleton.cards.HarvestCard;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.AbstractCropPower;

public class Pruning extends AbstractDynamicTextCard implements HarvestCard {
  public static final String ID = "TheSimpletonMod:Pruning";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/pruning.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;

  private static final int NUM_STACKS_TO_HARVEST = 1;
  private static final int NUM_STACKS_TO_GAIN = 2;
  private static final int NUM_STACKS_TO_GAIN_UPGRADE = 1;

  public final boolean harvestAll = false;
  public final boolean autoHarvest = false;

  public int numStacksToHarvest;

  public Pruning() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false), TYPE,  AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.numStacksToHarvest =  NUM_STACKS_TO_HARVEST;
    this.baseMagicNumber = this.magicNumber = NUM_STACKS_TO_GAIN;
    this.tags.add(TheSimpletonCardTags.HARVEST);
  }

  @Override
  public int getHarvestAmount() {
    return numStacksToHarvest;
  }

  @Override
  public boolean isHarvestAll() {
    return this.harvestAll;
  }

  @Override
  public boolean isAutoHarvest() {
    return this.autoHarvest;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new PruningAction(p, NUM_STACKS_TO_HARVEST, NUM_STACKS_TO_GAIN));
  }


  @Override
  public AbstractCard makeCopy() {
    return new Pruning();
  }

  public void updateDescription(boolean extendedDescription) {
    this.rawDescription = getDescription(extendedDescription);
    this.initializeDescription();
  }

  private static String getDescription(boolean extendedDescription) {
    String description = DESCRIPTION + NUM_STACKS_TO_HARVEST + EXTENDED_DESCRIPTION[0];

    if (extendedDescription) {
      if (AbstractCropPower.playerHasAnyActiveCropPowers()) {
        AbstractCropPower crop = AbstractCropPower.getOldestCropPower();
        TheSimpletonMod.logger.debug("Pruning::getDescription: using dynamic description. Crop: " + crop.name);
        description += EXTENDED_DESCRIPTION[2] +  crop.name + EXTENDED_DESCRIPTION[3];
      } else {
        TheSimpletonMod.logger.debug("Pruning::getDescription: using placeholder description");
        description += EXTENDED_DESCRIPTION[1];
      }
    }

    return description;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_STACKS_TO_GAIN_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
