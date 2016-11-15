package com.crowsofwar.avatar.client.gui;

import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.gui.AbilityIcon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityCard extends Gui {
	
	private final BendingAbility ability;
	private final AvatarPlayerData data;
	private final Minecraft mc;
	
	public AbilityCard(BendingAbility ability) {
		this.mc = Minecraft.getMinecraft();
		this.ability = ability;
		this.data = AvatarPlayerData.fetcher().fetchPerformance(mc.thePlayer);
	}
	
	public BendingAbility getAbility() {
		return ability;
	}
	
	// @formatter:off
	public void render(ScaledResolution res, int index) {
		
		// NOTE! Minecraft has automatic icon scaling; can be found via res.getScaleFactor()
		// To counteract this, normally you would use
		//   GlStateManager.scale(1f / res.getScaleFactor, 1f / res.getScaleFactor(), 1)
		// HOWEVER, since this is calculating scale already, I don't need to use that
		
		// There are 2 types of pixels here.
		// SCREEN PIXELS - The actual pixels of the screen. Requires resolution to make sure everything is proportioned.
		// CARD PIXELS   - Using scaling seen below, the card is now 100px x ??px (height depends on resolution).
		
		AbilityIcon icon = ability.getIcon();
		
		int spacing = (int) (res.getScaledWidth() / 8.5); // Spacing between each card
		int width = (int) (res.getScaledWidth() / 10.0);  // Width of each card;  1/10 of total width
		int height = (int) (res.getScaledHeight() * 0.6); // Height of each card; about 1/2 of total height
		
		float scale = width / 100f;
		
		float minX = (int) (index * (width + spacing));
		float minY = (res.getScaledHeight() - height) / 2;
		float maxX = minX + width;
		float maxY = minY + height;
		float midX = (minX + maxX) / 2;
		float midY = (minY + maxY) / 2;
		
		float iconX = 10;
		float iconY = 5;
		float iconWidth = 80;
		float iconHeight = 80;
		
		// Draw card background
		pushMatrix();
			GlStateManager.translate(minX, minY, 0);
			GlStateManager.scale(width, height, 1);
			mc.getTextureManager().bindTexture(AvatarUiTextures.skillsGui);
			drawTexturedModalRect(0, 0, 0, 0, 1, 1);
		popMatrix();
		
		pushMatrix();
			GlStateManager.translate(minX, minY, 0);
			GlStateManager.scale(scale, scale, 1);
			// Now is translated & scaled to size of 100px width (height is variable)
			
			// draw icon
			pushMatrix();
				GlStateManager.translate(iconX, iconY, 0);
				GlStateManager.scale(iconWidth / 32, iconHeight / 32, 1);
				mc.getTextureManager().bindTexture(AvatarUiTextures.icons);
				drawTexturedModalRect(0, 0, icon.getMinU(), icon.getMinV(), 32, 32);
			popMatrix();
			
		popMatrix();
		
		drawString(mc.fontRendererObj, ((int) data.getAbilityData(ability).getXp()) + "%", (int) minX, (int) minY + 40, 0xffffff);
		
	}
	// @formatter:on
	
}
