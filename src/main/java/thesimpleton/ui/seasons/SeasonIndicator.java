package thesimpleton.ui.seasons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.localization.UIStrings;
import thesimpleton.TheSimpletonMod;
import thesimpleton.seasons.Season;

public class SeasonIndicator {
  private static final SeasonIndicator WINTER_INIDCATOR;
  private static final SeasonIndicator SPRING_INDICATOR;
  private static final SeasonIndicator SUMMER_INDICATOR ;
  private static final SeasonIndicator AUTUMN_INDICATOR ;

  private static final float WIDTH = 190;
  private static final float HEIGHT = 52;
  private static final float X_OFFSET = 0;
  private static final float Y_OFFSET = 186;
  private static final float X_TEXT_OFFSET = 10;
  private static final float Y_TEXT_OFFSET = 30;

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

  public void update() { }

  public void render(SpriteBatch sb) {

    sb.draw(this.getIndicatorImage(), this.xOffset, this.yOffset,0, 0, Math.round(hb.width), Math.round(hb.height));

    FontHelper.renderFontLeft(
        sb,
        LABEL_FONT,
        getSeasonLabel(),
        this.xOffset + this.xTextOffset,
        this.yOffset + this.yTextOffset,
        LABEL_COLOR);

//    if (this.hb.hovered) {
//      TipHelper.renderGenericTip((float) InputHelper.mX + 50.0F * Settings.scale, (float) InputHelper.mY, TEXT[0], getTipBody());
//    }

    hb.render(sb);
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
  }
}
