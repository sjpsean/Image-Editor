package editor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ImageEditor {

  public static void main(String[] args) throws IOException {
    if (args.length < 3 || (args[2].equals("motionblur")&&(args.length<4))){
      System.out.println("USAGE: java ImageEditor in-file out-file (grayscale|invert|emboss|motionblur motion-blur-length)");
    }
    else {
      File file=new File(args[0]);
      Scanner sc=new Scanner(file);
      sc.useDelimiter("((#[^\\n]*\\n)|(\\s+))+"); // Delimit by whitespace, #line comments
      String ppmStart;
      int width;
      int height;
      int maxVal;
      int r;
      int g;
      int b;
      Pixels[][] pixels;

// Read File
      // Read the First Part
      ppmStart=sc.next();
      width=sc.nextInt();
      height=sc.nextInt();
      maxVal=sc.nextInt();
      pixels=new Pixels[height][width];
      // Read Pixels
      for (int h=0; h < height; h++) {
        for (int w=0; w < width && sc.hasNext(); w++) {
          r=sc.nextInt();
          g=sc.nextInt();
          b=sc.nextInt();
          pixels[h][w]=new Pixels(r, g, b);
//        System.out.println(pixels[h][w].toString());
        }
      }

//Edit
      // Invert
      switch (args[2]) {
        case "invert":
          invert(pixels, height, width);
          break;
        //Grayscale
        case "grayscale":
          grayscale(pixels, height, width);
          break;
        // Emboss
        case "emboss":
          emboss(pixels, height, width);
          break;
        // Motion Blur
        case "motionblur":
          int mbLength=Integer.parseInt(args[3]);
          motionblur(pixels, height, width, mbLength);
          break;
      }


// Write File
      // StringBuilder append all the beginning part
      File outputFile=new File(args[1]);
      FileWriter fr=new FileWriter(outputFile);
      StringBuilder output=new StringBuilder();
      output.append(ppmStart).append(" ").append(width).append(" ").append(height).append(" ").append(maxVal).append("\n");
      // append pixels
      for (int h=0; h < height; h++) {
        for (int w=0; w < width; w++) {
//        System.out.printf("print : %s\n", pixels[h][w].toString());
          output.append(pixels[h][w].toString());
        }
      }
//    System.out.println(output);
      fr.write(output.toString());
      fr.close();
    }
  }


  private static void invert(Pixels[][] pixels, int height, int width) {
    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        pixels[h][w].r = 255 - pixels[h][w].r;
        pixels[h][w].g = 255 - pixels[h][w].g;
        pixels[h][w].b = 255 - pixels[h][w].b;
      }
    }
  }

  private static void grayscale(Pixels[][] pixels, int height, int width) {
    int avg;
    for (int h = 0; h < height; h++) {
      for (int w = 0; w < width; w++) {
        avg = (pixels[h][w].r + pixels[h][w].g + pixels[h][w].b)/3;
        pixels[h][w].r = avg;
        pixels[h][w].g = avg;
        pixels[h][w].b = avg;
      }
    }
  }

  private static void emboss(Pixels[][] pixels, int height, int width){
    int rDiff;
    int gDiff;
    int bDiff;
    int embossValue = 0;
    for (int h = height-1; h >= 0; h--) {
      for (int w = width-1; w >= 0; w--) {
        embossValue = 128;  // reset embossValue
        if (h != 0 && w != 0) {
          rDiff=pixels[h][w].r - pixels[h - 1][w - 1].r;
          gDiff=pixels[h][w].g - pixels[h - 1][w - 1].g;
          bDiff=pixels[h][w].b - pixels[h - 1][w - 1].b;
          // r -> g -> b order when diff's are same value.
          // make if statements in order of when the largest value is b, g, r so when they have
          // the same value, the final result will r or g or b.
          if ((Math.abs(bDiff) >= Math.abs(gDiff)) && (Math.abs(bDiff) >= Math.abs(rDiff))) {
            embossValue=128 + bDiff;
          }
          if ((Math.abs(gDiff) >= Math.abs(bDiff)) && (Math.abs(gDiff) >= Math.abs(rDiff))) {
            embossValue=128 + gDiff;
          }
          if ((Math.abs(rDiff) >= Math.abs(bDiff)) && (Math.abs(rDiff) >= Math.abs(gDiff))) {
            embossValue=128 + rDiff;
          }
          if (embossValue < 0){
            embossValue = 0;
          }
          else if (embossValue > 255){
            embossValue = 255;
          }
        }
        pixels[h][w].r = embossValue;
        pixels[h][w].g = embossValue;
        pixels[h][w].b = embossValue;
        //System.out.printf("r, g, b : %d %d %d\n", pixels[h][w].r,pixels[h][w].g,pixels[h][w].b);
      }
    }
  }

  private static void motionblur(Pixels[][] pixels, int height, int width, int mbLength){
    int rAvg;
    int gAvg;
    int bAvg;
    if (mbLength > width) {
      mbLength = width;
    }
    for (int h = 0; h < height; h++) {
      int repeatNum = mbLength;
      for (int w = 0; w < width; w++) {
        if ((repeatNum + w) > width) {
          repeatNum--;
        }
        rAvg = 0;
        gAvg = 0;
        bAvg = 0;
        for (int n = 0; n < repeatNum; n++) {
          rAvg += pixels[h][w+n].r;
          gAvg += pixels[h][w+n].g;
          bAvg += pixels[h][w+n].b;
        }
        if (repeatNum != 0) {
          pixels[h][w].r = rAvg / repeatNum;
          pixels[h][w].g = gAvg / repeatNum;
          pixels[h][w].b = bAvg / repeatNum;
        }

        //System.out.printf("r, g, b : %d %d %d\n", pixels[h][w].r,pixels[h][w].g,pixels[h][w].b);
      }
    }
  }
}