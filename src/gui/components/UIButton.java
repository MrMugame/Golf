package gui.components;

import gui.UIComponent;
import gui.constraints.UICenterConstraint;
import gui.constraints.UIRelativeConstraint;

import java.awt.*;

// TODO: Scrap this
public class UIButton extends UIClickable {
    private String text;
    private Color background, foreground;
    private int fontSize;
    private String font;

    private UIText label;


    public UIButton(String text, Color background, Color foreground, String font, int fontSize) {
        this.text = text;
        this.background = background;
        this.foreground = foreground;
        this.fontSize = fontSize;
        this.font = font;
    }

    @Override
    protected void init() {
        // TODO: Add some kind of default font in assets
        label = new UIText(text, foreground, "roboto_black.ttf", fontSize, true);
        label.getConstraints().addX(new UICenterConstraint());
        label.getConstraints().addY(new UICenterConstraint());
        label.getConstraints().addWidth(new UIRelativeConstraint(0.75f));
        label.getConstraints().addHeight(new UIRelativeConstraint(1));

        UIComponent block = new UIBlock(background);
        block.getConstraints().addX(new UIRelativeConstraint(0));
        block.getConstraints().addY(new UIRelativeConstraint(0));
        block.getConstraints().addWidth(new UIRelativeConstraint(1));
        block.getConstraints().addHeight(new UIRelativeConstraint(1));

        block.add(label);
        super.add(block);
    }

    @Override
    protected void onHoverEnter() {
/*        background = background.darker();
        block.setColor(background);*/
        label.setColor(new Color(1f,1,0));
    }

    @Override
    protected void onHoverLeave() {
/*        background = background.brighter();
        block.setColor(background);*/
        label.setColor(new Color(1f, 1, 1));
    }
}