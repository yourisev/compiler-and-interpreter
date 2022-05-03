package other;

import java.util.ArrayList;
import java.util.List;

public class Matcher {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("switch");
        list.add("switchland");
        list.add("tv");
        list.add("switch");
        list.add("landswitch");
        list.add("switchland");
        list.add("tv");
        list.add("landswitch");
        list.add("switchland");
        list.add("switch");
        list.add("tv");
        list.add("switch");
        list.add("landswitch");
        list.add("tv");
        list.add("apple");
        list.add("food");
        List<String> newlist = uniqueList(list);
        for(int i = 0; i<list.size();i++){
            System.out.println(newlist.get(i));
        }
    }

    public  static List<String> uniqueList(List<String> deviceNames) {
        List<String> uniqueNames = new ArrayList<>();

        for (int i = 0; i < deviceNames.size(); i++) {
            int count = 1;
            String name = deviceNames.get(i);
            if (!uniqueNames.contains(name)) {
                uniqueNames.add(name);
                continue;
            }
            while (uniqueNames.contains(name + count)) {
                count++;
            }
            uniqueNames.add(name + count);
        }
        return uniqueNames;
    }
}
