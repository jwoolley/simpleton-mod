package thesimpleton.cards.power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.VolatileFumesPower;

public class VolatileFumes extends CustomCard {
  public static final String ID = "TheSimpletonMod:VolatileFumes";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/volatilefumes.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int BURNING_AMOUNT = 4;
  private static final int BURNING_UPGRADE_AMOUNT = 2;

  public VolatileFumes() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = BURNING_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new VolatileFumesPower(p, p, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new VolatileFumes();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(BURNING_UPGRADE_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}