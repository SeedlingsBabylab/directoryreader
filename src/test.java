import java.util.regex.*;

class Main
{
  public static void main(String[] args)
  {
    String txt="04_6_video.mp4";

    String re1="(\\d+)";	// Integer Number 1
    String re2="(_)";	// Any Single Character 1
    String re3="(\\d+)";	// Integer Number 2
    String re4="(_)";	// Any Single Character 2
    String re5="(video\\.mp4)";	// File Name 1

    Pattern p = Pattern.compile(re1+re2+re3+re4+re5,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher m = p.matcher(txt);
    if (m.find())
    {
        String int1=m.group(1);
        String c1=m.group(2);
        String int2=m.group(3);
        String c2=m.group(4);
        String file1=m.group(5);
        System.out.print("("+int1.toString()+")"+"("+c1.toString()+")"+"("+int2.toString()+")"+"("+c2.toString()+")"+"("+file1.toString()+")"+"\n");
    }
  }
}