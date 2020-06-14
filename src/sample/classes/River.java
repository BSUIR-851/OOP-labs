package sample.classes;

public class River extends Water {
    public float length;
    public float volume;

    public void setLength(float length) { this.length = length; }
    public float getLength() { return this.length; }

    public void setVolume(float volume) { this.volume = volume; }
    public float getVolume() { return this.volume; }

    public River() {
        super();
        this.length = 0f;
        this.volume = 0f;
    }
}
