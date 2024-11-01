package pl.creazy.creazykits.kit;

import lombok.NoArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class KitDelays implements Serializable {
  private final Map<String, Long> delays = new HashMap<>();

  public boolean canGetKit(@NotNull Kit kit) {
    var delay = delays.get(kit.getName());
    if (delay == null) {
      return true;
    }
    return delay + kit.getDelay() <= System.currentTimeMillis();
  }

  public void setDelay(@NotNull Kit kit) {
    delays.put(kit.getName(), System.currentTimeMillis());
  }
}
