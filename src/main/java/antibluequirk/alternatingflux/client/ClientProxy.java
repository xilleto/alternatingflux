package antibluequirk.alternatingflux.client;

import java.util.Locale;

import antibluequirk.alternatingflux.AlternatingFlux;
import antibluequirk.alternatingflux.CommonProxy;
import antibluequirk.alternatingflux.Config;
import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.energy.wires.WireApi;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.client.models.obj.IEOBJLoader;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IIEMetaBlock;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(this);
		OBJLoader.INSTANCE.addDomain(AlternatingFlux.MODID);
		IEOBJLoader.instance.addDomain(AlternatingFlux.MODID);
	}
	
	@Override
	public void postInit() {
		super.postInit();
		ManualInstance m = ManualHelper.getManual();
		
		m.addEntry("alternatingflux", ManualHelper.CAT_ENERGY,
				new ManualPages.Text(m, "alternatingflux0"),
				new ManualPages.Crafting(m, "alternatingfluxWire", new ItemStack(AlternatingFlux.item_wire, 1, 0), new ItemStack(AlternatingFlux.item_coil, 1, 0)),
				new ManualPages.Text(m, "alternatingflux1"),
				new ManualPages.Crafting(m, "alternatingfluxRelay", new ItemStack(AlternatingFlux.block_conn, 1, 0)),
				new ManualPages.Crafting(m, "alternatingfluxTransformer", new ItemStack(AlternatingFlux.block_conn, 1, 1)),
				new ManualPages.Text(m, "alternatingflux2"),
				new ManualPages.Text(m, "alternatingflux3")
		);
	}
	
	@SubscribeEvent
    public void registerTextures(TextureStitchEvent.Pre event){
        event.getMap().registerSprite(AlternatingFlux.TEX_PASSTHROUGH_AF);
    }
	
	public <BLOCK extends Block & IIEMetaBlock> void registerIEBlockModel(BLOCK block)
	{
		ResourceLocation blockname = block.getRegistryName();
		Item blockItem = Item.getItemFromBlock(block);
		if(blockItem == null) throw new RuntimeException("Item representation for block " + blockname + " doesn't exist!");
		if(block instanceof IIEMetaBlock)
		{
			IIEMetaBlock ieMetaBlock = (IIEMetaBlock)block;
			if(ieMetaBlock.useCustomStateMapper()) ModelLoader.setCustomStateMapper(block, IECustomStateMapper.getStateMapper(ieMetaBlock));
			ModelLoader.setCustomMeshDefinition(blockItem, new ItemMeshDefinition()
			{
				@Override
				public ModelResourceLocation getModelLocation(ItemStack stack)
				{
					return new ModelResourceLocation(blockname, "inventory");
				}
			});
			
			for(int meta = 0; meta < ieMetaBlock.getMetaEnums().length; meta++)
			{
				String location = blockname.toString();
				String prop = ieMetaBlock.appendPropertiesToState()?("inventory,"+ieMetaBlock.getMetaProperty().getName()+"="+ieMetaBlock.getMetaEnums()[meta].toString().toLowerCase(Locale.US)): null;
				if(ieMetaBlock.useCustomStateMapper())
				{
					String custom = ieMetaBlock.getCustomStateMapping(meta, true);
					if(custom!=null)
						location += "_"+custom;
				}
				try
				{
					ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(location, prop));
				} catch(NullPointerException npe)
				{
					throw new RuntimeException("WELP! apparently "+ieMetaBlock+" lacks an item!", npe);
				}
			}
		}
		else ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(blockname, "inventory"));
	}
	
	@SubscribeEvent
	@SuppressWarnings("deprecation")
	public void registerModels(ModelRegistryEvent evt)
	{
		WireApi.registerConnectorForRender("conn_af", new ResourceLocation("alternatingflux:block/connector/connector_af.obj"), null);
		WireApi.registerConnectorForRender("rel_af", new ResourceLocation("alternatingflux:block/connector/relay_af.obj"), null);
		WireApi.registerConnectorForRender("transformer_af_left", new ResourceLocation("alternatingflux:block/connector/transformer_af_left.obj"), null);
		WireApi.registerConnectorForRender("transformer_af_right", new ResourceLocation("alternatingflux:block/connector/transformer_af_right.obj"), null);
		
		ModelLoader.setCustomModelResourceLocation(AlternatingFlux.item_coil, 0, new ModelResourceLocation(AlternatingFlux.item_coil.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(AlternatingFlux.item_wire, 0, new ModelResourceLocation(AlternatingFlux.item_wire.getRegistryName(), "inventory"));
		this.registerIEBlockModel(AlternatingFlux.block_conn);
	}
	
	@SubscribeEvent
	public void updateConfig(OnConfigChangedEvent event)
	{
		if(event.getModID().equals(AlternatingFlux.MODID))
		{
    		ConfigManager.sync(AlternatingFlux.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
    		Config.refresh();
		}
	}
}
