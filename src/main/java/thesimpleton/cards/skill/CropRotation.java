package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropRotationAction;
import thesimpleton.cards.TheSimpletonCardTags;

public class CropRotation extends AbstractHarvestCard {
  public static final String ID = "TheSimpletonMod:CropRotation";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/croprotation.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.SKILL;
  private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
  private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 2;
  private static final int UPGRADED_COST = 1;

  private static final int NUM_CROPS = 1;

  public CropRotation() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH),
        COST, DESCRIPTION, TYPE, RARITY, TARGET, NUM_CROPS, false, false);
    this.baseMagicNumber = this.magicNumber = NUM_CROPS;
    this.tags.add(TheSimpletonCardTags.HARVEST);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToTop(new CropRotationAction(p, this.magicNumber, 1, false));
  }

  @Override
  public AbstractCard makeCopy() {
    return new CropRotation();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeBaseCost(UPGRADED_COST);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}