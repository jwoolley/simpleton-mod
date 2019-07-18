package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonCardHelper;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.status.Depletion;
import thesimpleton.enums.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Innovate extends CustomCard {
  public static final String ID = "TheSimpletonMod:Innovate";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;

  public static final String IMG_PATH = "cards/innovate.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int CARD_AMOUNT = 1;
  private static final int UPGRADE_CARD_AMOUNT = 1;
  private static final int COST_FOR_TURN = 0;

  public Innovate() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(CARD_AMOUNT, COST_FOR_TURN),
        TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CARD_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {

    List<AbstractCard> cardPool = SimpletonCardHelper.getHarvestCards();

    List<AbstractCard> cardsToAdd = new ArrayList<>();
    for (int i = 0; i < this.magicNumber; i++) {
      Collections.shuffle(cardPool);
      AbstractCard cardToAdd = cardPool.get(0).makeCopy();
      cardToAdd.costForTurn = COST_FOR_TURN;
      cardsToAdd.add(cardToAdd);
    }

    for (AbstractCard card : cardsToAdd) {
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Innovate();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(UPGRADE_CARD_AMOUNT);
      this.rawDescription = getDescription(this.magicNumber, COST_FOR_TURN);
      this.initializeDescription();
    }
  }

  private static String getDescription(int cardAmount, int costForTurn) {
    return (cardAmount == 1 ? DESCRIPTION : EXTENDED_DESCRIPTION[0]) + costForTurn + EXTENDED_DESCRIPTION[1];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}