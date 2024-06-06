package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ModifyCostAction;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.BurningPower;
import thesimpleton.utilities.SimpletonColorUtil;

import java.util.Iterator;

public class KeenEdge extends CustomCard {
  public static final String ID = "TheSimpletonMod:KeenEdge";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/keenedge.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 9;
  private static final int COST_INCREASE = 1;
  private static final int DAMAGE_INCREASE = 9;

  private static final int UPGRADE_DAMAGE = 3;

  public KeenEdge() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = DAMAGE_INCREASE;
    this.isEthereal = true;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractGameEffect effect = new FlashAtkImgEffect(m.hb.cX, m.hb.cY,
        AbstractGameAction.AttackEffect.SLASH_DIAGONAL, true);
    AbstractDungeon.effectList.add(effect);
    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_SCIMITAR_1"));

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));

    this.updateOnUse();
  }

  private void updateOnUse() {
    AbstractDungeon.actionManager.addToBottom(new ModifyDamageAction(this.uuid, this.magicNumber));

    if (shouldIncreaseCost()) {
      this.superFlash();
      AbstractDungeon.actionManager.addToBottom(new ModifyCostAction(this, COST_INCREASE));
    }
  }

  @Override
  public void triggerOnGlowCheck() {
    if (AbstractDungeon.player.hand.contains(this)) {
      if (AbstractDungeon.player.hand.contains(this) && !shouldIncreaseCost()) {
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
      } else {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
      }
    }
  }

  private boolean shouldIncreaseCost() {
    Iterator itr = AbstractDungeon.player.hand.group.iterator();

    while(itr.hasNext()) {
      AbstractCard card = (AbstractCard)itr.next();
      if (card.type == CardType.SKILL) {
        return true;
      }
    }
    return false;
  }

  @Override
  public AbstractCard makeCopy() {
    return new KeenEdge();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(UPGRADE_DAMAGE);
      this.upgradeMagicNumber(UPGRADE_DAMAGE);
      this.rawDescription = getDescription(true);
      initializeDescription();
    }
  }

  private static String getDescription(boolean isUpgraded) {
    return DESCRIPTION + COST_INCREASE  + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}