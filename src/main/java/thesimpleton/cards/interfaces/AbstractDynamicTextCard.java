package thesimpleton.cards.interfaces;

import basemod.abstracts.CustomCard;
import thesimpleton.cards.SimpletonUtil;

abstract public class AbstractDynamicTextCard extends CustomCard {
  public AbstractDynamicTextCard(String id, String name, String imgUrl, int cost,
                                 String rawDescription, CardType type, CardColor color,
                                 CardRarity rarity, CardTarget target) {
    super(id, name, imgUrl, cost, rawDescription, type, color, rarity, target);
  }


  @Override
  public void hover() {
    updateDescription(SimpletonUtil.isPlayerInCombat());
//    super.hover();
  }

  @Override
  public void unhover() {
    updateDescription(false);
    super.unhover();
    super.untip();
  }

  abstract protected void updateDescription(boolean extendedDescription);
}