package mattjohns.minecraft.common.system;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.math.BlockPos;

import mattjohns.common.math.General;
import net.minecraft.block.Block;

public class SystemUtility {
	public static final int TickPerSecond = 20;

	//// this is wrong
	public static int updateFrequencyToTickPeriod(double frequencySecond) {
		return (int)(SystemUtility.TickPerSecond * frequencySecond);
	}

	/**
	 * Returns a fractional number of ticks to wait for each period. So caller
	 * needs to track leftover amounts and use them up when they accumulate
	 * into an integer.
	 */
	public static double tickFrequencyToPeriod(double frequencySecond) {
		return (double)SystemUtility.TickPerSecond / frequencySecond;
	}

	public static void clientDisconnectFromServer() {
		Minecraft minecraftClient = Minecraft.getMinecraft();

		// code copied from GuiIngameMenu class
		boolean flag = minecraftClient.isIntegratedServerRunning();
		boolean flag1 = minecraftClient.isConnectedToRealms();
		minecraftClient.world.sendQuittingDisconnectingPacket();
		minecraftClient.loadWorld((WorldClient)null);

		if (flag) {
			minecraftClient.displayGuiScreen(new GuiMainMenu());
		}
		else if (flag1) {
			RealmsBridge realmsbridge = new RealmsBridge();
			realmsbridge.switchToRealms(new GuiMainMenu());
		}
		else {
			minecraftClient.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
		}
	}

	public static Block blockGet(String name) {
		return Block.getBlockFromName(name);
	}
	
	public static boolean blockIsExist(String name) {
		return blockGet(name) != null;
	}

	// offsets of all blocks that have their center within the given radius
	public static ArrayList<BlockPos> radiusGetBlockOffsetList(double radius) {
		ArrayList<BlockPos> result = new ArrayList<>();
		
		// first work out the rough square
		
		// square radius doesn't account for the center block, so radius of 1 means square width of 3
		int squareRadius = (int)Math.round(radius);
		
		for (int x = squareRadius * -1; x <= squareRadius; x++) {
			for (int y = squareRadius * -1; y <= squareRadius; y++) {
				double distanceSquare = (float)(x * x) + (float)(y * y);
				
				double distance;
				if (General.isNearlyZero(distanceSquare)) {
					distance = 0d;
				}
				else {
					distance = Math.sqrt(distanceSquare);
				}
				
				if (distance < radius) {
					// within radius, return it
					result.add(new BlockPos(x, 0, y));
				}
			}
		}
		
		return result;
	}
}
