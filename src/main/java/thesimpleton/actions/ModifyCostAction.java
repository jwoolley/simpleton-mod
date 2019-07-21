package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

public class ModifyCostAction extends AbstractGameAction {
    private final AbstractCard card;

    public ModifyCostAction(AbstractCard card, int amount) {
      this.card = card;
      this.amount = amount;
    }

    @Override
    public void update() {
      for (AbstractCard c : GetAllInBattleInstances.get(this.card.uuid)) {
        c.updateCost(this.amount);
      }
      this.isDone = true;
    }
}