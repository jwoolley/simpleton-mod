package thesimpleton.actions;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import thesimpleton.orbs.AbstractCrop;


public class CropSpawnAction extends AbstractGameAction {
    private AbstractOrb orbType;

    public CropSpawnAction(AbstractOrb newOrbType) {
        this.duration = Settings.ACTION_DUR_FAST;
        if (newOrbType != null) {
            this.orbType = newOrbType;
        }
    }


    public void update() {
        if (AbstractDungeon.player.maxOrbs > 0) {
            AbstractDungeon.player.channelOrb(this.orbType);
            tickDuration();
        }
        this.isDone = true;
    }

}



