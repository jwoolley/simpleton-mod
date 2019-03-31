package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.enums.AbstractCardEnum;

public class CullingStrike extends CustomCard {
  public static final String ID = "TheSimpletonMod:CullingStrike";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/cullingstrike.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 7;
  private static final int UPGRADE_DAMAGE_AMOUNT = 2;

  public CullingStrike() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.isMultiDamage = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    int numTrigger = 1;

    if (SimpletonUtil.hasHarvestedThisTurn()) {
      numTrigger++;
    }

    AbstractDungeon.actionManager.addToBottom(new SFXAction("DAGGER_THROW_2"));

//    AbstractGameEffect effect = new WhirlwindEffect();
//    AbstractDungeon.actionManager.addToBottom(new VFXAction(p, effect, 0.50F));

    for (int i = 0; i < numTrigger; i++) {
      AbstractDungeon.actionManager.addToBottom(
          new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
              AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new CullingStrike();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
