package sut.game01.core.screen;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.spite.Sp;
import sut.game01.core.spite.Zealot;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AxisLayout;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class TestScreen extends UIScreen{
    private final ScreenStack ss;
    private Zealot z;
    private Zealot z2;
    private Sp sp;
    private DebugDrawBox2D debugDraw;
    public static float m_per_pixel = 1/26.666667f;
    private static int width = 24;
    private static int height = 18;
    private World world;
    private boolean showDebugDraw=true;

    public static final Font TITLE_FONT = graphics().createFont(
            "Helvetica",
            Font.Style.PLAIN,
            24);
    @Override
    public void wasAdded(){
        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity,true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        z = new Zealot(world,150f,300f);
        sp = new Sp(world,450f,300f);
        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/TestScreen.m_per_pixel),
                    (int)(height/TestScreen.m_per_pixel));
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit |
                    DebugDraw.e_jointBit |
                    DebugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 26.666667f);
            world.setDebugDraw(debugDraw);
            world.setContactListener(new ContactListener() {
                @Override
                public void beginContact(Contact contact) {
                    if(contact.getFixtureA().getBody() == z.getBody() || contact.getFixtureB().getBody() == z.getBody()){
                        z.contact(contact);
                    }
                    if(contact.getFixtureA().getBody() == sp.getBody() || contact.getFixtureB().getBody() == sp.getBody()){
                        sp.contact(contact);
                    }
                }

                @Override
                public void endContact(Contact contact) {

                }

                @Override
                public void preSolve(Contact contact, Manifold manifold) {

                }

                @Override
                public void postSolve(Contact contact, ContactImpulse contactImpulse) {

                }
            });
            Body ground = world.createBody(new BodyDef());
            PolygonShape groundShape = new PolygonShape();
            groundShape.setAsEdge(new Vec2(0f, height - 2),
                    new Vec2(width - 0f, height - 2));
            ground.createFixture(groundShape, 0.0f);
        }
        Image bgImage = assets().getImage("images/game-over.png");
        bgImage.addCallback(new Callback<Image>() {
            @Override
            public void onSuccess(Image result) {

            }

            @Override
            public void onFailure(Throwable cause) {

            }
        });
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(z.layer());
        layer.add(sp.layer());
    }
    private Root root;
    public TestScreen(ScreenStack ss){
        this.ss = ss;
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        z.update(delta);
        sp.update(delta);
        world.step(0.033f,10,10);
    }

    @Override
    public void wasShown(){
        super.wasShown();
        root = iface.createRoot(
                AxisLayout.vertical().gap(15),
                SimpleStyles.newSheet(),layer);

        root.setSize(width(),height());
    }
    @Override
    public void paint(Clock clock){
        super.paint(clock);
        z.paint(clock);
        sp.paint(clock);
        if(false){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
