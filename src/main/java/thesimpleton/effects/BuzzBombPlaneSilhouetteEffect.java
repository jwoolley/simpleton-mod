package thesimpleton.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import thesimpleton.TheSimpletonMod;
import thesimpleton.effects.utils.SimpletonVfxHelper;

import java.util.Set;

public class BuzzBombPlaneSilhouetteEffect extends AbstractGameEffect {
    private static final String PLANE_SILHOUETTE_PATH =  SimpletonVfxHelper.getImgFilePath("buzzbomb-effect", "plane-silhouette");

    private static Texture PLANE_SILHOUETTE_IMG;
    private final int planeWidth;
    private final int planeHeight;
    private float _initialX;
    private float _initialY;

    private float _targetX;
    private float _targetY;

    private float _x;
    private float _y;

    public BuzzBombPlaneSilhouetteEffect(float initialX, float initialY, float targetX, float targetY, float duration) {



        this.duration = this.startingDuration = duration;

        this.color = Color.WHITE.cpy();
        this.scale = 1.0F;

        // statically initialize all the textures if needed
        if (PLANE_SILHOUETTE_IMG == null) {
            PLANE_SILHOUETTE_IMG = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(PLANE_SILHOUETTE_PATH));
        }

        planeWidth = PLANE_SILHOUETTE_IMG.getWidth();
        planeHeight = PLANE_SILHOUETTE_IMG.getHeight();

        this._initialX = initialX;
        this._initialY = initialY;
        this._targetX = targetX;
        this._targetY = targetY;


        TheSimpletonMod.traceLogger.log("BuzzBombPlaneSilhouetteEffect parameters "
            + ", initialX: " + initialX
            + ", initialY: " + initialY
            + ", targetX: " + targetX
            + ", targetY: " + targetY
            + ", planeWidth: " + planeWidth
            + ", planeHeight: " + planeHeight
            + " (Settings.WIDTH: " + Settings.WIDTH
            + ", Settings.HEIGHT: " + Settings.HEIGHT
            + ", Settings.xScale: " + Settings.xScale
            + ", Settings.yScale: " + Settings.yScale + ")"
        );
    }

    @Override
    public void update() {

        float t = (this.startingDuration - this.duration) / this.startingDuration;
        float tInterpolated = Interpolation.linear.apply(t);

        this._x = MathUtils.lerp(_initialX - planeWidth * Settings.xScale / 2.0F, _targetX, tInterpolated);
        this._y = MathUtils.lerp(_initialY, _targetY, tInterpolated) - planeHeight * Settings.yScale / 2.0F;

        TheSimpletonMod.traceLogger.log("BuzzBombPlaneSilhouetteEffect update "
            + " _x: " + this._x + ", _y: " + this._y
            + ", t: " + t + ", tInterpolated: " + tInterpolated
        );


        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(PLANE_SILHOUETTE_IMG, this._x, this._y,
                0, 0,
                planeWidth, planeHeight,
                this.scale * Settings.scale, this.scale * Settings.scale,
                this.rotation,
                0, 0,
                planeWidth, planeHeight,
                false, false);
    }

    @Override
    public void dispose() { }
}
