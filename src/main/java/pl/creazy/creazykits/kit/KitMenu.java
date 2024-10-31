package pl.creazy.creazykits.kit;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import pl.creazy.creazykits.CreazyKits;
import pl.creazy.creazylib.part.constraints.Injected;
import pl.creazy.creazylib.screen.menu.ContextPlayerMenu;
import pl.creazy.creazylib.screen.menu.MenuPage;
import pl.creazy.creazylib.screen.menu.constraints.Menu;
import pl.creazy.creazylib.util.menu.MenuContext;

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

  @Override
  public void onOpen(@NotNull InventoryOpenEvent event) {
    var inventory = event.getInventory();
    var context = MenuContext.get(inventory, KitMenu.class);
    var kit = kitManager.getKit(context.getKitName(), plugin);

    if (kit == null || !context.isUpdate()) {
      return;
    }

    var index = 0;
    var items = kit.getItems().iterator();

    for (var item : inventory.getContents()) {
      if (item == null && items.hasNext()) {
        inventory.setItem(index, items.next());
      }
      index++;
    }
  }
}
