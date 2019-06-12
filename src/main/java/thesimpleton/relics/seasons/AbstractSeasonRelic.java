package thesimpleton.relics.seasons;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.seasons.Season;

public abstract class AbstractSeasonRelic extends CustomRelic {
  private static final RelicTier TIER = RelicTier.STARTER;
  private static final LandingSound SOUND = LandingSound.MAGICAL;

  final private Season season;
  final Logger logger = TheSimpletonMod.logger;

  public AbstractSeasonRelic(String id, String imagePath, String largeImagePath, String outlineImagePath, Season season) {
    super(id, new Texture(TheSimpletonMod.getResourcePath(imagePath)),
        new Texture(TheSimpletonMod.getResourcePath(outlineImagePath)), TIER, SOUND);

    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(largeImagePath));
    this.season = season;

    // TODO: make this into a custom UI element instead of a relic

     logger.debug(this.getClass().getSimpleName() + " intantiated " + this);
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0]; // list in-season crops etc.
  }

  @Override
  public void onEquip() {
    logger.debug(this.getClass().getSimpleName() + " onEquip called for " + this);
  }

  public AbstractSeasonRelic makeCopy() {
    return this;
  }

  public static AbstractSeasonRelic getSeasonRelic(Season season) {
    switch (season) {
      case AUTUMN:
        return new AutumnSeasonRelic();
      case WINTER:
      case SPRING:
      case SUMMER:
      default:
        return new AutumnSeasonRelic();
    }
  }
}