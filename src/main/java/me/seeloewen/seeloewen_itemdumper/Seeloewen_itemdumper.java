package me.seeloewen.seeloewen_itemdumper;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLPaths;
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
    private static final Logger LOGGER = LogUtils.getLogger();
    public static String fileName;
    public static String modDirectory;
    private static final Logger logger = LogUtils.getLogger();
    public static ArrayList<String> itemIds = new ArrayList<String>();

    public Seeloewen_itemdumper(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLLoadCompleteEvent event)
    {
        logger.info("Loaded Seeloewen ItemDumper + VERSION");

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
            logger.info("----------------------------------------------------------");
            logger.info("[Seeloewen ItemDumper] Successfully dumped the item ids to the file.");
            logger.info("----------------------------------------------------------");
        }
        catch (IOException e)
        {
            logger.info("[Seeloewen ItemDumper] An error occurred while dumping the items:");
            logger.error(e.toString());
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
            logger.info("[Seeloewen ItemDumper] An error occurred while trying to create the dump file:");
            logger.error(e.toString());
        }
    }

    public static void GetItemIds()
    {
        //Go through the item registry and get the keys
        for (ResourceLocation id : BuiltInRegistries.ITEM.keySet())
        {
            //Only add valid items (filter ids that contain a dot or slash)
            if(id != null && !id.toString().contains(".") && !id.toString().contains("/"))
            {
                itemIds.add(id.toString());
            }
        }
    }
}
