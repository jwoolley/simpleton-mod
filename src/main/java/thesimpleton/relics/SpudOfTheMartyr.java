package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;

public class SpudOfTheMartyr extends CustomRelic {
    public static final String ID = "TheSimpletonMod:SpudOfTheMartyr";
    public static final String IMG_PATH = "relics/spudofthemartyr.png";
    public static final String IMG_PATH_LARGE = "relics/spudofthemartyr_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/spudofthemartyr_outline.png";

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.HEAVY;


    public static final int STR_PER_TRIGGER = 1;

    private static final Logger logger = TheSimpletonMod.logger;

    public SpudOfTheMartyr() {
        super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
                new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
        this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
    }


    public AbstractRelic makeCopy() {
        return new SpudOfTheMartyr();
    }

    public String getUpdatedDescription(){
        return this.DESCRIPTIONS[0] + STR_PER_TRIGGER + DESCRIPTIONS[1];
    }

    public void onLoseHp(int damageAmount) {
        if (SimpletonUtil.isPlayerInCombat()) {
            this.flash();
            if (this.counter < 0) {
                this.counter = 1;
            } else {
                this.counter++;
            }
        }
    }

    @Override
    public void atBattleStart() {
        this.counter = -1;
    }

    @Override
    public void atTurnStart() {
        AbstractPlayer player = AbstractDungeon.player;
        if (this.counter > 0) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_FAST_2"));
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(player, player, new StrengthPower(player, this.counter)));
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(player, player, new LoseStrengthPower(player, this.counter)));

            this.counter = -1;
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public void updateDescription(AbstractPlayer.PlayerClass c) {
        this.description = getUpdatedDescription();
        super.updateDescription(c);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }
}
