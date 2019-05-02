package thesimpleton.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.WoodChipperAction;
import thesimpleton.powers.PlantPotatoPower;

import java.lang.reflect.Type;
import java.util.function.Predicate;

public class PicklingJar extends CustomRelic implements CustomBottleRelic, CustomSavable<Integer> {
  public static final String ID = "TheSimpletonMod:PicklingJar";
  public static final String IMG_PATH = "relics/picklingjar.png";
  public static final String IMG_PATH_LARGE = "relics/picklingjar_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/picklingjar_outline.png";

  private static final RelicTier TIER = RelicTier.RARE;
  private static final LandingSound SOUND = LandingSound.CLINK;

  private static final int MAX_NUM_CARDS = 3;
  private boolean cardSelected = true;
//  private boolean activated = false;

  private AbstractCard card;

  public PicklingJar() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  @Override
  public void onEquip() {
    cardSelected = false;
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
    AbstractDungeon.gridSelectScreen.open(tmp, 1, DESCRIPTIONS[1] + name + ".",
        false, false, false, false);
  }

  @Override
  public Integer onSave() {
    // Return the location of the card in your deck. AbstractCard cannot be serialized so we use an Integer instead.
    return AbstractDungeon.player.masterDeck.group.indexOf(card);
  }

  @Override
  public void onLoad(Integer cardIndex) {
    if (cardIndex == null) {
      return;
    }
    if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
      card = AbstractDungeon.player.masterDeck.group.get(cardIndex);
      if (card != null) {
        setDescriptionAfterLoading();
      }
    }
  }

  //TODO: update me
  private void setDescriptionAfterLoading() {
    description = FontHelper.colorString(card.name, "y") + DESCRIPTIONS[2];
    tips.clear();
    tips.add(new PowerTip(name, description));
    initializeTips();
  }

  @Override
  public AbstractRelic makeCopy() {
    return new PicklingJar();
  }

  @Override
  public Predicate<AbstractCard> isOnCard() {
    return null;
  }
}