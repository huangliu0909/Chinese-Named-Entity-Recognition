import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HMM_NME {

    public static void main(String[] agr) throws FileNotFoundException {
        String fileForWord_0 = "data.txt";
        //String[] sentence = read_text(fileForWord_0);
        //String st = "/中共中央/总书记/、/国家/主席/江/泽民/发表/１９９８年/新年/讲话/《/迈向/充满/希望/的/新/世纪/》/。/（/新华社/记者/兰/红光/摄/）/";
        String st = "我/十分/高兴/地/通过/中央/人民/广播/电台/、/中国/国际/广播/电台/和/中央/电视台/，/向/全国/各族/人民/，/向/香港/特别/行政区/同胞/、/澳门/和/台湾/同胞/、/海外/侨胞/，/向/世界/各国/的/朋友/们/，/致以/诚挚/的/问候/和/良好/的/祝愿/！/" +
                "/大/会堂/今晚/座无虚席/，/观众/被/艺术家/们/精湛/的/表演/深深/打动/，/不断/报/以/经久不息/的/热烈/掌声/。/艺术家/们/频频/谢幕/，/指挥家/依次/指挥/演出/返/场/曲目/，/最后/音乐会/在/《/红色/娘子军/》/选曲/、/《/白毛女/》/选曲/、/《/北京/喜讯/到/边寨/》/等/乐曲声/中/达到/高潮/。/" +
                "/辞旧迎新/之际/，/国务院/总理/李/鹏/今天/上午/来到/[北京/石景山/发电/总厂/]考察/，/向/广大/企业/职工/表示/节日/的/祝贺/，/向/将要/在/节日/期间/坚守/工作/岗位/的/同志/们/表示/慰问/。/" +
                "/李/鹏/首先/向/[华北/电管局/]、/电厂/负责人/详细/询问/了/目前/电厂/生产/、/职工/生活/和/华北/电网/向/首都/供电/、/供热/的/有关/情况/。/随后/，/他/又/实地/察看/了/发电机组/的/运行/情况/和/电厂/一号机/、/二号机/控制室/。/在/控制室/，/李/鹏/与/职工/们/一一/握手/，";
        String[] sentence = st.split("/");
        //String[] sentence = {"(","博物馆",")"};
        String[] observation = sentence;
        String[] hidden_states = {"A", "B", "C", "D", "F", "G", "I", "J", "K", "L", "M", "P", "S", "W", "X", "Z"};
        List pattern = get_pattern_list();
        Map<String, Map<String, String>> transition_probability = get_transition_probability();
        Map<String, String> initial_probability = get_initial_vector();
        Map<String, Map<String, String>> emit_probability = get_emit();
        //System.out.print(emit_probability);
        List<String> list = viterbi(observation, hidden_states, initial_probability,
                transition_probability, emit_probability);
        int count = 0;
        for(int i = 0; i< list.size();i++){
            if(list.get(i).equals("D"))
                if(emit_probability.get("D").containsKey(sentence[i]))
                {
                    count += 1;
                    System.out.print(sentence[i]+"  ");
                }

        }
        float f = (float) count/9;
        System.out.print("error:" + f);
    }

    public static List[] get_organization(String[] observation,List<String> sequence,List<String> pattern){
        List<Integer> org_index = new ArrayList<>();//存放机构名的索引
        List<String> org = new ArrayList<>();//存放机构名
        String sequence_str = sequence.get(0);
        for(int i = 1; i< sequence.size();i++)
            sequence_str =sequence_str + sequence.get(i);

        for(String pat:pattern){
            int flag = 0;
        }
        return null;
    }


    public static String[] read_text(String filename){
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"gbk"));
            String str;
            String text = null;
            while ((str = bf.readLine()) != null) {
               if(text == null)
                   text = str;
               else
                   text += str;
            }
            String[] result = text.split("/");
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> viterbi(String[] observation, String[] hidden_states, Map<String, String> initial_probability,
                                       Map<String, Map<String, String>> transition_probability, Map<String, Map<String, String>> emit_probability) {

        List<Map<String, String>> record = new ArrayList<>();//记录每一次结果
        Map<String, String> tmp_result = new HashMap<>();
        //初始化
        for (String state : hidden_states) {
            //System.out.print(state);
            if (emit_probability.containsKey(state)) {
                //System.out.print(state);
                if (emit_probability.get(state).containsKey(observation[0])) {
                    //System.out.print(state);
                    float a = Float.parseFloat(initial_probability.get(state));
                    //System.out.print(a);
                    float b = Float.parseFloat(emit_probability.get(state).get(observation[0]));
                    tmp_result.put(state, String.valueOf(a * b));
                } else
                    tmp_result.put(state, "0");
            } else
                tmp_result.put(state, "0");

        }
        record.add(tmp_result);

        //对于之后的词语继续计算
        for (int i = 1; i < observation.length; i++) {
            //System.out.print(observation[i]);
            tmp_result = new HashMap<>();
            for (String current_state : hidden_states) {
                //取最大值，上一次的所有状态转移到当前状态的发射概率
                if (emit_probability.get(current_state) != null) {
                    if (emit_probability.get(current_state).containsKey(observation[i])) {
                        float f = 0;
                        for (String x : hidden_states) {
                            //compute_recode[index][x]
                            //transition_probability[x][current_state]
                            //emit_probability[current_state][word]

                            float a = Float.parseFloat(record.get(i - 1).get(x));
                            float b = Float.parseFloat(transition_probability.get(x).get(current_state));
                            float c = Float.parseFloat(emit_probability.get(current_state).get(observation[i]));
                            //System.out.print(a);
                            if (f < a * b * c)
                                f = a * b * c;
                        }

                        tmp_result.put(current_state, String.valueOf(f));
                        //.out.print(tmp_result);
                    } else
                        tmp_result.put(current_state, "0");
                } else tmp_result.put(current_state, "0");

            }
            record.add(tmp_result);
        }

        List<String> tag_sequence = new ArrayList<>();
        //System.out.print(record);
        for (Map<String, String> r : record) {

            int flag = 0;
            String ss = null;
            for (String key : r.keySet()) {
                if (flag == 0) {
                    ss = key;
                    flag++;
                } else {

                    if (Float.parseFloat(r.get(ss)) < Float.parseFloat(r.get(key)))
                        ss = key;
                }

            }
            tag_sequence.add(ss);
        }


        return tag_sequence;
    }

    public static Map<String, Map<String, String>> get_emit() {
        try {
            Map<String, Map<String, String>> map = new HashMap<>();
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("emit_probability.txt")));
            String str;
            while ((str = bf.readLine()) != null) {
                String[] ss = str.split(" ");
                if (map.containsKey(ss[0]))
                    map.get(ss[0]).put(ss[1], ss[2]);
                else {
                    Map<String, String> m = new HashMap<>();
                    m.put(ss[1], ss[2]);
                    map.put(ss[0], m);
                }
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //载入初始矩阵
    public static Map<String, String> get_initial_vector() {
        try {
            Map<String, String> map = new HashMap<>();
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("initial_vector.txt")));
            String str;
            while ((str = bf.readLine()) != null) {
                String[] ss = str.split(" ");
                map.put(ss[0], ss[2]);
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    //载入转移概率矩阵,key是首状态，value是到其它状态的概率的字典
    public static Map<String, Map<String, String>> get_transition_probability() {
        try {
            Map<String, Map<String, String>> map = new HashMap<>();
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("transition_probability.txt")));
            String str;
            while ((str = bf.readLine()) != null) {
                String[] ss = str.split(" ");
                if (map.containsKey(ss[0]))
                    map.get(ss[0]).put(ss[1], ss[2]);
                else {
                    Map<String, String> m = new HashMap<>();
                    m.put(ss[1], ss[2]);
                    map.put(ss[0], m);
                }

            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    //载入模式
    public static List<String> get_pattern_list() {
        try {
            List<String> pattern = new ArrayList<>();
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream("nt.pattern.txt")));
            String str;
            while ((str = bf.readLine()) != null) {
                pattern.add(str);
            }
            return pattern;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}