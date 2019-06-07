package thesimpleton.ui.seasons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.ui.ReadyButtonPanel;
import thesimpleton.ui.buttons.ReadyButton;

// TODO: see ShopScreen.open for example screen display logic (set game state as well as initialize pieces)
//        use showBlockScreen

public class SeasonScreen implements ReadyButtonPanel  {
  private static final String ID = "SeasonScreen";
  private static final String BACKGROUND_IMG_PATH = getUiPath("season-screen-background");
  private static UIStrings uiStrings;

  public static String[] TEXT;

  private ReadyButton readyButton;

  private static final int width = 930;
  private static final int height = 706;

  private Texture backgroundImage = null;
  private final float backgroundImageY = Settings.HEIGHT;

  private boolean show = false;
  private boolean wasDismissed = false;

  private static final Logger logger = TheSimpletonMod.logger;

  private boolean showCancelButton = false;
  private String cancelButtonText = "";

  // TODO: narrow hitbox down to ready button
  private final Hitbox hb;

  // TODO: move this up
  private static String getUiPath(String id) {
    return "customui/seasonscreen/" + id + ".png";
  }

  public SeasonScreen() {
    hb = new Hitbox(width * Settings.scale, height * Settings.scale);
//    hb.move(Settings.WIDTH * .08f, Settings.HEIGHT * .08f );

    // TODO: fix NPE on game load
//    uiStrings = CardCrawlGame.languagePack.getUIString(TheSimpletonMod.makeID(ID));
//    TEXT = uiStrings.TEXT;
  }

  public boolean isOpen() {
    return show;
  }

  public boolean wasDismissed() {
    return wasDismissed;
  }

  public void open() {
    // DEBUG

//
    logger.debug("SeasonScreen::open called");
    AbstractDungeon.isScreenUp = true;

    if (AbstractDungeon.overlayMenu != null) {
      CancelButton cancelButton = AbstractDungeon.overlayMenu.cancelButton;
      showCancelButton = cancelButton.isHidden;
      cancelButtonText = cancelButton.buttonText;
//        cancelButton.hide();
//        AbstractDungeon.overlayMenu.hideBlackScreen();
//        show = true;
    }

//   AbstractDungeon.dungeonMapScreen.map.hideInstantly();
    show = true;
    this.getReadyButton().enable();
    this.getReadyButton().show();

//    // TODO: set theme to this
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
//    logger.debug("SeasonScreen::update called");
    if (!isOpen()) {
      return;
    }

//    logger.debug("SeasonScreen::update open; calling hb.update");

    // handle ready button click /window dismissal here
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
    final int READY_BUTTON_X = 810;
    final int READY_BUTTON_Y = 254;
    final String READY_BUTTON_IMG = "Ready";

    return new ReadyButton(READY_BUTTON_X, READY_BUTTON_Y, READY_BUTTON_IMG, this);
  }

  public void render(SpriteBatch sb) {
//    logger.debug("SeasonScreen::render called");

    if (!isOpen()) {
      return;
    }

//    AbstractDungeon.dungeonMapScreen.map.hide();
//        AbstractDungeon.dungeonMapScreen.map.targetAlpha = .99f;
//    logger.debug("#@#@#@#@#@#@#@## SeasonScreen.render Current Screen: " + AbstractDungeon.screen);

//    logger.debug("SeasonScreen::render drawing background image");
//    AbstractDungeon.overlayMenu.hideBlackScreen();
    // TODO: scale this to screen
    sb.draw(getBackgroundImage(), 0.0F, backgroundImageY, Settings.WIDTH, Settings.HEIGHT);
    hb.render(sb);

    getReadyButton().render(sb);

//    if ((this.hb.clicked) || ((this.hb.hovered) && (CInputActionSet.select.isJustPressed()))) {
//      this.close();
//      return;
//    }
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