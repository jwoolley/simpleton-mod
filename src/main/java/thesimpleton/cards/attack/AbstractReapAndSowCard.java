package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
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
import thesimpleton.crops.Crop;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.AbstractCropOrb;

public abstract class AbstractReapAndSowCard extends CustomCard {
  public static final String ID = "TheSimpletonMod:ReapAndSow";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/reapandsow.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardRarity RARITY = CardRarity.BASIC;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;

  protected static final int COST = 1;
  protected static final int DAMAGE = 4;
  protected static final int ATTACK_UPGRADE_BONUS = 2;
  protected static final int PLANT_AMOUNT = 1;
  protected static final int UPGRADE_PLANT_AMOUNT = 1;

  private final int upgradeDamage;
  private final int upgradePlantAmount;
  private final Crop cropToPlant;
  private final AttackEffect attackEffect;

  public AbstractReapAndSowCard(String id, String name, String imgPath, int cost, Crop cropToPlant,
                                AttackEffect attackEffect, int damage, int upgradeDamage,
                                int plantAmount, int upgradePlantAmount) {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), cost, getDescription(cropToPlant), TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);

    this.baseDamage = this.damage = damage;
    this.baseMagicNumber = this.magicNumber = plantAmount;
    this.upgradeDamage = upgradeDamage;
    this.upgradePlantAmount = upgradePlantAmount;
    this.cropToPlant = cropToPlant;
    this.attackEffect = attackEffect;
    this.isMultiDamage = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, attackEffect));

    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(AbstractCropOrb.getCropOrb(this.cropToPlant),
        getPlantAmount(), true));

    AbstractDungeon.actionManager.addToBottom(
        new ReapAndSowThresholdAction(false)); // disabling upgrade Cultivate for now
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeDamage(this.upgradeDamage);
      upgradeMagicNumber(this.upgradePlantAmount);
        this.rawDescription = getDescription(this.cropToPlant);
        initializeDescription();
    }
  }

  abstract protected int getPlantAmount();
  public abstract AbstractCard makeCopy();

  public static String getDescription(Crop crop) {
    return DESCRIPTION + crop.getName() + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}