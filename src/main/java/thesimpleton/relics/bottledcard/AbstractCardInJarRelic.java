package thesimpleton.relics.bottledcard;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import thesimpleton.TheSimpletonMod;

public class AbstractCardInJarRelic extends CustomRelic {
    public static final String ID = "TheSimpletonMod:CardInJarApotheosis";
    public static final String IMG_PATH = "relics/cardinjar_apotheosis.png";
    public static final String IMG_PATH_LARGE = "relics/cardinjar_apotheosis_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/cardinjar_outline.png";

    private static final AbstractRelic.RelicTier TIER = AbstractRelic.RelicTier.SPECIAL;
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK;

    private AbstractCard card;

    public AbstractCardInJarRelic(String id, String imgPath, String imgPathLarge, AbstractCard card) {
        super(ID, new Texture(TheSimpletonMod.getImageResourcePath(imgPath)),
                new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
        this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(imgPathLarge));
        this.card = card;
    }

    public void atPreBattle() {
        this.flash();
        AbstractCard c = getCardInJar().makeCopy();
        UnlockTracker.markCardAsSeen(c.cardID);
        this.addToBot(new MakeTempCardInHandAction(c));
    }

    private AbstractCard getCardInJar() {
        return card;
    }
}
