package com.chayewuu.dieiftossed.client.gui;

import com.chayewuu.dieiftossed.DieIfTossed;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

/**
 * 两个确认 GUI 的抽象基类，封装共有的渲染逻辑：
 * - 顶部白色标题
 * - 右上角 GitHub 图标（点击打开仓库）
 * - GitHub 图标正下方的哔哩哔哩图标（点击打开主页）
 *
 * 图标设计：底层为 Minecraft 原生按钮纹理（widget/button 或 widget/button_highlighted），
 * 顶层为纯白色 logo。鼠标悬停时切换为 highlighted 版本并播放点击音效。
 */
public abstract class BasePromptScreen extends Screen {
    private static final Identifier GITHUB_ICON =
            Identifier.fromNamespaceAndPath(DieIfTossed.MOD_ID, "textures/gui/github_icon.png");
    private static final Identifier BILIBILI_ICON =
            Identifier.fromNamespaceAndPath(DieIfTossed.MOD_ID, "textures/gui/bilibili_icon.png");

    private static final Identifier BUTTON_SPRITE = Identifier.withDefaultNamespace("widget/button");
    private static final Identifier BUTTON_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("widget/button_highlighted");

    private static final String GITHUB_URL = "https://github.com/ChaYeWuu/dieiftossed";
    private static final String BILIBILI_URL = "https://space.bilibili.com/698351214";

    private static final int BUTTON_SIZE = 20;
    private static final int ICON_SIZE = 14;
    private static final int ICON_OFFSET = (BUTTON_SIZE - ICON_SIZE) / 2;
    private static final int MARGIN = 6;
    private static final int SPACING = 2;
    private static final int TEX_SIZE = 64;
    private static final int TITLE_Y = 40;

    private int githubX, githubY;
    private int bilibiliX, bilibiliY;

    protected BasePromptScreen(Component title) {
        super(title);
    }

    protected abstract String getTitleText();
    protected abstract void addButtons(int startY);

    @Override
    protected void init() {
        super.init();
        this.githubX = this.width - BUTTON_SIZE - MARGIN;
        this.githubY = MARGIN;
        this.bilibiliX = this.githubX;
        this.bilibiliY = this.githubY + BUTTON_SIZE + SPACING;
        addButtons(this.height / 2 - 10);
    }

    /** 判断鼠标是否悬停在指定图标上 */
    private boolean isHovered(int mouseX, int mouseY, int x, int y) {
        return mouseX >= x && mouseX < x + BUTTON_SIZE && mouseY >= y && mouseY < y + BUTTON_SIZE;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, getTitleText(), this.width / 2, TITLE_Y, 0xFFFFFFFF);

        // GitHub：根据悬停状态切换按钮纹理
        boolean ghHover = isHovered(mouseX, mouseY, this.githubX, this.githubY);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                ghHover ? BUTTON_HIGHLIGHTED_SPRITE : BUTTON_SPRITE,
                this.githubX, this.githubY, BUTTON_SIZE, BUTTON_SIZE);
        graphics.blit(RenderPipelines.GUI_TEXTURED, GITHUB_ICON,
                this.githubX + ICON_OFFSET, this.githubY + ICON_OFFSET, 0f, 0f,
                ICON_SIZE, ICON_SIZE, TEX_SIZE, TEX_SIZE, TEX_SIZE, TEX_SIZE, -1);

        // B站：根据悬停状态切换按钮纹理
        boolean blHover = isHovered(mouseX, mouseY, this.bilibiliX, this.bilibiliY);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                blHover ? BUTTON_HIGHLIGHTED_SPRITE : BUTTON_SPRITE,
                this.bilibiliX, this.bilibiliY, BUTTON_SIZE, BUTTON_SIZE);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BILIBILI_ICON,
                this.bilibiliX + ICON_OFFSET, this.bilibiliY + ICON_OFFSET, 0f, 0f,
                ICON_SIZE, ICON_SIZE, TEX_SIZE, TEX_SIZE, TEX_SIZE, TEX_SIZE, -1);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean inside) {
        if (event.button() == 0) {
            double mx = event.x();
            double my = event.y();
            if (inRect(mx, my, this.githubX, this.githubY, BUTTON_SIZE, BUTTON_SIZE)) {
                // 播放原版按钮点击音效
                AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                openUrl(GITHUB_URL);
                return true;
            }
            if (inRect(mx, my, this.bilibiliX, this.bilibiliY, BUTTON_SIZE, BUTTON_SIZE)) {
                AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                openUrl(BILIBILI_URL);
                return true;
            }
        }
        return super.mouseClicked(event, inside);
    }

    private static boolean inRect(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private static void openUrl(String url) {
        try {
            Util.getPlatform().openUri(url);
        } catch (Exception ignored) {
        }
    }
}
