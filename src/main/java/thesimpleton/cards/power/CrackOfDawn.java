package thesimpleton.cards.power;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.CrackOfDawnPower;

public class CrackOfDawn extends CustomCard {
  public static final String ID = "TheSimpletonMod:CrackOfDawn";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/crackofdawn.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 2;
  private static final int UPGRADED_COST = 1;
  private static final int TURNS_TO_WAIT = 2;

  public CrackOfDawn() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = CrackOfDawnPower.DAMAGE_AMOUNT_PER_STACK;
    this.baseMagicNumber = this.magicNumber = TURNS_TO_WAIT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    TheSimpletonMod.logger.debug("TheSimpletonMod:CrackOfDawn: use called");

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new CrackOfDawnPower(AbstractDungeon.player, TURNS_TO_WAIT)));
  }

  @Override
  public AbstractCard makeCopy() {
    return new CrackOfDawn();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBaseCost(UPGRADED_COST);
    }
  }


  private static String getDescription() {
    return DESCRIPTION  + CrackOfDawnPower.CROP_AMOUNT_PER_STACK + EXTENDED_DESCRIPTION[0]
        + StringUtils.repeat(EXTENDED_DESCRIPTION[1], CrackOfDawnPower.ENERGY_AMOUNT_PER_STACK)
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}