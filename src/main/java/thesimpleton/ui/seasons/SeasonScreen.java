package thesimpleton.ui.seasons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.TintEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.seasons.Season;
import thesimpleton.ui.ReadyButtonPanel;
import thesimpleton.ui.SettingsHelper;
import thesimpleton.ui.buttons.ReadyButton;
import thesimpleton.utilities.ModLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// TODO: see ShopScreen.open for example screen display logic (set game state as well as initialize pieces)
//        use showBlockScreen

// TODO: animate fade-in

public class SeasonScreen implements ReadyButtonPanel  {
  private static final String UI_NAME = TheSimpletonMod.makeID("SeasonScreen");
//  private static final String BACKGROUND_IMG_PATH = getUiPath("season-screen-background");

  private UIStrings uiStrings;

  private static final float CARD_GROUP_OFFSET_X = 18.F;
  private static final float CARD_GROUP_OFFSET_Y = 0.0F;
  private static final float CARD_OFFSET_X = -30.0F;
  private static final float CROP_TEXT_OFFSET_X = 16.0F;

  private final static float SEASON_DESCRIPTION_PREAMBLE_Y = 952;
  private final static float SEASON_DESCRIPTION_NAME_Y = 895;

  private ReadyButton readyButton;

  private static final int width = 930;
  private static final int height = 706;

  private Texture backgroundImage = null;
  private final float backgroundImageY = Settings.HEIGHT;


  private boolean show = false;
  private boolean wasDismissed = false;

  private final boolean SHOULD_SHOW_CANCEL_BUTTON = false;
  private boolean showCancelButtonOnClose = false;
  private String cancelButtonText = "";
  private static final float CROPS_IN_SEASON_TEXT_Y = 744.0F;
  private static final float CROP_CARDS_Y = 485.0F;

  private static final float TOOLTIP_X = 1253.0F;
  private static final float TOOLTIP_Y = 732.0F;

  private static final TintEffect textEffect = new TintEffect();

  private final List<AbstractCropPowerCard> inSeasonCropCards = new ArrayList<>();
  private SeasonTooltip tooltip;

  private static ModLogger logger = TheSimpletonMod.traceLogger;

  // TODO: narrow hitbox down to ready button
  private final Hitbox hb;

  // TODO: move this up
  private static String getUiPath(String id) {
    return "customui/seasonscreen/" + id + ".png";
  }

  private float textYScale = 1.0F;

  private static String getSeasonImagePath(Season season) {
    switch(season) {
      case WINTER:
        return getUiPath("season-screen-background-winter");
      case SPRING:
        return getUiPath("season-screen-background-spring");
      case SUMMER:
        return getUiPath("season-screen-background-summer");
      case AUTUMN:
      default:
        return getUiPath("season-screen-background-autumn");
    }
  }

  private static class SeasonTooltip {

    public static int TOOLTIP_WIDTH = 48;
    public static int TOOLTIP_HEIGHT = 48;

    public static int TOOLTIP_X_OFFSET = 22;
    public static int TOOLTIP_Y_OFFSET = 0;

    public static Color TOOLTIP_INACTIVE_COLOR = new Color(0.9F, 0.9F, 0.9F, 0.9F);
    public static Color TOOLTIP_ACTIVE_COLOR = Color.WHITE.cpy();

    private static String getTooltipImagePath(Season season) {
      switch(season) {
        case WINTER:
          return getUiPath("season-tooltip-winter");
        case SPRING:
          return getUiPath("season-tooltip-spring");
        case SUMMER:
          return getUiPath("season-tooltip-summer");
        case AUTUMN:
        default:
          return getUiPath("season-tooltip-autumn");
      }
    }

    public SeasonTooltip(Season season, float x, float y) {
      this.image = TheSimpletonMod.loadTexture(
          TheSimpletonMod.getImageResourcePath(getTooltipImagePath(season)));

      this.hb = new Hitbox(TOOLTIP_WIDTH * SettingsHelper.getScaleX(),
          TOOLTIP_HEIGHT * SettingsHelper.getScaleY());

      position(x, y);
    }

    final static ArrayList<PowerTip> tooltip;
    static {
      UIStrings tooltipText = CardCrawlGame.languagePack.getUIString("TheSimpletonMod:SeasonScreenTooltip");
      tooltip = new ArrayList<>(Collections.singletonList(
          new PowerTip(tooltipText.TEXT[0], tooltipText.TEXT[1])));
    }

    void update() {
      this.hb.update();
    }

    void render(SpriteBatch sb) {
      final Color originalColor = sb.getColor();

      if(this.hb.hovered) {
        sb.setColor(TOOLTIP_ACTIVE_COLOR);
        TipHelper.queuePowerTips(
            this.hb.x + (TOOLTIP_WIDTH + TOOLTIP_X_OFFSET) * SettingsHelper.getScaleX(),
            this.hb.y + (TOOLTIP_HEIGHT + TOOLTIP_Y_OFFSET) * SettingsHelper.getScaleY(), tooltip);
      } else {
        sb.setColor(TOOLTIP_INACTIVE_COLOR);
      }

      sb.draw(this.image, this.hb.x, this.hb.y, TOOLTIP_WIDTH * SettingsHelper.getScaleX(),
          TOOLTIP_HEIGHT * SettingsHelper.getScaleY());

      sb.setColor(originalColor);
    }

    private void position(float x, float y) {
      this.hb.x = x;
      this.hb.y = y;
    }

    private final Texture image;
    private final Hitbox hb;
  }

  public SeasonScreen() {

    logger.trace("SeasonScreen::constructor Settings.scale: "
        + Settings.scale + ", Settings.HEIGHT: " + Settings.HEIGHT + ", SCALE_X: " + SettingsHelper.getScaleX() +", SettingsHelper.getScaleY(): " + SettingsHelper.getScaleY());

    hb = new Hitbox(width * SettingsHelper.getScaleX(), height * SettingsHelper.getScaleY());

  }

  public float getTextYScale() {
    return 0.9F * ((float) Settings.HEIGHT) / 1080.0F;
//    return SettingsHelper.getScaleY();
  }

  public boolean isOpen() {
    return show;
  }

  public boolean wasDismissed() {
    return wasDismissed;
  }

  public void resetDismissed() {
    wasDismissed = false;
  }


  public void reset() {
    getReadyButton().reset();
    inSeasonCropCards.clear();
    wasDismissed = false;
    show = false;
    backgroundImage = null;
  }

  public void open() {
    textYScale = getTextYScale();

    logger.trace("SeasonScreen::open called");
    logger.trace("SeasonScreen::open Settings.scale: "+ Settings.scale
            + ", Settings.HEIGHT: " + Settings.HEIGHT + ", SCALE_X: " + SettingsHelper.getScaleX()
            +", SettingsHelper.getScaleY(): " + SettingsHelper.getScaleY() + ", textYScale: " + textYScale);

    AbstractDungeon.isScreenUp = true;

    if (AbstractDungeon.overlayMenu != null) {
      CancelButton cancelButton = AbstractDungeon.overlayMenu.cancelButton;
      showCancelButtonOnClose = cancelButton.isHidden;

      // TODO: localize this (or fix it properly)
      cancelButtonText =
          (cancelButton.buttonText != null
            && cancelButton.buttonText.length() > 0
            && !cancelButton.buttonText.equals("NOT_SET")) ?
              cancelButton.buttonText : "Return";

      if (!SHOULD_SHOW_CANCEL_BUTTON && !cancelButton.isHidden) {
        logger.trace("SeasonScreen::open hiding cancel button");

        cancelButton.hide();
        showCancelButtonOnClose = true;
      }
    }

    final List<AbstractCropPowerCard> seasonalCropCards = TheSimpletonMod.getSeasonalCropCards();

    if (seasonalCropCards != null && !seasonalCropCards.isEmpty()) {
      inSeasonCropCards.addAll(TheSimpletonMod.getSeasonalCropCards());
      positionCards((Settings.WIDTH + CARD_GROUP_OFFSET_X)/ 2.0f, (CROP_CARDS_Y + CARD_GROUP_OFFSET_Y) * SettingsHelper.getScaleY());
      generateCardHitboxes(inSeasonCropCards.stream().map(c -> (AbstractCard)c).collect(Collectors.toList()));

      show = true;
      this.getReadyButton().enable();
      this.getReadyButton().show();
    } else {
      logger.warn("No seasonal crops found");
    }

    tooltip = new SeasonTooltip(TheSimpletonMod.getSeason(), TOOLTIP_X * SettingsHelper.getScaleX(),
        TOOLTIP_Y * SettingsHelper.getScaleY());

    for (final AbstractCropPowerCard card : this.inSeasonCropCards) {
      UnlockTracker.markCardAsSeen(card.cardID);
    }
//    // TODO: set isScreenUp to previous value on close
  }

  public void close() {
    logger.trace("SeasonScreen::close called");

    if (showCancelButtonOnClose) {
      logger.trace("SeasonScreen::close showing cancel button (showCancelButtonOnClose == true)");

      AbstractDungeon.overlayMenu.cancelButton.show(cancelButtonText);
    }
    AbstractDungeon.dungeonMapScreen.open(false);

    this.dismiss();
  }

  public void dismiss() {
    logger.trace("SeasonScreen::dismiss called");
    logger.trace("SeasonScreen::dismiss setting wasDismissed to true");

    this.wasDismissed = true;
    show = false;
  }

  public void update() {
    if (!isOpen()) {
      return;
    }

    hb.update();

    for (AbstractCard card : inSeasonCropCards) {
      card.hb.update();
    }

    if (Settings.isControllerMode && AbstractDungeon.topPanel.potionUi.isHidden && !AbstractDungeon.topPanel.selectPotionMode) {
      getReadyButton().induceHover = true;
      if (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) {
        getReadyButton().pressed = true;
      }
    } else {
      getReadyButton().induceHover = false;
    }

    getReadyButton().update();

    tooltip.update();
  }

  private Texture getBackgroundImage() {
    if (backgroundImage == null) {
      backgroundImage = TheSimpletonMod.loadTexture(
          TheSimpletonMod.getImageResourcePath(getSeasonImagePath(TheSimpletonMod.getSeason())));
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
    final float READY_BUTTON_X = 806 * SettingsHelper.getScaleX();
    final float READY_BUTTON_Y = 146 * SettingsHelper.getScaleY();

    final String READY_BUTTON_LABEL = getUiText()[2];
    return new ReadyButton(READY_BUTTON_X, READY_BUTTON_Y, SettingsHelper.getScaleY(), READY_BUTTON_LABEL, this);
  }



  public void render(SpriteBatch sb) {
    if (!isOpen()) {
      return;
    }
//
//    logger.trace("SeasonScreen::render "
//        + " Settings.WIDTH: "  + Settings.WIDTH
//        + " Settings.HEIGHT: "  + Settings.HEIGHT
//        + " Settings.isSixteenByTen: " + Settings.isSixteenByTen
//        + " Settings.scale: "  + Settings.scale
//        + " SCALE_X: "  + SettingsHelper.getScaleX()
//        + " SettingsHelper.getScaleY() "  + SettingsHelper.getScaleY());

    final String[] uiText = getUiText();

    // TODO: scale this to screen
    sb.draw(getBackgroundImage(), 0.0F, backgroundImageY, Settings.WIDTH, Settings.HEIGHT);
    hb.render(sb);

    getReadyButton().render(sb);

    final TintEffect seasonLeadInTextEffect = new TintEffect();
    seasonLeadInTextEffect.changeColor(new Color(1.0F, 0.87F, 0.0F, 1.0F));

//    FontHelper.renderFontCentered(sb, FontHelper.losePowerFont, uiText[0], (Settings.WIDTH / 2.0F),
//        SEASON_DESCRIPTION_PREAMBLE_Y * textYScale, seasonLeadInTextEffect.color, textYScale);
//
//    final TintEffect seasonNameTextEffect = new TintEffect();
//
//    seasonNameTextEffect.changeColor(new Color(0.5F, 0.3F, 0.0F, 1.0F));
//
//    FontHelper.renderFontCentered(sb, FontHelper.bannerNameFont, TheSimpletonMod.getSeason().name, (Settings.WIDTH / 2.0F),
//        SEASON_DESCRIPTION_NAME_Y * textYScale, seasonNameTextEffect.color, textYScale);
//
//    textEffect.changeColor(new Color(0.9F, 0.9F, 0.9F, 1.0F));
//
//    FontHelper.renderFontCentered(sb, FontHelper.menuBannerFont, uiText[1], (Settings.WIDTH / 2.0F) + CROP_TEXT_OFFSET_X,
//        CROPS_IN_SEASON_TEXT_Y * textYScale, textEffect.color, textYScale);

    FontHelper.renderFontCentered(sb, FontHelper.losePowerFont, uiText[0], (Settings.WIDTH / 2.0F),
            SEASON_DESCRIPTION_PREAMBLE_Y * textYScale, seasonLeadInTextEffect.color, 1.0F);

    final TintEffect seasonNameTextEffect = new TintEffect();

    seasonNameTextEffect.changeColor(new Color(0.5F, 0.3F, 0.0F, 1.0F));

    FontHelper.renderFontCentered(sb, FontHelper.bannerNameFont, TheSimpletonMod.getSeason().name, (Settings.WIDTH / 2.0F),
            SEASON_DESCRIPTION_NAME_Y * textYScale, seasonNameTextEffect.color, 1.2F);

    textEffect.changeColor(new Color(0.9F, 0.9F, 0.9F, 1.0F));

    FontHelper.renderFontCentered(sb, FontHelper.menuBannerFont, uiText[1], (Settings.WIDTH / 2.0F) + CROP_TEXT_OFFSET_X,
            CROPS_IN_SEASON_TEXT_Y * textYScale, textEffect.color, 1.0F);

    for (AbstractCard card : inSeasonCropCards) {
      card.hb.render(sb);
    }

    for (final AbstractCard c : inSeasonCropCards) {
      c.render(sb);
      c.hb.render(sb);

      if (c.hb.hovered) {
        TipHelper.renderTipForCard(c, sb, c.keywords);
      }
    }

    tooltip.render(sb);
  }

  private void generateCardHitboxes(List<AbstractCard> cards) {
    for (AbstractCard card : cards) {
      card.hb = new Hitbox(card.target_x - card.hb.width / 2, (card.target_y - card.hb.height / 2 ),
          card.hb.width, card.hb.height);
    }
  }

  private void positionCards(float x, float y) {
    // TODO: animation — currentx for all starts at x; approaches target x on each tick (fans out)

    inSeasonCropCards.get(0).target_x = (Settings.WIDTH  + CARD_GROUP_OFFSET_X) / 2.0f - AbstractCard.IMG_WIDTH - CARD_OFFSET_X;
    inSeasonCropCards.get(1).target_x = (Settings.WIDTH  + CARD_GROUP_OFFSET_X) / 2.0f;
    inSeasonCropCards.get(2).target_x = (Settings.WIDTH  + CARD_GROUP_OFFSET_X) / 2.0f + AbstractCard.IMG_WIDTH + CARD_OFFSET_X;
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

  private String[] getUiText() {
    if (uiStrings == null) {
      uiStrings = CardCrawlGame.languagePack.getUIString(UI_NAME);
    }
    return uiStrings.TEXT;
  }

  @Override
  public void onReadyClicked() {
    logger.trace("SeasonScreen::onReadyClicked called");
    getReadyButton().disable();
    getReadyButton().hide();
    logger.trace("SeasonScreen::onReadyClicked setting wasDismissed to true");
    wasDismissed = true;
    this.close();
  }
}