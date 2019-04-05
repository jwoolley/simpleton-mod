package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.SoilSampleAction;
import thesimpleton.enums.AbstractCardEnum;

public class SoilSample extends CustomCard {
  public static final String ID = "TheSimpletonMod:SoilSample";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String  UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/soilsample.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int UPGRADED_COST = 0;
  private static final int NUM_CARDS_CHOSEN = 3;
  private static final int NUM_CARDS_DRAWN = 1;

  public SoilSample() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.exhaust = true;
    this.baseMagicNumber = this.magicNumber = NUM_CARDS_CHOSEN;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new SoilSampleAction(this.magicNumber, NUM_CARDS_DRAWN));
  }

  @Override
  public AbstractCard makeCopy() {
    return new SoilSample();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.updateCost(UPGRADED_COST);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}