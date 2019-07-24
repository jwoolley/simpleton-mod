package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.AdrenalineEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.TakeHeartAction;
import thesimpleton.enums.AbstractCardEnum;

public class TakeHeart extends CustomCard {
  public static final String ID = "TheSimpletonMod:TakeHeart";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/takeheart.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 3;
  private static final int UPGRADED_COST = 2;

  private static final int BLOCK = 16;
  private static final int HEAL_AMOUNT = 5;
  private static final int ARTICHOKE_PLANT_AMOUNT = 2;

  private static final int HEALTH_THRESHOLD_PERCENTAGE = 50;

  public TakeHeart() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = HEAL_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    final int numRepetitions = (((float) p.currentHealth / p.maxHealth) * 100 > HEALTH_THRESHOLD_PERCENTAGE) ? 1 : 2;

//    AbstractDungeon.actionManager.addToBottom(new VFXAction(new AdrenalineEffect()));

    AbstractDungeon.actionManager.addToBottom(
        new TakeHeartAction(p, this.block, this.magicNumber, ARTICHOKE_PLANT_AMOUNT, numRepetitions));
  }

  @Override
  public AbstractCard makeCopy() {
    return new TakeHeart();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeBaseCost(UPGRADED_COST);
    }
  }

  private static String getDescription() {
    return DESCRIPTION + ARTICHOKE_PLANT_AMOUNT + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}