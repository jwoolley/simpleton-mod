package thesimpleton.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.cards.attack.FlamingSpud;
import thesimpleton.cards.attack.SpudMissile;
import thesimpleton.cards.curse.Dregs;
import thesimpleton.cards.skill.Husk;
import thesimpleton.cards.skill.RootOut;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.powers.*;
import thesimpleton.relics.unused.FourLeafCloverCharm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpletonUtil {
    // As MakeTempCard... actions copy the card object, just define a static Dregs here for common use.
    public static final AbstractCard DREGS = new Dregs();
    public static final Boolean DREGS_ENABLED = false;
    public static final AbstractCard SPUD_MISSILE = new SpudMissile();
    public static final AbstractCard FLAMING_SPUD = new FlamingSpud();
    public static final AbstractCard HUSK = new Husk();
    public static final AbstractCard ROOT_OUT = new RootOut();

    public static TheSimpletonCharacter getPlayer() {
        return (TheSimpletonCharacter) AbstractDungeon.player;
    }

    public static int getNumCurse(ArrayList<AbstractCard> cards) {
        return (int) cards.stream()
                .filter(card -> card.type == AbstractCard.CardType.CURSE)
                .count();
    }

    public static boolean hasCurse(ArrayList<AbstractCard> cards) {
        return getNumCurse(cards) > 0;
    }

    public static boolean isPlayerInCombat() {
        return AbstractDungeon.isPlayerInDungeon()
            && AbstractDungeon.currMapNode != null
            && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    public static AbstractCard getRandomCurseCard() {
        if (DREGS_ENABLED && AbstractDungeon.player.hasRelic(FourLeafCloverCharm.ID)) {
            AbstractDungeon.player.getRelic(FourLeafCloverCharm.ID).flash();
            return new Dregs();
        } else {
            return CardLibrary.getCurse();
        }
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

    public static boolean hasHarvestedThisTurn() {
        return AbstractCropPower.hasHarvestedThisTurn();
    }
}
