package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;

public class HotPotato extends CustomRelic {
  public static final String ID = "TheSimpletonMod:HotPotato";
  public static final String IMG_PATH = "relics/hotpotato.png";
  public static final String IMG_PATH_LARGE = "relics/hotpotato_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/hotpotato_outline.png";

  private static final RelicTier TIER = RelicTier.UNCOMMON;
  private static final LandingSound SOUND = LandingSound.SOLID;


  public HotPotato() {
    super(ID, new Texture(TheSimpletonMod.getImageResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  public void activate(AbstractCreature source, AbstractCreature target, int amount) {
    flash();
  }

  @Override
  public AbstractRelic makeCopy() {
    return new HotPotato();
  }

}
