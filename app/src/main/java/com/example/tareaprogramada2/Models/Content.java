package com.example.tareaprogramada2.Models;

import java.util.HashMap;

public abstract class Content {
    ContentType type;

    public abstract HashMap<String, Object> toMap();
}
