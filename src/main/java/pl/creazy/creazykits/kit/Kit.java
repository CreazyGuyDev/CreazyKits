package pl.creazy.creazykits.kit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.creazy.creazykits.CreazyKits;
import pl.creazy.creazylib.data.persistence.nbt.NbtEditor;
import pl.creazy.creazylib.util.key.Key;
import pl.creazy.creazylib.util.mc.Mc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Kit implements ConfigurationSerializable {
  private static final NamespacedKey DELAYS_KEY = Key.create("delays", CreazyKits.class);

  private final List<String> requiredPermissions = new ArrayList<>();
  private final List<ItemStack> items = new ArrayList<>();
  @Getter
  @Setter
  private long delay = 0L;
  @Getter
  private final String name;

  @SuppressWarnings("unchecked")
  public Kit(@NotNull Map<String, Object> data) {
    requiredPermissions.addAll((List<String>) data.get("required-permissions"));
    items.addAll((List<ItemStack>) data.get("items"));
    delay = ((Integer) data.get("delay")).longValue();
    name = (String) data.get("name");
  }

  @Override
  public @NotNull Map<String, Object> serialize() {
    return Map.of(
        "required-permissions", requiredPermissions,
        "items", items,
        "delay", delay,
        "name", name
    );
  }

  boolean give(@NotNull Player player) {
    return give(player, false);
  }

  boolean give(@NotNull Player player, boolean ignorePermission) {
    if (!canGet(player) && !ignorePermission) {
      return false;
    }
    Mc.addItems(player, getItems());
    var delays = getKitDelays(player);
    delays.setDelay(this);
    NbtEditor.of(player).set(DELAYS_KEY, delays);
    return true;
  }

  public @NotNull List<String> getRequiredPermissions() {
    return List.copyOf(requiredPermissions);
  }

  public void addRequiredPermissions(@NotNull String... permissions) {
    requiredPermissions.addAll(Arrays.asList(permissions));
  }

  public @NotNull List<ItemStack> getItems() {
    return items.stream().map(ItemStack::clone).toList();
  }

  public void addItems(@NotNull ItemStack... items) {
    this.items.addAll(Arrays.asList(items));
  }

  public boolean canGet(@NotNull Player player) {
    for (var permission : requiredPermissions) {
      if (!player.hasPermission(permission)) {
        return false;
      }
    }
    return getKitDelays(player).canGetKit(this);
  }

  void clearItems() {
    items.clear();
  }

  private @NotNull KitDelays getKitDelays(@NotNull Player player) {
    return NbtEditor.of(player).find(DELAYS_KEY, KitDelays.class).orElse(new KitDelays());
  }
}
