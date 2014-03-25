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
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.util.Clock;
import sut.game01.core.spite.Player;
import sut.game01.core.spite.Sp;
import sut.game01.core.spite.Zealot;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.ui.Root;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by Boom on 5/3/2557.
 */
public class Gamescreen extends Screen{
    private ScreenStack ss;
    private Zealot z;
    private Zealot z2;
    private Player player;
    private Sp sp;
    private Root root;
    private DebugDrawBox2D debugDraw;
    public static float m_per_pixel = 1/26.666667f;
    private static int width = 24;
    private static int height = 18;
    private World world;
    private boolean showDebugDraw=true;
    private Body ground;
    public Gamescreen (ScreenStack ss){
        this.ss = ss;
    }

    @Override
    public void wasAdded() {
        super.wasAdded();
        Image bgImage = assets().getImage("images/basketballfabric.png");
        ImageLayer bgLayer = graphics().createImageLayer(bgImage);
        layer.add(bgLayer);
        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity,true);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        //player = new Player(world,150f,150f);
        z = new Zealot(world,200f,200f);
        layer.add(z.layer());
        //layer.add(player.layer());
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
            ground = world.createBody(new BodyDef());
            PolygonShape groundShape = new PolygonShape();
            groundShape.setAsEdge(new Vec2(0f, height),
                    new Vec2(width - 0f, height));
            ground.createFixture(groundShape, 0.0f);
            world.setContactListener(new ContactListener() {
                @Override
                public void beginContact(Contact contact) {
                    if(contact.getFixtureA().getBody() == z.getBody()){
                        z.contact(contact);
                        System.out.println("A is zealot");
                    }
                    if(contact.getFixtureB().getBody() == z.getBody()){
                        System.out.println("B is zealot");
                    }
                    if(contact.getFixtureA().getBody() == sp.getBody() ){
                        sp.contact(contact);
                        System.out.println("A is sp");
                    }
                    if(contact.getFixtureB().getBody() == sp.getBody()){
                        System.out.println("B is sp");
                    }
                    if(contact.getFixtureA().getBody() == ground){
                        System.out.println("A is ground");
                    }
                    if(contact.getFixtureB().getBody() == ground){
                        System.out.println("B is ground");
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
        }
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        world.step(0.033f,10,10);
        //player.update(delta);
        z.update(delta);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        //player.paint(clock);
        z.paint(clock);
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
