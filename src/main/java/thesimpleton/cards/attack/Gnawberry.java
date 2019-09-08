package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.TheSimpletonCardTags;

public class Gnawberry extends CustomCard {
  public static final String ID = "TheSimpletonMod:Gnawberry";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/gnawberry.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 11;
  private static final int UPGRADE_DAMAGE_AMOUNT = 4;
  private static final int HEAL_AMOUNT = 2;
  private static final int UPGRADE_HEAL_AMOUNT = 1;

  public Gnawberry() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY,
        TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = HEAL_AMOUNT;
    this.exhaust = true;
    this.tags.add(TheSimpletonCardTags.CROP);
    this.tags.add(AbstractCard.CardTags.HEALING);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (m != null) {
      AbstractDungeon.actionManager.addToBottom(new VFXAction(
          new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Color.SCARLET.cpy()), 0.1F));
    }

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
            AbstractGameAction.AttackEffect.NONE));

    AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Gnawberry();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
      this.upgradeMagicNumber(UPGRADE_HEAL_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}