package thesimpleton.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.ui.SettingsHelper;

import java.util.*;
import java.util.stream.Collectors;

public class ParasiteFruitOrb extends CustomOrb {
  public static final String ORB_ID = "TheSimpletonMod:ParasiteFruitOrb";

  public static final String IMG_PATH_PREFIX = "alienfruit/alienfruit";
  public static final String STARTING_IMG_PATH = "alienfruit/alienfruit1";

  public static Texture[] ORB_STAGE_IMAGES;

  private static final int NUM_STAGES = 5;

  public static final String HALO_IMG_PATH = "alienfruit/alienfruit_halo";
  public static final String TARGET_HALO_IMG_PATH = "alienfruit/alienfruit_target_halo";
  private static final String ORB_DESCRIPTION_ID = "TheSimpletonMod:ParasiteFruitOrb";
  private static final OrbStrings orbStrings;
  public static final String[] DESCRIPTIONS;
  public static final String NAME;

  private final static Color MATURE_CROP_HALO_COLOR = Color.WHITE.cpy();

//  private final static Color MATURE_CROP_HALO_COLOR = new Color(237.0F, 254.0F, 53.0F, 0.2F); //Color.YELLOW;
//  private final static Color CROP_STACK_COUNT_FONT_COLOR = Color.WHITE.cpy();
//  private final static Color MATURE_CROP_STACK_COUNT_FONT_COLOR = Color.YELLOW; // new Color(250.0F, 255.0F, 190.0F, 1.0F); //Color.YELLOW;

  private static final float TOOLTIP_X_OFFSET = 80.0F;
  private static final float TOOLTIP_Y_OFFSET = -48.0F;

  private Texture haloImage;
  private Texture targetHaloImage;
  private String haloImageFilename;
  private String targetHaloImageFilename;
  private int growthStage = 1;
  private boolean updatePowerTips = true;

  // TODO: separate CropOrbType (which has e.g. harvest info and description data) from CropOrb (which has stack count)

  public ParasiteFruitOrb(int amount) {
    super(ORB_ID, NAME, amount, NUM_STAGES, getDescription(1), "",
        TheSimpletonMod.getResourcePath(SimpletonOrbHelper.getUiPath(STARTING_IMG_PATH)));
    this.basePassiveAmount = this.passiveAmount = amount;
    this.haloImageFilename = HALO_IMG_PATH;
    this.targetHaloImageFilename = TARGET_HALO_IMG_PATH;

    initializeOrbStageImages();

    this.hideEvokeValues();
  }

  private void initializeOrbStageImages() {
    if (ORB_STAGE_IMAGES == null) {
      ORB_STAGE_IMAGES  = new Texture[5];
      for (int i = 0; i < NUM_STAGES; i++) {
        ORB_STAGE_IMAGES[i]= ImageMaster.loadImage(
            TheSimpletonMod.getResourcePath(SimpletonOrbHelper.getUiPath(IMG_PATH_PREFIX + (i + 1))));
      }
    }
  }

  private Texture getHaloImage() {
    if (this.haloImage == null) {
      haloImage = TheSimpletonMod.loadTexture(
          TheSimpletonMod.getResourcePath(SimpletonOrbHelper.getUiPath(haloImageFilename)));
    }
    return haloImage;
  }

  private Texture getTargetHaloImage() {
    if (this.targetHaloImage == null) {
      targetHaloImage = TheSimpletonMod.loadTexture(
          TheSimpletonMod.getResourcePath(SimpletonOrbHelper.getUiPath(targetHaloImageFilename)));
    }
    return targetHaloImage;
  }


//  abstract public AbstractOrb makeCopy(int amount);

  @Override
  public void onEvoke() {
    TheSimpletonMod.logger.info("ParasiteFruitOrb::onEvoke called");
//    this.getCrop().harvestAll();
  }

//  public int getAmount() {
//    return 0;
//    AbstractCropOrb orb = getCropOrb();
//
//    return orb != null ? orb.passiveAmount : 0;
//    return hasCropOrb() ? getCropOrb().passiveAmount : 0;
//  }

//
//  public boolean isMature(boolean thisIsActiveOrb) {
////    TheSimpletonMod.logger.debug("AbstractCropOrb::isMature amount: " + this.getAmount() + " maturityThreshold: " + getCrop().getMaturityThreshold());
//    if (thisIsActiveOrb) {
//      return this.passiveAmount >= this.getCrop().getMaturityThreshold();
//    } else {
//      return this.getAmount() >= getCrop().getMaturityThreshold();
//    }
//  }

//  public static boolean isMature(Crop crop) {
//    AbstractCropOrb orb = AbstractCropOrb.getCropOrb(crop);
//    return orb != null && orb.isMature(false);
////    return hasCropOrb(crop) && getCropOrb(crop).isMature();
//  }
//
//  public boolean hasCropOrb() {
//    return hasCropOrb(this.ID);
//  }
//
//  public static boolean hasCropOrb(Crop crop) {
//    return hasCropOrb(crop.getCropInfo().orbId);
//  }
//
//  public AbstractCropOrb getCropOrb() {
//    return getCropOrb(this.ID);
//  }
//
//  //TODO: move to util
//  public static boolean hasCropOrb(AbstractCropOrb orbType) {
//    return hasCropOrb(orbType.ID);
//  }
//
//  //TODO: move to util
//  public static List<AbstractCropOrb> getActiveCropOrbs() {
//    return AbstractDungeon.player.orbs.stream()
//        .filter(orb -> orb instanceof AbstractCropOrb)
//        .map(orb -> (AbstractCropOrb)orb)
//        .collect((Collectors.toList()));
//  }
//
//  public static boolean hasCropOrb(String orbId) {
////    TheSimpletonMod.logger.debug("AbstractCropOrb::hasCropOrb : Player has orbs:"
////        + AbstractDungeon.player.orbs.stream().map(orb -> orb.name).collect(Collectors.joining(", ")));
//
//    Optional<AbstractOrb> cropOrb = AbstractDungeon.player.orbs.stream()
//        .filter(orb -> orb.ID == orbId)
//        .findFirst();
//
//    return cropOrb.isPresent();
//  }
//
//  public static int getNumberActiveCropOrbs() {
//    return getActiveCropOrbs().size();
//  }

//  public void gainCropEffectBefore() {
//    AbstractDungeon.effectList.add(new GainCropSoundEffect(-1.0F, -1.0F));
//  }
//
//  public void gainCropEffectAfter() {
//    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y));
//  }
//
//  public void stackCropEffect() {
//    AbstractDungeon.effectList.add(new StackCropSoundEffect(this.hb.cX, this.hb.y));
//    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y));
//  }
//
//  public void harvestCropEffect() {
//    //TODO: use an animation that's visually distinct from gain or stack animations
//    AbstractDungeon.effectList.add(new HarvestCropSoundEffect(this.hb.cX, this.hb.y));
//    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y, Settings.ACTION_DUR_MED));
//  }


//  //TODO: move to util
//  public static AbstractCropOrb getCropOrb(AbstractCropOrb orbType) {
//    return getCropOrb(orbType.ID);
//  }
//
//  public static AbstractCropOrb getCropOrb(Crop crop) {
//    return getCropOrb(crop.getCropInfo().orbId);
//  }

  public void stackOrb(int amount) {

  }

//  public static AbstractCropOrb getCropOrb(String orbId) {
//    Optional<AbstractOrb> cropOrb = AbstractDungeon.player.orbs.stream()
//        .filter(orb -> orb instanceof AbstractCropOrb && orb.ID == orbId)
//        .findFirst();
//
//    if (!cropOrb.isPresent()) {
//      return null;
//    }
//
//    return (AbstractCropOrb)cropOrb.get();
//  }

  @Override
  public void onStartOfTurn() {
    TheSimpletonMod.logger.info("ParasiteFruitOrb::onStartOfTurn called");
    incrementStage();
    TheSimpletonMod.logger.info("ParasiteFruitOrb::onStartOfTurn new growthStage: " + this.growthStage);
  }

  private void incrementStage() {
    if (this.growthStage >= 1 && this.growthStage < NUM_STAGES) {
      TheSimpletonMod.logger.info("ParasiteFruitOrb::incrementStage triggered. growthStage: " + this.growthStage);
      this.growthStage++;
      TheSimpletonMod.logger.info("ParasiteFruitOrb::incrementStage new growthStage: " + this.growthStage);
      this.img = ORB_STAGE_IMAGES[this.growthStage - 1];
      AbstractDungeon.actionManager.addToBottom(new SFXAction("SQUELCH_SLIMY_1"));
      updateDescription();

      if (this.growthStage == NUM_STAGES) {
        this.updatePowerTips = true;
      }
    } else {
      TheSimpletonMod.logger.info("ParasiteFruitOrb::incrementStage did not trigger. growthStage: " + this.growthStage + "; threshold: " + NUM_STAGES);
    }
  }

  @Override
  public AbstractOrb makeCopy() {
    return new ParasiteFruitOrb(this.passiveAmount);
  }

  @Override
  public void render(SpriteBatch sb) {
    super.render(sb);

//    Color filterColor = MATURE_CROP_HALO_COLOR;
//    Color textColor = MATURE_CROP_STACK_COUNT_FONT_COLOR;
//
//    if (this.isMature(true)) {
//      // TODO: when stacks > maturity level, replace with flash image + add sound effect for those few frames
//      //      final Color overplantFilterColor = Color.LIME;
//      //      final Color overplantTextColor = Color.LIME;
//      //      if (this.getAmount() >  this.getCrop().getMaturityThreshold()) {
//      //        filterColor = overplantFilterColor;
//      //        textColor = overplantTextColor;
//      //      }
//
//      // TODO: Highlight targeted crop on dynamic card hover (e.g. Aerate) with different-colored halo
//
//      sb.draw(this.getHaloImage(), this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
//      this.c = textColor;
//      renderText(sb);
//      this.c = filterColor;
//    } else {
//      this.c = CROP_STACK_COUNT_FONT_COLOR;
//    }
//
//    Color highlightFilterColor = Color.GOLD.cpy();
//    if (CropOrbHelper.hasHighlightedOrb()) {
//      if (CropOrbHelper.getHighlightedOrb().name == this.name) {
////        this.c = highlightFilterColor;
//        sb.draw(this.getTargetHaloImage(), this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
//      }
//    } else {
//      this.c = filterColor;
//    }
  }

  @Override
  public void playChannelSFX() {

  }

  @Override
  public void update() {
    this.hb.update();

    if (this.hb.hovered) {
      TipHelper.queuePowerTips(
          hb.x + TOOLTIP_X_OFFSET * SettingsHelper.getScaleX(),
          hb.y - TOOLTIP_Y_OFFSET * SettingsHelper.getScaleY(),
          getPowerTips());

      this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }
  }

  protected List<Keyword> getBaseGameKeywords() {
    return new ArrayList<>();
  }

  protected List<String> getCustomKeywords() {
    return new ArrayList<>();
  }

  private ArrayList<PowerTip> keywordPowerTips;

  private ArrayList<PowerTip> getPowerTips() {
    if (this.updatePowerTips) {
      keywordPowerTips = new ArrayList<>();

      // main tooltip
      keywordPowerTips.add(new PowerTip(this.name, getDescription(this.growthStage)));
      // crop keyword tooltips
      for (Map.Entry<String,String> entry : getKeywords().entrySet()) {
        keywordPowerTips.add(new PowerTip(TipHelper.capitalize(entry.getKey()), entry.getValue()));
      }
      this.updatePowerTips = false;
    }
    return keywordPowerTips;
  }

  private Map<String, String> getKeywords() {
    Map<String, String> keywords = getCustomKeywords().stream()
        .map(k -> TheSimpletonMod.getKeyword(k))
        .filter(kw -> kw != null)
        .collect(Collectors.toMap(kw -> kw.PROPER_NAME, kw -> kw.DESCRIPTION));

    keywords.putAll(getBaseGameKeywords().stream()
        .collect(Collectors.toMap(kw -> kw.NAMES[0], kw -> kw.DESCRIPTION)));

    return keywords;
  }

//
//  public void onShuffle() {
//    if (!this.isMature(true)) {
//      AbstractCrop.stackOrb(this, this.stackAmountOnShuffle, false);
//    }
//  }

  private static String getDescription(int growthStage) {
    if (growthStage < NUM_STAGES) {
      return DESCRIPTIONS[0];
    } else {
      return DESCRIPTIONS[1];
    }
  }

  @Override
  public void updateDescription() {
    this.description = getDescription(this.growthStage);
    TheSimpletonMod.logger.info("ParasiteFruitOrb::updateDescription new Description. growthStage: " + this.growthStage + "; threshold: " + NUM_STAGES);
  }

  // TODO: special evoke text?
  @Override
  protected void renderText(SpriteBatch sb) {
    if (this.showEvokeValue) {

    }
  }


  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_DESCRIPTION_ID);
    NAME = orbStrings.NAME;
    DESCRIPTIONS = orbStrings.DESCRIPTION;
  }
}
