package org.lin114514.fsplugin.fsLib;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class sideBar {

    // 用来存储每个玩家的计分板
    private static final HashMap<UUID, FastBoard> boards = new HashMap<>();
    // 如果 FastBoard 初始化失败，标记为不可用以避免重复报错
    private static boolean fastBoardAvailable = true;

    public static void updSideBar(Player p) {
        // 如果已经检测到 FastBoard 无法使用，直接返回
        if (!fastBoardAvailable) return;
        if (FsLib.economy == null) {
            p.sendMessage("§c经济系统未加载！");
            return;
        }

        FastBoard board;
        try {
            // 如果玩家已有计分板，直接更新
            if (boards.containsKey(p.getUniqueId())) {
                board = boards.get(p.getUniqueId());
            } else {
                // 没有就新建
                board = new FastBoard(p);
                boards.put(p.getUniqueId(), board);
            }
        } catch (Throwable t) {
            // FastBoard 类在当前环境可能不兼容或初始化失败，记录一次日志并禁用后续侧边栏更新
            fastBoardAvailable = false;
            Bukkit.getLogger().severe("[FsLib] FastBoard 初始化失败，已禁用侧边栏: " + t);
            t.printStackTrace();
            return;
        }

        // 设置标题
        board.updateTitle("§6§lFutureServer");

        double money = FsLib.economy.getBalance(p);

        // 设置内容（右边永远无数字）
        board.updateLines(
                "§7--------------------",
                "§f玩家: §a" + p.getName(),
                "§f在线: §b" + Bukkit.getOnlinePlayers().size(),
                "§f钱数: §a" + Math.round(money),
                "",
                "§7fs.lin114514.top"
        );
    }
}