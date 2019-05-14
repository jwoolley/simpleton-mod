package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.TheSimpletonCardTags;

public class GiantTurnip extends CustomCard {
  public static final String ID = "TheSimpletonMod:GiantTurnip";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/giantturnip.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 0;
  private static final int DAMAGE = 3;
  private static final int UPGRADE_DAMAGE_AMOUNT = 1;

  public GiantTurnip() {
    this(DAMAGE);
  }
  
  public GiantTurnip(int baseNumber) {
    super(ID, baseNumber + "-Lb. " + NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = baseNumber;
    this.baseDamage = this.damage = baseNumber * baseNumber;
    this.exhaust = true;
    this.tags.add(TheSimpletonCardTags.CROP);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    final int totalDamage = this.damage;
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, totalDamage, this.damageTypeForTurn),
          AbstractGameAction.AttackEffect.BLUNT_HEAVY));
  }

  @Override
  public AbstractCard makeCopy() {
    return new GiantTurnip(this.damage);
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT * this.magicNumber);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
