package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.OnionCropOrb;
import thesimpleton.utilities.ModLogger;

public class OnionBelt extends CustomRelic {
  private static ModLogger logger = TheSimpletonMod.traceLogger;
  public static final String ID = "TheSimpletonMod:OnionBelt";
  public static final String IMG_PATH = "relics/onionbelt.png";
  public static final String IMG_PATH_LARGE = "relics/onionbelt_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/onionbelt_outline.png";

//  private static final RelicTier TIER = RelicTier.SHOP;
  private static final RelicTier TIER = RelicTier.BOSS;

  private static final LandingSound SOUND = LandingSound.HEAVY;

  private static final int CROP_AMOUNT = 2;

  public OnionBelt() {
    super(ID, new Texture(TheSimpletonMod.getImageResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_LARGE));

   logger.trace("Instantiating OnionBelt");
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  @Override
  public void atTurnStart() {
    if (!AbstractCropOrb.playerHasAnyCropOrbs()) {
      this.flash();
      addOnionStack(CROP_AMOUNT);
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new OnionBelt();
  }

  @Override
  public boolean canSpawn() {
    return AbstractDungeon.player.hasRelic(SpudOfTheInnocent.ID);
  }


  public static void addOnionStack(int amount) {
    logger.trace("OnionBelt: Adding onion stack");
    final AbstractPlayer p = AbstractDungeon.player;

    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new OnionCropOrb() , amount, false));
  }
}
