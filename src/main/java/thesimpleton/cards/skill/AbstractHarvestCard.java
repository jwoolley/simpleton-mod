package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.enums.AbstractCardEnum;

public abstract class AbstractHarvestCard extends CustomCard {
//  public final int harvestAmount;
  public final boolean harvestAll;
  public final boolean autoHarvest;

  public AbstractHarvestCard(
      String id, String name, String imagePath, int cost, String description, CardType type,
      CardRarity rarity, CardTarget target, int harvestAmount, boolean harvestAll, boolean autoHarvest) {
    super(id, name, imagePath, cost, description, type, AbstractCardEnum.THE_SIMPLETON_BLUE, rarity, target);
    this.baseMagicNumber = this.magicNumber = harvestAmount;
    this.harvestAll = harvestAll;
    this.autoHarvest = autoHarvest;
    this.tags.add(TheSimpletonCardTags.HARVEST);
  }

  public int getHarvestAmount() {
    return this.magicNumber;
  }

  public boolean isHarvestAll() {
    return harvestAll;
  }
}