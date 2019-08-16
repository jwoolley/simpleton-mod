package thesimpleton.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.SquashCropOrb;

public class ReapAndSquash extends AbstractReapAndSowCard {
  public static final String ID = "TheSimpletonMod:ReapAndSquash";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/reapandsquash.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
  private static  final AbstractCropOrb CROP_ORB = new SquashCropOrb();
  private static final AttackEffect ATTACK_EFFECT = AttackEffect.SHIELD;

  public ReapAndSquash() {
    super(ID, NAME, IMG_PATH, AbstractReapAndSowCard.COST, RARITY, CROP_ORB, ATTACK_EFFECT,
        AbstractReapAndSowCard.DAMAGE, AbstractReapAndSowCard.ATTACK_UPGRADE_BONUS,
        AbstractReapAndSowCard.PLANT_AMOUNT, AbstractReapAndSowCard.UPGRADE_PLANT_AMOUNT);

    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = PLANT_AMOUNT;
    this.isMultiDamage = true;
  }

  @Override
  public AbstractCard makeCopy() {
    return new ReapAndSquash();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}