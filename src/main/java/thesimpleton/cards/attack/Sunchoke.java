package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.SunchokeAction;
import thesimpleton.enums.AbstractCardEnum;

public class Sunchoke extends CustomCard {
  public static final String ID = "TheSimpletonMod:Sunchoke";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/sunchoke.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 7;
  private static final int DAMAGE_UPGRADE = 4;
  private static final int BURNING_AMOUNT = 7;
  private static final int BURNING_AMOUNT_UPGRADE = 4;

  public Sunchoke() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = BURNING_AMOUNT;
    this.isMultiDamage = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
//    AbstractDungeon.actionManager.addToBottom(
//        new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
//            AbstractGameAction.AttackEffect.FIRE));

    AbstractDungeon.actionManager.addToBottom(
        new SunchokeAction(p, this.damageType, this.multiDamage, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Sunchoke();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeDamage(DAMAGE_UPGRADE);
      upgradeMagicNumber(BURNING_AMOUNT_UPGRADE);
      initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}