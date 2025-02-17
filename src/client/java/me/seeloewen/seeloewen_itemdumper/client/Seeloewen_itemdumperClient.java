package me.seeloewen.seeloewen_itemdumper.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class Seeloewen_itemdumperClient implements ClientModInitializer
{
    public static String modDirectory;
    public static String fileName;
    public static Logger logger;
    public static ArrayList<String> itemIds = new ArrayList<String>();

    @Override
    public void onInitializeClient()
    {
        logger = LoggerFactory.getLogger(MOD_ID);
        logger.info("Loaded Seeloewen ItemDumper + VERSION");

        //Create the directory for the mod in the .minecraft folder of the instance (if it doesn't exist already)
        File directory = new File(FabricLoader.getInstance().getGameDir().toString(), "seeloewen_itemdumper");
        directory.mkdir();
        modDirectory = FabricLoader.getInstance().getGameDir() + "\\seeloewen_itemdumper";

        //Dump the items to a file
        DumpItems();
    }

    public static void DumpItems()
    {
        //Create the file the game will write into
        fileName = FabricLoader.getInstance().getGameDir() + "\\seeloewen_itemdumper\\items_" + LocalDateTime.now().toString().replace(':', '-').replace('.', '-') + ".txt";
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
            logger.error("[Seeloewen ItemDumper] An error occurred while dumping the items:");
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
            logger.error("An error occurred while trying to create the dump file.");
            logger.error(e.toString());
        }
    }

    public static void GetItemIds()
    {
        //Go through the item registry and get the keys

        for (Item item : Registries.ITEM)
        {
            Identifier id = Registries.ITEM.getId(item);

            //Only add valid items (filter ids that contain a dot or slash)
            if (!id.toString().contains(".") && !id.toString().contains("/"))
            {
                itemIds.add(id.toString());
            }
        }
    }
}
