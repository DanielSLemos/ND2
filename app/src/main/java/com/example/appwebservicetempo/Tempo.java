package com.example.appwebservicetempo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Tempo {
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("sys")
    public Sys sys;
    @SerializedName("weather")
    public ArrayList<Weather> weather = new ArrayList<Weather>();
    @SerializedName("main")
    public Main main;
    @SerializedName("wind")
    public Wind wind;
    @SerializedName("rain")
    public Rain rain;
    @SerializedName("clouds")
    public Clouds clouds;
    @SerializedName("dt")
    public float dt;
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("cod")
    public float cod;
}

class Coord{
    @SerializedName("lon")
    public float lon;
    @SerializedName("lat")
    public float lat;
}

class Sys{
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public float sunrise;
    @SerializedName("sunset")
    public float sunset;
}

//Nesta classe, modificamos os nomes dos atributos. Isso não tem problema
//pois o mapeamento com o SerializedName deve ocorrer da mesma forma
class Main{
    @SerializedName("temp")
    public float temperatura;
    @SerializedName("humidity")
    public float umidade;
    @SerializedName("pressure")
    public float pressao;
    @SerializedName("temp_min")
    public float minima;
    @SerializedName("temp_max")
    public float maxima;
}

class Wind{
    @SerializedName("speed")
    public float speed;
    @SerializedName("deg")
    public float deg;
}

class Rain{
    @SerializedName("h3")
    public float h3;
}

class Clouds{
    @SerializedName("all")
    public float all;
}

class Weather{
    @SerializedName("id")
    public int id;
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;
    @SerializedName("icon")
    public String icon;
}