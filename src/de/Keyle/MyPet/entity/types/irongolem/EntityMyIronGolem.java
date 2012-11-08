/*
 * Copyright (C) 2011-2012 Keyle
 *
 * This file is part of MyPet
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyPet. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.entity.types.irongolem;

import de.Keyle.MyPet.entity.pathfinder.PathfinderGoalAggressiveTarget;
import de.Keyle.MyPet.entity.pathfinder.PathfinderGoalControl;
import de.Keyle.MyPet.entity.pathfinder.PathfinderGoalControlTarget;
import de.Keyle.MyPet.entity.pathfinder.PathfinderGoalOwnerHurtByTarget;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.server.*;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

public class EntityMyIronGolem extends EntityMyPet
{
    public EntityMyIronGolem(World world, MyPet myPet)
    {
        super(world, myPet);
        this.texture = "/mob/villager_golem.png";
        this.a(1.4F, 2.9F);
        this.getNavigation().a(true);
        this.walkSpeed = 0.25F;

        PathfinderGoalControl controlPathfinder = new PathfinderGoalControl(myPet, this.walkSpeed+0.1F);

        this.goalSelector.a(1, new PathfinderGoalLeapAtTarget(this, this.walkSpeed+0.1F));
        this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, this.walkSpeed+0.1F, true));
        this.goalSelector.a(3, controlPathfinder);
        this.goalSelector.a(4, new de.Keyle.MyPet.entity.pathfinder.PathfinderGoalFollowOwner(this, this.walkSpeed, 5.0F, 2.0F, controlPathfinder));
        this.goalSelector.a(5, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(5, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
        this.targetSelector.a(2, new de.Keyle.MyPet.entity.pathfinder.PathfinderGoalOwnerHurtTarget(myPet));
        this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(4, new PathfinderGoalControlTarget(myPet, controlPathfinder, 1));
        this.targetSelector.a(5, new PathfinderGoalAggressiveTarget(myPet, 10));
    }

    @Override
    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            this.myPet = myPet;
            isMyPet = true;

            this.setHealth(myPet.getHealth() >= getMaxHealth() ? getMaxHealth() : myPet.getHealth());
        }
    }

    public int getMaxHealth()
    {
        return MyPet.getStartHP(MyIronGolem.class) + (isMyPet() && myPet.getSkillSystem().hasSkill("HP") ? myPet.getSkillSystem().getSkill("HP").getLevel() : 0);
    }

    @Override
    public org.bukkit.entity.Entity getBukkitEntity()
    {
        if (this.bukkitEntity == null)
        {
            this.bukkitEntity = new CraftMyIronGolem(this.world.getServer(), this);
        }
        return this.bukkitEntity;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void a()
    {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 0)); // flower???
    }

    @Override
    protected void a(int i, int j, int k, int l)
    {
        this.world.makeSound(this, "mob.irongolem.walk", 1.0F, 1.0F);
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String aW()
    {
        return "none";
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String aX()
    {
        return "mob.irongolem.hit";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String aY()
    {
        return "mob.irongolem.death";
    }

    /**
     * Is called when player rightclicks this MyPet
     * return:
     * true: there was a reaction on rightclick
     * false: no reaction on rightclick
     */
    public boolean c(EntityHuman entityhuman)
    {
        super.c(entityhuman);

        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == org.bukkit.Material.IRON_INGOT.getId())
        {
            if (getHealth() < getMaxHealth())
            {
                if (!entityhuman.abilities.canInstantlyBuild)
                {
                    --itemstack.count;
                }
                this.heal(10, RegainReason.EATING);
                if (itemstack.count <= 0)
                {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
                }
                this.tamedEffect(true);
                return true;
            }
        }
        return false;
    }

    public void setPlayerCreated(boolean flag)
    {
        byte b0 = this.datawatcher.getByte(16);

        if (flag)
        {
            this.datawatcher.watch(16, (byte) (b0 | 0x1));
        }
        else
        {
            this.datawatcher.watch(16, (byte) (b0 & 0xFFFFFFFE));
        }
    }

    public boolean l(Entity entity)
    {
        int damage = 2 + (isMyPet && myPet.getSkillSystem().hasSkill("Damage") ? myPet.getSkillSystem().getSkill("Damage").getLevel() : 0);

        this.e = 10;
        this.world.broadcastEntityEffect(this, (byte) 4);
        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), damage + this.random.nextInt(15));

        if (flag)
        {
            entity.motY += 0.4000000059604645D;
        }

        this.world.makeSound(this, "mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }
}