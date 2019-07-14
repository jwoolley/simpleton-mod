package thesimpleton.seasons.cropsetdefinitions;

import thesimpleton.crops.Crop;
import thesimpleton.seasons.Season;

import java.util.List;

public interface SingleCropSetDefinition {
    Season getSeason();
    List<Crop> getCropSet();
}