package com.lionfish.GUI.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CachedImageLoader implements ImageLoader {
    private final Map<String, Image> map;
    private final String pathFormat;

    public CachedImageLoader(String path) {
        this.map = new HashMap<>();
        if( path.charAt(path.length()-1) != '/') {
            path = path + '/';
        }
        this.pathFormat = path + "%s.png";
    }

    @Override
    public ImageView getResource(String name) {
        String path = String.format(pathFormat,name);
        System.out.println(path);
        if(!map.containsKey(name)) {
            map.put(name, new Image(Objects.requireNonNull(this.getClass().getResource(path)).toString()));
        }
        ImageView res = new ImageView(map.get(name));
        res.setPreserveRatio(true);
        res.setFitHeight(64);
        return res;
    }
}
