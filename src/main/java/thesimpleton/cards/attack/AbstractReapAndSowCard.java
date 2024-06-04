package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.actions.ReapAndSowThresholdAction;
import thesimpleton.cards.skill.Cultivate;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.AbstractCropOrb;

public abstract class AbstractReapAndSowCard extends CustomCard {
  public static final String ID = "TheSimpletonMod:AbstractReapAndSowCard";
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;

  protected static final int COST = 1;
  protected static final int DAMAGE = 4;
  protected static final int ATTACK_UPGRADE_BONUS = 3;
  protected static final int PLANT_AMOUNT = 1;
  protected static final int UPGRADE_PLANT_AMOUNT = 1;

  private static final AbstractCard PREVIEW_CARD;

  private final int upgradeDamage;
  private final int upgradePlantAmount;
  private final AbstractCropOrb cropOrbToPlant;
  private final AttackEffect attackEffect;

  public AbstractReapAndSowCard(String id, String name, String description, String imgPath, int cost, CardRarity rarity,
                                AbstractCropOrb cropOrbToPlant,
                                AttackEffect attackEffect, int damage, int upgradeDamage,
                                int plantAmount, int upgradePlantAmount) {
    super(id, name, TheSimpletonMod.getImageResourcePath(imgPath), cost, description,
        TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, rarity, TARGET);

    this.baseDamage = this.damage = damage;
    this.baseMagicNumber = this.magicNumber = plantAmount;
    this.upgradeDamage = upgradeDamage;
    this.upgradePlantAmount = upgradePlantAmount;
    this.cropOrbToPlant = cropOrbToPlant;
    this.attackEffect = attackEffect;
    this.isMultiDamage = true;
    this.cardsToPreview = PREVIEW_CARD;
  }

  protected void applyAttackEffect() {  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    TheSimpletonMod.debugLogger.log(this.name + ".use() called. damage: " + this.damage);

    applyAttackEffect();

    AbstractDungeon.actionManager.addToBottom(
            new DamageAllEnemiesAction(p,  this.multiDamage, this.damageTypeForTurn, attackEffect));

    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(this.cropOrbToPlant, this.magicNumber, true));

    AbstractDungeon.actionManager.addToBottom(
        new ReapAndSowThresholdAction(false)); // disabling upgrade Cultivate for now
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeDamage(this.upgradeDamage);
      upgradeMagicNumber(this.upgradePlantAmount);
      this.rawDescription = getUpdatedDescription();
      initializeDescription();
    }
  }

  public String getUpdatedDescription() {
    return this.rawDescription;
  }

  public abstract AbstractCard makeCopy();

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    PREVIEW_CARD = new Cultivate();
  }
}