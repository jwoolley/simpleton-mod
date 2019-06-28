package thesimpleton.cards.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.utilities.CropOrbHelper;
import thesimpleton.utilities.CropUtil;

public abstract class AbstractDynamicCropOrbHighlighterCard extends AbstractDynamicTextCard {
  public AbstractDynamicCropOrbHighlighterCard(String id, String name, String imgUrl, int cost, String rawDescription,
                                               CardType type, CardColor color, CardRarity rarity, CardTarget target) {
    super(id, name, imgUrl, cost, rawDescription, type, color, rarity, target);
  }

  @Override
  public void hover() {
    super.hover();
    if (AbstractDungeon.isPlayerInDungeon() && AbstractCropOrb.playerHasAnyCropOrbs() && !CropOrbHelper.hasHighlightedOrb()) {
      CropOrbHelper.setHighlightedOrb(this.findCropOrb());
    }
  }

  @Override
  public void unhover() {
    super.unhover();
    CropOrbHelper.clearHighlightedOrb();
  }

  public abstract AbstractCropOrb findCropOrb();
}
