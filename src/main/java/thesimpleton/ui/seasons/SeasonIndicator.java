package thesimpleton.ui.seasons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import thesimpleton.TheSimpletonMod;
import thesimpleton.seasons.Season;

import java.util.ArrayList;
import java.util.List;

public class SeasonIndicator {
  private static final SeasonIndicator WINTER_INIDCATOR;
  private static final SeasonIndicator SPRING_INDICATOR;
  private static final SeasonIndicator SUMMER_INDICATOR;
  private static final SeasonIndicator AUTUMN_INDICATOR;
  private static final String[] TEXT;

  private static final float WIDTH = 190;
  private static final float HEIGHT = 52;
  private static final float X_OFFSET = 0;
  private static final float Y_OFFSET = 190;
  private static final float X_TEXT_OFFSET = 10;
  private static final float Y_TEXT_OFFSET = 28;

  private static final BitmapFont LABEL_FONT = FontHelper.panelNameFont;
  private static final Color LABEL_COLOR = Color.valueOf("ffffdbff");
  private final String uiName;
  private String seasonLabel;
  private String imgPath;
  private Texture indicatorImage;
  private final int xOffset;
  private final int yOffset;
  private final int xTextOffset;
  private final int yTextOffset;
  private Hitbox hb;

  private static final String UI_NAME = TheSimpletonMod.makeID("SeasonalIndicator");

  private static String getUiPath(String id) {
    return "customui/seasonscreen/" + id + ".png";
  }

  private static ArrayList<String> seasonLabels = new ArrayList<>();
  private ArrayList<PowerTip> keywordPowerTips;

  // TODO: also render over dungeon map

  public SeasonIndicator(String uiName, String imgName) {
    this.uiName = uiName;
    this.imgPath = getUiPath(imgName);

    this.xOffset = Math.round(X_OFFSET * Settings.scale);
    this.yOffset =  Math.round((Settings.HEIGHT - Y_OFFSET)* Settings.scale);
    this.xTextOffset = Math.round(X_TEXT_OFFSET * Settings.scale);
    this.yTextOffset =  Math.round(Y_TEXT_OFFSET * Settings.scale);

    this.hb = new Hitbox(WIDTH * Settings.scale, HEIGHT * Settings.scale);
    hb.translate(xOffset, yOffset);
  }

  public static boolean shouldRender() {
     return AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && !TheSimpletonMod.seasonScreen.isOpen();
  }

  private Texture getIndicatorImage() {
    if (this.indicatorImage == null) {
      this.indicatorImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getResourcePath(this.imgPath));
    }
    return this.indicatorImage;
  }

  private String getSeasonLabel() {

    if (this.seasonLabel == null) {
      UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheSimpletonMod.makeID(uiName));
      this.seasonLabel = uiStrings.TEXT[0];
    }
    return this.seasonLabel;
  }

  private ArrayList<String> getCropLabels() {
    // TODO: create getSeasonalCrop method (which returns Crop enum values)
    if (seasonLabels.isEmpty()) {
      TheSimpletonMod.getSeasonalCropCards().stream().forEach(c -> seasonLabels.add(c.name));
    }
    return seasonLabels;
  }

  public void reset() {
    this.seasonLabel = null;
    this.indicatorImage = null;
    this.seasonLabels.clear();
  }

  public void update() {
    hb.update();
    //  handleClick();
  }

  private void handleClick() {
    if (InputHelper.justClickedLeft) {
      TheSimpletonMod.logger.debug("SeasonIndicator::update handling click");

      if (TheSimpletonMod.seasonScreen.isOpen()) {
        TheSimpletonMod.logger.debug("SeasonIndicator::update season screen is open. closing screen");

        TheSimpletonMod.seasonScreen.close();
      } else {
        TheSimpletonMod.logger.debug("SeasonIndicator::update season screen is closed. opening screen");
        TheSimpletonMod.seasonScreen.resetDismissed();
        TheSimpletonMod.seasonScreen.open();
      }
    }
  }

  public void render(SpriteBatch sb) {
    if (shouldRender()) {

      if (this.hb.hovered) {
        final float TOOLTIP_X_OFFSET = 0.0F;
        final float TOOLTIP_Y_OFFSET = -32.0F;
        TipHelper.queuePowerTips(hb.x + TOOLTIP_X_OFFSET, hb.y - TOOLTIP_Y_OFFSET, getPowerTips());
      }

      sb.draw(this.getIndicatorImage(), this.xOffset, this.yOffset, 0, 0, Math.round(hb.width), Math.round(hb.height));

      FontHelper.renderFontLeft(
          sb,
          LABEL_FONT,
          getSeasonLabel(),
          this.xOffset + this.xTextOffset,
          this.yOffset + this.yTextOffset,
          LABEL_COLOR);

      hb.render(sb);
    }
  }

  private ArrayList<PowerTip> getPowerTips() {
    if (keywordPowerTips == null) {
      keywordPowerTips = new ArrayList<>();

      // main tooltip
      keywordPowerTips.add(new PowerTip(getTipHeader(), getTipText()));

      // crop keyword tooltips
      for (String cropName: getCropLabels()) {
        final String keyword = cropName.toLowerCase();
        if (GameDictionary.keywords.containsKey(keyword)) {
          keywordPowerTips.add(new PowerTip(cropName, GameDictionary.keywords.get(keyword)));
        }
      }
    }

    return keywordPowerTips;
  }

  public String getTipHeader() {
    return TEXT[0] + getSeasonLabel() + ".";
  }

  public String getTipText() {
    List<String> seasonalCropLabels = getCropLabels();
    String tipText = "";

    if (seasonalCropLabels.size() > 0) {
      tipText += "#y" + seasonalCropLabels.get(0);
      if (seasonalCropLabels.size() > 1) {
        if (seasonalCropLabels.size() > 2) {
          tipText += TEXT[1] + "#y" + seasonalCropLabels.get(1) + TEXT[2] + "#y" + seasonalCropLabels.get(2);
        } else {
          tipText += TEXT[3] + "#y" + seasonalCropLabels.get(1);
        }
      }
      tipText += TEXT[4];
    }
    tipText += TEXT[5];

    return tipText;
  }

  public static SeasonIndicator getIndicator(Season season) {
    switch(season) {
      case WINTER:
        return WINTER_INIDCATOR;
      case SPRING:
        return SPRING_INDICATOR;
      case SUMMER:
        return SUMMER_INDICATOR;
      case AUTUMN:
      default:
        return AUTUMN_INDICATOR;
    }
  }

  static {
    WINTER_INIDCATOR = new SeasonIndicator("WinterSeason", "season-indicator-winter");
    SPRING_INDICATOR = new SeasonIndicator("SpringSeason", "season-indicator-spring");
    SUMMER_INDICATOR  = new SeasonIndicator("SummerSeason", "season-indicator-summer");
    AUTUMN_INDICATOR  = new SeasonIndicator("AutumnSeason", "season-indicator-autumn");

    UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_NAME);
    TEXT = uiStrings.TEXT;
  }
}
