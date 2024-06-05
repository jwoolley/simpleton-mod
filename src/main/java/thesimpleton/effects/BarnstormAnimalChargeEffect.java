package thesimpleton.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.BarnstormAction;
import thesimpleton.effects.utils.SimpletonVfxHelper;

public class BarnstormAnimalChargeEffect extends AbstractGameEffect {
    private static float INITIAL_WAIT_DURATION = 0.0F; // Settings.ACTION_DUR_XFAST;
    private static float ANIMAL_CHARGE_DURATION = 0.33F; // Settings.ACTION_DUR_MED;
    private static float INITIAL_WAIT_DURATION_FAST = 0.0F; // Settings.ACTION_DUR_XFAST;
    private static float ANIMAL_CHARGE_DURATION_FAST =  0.1F;

    private float _initialX;
    private float _initialY;

    private float _targetX;
    private float _targetY;

    private AbstractCreature target;

    private float _x;
    private float _y;

    private static final String BARN_UNDERLAY_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barn-underlay");

    private static final String BARN_OVERLAY_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barn-overlay");

    private static final String CHICKEN_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barnanimal-chicken");

    private static final String COW_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barnanimal-bull");

    private static final String PIG_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barnanimal-pig");

    private static final String SHEEP_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barnanimal-sheep");

    private static final String CHICKEN_SFX_PATH ="ANIMAL_CHICKEN_CLUCK_1";

    private static final String COW_SFX_PATH =  "ANIMAL_COW_MOO_1";

    private static final String PIG_SFX_PATH =  "ANIMAL_PIG_OINK_1";

    private static final String SHEEP_SFX_PATH ="ANIMAL_SHEEP_BAA_1";

    private static Texture barnUnderlayImage;

    private static Texture barnOverlayImage;

    private final Texture animalImage;
    private final String animalSfxKey;

    private final int barnWidth;
    private final int barnHeight;

    private final int animalWidth;
    private final int animalHeight;

    private final float animalOffsetX = 0;
    private final float animalOffsetY;

    private static Texture IMG_STATIC_ANIMAL_CHICKEN;
    private static Texture IMG_STATIC_ANIMAL_COW;
    private static Texture IMG_STATIC_ANIMAL_PIG;
    private static Texture IMG_STATIC_ANIMAL_SHEEP;

    private static void loadStaticTextures() {
        barnUnderlayImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(BARN_UNDERLAY_IMG_PATH));
        barnOverlayImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(BARN_OVERLAY_IMG_PATH));
        IMG_STATIC_ANIMAL_CHICKEN = TheSimpletonMod.loadTexture((TheSimpletonMod.getImageResourcePath(CHICKEN_IMG_PATH)));
        IMG_STATIC_ANIMAL_COW = TheSimpletonMod.loadTexture((TheSimpletonMod.getImageResourcePath(COW_IMG_PATH)));
        IMG_STATIC_ANIMAL_PIG = TheSimpletonMod.loadTexture((TheSimpletonMod.getImageResourcePath(PIG_IMG_PATH)));
        IMG_STATIC_ANIMAL_SHEEP = TheSimpletonMod.loadTexture((TheSimpletonMod.getImageResourcePath(SHEEP_IMG_PATH)));
    }

    public BarnstormAnimalChargeEffect(float initialX, float initialY, AbstractCreature target, BarnstormAction.BarnstormAnimal animal, float animalOffsetY) {
        this.duration = this.startingDuration = getInitialWaitDuration() + getAnimalChargeDuration();

        this.color = Color.WHITE.cpy();
        this.scale = 1.0F;

        // statically initialize all the textures if needed
        if (barnUnderlayImage == null || barnOverlayImage == null
        || IMG_STATIC_ANIMAL_CHICKEN == null || IMG_STATIC_ANIMAL_COW == null
        || IMG_STATIC_ANIMAL_PIG == null || IMG_STATIC_ANIMAL_SHEEP == null) {
            loadStaticTextures();
        }

        barnWidth = barnOverlayImage.getWidth();
        barnHeight = barnOverlayImage.getHeight();

        switch (animal) {
            case CHICKEN:
                animalImage = IMG_STATIC_ANIMAL_CHICKEN;
                animalSfxKey = CHICKEN_SFX_PATH;
                break;
            case COW:
                animalImage = IMG_STATIC_ANIMAL_COW;
                animalSfxKey = COW_SFX_PATH;
                break;
            case PIG:
                animalImage = IMG_STATIC_ANIMAL_PIG;
                animalSfxKey = PIG_SFX_PATH;
                break;
            case SHEEP:
            default:
                animalImage = IMG_STATIC_ANIMAL_SHEEP;
                animalSfxKey = SHEEP_SFX_PATH;
                break;
        }

        animalWidth = animalImage.getWidth();
        animalHeight = animalImage.getHeight();
        this.animalOffsetY = animalOffsetY;
        this._initialX = initialX;
        this._initialY = initialY;
        this._targetX = target.drawX - target.hb_w / 2.0F - animalWidth;
        this._targetY = target.drawY; //  initialY;
        this.target = target;
    }

    private boolean isAnimalCharging = false;
    @Override
    public void update() {

        if (this.duration < this.startingDuration - getInitialWaitDuration()) {
            if (!isAnimalCharging) {
                AbstractDungeon.actionManager.addToTop(new SFXAction(animalSfxKey));
                isAnimalCharging = true;
            }

            float t = ((this.startingDuration - getInitialWaitDuration()) - this.duration) / this.startingDuration;
            float tInterpolated = Interpolation.swing.apply(t);

            float animalInitialX = _initialX + animalOffsetX * Settings.scale;
            float animalInitialY = _initialY + animalOffsetY * Settings.scale;
            this._x = MathUtils.lerp(animalInitialX, _targetX, Interpolation.linear.apply(tInterpolated));
            this._y = MathUtils.lerp(animalInitialY, _targetY, Interpolation.exp5.apply(t));
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            isAnimalCharging = false;
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        if (isAnimalCharging) {

            sb.draw(barnUnderlayImage,
                    this._initialX,
                    this._initialY,
                    0, 0, // barnWidth/2f, barnHeight/2f,
                    barnWidth, barnHeight,
                    Settings.xScale, Settings.yScale,
                    this.rotation,
                    0, 0,
                    barnWidth, barnHeight,
                    false, false);

            sb.draw(animalImage, this._x, this._y,
                    0, 0,
                    animalWidth, animalHeight,   // 0.0F, 0.0F)
                    this.scale * Settings.scale, this.scale * Settings.scale,
                    this.rotation,
                    0, 0,
                    animalWidth, animalHeight,
                    false, false);

            sb.draw(barnOverlayImage,
                    this._initialX,
                    this._initialY,
                    0, 0, // barnWidth/2f, barnHeight/2f,
                    barnWidth, barnHeight,
                    Settings.xScale, Settings.yScale,
                    this.rotation,
                    0, 0,
                    barnWidth, barnHeight,
                    false, false);
        }
    }

    private float getInitialWaitDuration() {
        return Settings.FAST_MODE ? INITIAL_WAIT_DURATION_FAST  : INITIAL_WAIT_DURATION;
    }

    private float getAnimalChargeDuration() {
        return Settings.FAST_MODE ? ANIMAL_CHARGE_DURATION_FAST  : ANIMAL_CHARGE_DURATION;
    }

    @Override
    public void dispose() { }
}