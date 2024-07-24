package thesimpleton.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.effects.utils.SimpletonVfxHelper;

import java.util.ArrayList;
import java.util.List;

public class BiorefinementGlowEffect extends AbstractGameEffect {
    private static final String[] GLOW_EFFECT_PATHS = new String[] {
            SimpletonVfxHelper.getImgFilePath("biorefinement-effect", "biorefinement-glow-1"),
            SimpletonVfxHelper.getImgFilePath("biorefinement-effect", "biorefinement-glow-2"),
            SimpletonVfxHelper.getImgFilePath("biorefinement-effect", "biorefinement-glow-3"),
            SimpletonVfxHelper.getImgFilePath("biorefinement-effect", "biorefinement-glow-4"),
            SimpletonVfxHelper.getImgFilePath("biorefinement-effect", "biorefinement-glow-5")
    };

    private static Texture GLOW_IMG_1;
    private static Texture GLOW_IMG_2;
    private static Texture GLOW_IMG_3;
    private static Texture GLOW_IMG_4;
    private static Texture GLOW_IMG_5;

    private final int glowImageWidth;
    private final int glowImageHeight;

    private final float SINGLE_IMAGE_DISPLAY_DURATION = 0.0667f;

    private final float GLOW_IMG_X_POS = 533; // offset from the left of the screen
    private final float GLOW_IMG_Y_POS = 223; // offset from the bottom of the screen
    private List<Texture> textureSequence;
    private Texture currentTexture;

    public BiorefinementGlowEffect() {
        this.duration = this.startingDuration = duration;

        this.color = Color.WHITE.cpy();

        glowImageWidth = GLOW_IMG_1.getWidth();
        glowImageHeight = GLOW_IMG_1.getHeight();

        textureSequence = new ArrayList<Texture>() {{
            add(GLOW_IMG_1);
            add(GLOW_IMG_2);
            add(GLOW_IMG_3);
            add(GLOW_IMG_4);
            add(GLOW_IMG_5);
            add(GLOW_IMG_4);
            add(GLOW_IMG_3);
            add(GLOW_IMG_4);
            add(GLOW_IMG_5);
            add(GLOW_IMG_3);
            add(GLOW_IMG_2);
            add(GLOW_IMG_1);
        }};

        CardCrawlGame.sound.play("SCI_FI_HUM_1");
    }

    @Override
    public void update() {
        if (this.duration <= 0) {
            if (!textureSequence.isEmpty()) {
                currentTexture = textureSequence.remove(0);
                duration = SINGLE_IMAGE_DISPLAY_DURATION;
            } else {
                this.isDone = true;
                return;
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);

        sb.draw(currentTexture, GLOW_IMG_X_POS, GLOW_IMG_Y_POS,
                0, 0,
                glowImageWidth, glowImageHeight,
                1.0f, 1.0f,//this.scale, this.scale,
                this.rotation,
                0, 0,
                glowImageWidth, glowImageHeight,
                false, false);
    }

    @Override
    public void dispose() { }

    static {
        // statically initialize all the textures if needed
        if (GLOW_IMG_1 == null) {
            GLOW_IMG_1 = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(GLOW_EFFECT_PATHS[0]));
        }
        if (GLOW_IMG_2 == null) {
            GLOW_IMG_2 = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(GLOW_EFFECT_PATHS[1]));
        }
        if (GLOW_IMG_3 == null) {
            GLOW_IMG_3 = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(GLOW_EFFECT_PATHS[2]));
        }
        if (GLOW_IMG_4 == null) {
            GLOW_IMG_4 = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(GLOW_EFFECT_PATHS[3]));
        }
        if (GLOW_IMG_5 == null) {
            GLOW_IMG_5 = TheSimpletonMod.loadTexture(TheSimpletonMod.getImageResourcePath(GLOW_EFFECT_PATHS[4]));
        }
    }
}
