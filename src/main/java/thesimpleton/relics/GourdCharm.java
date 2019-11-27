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
import thesimpleton.orbs.SquashCropOrb;

public class GourdCharm extends CustomRelic {
  public static final String ID = "TheSimpletonMod:GourdCharm";
  public static final String IMG_PATH = "relics/gourdcharm.png";
  public static final String IMG_PATH_LARGE = "relics/gourdcharm_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/gourdcharm_outline.png";

  //  private static final RelicTier TIER = RelicTier.SHOP;
  private static final RelicTier TIER = RelicTier.BOSS;
  private static final LandingSound SOUND = LandingSound.HEAVY;

  private static final int CROP_AMOUNT = 2;

  public GourdCharm() {
    super(ID, new Texture(TheSimpletonMod.getImageResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_LARGE));

    Logger logger = TheSimpletonMod.logger;
    logger.debug("Instantiating GourdCharm");
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  @Override
  public void atTurnStart() {
    if (!AbstractCropOrb.playerHasAnyCropOrbs()) {
      this.flash();
      addSquashStack(CROP_AMOUNT);
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new GourdCharm();
  }

  @Override
  public boolean canSpawn() {
    return AbstractDungeon.player.hasRelic(SpudOfTheInnocent.ID);
  }

  //TODO: move this to potato power class
  public static void addSquashStack(int amount) {
    Logger logger = TheSimpletonMod.logger;
    logger.debug("GourdCharm: Adding squash stack");
    final AbstractPlayer p = AbstractDungeon.player;

    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new SquashCropOrb(), amount,false));
  }
}
