package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
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

  private int numReps;
  private int potatoesPerTick;

  private boolean isFirstRep;

  public HitTheSackAction(AbstractPlayer p, AbstractMonster m, DamageInfo info, int potatoesPerTick, boolean upgraded, boolean freeToPlayOnce, int energyOnUse)
  {
    this(p, m, info, potatoesPerTick, upgraded, freeToPlayOnce, energyOnUse, true, -1);
  }

  public HitTheSackAction(AbstractPlayer p, AbstractMonster m, DamageInfo info, int potatoesPerTick,
                          boolean upgraded, boolean freeToPlayOnce, int energyOnUse, boolean isFirstRep, int numReps)
  {
    this.p = p;
    this.m = m;
    this.info = info;
    this.amount = potatoesPerTick;
    this.potatoesPerTick = potatoesPerTick;
    this.freeToPlayOnce = freeToPlayOnce;
    this.upgraded = upgraded;
    this.duration = Settings.ACTION_DUR_XFAST;
    this.actionType = ActionType.DAMAGE;
    this.energyOnUse = energyOnUse;

    this.isFirstRep = isFirstRep;
    this.numReps = numReps;

    TheSimpletonMod.logger.debug("HitTheSackAction: energyOnUse: " + energyOnUse + "; upgraded: " + upgraded);
  }

  public void updates()
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

  @Override
  public void update() {
    if (isFirstRep) {
      this.numReps = EnergyPanel.totalCount;
      if (this.energyOnUse != -1) {
        this.numReps = this.energyOnUse;
      }
      if (this.upgraded) {
        this.numReps++;
      }
    }

    if (this.numReps <= 0) {
      this.isDone = true;
      return;
    } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
      AbstractDungeon.actionManager.clearPostCombatActions();
      this.isDone = true;
      return;
    }

    this.duration -= Gdx.graphics.getDeltaTime();

    if (this.duration < 0.0F)
    {
        if (numReps > 0)  {

          if (!m.isDead && !m.isDying) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(this.m, this.info, AttackEffect.BLUNT_LIGHT, true));
          }

          AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(p, p, new PlantPotatoPower(p, this.amount), this.amount));

          AbstractDungeon.actionManager.addToBottom(
              new HitTheSackAction(this.p, this.m, this.info, this.potatoesPerTick,
                  this.upgraded, this.freeToPlayOnce, this.energyOnUse, false,  --numReps));

          if (!this.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
          }
        }
      this.isDone = true;
    }
  }


}
