package pl.creazy.creazykits.kit;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.creazy.creazykits.CreazyKits;
import pl.creazy.creazylib.id.Id;
import pl.creazy.creazylib.item.ItemBuilder;
import pl.creazy.creazylib.screen.menu.MenuPage;
import pl.creazy.creazylib.util.mc.Mc;
import pl.creazy.creazylib.util.menu.MenuContext;

import java.util.Arrays;
import java.util.Objects;

@UtilityClass
class KitMenuPages {
  @RequiredArgsConstructor
  static class CreatePage extends MenuPage {
    private final KitManager kitManager;
    private final CreazyKits plugin;

    private static final Id ICON_ID = new Id("icon", CreazyKits.class);

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
      var player = (Player) event.getWhoClicked();
      var context = MenuContext.get(event.getInventory(), KitMenu.class);
      var type = Mc.getTye(event.getCurrentItem());
      switch (type) {
        case LIME_DYE -> {
          var kit = context.getKit();
          var content = Arrays.stream(event.getInventory().getContents())
              .filter(Objects::nonNull)
              .filter(item -> !ICON_ID.equals(Id.get(item)))
              .toArray(ItemStack[]::new);
          if (context.isUpdate()) {
            kitManager.updateKit(context.getKitName(), content);
          } else {
            kit.addItems(content);
            kitManager.addKit(kit, plugin);
          }
          player.closeInventory();
          player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }
        case RED_DYE -> {
          player.closeInventory();
          player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
        }
      }
      if (type == Material.GRAY_STAINED_GLASS_PANE || type == Material.LIME_DYE || type == Material.RED_DYE) {
        event.setCancelled(true);
      }
    }

    @Override
    protected void setContent(@NotNull Player player) {
      var bg = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
          .setHideTooltip(true)
          .setId(ICON_ID)
          .build();
      var createIcon = new ItemBuilder(Material.LIME_DYE)
          .setDisplayName("&aPotwierdź")
          .setId(ICON_ID)
          .build();
      var cancelIcon = new ItemBuilder(Material.RED_DYE)
          .setDisplayName("&cAnuluj")
          .setId(ICON_ID)
          .build();
      setIcon(bg, 0, 1, 2, 4, 6, 7, 8, 9, 18, 27, 36, 45, 46, 47, 48, 49, 50, 51, 52, 53, 44, 35, 26, 17);
      setIcon(createIcon, 3);
      setIcon(cancelIcon, 5);
    }
  }
}
