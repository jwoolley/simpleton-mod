package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.enums.AbstractCardEnum;

;
public class Rake extends CustomCard {
  public static final String ID = "TheSimpletonMod:Rake";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/rake.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 7;
  private static final int UPGRADE_DAMAGE_AMOUNT = 1;
  private static final int WEAK_AMOUNT = 1;
  private static final int UPGRADE_WEAK_AMOUNT = 1;


  public Rake() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,  AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = WEAK_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));

  }

  @Override
  public AbstractCard makeCopy() {
    return new Rake();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
      this.upgradeMagicNumber(UPGRADE_WEAK_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
