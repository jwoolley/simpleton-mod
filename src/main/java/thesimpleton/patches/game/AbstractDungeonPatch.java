package thesimpleton.patches.game;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.EventHelper;
import thesimpleton.TheSimpletonMod;
import thesimpleton.events.CustomSimpletonEvent;
import thesimpleton.events.CustomSimpletonOnlyEvent;
import thesimpleton.events.SimpletonEventHelper;
import thesimpleton.seasons.SeasonalEvents;
import thesimpleton.utilities.ModLogger;

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
    protected static final ModLogger logger = TheSimpletonMod.traceLogger;
    
    public static void Postfix (AbstractDungeon __instance, String name, String levelId, AbstractPlayer p, ArrayList<String> newSpecialOneTimeEventList) {

      logger.trace("AbstractDungeonPatch::after called. EventList: \n\t"
          + AbstractDungeon.eventList.stream().collect(Collectors.joining("\n\t")));

      logger.trace("AbstractDungeonPatch::after called. specialOneTimeEventList: "
          + AbstractDungeon.specialOneTimeEventList.stream().collect(Collectors.joining(";")));

      List<String> eventsToRemove = new ArrayList<>();
        if (TheSimpletonMod.isPlayingAsSimpleton()) {
            logger.trace("AbstractDungeonPatch::after removing events not in season: "
                + TheSimpletonMod.getSeason());

            logger.trace("AbstractDungeonPatch::after | in-season  events: "
                + TheSimpletonMod.getSeasonalEventIds().stream().map(c -> c).collect(Collectors.joining(", ")));

            logger.trace("AbstractDungeonPatch::after | all seasonal events: "
                + SeasonalEvents.getAllSeasonalEventIds().stream().collect(Collectors.joining(", ")));

            AbstractDungeon.eventList.stream()
                .filter(e -> SeasonalEvents.getAllSeasonalEventIds().stream().anyMatch(id -> e.equals(id)))
                .filter(e -> TheSimpletonMod.getSeasonalEventIds().stream().noneMatch(id -> e.equals(id)))
                .forEach(e -> {
                    logger.trace("AbstractDungeonPatch marking custom event for removal: " + e);
                    eventsToRemove.add(e);
                });
        } else if (TheSimpletonMod.ConfigData.enableEventsForAllCharacters) {
          logger.trace("AbstractDungeonPatch::after event config toggle enabled; removing hayseed-specific custom events.");

          logger.trace("AbstractDungeonPatch::after non-hayseed class; removing hayseed-specific events.: "
              + SimpletonEventHelper.SIMPLETON_ONLY_EVENT_IDS.stream().collect(Collectors.joining("\n\t")));

          SimpletonEventHelper.SIMPLETON_ONLY_EVENT_IDS
              .forEach(e -> {
                logger.trace("AbstractDungeonPatch::after testing event with key: " + e);
              });

          eventsToRemove.addAll(SimpletonEventHelper.SIMPLETON_ONLY_EVENT_IDS);
        } else {
          logger.trace("AbstractDungeonPatch::after event config toggle disabled; removing all custom hayseed events.");

          logger.trace("AbstractDungeonPatch::after unsupported class; removing all custom events: "
            +  SimpletonEventHelper.SIMPLETON_EVENT_IDS.stream().collect(Collectors.joining("\n\t")));

          SimpletonEventHelper.SIMPLETON_EVENT_IDS
              .forEach(e -> {
                logger.trace("AbstractDungeonPatch::after testing event with key: " + e);
              });

          eventsToRemove.addAll(SimpletonEventHelper.SIMPLETON_EVENT_IDS);
        }
        logger.trace("AbstractDungeonPatch removing " + eventsToRemove.size() + " custom events");
        AbstractDungeon.eventList.removeAll(eventsToRemove);

        logger.trace("AbstractDungeonPatch::after | remaining events: "
            +  AbstractDungeon.eventList.stream().collect(Collectors.joining(", ")));
    }
}