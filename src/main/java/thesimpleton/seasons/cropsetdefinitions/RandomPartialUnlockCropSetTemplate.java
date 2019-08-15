package thesimpleton.seasons.cropsetdefinitions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
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

        Logger logger = TheSimpletonMod.logger;
        logger.debug(this.getClass().getSimpleName()
                + "::RandomPartialUnlockCropSetTemplate constructor called. season: " + season + "; number of otherTemplates" + otherTemplates.size());
        this.season = season;

        this.commonCrops = crops.stream().filter(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.BASIC)
                .distinct()
                .collect(Collectors.toList());
        this.uncommonCrops = crops.stream().filter(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.COMMON)
                .distinct()
                .collect(Collectors.toList());
        this.rareCrops = crops.stream().filter(c -> c.getCropInfo().rarity == AbstractCard.CardRarity.UNCOMMON)
                .distinct()
                .collect(Collectors.toList());

//        if (commonCrops.size() == 0) {
//            throw new IllegalStateException("Unable to initialize " + this.getClass().getSimpleName()
//                    + ": no common crops");
//        }
//        if (uncommonCrops.size() == 0) {
//            throw new IllegalStateException("Unable to initialize " + this.getClass().getSimpleName()
//                    + ": no uncommon crops");
//        }
        if (this.rareCrops.size() == 0) {
            throw new IllegalStateException("Unable to initialize " + this.getClass().getSimpleName()
                    + ": no rare crops");
        }

        this.eligibleTemplates = new ArrayList<>();
        this.eligibleTemplates.add(this);

        for (RandomPartialUnlockCropSetTemplate template : otherTemplates) {
            logger.debug(this.getClass().getSimpleName()
                    + "::RandomPartialUnlockCropSetTemplate integrating otherTemplate with season: " + template.season);

            logger.debug(this.getClass().getSimpleName()
                    + "::RandomPartialUnlockCropSetTemplate integrating otherTemplate: adding: " + template.commonCrops.size() + " common crops");

            logger.debug(this.getClass().getSimpleName()
                    + "::RandomPartialUnlockCropSetTemplate integrating otherTemplate: adding: " + template.uncommonCrops.size() + " uncommon crops");

            this.commonCrops.addAll(template.commonCrops);
            this.uncommonCrops.addAll(template.uncommonCrops);
            this.eligibleTemplates.add(template);
        }

        logger.debug(this.getClass().getSimpleName()
                + "::RandomPartialUnlockCropSetTemplate integrating otherTemplate. total eligible templates: "
                + this.eligibleTemplates.size());
    }
    // TODO: initialize SeasonInfo using this
    public RandomPartialUnlockCropSetDefinition getRandomCropSetDefinition() {
        Logger logger = TheSimpletonMod.logger;
//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition called");
//
//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition selecting template from "
//                + this.eligibleTemplates.size() + " eligibleTemplates");
//
//
//
//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition eligible templates by season: "
//            + this.eligibleTemplates.stream().map(c -> c.season.name).collect(Collectors.joining(", ")));
//
//
//        logger.debug(this.getClass().getSimpleName()
//                        + "::getRandomCropSetDefinition RANDOM.nextInt(this.eligibleTemplates.size()): "
//                        + RANDOM.nextInt(this.eligibleTemplates.size()));
//
//        logger.debug(this.getClass().getSimpleName()
//                + "::getRandomCropSetDefinition RANDOM.nextInt(this.eligibleTemplates.size()): "
//                + RANDOM.nextInt(this.eligibleTemplates.size()));
//
//        logger.debug(this.getClass().getSimpleName()
//                + "::getRandomCropSetDefinition RANDOM.nextInt(this.eligibleTemplates.size()): "
//                + RANDOM.nextInt(this.eligibleTemplates.size()));
//
//        logger.debug(this.getClass().getSimpleName()
//                + "::getRandomCropSetDefinition RANDOM.nextInt(this.eligibleTemplates.size()): "
//                + RANDOM.nextInt(this.eligibleTemplates.size()));
//
//        logger.debug(this.getClass().getSimpleName()
//                + "::getRandomCropSetDefinition RANDOM.nextInt(this.eligibleTemplates.size()): "
//                + RANDOM.nextInt(this.eligibleTemplates.size()));

        final int seasonIndex = RANDOM.nextInt(this.eligibleTemplates.size());

        RandomPartialUnlockCropSetTemplate selectedTemplate = this.eligibleTemplates.get(seasonIndex);

//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition selected template with season:"
//            + selectedTemplate.season + " using index: " + seasonIndex);
//
//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition commonCrops: " +
//                this.commonCrops.stream().map(c -> c.name()).collect(Collectors.joining(", ")));
//
//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition selecting common from "
//                + this.commonCrops.size() + " common crops");
//
//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition selecting uncommon from "
//                + this.uncommonCrops.size() + " uncommon crops");
//
//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition uncommonCrops: " +
//                this.uncommonCrops.stream().map(c -> c.name()).collect(Collectors.joining(", ")));
//
//
//        logger.debug(this.getClass().getSimpleName() + "::getRandomCropSetDefinition selecting rare from "
//                + selectedTemplate.rareCrops.size() + " rare crops");

        List<Crop> cropSet = new ArrayList<>();
        cropSet.add(this.commonCrops.get(RANDOM.nextInt(this.commonCrops.size())));
        cropSet.add(this.uncommonCrops.get(RANDOM.nextInt(this.uncommonCrops.size())));
        cropSet.add(selectedTemplate.rareCrops.get(0));

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
