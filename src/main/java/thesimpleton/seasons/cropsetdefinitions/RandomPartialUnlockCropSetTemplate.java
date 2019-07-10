package thesimpleton.seasons.cropsetdefinitions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import thesimpleton.crops.Crop;
import thesimpleton.seasons.Season;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomPartialUnlockCropSetTemplate {
    private static final Random RANDOM = new Random();

    public static RandomPartialUnlockCropSetTemplate PARTIAL_UNLOCK_CROP_SET_1;
    public static RandomPartialUnlockCropSetTemplate PARTIAL_UNLOCK_CROP_SET_2;
    public static RandomPartialUnlockCropSetTemplate PARTIAL_UNLOCK_CROP_SET_3;

    private final Season season;
    private final List<Crop> commonCrops;
    private final List<Crop> uncommonCrops;
    private final List<Crop> rareCrops;
    private final List<RandomPartialUnlockCropSetTemplate> eligibleTemplates;

    public RandomPartialUnlockCropSetTemplate(Season season, List<Crop> crops) {
        this(season, crops, new ArrayList<>());
    }

    public RandomPartialUnlockCropSetTemplate(Season season, List<Crop> crops,
                                              List<RandomPartialUnlockCropSetTemplate> otherTemplates) {

        this.season = season;

        commonCrops = crops.stream().filter(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.BASIC)
                .collect(Collectors.toList());
        uncommonCrops = crops.stream().filter(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.COMMON)
                .collect(Collectors.toList());
        rareCrops = crops.stream().filter(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.UNCOMMON)
                .collect(Collectors.toList());

        if (commonCrops.size() == 0) {
            throw new IllegalStateException("Unable to initialize " + this.getClass().getSimpleName()
                    + ": no common crops");
        }
        if (uncommonCrops.size() == 0) {
            throw new IllegalStateException("Unable to initialize " + this.getClass().getSimpleName()
                    + ": no uncommon crops");
        }
        if (rareCrops.size() == 0) {
            throw new IllegalStateException("Unable to initialize " + this.getClass().getSimpleName()
                    + ": no rare crops");
        }

        this.eligibleTemplates = new ArrayList<>();
        eligibleTemplates.add(this);

        for (RandomPartialUnlockCropSetTemplate template : otherTemplates) {
            commonCrops.addAll(template.commonCrops);
            uncommonCrops.addAll(template.uncommonCrops);
            eligibleTemplates.add(template);
        }
    }
    // TODO: initialize SeasonInfo using this
    public RandomPartialUnlockCropSetDefinition getRandomCropSetDefinition() {
        RandomPartialUnlockCropSetTemplate selectedTemplate =
                this.eligibleTemplates.get(RANDOM.nextInt(this.eligibleTemplates.size() -1));

        List<Crop> cropSet = new ArrayList<>();
        cropSet.add(selectedTemplate.rareCrops.get(0));
        cropSet.add(selectedTemplate.commonCrops.get(RANDOM.nextInt(selectedTemplate.commonCrops.size() -1)));
        cropSet.add(selectedTemplate.uncommonCrops.get(RANDOM.nextInt(selectedTemplate.uncommonCrops.size() -1)));
        return new RandomPartialUnlockCropSetDefinition(selectedTemplate.season, cropSet);
    }


    static {
        //TODO: reference UnlockableSeasonCropSetDefinitions instead of redefining here

        RandomPartialUnlockCropSetTemplate PARTIAL_UNLOCK_CROP_SET_0 = new RandomPartialUnlockCropSetTemplate(
            UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_0.getSeason(),
            UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_0.getAllCrops());

        PARTIAL_UNLOCK_CROP_SET_1 = new RandomPartialUnlockCropSetTemplate(
            UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_1.getSeason(),
            UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_1.getAllCrops(),
            Arrays.asList(PARTIAL_UNLOCK_CROP_SET_0));


        PARTIAL_UNLOCK_CROP_SET_2 = new RandomPartialUnlockCropSetTemplate(
            UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_2.getSeason(),
            UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_2.getAllCrops(),
            Arrays.asList(PARTIAL_UNLOCK_CROP_SET_0, PARTIAL_UNLOCK_CROP_SET_1));

        PARTIAL_UNLOCK_CROP_SET_3 = new RandomPartialUnlockCropSetTemplate(
            UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_3.getSeason(),
            UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_3.getAllCrops(),
            Arrays.asList(PARTIAL_UNLOCK_CROP_SET_0, PARTIAL_UNLOCK_CROP_SET_1, PARTIAL_UNLOCK_CROP_SET_2));
    }
}
