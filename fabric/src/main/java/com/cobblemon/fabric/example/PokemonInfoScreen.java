package com.cobblemon.fabric.example;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import com.cobblemon.mod.common.pokemon.Pokemon;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Dimension;
import java.util.List;

public class PokemonInfoScreen extends Screen {
    private final Pokemon pokemon;
    private ResourceLocation pokemonSprite;
    private ResourceLocation originMark;
    public static final Logger LOGGER = LoggerFactory.getLogger("CobbleExtraData");

    protected PokemonInfoScreen(Pokemon pokemon) {
        super(Component.literal("Pokémon Info"));
        this.pokemon = pokemon;
        String originMarkSprite = pokemon.getPersistentData().contains("OriginGame")
                ? pokemon.getPersistentData().getString("OriginGame").toLowerCase()
                : "cobblemon";

        // Determine the sprite path based on whether the Pokémon is shiny and its form
        String speciesName = pokemon.getSpecies().getName().toLowerCase();
        String formName = pokemon.getForm().getName().toLowerCase();
        String spritePath = "textures/sprites/";

        // Check if the Pokémon is shiny
        if (pokemon.getShiny()) {
            spritePath += "shiny/";
        } else {
            spritePath += "regular/";
        }

        // Append the form name if it exists (e.g., "meowth-alola")
        if (!formName.isEmpty() && !formName.equals("normal")) {
            speciesName += "-" + formName;
        }

        // Construct the sprite path
        this.pokemonSprite = ResourceLocation.fromNamespaceAndPath("cobblemonextradata", spritePath + speciesName + ".png");
        this.originMark = ResourceLocation.fromNamespaceAndPath("cobblemonextradata", "textures/origin/" + originMarkSprite + ".png");
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        // Render the background with the correct arguments
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        // Define the position and size of the background panel
        int panelWidth = 220;
        int panelHeight = 120;
        int x = (this.width - panelWidth) / 2;
        int y = (this.height - panelHeight) / 2;

        // Size of the Pokémon sprite
        int spriteWidth = 68;
        int spriteHeight = 56;

        // Compute the coordinates to center the sprite horizontally and position it properly
        int spriteX = (this.width - spriteWidth) / 2;
        int spriteY = y - spriteHeight;

        // Draw the Pokémon sprite
        drawTexture(guiGraphics, pokemonSprite, spriteX, spriteY, spriteWidth, spriteHeight);

        // Draw a semi-transparent background panel with a shadow
        guiGraphics.fill(x, y, x + panelWidth, y + panelHeight, 0x80000000);
        guiGraphics.fill(x + 2, y + 2, x + panelWidth + 2, y + panelHeight + 2, 0x20000000);

        // Draw a border around the panel
        guiGraphics.fill(x - 1, y - 1, x + panelWidth + 1, y, 0xFFAAAAAA);
        guiGraphics.fill(x - 1, y + panelHeight, x + panelWidth + 1, y + panelHeight + 1, 0xFFAAAAAA);
        guiGraphics.fill(x - 1, y, x, y + panelHeight, 0xFFAAAAAA);
        guiGraphics.fill(x + panelWidth, y, x + panelWidth + 1, y + panelHeight, 0xFFAAAAAA);

        // Draw the Pokémon data on the screen
        int textX = x + 10;
        int textY = y + 10;

        // Title: Pokémon Info (with shadow)
        guiGraphics.drawString(this.font, "Pokémon Info", textX, textY, 0xFFFFFF, true);

        // Origin Game (with shadow)
        String originGame = pokemon.getPersistentData().contains("OriginGame")
                ? pokemon.getPersistentData().getString("OriginGame").toLowerCase()
                : "cobblemon";
        String regionName = OriginGameMapper.getRegion(originGame);
        guiGraphics.drawString(this.font, "This Pokémon originated from " + regionName, textX, textY + 20, 0xFFFFFF, true);

        // Met Date (with shadow)
        String dateString = pokemon.getPersistentData().contains("MetDate")
                ? pokemon.getPersistentData().getString("MetDate")
                : "Unknown";
        guiGraphics.drawString(this.font, dateString, textX, textY + 40, 0xFFFFFF, true);

        // Met Location (with shadow)
        String metLocation = pokemon.getPersistentData().getString("MetLocation");
        if (!metLocation.contains(":")) {
            metLocation = "Cobblemon Transporter";
        } else {
            metLocation = metLocation.substring(metLocation.indexOf(":") + 1);
            metLocation = metLocation.replace("_", " ");
            metLocation = capitalizeEachWord(metLocation);
        }
        guiGraphics.drawString(this.font, metLocation, textX, textY + 60, 0xFFFFFF, true);

        // Met Level (with shadow)
        int metLevel = pokemon.getPersistentData().contains("MetLevel")
                ? pokemon.getPersistentData().getInt("MetLevel")
                : pokemon.getLevel();
        guiGraphics.drawString(this.font, "Met at Lv. " + metLevel + ".", textX, textY + 80, 0xFFFFFF, true);

        // Draw the origin mark at the bottom right of the panel
        int originMarkSize = 16;
        int originMarkX = x + panelWidth - originMarkSize - 5;
        int originMarkY = y + panelHeight - originMarkSize - 5;

        drawTexture(guiGraphics, originMark, originMarkX, originMarkY, originMarkSize, originMarkSize);

        /* COMMENTED OUT: Marks/Ribbons display functionality
        // Fetch the list of marks from persistent data
        CompoundTag data = pokemon.getPersistentData();
        if (data.contains("Ribbons", 11)) {
            IntArrayTag ribbons = (IntArrayTag) data.get("Ribbons");
            assert ribbons != null;
            int[] ribbonIds = ribbons.getAsIntArray();

            // Ribbon settings
            int markSize = 16; // Size of each ribbon icon
            int markSpacing = 5; // Spacing between ribbon icons
            int maxRibbonsPerRow = 6; // Maximum number of ribbons per row
            int ribbonPanelPadding = 10; // Padding around the ribbon panel
            int ribbonPanelMargin = 20; // Additional margin between the main box and ribbon box

            // Calculate the number of rows needed
            int numRows = (int) Math.ceil((double) ribbonIds.length / maxRibbonsPerRow);

            // Calculate the total width and height of the ribbon panel
            int ribbonPanelWidth = Math.min(ribbonIds.length, maxRibbonsPerRow) * (markSize + markSpacing) - markSpacing;
            int ribbonPanelHeight = numRows * (markSize + markSpacing) - markSpacing;

            // Position the ribbon panel below the main box with additional margin
            int ribbonPanelX = x + (panelWidth - ribbonPanelWidth) / 2; // Center horizontally
            int ribbonPanelY = y + panelHeight + ribbonPanelMargin; // Position below the main box with margin

            // Draw a semi-transparent background for the ribbon panel
            guiGraphics.fill(
                    ribbonPanelX - ribbonPanelPadding,
                    ribbonPanelY - ribbonPanelPadding,
                    ribbonPanelX + ribbonPanelWidth + ribbonPanelPadding,
                    ribbonPanelY + ribbonPanelHeight + ribbonPanelPadding,
                    0x80000000
            );

            // Draw a border around the ribbon panel
            guiGraphics.fill(
                    ribbonPanelX - ribbonPanelPadding - 1,
                    ribbonPanelY - ribbonPanelPadding - 1,
                    ribbonPanelX + ribbonPanelWidth + ribbonPanelPadding + 1,
                    ribbonPanelY - ribbonPanelPadding,
                    0xFFAAAAAA
            );
            guiGraphics.fill(
                    ribbonPanelX - ribbonPanelPadding - 1,
                    ribbonPanelY + ribbonPanelHeight + ribbonPanelPadding,
                    ribbonPanelX + ribbonPanelWidth + ribbonPanelPadding + 1,
                    ribbonPanelY + ribbonPanelHeight + ribbonPanelPadding + 1,
                    0xFFAAAAAA
            );
            guiGraphics.fill(
                    ribbonPanelX - ribbonPanelPadding - 1,
                    ribbonPanelY - ribbonPanelPadding,
                    ribbonPanelX - ribbonPanelPadding,
                    ribbonPanelY + ribbonPanelHeight + ribbonPanelPadding,
                    0xFFAAAAAA
            );
            guiGraphics.fill(
                    ribbonPanelX + ribbonPanelWidth + ribbonPanelPadding,
                    ribbonPanelY - ribbonPanelPadding,
                    ribbonPanelX + ribbonPanelWidth + ribbonPanelPadding + 1,
                    ribbonPanelY + ribbonPanelHeight + ribbonPanelPadding,
                    0xFFAAAAAA
            );

            // Draw the ribbons
            int currentRow = 0;
            int currentCol = 0;
            for (int markId : ribbonIds) {
                ResourceLocation markTexture = MarkMapper.getMarkTexture(markId);

                if (markTexture != null) {
                    int ribbonX = ribbonPanelX + currentCol * (markSize + markSpacing);
                    int ribbonY = ribbonPanelY + currentRow * (markSize + markSpacing);

                    drawTexture(guiGraphics, markTexture, ribbonX, ribbonY, markSize, markSize);

                    // Add tooltip for the ribbon
                    if (mouseX >= ribbonX && mouseX < ribbonX + markSize && mouseY >= ribbonY && mouseY < ribbonY + markSize) {
                        String ribbonName = MarkMapper.getMarkName(markId);
                        guiGraphics.renderTooltip(this.font, Component.literal(ribbonName), mouseX, mouseY);
                    }

                    // Move to the next column
                    currentCol++;
                    if (currentCol >= maxRibbonsPerRow) {
                        currentCol = 0;
                        currentRow++;
                    }
                }
            }
        }
        */
    }

    // Helper method to capitalize each word
    private String capitalizeEachWord(String text) {
        String[] words = text.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            capitalized.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1).toLowerCase())
                    .append(" ");
        }
        return capitalized.toString().trim();
    }

    private void drawTexture(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        guiGraphics.blit(texture, x, y, 0, 0, width, height, width, height);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}