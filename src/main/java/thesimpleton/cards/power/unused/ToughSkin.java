package thesimpleton.cards.power.unused;

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
import thesimpleton.powers.unused.ToughSkinPower;

public class ToughSkin extends CustomCard {
  public static final String ID = "TheSimpletonMod:ToughSkin";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/toughskin.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK_AMOUNT = 4;
  private static final int BLOCK_UPGRADE_AMOUNT = 1;

  public ToughSkin() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = BLOCK_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new ToughSkinPower(p, p, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new ToughSkin();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(BLOCK_UPGRADE_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}