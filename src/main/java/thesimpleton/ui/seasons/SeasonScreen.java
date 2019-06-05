package thesimpleton.ui.seasons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import javax.xml.soap.Text;

// TODO: see ShopScreen.open for example screen display logic (set game state as well as initialize pieces)
//        use showBlockScreen

public class SeasonScreen {
  private static final String ID = "SeasonScreen";
  private static final String BACKGROUND_IMG_PATH = getUiPath("season-screen-background");

  private static UIStrings uiStrings;
  public static String[] TEXT;

  private static final int width = 930;
  private static final int height = 706;

  private Texture backgroundImage = null;
  private final float backgroundImageY = Settings.HEIGHT;

  private boolean show = false;
  private boolean wasDismissed = false;

  private static final Logger logger = TheSimpletonMod.logger;

  // TODO: narrow hitbox down to ready button
  private final Hitbox hb;

  // TODO: move this up
  private static String getUiPath(String id) {
    return "customui/seasonscreen/" + id + ".png";
  }

  public SeasonScreen() {
    hb = new Hitbox(width * Settings.scale, height * Settings.scale);
    hb.move(Settings.WIDTH * .08f, Settings.HEIGHT * .08f );

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
    return;
//
//    logger.debug("SeasonScreen::open called");
//    AbstractDungeon.isScreenUp = true;
//    show = true;
//
//    // AbstractDungeon.overlayMenu.showBlackScreen(0.5f);
//    // TODO: set theme to this
//    // TODO: set isScreenUp to false on close
  }

  public void close() {
    wasDismissed = true;
    show = false;
  }

  public void update() {
//    logger.debug("SeasonScreen::update called");
    if (!isOpen()) {
//      logger.debug("SeasonScreen::update not open; returning");
      return;
    }

//    logger.debug("SeasonScreen::update open; calling hb.update");


    // handle ready button click /window dismissal here
    hb.update();
  }

  private Texture getBackgroundImage() {
    if (backgroundImage == null) {
      backgroundImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getResourcePath(BACKGROUND_IMG_PATH));
    }
    return backgroundImage;
  }

  public void render(SpriteBatch sb) {
//    logger.debug("SeasonScreen::render called");

    if (!isOpen()) {
//      logger.debug("SeasonScreen::render not open; returning");
      return;
    }

//    logger.debug("SeasonScreen::render drawing background image");

    sb.draw(getBackgroundImage(), 0.0F, backgroundImageY, Settings.WIDTH, Settings.HEIGHT);
    hb.render(sb);
  }
}