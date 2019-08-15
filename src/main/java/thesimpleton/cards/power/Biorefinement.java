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
import thesimpleton.powers.BiorefinementPower;

public class Biorefinement extends CustomCard {
  public static final String ID = "TheSimpletonMod:Biorefinement";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/biorefinement.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 2;
  private static final int UPGRADED_COST = 1;

  public Biorefinement() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    TheSimpletonMod.logger.debug("TheSimpletonMod:Biorefinement: use called");

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new BiorefinementPower(1), 1));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Biorefinement();
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
  }
}