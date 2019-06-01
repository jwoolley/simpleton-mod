package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.FanTheFlamesAction;
import thesimpleton.enums.AbstractCardEnum;

public class FanTheFlames extends CustomCard {
  public static final String ID = "TheSimpletonMod:FanTheFlames";
  public static final String NAME;
  public static final String DESCRIPTION;

  public static final String IMG_PATH = "cards/fantheflames.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int BURNING = 5;
  private static final int BURNING_UPGRADE = 4;

  public FanTheFlames() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = BURNING;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new FanTheFlamesAction(m, p, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new FanTheFlames();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(BURNING_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}