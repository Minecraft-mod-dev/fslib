package org.lin114514.fsplugin.fsLib;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class sideBar {

    // 用来存储每个玩家的计分板
    private static final HashMap<UUID, FastBoard> boards = new HashMap<>();

    public static void updSideBar(Player p) {
        if (FsLib.economy == null) {
            p.sendMessage("§c经济系统未加载！");
            return;
        }

        FastBoard board;

        // 如果玩家已有计分板，直接更新
        if (boards.containsKey(p.getUniqueId())) {
            board = boards.get(p.getUniqueId());
        } else {
            // 没有就新建
            board = new FastBoard(p);
            boards.put(p.getUniqueId(), board);
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