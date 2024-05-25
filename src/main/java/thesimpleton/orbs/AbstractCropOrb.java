package thesimpleton.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.crops.Crop;
import thesimpleton.effects.orb.CropAnimationEffect;
import thesimpleton.effects.orb.GainCropSoundEffect;
import thesimpleton.effects.orb.HarvestCropSoundEffect;
import thesimpleton.effects.orb.StackCropSoundEffect;
import thesimpleton.orbs.utilities.CropOrbHelper;
import thesimpleton.ui.SettingsHelper;
import thesimpleton.utilities.ModLogger;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCropOrb extends CustomOrb {
  protected static final ModLogger ORB_LOGGER = TheSimpletonMod.debugLogger;
  private static final String ORB_DESCRIPTION_ID = "TheSimpletonMod:AbstractCropOrb";
  private final static String[] GENERIC_DESCRIPTION;
  private final static OrbStrings orbStrings;

  private final static Color MATURE_CROP_HALO_COLOR = Color.WHITE.cpy();
//  private final static Color MATURE_CROP_HALO_COLOR = new Color(237.0F, 254.0F, 53.0F, 0.2F); //Color.YELLOW;

  private final static Color CROP_STACK_COUNT_FONT_COLOR = Color.WHITE.cpy();
  private final static Color MATURE_CROP_STACK_COUNT_FONT_COLOR = Color.YELLOW; // new Color(250.0F, 255.0F, 190.0F, 1.0F); //Color.YELLOW;

  public static final String INDICATOR_ARROW_IMG_FILENAME = "arrow-indicator";
  public static final String INDICATOR_DOT_EMPTY_IMG_FILENAME = "indicator-dot-empty";
  public static final String INDICATOR_DOT_FULL_IMG_FILENAME = "indicator-dot-full";
  public static final String INDICATOR_DOT_GLOWING_IMG_FILENAME = "indicator-dot-glowing";

  float CROP_ORB_WIDTH = 128.0F;
  float CROP_ORB_HEIGHT = 128.0F;
  float ARROW_INDICATOR_WIDTH = 48.0F;
  float ARROW_INDICATOR_HEIGHT = 48.0F;

  float INDICATOR_DOT_WIDTH = 10.0F;
  float INDICATOR_DOT_HEIGHT = 10.0F;
  float INDICATOR_GLOWING_DOT_WIDTH = 10.0F;
  float INDICATOR_GLOWING_DOT_HEIGHT = 10.0F;

  float INDICATOR_DOT_FIXED_OFFSET_X = -CROP_ORB_WIDTH / 6.0F * Settings.scale;
  float INDICATOR_DOT_FIXED_OFFSET_Y =  CROP_ORB_HEIGHT / 8.5F * Settings.scale;

  private static final float TOOLTIP_X_OFFSET = 80.0F;
  private static final float TOOLTIP_Y_OFFSET = -48.0F;

  public static final int STACK_AMOUNT_ON_SHUFFLE = 1;

  private final Crop crop;

  private Texture haloImage;
  private Texture targetHaloImage;
  private String haloImageFilename;
  private String targetHaloImageFilename;
  private String indicatorArrowImageFilename;
  private String indicatorDotEmptyImageFilename;
  private String indicatorDotFullImageFilename;
  private String indicatorDotGlowingImageFilename;

  private Texture indicatorArrowImage;
  private Texture indicatorDotEmptyImage;
  private Texture indicatorDotFullImage;
  private Texture indicatorDotGlowingImage;


  private final int stackAmountOnShuffle;

  // TODO: separate CropOrbType (which has e.g. harvest info and description data) from CropOrb (which has stack count)

  public AbstractCropOrb(Crop crop, String ID, String NAME, int amount, int maturityThreshold, String description, String imgPath, String haloImgFilename, String targetHaloImgFilename) {
    super(ID, NAME, amount, maturityThreshold, description, "", TheSimpletonMod.getImageResourcePath(getUiPath(imgPath)));
    this.crop = crop;
    this.basePassiveAmount = this.passiveAmount = amount;
    this.haloImageFilename = haloImgFilename;
    this.targetHaloImageFilename = targetHaloImgFilename;
    this.indicatorArrowImageFilename = INDICATOR_ARROW_IMG_FILENAME;
    this.indicatorDotEmptyImageFilename = INDICATOR_DOT_EMPTY_IMG_FILENAME;
    this.indicatorDotFullImageFilename = INDICATOR_DOT_FULL_IMG_FILENAME;
    this.indicatorDotGlowingImageFilename = INDICATOR_DOT_GLOWING_IMG_FILENAME;
    this.stackAmountOnShuffle = STACK_AMOUNT_ON_SHUFFLE;
  }

  private Texture getHaloImage() {
    if (this.haloImage == null) {
      haloImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(getUiPath(haloImageFilename)));
    }
    return haloImage;
  }

  private Texture getTargetHaloImage() {
    if (this.targetHaloImage == null) {
      targetHaloImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(getUiPath(targetHaloImageFilename)));
    }
    return targetHaloImage;
  }
  private Texture getIndicatorArrow() {
    if (this.indicatorArrowImage == null) {
      indicatorArrowImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(getUiPath(indicatorArrowImageFilename)));
    }
    return indicatorArrowImage;
  }

  private Texture getIndicatorDotEmpty() {
    if (this.indicatorDotEmptyImage == null) {
      indicatorDotEmptyImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(getUiPath(indicatorDotEmptyImageFilename)));
    }
    return indicatorDotEmptyImage;
  }

  private Texture getIndicatorDotFull() {
    if (this.indicatorDotFullImage == null) {
      indicatorDotFullImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(getUiPath(indicatorDotFullImageFilename)));
    }
    return indicatorDotFullImage;
  }

  private Texture getIndicatorDotGlowing() {
    if (this.indicatorDotGlowingImage == null) {
      indicatorDotGlowingImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(getUiPath(indicatorDotGlowingImageFilename)));
    }
    return indicatorDotGlowingImage;
  }


  abstract public AbstractCropOrb makeCopy(int amount);

  @Override
  public void onEvoke() {
    this.getCrop().harvestAll();
  }

  public AbstractCrop getCrop() { return this.crop.getCropInfo().getCrop(); }

  public int getAmount() {
    AbstractCropOrb orb = getCropOrb();

    return orb != null ? orb.passiveAmount : 0;
  }

  public static String getGenericDescription(int maturityThreshold) {
    return GENERIC_DESCRIPTION[0] + maturityThreshold + GENERIC_DESCRIPTION[1];
  }

  public boolean isMature(boolean thisIsActiveOrb) {
    if (thisIsActiveOrb) {
      return this.passiveAmount >= this.getCrop().getMaturityThreshold();
    } else {
      return this.getAmount() >= getCrop().getMaturityThreshold();
    }
  }

  public static boolean isMature(Crop crop) {
    AbstractCropOrb orb = AbstractCropOrb.getCropOrb(crop);
    return orb != null && orb.isMature(false);
  }

  public static boolean hasCropOrb(Crop crop) {
    return hasCropOrb(crop.getCropInfo().orbId);
  }

  public AbstractCropOrb getCropOrb() {
    return getCropOrb(this.ID);
  }


  //TODO: move to util
  public static List<AbstractCropOrb> getActiveCropOrbs() {
    return AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb instanceof AbstractCropOrb)
        .map(orb -> (AbstractCropOrb)orb)
        .collect((Collectors.toList()));
  }

  public static boolean hasCropOrb(String orbId) {
    Optional<AbstractOrb> cropOrb = AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb.ID == orbId)
        .findFirst();

    return cropOrb.isPresent();
  }

  public static int getNumberActiveCropOrbs() {
    return getActiveCropOrbs().size();
  }

  public void gainCropEffectBefore() {
    AbstractDungeon.effectList.add(new GainCropSoundEffect(-1.0F, -1.0F));
  }

  public void gainCropEffectAfter() {
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y));
  }

  public void stackCropEffect() {
    AbstractDungeon.effectList.add(new StackCropSoundEffect(this.hb.cX, this.hb.y));
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y));
  }

  public void harvestCropEffect() {
    //TODO: use an animation that's visually distinct from gain or stack animations
    AbstractDungeon.effectList.add(new HarvestCropSoundEffect(this.hb.cX, this.hb.y));
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y, Settings.ACTION_DUR_MED));
  }

  public static boolean playerHasAnyCropOrbs() {
    return getActiveCropOrbs().size() > 0;
  }

  public static AbstractCropOrb getOldestCropOrb() {
    List<AbstractCropOrb> activeCropOrbs = getActiveCropOrbs();
    if (!playerHasAnyCropOrbs()) {
      return null;
    }
    return activeCropOrbs.get(0);
  }

  public static AbstractCropOrb getNewestCropOrb() {
    List<AbstractCropOrb> activeCropOrbs = getActiveCropOrbs();
    if (!playerHasAnyCropOrbs()) {
      return null;
    }
    return activeCropOrbs.get(activeCropOrbs.size() - 1);
  }
  //TODO: move to util
  public static AbstractCropOrb getCropOrb(AbstractCropOrb orbType) {
    return getCropOrb(orbType.ID);
  }

  public static AbstractCropOrb getCropOrb(Crop crop) {
    return getCropOrb(crop.getCropInfo().orbId);
  }

  public static AbstractCropOrb getCropOrb(String orbId) {

    Optional<AbstractOrb> cropOrb = AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb instanceof AbstractCropOrb && orb.ID == orbId)
        .findFirst();

    if (!cropOrb.isPresent()) {
      return null;
    }

    return (AbstractCropOrb)cropOrb.get();
  }

  @Override
  public void onStartOfTurn() {
    getCrop().atStartOfTurn();
  }

  protected static String getUiPath(String id) {
    return "orbs/" + id + ".png";
  }

  @Override
  public void render(SpriteBatch sb) {
    super.render(sb);

    Color filterColor = MATURE_CROP_HALO_COLOR;
    Color textColor = MATURE_CROP_STACK_COUNT_FONT_COLOR;

    float origin_X = CROP_ORB_WIDTH / 2.0F;
    float origin_Y = CROP_ORB_HEIGHT / 2.0F;

    float ARROW_OFFSET_X = -CROP_ORB_WIDTH / 5.0F;
    float ARROW_OFFSET_Y = 32.0F;

    // Show halo outline around mature crops
    if (this.isMature(true)) {
      // TODO: when stacks > maturity level, replace with flash image + add sound effect for those few frames
      //      final Color overplantFilterColor = Color.LIME;
      //      final Color overplantTextColor = Color.LIME;
      //      if (this.getAmount() >  this.getCrop().getMaturityThreshold()) {
      //        filterColor = overplantFilterColor;
      //        textColor = overplantTextColor;
      //      }

      // TODO: Highlight targeted crop on dynamic card hover (e.g. Aerate) with different-colored halo

      sb.draw(this.getHaloImage(), this.cX - origin_X, this.cY - origin_Y + this.bobEffect.y / 2.0F, origin_X, origin_Y, CROP_ORB_WIDTH, CROP_ORB_HEIGHT, this.scale, this.scale, 0.0F, 0, 0, (int) CROP_ORB_WIDTH, (int) CROP_ORB_HEIGHT, false, false);
      this.c = textColor;
      renderText(sb);
      this.c = filterColor;
    } else {
      this.c = CROP_STACK_COUNT_FONT_COLOR;
    }

    // Show arrow indicator for card-specific crop interactions (when the card is selected)
    Color highlightFilterColor = Color.GOLD.cpy();
    if (CropOrbHelper.hasHighlightedOrb()) {
      if (CropOrbHelper.getHighlightedOrb().name == this.name) {
//        this.c = highlightFilterColor;

        float arrowXPos = this.cX - (ARROW_INDICATOR_WIDTH / 2.0F - ARROW_OFFSET_X) * this.scale;
        float arrowYPos = this.cY + (ARROW_INDICATOR_HEIGHT + ARROW_OFFSET_Y) * this.scale + this.bobEffect.y / 2.0F;

        sb.draw(this.getTargetHaloImage(), this.cX - origin_X, this.cY - origin_Y + this.bobEffect.y / 2.0F, origin_X, origin_Y, CROP_ORB_WIDTH, CROP_ORB_HEIGHT, this.scale, this.scale, 0.0F, 0, 0,  (int) CROP_ORB_WIDTH, (int) CROP_ORB_HEIGHT, false, false);
        sb.draw(this.getIndicatorArrow(),
                arrowXPos,
                arrowYPos,
                0,
                0,
                ARROW_INDICATOR_WIDTH, ARROW_INDICATOR_HEIGHT,
                this.scale, this.scale,
                0.0F, 0, 0,
                (int) ARROW_INDICATOR_WIDTH, (int) ARROW_INDICATOR_HEIGHT,
                false, false);
      }
    } else {
      this.c = filterColor;
    }

    // Show crop amount indicator pips if any player is hovering over any orb
    if (CropOrbHelper.hasHoveredOrb() && (this.getAmount() > 0 || this.passiveAmount > 0)) {
      Texture indicatorDotEmpty = this.getIndicatorDotEmpty();
      Texture indicatorDotFull = this.getIndicatorDotFull();
      Texture indicatorDotGlowing = this.getIndicatorDotGlowing();
      int maturityThreshold = this.getCrop().getMaturityThreshold();
      float midpointIndex = (maturityThreshold - 1.0F)/ 2.0F;

      for (int i = 0; i < maturityThreshold; i++) {
        float xOffset = CROP_ORB_WIDTH / 2 + indicatorDotFull.getWidth() * (i - midpointIndex - 0.5F) + INDICATOR_DOT_FIXED_OFFSET_X;
        float yOffset = indicatorDotFull.getHeight() + INDICATOR_DOT_FIXED_OFFSET_Y;

        boolean isMatureDot = i == maturityThreshold - 1 && i == getAmount() - 1;

        Texture indicatorDotImage = isMatureDot
                          ? indicatorDotGlowing
                      : (i < this.getAmount() ? indicatorDotFull : indicatorDotEmpty);

        if (isMatureDot) {
          xOffset -= (indicatorDotGlowing.getWidth() - indicatorDotFull.getWidth()) / 2.0F;
          yOffset -= (indicatorDotGlowing.getHeight() - indicatorDotFull.getHeight()) / 2.0F;
          float dX = (indicatorDotGlowing.getWidth() - indicatorDotFull.getWidth()) / 2.0F;
          float dY = (indicatorDotGlowing.getWidth() - indicatorDotFull.getWidth()) / 2.0F;
          ORB_LOGGER.log("xOffset: " + xOffset + ", yOffset: " + -yOffset + " dX: " + dX + ", dY: " + dY);
        }

        ORB_LOGGER.log("drawing " + ( i < this.getAmount() ? "full" : "empty") + " (" + this.name + ") indicator dot at " + xOffset + ", " + yOffset + " (scale: " + Settings.scale + ")");

        sb.draw(indicatorDotImage,
                this.cX - origin_X + xOffset,
                this.cY - origin_Y + yOffset + this.bobEffect.y / 2.0F,
                origin_X, origin_Y,
                indicatorDotImage.getWidth(),
                indicatorDotImage.getHeight(),
                this.scale,
                this.scale,
                0.0F, 0, 0,
                indicatorDotImage.getWidth(), indicatorDotImage.getHeight(),
                false, false);
      }
    }
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
      if (keywordPowerTips == null) {
        keywordPowerTips = new ArrayList<>();

        // main tooltip
        keywordPowerTips.add(new PowerTip(this.name, this.description));
        // crop keyword tooltips
        for (Map.Entry<String,String> entry : getKeywords().entrySet()) {
          keywordPowerTips.add(new PowerTip(TipHelper.capitalize(entry.getKey()), entry.getValue()));
        }
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

    com.evacipated.cardcrawl.mod.stslib.Keyword maturityKeyword
        = TheSimpletonMod.getKeyword("TheSimpletonMod:MaturityKeyword");
    keywords.put(maturityKeyword.PROPER_NAME, maturityKeyword.DESCRIPTION);

    return keywords;
  }

  public void onShuffle() {
    if (!this.isMature(true)) {
      AbstractCrop.stackOrb(this, this.stackAmountOnShuffle, false);
    }
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_DESCRIPTION_ID);
    GENERIC_DESCRIPTION = orbStrings.DESCRIPTION;
  }
}