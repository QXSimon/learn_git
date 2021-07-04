import java.util.HashMap;
import java.util.Map;

/**
 * @author SimonChiou
 * @version 1.0
 * @description 内存泄漏例子
 * @date 2021/7/4 上午10:33
 */
public class KeylessEntry {
    static class Key{
        Integer id;
        Key(Integer id){
            this.id =id;
        }
        @Override
        public int hashCode() { //1.Object,默认的equals方法,直接比对的两个对象 == , 新建的对象之间无论id值是否相等
            return id.hashCode();
        }
        // 3.解决方法,重写equals(), 使用比对id,来判断两个Key对象是否相等
        @Override
        public boolean equals(Object obj) {
            boolean response=false;
            if(obj instanceof Key){
                response = ((Key)obj).id.equals(this.id);   //直接比对id的值
            }
            return response;
        }
    }

    public static void main(String[] args) {
        Map m = new HashMap();
        while(true){
            for (int i = 0; i < 10000; i++) {
                // 2.因为重写的hashCode方法,equals比对,每次都是新对象,所以每次都不包含这个对象,都会新增map的元素.并没有达到预期覆盖的想法.
                if(!m.containsKey(new Key(i))){
                    m.put(new Key(i),"Number:"+i);
                }
            }
            System.out.println("m.size()="+m.size());
        }
    }
}
