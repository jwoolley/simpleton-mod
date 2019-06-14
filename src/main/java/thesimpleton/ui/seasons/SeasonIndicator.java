package thesimpleton.ui.seasons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.seasons.Season;

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
  private static final float Y_OFFSET = 186;
  private static final float X_TEXT_OFFSET = 10;
  private static final float Y_TEXT_OFFSET = 28;

  private static final BitmapFont LABEL_FONT = FontHelper.panelNameFont;
  private static final Color LABEL_COLOR = Color.WHITE;
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

  private Texture getIndicatorImage() {
    if (this.indicatorImage == null) {
      this.indicatorImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getResourcePath(this.imgPath));
    }
    return this.indicatorImage;
  }

  private String getSeasonLabel() {

    if (this.seasonLabel == null) {
      TheSimpletonMod.logger.debug("SeasonIndicator::getSeasonLabel getting label for " + this.uiName);
      UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheSimpletonMod.makeID(uiName));
      TheSimpletonMod.logger.debug("SeasonIndicator::getSeasonLabel uiStrings " + uiStrings);
      TheSimpletonMod.logger.debug("SeasonIndicator::getSeasonLabel TEXT " + uiStrings.TEXT);

      this.seasonLabel = uiStrings.TEXT[0];
    }
    return this.seasonLabel;
  }

  public void update() {
    hb.update();
  }

  public void render(SpriteBatch sb) {

    sb.draw(this.getIndicatorImage(), this.xOffset, this.yOffset,0, 0, Math.round(hb.width), Math.round(hb.height));

    FontHelper.renderFontLeft(
        sb,
        LABEL_FONT,
        getSeasonLabel(),
        this.xOffset + this.xTextOffset,
        this.yOffset + this.yTextOffset,
        LABEL_COLOR);

    if (this.hb.hovered && !AbstractDungeon.isScreenUp) {
      TheSimpletonMod.logger.debug("SeasonIndicator::update renderingTip");

      final float TOOLTIP_Y_OFFSET = 2.0F;
      TipHelper.renderGenericTip(
          hb.x,
          hb.y - HEIGHT - TOOLTIP_Y_OFFSET,
          getTipHeader(), getTipText());
    }

    hb.render(sb);
  }

  public String getTipHeader() {
    return TEXT[0] + getSeasonLabel() + ".";
  }

  public String getTipText() {
    final List<AbstractCropPowerCard> seasonalCropCards = TheSimpletonMod.getSeasonalCropCards();

    String tipText = "";

    if (seasonalCropCards.size() > 0) {
      tipText += "#y" + seasonalCropCards.get(0);
      if (seasonalCropCards.size() > 1) {
        if (seasonalCropCards.size() > 2) {
          tipText += TEXT[1] + "#y" + seasonalCropCards.get(1) + TEXT[2] + "#y" + seasonalCropCards.get(2);
        } else {
          tipText += TEXT[3] + "#y" + seasonalCropCards.get(1);
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
