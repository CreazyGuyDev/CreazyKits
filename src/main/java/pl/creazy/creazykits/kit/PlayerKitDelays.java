package pl.creazy.creazykits.kit;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PlayerKitDelays implements ConfigurationSerializable {
  private final Map<String, KitDelay> delays = new HashMap<>();

  public PlayerKitDelays(@NotNull Map<String, Object> data) {

  }

  public @Nullable KitDelay getDelay(@NotNull String kitName) {
    return delays.get(kitName);
  }

  public void setDelay(@NotNull String kitName, @NotNull KitDelay delay) {
    delays.put(kitName, delay);
  }

  @Override
  public @NotNull Map<String, Object> serialize() {
    return Map.copyOf(delays);
  }
}
