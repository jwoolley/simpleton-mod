package thesimpleton.patches.game;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.seasons.SeasonalEvents;

import java.util.ArrayList;
import java.util.List;

@SpirePatch(
    clz = AbstractDungeon.class,
    method = SpirePatch.CONSTRUCTOR,
    paramtypez = {
        String.class,
        String.class,
        AbstractPlayer.class,
        ArrayList.class
    }
)

public class AbstractDungeonPatch {
    public static void Postfix (AbstractDungeon __instance, String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList) {
        TheSimpletonMod.logger.info("AbstractDungeonPatch after called");

        List<String> eventsToRemove = new ArrayList<>();

        TheSimpletonMod.logger.info("AbstractDungeonPatch:::after removing events not in season: "
                + TheSimpletonMod.getSeason());

        AbstractDungeon.eventList.stream()
            .filter(e -> SeasonalEvents.getAllSeasonalEventIds().stream().anyMatch(id -> e.equals(id)))
            .filter(e -> TheSimpletonMod.getSeasonalEventIds().stream().noneMatch(id -> e.equals(id)))
            .forEach(e -> {
                TheSimpletonMod.logger.info("AbstractDungeonPatch marking custom event for removal: " + e);
                eventsToRemove.add(e);
            });

        TheSimpletonMod.logger.info("AbstractDungeonPatch removing " + eventsToRemove.size() + " custom events");
        AbstractDungeon.eventList.removeAll(eventsToRemove);
    }
}