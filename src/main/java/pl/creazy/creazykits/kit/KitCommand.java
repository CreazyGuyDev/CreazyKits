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

  @Args("create ?s")
  @HasPermissions("creazykits.kit.create")
  void createKit(Player sender, String kitName) {
    var kit = new Kit(kitName);
    var context = new KitMenuContext();
    context.setKit(kit);
    ContextPlayerMenu.open(KitMenu.class, sender, context);
  }

  @Args("give ?s ?s")
  @HasPermissions("creazykits.kit.give")
  void giveKit(Player sender, String playerName, String kitName) {
    var player = Bukkit.getPlayer(playerName);
    var kit = kitManager.getKit(kitName, plugin);
    if (player == null || kit == null) {
      return;
    }
    kit.give(sender, true);
  }

  @Args("update ?s")
  @HasPermissions("creazykits.kit.update")
  void updateKit(Player sender, String kitName) {
    var context = new KitMenuContext();
    context.setUpdate(true);
    context.setKitName(kitName);
    ContextPlayerMenu.open(KitMenu.class, sender, context);
  }

  @Args("remove ?s")
  @HasPermissions("creazykits.kit.remove")
  void removeKit(Player sender, String kitName) {
    kitManager.removeKit(kitName, plugin);
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
    if (args.length == 3 && "give".equals(args[0])) {
      return kitManager.getKitsNames();
    }
    return null;
  }
}
