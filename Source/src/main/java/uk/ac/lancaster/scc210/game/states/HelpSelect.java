package uk.ac.lancaster.scc210.game.states;

import org.jsfml.graphics.*;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.KeyEvent;
import uk.ac.lancaster.scc210.engine.InputListener;
import uk.ac.lancaster.scc210.engine.StateBasedGame;
import uk.ac.lancaster.scc210.engine.ViewSize;
import uk.ac.lancaster.scc210.engine.content.FontManager;
import uk.ac.lancaster.scc210.engine.content.TextureManager;
import uk.ac.lancaster.scc210.engine.gui.InterfaceGrid;
import uk.ac.lancaster.scc210.engine.gui.InterfaceList;
import uk.ac.lancaster.scc210.engine.states.State;
import uk.ac.lancaster.scc210.game.content.LevelManager;
import uk.ac.lancaster.scc210.game.level.Level;

import java.util.ArrayList;
import java.util.List;

public class HelpSelect implements State, InputListener {
    private StateBasedGame game;
    private Keyboard.Key pressedKey;

    private FontManager fontManager;

    private LevelManager levelManager;

    private Sprite background;

    private FloatRect viewBounds;

    private InterfaceGrid interfaceGrid;

    private Text exitText;

    private Sprite item1;

    private Text menuHeaderTitle1;
    private Text menuHeaderTitle2;



    @Override
    public void setup(StateBasedGame game) {
        this.game = game;

        game.addKeyListener(this);

        fontManager = (FontManager) game.getServiceProvider().get(FontManager.class);

        levelManager = (LevelManager) game.getServiceProvider().get(LevelManager.class);

        viewBounds = ((ViewSize) game.getServiceProvider().get(ViewSize.class)).getViewBounds();

        TextureManager textureManager = (TextureManager) game.getServiceProvider().get(TextureManager.class);

        background = new Sprite(textureManager.get("level-select.jpg:level-select"));

        background.setScale(2, 2);



        item1 = new Sprite(textureManager.get("enemy.png:enemy"));

        item1.setScale(2,2);

        item1.setPosition(500,500);



        createExitText();

        createHeader();
    }

    @Override
    public void update(Time deltaTime) {

    }


    private void createExitText() {
        exitText = new Text();

        exitText.setString("Press ESC to go back");

        exitText.setFont(fontManager.get("font"));

        exitText.setPosition(0, 0);
        exitText.setCharacterSize(50);

        exitText.setColor(Color.WHITE);
    }


    private void createHeader() {
        menuHeaderTitle1 = new Text();
        menuHeaderTitle1.setString("HELP:");
        Vector2f headerPos = new Vector2f(1155,480);
        menuHeaderTitle1.setPosition(headerPos);
        menuHeaderTitle1.setCharacterSize(60);
        //menuHeaderTitle1.setStyle(3);
        menuHeaderTitle1.setColor(Color.CYAN);
        menuHeaderTitle1.setFont(fontManager.get("font"));
    }

    @Override
    public void draw(RenderTarget target) {
        target.draw(background);
        target.draw(item1);
        //target.draw(interfaceGrid);
        target.draw(exitText);
        target.draw(menuHeaderTitle1);
    }

    @Override
    public void keyPressed(KeyEvent keyevent) {
        pressedKey = keyevent.key;
    }
}
