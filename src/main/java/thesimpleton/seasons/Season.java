package thesimpleton.seasons;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import thesimpleton.TheSimpletonMod;

import java.util.*;

public enum Season {
  WINTER("WinterSeason"),
  SPRING("SpringSeason"),
  SUMMER("SummerSeason"),
  AUTUMN("AutumnSeason");

  public final String uiName;
  public final String name;

  Season(String uiName) {
    this.uiName = uiName;

    final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(TheSimpletonMod.makeID(uiName));
    name = uiStrings.TEXT[0];
  }

  public static Season randomSeason() { return SEASONS.get(RANDOM.nextInt(SEASONS.size())); }

  private static final List<Season> SEASONS = Collections.unmodifiableList(Arrays.asList(values()));
  private static final Random RANDOM = new Random();
}