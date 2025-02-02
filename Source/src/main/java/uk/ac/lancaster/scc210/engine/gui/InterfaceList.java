package uk.ac.lancaster.scc210.engine.gui;

import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.KeyEvent;
import org.jsfml.window.event.TextEvent;
import uk.ac.lancaster.scc210.engine.InputListener;
import uk.ac.lancaster.scc210.engine.StateBasedGame;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Interface list.
 */
public class InterfaceList implements InputListener, Drawable {
    public static final int LIST_PADDING = 250;

    private final int OPTION_HEIGHT_PADDING = 50;

    private final List<ListOption> options;

    private final Font font;

    private Vector2f position;

    private Keyboard.Key previousKey, pressedKey;

    private int previousSelected, selectedIndex;

    private boolean enabled;

    /**
     * Instantiates a new Interface list.
     *
     * @param game     the game
     * @param font     the font
     * @param position the position
     */
    public InterfaceList(StateBasedGame game, Font font, Vector2f position) {
        this.font = font;
        this.position = position;

        options = new ArrayList<>();

        previousSelected = -1;

        selectedIndex = 0;

        enabled = false;

        game.addKeyListener(this);
    }

    /**
     * Add list option.
     *
     * @param label    the label
     * @param listener the listener
     */
    public void addListOption(String label, SelectedListener listener) {
        ListOption listOption = new ListOption(font, label);

        listOption.addSelectedListener(listener);

        options.add(listOption);

        setOptionPosition(listOption);
    }

    /**
     * Update.
     */
    public void update() {
        if (!enabled) {
            return;
        }

        handleInput();

        // Don't handle repeat actions
        if (previousSelected > -1) {
            // Loop back around from the top to the bottom
            if (selectedIndex < 0) {
                selectedIndex = options.size() - 1;
            }

            // Loop back around from the bottom to the top
            if (selectedIndex >= options.size()) {
                selectedIndex = 0;
            }

            ListOption newSelected = options.get(selectedIndex);

            if (newSelected.isEnabled()) {
                newSelected.setCurrent();

                // Deselect the previous item. Don't set to not current, if the current and previous are the same
                if (previousSelected >= 0 && selectedIndex != previousSelected) {
                    options.get(previousSelected).setNotCurrent();
                }

                selectOption(newSelected);

            } else {
                // If the currently selected index is listed as disabled, reset the selected index the previous (enabled) option
                selectedIndex = previousSelected;
            }

        } else {
            // If the current selection is the first item in the list, set the first item to be selected by default
            ListOption firstOption = options.get(selectedIndex);

            firstOption.setCurrent();

            selectOption(firstOption);
        }

        pressedKey = null;
    }

    @Override
    public void draw(RenderTarget renderTarget, RenderStates renderStates) {
        options.forEach(option -> option.draw(renderTarget));
    }

    private void selectOption(ListOption selected) {
        if (pressedKey == Keyboard.Key.SPACE || pressedKey == Keyboard.Key.RETURN) {
            selected.select();
        }
    }

    private void handleInput() {
        // Don't process if nothing is pressed or not enabled
        if ((pressedKey == null && previousKey != null) || !enabled) {
            return;
        }

        if (pressedKey == Keyboard.Key.W || pressedKey == Keyboard.Key.UP) {
            previousSelected = selectedIndex;

            selectedIndex--;
        }

        if (pressedKey == Keyboard.Key.S || pressedKey == Keyboard.Key.DOWN) {
            previousSelected = selectedIndex;

            selectedIndex++;
        }
    }

    @Override
    public void keyPressed(KeyEvent keyevent) {
        previousKey = pressedKey;

        pressedKey = keyevent.key;
    }

    @Override
    public void keyTyped(TextEvent textevent) {

    }

    /**
     * Gets global bounds.
     *
     * @return the global bounds
     */
    FloatRect getGlobalBounds() {
        // Find the total bounds of the list. The left and top can be found from the first element.
        // The width if the widest element in the list.
        // The height is the sum of the individual heights of each element in the list
        FloatRect bounds = FloatRect.EMPTY;

        float left = 0;

        float top = 0;

        float width = 0;

        for (int i = 0; i < options.size(); i++) {
            ListOption option = options.get(i);

            FloatRect optionBounds = option.getText().getGlobalBounds();

            if (i == 0) {
                left = optionBounds.left;

                top = optionBounds.top;
            }

            if (optionBounds.width > width) {
                width = optionBounds.width;
            }

            bounds = new FloatRect(left, top, width, bounds.height + optionBounds.height);
        }

        return bounds;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(Vector2f position) {
        this.position = position;

        setOptionPositions();
    }

    private void setOptionPositions() {
        for (ListOption option : options) {
            setOptionPosition(option);
        }
    }

    private void setOptionPosition(ListOption option) {
        FloatRect bounds = option.getText().getGlobalBounds();

        Vector2f pos = new Vector2f(position.x, position.y + ((bounds.height + OPTION_HEIGHT_PADDING) * options.indexOf(option)));

        option.setPosition(pos);
    }

    /**
     * Is enabled boolean.
     *
     * @return the boolean
     */
    boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets enabled.
     *
     * @param enabled the enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (!enabled) {
            options.get(selectedIndex).setNotCurrent();
        }
    }

    /**
     * Gets options.
     *
     * @return the options
     */
    public List<ListOption> getOptions() {
        return options;
    }

    /**
     * Gets selected index.
     *
     * @return the selected index
     */
    int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Gets previous selected.
     *
     * @return the previous selected
     */
    int getPreviousSelected() {
        return previousSelected;
    }

    /**
     * Sets selected index.
     *
     * @param selectedIndex    the selected index
     * @param previousSelected the previous selected
     */
    void setSelectedIndex(int selectedIndex, int previousSelected) {
        this.selectedIndex = selectedIndex;
        this.previousSelected = previousSelected;
    }
}
