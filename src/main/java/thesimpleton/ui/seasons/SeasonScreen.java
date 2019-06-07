package thesimpleton.ui.seasons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.TintEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.ui.ReadyButtonPanel;
import thesimpleton.ui.buttons.ReadyButton;

import java.util.ArrayList;
import java.util.List;

// TODO: see ShopScreen.open for example screen display logic (set game state as well as initialize pieces)
//        use showBlockScreen

// TODO: animate fade-in

public class SeasonScreen implements ReadyButtonPanel  {
  private static final String ID = "SeasonScreen";
  private static final String BACKGROUND_IMG_PATH = getUiPath("season-screen-background");
  private static UIStrings uiStrings;

  public static String[] TEXT = {
      "The Season is",
      "The Following Crops are in Season:"
  };

  private float scale = 1.0F;

  private static final float CARD_OFFSET_X = -16.0F;

  private ReadyButton readyButton;

  private static final int width = 930;
  private static final int height = 706;

  private Texture backgroundImage = null;
  private final float backgroundImageY = Settings.HEIGHT;


  private boolean show = false;
  private boolean wasDismissed = false;

  private boolean showCancelButton = false;
  private String cancelButtonText = "";
  private static final float CROPS_IN_SEASON_TEXT_Y = 764.0F;

  private static final float CROP_CARDS_Y = 560.0F;

  private static final TintEffect textEffect = new TintEffect();

  private final List<AbstractCropPowerCard> inSeasonCropCards = new ArrayList<>();

  private static final Logger logger = TheSimpletonMod.logger;

  // TODO: narrow hitbox down to ready button
  private final Hitbox hb;

  // TODO: move this up
  private static String getUiPath(String id) {
    return "customui/seasonscreen/" + id + ".png";
  }

  public SeasonScreen() {
    hb = new Hitbox(width * Settings.scale, height * Settings.scale);

    textEffect.changeColor(new Color(0.9F, 0.9F, 0.9F, 1.0F));
    // TODO: fix NPE on game load

    // TODO:
    //  uiStrings = CardCrawlGame.languagePack.getUIString(TheSimpletonMod.makeID(ID));
    //  TEXT = uiStrings.TEXT;
  }

  public boolean isOpen() {
    return show;
  }

  public boolean wasDismissed() {
    return wasDismissed;
  }

  public void open() {
    logger.debug("SeasonScreen::open called");
    AbstractDungeon.isScreenUp = true;

    if (AbstractDungeon.overlayMenu != null) {
      CancelButton cancelButton = AbstractDungeon.overlayMenu.cancelButton;
      showCancelButton = cancelButton.isHidden;
      cancelButtonText = cancelButton.buttonText;
    }

    inSeasonCropCards.addAll(TheSimpletonMod.getSeasonalCropCards());
    positionCards(Settings.WIDTH / 2.0f, CROP_CARDS_Y);

    show = true;
    this.getReadyButton().enable();
    this.getReadyButton().show();

    for (final AbstractCropPowerCard card : this.inSeasonCropCards) {
      UnlockTracker.markCardAsSeen(card.cardID);
    }
//    // TODO: set isScreenUp to false on close
  }

  public void close() {
    logger.debug("SeasonScreen::close called");
    AbstractDungeon.overlayMenu.cancelButton.show(cancelButtonText);
    AbstractDungeon.dungeonMapScreen.open(true);
    wasDismissed = true;
    show = false;
  }

  public void update() {
    if (!isOpen()) {
      return;
    }

    hb.update();
    getReadyButton().update();
  }

  private Texture getBackgroundImage() {
    if (backgroundImage == null) {
      backgroundImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getResourcePath(BACKGROUND_IMG_PATH));
    }
    return backgroundImage;
  }

  private ReadyButton getReadyButton() {
    if (readyButton == null) {
      readyButton = makeReadyButton();
    }
    return readyButton;
  }

  private ReadyButton makeReadyButton() {
    // TODO: scale this appropriately
    final int READY_BUTTON_X = 810;
    final int READY_BUTTON_Y = 254;
    final String READY_BUTTON_IMG = "Ready";

    return new ReadyButton(READY_BUTTON_X, READY_BUTTON_Y, READY_BUTTON_IMG, this);
  }

  public void render(SpriteBatch sb) {
    if (!isOpen()) {
      return;
    }

    // TODO: scale this to screen
    sb.draw(getBackgroundImage(), 0.0F, backgroundImageY, Settings.WIDTH, Settings.HEIGHT);
    hb.render(sb);

    getReadyButton().render(sb);

    FontHelper.renderFontCentered(sb, FontHelper.bannerFont, TEXT[1], Settings.WIDTH / 2.0F,   CROPS_IN_SEASON_TEXT_Y * Settings.scale, textEffect.color, this.scale);

    for (final AbstractCard c : inSeasonCropCards) {
      c.render(sb);
    }
    for (final AbstractCard c : inSeasonCropCards) {
      c.renderCardTip(sb);
    }
  }

  private void positionCards(float x, float y) {
    // TODO: animation — currentx for all starts at x; approaches target x on each tick (fans out)

    inSeasonCropCards.get(0).target_x = Settings.WIDTH / 2.0f - AbstractCard.IMG_WIDTH - CARD_OFFSET_X;
    inSeasonCropCards.get(1).target_x = Settings.WIDTH / 2.0f;
    inSeasonCropCards.get(2).target_x = Settings.WIDTH / 2.0f + AbstractCard.IMG_WIDTH + CARD_OFFSET_X;
    inSeasonCropCards.get(0).target_y = y;
    inSeasonCropCards.get(1).target_y = y;
    inSeasonCropCards.get(2).target_y = y;

    for (final AbstractCard card : inSeasonCropCards) {
      card.drawScale = 0.75f;
      card.targetDrawScale = 0.75f;
      card.current_x = card.target_x;
      card.current_y = card.target_y;
    }
  }

  @Override
  public void onReadyClicked() {
    logger.debug("SeasonScreen.onReadyClicked Called");
    getReadyButton().disable();
    getReadyButton().hide();
    wasDismissed = true;
    this.close();
  }
}