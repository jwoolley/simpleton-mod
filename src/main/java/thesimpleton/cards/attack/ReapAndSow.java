package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.actions.ReapAndSowThresholdAction;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.PotatoCropOrb;

public class ReapAndSow extends CustomCard {
  public static final String ID = "TheSimpletonMod:ReapAndSow";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/reapandsow.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 4;
  private static final int ATTACK_UPGRADE_BONUS = 2;
  private static final int PLANT_AMOUNT = 1;
  private static final int UPGRADE_PLANT_AMOUNT = 1;

  public ReapAndSow() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = PLANT_AMOUNT;
    this.isMultiDamage = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));

    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new PotatoCropOrb(), this.magicNumber, true));

    AbstractDungeon.actionManager.addToBottom(
        new ReapAndSowThresholdAction(false)); // disabling upgrade Cultivate for now
  }

  @Override
  public AbstractCard makeCopy() {
    return new ReapAndSow();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeDamage(ATTACK_UPGRADE_BONUS);
      upgradeMagicNumber(UPGRADE_PLANT_AMOUNT);
      //  this.rawDescription = UPGRADE_DESCRIPTION;
      //  initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}