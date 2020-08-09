package antibluequirk.alternatingflux;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import antibluequirk.alternatingflux.block.BlockConnector;
import antibluequirk.alternatingflux.item.ItemAFBase;
import antibluequirk.alternatingflux.item.ItemMaterial;
import antibluequirk.alternatingflux.item.ItemWireCoil;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = AlternatingFlux.MODID, version = AlternatingFlux.VERSION, dependencies = "required-after:immersiveengineering@[0.12,)", acceptedMinecraftVersions = "[1.12.2]")
@Mod.EventBusSubscriber
public class AlternatingFlux {
	public static final String MODID = "alternatingflux";
	public static final String VERSION = "${version}";
	public static final String MODNAME = "Alternating Flux";
	
	public static Logger logger;
	
	@Mod.Instance(MODID)
	public static AlternatingFlux instance = new AlternatingFlux();
	public static CreativeTabs creativeTab = new CreativeTabs(MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(AlternatingFlux.item_coil, 1, 0);
		}
	};
	
	public static final List<Block> blocks = new ArrayList<>();
	public static final List<Item> items = new ArrayList<>();
	
	public static BlockConnector block_conn = null;
	
	public static ItemAFBase item_conn = null;
	public static ItemWireCoil item_coil = null;
	public static ItemMaterial item_material = null;
	
	public static ResourceLocation TEX_PASSTHROUGH_AF = new ResourceLocation(AlternatingFlux.MODID, "blocks/passthrough_af");
	
	@SidedProxy(clientSide = "antibluequirk.alternatingflux.client.ClientProxy", serverSide = "antibluequirk.alternatingflux.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		Config.preInit(event);
		
		item_material = new ItemMaterial();
		block_conn = new BlockConnector();
		item_coil = new ItemWireCoil();
		
		proxy.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
	
	private static ResourceLocation createRegistryName(String unlocalized)
	{
		unlocalized = unlocalized.substring(unlocalized.indexOf(MODID));
		unlocalized = unlocalized.replaceFirst("\\.", ":");
		return new ResourceLocation(unlocalized);
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {		
		for(Block block : blocks)
			event.getRegistry().register(block.setRegistryName(createRegistryName(block.getTranslationKey())));
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		for(Item item : items)
			event.getRegistry().register(item.setRegistryName(createRegistryName(item.getTranslationKey())));
	}
}