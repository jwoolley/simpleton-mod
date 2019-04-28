package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;

public class GasCan extends CustomRelic {
  public static final String ID = "TheSimpletonMod:GasCan";
  public static final String IMG_PATH = "relics/gascan.png";
  public static final String IMG_PATH_LARGE = "relics/gascan_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/gascan_outline.png";

  private static final RelicTier TIER = RelicTier.UNCOMMON;
  private static final LandingSound SOUND = LandingSound.FLAT;


  public GasCan() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  public void triggerEffect() {
    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_FIRE"));
    this.flash();
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  @Override
  public AbstractRelic makeCopy() {
    return new GasCan();
  }
}
