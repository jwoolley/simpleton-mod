package thesimpleton.seasons;

import thesimpleton.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SeasonalEvents {
    public final List<String> exordiumEvents;
    public final List<String> cityEvents;
    public final List<String> beyondEvents;
    public final List<String> globalEvents;

    public SeasonalEvents(List<String> exordiumEvents, List<String> cityEvents,
                          List<String> beyondEvents) {
        this(exordiumEvents, cityEvents, beyondEvents, Collections.emptyList());
    }

    public SeasonalEvents(List<String> exordiumEvents, List<String> cityEvents,
                          List<String> beyondEvents, List<String> globalEvents) {
        this.exordiumEvents = Collections.unmodifiableList(exordiumEvents);
        this.cityEvents = Collections.unmodifiableList(cityEvents);
        this.beyondEvents = Collections.unmodifiableList(beyondEvents);
        this.globalEvents = Collections.unmodifiableList(globalEvents);
    }


    public static SeasonalEvents getSeasonalEvents(Season season) {
        switch (season) {
            case AUTUMN:
                return AUTUMN_EVENTS;
            case WINTER:
                return WINTER_EVENTS;
            case SPRING:
                return SPRING_EVENTS;
            case SUMMER:
                return SUMMER_EVENTS;
            default:
                return null;

        }
    }

    public List<String> getSeasonalEventIds() {
        List<String> ids = new ArrayList<>();
        ids.addAll(this.exordiumEvents);
        ids.addAll(this.cityEvents);
        ids.addAll(this.beyondEvents);
        ids.addAll(this.globalEvents);
        return ids;
    }


    public static List<String> getAllSeasonalEventIds() {
        return Collections.unmodifiableList(ALL_SEASONAL_EVENT_IDS);
    }

    static private final SeasonalEvents AUTUMN_EVENTS;
    static private final SeasonalEvents WINTER_EVENTS;
    static private final SeasonalEvents SPRING_EVENTS;
    static private final SeasonalEvents SUMMER_EVENTS;
    static private final SeasonalEvents ALL_SEASONAL_EVENTS;

    static private final List<String> ALL_SEASONAL_EVENT_IDS;

    static {
        AUTUMN_EVENTS = new SeasonalEvents(
                Arrays.asList(ReaptideEvent.ID), Collections.emptyList(), Collections.emptyList());

        WINTER_EVENTS = new SeasonalEvents(
                Arrays.asList(SnowedInEvent.ID), Arrays.asList(BorealisEvent.ID), Collections.emptyList());

        SPRING_EVENTS = new SeasonalEvents(
                Arrays.asList(EarlyThawEvent.ID), Collections.emptyList(), Collections.emptyList());

        SUMMER_EVENTS = new SeasonalEvents(
                Arrays.asList(FirefliesEvent.ID), Arrays.asList(HeatWaveEvent.ID), Collections.emptyList());

        List<String> allExordiumSeasonalEvents = new ArrayList<>();
        allExordiumSeasonalEvents.addAll(AUTUMN_EVENTS.exordiumEvents);
        allExordiumSeasonalEvents.addAll(WINTER_EVENTS.exordiumEvents);
        allExordiumSeasonalEvents.addAll(SPRING_EVENTS.exordiumEvents);
        allExordiumSeasonalEvents.addAll(SUMMER_EVENTS.exordiumEvents);
        List<String> allCitySeasonalEvents = new ArrayList<>();
        allCitySeasonalEvents.addAll(AUTUMN_EVENTS.cityEvents);
        allCitySeasonalEvents.addAll(WINTER_EVENTS.cityEvents);
        allCitySeasonalEvents.addAll(SPRING_EVENTS.cityEvents);
        allCitySeasonalEvents.addAll(SUMMER_EVENTS.cityEvents);
        List<String> allBeyondSeasonalEvents = new ArrayList<>();
        allBeyondSeasonalEvents.addAll(AUTUMN_EVENTS.beyondEvents);
        allBeyondSeasonalEvents.addAll(WINTER_EVENTS.beyondEvents);
        allBeyondSeasonalEvents.addAll(SPRING_EVENTS.beyondEvents);
        allBeyondSeasonalEvents.addAll(SUMMER_EVENTS.beyondEvents);
        List<String> allGlobalSeasonalEvents = new ArrayList<>();
        allGlobalSeasonalEvents.addAll(AUTUMN_EVENTS.globalEvents);
        allGlobalSeasonalEvents.addAll(WINTER_EVENTS.globalEvents);
        allGlobalSeasonalEvents.addAll(SPRING_EVENTS.globalEvents);
        allGlobalSeasonalEvents.addAll(SUMMER_EVENTS.globalEvents);

        ALL_SEASONAL_EVENTS = new SeasonalEvents(
                allExordiumSeasonalEvents, allCitySeasonalEvents, allBeyondSeasonalEvents,
                allGlobalSeasonalEvents);

        ALL_SEASONAL_EVENT_IDS = new ArrayList<>();
        ALL_SEASONAL_EVENT_IDS.addAll(allExordiumSeasonalEvents);
        ALL_SEASONAL_EVENT_IDS.addAll(allCitySeasonalEvents);
        ALL_SEASONAL_EVENT_IDS.addAll(allBeyondSeasonalEvents);
        ALL_SEASONAL_EVENT_IDS.addAll(allGlobalSeasonalEvents);
    }
}
