import java.io.*;
import java.util.HashMap;

public class Generate_data {

    public static void main(String[] agr) throws FileNotFoundException{
        String[] states = {"A", "B", "C", "D", "Z"};
        genertate_initial_vector(states);
        generate_transition_probability(states);
        generate_emit_probability(states);
    }

    //生成发射矩阵，命名为emit_probability.txt；每一行：隐状态,显状态,概率
    public static  void generate_emit_probability(String[] states){
        try {
            HashMap<String, Integer> map_freq = new HashMap<>();
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("initial_vector.txt")));
            String str;
            while ((str = bf.readLine()) != null) {
                String[] ss = str.split(" ");
                map_freq.put(ss[0], Integer.parseInt(ss[1]));
            }
            BufferedReader bff = new BufferedReader(new InputStreamReader(new FileInputStream("nt.txt")));
            String strr;
            HashMap<String,HashMap<String, String>> emit = new HashMap<>();
            while ((strr = bff.readLine()) != null) {
                String[] ss = strr.split(" ");
                String observe = ss[0];
                //System.out.print(observe);
                for(int i = 1; i< ss.length-2;i+=2){
                    float p = (float)Integer.parseInt(ss[i+1])/map_freq.get(ss[i]);
                    HashMap<String, String> m = new HashMap<>();
                    m.put(observe,String.valueOf(p));
                    if(emit.containsKey(ss[i])){
                        emit.get(ss[i]).put(observe,String.valueOf(p));
                    }
                    else emit.put(ss[i],m);
                }
            }

            File file = new File("emit_probability.txt");
            // if file doesnt exists, then create it
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for(String key: emit.keySet()){
                //String content = key + " ";
                HashMap<String, String> m = emit.get(key);
                for(String kk:m.keySet()){
                    String content = key + " " +kk +" " +m.get(kk) + "\n";
                    //System.out.print(content);
                    bw.write(content);
                }

            }

            bw.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String keyString(int i){
        if(i == 0)
            return "A";
        if(i == 1)
            return "B";
        if(i == 2)
            return "C";
        if(i == 3)
            return "D";
        if(i == 4)
            return "F";
        if(i == 5)
            return "G";
        if(i == 6)
            return "I";
        if(i == 7)
            return "J";
        if(i == 8)
            return "K";
        if(i == 9)
            return "L";
        if(i == 10)
            return "M";
        if(i == 11)
            return "P";
        if(i == 12)
            return "S";
        if(i == 13)
            return "W";
        if(i == 14)
            return "X";
        if(i == 15)
            return "Z";
        else return "0";
    }

    //生成转移概率矩阵，命名为transition_probability.txt；每一行：状态1,状态2,概率
    public static void  generate_transition_probability(String[] states){
        try{
            int sum = 0;
            int[][] trans = new int[16][16];
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("nt.tr.txt")));
            String str;			// 按行读取字符串
            int flag = 0;
            while ((str = bf.readLine()) != null) {
                if(flag == 0)
                    flag ++;
                else{
                    String[] ss = str.split(",");
                    for(int i = 0; i<16;i++){
                        sum += Integer.parseInt(ss[i+1]);
                        trans[flag - 1][i] = Integer.parseInt(ss[i+1]);
                    }
                    flag ++;
                }

            }
            //System.out.print(trans);

            File file = new File("transition_probability.txt");
            // if file doesnt exists, then create it
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for(int i = 0;i<16;i++)
                for(int j = 0; j< 16; j++){
                    String content = keyString(i) + " " + keyString(j)+ " " + String.valueOf((float) trans[i][j]/sum) + "\n";
                    bw.write(content);
                }
            bw.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //生成初始化概率向量Π，命名为initial_vector.txt，格式每一行为：状态,出现次数,概率
    public  static void genertate_initial_vector(String[] states){
        try {
            int count = 0;
            HashMap<String, Integer> initMap = new HashMap<>();
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("nt.txt")));
            String str;			// 按行读取字符串
            while ((str = bf.readLine()) != null) {
                //System.out.print(str);
                String[] ss = str.split(" ");
                for(int i =1; i< ss.length -2;i+=2){
                    //System.out.print(ss[i] + ss[i+1] + "\n");
                    int x = Integer.parseInt(ss[i+1]);
                    count += x;
                    if(initMap.containsKey(ss[i])){
                        x += initMap.get(ss[i]);
                        initMap.remove(ss[i]);
                    }
                    initMap.put(ss[i],x);
                }
            }

            File file = new File("initial_vector.txt");
            // if file doesnt exists, then create it
            if (!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String key : initMap.keySet()) {
                String content = key +  " "+ initMap.get(key).toString() + " ";
                double f = (double)initMap.get(key)/count;
                String s = String.valueOf(f)+"\n";
                content += s;
                bw.write(content);
            }
            bw.close();


        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
