package sut.game01.core.spite;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screen.TestScreen;

/**
 * Created by Boom on 4/3/2557.
 */
public class Player {
    private Sprite sprite;
    private int spriteIndex =0;
    private boolean hasLoaded=false;
    private float x;
    private float y;

    public enum State{
        WALK,JUMP
    };
    public State state = State.WALK;
    private int e =0;
    private int offset = 0;
    private Body body;
    private Body other;
    private boolean contacted;
    private int contactCheck;
    private Body initPhysicsBody(World world,float x, float y){
        BodyDef bodydef = new BodyDef();
        bodydef.type = BodyType.DYNAMIC;
        bodydef.position = new Vec2(0,0);
        Body body = world.createBody(bodydef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(56 * TestScreen.m_per_pixel/2,
                sprite.layer().height()*TestScreen.m_per_pixel/2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape =  shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.5f;
        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x,y),0f);
        return body;
    }
    public Player(final World world,final float x,final float y){
        this.x = x;
        this.y = y;
        this.sprite = SpriteLoader.getSprite("images/player.json");
        sprite.addCallback(new Callback<Sprite>() {
            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width()/2f,sprite.height()/2f);
                sprite.layer().setTranslation(x,y+13f);
                body = initPhysicsBody(world, x*TestScreen.m_per_pixel, y*TestScreen.m_per_pixel);
                hasLoaded = true;
            }
            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!",cause);
            }
        });
    }
    public void update(int delta){
        if(!hasLoaded)return;
        e+=delta;
        if(e>150){
            switch (state){
                case WALK: offset = 0;

                    break;
                case JUMP: offset =8;
                    if(spriteIndex==9){
                        body.applyForce(new Vec2(0f,-300f),body.getPosition());
                    }
                    if(spriteIndex==15){
                        state = State.WALK;
                    }
                    break;
            }
            spriteIndex = offset + ((spriteIndex+1)%8);
            sprite.setSprite(spriteIndex);
            e=0;
        }
    }
    public Body getBody(){
        return this.body;
    }
    public void paint(Clock clock){
        if(!hasLoaded) return;
        sprite.layer().setTranslation(
                (body.getPosition().x/TestScreen.m_per_pixel),
                body.getPosition().y/TestScreen.m_per_pixel);
    }
    public Layer layer(){
        return sprite.layer();
    }
    public void sete(int a){
        this.e=a;
    }
    public void setspriteindex(int a){
        this.spriteIndex = a;
    }
    public int spriteIndex(){
        return this.spriteIndex;
    }
    public void contact(Contact contact){
        contacted = true;
        contactCheck = 0;
        if(contact.getFixtureA().getBody()==body){
            other = contact.getFixtureB().getBody();
        } else {
            other = contact.getFixtureA().getBody();
        }
    }
}
