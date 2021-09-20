package me.ponktacology.clashmc.sector.sector.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.sector.sector.menu.SectorMenuFactory;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class ChannelCommand {

    
    private final SectorMenuFactory menuFactory;

    @Command(value = {"channel", "ch"}, description = "Pokazuje listę spawnów")
    public void channel( @Sender Player sender) {
        this.menuFactory.getChannelMenu().openMenu(sender);
    }
}
