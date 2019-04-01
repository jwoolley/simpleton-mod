package thesimpleton.relics.unused;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.unused.ApplyBleedAction;

public class Tack extends CustomRelic {
    public static final String ID = "TheSimpletonMod:Tack";
    public static final String IMG_PATH = "relics/tack.png";
    public static final String IMG_PATH_LARGE = "relics/tack_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/tack_outline.png";

    private static final RelicTier TIER = RelicTier.COMMON;
    private static final LandingSound SOUND = LandingSound.CLINK;

    private static final int BLEED_AMOUNT = 4;

    public Tack() {
        super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
                new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
        this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + BLEED_AMOUNT + this.DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> !m.isDead && !m.isDying)
                .forEach(m -> AbstractDungeon.actionManager.addToBottom(
                        new ApplyBleedAction(m, AbstractDungeon.player, BLEED_AMOUNT)));
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Tack();
    }

}
