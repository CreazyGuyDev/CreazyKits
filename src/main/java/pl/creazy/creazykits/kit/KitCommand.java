package pl.creazy.creazykits.kit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.creazy.creazykits.CreazyKits;
import pl.creazy.creazylib.command.TabCompleteHandler;
import pl.creazy.creazylib.command.constraints.Args;
import pl.creazy.creazylib.command.constraints.Command;
import pl.creazy.creazylib.command.constraints.HasPermissions;
import pl.creazy.creazylib.part.constraints.Injected;
import pl.creazy.creazylib.screen.menu.ContextPlayerMenu;

import java.util.List;

@Command("kit")
class KitCommand implements TabCompleteHandler {
  @Injected
  private KitManager kitManager;

  @Injected
  private CreazyKits plugin;

  @Args("create ?s ?l")
  @HasPermissions("creazykits.kit.create")
  void createKit(Player sender, String kitName, long delay) {
    var kit = new Kit(kitName);
    kit.setDelay(delay);
    var context = new KitMenuContext();
    context.setKit(kit);
    ContextPlayerMenu.open(KitMenu.class, sender, context);
  }

  @Args("give ?s ?s")
  @HasPermissions("creazykits.kit.give")
  void giveKit(Player sender, String playerName, String kitName) {
    kitManager.giveKit(kitName, playerName, plugin).sendChat(sender);
  }

  @Args("update ?s")
  @HasPermissions("creazykits.kit.update")
  void updateKit(Player sender, String kitName) {
    var status = kitManager.kitExistsMessage(kitName, plugin);
    if (!status.status()) {
      status.result().sendChat(sender);
      return;
    }
    var context = new KitMenuContext();
    context.setUpdate(true);
    context.setKitName(kitName);
    ContextPlayerMenu.open(KitMenu.class, sender, context);
  }

  @Args("remove ?s")
  @HasPermissions("creazykits.kit.remove")
  void removeKit(Player sender, String kitName) {
    kitManager.removeKit(kitName, plugin).sendChat(sender);
  }

  @Override
  public @Nullable List<String> handleTabComplete(@NotNull Player player, @NotNull String[] args) {
    if (args.length == 2) {
      switch (args[0]) {
        case "create" -> {
          return List.of("Enter name");
        }
        case "give" -> {
          return Bukkit.getOnlinePlayers().stream()
              .map(Player::getDisplayName)
              .filter(name -> name.startsWith(args[1]))
              .toList();
        }
        case "update", "remove" -> {
          return kitManager.getKitsNames();
        }
      }
    }
    if (args.length == 3) {
      switch (args[0]) {
        case "give" -> {
          return kitManager.getKitsNames();
        }
        case "create" -> {
          return List.of("Enter delay");
        }
      }
    }
    return null;
  }
}
