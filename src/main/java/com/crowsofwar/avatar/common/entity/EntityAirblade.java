/* 
  This file is part of AvatarMod.
    
  AvatarMod is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  AvatarMod is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with AvatarMod. If not, see <http://www.gnu.org/licenses/>.
*/
package com.crowsofwar.avatar.common.entity;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import java.util.List;

import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.entity.data.OwnerAttribute;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class EntityAirblade extends AvatarEntity {
	
	public static final DataParameter<String> SYNC_OWNER = EntityDataManager.createKey(EntityAirblade.class,
			DataSerializers.STRING);
	
	private final OwnerAttribute ownerAttr;
	private float damage;
	
	public EntityAirblade(World world) {
		super(world);
		setSize(1.5f, .2f);
		this.ownerAttr = new OwnerAttribute(this, SYNC_OWNER);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		Vector v = velocity().mul(.96).dividedBy(20);
		moveEntity(MoverType.SELF, v.x(), v.y(), v.z());
		if (!worldObj.isRemote && velocity().sqrMagnitude() <= .9) setDead();
		
		if (worldObj.getBlockState(getPosition()).getBlock() == Blocks.WATER) setDead();
		
		if (!isDead && !worldObj.isRemote) {
			List<Entity> collidedList = worldObj.getEntitiesWithinAABBExcludingEntity(this,
					getEntityBoundingBox());
			
			if (!collidedList.isEmpty()) {
				
				Entity collided = collidedList.get(0);
				if (collided instanceof EntityLivingBase) {
					
					EntityLivingBase lb = (EntityLivingBase) collided;
					lb.attackEntityFrom(AvatarDamageSource.causeAirbladeDamage(collided, getOwner()), damage);
					
				}
				
				Vector motion = velocity().copy();
				motion.mul(STATS_CONFIG.airbladeSettings.push);
				motion.setY(0.08);
				collided.addVelocity(motion.x(), motion.y(), motion.z());
				
				if (getOwner() != null) {
					AvatarPlayerData data = AvatarPlayerData.fetcher().fetch(getOwner());
					data.getAbilityData(BendingAbility.ABILITY_AIRBLADE).addXp(SKILLS_CONFIG.airbladeHit);
				}
				
				setDead();
				
			}
		}
		
	}
	
	public EntityPlayer getOwner() {
		return ownerAttr.getOwner();
	}
	
	public void setOwner(EntityPlayer owner) {
		ownerAttr.setOwner(owner);
	}
	
	public void setDamage(float damage) {
		this.damage = damage;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		ownerAttr.load(nbt);
		damage = nbt.getFloat("Damage");
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		ownerAttr.save(nbt);
		nbt.setFloat("Damage", damage);
	}
	
}
