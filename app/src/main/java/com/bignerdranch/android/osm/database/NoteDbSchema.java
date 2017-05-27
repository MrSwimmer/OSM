package com.bignerdranch.android.osm.database;


public class NoteDbSchema {
    public static final class NoteTable {
        public static final String NAME = "notes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String RADIO = "radio";
            public static final String UP = "up";
            public static final String DOWN = "down";
            public static final String REC = "rec";
            public static final String POINTS = "points";
            public static final String ZONE = "zone";
        }
    }

}
