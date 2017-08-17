package com.roervik.timetable.server;

import static spark.Spark.get;

public class Server {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello world");
    }
}
