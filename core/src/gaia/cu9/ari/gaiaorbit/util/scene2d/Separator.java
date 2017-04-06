/*
 * Copyright 2014-2016 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gaia.cu9.ari.gaiaorbit.util.scene2d;

import java.awt.PopupMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * A separator widget (horizontal or vertical bar) that can be used in menus, tables or other widgets, typically added
 * to new row with growX() (if creating horizontal separator) OR growY() (if creating vertical separator)
 * {@link PopupMenu} and {@link VisTable} provides utilities addSeparator() methods that adds new separator.
 * @author Kotcrab
 * @author langurmonkey
 * @since 0.1.0
 */
public class Separator extends Widget {
    private Skin skin;
    private SeparatorStyle style;

    /** New separator with default style */
    public Separator(Skin skin) {
        this.skin = skin;
        style = skin.get(SeparatorStyle.class);
    }

    public Separator(Skin skin, String styleName) {
        this.skin = skin;
        style = skin.get(styleName, SeparatorStyle.class);
    }

    public Separator(SeparatorStyle style) {
        this.style = style;
    }

    @Override
    public float getPrefHeight() {
        return style.thickness;
    }

    @Override
    public float getPrefWidth() {
        return style.thickness;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a * parentAlpha);
        style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
    }

    public SeparatorStyle getStyle() {
        return style;
    }

    static public class SeparatorStyle {
        public Drawable background;
        public int thickness;

        public SeparatorStyle() {
        }

        public SeparatorStyle(SeparatorStyle style) {
            this.background = style.background;
            this.thickness = style.thickness;
        }

        public SeparatorStyle(Drawable bg, int thickness) {
            this.background = bg;
            this.thickness = thickness;
        }
    }
}
