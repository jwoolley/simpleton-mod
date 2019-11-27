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
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.enums.AbstractCardEnum;

;
public class SlashAndBurn extends CustomCard {
  public static final String ID = "TheSimpletonMod:SlashAndBurn";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/slashandburn.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 7;
  private static final int BURN_AMOUNT = 7;

  private static final int UPGRADE_DAMAGE_AMOUNT = 3;
  private static final int UPGRADE_BURN_AMOUNT = 3;


  public SlashAndBurn() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,  AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = BURN_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
            AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

    AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(m, p, this.magicNumber));

  }

  @Override
  public AbstractCard makeCopy() {
    return new SlashAndBurn();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
      this.upgradeMagicNumber(UPGRADE_BURN_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
