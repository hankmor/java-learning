package main.java.com.belonk.string.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static main.java.com.belonk.util.PrintHelper.print;
import static main.java.com.belonk.util.PrintHelper.println;

/**
 * Created by sun on 16-1-12.
 *
 * @author sun
 * @version 1.0
 * @since 1.0
 */
public class Groups2 {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================

    //~ Methods ========================================================================================================

    /**
     * 查询不以大写字母开头的单词，并统计每词的数量
     *
     * @param args
     */
    public static void main(String[] args) {
        String words = Groups.POEM;
        // 不以大写字母开头的单词
        String regex = "\\b([a-z]\\w*)\\b";
        Matcher matcher = Pattern.compile(regex).matcher(words);
        Map<String, Integer> map = new HashMap<String, Integer>();
        while (matcher.find()) {
            String s = matcher.group(1);
            Integer cnt = map.get(s);
            if (cnt == null)
                cnt = 0;
            map.put(s, ++cnt);
            print("[" + s + "]");
        }
        println();
        for (String s : map.keySet()) {
            println(s + " : " + map.get(s));
        }
    }
}
/* Output :
[quietly][take][my][leave][quietly][as][came][here][wave][good][bye][the][rosy][clouds][in][the][western][sky]
here : 1
sky : 1
in : 1
clouds : 1
my : 1
good : 1
wave : 1
bye : 1
the : 2
take : 1
as : 1
leave : 1
rosy : 1
came : 1
western : 1
quietly : 2
 */