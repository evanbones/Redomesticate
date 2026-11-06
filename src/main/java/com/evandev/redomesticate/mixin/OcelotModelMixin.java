package com.evandev.redomesticate.mixin;

//? if <26.1 {
import com.evandev.redomesticate.api.ICommandableMob;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OcelotModel.class)
public abstract class OcelotModelMixin<T extends Entity> {

    @Shadow
    @Final
    protected ModelPart head;
    @Shadow
    @Final
    protected ModelPart body;
    @Shadow
    @Final
    protected ModelPart tail1;
    @Shadow
    @Final
    protected ModelPart tail2;
    @Shadow
    @Final
    protected ModelPart leftHindLeg;
    @Shadow
    @Final
    protected ModelPart rightHindLeg;
    @Shadow
    @Final
    protected ModelPart leftFrontLeg;
    @Shadow
    @Final
    protected ModelPart rightFrontLeg;
    @Shadow
    protected int state;

    @Inject(method = "prepareMobModel", at = @At("TAIL"))
    private void redomesticate$applySittingPose(T entity, float limbSwing, float limbSwingAmount, float partialTick, CallbackInfo ci) {
        if (entity instanceof ICommandableMob commandable && commandable.redomesticate$isStayingStill() && entity.getType() == EntityType.OCELOT) {
            this.body.xRot = ((float) Math.PI / 4F);
            this.body.y += -4.0F;
            this.body.z += 5.0F;
            this.head.y += -3.3F;
            ++this.head.z;
            this.tail1.y += 8.0F;
            this.tail1.z += -2.0F;
            this.tail2.y += 2.0F;
            this.tail2.z += -0.8F;
            this.tail1.xRot = 1.7278761F;
            this.tail2.xRot = 2.670354F;
            this.leftFrontLeg.xRot = -0.15707964F;
            this.leftFrontLeg.y = 16.1F;
            this.leftFrontLeg.z = -7.0F;
            this.rightFrontLeg.xRot = -0.15707964F;
            this.rightFrontLeg.y = 16.1F;
            this.rightFrontLeg.z = -7.0F;
            this.leftHindLeg.xRot = (-(float) Math.PI / 2F);
            this.leftHindLeg.y = 21.0F;
            this.leftHindLeg.z = 1.0F;
            this.rightHindLeg.xRot = (-(float) Math.PI / 2F);
            this.rightHindLeg.y = 21.0F;
            this.rightHindLeg.z = 1.0F;

            this.state = 3;
        }
    }
}
//?}
