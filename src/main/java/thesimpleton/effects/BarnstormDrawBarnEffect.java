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

public class BarnstormDrawBarnEffect extends AbstractGameEffect {
    private float _initialX;
    private float _initialY;

    private float _targetX;
    private float _targetY;

    private AbstractCreature target;

    private float _x;
    private float _y;

    private static final String BARN_UNDERLAY_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barn-underlay");

    private static final String BARN_OVERLAY_IMG_PATH =  SimpletonVfxHelper.getImgFilePath("barnstorm-effect", "barn-overlay");

    private static Texture barnUnderlayImage;

    private static Texture barnOverlayImage;


    private final int barnWidth;
    private final int barnHeight;

    public BarnstormDrawBarnEffect(float initialX, float initialY, float duration) {
        this.duration = this.startingDuration = duration;

        this.color = Color.WHITE.cpy();

        // statically initialize the textures
        if (barnUnderlayImage == null) {
            barnUnderlayImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(BARN_UNDERLAY_IMG_PATH));
        } if (barnOverlayImage == null) {
            barnOverlayImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(BARN_OVERLAY_IMG_PATH));
        }

        barnWidth = barnOverlayImage.getWidth();
        barnHeight = barnOverlayImage.getHeight();

        this._initialX = initialX;
        this._initialY = initialY;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F || AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);

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
    }


    @Override
    public void dispose() { }
}