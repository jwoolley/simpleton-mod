package thesimpleton.patches.game;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.seasons.SeasonalEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public static void Postfix (AbstractDungeon __instance, String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList) {         TheSimpletonMod.logger.debug("AbstractDungeonPatch after called");
        List<String> eventsToRemove = new ArrayList<>();
        if (TheSimpletonMod.isPlayingAsSimpleton()) {
            TheSimpletonMod.logger.debug("AbstractDungeonPatch:::after removing events not in season: "
                + TheSimpletonMod.getSeason());

            TheSimpletonMod.logger.debug("AbstractDungeonPatch::after | in-season  events: "
                + TheSimpletonMod.getSeasonalEventIds().stream().map(c -> c).collect(Collectors.joining(", ")));

            TheSimpletonMod.logger.debug("AbstractDungeonPatch::after | all seasonal events: "
                + SeasonalEvents.getAllSeasonalEventIds().stream().collect(Collectors.joining(", ")));

            AbstractDungeon.eventList.stream()
                .filter(e -> SeasonalEvents.getAllSeasonalEventIds().stream().anyMatch(id -> e.equals(id)))
                .filter(e -> TheSimpletonMod.getSeasonalEventIds().stream().noneMatch(id -> e.equals(id)))
                .forEach(e -> {
                    TheSimpletonMod.logger.debug("AbstractDungeonPatch marking custom event for removal: " + e);
                    eventsToRemove.add(e);
                });
        } else {
            TheSimpletonMod.logger.debug("AbstractDungeonPatch:: unsupported class; removing all custom events.");
            AbstractDungeon.eventList.stream()
                .filter(e -> SeasonalEvents.getAllSeasonalEventIds().stream().anyMatch(id -> e.equals(id)))
                .forEach(e -> {
                    TheSimpletonMod.logger.debug("AbstractDungeonPatch marking custom event for removal: " + e);
                    eventsToRemove.add(e);
                });
        }
        TheSimpletonMod.logger.debug("AbstractDungeonPatch removing " + eventsToRemove.size() + " custom events");
        AbstractDungeon.eventList.removeAll(eventsToRemove);

        TheSimpletonMod.logger.debug("AbstractDungeonPatch::after | remaining events: "
            +  AbstractDungeon.eventList.stream().collect(Collectors.joining(", ")));
    }
}