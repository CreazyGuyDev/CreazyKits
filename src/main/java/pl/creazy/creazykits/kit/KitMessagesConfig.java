package pl.creazy.creazykits.kit;

import lombok.Getter;
import pl.creazy.creazylib.data.persistence.config.constraints.ConfigFile;
import pl.creazy.creazylib.data.persistence.config.constraints.ConfigVar;

@Getter
@ConfigFile(name = "kits", path = "messages")
public class KitMessagesConfig {
  @ConfigVar("kit.created")
  private String kitCreated = "&aUtworzono nowy kit &e${NAME}";

  @ConfigVar("kit.already-exists")
  private String kitAlreadyExists = "&aKit &e${NAME} &ajuż istnieje";

  @ConfigVar("kit.not-exist")
  private String kitNotExist = "&aKit &e${NAME} &anie istnieje";

  @ConfigVar("kit.removed")
  private String kitRemoved = "&aKit &e${NAME} &azostał usunięty";

  @ConfigVar("kit.updated")
  private String kitUpdated = "&aKit &e${NAME} &azostał zaktualizowany";

  @ConfigVar("kit.gave")
  private String kitGave = "&aKit &e${NAME} &azostał dodany graczowi &e${PLAYER_NAME}";

  @ConfigVar("player.not-exist")
  private String playerNotExist = "&aGracz &e${PLAYER_NAME} &anie istnieje";

  @ConfigVar("kit.delay")
  private String kitDelay = "&aNie możesz dostawać kitu &e${NAME} &atak często";
}
