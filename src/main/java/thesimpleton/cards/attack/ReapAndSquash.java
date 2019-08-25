package thesimpleton.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
  private static final AttackEffect ATTACK_EFFECT = AttackEffect.NONE;

  private static final int BLOCK_AMOUNT = 4;
  private static final int UPDGRADE_BLOCK_AMOUNT = 2;


  public ReapAndSquash() {
    super(ID, NAME, DESCRIPTION, IMG_PATH, AbstractReapAndSowCard.COST, RARITY, CROP_ORB, ATTACK_EFFECT,
        AbstractReapAndSowCard.DAMAGE, AbstractReapAndSowCard.ATTACK_UPGRADE_BONUS,
        AbstractReapAndSowCard.PLANT_AMOUNT, AbstractReapAndSowCard.UPGRADE_PLANT_AMOUNT);

    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = PLANT_AMOUNT;
    this.isMultiDamage = true;
    this.baseBlock = this.block = BLOCK_AMOUNT;
  }

  @Override
  protected void applyAttackEffect() {
    AbstractPlayer player = AbstractDungeon.player;
    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_SPLAT_1"));
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, this.block));
  }

  @Override
  public AbstractCard makeCopy() {
    return new ReapAndSquash();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeBlock(UPDGRADE_BLOCK_AMOUNT);
    }
    super.upgrade();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}