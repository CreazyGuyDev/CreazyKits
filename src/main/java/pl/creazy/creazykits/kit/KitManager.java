package pl.creazy.creazykits.kit;

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

import java.util.*;

@Manager
public class KitManager {
  private final Map<String, List<Kit>> kits = new HashMap<>();

  @Injected
  private CreazyKits plugin;

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

  public void updateKit(@NotNull String kitName, ItemStack... content) {
    var kit = getKit(kitName, plugin);
    if (kit == null) {
      return;
    }
    kit.clearItems();
    kit.addItems(content);
  }

  public void addKit(@NotNull Kit kit, @NotNull CreazyPlugin plugin) {
    if (!kits.containsKey(plugin.getName())) {
      kits.put(plugin.getName(), new ArrayList<>());
    }
    var kits = this.kits.get(plugin.getName());
    if (kits.stream().anyMatch(other -> kit.getName().equals(other.getName()))) {
      return;
    }
    kits.add(kit);
  }

  public @Nullable Kit getKit(@NotNull String kitName, @NotNull CreazyPlugin plugin) {
    for (var kit : kits.getOrDefault(plugin.getName(), new ArrayList<>())) {
      if (kit.getName().equals(kitName)) {
        return kit;
      }
    }
    return null;
  }

  public void removeKit(@NotNull String kitName, @NotNull CreazyPlugin plugin) {
    var kits = this.kits.get(plugin.getName());
    if (kits != null) {
      kits.removeIf(kit -> kitName.equals(kit.getName()));
    }
  }

  public @NotNull List<String> getKitsNames() {
    var names = new LinkedList<String>();
    for (var entry : kits.entrySet()) {
      names.addAll(entry.getValue().stream().map(Kit::getName).toList());
    }
    return names;
  }
}
