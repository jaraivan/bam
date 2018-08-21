package com.jaraprystupiuk.bam.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.jaraprystupiuk.bam.Bam;
import com.jaraprystupiuk.bam.Screens.PlayScreen;

public class Fan extends Sprite {

    public World world;
    public Body b2body;
    private TextureRegion fanParado;

    public Fan(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("fan"));
        this.world = world;
        defineCharacter();
        fanParado = new TextureRegion(getTexture(), 20, 40, 16, 20);
        setBounds(20, 40, 16 / Bam.PPM, 20 / Bam.PPM);
        setRegion(fanParado);

    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Bam.PPM, 32 / Bam.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / Bam.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

}
