package thesimpleton.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.cards.attack.SpudMissile;
import thesimpleton.cards.curse.Dregs;
import thesimpleton.cards.skill.RootOut;
import thesimpleton.powers.*;
import thesimpleton.relics.FourLeafCloverCharm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SimpletonUtil {
    // As MakeTempCard... actions copy the card object, just define a static Dregs here for common use.
    public static final AbstractCard DREGS = new Dregs();
    public static final AbstractCard SPUD_MISSILE = new SpudMissile();
    public static final AbstractCard ROOT_OUT = new RootOut();

    public static int getNumCurse(ArrayList<AbstractCard> cards) {
        return (int) cards.stream()
                .filter(card -> card.type == AbstractCard.CardType.CURSE)
                .count();
    }

    public static boolean hasCurse(ArrayList<AbstractCard> cards) {
        return getNumCurse(cards) > 0;
    }


    public static AbstractCard getRandomCurseCard() {
        if (AbstractDungeon.player.hasRelic(FourLeafCloverCharm.ID)) {
            AbstractDungeon.player.getRelic(FourLeafCloverCharm.ID).flash();
            return DREGS;
        } else {
            return CardLibrary.getCurse();
        }
    }

    public static AbstractMonster getRandomMonster() {
        List<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters.stream()
                .filter(m -> !m.isDead && !m.isDying)
                .collect(Collectors.toList());

        Collections.shuffle(monsters);
        if (monsters.size() == 0) {
            return null;
        }

        return monsters.get(0);
    }
}
