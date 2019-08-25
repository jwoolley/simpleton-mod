package thesimpleton.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import thesimpleton.actions.BurnAllEnemiesAction;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.OnionCropOrb;

public class ReekAndSow extends AbstractReapAndSowCard {
  public static final String ID = "TheSimpletonMod:ReekAndSow";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String EXTENDED_DESCRIPTION[];
  public static final String IMG_PATH = "cards/reekandsow.png";
  private static final int DAMAGE = 4;
  private static final int PLANT_AMOUNT = 2;
  private static final int BURNING_AMOUNT = 4;
  private static final int UPGRADE_BURNING_AMOUNT = 2;

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
  private static  final AbstractCropOrb CROP_ORB = new OnionCropOrb();
  private static final AttackEffect ATTACK_EFFECT = AttackEffect.POISON;

  private int burningAmount;

  public ReekAndSow() {
    super(ID, NAME, getDescription(BURNING_AMOUNT), IMG_PATH, AbstractReapAndSowCard.COST, RARITY, CROP_ORB, ATTACK_EFFECT,
        DAMAGE, AbstractReapAndSowCard.ATTACK_UPGRADE_BONUS,
        PLANT_AMOUNT, AbstractReapAndSowCard.UPGRADE_PLANT_AMOUNT);

    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = PLANT_AMOUNT;
    this.isMultiDamage = true;

    this.burningAmount = BURNING_AMOUNT;
  }

  @Override
  protected void applyAttackEffect() {
    AbstractDungeon.actionManager.addToTop(new BurnAllEnemiesAction(this.getBurningAmount()));
  }

  private static String getDescription(int burningAmount) {
    return DESCRIPTION + burningAmount + EXTENDED_DESCRIPTION[0];
  }

  public String getUpdatedDescription() {
    return getDescription(this.getBurningAmount());
  }

  public int getBurningAmount() {
    return this.burningAmount;
  }

  public void upgradeBurningAmount(int upgradeAmount) {
    this.burningAmount += upgradeAmount;
  }

  @Override
  public AbstractCard makeCopy() {
    return new ReekAndSow();
  }

  public void upgrade() {
    if (!this.upgraded) {
      upgradeBurningAmount(UPGRADE_BURNING_AMOUNT);
    }
    super.upgrade();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}