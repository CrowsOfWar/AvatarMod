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
package com.crowsofwar.avatar.common.network.packets;

import com.crowsofwar.avatar.common.network.PacketRedirector;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author CrowsOfWar
 */
public class PacketCErrorMessage extends AvatarPacket<PacketCErrorMessage> {

	private String message;

	public PacketCErrorMessage() {
	}

	public PacketCErrorMessage(String message) {
		this.message = message;
	}

	@Override
	public void avatarFromBytes(ByteBuf buf) {
		message = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void avatarToBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, message);
	}

	@Override
	protected Side getReceivedSide() {
		return Side.CLIENT;
	}

	@Override
	protected com.crowsofwar.avatar.common.network.packets.AvatarPacket.Handler<PacketCErrorMessage> getPacketHandler() {
		return PacketRedirector::redirectMessage;
	}

	public String getMessage() {
		return message;
	}

}
