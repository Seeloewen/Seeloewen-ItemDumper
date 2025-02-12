package me.seeloewen.seeloewen_itemdumper;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Mod(Seeloewen_itemdumper.MODID)
public class Seeloewen_itemdumper
{
    public static final String MODID = "seeloewen_itemdumper";
    public static final String VERSION = "1.0.0-Dev";
    public static String fileName;
    public static String modDirectory;
    private static final Logger logger = LogUtils.getLogger();
    public static ArrayList<String> itemIds = new ArrayList<String>();

    public Seeloewen_itemdumper(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            logger.info(String.format("Loaded Seeloewen ItemDumper {0}", VERSION));

            //Create the directory for the mod in the .minecraft folder of the instance (if it doesn't exist already)
            File directory = new File(FMLPaths.GAMEDIR.get().toString(), "seeloewen_itemdumper");
            directory.mkdir();
            modDirectory = FMLPaths.GAMEDIR.get() + "\\seeloewen_itemdumper";

            //Dump the items to a file
            DumpItems();
        }

        public static void DumpItems()
        {
            //Create the file the game will write into
            fileName = FMLPaths.GAMEDIR.get() + "\\seeloewen_itemdumper\\items_" + LocalDateTime.now().toString().replace(':', '-').replace('.', '-') + ".txt";
            CreateFile(fileName);

            //Get all ids and write the ids to the file
            GetItemIds();

            try
            {
                FileWriter writer = new FileWriter(fileName);

                for (String id : itemIds)
                {
                    writer.write(id + "\n");
                }

                writer.close();
                System.out.println("Successfully dumped the item ids to the file.");
            }
            catch (IOException e)
            {
                System.out.println("An error occurred while dumping the items.");
                e.printStackTrace();
            }
        }

        public static void CreateFile(String fileName)
        {
            try
            {
                File file = new File(fileName);
                file.createNewFile();

            }
            catch (IOException e)
            {
                System.out.println("An error occurred while trying to create the dump file.");
                e.printStackTrace();
            }
        }

        public static void GetItemIds()
        {
            //Go through the item registry and get the keys
            for (Item item : ForgeRegistries.ITEMS.getValues())
            {
                ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);

                //Only add valid items (filter ids that contain a dot or slash)
                if(id != null && !id.toString().contains(".") && !id.toString().contains("/"))
                {
                    itemIds.add(id.toString());
                }
            }
        }
    }
}
