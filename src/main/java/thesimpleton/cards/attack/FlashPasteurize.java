package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.DamageAllCharactersAction;
import thesimpleton.enums.AbstractCardEnum;

public class FlashPasteurize extends CustomCard {
  public static final String ID = "TheSimpletonMod:FlashPasteurize";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/flashpasteurize.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 0;
  private static final int DAMAGE = 7;
  private static final int BLOCK = 7;

  public FlashPasteurize() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseBlock = this.block = BLOCK;
    this.isMultiDamage = true;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAllCharactersAction(p, this.multiDamage, this.damageTypeForTurn,
            AbstractGameAction.AttackEffect.FIRE));

    AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(AbstractDungeon.player));

    if (this.upgraded) {
      AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new FlashPasteurize();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      this.rawDescription = UPGRADE_DESCRIPTION;
      initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}