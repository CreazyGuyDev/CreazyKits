package pl.creazy.creazykits.kit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.creazy.creazykits.CreazyKits;
import pl.creazy.creazylib.data.persistence.config.Config;
import pl.creazy.creazylib.manager.constraints.Manager;
import pl.creazy.creazylib.part.constraints.Injected;
import pl.creazy.creazylib.part.constraints.OnDisable;
import pl.creazy.creazylib.part.constraints.OnEnable;
import pl.creazy.creazylib.plugin.CreazyPlugin;
import pl.creazy.creazylib.util.message.Message;
import pl.creazy.creazylib.util.message.Placeholder;
import pl.creazy.creazylib.util.result.StatusResult;

import java.util.*;

@Manager
public class KitManager {
  private final Map<String, List<Kit>> kits = new HashMap<>();

  @Injected
  private KitMessagesConfig messages;

  @OnDisable
  private void save(@Injected CreazyKits plugin) {
    var data = getConfig(plugin);
    for (var entry : kits.entrySet()) {
      data.set(entry.getKey(), entry.getValue());
    }
    data.save();
  }

  @SuppressWarnings("unchecked")
  @OnEnable
  private void restore(@Injected CreazyKits plugin) {
    var data = getConfig(plugin);

    for (var key : data.getKeys()) {
      var kits = (List<Kit>) data.getList(key);
      if (kits != null) {
        this.kits.put(key, kits);
      }
    }
  }

  private @NotNull Config getConfig(CreazyKits plugin) {
    return Config.getConfig("kits", "data", plugin);
  }

  public @NotNull Message updateKit(@NotNull String kitName, @NotNull CreazyPlugin plugin, ItemStack... content) {
    var kit = getKit(kitName, plugin);

    if (kit == null) {
      return Message.create(messages.getKitNotExist(), Placeholder.name(kitName));
    }

    kit.clearItems();
    kit.addItems(content);

    return Message.create(messages.getKitUpdated(), Placeholder.name(kitName));
  }

  public @NotNull Message addKit(@NotNull Kit kit, @NotNull CreazyPlugin plugin) {
    if (!kits.containsKey(plugin.getName())) {
      kits.put(plugin.getName(), new ArrayList<>());
    }

    var kits = this.kits.get(plugin.getName());
    var kitName = kit.getName();

    if (kits.stream().anyMatch(other -> kitName.equals(other.getName()))) {
      return Message.create(messages.getKitAlreadyExists(), Placeholder.name(kitName));
    }

    kits.add(kit);

    return Message.create(messages.getKitCreated(), Placeholder.name(kitName));
  }

  public @Nullable Kit getKit(@Nullable String kitName, @NotNull CreazyPlugin plugin) {
    if (kitName == null) {
      return null;
    }
    for (var kit : kits.getOrDefault(plugin.getName(), new ArrayList<>())) {
      if (kit.getName().equals(kitName)) {
        return kit;
      }
    }

    return null;
  }

  public @NotNull Message giveKit(@NotNull String kitName, @NotNull String playerName, @NotNull CreazyPlugin plugin, boolean ignorePermissions) {
    var player = Bukkit.getPlayer(playerName);

    if (player == null) {
      return Message.create(messages.getPlayerNotExist(), Placeholder.playerName(playerName));
    }
    return giveKit(kitName, player, plugin, ignorePermissions);
  }

  public @NotNull Message giveKit(@NotNull String kitName, @NotNull Player player, @NotNull CreazyPlugin plugin, boolean ignorePermissions) {
    var kit = getKit(kitName, plugin);

    if (kit == null) {
      return Message.create(messages.getKitNotExist(), Placeholder.name(kitName));
    }

    if (kit.give(player, ignorePermissions)) {
      return Message.create(
          messages.getKitGave(),
          Placeholder.name(kitName),
          Placeholder.playerName(player.getDisplayName())
      );
    } else {
     return Message.create(messages.getKitDelay(), Placeholder.name(kitName));
    }
  }

  public @NotNull Message removeKit(@NotNull String kitName, @NotNull CreazyPlugin plugin) {
    var kits = this.kits.get(plugin.getName());

    if (kits != null) {
      kits.removeIf(kit -> kitName.equals(kit.getName()));
      return Message.create(messages.getKitRemoved(), Placeholder.name(kitName));
    }

    return Message.create(messages.getKitNotExist());
  }

  public @NotNull StatusResult<Message, Boolean> kitExistsMessage(@NotNull String kitName, @NotNull CreazyPlugin plugin) {
    var placeholder = Placeholder.name(kitName);

    if (getKit(kitName, plugin) == null) {
      return StatusResult.create(Message.create(messages.getKitNotExist(), placeholder), false);
    }
    return StatusResult.create(Message.create(messages.getKitAlreadyExists(), placeholder), true);
  }

  public @NotNull List<String> getKitsNames() {
    var names = new LinkedList<String>();

    for (var entry : kits.entrySet()) {
      names.addAll(entry.getValue().stream().map(Kit::getName).toList());
    }

    return names;
  }
}
