package WolfShotz.Wyrmroost.content.entities.dragon.rooststalker;

import WolfShotz.Wyrmroost.content.entities.dragon.AbstractDragonRenderer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("deprecation")
public class RoostStalkerRenderer extends AbstractDragonRenderer<RoostStalkerEntity>
{
    private final ResourceLocation BODY = location("body.png");
    private final ResourceLocation BODY_SPE = location("body_spe.png");
    private final ResourceLocation BODY_XMAS = location("body_christmas.png");
    private final ResourceLocation BODY_GLOW = location("body_glow.png");
    private final ResourceLocation BODY_SPE_GLOW = location("body_spe_glow.png");
    private final ResourceLocation SLEEP = location("sleep.png");
    
    public RoostStalkerRenderer(EntityRendererManager manager) {
        super(manager, new RoostStalkerModel(), 0.5f);
        addLayer(new ItemStackRenderer());
        addLayer(new GlowLayer(this, stalker -> stalker.isSpecial()? BODY_SPE_GLOW : BODY_GLOW));
        addLayer(new SleepLayer(this, SLEEP));
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(RoostStalkerEntity entity) {
        if (isChristmas) return BODY_XMAS;
        return entity.isSpecial()? BODY_SPE : BODY;
    }
    
    @Override
    public String getResourceDirectory() { return DEF_LOC + "rooststalker/"; }
    
    class ItemStackRenderer extends AbstractLayerRenderer
    {
        ItemStackRenderer() { super(RoostStalkerRenderer.this); }
        
        @Override
        public void render(RoostStalkerEntity stalker, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            ItemStack stack = stalker.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            
            if (!stack.isEmpty()) {
                float i = stalker.isChild()? 1f : 0;
                
                GlStateManager.pushMatrix();
                
                GlStateManager.rotatef(netHeadYaw / 3f, 0, 1f, 0);
                GlStateManager.rotatef(90, 1, 0, 0);
                
                if (stalker.isSleeping() && stalker.getAnimation() != RoostStalkerEntity.WAKE_ANIMATION) {
                    GlStateManager.translatef(-0.5f - (i * 2.8f), -0.6f - (i * 1.8f), -1.49f);
                    GlStateManager.rotatef(240, 0, 0, 1);
                } else {
                    GlStateManager.translatef(0, -0.5f - (i * -0.4f), (stalker.isSitting()? -1.3f : -1.2f) - (i * 0.135f));
                    GlStateManager.rotatef(headPitch / (1.7f - (i * -1f)), 1f, 0, 0);
                    GlStateManager.translatef(0, -0.3f, 0f);
                }
                if (stalker.isChild()) GlStateManager.scalef(0.45f, 0.45f, 0.45f);
                
                Minecraft.getInstance().getItemRenderer().renderItem(stack, stalker, ItemCameraTransforms.TransformType.GROUND, false);
                
                GlStateManager.popMatrix();
            }
        }
    }
}
