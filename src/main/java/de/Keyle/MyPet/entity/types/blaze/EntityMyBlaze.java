/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2013 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.entity.types.blaze;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.server.v1_6_R2.World;

@EntitySize(width = 0.6F, height = 0.7F)
public class EntityMyBlaze extends EntityMyPet
{
    public EntityMyBlaze(World world, MyPet myPet)
    {
        super(world, myPet);
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);
            setOnFire(((MyBlaze) myPet).isOnFire());
        }
    }

    public boolean isOnFire()
    {
        return ((MyBlaze) myPet).isOnFire;
    }

    public void setOnFire(boolean flag)
    {
        this.datawatcher.watch(16, (byte) (flag ? 1 : 0));
        ((MyBlaze) myPet).isOnFire = flag;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void a()
    {
        super.a();
        getDataWatcher().a(16, new Byte((byte) 0)); // burning
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    @Override
    protected String aN()
    {
        return "mob.blaze.hit";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    @Override
    protected String aO()
    {
        return "mob.blaze.death";
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String r()
    {
        return !playIdleSound() ? "" : "mob.blaze.breathe";
    }
}