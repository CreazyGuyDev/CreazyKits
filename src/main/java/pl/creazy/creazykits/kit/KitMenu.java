package pl.creazy.creazykits.kit;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.creazy.creazykits.CreazyKits;
import pl.creazy.creazylib.part.constraints.Injected;
import pl.creazy.creazylib.screen.menu.ContextPlayerMenu;
import pl.creazy.creazylib.screen.menu.MenuPage;
import pl.creazy.creazylib.screen.menu.constraints.Menu;

@Menu
class KitMenu implements ContextPlayerMenu<KitMenuContext> {
  @Injected
  private KitManager kitManager;

  @Injected
  private CreazyKits plugin;

  @Override
  public @NotNull MenuPage getPage(int i, @NotNull Player player) {
    return new KitMenuPages.CreatePage(kitManager, plugin);
  }
}
