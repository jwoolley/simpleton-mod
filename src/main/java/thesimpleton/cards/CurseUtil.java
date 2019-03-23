package thesimpleton.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import thesimpleton.cards.attack.SpudMissile;
import thesimpleton.cards.skill.Harvest;
import thesimpleton.cards.curse.Dregs;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import thesimpleton.powers.*;
import thesimpleton.relics.FourLeafCloverCharm;

import java.util.ArrayList;
import java.util.Collections;

public class CurseUtil {
    // As MakeTempCard... actions copy the card object, just define a static Dregs here for common use.
    public static final AbstractCard DREGS = new Dregs();
    public static final AbstractCard SHIV = new Shiv();
    public static final AbstractCard HARVEST = new Harvest();
    public static final AbstractCard SPUD_MISSILE = new SpudMissile();

    public static int getNumCurse(ArrayList<AbstractCard> cards) {
        return (int) cards.stream()
                .filter(card -> card.type == AbstractCard.CardType.CURSE)
                .count();
    }

    public static boolean hasCurse(ArrayList<AbstractCard> cards) {
        return getNumCurse(cards) > 0;
    }

    public static AbstractCropPower getRandomCropPower(AbstractPlayer p, int numStacks) {
        // TODO: move this logic to a plant power manager class
        final PlantPotatoPower potatoPower = new PlantPotatoPower(p, numStacks);
        final PlantSpinachPower spinachPower = new PlantSpinachPower(p, numStacks);
        final PlantOnionPower onionPower = new PlantOnionPower(p, numStacks);

        ArrayList<AbstractCropPower> cropPowers = new ArrayList<>();
        cropPowers.add(potatoPower);
        cropPowers.add(spinachPower);
        cropPowers.add(onionPower);

        Collections.shuffle(cropPowers);
        return cropPowers.get(0);
    }

    public static AbstractCard getRandomCurseCard() {
        if (AbstractDungeon.player.hasRelic(FourLeafCloverCharm.ID)) {
            AbstractDungeon.player.getRelic(FourLeafCloverCharm.ID).flash();
            return DREGS;
        } else {
            return CardLibrary.getCurse();
        }
    }
}
