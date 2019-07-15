package thesimpleton.seasons.cropsetdefinitions;

import thesimpleton.crops.Crop;
import thesimpleton.seasons.Season;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomPartialUnlockCropSetDefinition implements SingleCropSetDefinition {

    private final Season season;
    private final List<Crop> crops;

    public RandomPartialUnlockCropSetDefinition(Season season, List<Crop> crops) {
        this.season = season;
        this.crops = new ArrayList<>(crops);
    }

    public Season getSeason() {
        return season;
    }

    public List<Crop> getCropSet() {
        return Collections.unmodifiableList(this.crops);
    }
}
