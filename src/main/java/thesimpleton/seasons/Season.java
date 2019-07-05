package thesimpleton.seasons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.Crop;

import java.util.*;
import java.util.stream.Collectors;

public enum Season {
  WINTER("WinterSeason", Crop.SPINACH),
  SPRING("SpringSeason", Crop.STRAWBERRIES),
  SUMMER("SummerSeason", Crop.CHILIS),
  AUTUMN("AutumnSeason", Crop.ASPARAGUS),
  UNKNOWN("UnknownSeason", null);

  public final String uiName;
  public final String name;
  public final Crop associatedCrop;

  Season(String uiName, Crop crop) {
    this.uiName = uiName;

    final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheSimpletonMod.makeID(uiName));
    name = uiStrings.TEXT[0];
    this.associatedCrop = crop;
  }

  public static Season randomSeason() {
    final int numSeasons = SEASONS.size() - 1;
    final int randomIndex = RANDOM.nextInt(numSeasons);

    TheSimpletonMod.logger.info("Season::randomSeason numSeasons: " + numSeasons + "; randomIndex: " + randomIndex);

    return SEASONS.stream()
        .filter(s -> s != UNKNOWN).collect(Collectors.toList()).get(RANDOM.nextInt(SEASONS.size() - 1)); }

  private static final List<Season> SEASONS = Collections.unmodifiableList(Arrays.asList(values()));
  private static final Random RANDOM = new Random();

  public final static String WINTER_UI_NAME = "WinterSeason";
  public final static String SPRING_UI_NAME = "SpringSeason";
  public final static String SUMMER_UI_NAME = "SummerSeason";
  public final static String AUTUMN_UI_NAME = "AutumnSeason";
  public final static String UNKNOWN_UI_NAME = "UnknownSeason";
}