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
import thesimpleton.actions.RootDownAction;
import thesimpleton.enums.AbstractCardEnum;

public class RootDown extends CustomCard {
  public static final String ID = "TheSimpletonMod:RootDown";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/rootdown.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 0;
  private static final int DAMAGE = 6;
  private static final int PLANT_AMOUNT = 1;

  public RootDown() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.block = DAMAGE;
    this.baseMagicNumber = this.magicNumber = PLANT_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
            AbstractGameAction.AttackEffect.BLUNT_LIGHT));
     AbstractDungeon.actionManager.addToBottom(new RootDownAction(p, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new RootDown();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      this.isInnate = true;
      this.rawDescription = UPGRADE_DESCRIPTION;
      this.initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}