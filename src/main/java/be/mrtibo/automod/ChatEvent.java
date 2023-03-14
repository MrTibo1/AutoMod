package be.mrtibo.automod;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class ChatEvent implements @NotNull Listener {

    public ChatEvent(){
        Automod.getInstance().getServer().getPluginManager().registerEvents(this, Automod.getInstance());
    }

    @EventHandler
    public void onChat(AsyncChatEvent e){

        String message = PlainTextComponentSerializer.plainText().serialize(e.message());
        Automod.getInstance().getLogger().info("[MESSAGE] " + e.getPlayer().getName() + ": " + message);
        Moderate mod = new Moderate(message);

        if(mod.getResult().getResult() == Result.FLAGGED){
            e.setCancelled(true);
            Component flag = deserialize("<red>MESSAGE FLAGGED! <gray>For: " + mod.getResult().getReason() + ". Watch your profanity!");
            e.getPlayer().sendMessage(flag);
            Automod.getInstance().getLogger().warning("Message by " + e.getPlayer().getName() + " flagged for " + mod.getResult().getReason() + ".");
            Component modFlag = deserialize("<red>Message by <yellow>" + e.getPlayer().getName() + "<red> flagged for <yellow>" + mod.getResult().getReason() + "<red>.");
            Bukkit.broadcast(modFlag, "automod.flags");
        }

    }

    private Component deserialize(String in){
        return MiniMessage.miniMessage().deserialize(in);
    }

}
