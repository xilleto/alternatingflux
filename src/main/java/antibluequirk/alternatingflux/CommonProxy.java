package antibluequirk.alternatingflux;

import antibluequirk.alternatingflux.block.TileEntityRelayAF;
import antibluequirk.alternatingflux.block.TileEntityTransformerAF;
import antibluequirk.alternatingflux.wire.AFWireType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy {
	public void preInit() {
		GameRegistry.registerTileEntity(TileEntityRelayAF.class, new ResourceLocation(AlternatingFlux.MODID, "af_relay"));
		GameRegistry.registerTileEntity(TileEntityTransformerAF.class, new ResourceLocation(AlternatingFlux.MODID, "af_af_transformer"));
	}
	
	public void init() {
		AFWireType.init();
	}

	public void postInit() {
		OreDictionary.registerOre("wireConstantan", new ItemStack(AlternatingFlux.item_material, 1, 0));
	}
}