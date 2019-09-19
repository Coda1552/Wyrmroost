package WolfShotz.Wyrmroost.util.network;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonEntity;
import WolfShotz.Wyrmroost.util.utils.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Created by WolfShotz - 8/9/2019 - 02:03
 *
 * Class Handling the packet sending of keybind inputs.
 * keybinds are assigned an int, and as such follow the following format:
 *      0: Generic Attack
 *      1: Special Attack start
 *      2: Special Attack end
 *      3: Call Dragon
 */
public class DragonKeyBindMessage
{
    public static final int PERFORM_GENERIC_ATTACK = 0;
    public static final int START_SPECIAL_ATTACK = 1;
    public static final int END_SPECIAL_ATTACK = 2;
    public static final int CALL_DRAGON = 3;
    
    private int dragonID;
    private int key;
    
    public DragonKeyBindMessage(AbstractDragonEntity entity, int key) {
        this.dragonID = entity.getEntityId();
        this.key = key;
    }
    
    public DragonKeyBindMessage(PacketBuffer buf) {
        dragonID = buf.readInt();
        key = buf.readInt();
    }
    
    public void encode(PacketBuffer buf) {
        buf.writeInt(dragonID);
        buf.writeInt(key);
    }
    
    public void handle(Supplier<NetworkEvent.Context> context) {
        World world = context.get().getSender().world;
        AbstractDragonEntity dragon = (AbstractDragonEntity) world.getEntityByID(dragonID);
        
        switch (key) {
            case 0: dragon.performGenericAttack(); break;
            case 1: dragon.performSpecialAttack(true); break;
            case 2: dragon.performSpecialAttack(false); break;
            case 3: dragon.callDragon(null); break;
            default: ModUtils.L.error("Unknown KeyPress packet key... wat?");
            
        }
    }
}