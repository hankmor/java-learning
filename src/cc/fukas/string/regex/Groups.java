package cc.fukas.string.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cc.fukas.util.PrintHelper.*;

/**
 * Created by sun on 16-1-12.
 *
 * @author sun
 * @version 1.0
 * @since 1.0
 */
public class Groups {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    public static final String POEM =
            "Very quietly I take my leave,\n" +
            "As quietly as I came here.\n" +
            "Quietly I wave good-bye,\n" +
            "To the rosy clouds in the western sky.";

    //~ Methods ========================================================================================================

    public static void main(String[] args) {
        // (?m)表示多行模式，此时^和$匹配一行的开始和结束
        String regex = "(?m)(\\S+)\\s+((\\S+)\\s+(\\S+))$";
        Matcher matcher = Pattern.compile(regex).matcher(POEM);
        while (matcher.find()) {
            // 一共5个组
            for (int i = 0; i <= matcher.groupCount(); i++) {
                String s = matcher.group(i);
                print("[" + s + "]");
            }
            println();
        }
    }
}
/* Output :
[take my leave,][take][my leave,][my][leave,]
[I came here.][I][came here.][came][here.]
[I wave good-bye,][I][wave good-bye,][wave][good-bye,]
[the western sky.][the][western sky.][western][sky.]
 */