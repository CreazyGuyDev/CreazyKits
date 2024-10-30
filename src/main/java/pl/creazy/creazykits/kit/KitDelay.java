package pl.creazy.creazykits.kit;

import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
public class KitDelay implements ConfigurationSerializable {
  private final String kitName;
  private final long usedAt;
  private final long delay;

  public KitDelay(long delay, String kitName) {
    this.delay = delay;
    this.kitName = kitName;
    usedAt = System.currentTimeMillis();
  }

  public KitDelay(@NotNull Map<String, Object> data) {
    kitName = (String) data.get("kit-name");
    usedAt = (long) data.get("used-at");
    delay = (long) data.get("delay");
  }

  @Override
  public @NotNull Map<String, Object> serialize() {
    return Map.of(
        "kit-name", kitName,
        "used-at", usedAt,
        "delay", delay
    );
  }

  public boolean isExpired() {
    return usedAt + delay <= System.currentTimeMillis();
  }
}
