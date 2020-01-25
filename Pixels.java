package editor;


public class Pixels {
  public int r;
  public int g;
  public int b;

  public Pixels(int red, int green, int blue) {
    this.r= red;
    this.g= green;
    this.b= blue;
  }
  //  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//    Pixels pixels= (Pixels) o;
//    return red == pixels.red &&
//            green == pixels.green &&
//            blue == pixels.blue;
//  }
  @Override
  public String toString() {
    return r + " " + g + " " + b + " ";
  }
}
