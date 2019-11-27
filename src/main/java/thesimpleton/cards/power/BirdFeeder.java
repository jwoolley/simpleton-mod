package thesimpleton.cards.power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.BirdFeederPower;

public class BirdFeeder extends CustomCard {
  public static final String ID = "TheSimpletonMod:BirdFeeder";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/birdfeeder.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int NUM_STACKS = 3;
  private static final int NUM_STACKS_UPGRADE = 2;

  private static final int HP_PER_STACK = 4;

  private final int hpPerStack;

  public BirdFeeder() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(HP_PER_STACK), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = NUM_STACKS;
    this.hpPerStack = HP_PER_STACK;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new BirdFeederPower(p, p, this.magicNumber, this.hpPerStack), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new BirdFeeder();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_STACKS_UPGRADE);
    }
  }

  public static String getDescription(int hpPerStack) {
    return DESCRIPTION + hpPerStack + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}