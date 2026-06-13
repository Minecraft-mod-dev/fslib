package org.lin114514.fsplugin.fsLib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public final class FsLib extends JavaPlugin implements Listener, CommandExecutor {

    private final String[] logo = {
            "\u001B[32m I |   _____      _      _  _     \u001B[0m",
            "\u001B[32m I |  |  ___|___ | |    (_)| |__  \u001B[0m",
            "\u001B[32m I |  | |_  / __|| |    | || '_ \\ \u001B[0m",
            "\u001B[32m I |  |  _| \\__ \\| |___ | || |_) |\u001B[0m",
            "\u001B[32m I |  |_|   |___/|_____||_||_.__/ \u001B[0m"
    };

    public static Economy economy = null;

    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault兼容错误!插件将无法使用。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // 注册事件（现在能识别到 Listener）
        getServer().getPluginManager().registerEvents(this, this);
        // 注册命令
        this.getCommand("fsi").setExecutor(this);

        getLogger().info("\u001B[33mVault绑定完成!\u001B[0m");
        for (String line : logo) {
            getLogger().info(line);
        }
        getLogger().info("\u001B[31mfsLib已经成功激活!\u001B[0m");

        // 每2秒更新一次所有人的侧边栏（暂时保留，后面建议优化）
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                sideBar.updSideBar(p);
            }
        }, 0, 20 * 2);
    }

    @Override
    public void onDisable() {
        getLogger().info("\u001B[31mfsLib已经成功停用!\u001B[0m");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Bukkit.dispatchCommand(p, "fsi");
        sideBar.updSideBar(p);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("fsi")) return false;

        if (sender instanceof Player p) {
            p.sendMessage("§6" + p.getName() + "你好!欢迎来到FutureServer!");
            if (!p.hasPlayedBefore()) {
                p.sendMessage("§b[新手系统]§r检测到你是第一次上线，奖励即将派发!");
                p.getInventory().addItem(new ItemStack(Material.IRON_INGOT, 32));
                p.getInventory().addItem(new ItemStack(Material.DIAMOND, 5));
                p.getInventory().addItem(new ItemStack(Material.OAK_LOG, 16));
                p.sendMessage("§b[新手系统]§r派发成功!");
            }
            return true;
        } else if (sender instanceof ConsoleCommandSender) {
            getLogger().info("fsLib插件已经就绪!");
            return true;
        } else {
            // 其他发送者：返回 false，显示用法
            return false;
        }
    }
}