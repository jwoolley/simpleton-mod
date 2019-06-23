package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.status.Depletion;
import thesimpleton.enums.AbstractCardEnum;

import java.util.List;

public class Polyculture extends CustomCard {
  public static final String ID = "TheSimpletonMod:Polyculture";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/polyculture.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int CARD_AMOUNT = 2;
  private static final int UPGRADE_CARD_AMOUNT = 1;

  public Polyculture() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CARD_AMOUNT;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    List<AbstractCropPowerCard> cardsToAdd =
        AbstractCropPowerCard.getRandomCropPowerCards(this.magicNumber, false);

    for (AbstractCropPowerCard card : cardsToAdd) {
      AbstractDungeon.actionManager.addToBottom(
          new MakeTempCardInDrawPileAction(card, 1, true, false));
    }

    AbstractDungeon.actionManager.addToBottom(new ShuffleAction(p.drawPile, true));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Polyculture();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(UPGRADE_CARD_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}