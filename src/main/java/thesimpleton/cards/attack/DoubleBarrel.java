package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
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
import thesimpleton.actions.DoubleBarrelAction;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.BurningPower;

import java.util.Iterator;

public class DoubleBarrel extends CustomCard {
  public static final String ID = "TheSimpletonMod:DoubleBarrel";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/doublebarrel.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

  private static final AbstractGameAction.AttackEffect ATTACK_EFFECT = AttackEffect.BLUNT_HEAVY;

  private static final AttackEffect attackEffect = ATTACK_EFFECT;
  private static final int COST = 1;
  private static final int DAMAGE = 6;
  private static final int DAMAGE_UPGRADE = 3;
  private static final int ENERGY_AMOUNT = 1;

  public DoubleBarrel() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = ENERGY_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
        new DamageInfo(p, this.damage, this.damageTypeForTurn), this.attackEffect));

    AbstractDungeon.actionManager.addToBottom(
        new DoubleBarrelAction(p, m, this.attackEffect, this.damage, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new DoubleBarrel();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeDamage(DAMAGE_UPGRADE);
      initializeDescription();
    }
  }

  @Override
  public void triggerOnGlowCheck() {
    this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

    Iterator itr = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

    while(itr.hasNext()) {
      AbstractMonster m = (AbstractMonster)itr.next();
      if (!m.isDeadOrEscaped() && m.hasPower(WeakPower.POWER_ID)) {
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        break;
      }
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}