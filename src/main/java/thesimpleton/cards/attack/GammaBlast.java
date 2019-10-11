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
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.AbstractCropOrb;

public class GammaBlast extends CustomCard {
  public static final String ID = "TheSimpletonMod:GammaBlast";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/gammablast.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 20;
  private static final int UPGRADE_DAMAGE_AMOUNT = 5;
  private static final int CROP_INCREASE_AMOUNT = 2;

  public GammaBlast() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = CROP_INCREASE_AMOUNT;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new SFXAction("ENERGY_BLAST_1"));

    AbstractGameEffect effect = new MindblastEffect(p.hb.cX, p.hb.cY, false);

    AbstractGameEffect effect2 = new MindblastEffect(p.hb.cX, p.hb.cY, true);
    AbstractDungeon.actionManager.addToBottom(new VFXAction(p, effect, 0.2F));
    AbstractDungeon.actionManager.addToBottom(new VFXAction(p, effect2, 0.1F));

    AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

    AbstractCropOrb.getActiveCropOrbs()
        .forEach(orb -> orb.getCrop().stackOrb(this.magicNumber, true));
  }

  @Override
  public AbstractCard makeCopy() {
    return new GammaBlast();
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
