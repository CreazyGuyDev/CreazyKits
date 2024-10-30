package pl.creazy.creazykits.kit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KitMenuContext {
  private Kit kit;
  private boolean isUpdate = false;
  private String kitName;
}
