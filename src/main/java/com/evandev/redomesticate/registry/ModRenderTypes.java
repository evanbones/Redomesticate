package com.evandev.redomesticate.registry;

//? if >=26.1 {
/*import com.evandev.redomesticate.Constants;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
//? if >=26.2 {
/^import com.mojang.blaze3d.PrimitiveTopology;
import net.minecraft.client.renderer.BindGroupLayouts;
^///?} else {
import com.mojang.blaze3d.shaders.UniformType;
//?}
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import org.joml.Matrix4f;

/^*
 * Custom render types. 26.1+ builds them from render pipelines; the shaders are the vanilla glint and end portal ones.
 ^/
public class ModRenderTypes {
    //? if >=26.2 {
    /^private static final RenderPipeline.Snippet GLINT_SNIPPET = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            //? if >=26.3 {
            /^¹.withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS)
            ¹^///?} else {
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            //?}
            .withBindGroupLayout(BindGroupLayouts.FOG)
            .withVertexShader("core/glint")
            .withFragmentShader("core/glint")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .buildSnippet();
    ^///?} else {
    private static final RenderPipeline.Snippet GLINT_SNIPPET = RenderPipeline.builder()
            .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withUniform("Fog", UniformType.UNIFORM_BUFFER)
            .withUniform("Globals", UniformType.UNIFORM_BUFFER)
            .withVertexShader("core/glint")
            .withFragmentShader("core/glint")
            .withSampler("Sampler0")
            .withCull(false)
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
            .buildSnippet();
    //?}

    /^* Additive, depth tested glint used by the psychic wall and healing aura. ^/
    private static final RenderPipeline GLINT_ADDITIVE = RenderPipeline.builder(GLINT_SNIPPET)
            .withLocation(id("pipeline/glint_additive"))
            .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .build();

    /^* Opaque glint-shaded geometry used by the shadow hands. ^/
    private static final RenderPipeline GLINT_OPAQUE = RenderPipeline.builder(GLINT_SNIPPET)
            .withLocation(id("pipeline/glint_opaque"))
            .withColorTargetState(ColorTargetState.DEFAULT)
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, true))
            .build();

    public static final RenderType VOID_CLOUD = RenderType.create("void_cloud", RenderSetup.builder(RenderPipelines.END_PORTAL)
            .withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
            .withTexture("Sampler1", id("textures/void_cloud.png"))
            .createRenderSetup());
    public static final RenderType PSYCHIC_WALL_BORDER = RenderType.create("psychic_wall_border", RenderSetup.builder(GLINT_ADDITIVE)
            .withTexture("Sampler0", id("textures/psychic_wall_border.png"))
            .sortOnUpload()
            .createRenderSetup());
    public static final RenderType HEALING_AURA = RenderType.create("healing_aura", RenderSetup.builder(GLINT_ADDITIVE)
            .withTexture("Sampler0", id("textures/healing_aura.png"))
            .sortOnUpload()
            .createRenderSetup());
    public static final RenderType IFRAME_GLINT = RenderType.create("iframe_glint", RenderSetup.builder(RenderPipelines.GLINT)
            .withTexture("Sampler0", id("textures/immunity_frame_overlay.png"))
            .setTextureTransform(new TextureTransform("iframe_texturing", () -> scrollingMatrix(3, 7L, (float) (Math.PI / 4F), false)))
            .createRenderSetup());
    public static final RenderType SHADOW_HAND_ENTITY = RenderType.create("shadow_hand_entity", RenderSetup.builder(GLINT_OPAQUE)
            .withTexture("Sampler0", id("textures/shadow_hand.png"))
            .setTextureTransform(new TextureTransform("shadow_hand_texturing", () -> scrollingMatrix(0.5F, 2L, 0, true)))
            .useLightmap()
            .createRenderSetup());
    public static final RenderType PSYCHIC_WALL = RenderType.create("psychic_wall", RenderSetup.builder(GLINT_ADDITIVE)
            .withTexture("Sampler0", id("textures/psychic_wall_overlay.png"))
            .setTextureTransform(new TextureTransform("psychic_wall_texturing", ModRenderTypes::psychicWallMatrix))
            .sortOnUpload()
            .createRenderSetup());

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }

    private static Matrix4f scrollingMatrix(float scale, long speed, float rotation, boolean downwards) {
        long i = Util.getMillis() * speed;
        float offset = (float) (i % 30000L) / 30000.0F;
        Matrix4f matrix4f = new Matrix4f().translation(0, downwards ? -offset : offset, 0.0F);
        return matrix4f.rotateZ(rotation).scale(scale);
    }

    private static Matrix4f psychicWallMatrix() {
        long i = Util.getMillis() * 2;
        float f2 = (float) Math.sin(i / 20000.0F) + 1.0F;
        float f3 = (float) Math.cos(i / 20000.0F) + 1.0F;
        return new Matrix4f().translation(1 + f2, 1 + f3, 0.0F).scale(0.5F);
    }
}
*///?} else {
import com.evandev.redomesticate.Constants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class ModRenderTypes extends RenderType {
    public static final RenderType VOID_CLOUD = create("void_cloud", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder().setShaderState(RENDERTYPE_END_PORTAL_SHADER).setTextureState(MultiTextureStateShard.builder().add(TheEndPortalRenderer.END_SKY_LOCATION, false, false).add(ResourceLocation.parse(Constants.MOD_ID + ":textures/void_cloud.png"), false, false).build()).setLightmapState(LIGHTMAP).createCompositeState(false));
    public static final RenderType PSYCHIC_WALL_BORDER = create("psychic_wall2", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_SHADER).setTextureState(new TextureStateShard(ResourceLocation.parse(Constants.MOD_ID + ":textures/psychic_wall_border.png"), true, false)).setLightmapState(LIGHTMAP).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(true));
    public static final RenderType HEALING_AURA = create("healing_aura", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_SHADER).setTextureState(new TextureStateShard(ResourceLocation.parse(Constants.MOD_ID + ":textures/healing_aura.png"), true, false)).setLightmapState(LIGHTMAP).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setWriteMaskState(COLOR_DEPTH_WRITE).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(true));
    public static final RenderType TRANSLUCENT_NO_CULL = translucentNoCull();
    protected static final TexturingStateShard IFRAME_TEXTURING = new TexturingStateShard("entity_glint_texturing", () -> {
        setupIframeShading(3, 7L);
    }, RenderSystem::resetTextureMatrix);
    public static final RenderType IFRAME_GLINT = create("iframe_glint", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER).setTextureState(new TextureStateShard(ResourceLocation.parse(Constants.MOD_ID + ":textures/immunity_frame_overlay.png"), true, false)).setWriteMaskState(COLOR_DEPTH_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(IFRAME_TEXTURING).setOverlayState(OVERLAY).createCompositeState(true));
    protected static final TexturingStateShard SHADOW_HAND_TEXTURING = new TexturingStateShard("entity_glint_texturing", () -> {
        setupShadowHandShading(0.5F, 2L);
    }, RenderSystem::resetTextureMatrix);
    public static final RenderType SHADOW_HAND_ENTITY = create("shadow_hand_entity", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER).setTextureState(new TextureStateShard(ResourceLocation.parse(Constants.MOD_ID + ":textures/shadow_hand.png"), false, false)).setWriteMaskState(COLOR_DEPTH_WRITE).setTexturingState(SHADOW_HAND_TEXTURING).setLightmapState(LIGHTMAP).setCullState(RenderStateShard.NO_CULL).createCompositeState(false));
    protected static final TexturingStateShard PSYCHIC_WALL_TEXTURING = new TexturingStateShard("entity_glint_texturing", () -> {
        setupPsychicWallTexturing(0.5F, 40L);
    }, RenderSystem::resetTextureMatrix);
    public static final RenderType PSYCHIC_WALL = create("psychic_wall2", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_GLINT_SHADER).setTextureState(new TextureStateShard(ResourceLocation.parse(Constants.MOD_ID + ":textures/psychic_wall_overlay.png"), false, false)).setTexturingState(PSYCHIC_WALL_TEXTURING).setLightmapState(LIGHTMAP).setCullState(RenderStateShard.NO_CULL).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(true));

    public ModRenderTypes(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    public static RenderType getZombieOverlay(ResourceLocation texture, int x, int y) {
        return create("zombie_overlay", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, true, true, CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER)
                .setTextureState(new TextureStateShard(ResourceLocation.parse(Constants.MOD_ID + ":textures/zombie_overlay.png"), false, false)).setWriteMaskState(COLOR_DEPTH_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setTexturingState(new ZombieTexturing("zombie", x, y)).setOverlayState(OVERLAY).createCompositeState(true));
    }

    private static RenderType translucentNoCull() {
        RenderType type = create("translucent_no_cull", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, true, CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(RENDERTYPE_TRANSLUCENT_SHADER).setCullState(NO_CULL).setTextureState(BLOCK_SHEET_MIPPED).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(TRANSLUCENT_TARGET).createCompositeState(true));
        return type;
    }

    private static void setupShadowHandShading(float in, long time) {
        long i = Util.getMillis() * time;
        float f = (float) (i % 110000L) / 110000.0F;
        float f1 = (float) (i % 30000L) / 30000.0F;
        Matrix4f matrix4f = (new Matrix4f()).translation(0, -f1, 0.0F);
        matrix4f.scale(in);
        RenderSystem.setTextureMatrix(matrix4f);
    }

    private static void setupIframeShading(float in, long time) {
        long i = Util.getMillis() * time;
        float f1 = (float) (i % 30000L) / 30000.0F;
        Matrix4f matrix4f = (new Matrix4f()).translation(0, f1, 0.0F);
        matrix4f.rotateZ((float) (Math.PI / 4F)).scale(in);
        RenderSystem.setTextureMatrix(matrix4f);
    }

    private static void setupPsychicWallTexturing(float in, long time) {
        long i = Util.getMillis() * 2;
        float f1 = (float) (i % 30000L) / 30000.0F;
        float f2 = (float) Math.sin(i / 20000.0F) + 1.0F;
        float f3 = (float) Math.cos(i / 20000.0F) + 1.0F;
        Matrix4f matrix4f = (new Matrix4f()).translation(1 + f2, 1 + f3, 0.0F);
        matrix4f.scale(in);
        RenderSystem.setTextureMatrix(matrix4f);
    }

    private static class ZombieTexturing extends TexturingStateShard {

        public ZombieTexturing(String name, int x, int y) {
            super(name, () -> setupZombieTexturing(x, y), RenderSystem::resetTextureMatrix);
        }

        private static void setupZombieTexturing(int x, int y) {
            Matrix4f matrix4f = (new Matrix4f()).translation(0.0F, 0.0F, 0.0F);
            matrix4f.scale(x / 64F, y / 64F, 1);
            RenderSystem.setTextureMatrix(matrix4f);
        }
    }
}
//?}
