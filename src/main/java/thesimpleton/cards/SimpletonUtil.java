package thesimpleton.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.attack.FlamingSpud;
import thesimpleton.cards.attack.Gnawberry;
import thesimpleton.cards.attack.SpudMissile;
import thesimpleton.cards.skill.unused.Husk;
import thesimpleton.cards.skill.unused.RootOut;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.crops.AbstractCrop;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpletonUtil {
    public static final AbstractCard SPUD_MISSILE = new SpudMissile();
    public static final AbstractCard FLAMING_SPUD = new FlamingSpud();
    public static final AbstractCard GNAWBERRY = new Gnawberry();

//    public static TheSimpletonCharacter getPlayer() {
//        return (TheSimpletonCharacter) AbstractDungeon.player;
//    }

    public static boolean isPlayerInCombat() {
        return AbstractDungeon.isPlayerInDungeon()
            && AbstractDungeon.currMapNode != null
            && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    public static AbstractMonster getRandomMonster() {
        List<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> !m.isDeadOrEscaped())
                .collect(Collectors.toList());

        Collections.shuffle(monsters);
        if (monsters.size() == 0) {
            return null;
        }

        return monsters.get(0);
    }

    public static List<AbstractMonster> getMonsters() {
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream()
            .filter(m -> !m.isDead && !m.isDying && m.currentHealth > 0)
            .collect(Collectors.toList());
    }

    public static AbstractCreature getDummyCreature() {
        return (new AbstractMonster("Dummy", "Dummy", 1, 0.0F, 0.0F, 1.0F, 1.0F, null) {
            public void damage(DamageInfo di) {}
            public void render(com.badlogic.gdx.graphics.g2d.SpriteBatch spriteBatch) {}
            protected void getMove(int i) {}
            public void takeTurn() {}
        });
    }

    // TODO: ensure that this counts Defect orbs etc. (i.e. orb.ID != null)
    public static List<AbstractOrb> getActiveOrbs() {
        Logger logger = TheSimpletonMod.logger;

        logger.info("SimpletonUtil::getActiveOrbs player has orbs: " + AbstractDungeon.player.orbs.stream().map(orb -> orb.ID).collect(Collectors.joining(", ")));
        return AbstractDungeon.player.orbs.stream().filter(orb -> orb.ID != null && orb.ID != EmptyOrbSlot.ORB_ID).collect(Collectors.toList());
    }

    // TODO: move these harvest functions to CropUtil, move CropUtil to Crop package, and make the called functions
    //       package-scoped
    public static boolean hasHarvestedThisTurn() {
        return AbstractCrop.hasHarvestedThisTurn();
    }

    public static int getNumTimesHarvestedThisTurn() {
        return AbstractCrop.getNumTimesHarvestedThisTurn();
    }
}
