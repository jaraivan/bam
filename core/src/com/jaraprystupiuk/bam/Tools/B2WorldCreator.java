package com.jaraprystupiuk.bam.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.jaraprystupiuk.bam.Bam;

public class B2WorldCreator {

    public B2WorldCreator(World world, TiledMap map) {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Bam.PPM, (rect.getY() + rect.getHeight() / 2) / Bam.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Bam.PPM, rect.getHeight() / 2 / Bam.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

        }

      /*  for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Bam.PPM, (rect.getY() + rect.getHeight() / 2) / Bam.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Bam.PPM, rect.getHeight() / 2 / Bam.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

        }*/

    }
}
