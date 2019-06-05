package thesimpleton.seasons;

import java.util.*;

public enum Season {
  WINTER,
  SPRING,
  SUMMER,
  FALL;

  public static Season randomSeason() { return SEASONS.get(RANDOM.nextInt(SEASONS.size())); }

  private static final List<Season> SEASONS = Collections.unmodifiableList(Arrays.asList(values()));
  private static final Random RANDOM = new Random();
}
