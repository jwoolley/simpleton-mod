package thesimpleton.relics.seasons;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;

public class PlaceholderSeasonRelic extends CustomRelic {
  public static final String ID = "TheSimpletonMod:PlaceholderSeasonRelic";
  public static final String IMG_PATH = "relics/seasonautumn.png";
  public static final String IMG_PATH_LARGE = "relics/seasonautumn_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/seasonautumn_outline.png";


  private static final RelicTier TIER = RelicTier.SPECIAL;
  private static final LandingSound SOUND = LandingSound.MAGICAL;

    public PlaceholderSeasonRelic() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  @Override
  public AbstractRelic makeCopy() {
    return new PlaceholderSeasonRelic();
  }
}
