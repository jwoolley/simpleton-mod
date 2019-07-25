package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.BuzzBombAction;
import thesimpleton.enums.AbstractCardEnum;

public class BuzzBomb extends CustomCard {
  public static final String ID = "TheSimpletonMod:BuzzBomb";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/buzzbomb.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 12;
  private static final int NUM_ATTACKS = 2;
  private static final int UPGRADE_NUM_ATTACKS_AMOUNT = 1;

  public BuzzBomb() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_ATTACKS;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_BUZZ_1"));

    if (!Settings.FAST_MODE) {
      AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5F));
    }

    AbstractDungeon.actionManager.addToBottom(new BuzzBombAction(
        p, AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
        this.damage, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new BuzzBomb();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(UPGRADE_NUM_ATTACKS_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
