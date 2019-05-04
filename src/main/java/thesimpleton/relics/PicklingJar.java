package thesimpleton.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.List;
import java.util.function.Predicate;

public class PicklingJar extends CustomRelic implements CustomBottleRelic, CustomSavable<String> {
  public static final String ID = "TheSimpletonMod:PicklingJar";
  public static final String IMG_PATH = "relics/picklingjar.png";
  public static final String IMG_PATH_LARGE = "relics/picklingjar_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/picklingjar_outline.png";

  private static final RelicTier TIER = RelicTier.UNCOMMON;
  private static final LandingSound SOUND = LandingSound.CLINK;

  private boolean cardSelected = true;

  private static final int NUM_SHUFFLES = 3;
  private static final int COST_FOR_TURN = 0;


  private AbstractCard card;

  public PicklingJar() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + NUM_SHUFFLES + this.DESCRIPTIONS[1];
  }

  @Override
  public void onEquip() {
    cardSelected = false;
    this.counter = 0;

    if (AbstractDungeon.isScreenUp) {
      AbstractDungeon.dynamicBanner.hide();
      AbstractDungeon.overlayMenu.cancelButton.hide();
      AbstractDungeon.previousScreen = AbstractDungeon.screen;
    }
    AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

    CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
        tmp.addToTop(c);
    }
    AbstractDungeon.gridSelectScreen.open(tmp, 1, DESCRIPTIONS[2] + name + ".",
        false, false, false, false);
  }

  @Override
  public String onSave() {
    // TODO: add upgraded prefix and parse on load
    return card.cardID;
  }

  public void onShuffle() {
    this.counter += 1;
    if (this.counter == 3) {
      this.counter = 0;
      flash();
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));

      AbstractCard cardCopy = card.makeCopy();
      if (cardCopy.cost > 0) {
        cardCopy.setCostForTurn(COST_FOR_TURN);
      }
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(cardCopy));
    }
  }

  @Override
  public void onLoad(String cardId) {
    List<AbstractCard> cards = CardLibrary.getAllCards();

    Logger logger = TheSimpletonMod.logger;
    for(AbstractCard card : cards) {
      if (card.cardID.equals(cardId)) {
        this.card = card;
      }
    }
    this.setDescriptionAfterLoading();
  }

  //TODO: update me
  private void setDescriptionAfterLoading() {
    description = DESCRIPTIONS[3] + NUM_SHUFFLES + this.DESCRIPTIONS[4] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[5];
    tips.clear();
    tips.add(new PowerTip(name, description));
    initializeTips();
  }

  @Override
  public AbstractRelic makeCopy() {
    return new PicklingJar();
  }

  public void update() {
    super.update();
    if ((!this.cardSelected) && (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty())) {
      this.cardSelected = true;
      this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
      AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
      AbstractDungeon.gridSelectScreen.selectedCards.clear();
      this.description = DESCRIPTIONS[3] + NUM_SHUFFLES + this.DESCRIPTIONS[4] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[5];
      this.tips.clear();
      this.tips.add(new PowerTip(this.name, this.description));
      initializeTips();
    }
  }

  @Override
  public Predicate<AbstractCard> isOnCard() {
    return card -> false;
  }
}