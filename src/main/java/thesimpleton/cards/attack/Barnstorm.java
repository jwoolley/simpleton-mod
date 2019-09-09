package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.BarnstormAction;
import thesimpleton.enums.AbstractCardEnum;

public class Barnstorm extends CustomCard {
  public static final String ID = "TheSimpletonMod:Barnstorm";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/barnstorm.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 4;
  private static final int UPGRADED_COST = 3;
  private static final int DAMAGE = 5;
  private static final int STUN_THRESHOLD = 5;

  public Barnstorm() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = STUN_THRESHOLD;
//    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new BarnstormAction(
        p, AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
        this.damage, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Barnstorm();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBaseCost(UPGRADED_COST);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}
