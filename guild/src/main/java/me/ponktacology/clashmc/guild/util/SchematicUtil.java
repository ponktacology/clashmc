package me.ponktacology.clashmc.guild.util;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;

import java.io.File;

@UtilityClass
public class SchematicUtil {

  @SneakyThrows
  public static void pasteSchematic(File schematicFile, Location location, boolean noAir) {
    EditSession editSession = new EditSession(new BukkitWorld(location.getWorld()), 999999999);
    editSession.enableQueue();

    SchematicFormat schematic = SchematicFormat.getFormat(schematicFile);
    CuboidClipboard clipboard = schematic.load(schematicFile);

    clipboard.paste(editSession, BukkitUtil.toVector(location), noAir);
    editSession.flushQueue();
  }
}
