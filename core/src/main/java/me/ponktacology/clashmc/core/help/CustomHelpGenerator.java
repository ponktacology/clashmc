package me.ponktacology.clashmc.core.help;

import me.ponktacology.clashmc.core.help.annotation.HelpIndex;
import me.ponktacology.clashmc.core.help.annotation.Hidden;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Flag;
import me.vaperion.blade.command.container.BladeCommand;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.utils.MessageBuilder;
import org.bukkit.ChatColor;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomHelpGenerator implements me.vaperion.blade.command.help.HelpGenerator {

  
  @Override
  public List<String> generate(
       BladeContext bladeContext,  List<BladeCommand> list) {
    if (list.isEmpty())
      return Collections.singletonList(
          Text.colored("&cNie znaleziono takiej komendy. Wszystkie komendy pod &7/pomoc&c."));
    List<String> help = new ArrayList<>(Collections.singletonList(Text.colored("   &6Pomoc:")));
    for (BladeCommand command :
        list.stream()
            .filter(it -> !it.getMethod().isAnnotationPresent(Hidden.class))
            .sorted(
                (o1, o2) -> {
                  Method method1 = o1.getMethod();
                  Method method2 = o2.getMethod();

                  return (method1.isAnnotationPresent(HelpIndex.class)
                          ? method1.getAnnotation(HelpIndex.class).value()
                          : 0)
                      - (method2.isAnnotationPresent(HelpIndex.class)
                          ? method2.getAnnotation(HelpIndex.class).value()
                          : 0);
                })
            .collect(Collectors.toList())) {
      help.add(
          Text.colored(
              getUsage(command, command.getAliases()[0]) + " &8-&7 " + command.getDescription()));
    }

    return help;
  }

  public String getUsage( BladeCommand command, String alias) {
    boolean hasDesc =
        command.getDescription() != null && !command.getDescription().trim().isEmpty();

    MessageBuilder builder =
        new MessageBuilder(ChatColor.YELLOW + "/").append(ChatColor.YELLOW + alias);

    Optional.of(command.getFlagParameters())
        .ifPresent(
            flagParameters -> {
              if (!flagParameters.isEmpty()) {
                builder.append(" ").append(ChatColor.YELLOW + "(");
                if (hasDesc)
                  builder.hover(
                      Collections.singletonList(ChatColor.GRAY + command.getDescription().trim()));

                int i = 0;
                for (BladeParameter.FlagParameter flagParameter : flagParameters) {
                  builder.append(i++ == 0 ? "" : (ChatColor.GRAY + " | ")).reset();
                  if (hasDesc)
                    builder.hover(
                        Collections.singletonList(
                            ChatColor.GRAY + command.getDescription().trim()));

                  Flag flag = flagParameter.getFlag();

                  builder.append(ChatColor.AQUA + "-" + flag.value());
                  if (!flagParameter.isBooleanFlag())
                    builder.append(ChatColor.AQUA + " <" + flagParameter.getName() + ">");
                  if (!flag.description().trim().isEmpty())
                    builder.hover(
                        Collections.singletonList(ChatColor.YELLOW + flag.description().trim()));
                }

                builder.append(ChatColor.YELLOW + ")");
                if (hasDesc)
                  builder.hover(
                      Collections.singletonList(ChatColor.GRAY + command.getDescription().trim()));
              }
            });

    Optional.of(command.getCommandParameters())
        .ifPresent(
            commandParameters -> {
              if (!commandParameters.isEmpty()) {
                builder.append(" ");
                if (hasDesc)
                  builder.hover(
                      Collections.singletonList(ChatColor.GRAY + command.getDescription().trim()));

                int i = 0;
                for (BladeParameter.CommandParameter commandParameter : commandParameters) {
                  builder.append(i++ == 0 ? "" : " ");

                  builder.append(ChatColor.YELLOW + (commandParameter.isOptional() ? "(" : "<"));
                  builder.append(ChatColor.YELLOW + commandParameter.getName());
                  builder.append(ChatColor.YELLOW + (commandParameter.isOptional() ? ")" : ">"));
                }
              }
            });

    if (command.getExtraUsageData() != null && !command.getExtraUsageData().trim().isEmpty()) {
      builder.append(" ");
      builder.append(ChatColor.YELLOW + command.getExtraUsageData());
      if (hasDesc)
        builder.hover(Collections.singletonList(ChatColor.GRAY + command.getDescription().trim()));
    }

    return builder.toStringFormat();
  }
}
