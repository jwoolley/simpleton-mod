package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.PlantPotatoPower;

public class HitTheSackAction  extends AbstractGameAction
{
  private DamageInfo info;
  private boolean freeToPlayOnce;
  private boolean upgraded ;
  private int energyOnUse;
  private AbstractPlayer p;
  private AbstractMonster m;

  public HitTheSackAction(AbstractPlayer p, AbstractMonster m, DamageInfo info, int potatoesPerTick, boolean upgraded, boolean freeToPlayOnce, int energyOnUse)
  {
    this.p = p;
    this.m = m;
    this.info = info;
    this.amount = potatoesPerTick;
    this.freeToPlayOnce = freeToPlayOnce;
    this.upgraded = upgraded;
    this.duration = Settings.ACTION_DUR_XFAST;
    this.actionType = ActionType.DAMAGE;
    this.energyOnUse = energyOnUse;

    TheSimpletonMod.logger.debug("HitTheSackAction: energyOnUse: " + energyOnUse + "; upgraded: " + upgraded);
  }

  public void update()
  {
    int effect = EnergyPanel.totalCount;
    if (this.energyOnUse != -1) {
      effect = this.energyOnUse;
    }
    if (this.upgraded) {
      effect++;
    }

    if (effect > 0)
    {
      for (int i = 0; i < effect; i++)
      {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(this.m, this.info, AttackEffect.BLUNT_HEAVY, true));
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new PlantPotatoPower(p, this.amount), this.amount));
      }
      if (!this.freeToPlayOnce) {
        this.p.energy.use(EnergyPanel.totalCount);
      }
    }
    this.isDone = true;
  }
}
